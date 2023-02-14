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

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.WriteConfigFile;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.dialog.QuitDialogController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.nio.file.Path;

public class ProgQuitFactory {

    private ProgQuitFactory() {
    }

    public static void saveAll() {
        PLog.sysLog("save all data");
        saveProgConfig();
    }

    public static void saveProgConfig() {
        //sind die Programmeinstellungen
        PLog.sysLog("save progConfig");

        final Path xmlFilePath = ProgInfosFactory.getSettingsFile();
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
        ProgConfig.addConfigData(configFile);

        WriteConfigFile writeConfigFile = new WriteConfigFile();
        writeConfigFile.addConfigFile(configFile);
        writeConfigFile.writeConfigFile();
    }

    /**
     * Quit the application
     *
     * @param showOptionTerminate show options dialog when stations are running
     */
    public static void quit(Stage stage, boolean showOptionTerminate) {
        if (quit_(stage, showOptionTerminate)) {

            // dann jetzt beenden -> Thüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private static boolean quit_(Stage stage, boolean showOptionTerminate) {
        // erst mal prüfen ob noch Episoden laufen
        if (ProgData.getInstance().episodeList.countStartedAndRunningEpisode() > 0) {
            // und ob der Dialog angezeigt werden soll
            if (showOptionTerminate) {
                QuitDialogController quitDialogController;
                quitDialogController = new QuitDialogController(stage);
                if (!quitDialogController.canTerminate()) {
                    return false;
                }
            }
        }

        // und dann Programm beenden
        stopAll();
        writeTabSettings();

        saveProgConfig();
        LogMessage.endMsg();
        return true;
    }

    private static void stopAllDownloads() {
        //erst mal alle Downloads stoppen und zurücksetzen
        ProgData.getInstance().downloadList.forEach(download -> {
            if (!download.isStateFinished()) {
                download.stopDownload();
            }
        });

        //fertige werden entfernt
        ProgData.getInstance().downloadList.removeIf(download -> download.isStateFinished());
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
