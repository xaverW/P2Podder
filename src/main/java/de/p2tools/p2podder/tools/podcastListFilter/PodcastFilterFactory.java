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

package de.p2tools.p2podder.tools.podcastListFilter;

import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.tools.storedFilter.Filter;

import java.util.regex.Pattern;

public class PodcastFilterFactory {

    public static boolean checkPodcastName(Filter podcastName, Podcast podcast) {
        if (podcastName.exact) {
            // da ist keine Form optimal?? aber so passt es zur Sortierung der Themenliste
            if (!podcastName.filter.equalsIgnoreCase(podcast.getName())) {
                return false;
            }
        } else {
            if (!check(podcastName, podcast.getName())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPodcastGenre(Filter filter, Podcast podcast) {
        // nur ein Suchbegriff muss passen
        if (filter.exact) {
            if (!filter.filter.equalsIgnoreCase(podcast.getGenre())) {
                return false;
            }
        } else {
            if (!check(filter, podcast.getGenre())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPodcastUrl(Filter url, Podcast podcast) {
        //todo evtl. noch urlResolved einfügen???
        if (!check(url, podcast.getWebsite())
                && !check(url, podcast.getUrl())) {
            return false;
        }
        return true;
    }

    public static boolean checkDownloadEpisodeTitel(Filter episodeTitle, Download download) {
        if (episodeTitle.exact) {
            // da ist keine Form optimal?? aber so passt es zur Sortierung der Themenliste
            if (!episodeTitle.filter.equalsIgnoreCase(download.getEpisodeTitle())) {
                return false;
            }
        } else {
            if (!check(episodeTitle, download.getEpisodeTitle())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDownloadGenre(Filter filter, Download download) {
        // nur ein Suchbegriff muss passen
        if (filter.exact) {
            if (!filter.filter.equalsIgnoreCase(download.getGenre())) {
                return false;
            }
        } else {
            if (!check(filter, download.getGenre())) {
                return false;
            }
        }
        return true;
    }

    private static boolean check(Filter filter, String im) {
        // wenn einer passt, dann ists gut
        if (filter.filterArr.length == 1) {
            return check(filter.filterArr[0], filter.pattern, im);
        }

        if (filter.filterAnd) {
            // Suchbegriffe müssen alle passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (!im.toLowerCase().contains(s)) {
                    return false;
                }
            }
            return true;

        } else {
            // nur ein Suchbegriff muss passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (im.toLowerCase().contains(s)) {
                    return true;
                }
            }
        }

        // nix wars
        return false;
    }

    private static boolean check(String filter, Pattern pattern, String im) {
        if (pattern != null) {
            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }
        if (im.toLowerCase().contains(filter)) {
            // wenn einer passt, dann ists gut
            return true;
        }

        // nix wars
        return false;
    }
}
