/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.controller.parser;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParserThread {
    private final ProgData progData;
    private Parser parser = new Parser();
    private boolean andStartDownload = false;

    public ParserThread(ProgData progData) {
        this.progData = progData;
        ProgConfig.META_PODCAST_LIST_DATE.setValue(LocalDateTime.now());
    }

    public void parse(Podcast podcast) {
        //der wird immer geparst, ist ja explizit aufgerufen worden
        parser.addPodcast(podcast);
        new Thread(parser).start();
    }

    public void parse(List<Podcast> podcastList) {
        //hier werden nur die "eingeschalteten" Pods geparst
        parse(podcastList, false);
    }

    public void parse(List<Podcast> podcastList, boolean andStartDownload) {
        //hier werden nur die "eingeschalteten" Pods geparst
        this.andStartDownload = andStartDownload;
        podcastList.stream().forEach(p -> {
            if (p.isActive()) {
                parser.addPodcast(p);
            }
        });
        new Thread(parser).start();
    }

    public void stopParser() {
        if (parser != null) {
            parser.stopParser();
        }
    }

    private class Parser implements Runnable {
        private List<Podcast> podcastList = new ArrayList<>();
        private boolean stop = false;

        public Parser() {
        }

        void addPodcast(Podcast podcast) {
            this.podcastList.add(podcast);
        }

        void addPodcast(List<Podcast> podcastList) {
            this.podcastList.addAll(podcastList);
        }

        @Override
        public void run() {
            try {
                //erst mal parsen
                for (Podcast podcast : podcastList) {
                    if (stop) {
                        break;
                    }
                    P2Log.sysLog("Parse Podcast: " + podcast.getName());
                    ParseRss.parse(progData, podcast);
                }

                //und dann noch starten, falls gewünscht
                if (!andStartDownload || progData.downloadList.isEmpty()) {
                    return;
                }

                //dann die Downloads auch noch starten
                P2Log.sysLog("Downloads: " + progData.downloadList.getSize() + ", jetzt noch starten");
                if (progData.downloadList.size() < 15) {
                    //wenn nicht zu viele, dann sofort starten
                    DownloadFactory.startAllDownloads();
                    return;
                }


                Platform.runLater(() -> {
                    //dann vorher auf jeden Fall fragen
                    P2Alert.BUTTON button = P2Alert.showAlert_yes_no(progData.primaryStage, "Download der Episoden",
                            "Viele Episoden gefunden", "Es wurden " + progData.downloadList.size() +
                                    " Episoden gefunden. Sollen die gefundenen Episoden jetzt " +
                                    "geladen werden?");
                    if (button.equals(P2Alert.BUTTON.YES)) {
                        DownloadFactory.startAllDownloads();
                    }
                });
            } catch (final Exception ignored) {
            }
        }

        public void stopParser() {
            stop = true;
        }
    }
}
