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


import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.configFile.pConfData.PColorList;
import de.p2tools.p2Lib.configFile.pData.PData;
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    private ProgColorList() {
        super();
    }

    //Tabelle Episodes
    public static final PColorData EPISODE_NEW = addNewKey("COLOR__EPISODE_NEW",
            Color.rgb(0, 0, 240),
            Color.rgb(0, 0, 240), "Tabelle Episoden, neue");

    public static final PColorData EPISODE_RUN = addNewKey("COLOR__EPISODE_RUN",
            Color.rgb(255, 245, 176),
            Color.rgb(174, 150, 85), "Tabelle Episoden, läuft");

    public static final PColorData EPISODE_ERROR = addNewKey("COLOR__EPISODE_ERROR",
            Color.rgb(255, 233, 233),
            Color.rgb(163, 82, 82), "Tabelle Episoden, fehlerhaft");

    public static final PColorData EPISODE_HISTORY = addNewKey("COLOR__EPISODE_HISTORY",
            Color.rgb(223, 223, 223),
            Color.rgb(100, 100, 100), "Tabelle Episoden, bereits gehört");

    //Tabelle Downloads
    public static final PColorData DOWNLOAD_WAIT = addNewKey("COLOR__DOWNLOAD_WAIT",
            Color.rgb(239, 244, 255),
            Color.rgb(99, 100, 105), "Tabelle Download, noch nicht gestartet");

    public static final PColorData DOWNLOAD_RUN = addNewKey("COLOR__DOWNLOAD_RUN",
            Color.rgb(255, 245, 176),
            Color.rgb(174, 150, 85), "Tabelle Download, läuft");

    public static final PColorData DOWNLOAD_FINISHED = addNewKey("COLOR__DOWNLOAD_FINISHED",
            Color.rgb(206, 255, 202),
            Color.rgb(79, 129, 74), "Tabelle Download, fertig");

    public static final PColorData DOWNLOAD_ERROR = addNewKey("COLOR__DOWNLOAD_ERROR",
            Color.rgb(255, 233, 233),
            Color.rgb(163, 82, 82), "Tabelle Download, fehlerhaft");

    // DialogPodcast
    public static final PColorData PODCAST_NAME_ERROR = addNewKey("COLOR_PODCAST_NAME_ERROR",
            Color.rgb(255, 233, 233), Color.rgb(200, 183, 183), "Podcast, Name ist fehlerhaft");

    // Filter wenn RegEx
    public static final PColorData FILTER_REGEX = addNewKey("COLOR_FILTER_REGEX",
            Color.rgb(225, 255, 225), Color.rgb(128, 179, 213), "Filter ist RegEx");
    public static final PColorData FILTER_REGEX_ERROR = addNewKey("COLOR_FILTER_REGEX_ERROR",
            Color.rgb(255, 230, 230), Color.rgb(170, 0, 0), "Filter ist Regex, fehlerhaft");
    
    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getColorList().size(); ++i) {
            getColorList().get(i).setColorTheme(dark);
        }
    }

    public static PData getConfigsData() {
        return PColorList.getPData();
    }
}
