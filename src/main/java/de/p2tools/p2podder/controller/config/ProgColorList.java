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


package de.p2tools.p2podder.controller.config;


import de.p2tools.p2lib.data.P2ColorData;
import de.p2tools.p2lib.data.P2ColorList;
import javafx.scene.paint.Color;

public class ProgColorList extends P2ColorList {

    private ProgColorList() {
        super();
    }

    //Tabelle Episodes
    public static final P2ColorData EPISODE_NEW_BG = addNewKey("COLOR__EPISODE_NEW_BG",
            Color.rgb(210, 210, 255),
            Color.rgb(210, 210, 255),
            false, "Neue Episode, Tabellenzeile");
    public static final P2ColorData EPISODE_NEW = addNewKey("COLOR__EPISODE_NEW",
            Color.rgb(0, 0, 240),
            Color.rgb(0, 0, 240),
            true, "Neue Episode, Schriftfarbe");

    public static final P2ColorData EPISODE_STARTED_BG = addNewKey("COLOR__EPISODE_STARTED_BG",
            Color.rgb(136, 202, 136),
            Color.rgb(136, 202, 136),
            true, "Gestartete Episode, Tabellenzeile");
    public static final P2ColorData EPISODE_STARTED = addNewKey("COLOR__EPISODE_STARTED",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Gestartete Episode, Schriftfarbe");

    public static final P2ColorData EPISODE_RUNNING_BG = addNewKey("COLOR__EPISODE_RUNNING_BG",
            Color.rgb(198, 255, 198),
            Color.rgb(198, 255, 198),
            true, "Episode läuft, Tabellenzeile");
    public static final P2ColorData EPISODE_RUNNING = addNewKey("COLOR__EPISODE_RUNNING",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Episode läuft, Schriftfarbe");

    public static final P2ColorData EPISODE_ERROR_BG = addNewKey("COLOR__EPISODE_ERROR_BG",
            Color.rgb(163, 82, 82),
            Color.rgb(163, 82, 82),
            true, "Episode ist fehlerhaft, Tabellenzeile");
    public static final P2ColorData EPISODE_ERROR = addNewKey("COLOR__EPISODE_ERROR",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Episode ist fehlerhaft, Schriftfarbe");


    public static final P2ColorData EPISODE_HISTORY_BG = addNewKey("COLOR__EPISODE_HISTORY_BG",
            Color.rgb(223, 223, 223),
            Color.rgb(100, 100, 100),
            true, "Episode wurde bereits gehört, Tabellenzeile");
    public static final P2ColorData EPISODE_HISTORY = addNewKey("COLOR__EPISODE_HISTORY",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Episode wurde bereits gehört, Schriftfarbe");

    //Tabelle Downloads
    public static final P2ColorData DOWNLOAD_WAIT_BG = addNewKey("COLOR__DOWNLOAD_WAIT_BG",
            Color.rgb(223, 223, 223),
            Color.rgb(100, 100, 100),
            true, "Download noch nicht gestartet, Tabellenzeile");
    public static final P2ColorData DOWNLOAD_WAIT = addNewKey("COLOR__DOWNLOAD_WAIT",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Download noch nicht gestartet, Schriftfarbe");

    public static final P2ColorData DOWNLOAD_RUN_BG = addNewKey("COLOR__DOWNLOAD_RUN_BG",
            Color.rgb(255, 245, 176),
            Color.rgb(174, 150, 85),
            "Download läuft, Tabellenzeile");
    public static final P2ColorData DOWNLOAD_RUN = addNewKey("COLOR__DOWNLOAD_RUN",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false,
            "Download läuft, Schriftfarbe");

    public static final P2ColorData DOWNLOAD_FINISHED_BG = addNewKey("COLOR__DOWNLOAD_FINISHED_BG",
            Color.rgb(198, 255, 198),
            Color.rgb(145, 181, 145),
            true, "Download ist fertig, Tabellenzeile");
    public static final P2ColorData DOWNLOAD_FINISHED = addNewKey("COLOR__DOWNLOAD_FINISHED",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Download ist fertig, Schriftfarbe");

    public static final P2ColorData DOWNLOAD_ERROR_BG = addNewKey("COLOR__DOWNLOAD_ERROR_BG",
            Color.rgb(163, 82, 82),
            Color.rgb(163, 82, 82),
            true, "Download ist fehlerhaft, Tabellenzeile");
    public static final P2ColorData DOWNLOAD_ERROR = addNewKey("COLOR__DOWNLOAD_ERROR",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Download ist fehlerhaft, Schriftfarbe");


    public synchronized static P2ColorList getInstance() {
        return P2ColorList.getInst();
    }

    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getInstance().size(); ++i) {
            getInstance().get(i).setColorTheme(dark);
        }
    }
}
