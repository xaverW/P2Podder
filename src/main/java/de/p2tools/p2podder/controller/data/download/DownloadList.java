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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
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

public class DownloadList extends SimpleListProperty<DownloadData> implements P2DataList<DownloadData> {

    public static final String TAG = "downloadList" + P2Data.TAGGER + "DownloadList";
    private final ProgData progData;
    private int no = 0;
    private boolean found = false;
    private final FilteredList<DownloadData> filteredList;
    private final SortedList<DownloadData> sortedList;

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
    public DownloadData getNewItem() {
        return new DownloadData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(DownloadData.class)) {
            DownloadData d = (DownloadData) obj;
            add(d);
        }
    }

    public synchronized void addNewDownloads(ArrayList<DownloadData> list) {
        DownloadListFactory.addNewDownloads(list);
    }

    public synchronized void initList() {
        // bei Downloads nach einem Programmstart
        // den Podcast wieder eintragen
        P2Duration.counterStart("initList");
        for (DownloadData download : this) {
            download.setPodcast(progData.podcastList.getPodcastById(download.getPodcastId()));
            download.setGuiState(download.getState());
        }
        genGenreList();
        this.addListener((v, o, n) -> genGenreList());
        P2Duration.counterStop("initList");
    }

    public SortedList<DownloadData> getSortedList() {
        return sortedList;
    }

    public synchronized void filteredListSetPred(Predicate<DownloadData> predicate) {
        filteredList.setPredicate(predicate);
    }

    public void sort() {
        Collections.sort(this);
    }

    @Override
    public synchronized boolean add(DownloadData d) {
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends DownloadData> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(DownloadData... var1) {
        for (DownloadData f : var1) {
            f.setNo(++no);
        }
        return super.addAll(var1);
    }

    public synchronized boolean remove(DownloadData objects) {
        return super.remove(objects);
    }

    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized void putBackDownloads(DownloadData download) {
        ArrayList<DownloadData> list = new ArrayList<>();
        list.add(download);
        if (DownloadListStartStopFactory.putBackDownloads(list)) {
            setDownloadsChanged();
        }
    }

    public void startDownloads(Collection<DownloadData> list, boolean alsoFinished) {
        if (DownloadListStartStopFactory.startDownloads(list, alsoFinished)) {
            setDownloadsChanged();
        }
    }

    public void startDownloads(DownloadData download) {
        DownloadListStartStopFactory.startDownloads(download);
        setDownloadsChanged();
    }

    public void stopAllDownloads() {
        DownloadListStartStopFactory.stopDownloads();
        setDownloadsChanged();
    }

    public synchronized void addNumber(ArrayList<DownloadData> downloads) {
        int i = getNextNumber();
        for (DownloadData download : downloads) {
            download.setNo(i++);
        }
    }

    public int getNextNumber() {
        int i = 1;
        for (final DownloadData download : this) {
            if (download.getNo() < P2LibConst.NUMBER_NOT_STARTED && download.getNo() >= i) {
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
        P2Log.sysLog("DownloadList: genGenreList");
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
