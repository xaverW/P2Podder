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

import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.controller.data.ImportSetDataFactory;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.gui.startdialog.StartDialogController;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ProgStartBeforeGui {

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui() {
        boolean load = loadAll();
        initP2lib();

        if (!load) {
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

        ProgData.getInstance().episodeList.initList();
        ProgData.getInstance().podcastList.initList();
        ProgData.getInstance().downloadList.initList();
        if (ProgData.getInstance().setDataList.isEmpty()) {
            //beim ersten Start oder wenn sie sonst fehlen
            addStandardSets();
        }
    }

    private static void initP2lib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStageBig, ProgConst.PROGRAM_NAME, "",
                ProgConfig.SYSTEM_DARK_THEME, ProgConfig.SYSTEM_BLACK_WHITE_ICON, ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConst.CSS_FILE, ProgConst.CSS_FILE_DARK_THEME, ProgConfig.SYSTEM_STYLE_SIZE,
                "", "",
                ProgData.debug, ProgData.duration);
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

    private static void clearTheConfigs() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.downloadList.clear();
        progData.episodeList.clear();
    }

    private static void addStandardSets() {
        Platform.runLater(() -> {
            P2Duration.onlyPing("Erster Start: PSet");
            try {
                // kann ein Dialog aufgehen
                final SetDataList pSet = ImportSetDataFactory.getStandarset();
                if (pSet != null) {
                    ProgData.getInstance().setDataList.addSetData(pSet);
                    ProgConfig.SYSTEM_UPDATE_PROGSET_VERSION.setValue(pSet.version);
                }
            } catch (Exception ex) {
                P2Log.errorLog(210124589, "Konnte die Sets nicht laden!");
                P2Alert.showErrorAlert(ProgData.getInstance().primaryStage, "Sets einrichten",
                        "Konnte die Sets zum Abspielen " +
                                "der Episoden nicht einrichten");
            }
            P2Duration.onlyPing("Erster Start: PSet geladen");
        });
    }

    private static void initAfterLoad() {
        ProgConfigUpdate.update(); // falls es ein Programmupdate gab, Configs anpassen
        ProgColorList.setColorTheme(); // Farben einrichten
    }
}