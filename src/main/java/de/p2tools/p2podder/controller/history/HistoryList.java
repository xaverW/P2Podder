/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2podder.controller.history;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public class HistoryList extends SimpleListProperty<HistoryData> {

    private final HashSet<String> urlHash = new HashSet<>();
    private final String fileName;
    private FilteredList<HistoryData> filteredList = null;
    private SortedList<HistoryData> sortedList = null;
    private BooleanProperty isWorking = new SimpleBooleanProperty(false);
    public final HistoryWorker historyWorker;

    public HistoryList(String fileName, String settingsDir) {
        super(FXCollections.observableArrayList());

        this.fileName = fileName;
        this.historyWorker = new HistoryWorker(fileName, settingsDir);

        historyWorker.readHistoryDataFromFile(this);
        fillUrlHash();
    }

    public SortedList<HistoryData> getSortedList() {
        filteredList = getFilteredList();
        if (sortedList == null) {
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    public FilteredList<HistoryData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filteredListSetPredicate(Predicate<HistoryData> predicate) {
        filteredList.setPredicate(predicate);
    }

    public synchronized void filterdListSetPredFalse() {
        filteredList.setPredicate(p -> false);
    }

    public synchronized void filterdListSetPredTrue() {
        filteredList.setPredicate(p -> true);
    }

    public synchronized void clearAll(Stage stage) {
        final int size = this.size();
        final String title = "Filme";

        if (size <= 1 || P2Alert.showAlertOkCancel(stage, "Löschen", title + " löschen",
                "Soll die gesamte Liste " +
                        "(" + size + " " + title + ")" +
                        " gelöscht werden?")) {
            clearList();
            historyWorker.deleteHistoryFile();
        }
    }

    public synchronized boolean checkIfUrlAlreadyIn(String urlFilm) {
        // wenn url gefunden, dann true zurück
        return urlHash.contains(urlFilm);
    }

    public synchronized void addHistoryDataToHistory(DownloadData download) {
        addHistoryDataToHistory(download.getGenre(), download.getEpisodeTitle(), download.getEpisodeUrl());
    }

    public synchronized void addHistoryDataToHistory(Episode episode) {
        addHistoryDataToHistory(episode.getGenre(), episode.getEpisodeTitle(), episode.getEpisodeUrl());
    }

    private synchronized void addHistoryDataToHistory(String theme, String title, String url) {
        // einen Film in die History schreiben
        if (checkIfUrlAlreadyIn(url)/* || checkIfLiveStream(theme)*/) {
            return;
        }

        P2Duration.counterStart("History: addDataToHistory");
        final ArrayList<HistoryData> list = new ArrayList<>();
        final String datum = P2DateConst.F_FORMAT_dd_MM_yyyy.format(new Date());
        HistoryData historyData = new HistoryData(datum, theme, title, url);
        addToThisList(historyData);
        list.add(historyData);

        writeToFile(list, true);
        P2Duration.counterStop("History: addDataToHistory");
    }

    public synchronized void addDownloadDataListToHistory(ArrayList<DownloadData> downloadList) {
        // eine Liste Downloads in die Hitory schreiben
        if (downloadList == null || downloadList.isEmpty()) {
            return;
        }

        final ArrayList<HistoryData> list = new ArrayList<>(downloadList.size());
        final String datum = P2DateConst.F_FORMAT_dd_MM_yyyy.format(new Date());

        P2Duration.counterStart("History: addDataToHistory");
        for (final DownloadData download : downloadList) {
            if (checkIfUrlAlreadyIn(download.getEpisodeUrl())) {
                continue;
            }

            HistoryData historyData = new HistoryData(datum, download.getGenre(), download.getEpisodeTitle(), download.getEpisodeUrl());
            addToThisList(historyData);
            list.add(historyData);
        }

        writeToFile(list, true);
        P2Duration.counterStop("History: addDataToHistory");
    }


    private void writeToFile(List<HistoryData> list, boolean append) {
        waitWhileWorkingAndSetWorking();

        try {
            Thread th = new Thread(new HistoryWriteToFile(list, append, isWorking, historyWorker));
            th.setName("HistoryWriteToFile");
            th.start();
            // th.run();
        } catch (Exception ex) {
            P2Log.errorLog(912030254, ex, "writeToFile");
            isWorking.setValue(false);
        }
    }

    private void waitWhileWorking() {
        while (isWorking.get()) {
            // sollte nicht passieren, aber wenn ..
            P2Log.errorLog(741025896, "waitWhileWorking: write to history file");

            try {
                wait(100);
            } catch (final Exception ex) {
                P2Log.errorLog(915236547, ex, "waitWhileWorking");
                isWorking.setValue(false);
            }
        }

    }

    private void waitWhileWorkingAndSetWorking() {
        waitWhileWorking();
        isWorking.setValue(true);
    }

    public synchronized void removeHistoryDataFromHistory(ArrayList<HistoryData> historyDataList) {
        // Historydaten aus der History löschen und File wieder schreiben
        if (historyDataList == null || historyDataList.isEmpty()) {
            return;
        }

        P2Duration.counterStart("History: removeDataFromHistory");
        final HashSet<String> hash = new HashSet<>(historyDataList.size() + 1, 0.75F);
        for (HistoryData historyData : historyDataList) {
            hash.add(historyData.getUrl());
        }

        removeFromHistory(hash);
        P2Duration.counterStop("History: removeDataFromHistory");
    }

    public synchronized void removeFilmDataFromHistory(ArrayList<DownloadData> filmList) {
        // eine Liste Filme aus der History löschen und File wieder schreiben

        if (filmList == null || filmList.isEmpty()) {
            return;
        }

        P2Duration.counterStart("History: removeDataFromHistory");
        final HashSet<String> hash = new HashSet<>(filmList.size() + 1, 0.75F);
        filmList.stream().forEach(film -> {
            hash.add(film.getEpisodeUrl());
        });

        removeFromHistory(hash);
        P2Duration.counterStop("History: removeDataFromHistory");
    }

    public synchronized void removeDownloadDataFromHistory(ArrayList<DownloadData> downloadList) {
        // eine Liste Downloads aus der History löschen und File wieder schreiben

        if (downloadList == null || downloadList.isEmpty()) {
            return;
        }

        P2Duration.counterStart("History: removeDataFromHistory");
        final HashSet<String> hash = new HashSet<>(downloadList.size() + 1, 0.75F);
        downloadList.stream().forEach(download -> {
            hash.add(download.getEpisodeUrl());
        });

        removeFromHistory(hash);
        P2Duration.counterStop("History: removeDataFromHistory");
    }

    private boolean found = false;

    private void removeFromHistory(HashSet<String> urlHash) {
        final ArrayList<HistoryData> newHistoryList = new ArrayList<>();

        found = false;

        P2Duration.counterStart("History: removeFromHistory");
        P2Log.sysLog("Aus Historyliste löschen: " + urlHash.size() + ", löschen aus: " + fileName);

        waitWhileWorking(); // wird diese Liste abgesucht

        this.stream().forEach(historyData -> {

            if (urlHash.contains(historyData.getUrl())) {
                // nur dann muss das Logfile auch geschrieben werden
                found = true;
            } else {
                // kommt wieder in die history
                newHistoryList.add(historyData);
            }

        });

        if (found) {
            // und nur dann wurde was gelöscht und muss geschreiben werden
            replaceThisList(newHistoryList);
            writeToFile(newHistoryList, false);
        }

        P2Duration.counterStop("History: removeFromHistory");
    }

    private void clearList() {
        urlHash.clear();
        this.clear();
    }

    private void addToThisList(HistoryData historyData) {
        this.add(historyData);
        urlHash.add(historyData.getUrl());
    }

    private void replaceThisList(List<HistoryData> historyData) {
        clearList();
        this.addAll(historyData);
        fillUrlHash();
    }

    private void fillUrlHash() {
        urlHash.clear();
        this.stream().forEach(h -> urlHash.add(h.getUrl()));
    }

}
