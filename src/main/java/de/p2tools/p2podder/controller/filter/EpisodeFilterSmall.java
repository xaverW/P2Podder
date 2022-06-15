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

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;

import java.util.function.Predicate;

public class EpisodeFilterSmall extends EpisodeFilterSmallProps {

    public EpisodeFilterSmall(boolean init) {
        if (init) {
            initFilter();
        }
    }

    public void clearFilter() {
        // alle Filter löschen
        setPodcastId(0);
    }

    private void initFilter() {
        podcastIdProperty().addListener(l -> setPredicate());

        isAllProperty().addListener(l -> setPredicate());
        isNewProperty().addListener(l -> setPredicate());
        isStartedProperty().addListener(l -> setPredicate());
        isRunningProperty().addListener(l -> setPredicate());
        wasShownProperty().addListener(l -> setPredicate());
    }

    public void setPredicate() {
        //setzen und merken :)
        ProgData.getInstance().episodeList.smallFilteredListSetPred(getPredicateEpisode());
    }

    private Predicate<Episode> getPredicateEpisode() {
        Predicate<Episode> predicate = episode -> true;

        if (getPodcastId() > 0) {
            predicate = predicate.and(episode -> episode.podcastIdProperty().getValue().equals(getPodcastId()));
        }
        if (isIsNew()) {
            predicate = predicate.and(episode -> episode.isNew());
        }
        if (isIsStarted()) {
            predicate = predicate.and(episode -> EpisodeFactory.episodeIsStarted(episode));
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
}
