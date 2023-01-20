/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

public class EpisodeFilterFactory {

    public static EpisodeFilter copyFilter(EpisodeFilter sfFrom, EpisodeFilter sfTo) {
        sfTo.setPodcastId(sfFrom.getPodcastId());
        sfTo.setGenre(sfFrom.getGenre());
        sfTo.setTitle(sfFrom.getTitle());
        sfTo.setDescription(sfFrom.getDescription());
        sfTo.setTimeRange(sfFrom.getTimeRange());
        return sfTo;
    }

    public static boolean compareFilter(EpisodeFilter sfFrom, EpisodeFilter sfTo) {
        if (sfFrom == null || sfTo == null) {
            return false;
        }

        for (int i = 0; i < sfFrom.sfIntegerPropArr.length; i++) {
            if (sfFrom.sfIntegerPropArr[i].get() != sfTo.sfIntegerPropArr[i].get()) {
                return false;
            }
        }
        for (int i = 0; i < sfFrom.sfLongPropArr.length; i++) {
            if (sfFrom.sfLongPropArr[i].get() != sfTo.sfLongPropArr[i].get()) {
                return false;
            }
        }
        for (int i = 0; i < sfFrom.sfStringPropArr.length; i++) {
            if (!sfFrom.sfStringPropArr[i].getValueSafe().equals(sfTo.sfStringPropArr[i].getValueSafe())) {
                return false;
            }
        }
        return true;
    }
}
