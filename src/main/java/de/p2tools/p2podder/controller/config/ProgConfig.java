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

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.tools.MLBandwidthTokenBucket;
import javafx.beans.property.*;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static ProgConfig instance;
    private static final ArrayList<Config> arrayList = new ArrayList<>();
    public static final String SYSTEM = "system";

    // Programm-Configs, änderbar nur im Config-File
    // ============================================
    // Downloadfehlermeldung wird xx Sedunden lang angezeigt
    public static IntegerProperty SYSTEM_PARAMETER_START_STATION_ERRORMSG_IN_SECOND = addInt("__system-parameter__download-errormsg-in-second_30__", 30);
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART = addInt("__system-parameter__download-max-restart_5__", 3);
    public static BooleanProperty DOWNLOAD_MAX_ONE_PER_SERVER = addBool("download-max-one-per-server"); // nur ein Download pro Server - sonst max 2
    // 250 Sekunden, wie bei Firefox
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_TIMEOUT_SECOND = addInt("__system-parameter__download-timeout-second_250__", 250);
    // max. Startversuche für fehlgeschlagene Downloads, direkt beim Download
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART_HTTP = addInt("__system-parameter__download-max-restart-http_10__", 5);


    // ===========================================
    //Filter Episoden
    public static IntegerProperty FILTER_EPISODE_SEL_FILTER = addInt("filter-episode-sel-filter");


    // Configs der Programmversion
    public static StringProperty SYSTEM_PROG_VERSION = addStr("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStr("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStr("system-prog-build-date");
    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStr("system-download-dir-new-version", "");
    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStr("system-prog-open-dir", "");


    // Configs zum Aktualisieren beim Programmupdate
    public static IntegerProperty SYSTEM_UPDATE_STATE = addInt("system-update-state", 0);

    // Configs zur Programmupdatesuche
    public static StringProperty SYSTEM_UPDATE_DATE = addStr("system-update-date"); // Datum der letzten Prüfung
    public static StringProperty SYSTEM_UPDATE_PROGSET_VERSION = addStr("system-update-progset-version");

    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBool("system-update-search-act", true); //Infos und Programm
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBool("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBool("system-update-search-daily", false); //daily suchen

    public static StringProperty SYSTEM_UPDATE_LAST_INFO = addStr("system-update-last-info");
    public static StringProperty SYSTEM_UPDATE_LAST_ACT = addStr("system-update-last-act");
    public static StringProperty SYSTEM_UPDATE_LAST_BETA = addStr("system-update-last-beta");
    public static StringProperty SYSTEM_UPDATE_LAST_DAILY = addStr("system-update-last-daily");

    // ConfigDialog, Dialog nach Start immer gleich öffnen
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_POD = new SimpleIntegerProperty(-1);

    // Configs
    public static BooleanProperty SYSTEM_TRAY_USE_OWN_ICON = addBool("system-tray-own-icon", Boolean.FALSE);
    public static StringProperty SYSTEM_TRAY_ICON_PATH = addStr("system-tray-icon", ""); //ein eigenes Tray-Icon
    public static BooleanProperty SYSTEM_TRAY = addBool("system-tray", Boolean.TRUE);
    public static StringProperty SYSTEM_USERAGENT = addStr("system-useragent", ProgConst.USER_AGENT_DEFAULT);    // Useragent für direkte Downloads
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStr("system-prog-open-url");
    public static BooleanProperty SYSTEM_STYLE = addBool("system-style", Boolean.FALSE);
    public static IntegerProperty SYSTEM_STYLE_SIZE = addInt("system-style-size", 14);
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");
    public static StringProperty SYSTEM_POD_DIR = addStr("system-pod-dir", "");
    public static BooleanProperty SYSTEM_LOG_ON = addBool("system-log-on", Boolean.TRUE);
    public static StringProperty SYSTEM_UPDATE_PODCAST_DATE = addStr("system-update-podcast-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_PODCAST_DAILY = addBool("system-update-podcast-daily", Boolean.TRUE);
    public static BooleanProperty SYSTEM_START_DAILY_DOWNLOAD = addBool("system-start-daily-download", Boolean.TRUE);
    public static BooleanProperty SYSTEM_SMALL_BUTTON_TABLE_ROW = addBool("system-small-button-table-row", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DARK_THEME = addBool("system-dark-theme", Boolean.FALSE);
    public static BooleanProperty SYSTEM_THEME_CHANGED = addBool("system-theme-changed");
    public static IntegerProperty SYSTEM_LAST_TAB = addInt("system-last-tab", 0);
    public static BooleanProperty SYSTEM_DELETE_EPISODE_FILE_ASK = addBool("system-delete-episode-file-ask", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DELETE_EPISODE_FILE = addBool("system-delete-episode-file", Boolean.TRUE);

    // Fenstereinstellungen
    public static StringProperty SYSTEM_SIZE_GUI = addStr("system-size-gui", "1000:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_INFO = addStr("system-size-dialog-episode-info", "600:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL = addStr("system-size-dialog-episode-info-small", "600:300");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_DEL = addStr("system-size-dialog-episode-del");
    public static StringProperty SYSTEM_SIZE_DIALOG_DOWNLOAD_DEL = addStr("system-size-dialog-download-del");

    //Gui SmallPodder
    public static BooleanProperty SYSTEM_SMALL_PODDER = addBool("system-small-podder", false);
    public static StringProperty SMALL_PODDER_SIZE = addStr("small-podder-size");


    public static StringProperty SYSTEM_PATH_VLC = addStr("system-path-vlc", SetFactory.getTemplatePathVlc());

    // Download
    public static BooleanProperty DOWNLOAD_BEEP = addBool("download-beep");
    public static IntegerProperty DOWNLOAD_MAX_BANDWIDTH_KBYTE = addInt("download-max-bandwidth-kbyte", MLBandwidthTokenBucket.BANDWIDTH_MAX_KBYTE);
    public static BooleanProperty SYSTEM_SSL_ALWAYS_TRUE = addBool("system-ssl-always-true");

    // Gui Podcast
    public static DoubleProperty PODCAST_GUI_FILTER_DIVIDER = addDouble("podcast-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty PODCAST_GUI_FILTER_ON = addBool("podcast-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty PODCAST_GUI_DIVIDER = addDouble("podcast-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    public static BooleanProperty PODCAST_GUI_INFO_ON = addBool("podcast-gui-info-on", Boolean.TRUE);
    public static StringProperty PODCAST_GUI_TABLE_WIDTH = addStr("podcast-gui-table-width");
    public static StringProperty PODCAST_GUI_TABLE_SORT = addStr("podcast-gui-table-sort");
    public static StringProperty PODCAST_GUI_TABLE_UP_DOWN = addStr("podcast-gui-table-up-down");
    public static StringProperty PODCAST_GUI_TABLE_VIS = addStr("podcast-gui-table-vis");
    public static StringProperty PODCAST_GUI_TABLE_ORDER = addStr("podcast-gui-table-order");
    public static StringProperty PODCAST_EDIT_DIALOG_SIZE = addStr("podcast-edit-dialog-size", "600:800");

    //Gui Download
    public static DoubleProperty DOWNLOAD_GUI_FILTER_DIVIDER = addDouble("download-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty DOWNLOAD_GUI_FILTER_ON = addBool("download-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty DOWNLOAD_GUI_DIVIDER = addDouble("download-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    public static BooleanProperty DOWNLOAD_GUI_INFO_ON = addBool("download-gui-info-on", Boolean.TRUE);
    public static StringProperty DOWNLOAD_GUI_TABLE_WIDTH = addStr("download-gui-table-width");
    public static StringProperty DOWNLOAD_GUI_TABLE_SORT = addStr("download-gui-table-sort");
    public static StringProperty DOWNLOAD_GUI_TABLE_UP_DOWN = addStr("download-gui-table-up-down");
    public static StringProperty DOWNLOAD_GUI_TABLE_VIS = addStr("download-gui-table-vis");
    public static StringProperty DOWNLOAD_GUI_TABLE_ORDER = addStr("download-gui-table-order");

    //Gui Episodes
    public static DoubleProperty EPISODE_GUI_FILTER_DIVIDER = addDouble("episode-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty EPISODE_GUI_FILTER_ON = addBool("episode-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty EPISODE_GUI_DIVIDER = addDouble("episode-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    public static BooleanProperty EPISODE_GUI_DIVIDER_ON = addBool("episode-gui-divider-on", Boolean.TRUE);
    public static StringProperty EPISODE_DIALOG_EDIT_SIZE = addStr("episode-dialog-edit-size", "800:800");
    public static StringProperty EPISODE_DIALOG_ADD_SIZE = addStr("episode-dialog-add-size", "800:800");
    public static StringProperty START_STATION_ERROR_DIALOG_SIZE = addStr("start-station-error-dialog-size", "");
    public static StringProperty EPISODE_GUI_TABLE_WIDTH = addStr("episode-gui-table-width");
    public static StringProperty EPISODE_GUI_TABLE_SORT = addStr("episode-gui-table-sort");
    public static StringProperty EPISODE_GUI_TABLE_UP_DOWN = addStr("episode-gui-table-up-down");
    public static StringProperty EPISODE_GUI_TABLE_VIS = addStr("episode-gui-table-vis");
    public static StringProperty EPISODE_GUI_TABLE_ORDER = addStr("episode-gui-table-order");
    public static BooleanProperty EPISODE_SHOW_NOTIFICATION = addBool("episode-show-notification", Boolean.TRUE);

    //Gui SmallEpisode
    public static DoubleProperty SMALL_EPISODE_GUI_FILTER_DIVIDER = addDouble("small-episode-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static StringProperty SMALL_EPISODE_GUI_TABLE_WIDTH = addStr("small-episode-gui-table-width");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_SORT = addStr("small-episode-gui-table-sort");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_UP_DOWN = addStr("small-episode-gui-table-up-down");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_VIS = addStr("small-episode-gui-table-vis");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_ORDER = addStr("small-episode-gui-table-order");
    public static BooleanProperty SMALL_EPISODE_GUI_FILTER_ON = addBool("small-episode-gui-filter-on", Boolean.TRUE);

    // ConfigDialog
    public static StringProperty CONFIG_DIALOG_SIZE = addStr("config-dialog-size");
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBool("config_dialog-accordion", Boolean.TRUE);
    public static DoubleProperty CONFIG_DIALOG_SET_DIVIDER = addDouble("config-dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static StringProperty CONFIG_DIALOG_IMPORT_SET_SIZE = addStr("config-dialog-import-set-size", "800:700");
    public static DoubleProperty CONFIG_DIALOG_SHORTCUT_DIVIDER = addDouble("config-dialog-shortcut-divider", 0.1);

    //StartDialog
    public static StringProperty START_DIALOG_DOWNLOAD_PATH = addStr("start-dialog-download-path", PSystemUtils.getStandardDownloadPath());

    //EpisodeInfoDialog
    public static BooleanProperty EPISODE_INFO_DIALOG_SHOW_BIG = addBool("episode-info-dialog-show-big", Boolean.TRUE);

    //Shorcuts Hauptmenü
    public static final String SHORTCUT_QUIT_PROGRAM_INIT = "Ctrl+Q";
    public static StringProperty SHORTCUT_QUIT_PROGRAM = addStr("SHORTCUT_QUIT_PROGRAM", SHORTCUT_QUIT_PROGRAM_INIT);

    //Shortcuts Podcastmenü
    public static final String SHORTCUT_UPDATE_PODCAST_INIT = "Ctrl+P";
    public static StringProperty SHORTCUT_UPDATE_PODCAST = addStr("SHORTCUT_PLAY_STATION", SHORTCUT_UPDATE_PODCAST_INIT);
    public static final String SHORTCUT_SAVE_PODCAST_INIT = "Ctrl+S";
    public static StringProperty SHORTCUT_SAVE_PODCAST = addStr("SHORTCUT_SAVE_STATION", SHORTCUT_SAVE_PODCAST_INIT);

    // Shortcuts Episodenmenü
    public static final String SHORTCUT_EPISODE_START_INIT = "Ctrl+F";
    public static StringProperty SHORTCUT_EPISODE_START = addStr("SHORTCUT_EPISODE_START", SHORTCUT_EPISODE_START_INIT);
    public static final String SHORTCUT_EPISODE_STOP_INIT = "Ctrl+T";
    public static StringProperty SHORTCUT_EPISODE_STOP = addStr("SHORTCUT_EPISODE_STOP", SHORTCUT_EPISODE_STOP_INIT);
    public static final String SHORTCUT_EPISODE_CHANGE_INIT = "Ctrl+C";
    public static StringProperty SHORTCUT_EPISODE_CHANGE = addStr("SHORTCUT_EPISODE_CHANGE", SHORTCUT_EPISODE_CHANGE_INIT);

    public static IntegerProperty DOWNLOAD_MAX_DOWNLOADS = addInt("download-max-downloads", 1);


    private ProgConfig() {
        super(arrayList, "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        ProgConfig.SYSTEM_PROG_VERSION.set(ProgramTools.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(ProgramTools.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(ProgramTools.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getConfigsData());
        configFile.addConfigs(ProgData.getInstance().setDataList);
        configFile.addConfigs(ProgData.getInstance().podcastList);
        configFile.addConfigs(ProgData.getInstance().downloadList);
        configFile.addConfigs(ProgData.getInstance().episodeList);
    }

    public static void getConfigLog(ArrayList<String> list) {
        list.add(PLog.LILNE2);
        list.add("Programmeinstellungen");
        list.add("===========================");
        arrayList.stream().forEach(c -> {
            String s = c.getKey();
            if (s.startsWith("_")) {
                while (s.length() < 55) {
                    s += " ";
                }
            } else {
                while (s.length() < 35) {
                    s += " ";
                }
            }

            list.add(s + "  " + c.getActValueString());
        });
    }

    private static StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private static StringProperty addStrC(String comment, String key) {
        return addStrPropC(comment, arrayList, key);
    }

    private static StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private static StringProperty addStrC(String comment, String key, String init) {
        return addStrPropC(comment, arrayList, key, init);
    }

    private static DoubleProperty addDouble(String key, double init) {
        return addDoubleProp(arrayList, key, init);
    }

    private static DoubleProperty addDoubleC(String comment, String key, double init) {
        return addDoublePropC(comment, arrayList, key, init);
    }

    private static IntegerProperty addInt(String key) {
        return addIntProp(arrayList, key, 0);
    }

    private static IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private static IntegerProperty addIntC(String comment, String key, int init) {
        return addIntPropC(comment, arrayList, key, init);
    }

    private static LongProperty addLong(String key) {
        return addLongProp(arrayList, key, 0);
    }

    private static LongProperty addLong(String key, long init) {
        return addLongProp(arrayList, key, init);
    }

    private static LongProperty addLongC(String comment, String key, long init) {
        return addLongPropC(comment, arrayList, key, init);
    }

    private static BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key) {
        return addBoolProp(arrayList, key, Boolean.FALSE);
    }

    private static BooleanProperty addBoolC(String comment, String key, boolean init) {
        return addBoolPropC(comment, arrayList, key, init);
    }
}
