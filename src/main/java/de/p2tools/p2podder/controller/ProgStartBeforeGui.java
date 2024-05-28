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
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.controller.data.ImportSetDataFactory;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.gui.startdialog.StartDialogController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ProgStartBeforeGui {
//    public static boolean firstProgramStart = false;

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui() {
        if (!loadAll()) {
            // dann ist der erste Start
            P2Duration.onlyPing("Erster Start");
            ProgData.firstProgramStart = true;

            ProgConfigUpdate.setUpdateDone(); // dann ist's ja kein Programmupdate

            StartDialogController startDialogController = new StartDialogController();
            if (!startDialogController.isOk()) {
                // dann jetzt beenden -> Tschüs
                Platform.exit();
                System.exit(0);
            }
        }


//        boolean loadOk = loadProgConfigData();
//        if (ProgConfig.SYSTEM_LOG_ON.get()) {
//            P2Logger.setFileHandler(ProgInfosFactory.getLogDirectoryString());
//        }
//
//        if (!loadOk) {
//            P2Duration.onlyPing("Erster Start");
//            firstProgramStart = true;
//
//            StartDialogController startDialogController = new StartDialogController();
//            if (!startDialogController.isOk()) {
//                // dann jetzt beenden -> Tschüss
//                Platform.exit();
//                System.exit(0);
//            }
//        }

        ProgData.getInstance().episodeList.initList();
        ProgData.getInstance().podcastList.initList();
        ProgData.getInstance().downloadList.initList();
        if (ProgData.getInstance().setDataList.isEmpty()) {
            //beim ersten Start oder wenn sie sonst fehlen
            addStandarSets();
        }

//        resetConfigs();
//        return firstProgramStart;
    }


    private static void clearTheConfigs() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.downloadList.clear();
        progData.episodeList.clear();
    }

    private static boolean loadAll() {
        ArrayList<String> logList = new ArrayList<>();
        boolean ret = load(logList);

        if (ProgConfig.SYSTEM_LOG_ON.getValue()) {
            // dann für den evtl. geänderten LogPfad
            P2Logger.setFileHandler(ProgInfos.getLogDirectory_String());
        }
        P2Log.sysLog(logList);

        if (!ret) {
            P2Log.sysLog("Weder Konfig noch Backup konnte geladen werden!");
            // teils geladene Reste entfernen
            clearTheConfigs();
        }

        return ret;
    }

    private static boolean load(ArrayList<String> logList) {
        final Path xmlFilePath = ProgInfosFactory.getSettingsFile();
        P2Duration.onlyPing("ProgStartFactory.loadProgConfigData");
        try {
            if (!Files.exists(xmlFilePath)) {
                //dann gibts das Konfig-File gar nicht
                logList.add("Konfig existiert nicht!");
                return false;
            }

            logList.add("Programmstart und ProgConfig laden von: " + xmlFilePath);
            ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true) {
                @Override
                public void clearConfigFile() {
                    clearTheConfigs();
                }
            };
            ProgConfig.addConfigData(configFile);
            if (ConfigReadFile.readConfig(configFile)) {
                initAfterLoad();
                logList.add("Konfig wurde geladen!");
                return true;

            } else {
                // dann hat das Laden nicht geklappt
                logList.add("Konfig konnte nicht geladen werden!");
                return false;
            }
        } catch (final Exception ex) {
            logList.add(ex.getLocalizedMessage());
        }
        return false;
    }


//    private static boolean loadProgConfigData() {
//        P2Duration.onlyPing("ProgStartFactory.loadProgConfigData");
//        boolean found;
//        if ((found = loadProgConfig()) == false) {
//            //todo? teils geladene Reste entfernen
//            P2Log.sysLog("-> konnte nicht geladen werden!");
//            clearTheConfigs();
//        } else {
//            P2Log.sysLog("-> wurde gelesen!");
//        }
//        return found;
//    }

//    private static void resetConfigs() {
//        if (!ProgConfig.SYSTEM_PROG_VERSION.getValueSafe().equals(P2ToolsFactory.getProgVersion()) ||
//                !ProgConfig.SYSTEM_PROG_BUILD_NO.getValueSafe().equals(P2ToolsFactory.getBuild()) ||
//                !ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe().equals(P2ToolsFactory.getCompileDate())) {
//
//            //dann ist eine neue Version/Build
//            P2Log.sysLog("===============================");
//            P2Log.sysLog(" eine neue Version/Build");
//            P2Log.sysLog(" Einstellung zurücksetzen");
//
//            ProgConfig.PODCAST_GUI_TABLE_WIDTH.setValue("");
//            ProgConfig.PODCAST_GUI_TABLE_SORT.setValue("");
//            ProgConfig.PODCAST_GUI_TABLE_UP_DOWN.setValue("");
//            ProgConfig.PODCAST_GUI_TABLE_VIS.setValue("");
//            ProgConfig.PODCAST_GUI_TABLE_ORDER.setValue("");
//
//            ProgConfig.DOWNLOAD_GUI_TABLE_WIDTH.setValue("");
//            ProgConfig.DOWNLOAD_GUI_TABLE_SORT.setValue("");
//            ProgConfig.DOWNLOAD_GUI_TABLE_UP_DOWN.setValue("");
//            ProgConfig.DOWNLOAD_GUI_TABLE_VIS.setValue("");
//            ProgConfig.DOWNLOAD_GUI_TABLE_ORDER.setValue("");
//
//            ProgConfig.EPISODE_GUI_TABLE_WIDTH.setValue("");
//            ProgConfig.EPISODE_GUI_TABLE_SORT.setValue("");
//            ProgConfig.EPISODE_GUI_TABLE_UP_DOWN.setValue("");
//            ProgConfig.EPISODE_GUI_TABLE_VIS.setValue("");
//            ProgConfig.EPISODE_GUI_TABLE_ORDER.setValue("");
//
//            ProgConfig.SMALL_EPISODE_GUI_TABLE_WIDTH.setValue("");
//            ProgConfig.SMALL_EPISODE_GUI_TABLE_SORT.setValue("");
//            ProgConfig.SMALL_EPISODE_GUI_TABLE_UP_DOWN.setValue("");
//            ProgConfig.SMALL_EPISODE_GUI_TABLE_VIS.setValue("");
//            ProgConfig.SMALL_EPISODE_GUI_TABLE_ORDER.setValue("");
//        }
//    }

    private static void addStandarSets() {
        Platform.runLater(() -> {
            P2Duration.onlyPing("Erster Start: PSet");
            // kann ein Dialog aufgehen
            final SetDataList pSet = ImportSetDataFactory.getStandarset();
            if (pSet != null) {
                ProgData.getInstance().setDataList.addSetData(pSet);
                ProgConfig.SYSTEM_UPDATE_PROGSET_VERSION.setValue(pSet.version);
            }
            P2Duration.onlyPing("Erster Start: PSet geladen");
        });

    }

    public static void setTitle(Stage stage) {
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2ToolsFactory.getProgVersion());
        }
    }

    private static void initAfterLoad() {
        ProgConfigUpdate.update(); // falls es ein Programmupdate gab, Configs anpassen
        ProgColorList.setColorTheme(); // Farben einrichten
    }
}