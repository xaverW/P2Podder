/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.controller.data.download;

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

public class DownloadList extends SimpleListProperty<Download> implements PDataList<Download> {

    public static final String TAG = "DownloadList";
    private final ProgData progData;
    private int no = 0;
    private boolean found = false;
    private final FilteredList<Download> filteredList;
    private final SortedList<Download> sortedList;

    private BooleanProperty downloadsChanged = new SimpleBooleanProperty(true);
    private ObservableList<String> genreList = FXCollections.observableArrayList();

    public DownloadList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;

        this.filteredList = new FilteredList<>(this, p -> true);
        this.sortedList = new SortedList<>(filteredList);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Downloads";
    }

    @Override
    public Download getNewItem() {
        return new Download();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Download.class)) {
            Download d = (Download) obj;
            add(d);
//            if (d.getSize() > 0) {
//                d.getDownloadSize().setFileSize(d.getSize());
//            }
        }
    }

    public synchronized void addNewDownloads(ArrayList<Download> list) {
        DownloadListFactory.addNewDownloads(list);
    }

    public synchronized void initList() {
        // bei Downloads nach einem Programmstart
        // den Podcast wieder eintragen
        PDuration.counterStart("initList");
        for (Download download : this) {
            download.setPodcast(progData.podcastList.getPodcastById(download.getPodcastId()));
            download.setGuiState(download.getState());
        }
        genGenreList();
        this.addListener((v, o, n) -> genGenreList());
        PDuration.counterStop("initList");
    }

    public SortedList<Download> getSortedList() {
        return sortedList;
    }

    public synchronized void filteredListSetPred(Predicate<Download> predicate) {
        filteredList.setPredicate(predicate);
    }

    public void sort() {
        Collections.sort(this);
    }

    @Override
    public synchronized boolean add(Download d) {
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends Download> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(Download... var1) {
        for (Download f : var1) {
            f.setNo(++no);
        }
        return super.addAll(var1);
    }

    public synchronized boolean remove(Download objects) {
        return super.remove(objects);
    }

    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized void putBackDownloads(Download download) {
        ArrayList<Download> list = new ArrayList<>();
        list.add(download);
        if (DownloadListStartStopFactory.putBackDownloads(list)) {
            setDownloadsChanged();
        }
    }

    public void startDownloads(Collection<Download> list, boolean alsoFinished) {
        if (DownloadListStartStopFactory.startDownloads(list, alsoFinished)) {
            setDownloadsChanged();
        }
    }

    public void startDownloads(Download download) {
        DownloadListStartStopFactory.startDownloads(download);
        setDownloadsChanged();
    }

    public void stopAllDownloads() {
        DownloadListStartStopFactory.stopDownloads();
        setDownloadsChanged();
    }

    public synchronized void addNumber(ArrayList<Download> downloads) {
        int i = getNextNumber();
        for (Download download : downloads) {
            download.setNo(i++);
        }
    }

    public int getNextNumber() {
        int i = 1;
        for (final Download download : this) {
            if (download.getNo() < DownloadConstants.DOWNLOAD_NUMBER_NOT_STARTED && download.getNo() >= i) {
                i = download.getNo() + 1;
            }
        }
        return i;
    }

    synchronized void setDownloadsChanged() {
        downloadsChanged.set(!downloadsChanged.get());
    }

    public synchronized ObservableList<String> getGenreList() {
        return genreList;
    }

    public synchronized void genGenreList() {
        PLog.sysLog("DownloadList: genGenreList");
        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(10);
        ArrayList<String> arrayList = new ArrayList<>();
        stream().forEach((download) -> {
            String podcastName = download.getGenre();
            if (!hashSet.contains(podcastName)) {
                hashSet.add(podcastName);
                arrayList.add(podcastName);
            }
        });
        genreList.setAll(arrayList);
    }
}
