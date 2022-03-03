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

import de.p2tools.p2podder.controller.config.ProgData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EpisodeStartsFactory {
    private final ProgData progData;
    private final EpisodeList episodeList;

    public EpisodeStartsFactory(ProgData progData, EpisodeList episodeList) {
        this.progData = progData;
        this.episodeList = episodeList;
    }

    /**
     * Return a List of all loading but not yet finished stations.
     *
     * @param source Use QUELLE_XXX constants
     * @return A list with all station objects.
     */
    synchronized List<Episode> getListOfStartsNotFinished(String source) {
        final List<Episode> episodes = new ArrayList<>();

        episodes.addAll(episodeList.stream()
                .filter(episode -> episode.getStart() != null && episode.getStart().getStartStatus().isStateStartedRun())
                .filter(download -> source.equals(EpisodeConstants.ALL) /*|| download.getSource().equals(source)*/)
                .collect(Collectors.toList()));

        return episodes;
    }
}