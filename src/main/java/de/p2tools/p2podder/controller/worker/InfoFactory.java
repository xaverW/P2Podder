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

import java.text.NumberFormat;
import java.util.Locale;

public class InfoFactory {

    private static final String SEPARATOR = "   |   ";
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    private static final ProgData progData = ProgData.getInstance();

    private InfoFactory() {
    }

    public static String getInfosEpisode() {
        String textLinks;
        final int sumEpisodeList = progData.episodeList.size();
        final int sumEpisodesShown = progData.episodeGui.getEpisodeGuiController().getEpisodesShown();
        String sumEpisodeListStr = numberFormat.format(sumEpisodeList);
        String sumEpisodeShownStr = numberFormat.format(sumEpisodesShown);

        // Anzahl der Episoden
        if (sumEpisodesShown == 1) {
            textLinks = "1 Episode";
        } else {
            textLinks = sumEpisodeShownStr + " Episoden";
        }
        if (sumEpisodeList != sumEpisodesShown) {
            textLinks += " (Insgesamt: " + sumEpisodeListStr + ")";
        }

        if (progData.episodeInfos.getAmount() > 0) {
            //nur wenn eine Episode läuft
            textLinks += getEpisodeRunningInfos();
        }

        return textLinks;
    }

    public static synchronized String getInfosPodcasts() {
        String textLinks;
        final int sumPodcastsList = progData.podcastList.size();
        final int sumPodcastsShown = progData.podcastGui.getPodcastGuiController().getPodcastShown();
        final String sumPodcastListStr = numberFormat.format(sumPodcastsList);
        final String sumPodcastShownStr = numberFormat.format(sumPodcastsShown);

        // Anzahl der Podcasts
        if (sumPodcastsShown == 1) {
            textLinks = "1 Podcast";
        } else {
            textLinks = sumPodcastShownStr + " Podcasts";
        }
        if (sumPodcastsList != sumPodcastsShown) {
            textLinks += " (Insgesamt: " + sumPodcastListStr + " )";
        }

        // auch die Episoden anzeigen
        textLinks += addEpisodeInfos();

        return textLinks;
    }

    public static String getInfosDownloads() {
        String textLinks;
        final int sumDownloadsList = progData.downloadList.size();
        final int sumDownloadsShown = progData.downloadGui.getDownloadGuiController().getDownloadsShown();
        final long running = DownloadListFactory.countRunningDownloads();
        final String sumDownloadListStr = numberFormat.format(sumDownloadsList);
        final String sumDownloadShownStr = numberFormat.format(sumDownloadsShown);

        // Anzahl der Downloads
        if (sumDownloadsShown == 1) {
            textLinks = "1 Download";
        } else {
            textLinks = sumDownloadShownStr + " Downloads";
        }
        if (sumDownloadsList != sumDownloadsShown) {
            textLinks += " (Insgesamt: " + sumDownloadListStr + " )";
        }

        // laufende Programme
        if (running == 1) {
            textLinks += SEPARATOR;
            textLinks += (running + " gestarteter Download");
        } else if (running > 1) {
            textLinks += SEPARATOR;
            textLinks += (running + " gestartete Downloads");
        }

        // auch die Episoden anzeigen
        textLinks += addEpisodeInfos();
        return textLinks;
    }

    private static String addEpisodeInfos() {
        String txt = "";
        if (progData.episodeInfos.getAmount() > 0) {
            txt += SEPARATOR;

            // Anzahl der Episoden
            if (progData.episodeInfos.getAmount() == 1) {
                txt += "1 Episode";
            } else {
                txt += progData.episodeInfos.getAmount() + " Episoden";
            }
            //und noch die laufenden Episoden
            txt += getEpisodeRunningInfos();
        }
        return txt;
    }

    private static synchronized String getEpisodeRunningInfos() {
        String textLinks = SEPARATOR;
        int running = 0;
        running = progData.episodeInfos.getStarted();
        if (running == 0) {
            textLinks = "";

        } else if (running == 1) {
            textLinks += "1 Episode gestartet";

        } else {
            textLinks += running + " Episoden gestartet";
        }

        return textLinks;
    }
}
