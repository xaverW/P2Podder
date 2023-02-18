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
import de.p2tools.p2Lib.configFile.ConfigReadFile;
import de.p2tools.p2Lib.tools.ProgramToolsFactory;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.ImportSetDataFactory;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.gui.startDialog.StartDialogController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProgStartBeforeGui {
    public static boolean firstProgramStart = false;

    private ProgStartBeforeGui() {
    }

    public static boolean workBeforeGui() {
        boolean loadOk = loadProgConfigData();
        if (ProgConfig.SYSTEM_LOG_ON.get()) {
            PLogger.setFileHandler(ProgInfosFactory.getLogDirectoryString());
        }

        if (!loadOk) {
            PDuration.onlyPing("Erster Start");
            firstProgramStart = true;

            StartDialogController startDialogController = new StartDialogController();
            if (!startDialogController.isOk()) {
                // dann jetzt beenden -> Tschüss
                Platform.exit();
                System.exit(0);
            }
        }
        ProgData.getInstance().episodeList.initList();
        ProgData.getInstance().podcastList.initList();
        ProgData.getInstance().downloadList.initList();
        if (ProgData.getInstance().setDataList.isEmpty()) {
            //beim ersten Start oder wenn sie sonst fehlen
            addStandarSets();
        }

        resetConfigs();
        return firstProgramStart;
    }


    private static void clearTheConfigs() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.downloadList.clear();
        progData.episodeList.clear();
    }

    private static boolean loadProgConfig() {
        final Path xmlFilePath = ProgInfosFactory.getSettingsFile();
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        try {
            if (!Files.exists(xmlFilePath)) {
                //dann gibts das Konfig-File gar nicht
                PLog.sysLog("Konfig existiert nicht!");
                return false;
            }

            PLog.sysLog("Programmstart und ProgConfig laden von: " + xmlFilePath);
            ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true) {
                @Override
                public void clearConfigFile() {
                    clearTheConfigs();
                }
            };
            ProgConfig.addConfigData(configFile);
            if (ConfigReadFile.readConfig(configFile)) {
                PLog.sysLog("Konfig wurde geladen!");
                return true;

            } else {
                // dann hat das Laden nicht geklappt
                PLog.sysLog("Konfig konnte nicht geladen werden!");
                return false;
            }
        } catch (final Exception ex) {
            PLog.errorLog(915470101, ex);
        }
        return false;
    }


    private static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        boolean found;
        if ((found = loadProgConfig()) == false) {
            //todo? teils geladene Reste entfernen
            PLog.sysLog("-> konnte nicht geladen werden!");
            clearTheConfigs();
        } else {
            PLog.sysLog("-> wurde gelesen!");
        }
        return found;
    }

    private static void resetConfigs() {
        if (!ProgConfig.SYSTEM_PROG_VERSION.getValueSafe().equals(ProgramToolsFactory.getProgVersion()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_NO.getValueSafe().equals(ProgramToolsFactory.getBuild()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe().equals(ProgramToolsFactory.getCompileDate())) {

            //dann ist eine neue Version/Build
            PLog.sysLog("===============================");
            PLog.sysLog(" eine neue Version/Build");
            PLog.sysLog(" Einstellung zurücksetzen");

            ProgConfig.PODCAST_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.PODCAST_GUI_TABLE_SORT.setValue("");
            ProgConfig.PODCAST_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.PODCAST_GUI_TABLE_VIS.setValue("");
            ProgConfig.PODCAST_GUI_TABLE_ORDER.setValue("");

            ProgConfig.DOWNLOAD_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.DOWNLOAD_GUI_TABLE_SORT.setValue("");
            ProgConfig.DOWNLOAD_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.DOWNLOAD_GUI_TABLE_VIS.setValue("");
            ProgConfig.DOWNLOAD_GUI_TABLE_ORDER.setValue("");

            ProgConfig.EPISODE_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.EPISODE_GUI_TABLE_SORT.setValue("");
            ProgConfig.EPISODE_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.EPISODE_GUI_TABLE_VIS.setValue("");
            ProgConfig.EPISODE_GUI_TABLE_ORDER.setValue("");

            ProgConfig.SMALL_EPISODE_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.SMALL_EPISODE_GUI_TABLE_SORT.setValue("");
            ProgConfig.SMALL_EPISODE_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.SMALL_EPISODE_GUI_TABLE_VIS.setValue("");
            ProgConfig.SMALL_EPISODE_GUI_TABLE_ORDER.setValue("");
        }
    }

    private static void addStandarSets() {
        Platform.runLater(() -> {
            PDuration.onlyPing("Erster Start: PSet");
            // kann ein Dialog aufgehen
            final SetDataList pSet = ImportSetDataFactory.getStandarset();
            if (pSet != null) {
                ProgData.getInstance().setDataList.addSetData(pSet);
                ProgConfig.SYSTEM_UPDATE_PROGSET_VERSION.setValue(pSet.version);
            }
            PDuration.onlyPing("Erster Start: PSet geladen");
        });

    }

    public static void setTitle(Stage stage) {
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramToolsFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramToolsFactory.getProgVersion());
        }
    }
}