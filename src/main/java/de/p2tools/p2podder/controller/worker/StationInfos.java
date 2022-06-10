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

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.tools.Listener;

public class StationInfos {

    private int amount = 0; //Gesamtanzahl
    private int notStarted = 0; //davon gestartet, alle, egal ob warten, laden oder fertig
    private int started = 0; //davon gestartet, alle, egal ob warten, laden oder fertig

    private final ProgData progData;
    private boolean search = false;

    public StationInfos(ProgData progData) {
        this.progData = progData;
        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StationInfos.class.getSimpleName()) {
            @Override
            public void ping() {
                generateInfos();
            }
        });
    }


    public synchronized int getAmount() {
        return amount;
    }

    public int getNotStarted() {
        return notStarted;
    }

    public synchronized int getStarted() {
        return started;
    }

    private synchronized void generateInfos() {
        search = !search;
        if (!search) {
            //nur alle 2 Sekunden suchen
            return;
        }

        PDuration.counterStart("generateInfos");
        // generiert die Anzahl Episoden
        clean();
        for (final Podcast podcast : progData.podcastList) {
            ++amount;
//            if (podcast.getStart() != null) {
//                ++started;
//            } else {
            ++notStarted;
//            }
        }
        PDuration.counterStop("generateInfos");
    }

    private synchronized void clean() {
        //DonwloadInfos
        amount = 0;
        notStarted = 0;
        started = 0;
    }
}
