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

package de.p2tools.p2podder.controller.config;

public class ProgConst {

    public static final String PROGRAM_NAME = "P2Podder";
    public static final String USER_AGENT_DEFAULT = PROGRAM_NAME;
    public static final int MAX_USER_AGENT_SIZE = 100;
    public static final int SYSTEM_UPDATE_STATE = 0;

    // settings file
    public static final String CONFIG_FILE = "p2podder.xml";
    public static final String STYLE_FILE = "style.css";

    public static final String CONFIG_DIRECTORY = "p2Podder"; // im Homeverzeichnis
    public static final String XML_START = "P2Podder";

    public static final String LOG_DIR = "Log";
    public static final String POD_DEST_DIR = "Podcast";
    public static final String CSS_FILE = "de/p2tools/p2podder/podder.css";
    public static final String CSS_FILE_DARK_THEME = "de/p2tools/p2podder/podder-dark.css";

    public static final String FORMAT_ZIP = ".zip";


    // Website
    public static final String URL_WEBSITE = "https://www.p2tools.de/";
    public static final String URL_WEBSITE_DOWNLOAD = "https://www.p2tools.de/p2podder/download/";
    public static final String URL_WEBSITE_HELP = "https://www.p2tools.de/p2podder/manual/";

    // ProgrammUrls
    public static final String ADRESSE_WEBSITE_VLC = "http://www.videolan.org";

    public static final double GUI_FILTER_DIVIDER_LOCATION = 0.3;
    public static final double GUI_DIVIDER_LOCATION = 0.7;

    public static final double CONFIG_DIALOG_SET_DIVIDER = 0.2;

    public static final int MIN_TABLE_HEIGHT = 200;
    public static final int MIN_TEXTAREA_HEIGHT_LOW = 50;

    // minimale Größe (256 kB) eines Films um nicht als Fehler zu gelten
    public static final int MIN_DATEI_GROESSE_FILM = 256 * 1000;
    // es können maximal so viele Downloads gleichzeitig geladen werden
    public static final int MAX_DOWNLOADS_LADEN = 2;

    public static final String FILE_FINISHED_DOWNLOADS = "downloads.txt";
    public static final String FILE_PLAYED_EPISODES = "episodes.txt";

    public static final String FILE_PROG_ICON = "/de/p2tools/p2podder/res/P2.png";

    public static final int LAST_TAB_STATION_EPISODE = 0;
    public static final int LAST_TAB_STATION_PODCAST = 1;
    public static final int LAST_TAB_STATION_DOWNLOAD = 2;

    public static final int FILTER_TIME_RANGE_ALL_VALUE = 0;
    public static final int FILTER_TIME_RANGE_MIN_VALUE = 0;
    public static final int FILTER_TIME_RANGE_MAX_VALUE = 50;

    public static final int TABLE_SMALL_BUTTON = 18;
    public static final int EPISODE_FILTER_MAX_BACKWARD_SIZE = 10;
}
