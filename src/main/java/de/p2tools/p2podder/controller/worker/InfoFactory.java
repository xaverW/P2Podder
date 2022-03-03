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


package de.p2tools.p2podder.controller.worker;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadListFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;

import java.text.NumberFormat;
import java.util.Locale;

public class InfoFactory {

    private static final String SEPARATOR = "  ||  ";
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    private static final ProgData progData = ProgData.getInstance();

    private InfoFactory() {
    }

    public static String getInfosEpisode() {
        String textLinks;
        String sumEpisodeListStr = numberFormat.format(progData.episodeStoredList.size());
        String sumEpisodeShownStr = numberFormat.format(progData.episodeGui.getEpisodeGuiController().getEpisodesShown());

        // Anzahl der Episoden
        if (progData.episodeGui.getEpisodeGuiController().getEpisodesShown() == 1) {
            textLinks = "1 Episode";
        } else {
            textLinks = sumEpisodeShownStr + " Episoden";
        }

        // weitere Infos anzeigen
        if (progData.episodeStoredList.size() != progData.episodeGui.getEpisodeGuiController().getEpisodesShown()) {
            textLinks += " (Insgesamt: " + sumEpisodeListStr;
            textLinks += ")";
        }

        if (progData.episodeInfos.getAmount() > 0) {
            // nur wenn ein Favorite läuft, wartet, ..
            textLinks += getRunningInfos();
        }

        return textLinks;
    }

    public static synchronized String getInfosPodcasts() {
        String textLinks;
        final int sumPodcastsList = progData.podcastList.size();
        final int sumPodcastsShown = progData.podcastGui.getPodcastGuiController().getStationCount();
        final int runs = progData.episodeStoredList.getListOfStartsNotFinished(EpisodeConstants.SRC_BUTTON).size();

        String sumPodcastListStr = numberFormat.format(sumPodcastsShown);
        String sumPodcastShownStr = numberFormat.format(sumPodcastsList);

        // Anzahl der Podcasts
        textLinks = sumPodcastListStr + " Podcasts";
        if (sumPodcastsList != sumPodcastsShown) {
            textLinks += " (Insgesamt: " + sumPodcastShownStr + " )";
        }
        // laufende Programme
        if (runs == 1) {
            textLinks += SEPARATOR;
            textLinks += (runs + " laufende Podcast");
        } else if (runs > 1) {
            textLinks += SEPARATOR;
            textLinks += (runs + " laufende Podcasts");
        }

        // auch die Favoriten anzeigen
        if (progData.episodeInfos.getAmount() > 0) {
            textLinks += SEPARATOR;

            // Anzahl der Favoriten
            if (progData.episodeInfos.getAmount() == 1) {
                textLinks += "1 Episode";
            } else {
                textLinks += progData.episodeInfos.getAmount() + " Episoden";
            }
            textLinks += getRunningInfos();
        }

        return textLinks;
    }

    public static String getInfosDownloads() {
        String textLinks;
        String sumDownloadListStr = numberFormat.format(progData.downloadList.size());

        // Anzahl der Downloads
        if (progData.downloadList.getSize() == 1) {
            textLinks = "1 Download";
        } else {
            textLinks = sumDownloadListStr + " Downloads";
        }

        long running = DownloadListFactory.countRunningDownloads();
        if (running > 0) {
            textLinks += " (Laufende: " + running + " )";
        }

        return textLinks;
    }

    private static synchronized String getRunningInfos() {
        String textLinks = " || ";
        int running = 0;
        running = progData.episodeInfos.getStarted();
        running += progData.stationInfos.getStarted();
        if (running == 0) {
            textLinks = "";

        } else if (running == 1) {
            textLinks += "1 Episode läuft";

        } else {
            textLinks += running + " Episoden laufen";
        }

        return textLinks;
    }
}
