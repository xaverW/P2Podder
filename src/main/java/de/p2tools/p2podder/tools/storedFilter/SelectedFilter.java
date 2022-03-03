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

package de.p2tools.p2podder.tools.storedFilter;

import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.tools.podcastListFilter.PodcastFilterFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Predicate;

public final class SelectedFilter extends SelectedFilterProps {

    private final BooleanProperty filterChange = new SimpleBooleanProperty(false);
    private boolean reportChange = true;

    public SelectedFilter() {
        initFilter();
        setName("Filter");
    }

    public SelectedFilter(String name) {
        initFilter();
        setName(name);
    }

    public boolean isReportChange() {
        return reportChange;
    }

    public void setReportChange(boolean reportChange) {
        this.reportChange = reportChange;
    }

    public BooleanProperty filterChangeProperty() {
        return filterChange;
    }

    public void setNameAndVis(String set) {
        setTitle(set);
        setTitleVis(true);
    }

    public void setGenreAndVis(String set) {
        setGenre(set);
        setGenreVis(true);
    }

    public void setUrlAndVis(String set) {
        setUrl(set);
        setUrlVis(true);
    }

    public void initFilter() {
        clearFilter();

        setTitleVis(true);
        setGenreVis(true);
        setUrlVis(false);

        titleVisProperty().addListener(l -> reportFilterChange());
        titleProperty().addListener(l -> reportFilterChange());

        genreVisProperty().addListener(l -> reportFilterChange());
        genreProperty().addListener(l -> reportFilterChange());

        urlVisProperty().addListener(l -> reportFilterChange());
        urlProperty().addListener(l -> reportFilterChange());
    }

    private void reportFilterChange() {
        if (reportChange) {
            filterChange.setValue(!filterChange.getValue());
        }
    }

    public void clearFilter() {
        // alle Filter löschen, Button Black bleibt wie er ist
        setTitle("");
        setGenre("");
        setUrl("");
    }

    public boolean isTextFilterEmpty() {
        return getTitle().isEmpty() &&
                getGenre().isEmpty() &&
                getUrl().isEmpty();
    }


    public boolean clearTxtFilter() {
        boolean ret = false;
        if (!getTitle().isEmpty()) {
            ret = true;
            setTitle("");
        }
        if (!getGenre().isEmpty()) {
            ret = true;
            setGenre("");
        }
        if (!getUrl().isEmpty()) {
            ret = true;
            setUrl("");
        }
        return ret;
    }

    public Predicate<Podcast> getPredicatePodcast() {
        SelectedFilter selectedFilter = this;

        Filter fStationName;
        Filter fGenre;
        Filter fUrl;

        String filterStationName = selectedFilter.getTitleVis() ? selectedFilter.getTitle() : "";
        String filterGenre = selectedFilter.isGenreVis() ? selectedFilter.getGenre() : "";
        String filterUrl = selectedFilter.isUrlVis() ? selectedFilter.getUrl() : "";

        fStationName = new Filter(filterStationName, false, true);
        fGenre = new Filter(filterGenre, true);
        fUrl = new Filter(filterUrl, false); // gibt URLs mit ",", das also nicht trennen

        Predicate<Podcast> predicate = station -> true;

        if (!fStationName.empty) {
            predicate = predicate.and(station -> PodcastFilterFactory.checkPodcastName(fStationName, station));
        }

        if (!fGenre.empty) {
            predicate = predicate.and(station -> PodcastFilterFactory.checkPodcastGenre(fGenre, station));
        }

        if (!fUrl.empty) {
            predicate = predicate.and(station -> PodcastFilterFactory.checkPodcastUrl(fUrl, station));
        }

        return predicate;
    }

    public Predicate<Download> getPredicateDownload() {
        SelectedFilter selectedFilter = this;

        Filter fStationName;
        Filter fGenre;

        String filterStationName = selectedFilter.getTitleVis() ? selectedFilter.getTitle() : "";
        String filterGenre = selectedFilter.isGenreVis() ? selectedFilter.getGenre() : "";
        if (filterGenre == null) {
            filterGenre = "";
        }

        fStationName = new Filter(filterStationName, false, true);
        fGenre = new Filter(filterGenre, true);

        Predicate<Download> predicate = station -> true;

        if (!fStationName.empty) {
            predicate = predicate.and(station -> PodcastFilterFactory.checkDownloadEpisodeTitel(fStationName, station));
        }

        if (!fGenre.empty) {
            predicate = predicate.and(station -> PodcastFilterFactory.checkDownloadGenre(fGenre, station));
        }

        return predicate;
    }
}
