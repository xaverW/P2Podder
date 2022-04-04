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

import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.dialog.QuitDialogController;
import javafx.application.Platform;

public class ProgQuitFactory {

    private ProgQuitFactory() {
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

    /**
     * Quit the application
     *
     * @param showOptionTerminate show options dialog when stations are running
     */
    public static void quit(boolean showOptionTerminate) {
        if (quit_(showOptionTerminate)) {

            // dann jetzt beenden -> Thüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private static boolean quit_(boolean showOptionTerminate) {
        // erst mal prüfen ob noch Episoden laufen
        if (ProgData.getInstance().episodeStoredList.countStartedAndRunningEpisode() > 0) {
            // und ob der Dialog angezeigt werden soll
            if (showOptionTerminate) {
                QuitDialogController quitDialogController;
                quitDialogController = new QuitDialogController();
                if (!quitDialogController.canTerminate()) {
                    return false;
                }
            }
        }

        // und dann Programm beenden
        stopAll();
        writeTabSettings();

        ProgSaveFactory.saveProgConfig();
        LogMessage.endMsg();
        return true;
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
