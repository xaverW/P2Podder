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

import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.dialog.PodcastEditDialogController;

import java.text.SimpleDateFormat;
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
        Podcast podcast = new Podcast();
        PodcastEditDialogController podcastEditDialogController =
                new PodcastEditDialogController(ProgData.getInstance(), podcast, true);
        if (podcastEditDialogController.isOk()) {
            ProgData.getInstance().podcastList.addNewItem(podcast);
        }
    }

    public static void cleanFaultyCharacterStationList() {
        //damit werden Unicode-Zeichen korrigiert
        //gibt da einen Java-Bug
        //https://github.com/javafxports/openjdk-jfx/issues/287
        PDuration.counterStart("cleanFaultyCharacter");
        PodcastList podcastList = ProgData.getInstance().podcastList;
        podcastList.stream().forEach(station -> {
        });

        for (Map.Entry<Character, Integer> entry : counterMap.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            PLog.sysLog("Key: " + (int) key + "  Key: " + key + "  Anz: " + value);

        }
        PDuration.counterStop("cleanFaultyCharacter");
    }


    final static String regEx1 = "[\\n\\r]";
    final static String regEx2 = "[\\p{Cc}&&[^\\t\\n\\r]]";

    public static String cleanUnicode(String ret) {
        return clean_1(ret, true);
    }

    private static String clean_1(String ret, boolean alsoNewLine) {
        //damit werden Unicode-Zeichen korrigiert
        //gibt da eine Java-Bug
        //https://github.com/javafxports/openjdk-jfx/issues/287
        if (alsoNewLine) {
            ret = ret.replaceAll(regEx1, " ").replaceAll(regEx2, "");
        } else {
            ret = ret.replaceAll(regEx2, "");
        }
        return ret;
    }

    private static String clean_2(String test) {
        //damit werden Unicode-Zeichen korrigiert
        //gibt da eine Java-Bug, auch Probleme bei Linux mit fehlenden Zeichen in den code tablen
        //https://github.com/javafxports/openjdk-jfx/issues/287
        char[] c = test.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if ((int) c[i] > 11263) { //der Wert ist jetzt einfach mal geschätzt und kommt ~ 20x vor
                counterMap.merge(c[i], 1, Integer::sum);
                c[i] = ' ';
                test = String.valueOf(c);
            }
        }
        return test;
    }
}
