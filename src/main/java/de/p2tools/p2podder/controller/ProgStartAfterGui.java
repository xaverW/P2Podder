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

package de.p2tools.p2podder.controller;

import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.parser.ParserThread;
import de.p2tools.p2podder.tools.update.SearchProgramUpdate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartAfterGui {

    private ProgStartAfterGui() {
    }

    /**
     * alles was nach der GUI gemacht werden soll z.B.
     * Podcast beim Programmstart!! laden
     */
    public static void workAfterGui() {
        startMsg();
        setTitle();

        ProgData.getInstance().initProgData();
        checkProgUpdate();
        loadPodcastListProgStart();
    }

    private static void setTitle() {
        // muss nur für das große GUI gesetzt werden
        Stage stage = ProgData.getInstance().primaryStageBig;
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion());
        }
    }

    /**
     * Podcastliste beim Programmstart laden
     */
    public static void loadPodcastListProgStart() {
        // Gui startet ein wenig flüssiger
        Thread th = new Thread(() -> {
            P2Duration.onlyPing("Programmstart Podcastliste laden: start");

            if (ProgData.firstProgramStart) {
                //schon mal ein paar eintragen
                addStartPodcasts();
            } else {
                //wenn gewünscht und heute noch nicht gemacht, die Podcasts aktualisieren
                if (ProgConfig.SYSTEM_UPDATE_PODCAST_DAILY.getValue()) {
                    if (ProgConfig.SYSTEM_UPDATE_PODCAST_DATE.get().equals(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()))) {
                        P2Log.sysLog("keine neuen Episoden suchen: Heute schon gemacht");
                    } else {
                        P2Log.sysLog("nach neuen Episoden suchen");
                        ProgConfig.SYSTEM_UPDATE_PODCAST_DATE.setValue(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
                        new ParserThread(ProgData.getInstance()).parse(ProgData.getInstance().podcastList, ProgConfig.SYSTEM_START_DAILY_DOWNLOAD.getValue());
                    }
                } else {
                    P2Log.sysLog("keine neuen Episoden suchen: Nicht gewünscht");
                }
            }
        });

        th.setName("loadStationProgStart");
        th.start();
    }

    private static void addStartPodcasts() {
        //schon mal ein paar eintragen
        P2Log.sysLog(P2Log.LILNE1);
        P2Log.sysLog("erster Programmstart, ein paar Pods eintragen");

        Podcast podcast = new Podcast();
        podcast.setName("Deutschlandfunk – Der Tag");
        podcast.setGenre("Nachrichten");
        podcast.setMaxAge(40);
        podcast.setUrl("https://www.deutschlandfunk.de/podcast-104.xml");
        podcast.setWebsite("https://www.deutschlandfunk.de");
        podcast.setDescription("Ausgewählte Themen hintergründig eingeordnet – das ist der Anspruch unseres " +
                "täglichen Podcasts „Der Tag“. Was steckt hinter einer Nachricht und was ergibt sich daraus?");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("Auf den Punkt - der SZ-Nachrichtenpodcast");
        podcast.setGenre("Nachrichten");
        podcast.setMaxAge(40);
        podcast.setUrl("https://sz-auf-den-punkt.podigee.io/feed/mp3");
        podcast.setWebsite("https://www.sueddeutsche.de/");
        podcast.setDescription("Die Nachrichten des Tages - als Podcast auf den Punkt gebracht. " +
                "Bleiben Sie auf dem Laufenden mit aktuellen Meldungen, Interviews und " +
                "Hintergrundberichten. Kostenlos und immer aktuell. Jeden Montag bis Freitag um 17 Uhr.");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("Der Utopia-Podcast – Einfach nachhaltig leben");
        podcast.setGenre("Ökologie");
        podcast.setMaxAge(40);
        podcast.setUrl("https://utopia.podigee.io/feed/mp3");
        podcast.setWebsite("https://utopia.de/");
        podcast.setDescription("Alle sprechen von Nachhaltigkeit – aber was ist damit eigentlich gemeint? " +
                "Ist es nur Marketing – oder die einzige Möglichkeit, wie wir die kommenden " +
                "ökologischen Herausforderungen meistern können? Der Utopia-Podcast hilft, " +
                "den Überblick zu behalten! Und zwar nicht abgehoben, sondern ganz alltagsnah, " +
                "mit vielen Tipps und nützlichen Hinweisen.");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("Radio Tux");
        podcast.setGenre("Linux");
        podcast.setMaxAge(40);
        podcast.setUrl("http://prometheus.radiotux.de/index.php?/feeds/index.rss2");
        podcast.setWebsite("https://radiotux.de");
        podcast.setDescription("RadioTux ist ein 2001 gegründetes Podcast-Projekt mit den thematischen " +
                "Schwerpunkten Linux, Open Source und Netzkultur. Es produziert in " +
                "regelmäßigen Abständen verschiedene Formate und hat sich zu einem " +
                "gefragten Medienpartner für OpenSource Events entwickelt.");
        ProgData.getInstance().podcastList.addNewItem(podcast);
    }

    private static void startMsg() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfosFactory.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfosFactory.getSettingsDirectoryString());
        list.add(P2Log.LILNE2);
        list.add("");
        list.add("Programmsets:");
        list.addAll(ProgData.getInstance().setDataList.getStringListSetData());
        ProgConfig.getConfigLog(list);
        P2LogMessage.startMsg(ProgConst.PROGRAM_NAME, list);
    }

    private static void checkProgUpdate() {
        // Prüfen obs ein Programmupdate gibt
        P2Duration.onlyPing("checkProgUpdate");

        if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
                !updateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck(false);

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List list = new ArrayList(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get()) {
                list.add("  der User will nicht");
            }
            if (updateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            P2Log.sysLog(list);
        }
    }

    private static boolean updateCheckTodayDone() {
        return ProgConfig.SYSTEM_UPDATE_DATE.get().equals(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }

    private static void runUpdateCheck(boolean showAlways) {
        //prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version, ..
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
        new SearchProgramUpdate(ProgData.getInstance()).searchNewProgramVersion(showAlways);
    }
}