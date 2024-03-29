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

package de.p2tools.p2podder.controller.starterdownload;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.pnotification.P2Notification;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.episode.Episode;


public class MTNotification {
    public static void addNotification(Episode episode, boolean error) {
        String text = ("Podcast:   " + episode.getPodcastName() + P2LibConst.LINE_SEPARATOR +
                "Episode: " + episode.getEpisodeTitle() + P2LibConst.LINE_SEPARATOR +
                (error ? "Download war fehlerhaft" : "Download war erfolgreich"));

        add(text, error);
    }

    private static void add(String text, boolean error) {
        if (!ProgConfig.EPISODE_SHOW_NOTIFICATION.get()) {
            return;
        }

        P2Notification.addNotification("Download beendet", text, error);
    }

}





