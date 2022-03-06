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

package de.p2tools.p2podder.gui.filter;

import de.p2tools.p2Lib.tools.log.PDebugLog;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Locale;
import java.util.function.Predicate;

public class SelectedFilter extends SelectedFilterProps {

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

    public void initFilter() {
        clearFilter();
        podcastIdProperty().addListener(l -> reportFilterChange());
        genreProperty().addListener(l -> reportFilterChange());
        titleProperty().addListener(l -> reportFilterChange());
        timeRangeProperty().addListener(l -> reportFilterChange());
        isNewProperty().addListener(l -> reportFilterChange());
        isRunningProperty().addListener(l -> reportFilterChange());
        wasShownProperty().addListener(l -> reportFilterChange());
    }

    private void reportFilterChange() {
        if (reportChange) {
            PDebugLog.sysLog("reportFilterChange");
            filterChange.setValue(!filterChange.getValue());
        }
    }

    public void clearFilter() {
        // alle Filter löschen, Button Black bleibt wie er ist
        setPodcastId(0);
        setGenre("");
        setTitle("");
        setTimeRange(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
        setIsNew(false);
        setIsRunning(false);
        setIsNew(false);
    }

    public boolean isTextFilterEmpty() {
        return getGenre().isEmpty() &&
                getTitle().isEmpty();
    }


    public boolean clearTxtFilter() {
        boolean ret = false;
        if (!getGenre().isEmpty()) {
            ret = true;
            setGenre("");
        }
        if (!getTitle().isEmpty()) {
            ret = true;
            setTitle("");
        }
        return ret;
    }

    public Predicate<Episode> getPredicateEpisode() {
        Predicate<Episode> predicate = episode -> true;

        if (getPodcastId() > 0) {
            predicate = predicate.and(episode -> episode.podcastIdProperty().getValue().equals(getPodcastId()));
        }
        if (!getGenre().isEmpty()) {
            predicate = predicate.and(episode -> episode.genreProperty().getValue().equals(getGenre()));
        }
        if (!getTitle().isEmpty()) {
            predicate = predicate.and(episode ->
                    episode.getEpisodeTitle().toLowerCase(Locale.ROOT).contains(getTitle()));
        }


        if (getTimeRange() != ProgConst.FILTER_TIME_RANGE_ALL_VALUE) {
            predicate = predicate.and(episode -> episode.checkDays(getTimeRange()));
        }
        if (isIsNew()) {
            predicate = predicate.and(episode -> episode.isNew());
        }
        if (isIsRunning()) {
            predicate = predicate.and(episode -> EpisodeFactory.episodeIsRunning(episode));
        }
        if (isWasShown()) {
            predicate = predicate.and(episode -> ProgData.getInstance().historyEpisodes.
                    checkIfUrlAlreadyIn(episode.getEpisodeUrl()));
        }
        return predicate;
    }

    public void setAll() {
        setIsNew(false);
        setIsRunning(false);
        setWasShown(false);
    }

    public void setNew() {
        setIsNew(true);
        setIsRunning(false);
        setWasShown(false);
    }

    public void setRunning() {
        setIsNew(false);
        setIsRunning(true);
        setWasShown(false);
    }

    public void setWasShown() {
        setIsNew(false);
        setIsRunning(false);
        setWasShown(true);
    }
}
