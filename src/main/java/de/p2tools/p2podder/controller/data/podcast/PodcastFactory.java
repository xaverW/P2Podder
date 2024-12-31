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


package de.p2tools.p2podder.controller.data.podcast;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.podcastadd.PodcastAddDialogController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PodcastFactory {
    private static Map<Character, Integer> counterMap = new HashMap<>(25);
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

    private PodcastFactory() {
    }

    public static void addNewPodcast() {
        ArrayList<Podcast> list = new ArrayList<>();
        list.add(new Podcast());
        new PodcastAddDialogController(ProgData.getInstance(), list, true);
    }
}
