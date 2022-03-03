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

import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;

import java.util.ArrayList;
import java.util.List;

public class ParserThread {
    private final ProgData progData;
    private Parser parser = new Parser();

    public ParserThread(ProgData progData) {
        this.progData = progData;
        progData.podcastList.setGenDateNow();
    }

    public void parse(Podcast podcast) {
        parser.addPodcast(podcast);
        new Thread(parser).start();
    }

    public void parse(List<Podcast> podcastList) {
        parser.addPodcast(podcastList);
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
                for (Podcast podcast : podcastList) {
                    if (stop) {
                        break;
                    }
                    PLog.sysLog("Parse Podcast: " + podcast.getName());
                    ParseRss.parse(progData, podcast);
                }
            } catch (final Exception ignored) {
            }
        }

        public void stopParser() {
            stop = true;
        }
    }
}
