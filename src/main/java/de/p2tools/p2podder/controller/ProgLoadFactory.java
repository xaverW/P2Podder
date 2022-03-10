/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.controller;

import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadConfigFile;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.controller.data.podcast.Podcast;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProgLoadFactory {

    private ProgLoadFactory() {
    }

    public static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        boolean found;
        if ((found = loadProgConfig()) == false) {
            //todo? teils geladene Reste entfernen
            PLog.sysLog("-> konnte nicht geladen werden!");
            clearConfig();
        } else {
            UpdateConfig.update(); // falls es ein Programmupdate gab, Configs anpassen
            PLog.sysLog("-> wurde gelesen!");
        }
        initP2Lib();
        return found;
    }

    /**
     * Podcastliste beim Programmstart laden
     */
    public static void loadPodcastListProgStart(boolean firstProgramStart) {
        // Gui startet ein wenig flüssiger
        Thread th = new Thread(() -> {
            final ProgData progData = ProgData.getInstance();
            PDuration.onlyPing("Programmstart Podcastliste laden: start");

            final List<String> logList = new ArrayList<>();
            logList.add("");
            logList.add(PLog.LILNE1);

            if (firstProgramStart) {
                //schon mal ein paar eintragen
                logList.add("erster Programmstart, ein paar Pods eintragen");

                Podcast podcast = new Podcast();
                podcast.setName("Deutschlandfunk – Der Tag");
                podcast.setGenre("Nachrichten");
                podcast.setUrl("https://www.deutschlandfunk.de/podcast-104.xml");
                podcast.setWebsite("https://www.deutschlandfunk.de");
                podcast.setDescription("Ausgewählte Themen hintergründig eingeordnet – das ist der Anspruch unseres " +
                        "täglichen Podcasts „Der Tag“. Was steckt hinter einer Nachricht und was ergibt sich daraus?");
                progData.podcastList.addNewItem(podcast);

                podcast = new Podcast();
                podcast.init();
                podcast.setName("Auf den Punkt - der SZ-Nachrichtenpodcast");
                podcast.setGenre("Nachrichten");
                podcast.setUrl("https://sz-auf-den-punkt.podigee.io/feed/mp3");
                podcast.setWebsite("https://www.sueddeutsche.de/");
                podcast.setDescription("Die Nachrichten des Tages - als Podcast auf den Punkt gebracht. " +
                        "Bleiben Sie auf dem Laufenden mit aktuellen Meldungen, Interviews und " +
                        "Hintergrundberichten. Kostenlos und immer aktuell. Jeden Montag bis Freitag um 17 Uhr.");
                progData.podcastList.addNewItem(podcast);

                podcast = new Podcast();
                podcast.init();
                podcast.setName("Der Utopia-Podcast – Einfach nachhaltig leben");
                podcast.setGenre("Ökologie");
                podcast.setUrl("https://utopia.podigee.io/feed/mp3");
                podcast.setWebsite("https://utopia.de/");
                podcast.setDescription("Alle sprechen von Nachhaltigkeit – aber was ist damit eigentlich gemeint? " +
                        "Ist es nur Marketing – oder die einzige Möglichkeit, wie wir die kommenden " +
                        "ökologischen Herausforderungen meistern können? Der Utopia-Podcast hilft, " +
                        "den Überblick zu behalten! Und zwar nicht abgehoben, sondern ganz alltagsnah, " +
                        "mit vielen Tipps und nützlichen Hinweisen.");
                progData.podcastList.addNewItem(podcast);

                podcast = new Podcast();
                podcast.init();
                podcast.setName("Radio Tux");
                podcast.setGenre("Linux");
                podcast.setUrl("http://prometheus.radiotux.de/index.php?/feeds/index.rss2");
                podcast.setWebsite("https://radiotux.de");
                podcast.setDescription("RadioTux ist ein 2001 gegründetes Podcast-Projekt mit den thematischen " +
                        "Schwerpunkten Linux, Open Source und Netzkultur. Es produziert in " +
                        "regelmäßigen Abständen verschiedene Formate und hat sich zu einem " +
                        "gefragten Medienpartner für OpenSource Events entwickelt.");
                progData.podcastList.addNewItem(podcast);

                podcast = new Podcast();
                podcast.init();
                podcast.setName("beVegt-Podcast");
                podcast.setGenre("Gesundheit");
                podcast.setUrl("https://bevegt.libsyn.com/rss");
                podcast.setWebsite("https://www.bevegt.de/");
                podcast.setDescription("Im beVegt-Podcast versorgen wir dich einmal pro Woche " +
                        "mit Tipps rund um vegane Ernährung, Laufen, Motivation, Nachhaltigkeit, " +
                        "Minimalismus und mehr. Wir helfen dir dabei, ein besserer Läufer zu werden, " +
                        "Spaß an einem gesunden und aktiven Lebensstil zu haben und deine " +
                        "persönlichen Ziele zu erreichen.");
                progData.podcastList.addNewItem(podcast);

            }
            logList.add(PLog.LILNE1);
            logList.add("");
            PLog.addSysLog(logList);
        });

        th.setName("loadStationProgStart");
        th.start();
    }

    private static void clearConfig() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.downloadList.clear();
        progData.episodeStoredList.clear();
    }

    private static boolean loadProgConfig() {
        final Path path = ProgInfos.getSettingsFile();
        PLog.sysLog("Programmstart und ProgConfig laden von: " + path.toString());
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, path);

        ProgConfig.addConfigData(configFile);
        ReadConfigFile readConfigFile = new ReadConfigFile();
        readConfigFile.addConfigFile(configFile);
        boolean ret = readConfigFile.readConfigFile();
        return ret;
    }

    private static void initP2Lib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAM_NAME, ProgInfos.getUserAgent(),
                ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }
}


