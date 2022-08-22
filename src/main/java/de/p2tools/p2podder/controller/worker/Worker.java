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

package de.p2tools.p2podder.controller.worker;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.parser.ParserThread;

import java.util.ArrayList;
import java.util.Optional;

public class Worker extends Thread {

    private final ProgData progData;

    public Worker(ProgData progData) {
        this.progData = progData;
    }

    public void setPodcastActive(boolean set) {
        //ein/ausschalten
        progData.podcastGui.getPodcastGuiController().getSelList()
                .stream().forEach(p -> p.setActive(set));
    }

    public void setPodcastActive(Podcast podcast) {
        //ein/ausschalten
        podcast.setActive(!podcast.isActive());
    }

    public void updatePodcast(boolean all) {
        // Menü/Button podcast aktualisieren
        if (all) {
            new ParserThread(progData).parse(progData.podcastList);
        } else {
            ArrayList<Podcast> list = progData.podcastGui.getPodcastGuiController().getSelList();
            if (!list.isEmpty()) {
                new ParserThread(progData).parse(list);
            }
        }
    }

    public void updateSelectedPodcast() {
        // Menü/Button podcast aktualisieren
        final Optional<Podcast> podcast = progData.podcastGui.getPodcastGuiController().getSel();
        if (podcast.isPresent()) {
            new ParserThread(progData).parse(podcast.get());
        }
    }
}