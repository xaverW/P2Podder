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

package de.p2tools.p2podder.controller.data.podcast;

import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public class PodcastList extends SimpleListProperty<Podcast> implements P2DataList<Podcast> {

    {
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
    }

    public static final String TAG = "podcastList" + P2Data.TAGGER + "PodcastList";

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

    public int nr = 1;
    public String[] genres = {""};

    private FilteredList<Podcast> filteredList = null;
    private SortedList<Podcast> sortedList = null;
    private ObservableList<String> genreList = FXCollections.observableArrayList();

    public PodcastList() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Podcasts";
    }

    @Override
    public Podcast getNewItem() {
        return new Podcast();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Podcast.class)) {
            importPodcastOnlyWithNo((Podcast) obj);
        }
    }

    public void addNewItem(List<Podcast> podcastList) {
        for (Podcast p : podcastList) {
            importPodcastOnlyWithNo(p);
        }
    }

    public synchronized void initList() {
        P2Duration.counterStart("initList");
        createFilterLists();
        genGenreList();
        this.addListener((v, o, n) -> genGenreList());
        P2Duration.counterStop("initList");
    }

    public boolean podcastExistsAlready(Podcast podcast) {
        // true wenn es ihn schon gibt
        for (final Podcast pod : this) {
            //todo
//            if (FilmFilter.aboExistsAlready(pod, podcast)) {
//                return true;
//            }
        }
        return false;
    }

    public synchronized SortedList<Podcast> getSortedList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    public synchronized FilteredList<Podcast> getFilteredList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<Podcast>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return filteredList;
    }

    public synchronized void filteredListSetPred(Predicate<Podcast> predicate) {
        filteredList.setPredicate(predicate);
    }

    public synchronized boolean importPodcastOnlyWithNo(Podcast podcast) {
        //hier nur beim Laden aus einer fertigen Podcastliste mit der GUI
        //die Podcasts sind schon sortiert, nur die Nummer muss noch ergänzt werden
        podcast.setNo(nr++);
        return super.add(podcast);
    }

    int countDouble = 0;

    public synchronized int markStations() {
        // läuft direkt nach dem Laden der Liste!
        // doppelte Podcasts (URL), Geo, InFuture markieren

        final HashSet<String> urlHashSet = new HashSet<>(size(), 0.75F);

        // todo exception parallel?? Unterschied ~10ms (bei Gesamt: 110ms)
        P2Duration.counterStart("Podcast markieren");
        try {
            countDouble = 0;
            this.stream().forEach(station -> {
                if (!urlHashSet.add(station.getUrl())) {
                    ++countDouble;
                }
            });

        } catch (Exception ex) {
            P2Log.errorLog(951024789, ex);
        }
        P2Duration.counterStop("Podcast markieren");

        urlHashSet.clear();
        return countDouble;
    }

    @Override
    public synchronized void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized void sort() {
        Collections.sort(this);
        // und jetzt noch die Nummerierung in Ordnung bringen
        int i = 1;
        for (final Podcast podcast : this) {
            podcast.setNo(i++);
        }
    }

    public synchronized Podcast getPodcastById(final Long id) {
        final Optional<Podcast> opt =
                stream().filter(station -> station.getId() == id).findAny();
        return opt.orElse(null);
    }

    public synchronized boolean removePodcast(Podcast podcast) {
        return this.remove(podcast);
    }

    public synchronized boolean removePodcast(List<Podcast> podcast) {
        return this.removeAll(podcast);
    }

    public synchronized void removeAllPodcast() {
        this.clear();
    }

    /**
     * Erstellt StringArrays der Codecs/Länder eines Podcasts.
     */
    public synchronized void createFilterLists() {
        P2Duration.counterStart("Filter-Listen suchen");

        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(21);
        //Genres
        hashSet.add(""); //der erste ist ""
        stream().forEach((podcast) -> {
            String[] genreArr = podcast.getGenre().split(",");
            for (String s : genreArr) {
                hashSet.add(s);
            }
        });
        genres = hashSet.toArray(new String[hashSet.size()]);

        P2Duration.counterStop("Filter-Listen suchen");
    }

    public synchronized ObservableList<String> getGenreList() {
        return genreList;
    }

    private synchronized void genGenreList() {
        P2Log.sysLog("PodcastList: genGenreList");
        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(10);
        ArrayList<String> arrayList = new ArrayList<>();
        stream().forEach((episode) -> {
            String podcastName = episode.getGenre();
            if (!hashSet.contains(podcastName)) {
                hashSet.add(podcastName);
                arrayList.add(podcastName);
            }
        });
        genreList.setAll(arrayList);
    }
}
