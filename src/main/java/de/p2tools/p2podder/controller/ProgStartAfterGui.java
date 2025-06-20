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

import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.parser.ParserThread;
import de.p2tools.p2podder.gui.dialog.AutoDialog;
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
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion());
        }
    }

    public static void loadPodcastListProgStart() {
        P2Duration.onlyPing("Programmstart Podcastliste laden: start");

        if (ProgData.firstProgramStart) {
            //schon mal ein paar eintragen
            addStartPodcasts();
            return;
        }

        if (ProgData.auto) {
            // immer suchen und starten
            P2Log.sysLog("Auto: Nach neuen Episoden suchen");
            ProgConfig.SYSTEM_UPDATE_PODCAST_DATE.setValue(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
            new ParserThread(ProgData.getInstance()).parse(ProgData.getInstance().podcastList, true);
            new AutoDialog(ProgData.getInstance());
            return;
        }

        if (ProgConfig.SYSTEM_UPDATE_PODCAST_DAILY.getValue()) {
            //wenn gewünscht und heute noch nicht gemacht, die Podcasts aktualisieren
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

    private static void addStartPodcasts() {
        //schon mal ein paar eintragen
        P2Log.sysLog(P2Log.LILNE1);
        P2Log.sysLog("erster Programmstart, ein paar Pods eintragen");

        Podcast podcast = new Podcast();
        podcast.setName("DRadio-Europa Heute");
        podcast.setGenre("Nachrichten");
        podcast.setMaxAge(40);
        podcast.setUrl("https://www.deutschlandfunk.de/europa-heute-104.xml");
        podcast.setWebsite("https://www.deutschlandfunk.de/europa-heute-100.html");
        podcast.setDescription("In \"Europa heute\" wird unser Kontinent konkret. Unsere Reporter betrachten " +
                "Alltagsphänomene und stellen sie in den großen, politischen " +
                "Zusammenhang. \"Europa heute\" erklärt und schildert.");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("Forschung Aktuell - Deutschlandfunk");
        podcast.setGenre("Forschung");
        podcast.setMaxAge(40);
        podcast.setUrl("https://www.deutschlandfunk.de/forschung-aktuell-104.xml");
        podcast.setWebsite("https://www.deutschlandfunk.de/forschung-aktuell-100.html");
        podcast.setDescription("Täglich das Neueste aus Naturwissenschaft, Medizin und Technik. Berichte, " +
                "Reportagen und Interviews aus der Welt der Wissenschaft. Ob Astronomie, Biologie, " +
                "Chemie, Geologie, Ökologie, Physik oder Raumfahrt: Forschung Aktuell liefert Wissen im " +
                "Kontext und Bildung mit Unterhaltungswert.");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("Computer und Kommunikation (komplette Sendung) - Deutschlandfunk");
        podcast.setGenre("Computer");
        podcast.setMaxAge(40);
        podcast.setUrl("https://www.deutschlandfunk.de/computer-und-kommunikation-102.xml");
        podcast.setWebsite("https://www.deutschlandfunk.de/computer-und-kommunikation-102.html");
        podcast.setDescription("Jeden Samstag das Neueste aus Computertechnik und Informationstechnologie. " +
                "Beiträge, Reportagen und Interviews zu IT-Sicherheit, Informatik, " +
                "Datenschutz, Smartphones, Cloud-Computing und IT-Politik. Die Trends der IT " +
                "werden kompakt und informativ zusammengefasst.");
        ProgData.getInstance().podcastList.addNewItem(podcast);

        podcast = new Podcast();
        podcast.setName("DRadio-Umwelt und Verbraucher");
        podcast.setGenre("Umweltschutz");
        podcast.setMaxAge(40);
        podcast.setUrl("https://www.deutschlandfunk.de/umwelt-und-verbraucher-102.xml");
        podcast.setWebsite("https://www.deutschlandfunk.de/umwelt-und-verbraucher-100.html");
        podcast.setDescription("Von Mülltrennung bis Klimaschutz – alles, was die Umwelt bewegt und wie sie sich verändert. " +
                "Dazu Informationen für Verbraucher. Politisch und praktisch mit viel Nutzen für den Alltag.");
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
        if (ProgConfig.SYSTEM_SEARCH_UPDATE.getValue() &&
                !isUpdateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck();

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List<String> list = new ArrayList<>(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_SEARCH_UPDATE.getValue()) {
                list.add("  der User will nicht");
            }
            if (isUpdateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            P2Log.sysLog(list);
        }
    }

    private static boolean isUpdateCheckTodayDone() {
        return ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.get().equals(P2LDateFactory.getNowStringR());
    }

    private static void runUpdateCheck() {
        ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.setValue(P2LDateFactory.getNowStringR());
        new SearchProgramUpdate(ProgData.getInstance()).searchNewProgramVersion(false);
    }
}