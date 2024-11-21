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

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigWriteFile;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.application.Platform;

import java.nio.file.Path;

public class ProgQuitFactory {

    private ProgQuitFactory() {
    }

    public static void saveAll() {
        P2Log.sysLog("save all data");
        saveProgConfig();
    }

    public static void saveProgConfig() {
        //sind die Programmeinstellungen
        P2Log.sysLog("Alle Programmeinstellungen sichern");
        final Path xmlFilePath = ProgInfosFactory.getSettingsFile();
        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true);
        ProgConfig.addConfigData(configFile);
        ConfigWriteFile.writeConfigFile(configFile);
    }

    /**
     * Quit the application
     */
    public static void quit() {
        ProgData.getInstance().downloadList.forEach(d -> {
            System.out.println(d.getState());
            System.out.println(d.getGuiState());
        });


        if (quit_()) {

            // dann jetzt beenden -> Thüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private static boolean quit_() {
        // und dann Programm beenden
        stopAll();
        writeTabSettings();

        saveProgConfig();
        P2LogMessage.endMsg();
        return true;
    }

    private static void stopAllDownloads() {
        // Fertige werden entfernt
        ProgData.getInstance().downloadList.removeIf(download -> download.isStateFinished());

        // Alle Downloads stoppen und zurücksetzen
        ProgData.getInstance().downloadList.forEach(download -> {
            download.stopDownload();
            download.setStateInit(); // sonst stehen sie auf "Abgebrochen"
        });
    }

    private static void stopAll() {
        EpisodeFactory.stopAllEpisode();
        stopAllDownloads();
    }

    private static void writeTabSettings() {
        // Tabelleneinstellungen merken
        ProgData.getInstance().podcastGui.getPodcastGuiController().saveTable();
        ProgData.getInstance().downloadGui.getDownloadGuiController().saveTable();
        ProgData.getInstance().episodeGui.getEpisodeGuiController().saveTable();
    }
}
