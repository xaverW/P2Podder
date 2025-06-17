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

package de.p2tools.p2podder.controller.filter;

import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;

import java.util.function.Predicate;

public class EpisodeFilter extends EpisodeFilterProps {
    private EpisodeFilterForwardBackward episodeFilterForwardBackward = null; // gespeicherte Filterprofile

    public EpisodeFilter() {
        new EpisodeFilter(false);
    }

    public EpisodeFilter(boolean init) {
        if (init) {
            episodeFilterForwardBackward = new EpisodeFilterForwardBackward(this);
            initFilter();
        }
    }

    public void clearFilter() {
        // alle Filter löschen
        setPodcastId(0);
        setGenre("");
        setTitle("");
        setDescription("");
        setTimeRange(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
        setDurationMin(ProgConst.FILTER_DURATION_MIN_VALUE);
        setDurationMax(ProgConst.FILTER_DURATION_MAX_MINUTE);
    }

    public EpisodeFilterForwardBackward getEpisodeFilterForwardBackward() {
        return episodeFilterForwardBackward;
    }

    private void initFilter() {
        podcastIdProperty().addListener(l -> setPredicate());
        genreProperty().addListener(l -> setPredicate());
        titleProperty().addListener(l -> setPredicate());
        descriptionProperty().addListener(l -> setPredicate());
        timeRangeProperty().addListener(l -> setPredicate());
        durationMinProperty().addListener(l -> setPredicate());
        durationMaxProperty().addListener(l -> setPredicate());

        isAllProperty().addListener(l -> setPredicate());
        isNewProperty().addListener(l -> setPredicate());
        isStartetProperty().addListener(l -> setPredicate());
        isRunningProperty().addListener(l -> setPredicate());
        wasShownProperty().addListener(l -> setPredicate());
    }

    private void setPredicate() {
        setPredicate(true);
    }

    public void setPredicate(boolean setInForwardBackward) {
        //setzen und merken :)
        if (setInForwardBackward) {
            episodeFilterForwardBackward.addNewFilter();
        }
        ProgData.getInstance().episodeList.filteredListSetPred(getPredicateEpisode());
    }

    private Predicate<Episode> getPredicateEpisode() {
        Predicate<Episode> predicate = episode -> true;

        Filter fTitle = new Filter(getTitle(), true);
        Filter fDescription = new Filter(getDescription(), true);

        if (getPodcastId() > 0) {
            predicate = predicate.and(episode -> episode.podcastIdProperty().getValue().equals(getPodcastId()));
        }

        if (getGenre() != null && !getGenre().isEmpty()) {
            predicate = predicate.and(episode -> episode.genreProperty().getValue().equals(getGenre()));
        }
        if (!fTitle.empty) {
            predicate = predicate.and(episode -> FilterCheck.checkString(fTitle, episode.getEpisodeTitle()));
        }
        if (!fDescription.empty) {
            predicate = predicate.and(episode -> FilterCheck.checkString(fDescription, episode.getDescription()));
        }
        if (getTimeRange() != ProgConst.FILTER_TIME_RANGE_ALL_VALUE) {
            predicate = predicate.and(episode -> episode.checkDays(getTimeRange()));
        }

        if (getDurationMin() != ProgConst.FILTER_DURATION_MIN_VALUE) {
            predicate = predicate.and(episode -> episode.checkDurationMin(getDurationMin()));
        }
        if (getDurationMax() != ProgConst.FILTER_DURATION_MAX_MINUTE) {
            predicate = predicate.and(episode -> episode.checkDurationMax(getDurationMax()));
        }

        if (isIsNew()) {
            predicate = predicate.and(Episode::isNew);
        }
        if (isIsStartet()) {
            predicate = predicate.and(EpisodeFactory::episodeIsStarted);
        }
        if (isIsRunning()) {
            predicate = predicate.and(EpisodeFactory::episodeIsRunning);
        }
        if (isWasShown()) {
            predicate = predicate.and(episode -> ProgData.getInstance().historyEpisodes.
                    checkIfUrlAlreadyIn(episode.getEpisodeUrl()));
        }
        return predicate;
    }
}
