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


package de.p2tools.p2podder.controller;

import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfos;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;

public class ProgConfigUpdate {
    // hier werden geänderte Programmeinstellungen/Funktionen angepasst,
    // muss immer nur einmal laufen!!
    private ProgConfigUpdate() {
    }

    public static void setUpdateDone() {
        ProgConfig.SYSTEM_CHANGE_LOG_DIR.setValue(true); // für Version 1
        ProgConfig.SYSTEM_RESET_COLOR.setValue(true); // Anpassung der Farben
        ProgConfig.SYSTEM_SET_DURATION_INT.setValue(true); // Ändern der Duration auf int
    }

    public static void update() {
        if (!ProgConfig.SYSTEM_CHANGE_LOG_DIR.getValue()) {
            // dann sind noch alte LogDir Einstellungen gespeichert
            final String logDir = ProgConfig.SYSTEM_LOG_DIR.getValueSafe();
            final String standardDir = ProgInfos.getStandardLogDirectory_String();
            if (logDir.equals(standardDir)) {
                // wenn eh der StandardPfad drin steht, dann löschen
                ProgConfig.SYSTEM_LOG_DIR.setValue("");
            }
        }

        if (!ProgConfig.SYSTEM_RESET_COLOR.getValue()) {
            // gibt neue Farben
            ProgColorList.resetAllColorDarkLight();

            ProgColorList.EPISODE_NEW_BG.setUse(false);
            ProgColorList.EPISODE_NEW.setUse(true);

            ProgColorList.EPISODE_STARTED_BG.setUse(true);
            ProgColorList.EPISODE_STARTED.setUse(false);

            ProgColorList.EPISODE_RUNNING_BG.setUse(true);
            ProgColorList.EPISODE_RUNNING.setUse(false);

            ProgColorList.EPISODE_ERROR_BG.setUse(true);
            ProgColorList.EPISODE_ERROR.setUse(false);

            ProgColorList.EPISODE_HISTORY_BG.setUse(true);
            ProgColorList.EPISODE_HISTORY.setUse(false);

            ProgColorList.DOWNLOAD_WAIT_BG.setUse(true);
            ProgColorList.DOWNLOAD_WAIT.setUse(false);

            ProgColorList.DOWNLOAD_RUN_BG.setUse(true);
            ProgColorList.DOWNLOAD_RUN.setUse(false);

            ProgColorList.DOWNLOAD_FINISHED_BG.setUse(true);
            ProgColorList.DOWNLOAD_FINISHED.setUse(false);

            ProgColorList.DOWNLOAD_ERROR_BG.setUse(true);
            ProgColorList.DOWNLOAD_ERROR.setUse(false);
        }

        if (!ProgConfig.SYSTEM_SET_DURATION_INT.getValue()) {
            ProgData.getInstance().downloadList.forEach(download -> {
                if (!download.getOldDuration().isEmpty()) {
                    download.durationIntProperty().set(DownloadFactory.getDuration(download.getOldDuration()));
                    download.setOldDuration("");
                }
            });
            ProgData.getInstance().episodeList.forEach(episode -> {
                if (!episode.getOldDuration().isEmpty()) {
                    episode.durationIntProperty().set(DownloadFactory.getDuration(episode.getOldDuration()));
                    episode.setOldDuration("");
                }
            });

        }

        setUpdateDone();
    }
}
