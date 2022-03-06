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

package de.p2tools.p2podder.controller.data.episode;

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.function.Predicate;

public class EpisodeList extends SimpleListProperty<Episode> implements PDataList<Episode> {

    public static final String TAG = "EpisodeList";
    private final ProgData progData;
    private final EpisodeStartsFactory episodeStartsFactory;
    private int no = 0;

    private final FilteredList<Episode> filteredList;
    private final SortedList<Episode> episodeSortedList;

    private BooleanProperty episodeChanged = new SimpleBooleanProperty(true);
    private ObservableList<String> genreList = FXCollections.observableArrayList();

    public EpisodeList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
        this.episodeStartsFactory = new EpisodeStartsFactory(progData, this);

        this.filteredList = new FilteredList<>(this, p -> true);
        this.episodeSortedList = new SortedList<>(filteredList);
        progData.storedFilters.filterChangeProperty().addListener((observable, oldValue, newValue) -> filter()); // Filmfilter (User) haben sich geändert
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Episoden";
    }

    @Override
    public Episode getNewItem() {
        return new Episode();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Episode.class)) {
            add((Episode) obj);
        }
    }

    public synchronized void initList() {
        PDuration.counterStart("initList");
        for (Episode episode : this) {
            episode.setThePodcast(progData.podcastList.getPodcastById(episode.getPodcastId()));
        }
        countEpisodes();
        genGenreList();
        this.addListener((v, o, n) -> genGenreList());
        PDuration.counterStop("initList");
    }

    public FilteredList<Episode> getFilteredList() {
        return filteredList;
    }

    public SortedList<Episode> getEpisodeSortedList() {
        return episodeSortedList;
    }

    public synchronized void filteredListSetPred(Predicate<Episode> predicate) {
        filteredList.setPredicate(predicate);
    }

    public void sort() {
        Collections.sort(this);
    }

    public void filter() {
        filteredList.setPredicate(progData.storedFilters.getActFilterSettings().getPredicateEpisode());
    }

    @Override
    public synchronized boolean add(Episode d) {
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends Episode> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(Episode... var1) {
        for (Episode f : var1) {
            f.setNo(++no);
        }
        return super.addAll(var1);
    }

    public synchronized boolean remove(Episode objects) {
        return super.remove(objects);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized void countEpisodes() {
        progData.podcastList.stream().forEach(p -> {
            p.setNumber(0);
        });
        this.stream().forEach(e -> {
            Podcast p = progData.podcastList.getPodcastById(e.getPodcastId());
            p.setNumber(p.getNumber() + 1);
        });
    }

    public synchronized int countStartedAndRunningEpisode() {
        //es wird nach gestarteten und laufenden Favoriten gesucht
        int ret = 0;
        for (final Episode episode : this) {
            if (episode.getStart() != null &&
                    (episode.getStart().getStartStatus().isStarted() || episode.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

    public Episode getNextStart() {
        if (this.isEmpty()) {
            return null;
        }
        return remove(0);
    }

    public synchronized Episode getUrlStation(String urlStation) {
        for (final Episode episode : this) {
            if (episode.getEpisodeUrl().equals(urlStation)) {
                return episode;
            }
        }
        return null;
    }

    public synchronized List<Episode> getListOfStartsNotFinished(String source) {
        return episodeStartsFactory.getListOfStartsNotFinished(source);
    }

    public synchronized List<Podcast> getPodcastList() {
        PDuration.counterStart("getPodcastList");

        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(10);
        ArrayList<Podcast> arrayList = new ArrayList<>();
        stream().forEach((episode) -> {
            String podcastName = episode.getPodcastName();
            Podcast podcast = progData.podcastList.getPodcastById(episode.getPodcastId());
            if (podcast != null && !hashSet.contains(podcastName)) {
                hashSet.add(podcastName);
                arrayList.add(podcast);
            }
        });

        PDuration.counterStop("getPodcastList");
        return arrayList;
    }

    public synchronized ObservableList<String> getGenreList() {
        return genreList;
    }

    public synchronized void genGenreList() {
        PDuration.counterStart("genGenreList");
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
        PDuration.counterStop("genGenreList");
    }
}
