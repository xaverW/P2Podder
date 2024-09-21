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

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.data.P2DataProgConfig;
import de.p2tools.p2lib.tools.P2SystemUtils;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2LDateTimeProperty;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.tools.MLBandwidthTokenBucket;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProgConfig extends P2DataProgConfig {

    private static ProgConfig instance;
    private static final ArrayList<Config> arrayList = new ArrayList<>();
    public static final String SYSTEM = "system";

    // Programm-Configs, änderbar nur im Config-File
    // ============================================
    // Downloadfehlermeldung wird xx Sedunden lang angezeigt
    public static IntegerProperty SYSTEM_PARAMETER_START_STATION_ERRORMSG_IN_SECOND = addIntProp("__system-parameter__download-errormsg-in-second_30__", 30);
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART = addIntProp("__system-parameter__download-max-restart_5__", 3);
    public static BooleanProperty DOWNLOAD_MAX_ONE_PER_SERVER = addBoolProp("download-max-one-per-server"); // nur ein Download pro Server - sonst max 2
    // 250 Sekunden, wie bei Firefox
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_TIMEOUT_SECOND = addIntProp("__system-parameter__download-timeout-second_250__", 250);
    // max. Startversuche für fehlgeschlagene Downloads, direkt beim Download
    public static IntegerProperty SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART_HTTP = addIntProp("__system-parameter__download-max-restart-http_10__", 5);


    // ===========================================
    //Filter Episoden
    public static IntegerProperty FILTER_EPISODE_SEL_FILTER = addIntProp("filter-episode-sel-filter");

    // Configs zum Aktualisieren beim Programmupdate
    public static BooleanProperty SYSTEM_CHANGE_LOG_DIR = addBoolProp("system-change-log-dir", Boolean.FALSE);


    // Configs der Programmversion
    public static StringProperty SYSTEM_PROG_VERSION = addStrProp("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStrProp("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStrProp("system-prog-build-date");
    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStrProp("system-download-dir-new-version", "");
    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");


    // Configs zur Programmupdatesuche
    public static StringProperty SYSTEM_UPDATE_DATE = addStrProp("system-update-date"); // Datum der letzten Prüfung
    public static StringProperty SYSTEM_UPDATE_PROGSET_VERSION = addStrProp("system-update-progset-version");

    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBoolProp("system-update-search-act", true); //Infos und Programm
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBoolProp("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBoolProp("system-update-search-daily", false); //daily suchen

    public static StringProperty SYSTEM_UPDATE_LAST_INFO = addStrProp("system-update-last-info");
    public static StringProperty SYSTEM_UPDATE_LAST_ACT = addStrProp("system-update-last-act");
    public static StringProperty SYSTEM_UPDATE_LAST_BETA = addStrProp("system-update-last-beta");
    public static StringProperty SYSTEM_UPDATE_LAST_DAILY = addStrProp("system-update-last-daily");

    // ConfigDialog, Dialog nach Start immer gleich öffnen
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_POD = new SimpleIntegerProperty(-1);

    // Configs
    public static BooleanProperty SYSTEM_TRAY_USE_OWN_ICON = addBoolProp("system-tray-own-icon", Boolean.FALSE);
    public static StringProperty SYSTEM_TRAY_ICON_PATH = addStrProp("system-tray-icon", ""); //ein eigenes Tray-Icon
    public static BooleanProperty SYSTEM_TRAY = addBoolProp("system-tray", Boolean.TRUE);
    public static StringProperty SYSTEM_USERAGENT = addStrProp("system-useragent", ProgConst.USER_AGENT_DEFAULT);    // Useragent für direkte Downloads
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-url");
    public static BooleanProperty SYSTEM_STYLE = addBoolProp("system-style", Boolean.FALSE);
    public static IntegerProperty SYSTEM_STYLE_SIZE = addIntProp("system-style-size", 14);
    public static StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");
    public static StringProperty SYSTEM_POD_DIR = addStrProp("system-pod-dir", "");
    public static BooleanProperty SYSTEM_LOG_ON = addBoolProp("system-log-on", Boolean.TRUE);
    public static StringProperty SYSTEM_UPDATE_PODCAST_DATE = addStrProp("system-update-podcast-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_PODCAST_DAILY = addBoolProp("system-update-podcast-daily", Boolean.FALSE);
    public static BooleanProperty SYSTEM_START_DAILY_DOWNLOAD = addBoolProp("system-start-daily-download", Boolean.FALSE);
    public static BooleanProperty SYSTEM_SMALL_BUTTON_TABLE_ROW = addBoolProp("system-small-button-table-row", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DARK_THEME = addBoolProp("system-dark-theme", Boolean.FALSE);
    public static BooleanProperty SYSTEM_THEME_CHANGED = addBoolProp("system-theme-changed");
    public static IntegerProperty SYSTEM_LAST_TAB = addIntProp("system-last-tab", 0);
    public static BooleanProperty SYSTEM_DELETE_EPISODE_FILE_ASK = addBoolProp("system-delete-episode-file-ask", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DELETE_EPISODE_FILE = addBoolProp("system-delete-episode-file", Boolean.TRUE);

    //Meta-Daten
    public static P2LDateTimeProperty META_PODCAST_LIST_DATE = addPLocalDateTimeProp("meta-podcast-list-date", LocalDateTime.MIN);

    // Fenstereinstellungen
    public static StringProperty SYSTEM_SIZE_GUI = addStrProp("system-size-gui", "1000:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_INFO = addStrProp("system-size-dialog-episode-info", "600:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL = addStrProp("system-size-dialog-episode-info-small", "600:300");
    public static StringProperty SYSTEM_SIZE_DIALOG_EPISODE_DEL = addStrProp("system-size-dialog-episode-del");
    public static StringProperty SYSTEM_SIZE_DIALOG_DOWNLOAD_DEL = addStrProp("system-size-dialog-download-del");

    //Gui SmallPodder
    public static BooleanProperty SYSTEM_SMALL_PODDER = addBoolProp("system-small-podder", false);
    public static StringProperty SMALL_PODDER_SIZE = addStrProp("small-podder-size", "800:400");


    public static StringProperty SYSTEM_PATH_VLC = addStrProp("system-path-vlc", SetFactory.getTemplatePathVlc());

    // Download
    public static BooleanProperty DOWNLOAD_BEEP = addBoolProp("download-beep");
    public static IntegerProperty DOWNLOAD_MAX_BANDWIDTH_KBYTE = addIntProp("download-max-bandwidth-kbyte", MLBandwidthTokenBucket.BANDWIDTH_MAX_KBYTE);
    public static BooleanProperty SYSTEM_SSL_ALWAYS_TRUE = addBoolProp("system-ssl-always-true");

    // Gui Podcast
//    public static DoubleProperty PODCAST_GUI_FILTER_DIVIDER = addDoubleProp("podcast-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
//    public static BooleanProperty PODCAST_GUI_FILTER_ON = addBoolProp("podcast-gui-filter-on", Boolean.TRUE);
//    public static DoubleProperty PODCAST_GUI_DIVIDER = addDoubleProp("podcast-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    //    public static BooleanProperty PODCAST_GUI_INFO_ON = addBoolProp("podcast-gui-info-on", Boolean.TRUE);
    public static StringProperty PODCAST_GUI_TABLE_WIDTH = addStrProp("podcast-gui-table-width");
    public static StringProperty PODCAST_GUI_TABLE_SORT = addStrProp("podcast-gui-table-sort");
    public static StringProperty PODCAST_GUI_TABLE_UP_DOWN = addStrProp("podcast-gui-table-up-down");
    public static StringProperty PODCAST_GUI_TABLE_VIS = addStrProp("podcast-gui-table-vis");
    public static StringProperty PODCAST_GUI_TABLE_ORDER = addStrProp("podcast-gui-table-order");
    public static StringProperty PODCAST_EDIT_DIALOG_SIZE = addStrProp("podcast-edit-dialog-size", "600:800");
    public static DoubleProperty PODCAST_GUI_INFO_DIVIDER = addDoubleProp("podcast-gui-info-divider", 0.7);
    public static StringProperty PODCAST_DIALOG_ADD_SIZE = addStrProp("podcast-dialog-add-size", "700:700");
    public static StringProperty PODCAST_DIALOG_ADD_MORE_SIZE = addStrProp("podcast-dialog-add-more-size", "800:700");

    //Gui Download
    public static DoubleProperty DOWNLOAD_GUI_FILTER_DIVIDER = addDoubleProp("download-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty DOWNLOAD_GUI_FILTER_ON = addBoolProp("download-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty DOWNLOAD_GUI_DIVIDER = addDoubleProp("download-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    //    public static BooleanProperty DOWNLOAD_GUI_INFO_ON = addBoolProp("download-gui-info-on", Boolean.TRUE);
    public static StringProperty DOWNLOAD_GUI_TABLE_WIDTH = addStrProp("download-gui-table-width");
    public static StringProperty DOWNLOAD_GUI_TABLE_SORT = addStrProp("download-gui-table-sort");
    public static StringProperty DOWNLOAD_GUI_TABLE_UP_DOWN = addStrProp("download-gui-table-up-down");
    public static StringProperty DOWNLOAD_GUI_TABLE_VIS = addStrProp("download-gui-table-vis");
    public static StringProperty DOWNLOAD_GUI_TABLE_ORDER = addStrProp("download-gui-table-order");
    public static DoubleProperty DOWNLOAD_GUI_INFO_DIVIDER = addDoubleProp("download-gui-info-divider", 0.7);

    //Gui Episodes
    public static DoubleProperty EPISODE_GUI_FILTER_DIVIDER = addDoubleProp("episode-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty EPISODE_GUI_FILTER_ON = addBoolProp("episode-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty EPISODE_GUI_DIVIDER = addDoubleProp("episode-gui-divider", ProgConst.GUI_DIVIDER_LOCATION);
    //    public static BooleanProperty EPISODE_GUI_DIVIDER_ON = addBoolProp("episode-gui-divider-on", Boolean.TRUE);
    public static StringProperty EPISODE_DIALOG_EDIT_SIZE = addStrProp("episode-dialog-edit-size", "800:800");
    public static StringProperty START_STATION_ERROR_DIALOG_SIZE = addStrProp("start-station-error-dialog-size", "");
    public static StringProperty EPISODE_GUI_TABLE_WIDTH = addStrProp("episode-gui-table-width");
    public static StringProperty EPISODE_GUI_TABLE_SORT = addStrProp("episode-gui-table-sort");
    public static StringProperty EPISODE_GUI_TABLE_UP_DOWN = addStrProp("episode-gui-table-up-down");
    public static StringProperty EPISODE_GUI_TABLE_VIS = addStrProp("episode-gui-table-vis");
    public static StringProperty EPISODE_GUI_TABLE_ORDER = addStrProp("episode-gui-table-order");
    public static BooleanProperty EPISODE_SHOW_NOTIFICATION = addBoolProp("episode-show-notification", Boolean.TRUE);
    public static DoubleProperty EPISODE_GUI_INFO_DIVIDER = addDoubleProp("episode-gui-info-divider", 0.7);

    //Gui SmallEpisode
    public static BooleanProperty SMALL_EPISODE_GUI_FILTER_ON = addBoolProp("small-episode-gui-filter-on", Boolean.TRUE);
    public static DoubleProperty SMALL_EPISODE_GUI_FILTER_DIVIDER = addDoubleProp("small-episode-gui-filter-divider", 0.2);
    public static StringProperty SMALL_EPISODE_GUI_TABLE_WIDTH = addStrProp("small-episode-gui-table-width");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_SORT = addStrProp("small-episode-gui-table-sort");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_UP_DOWN = addStrProp("small-episode-gui-table-up-down");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_VIS = addStrProp("small-episode-gui-table-vis");
    public static StringProperty SMALL_EPISODE_GUI_TABLE_ORDER = addStrProp("small-episode-gui-table-order");

    // Download Info
    public static BooleanProperty DOWNLOAD__INFO_IS_SHOWING = addBoolProp("download--info-is-showing", Boolean.TRUE);
    public static BooleanProperty DOWNLOAD__PANE_INFO_IS_RIP = addBoolProp("download--pane-info-is-rip", Boolean.FALSE);
    public static StringProperty DOWNLOAD__DIALOG_INFO_SIZE = addStrProp("download--dialog-info-size", "400:400");
    public static DoubleProperty DOWNLOAD__INFO_DIVIDER = addDoubleProp("download--info-divider", ProgConst.GUI_DIVIDER_LOCATION);

    public static DoubleProperty DOWNLOAD__FILTER_DIVIDER = addDoubleProp("download--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty DOWNLOAD__FILTER_IS_SHOWING = addBoolProp("download--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty DOWNLOAD__FILTER_IS_RIP = addBoolProp("download--filter-is-rip", Boolean.FALSE);
    public static StringProperty DOWNLOAD__FILTER_DIALOG_SIZE = addStrProp("download--filter-dialog-size", "400:600");


    // Episode Info
    public static BooleanProperty EPISODE__INFO_IS_SHOWING = addBoolProp("episode--info-is-showing", Boolean.TRUE);
    public static BooleanProperty EPISODE__PANE_INFO_IS_RIP = addBoolProp("episode--pane-info-is-rip", Boolean.FALSE);
    public static StringProperty EPISODE__DIALOG_INFO_SIZE = addStrProp("episode--dialog-info-size", "400:400");
    public static DoubleProperty EPISODE__INFO_DIVIDER = addDoubleProp("episode--info-divider", ProgConst.GUI_DIVIDER_LOCATION);

    public static DoubleProperty EPISODE__FILTER_DIVIDER = addDoubleProp("episode--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty EPISODE__FILTER_IS_SHOWING = addBoolProp("episode--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty EPISODE__FILTER_IS_RIP = addBoolProp("episode--filter-is-rip", Boolean.FALSE);
    public static StringProperty EPISODE__FILTER_DIALOG_SIZE = addStrProp("episode--filter-dialog-size", "400:600");

    // Podcast Info
    public static BooleanProperty PODCAST__INFO_IS_SHOWING = addBoolProp("podcast--info-is-showing", Boolean.TRUE);
    public static BooleanProperty PODCAST__PANE_INFO_IS_RIP = addBoolProp("podcast--pane-info-is-rip", Boolean.FALSE);
    public static StringProperty PODCAST__DIALOG_INFO_SIZE = addStrProp("podcast--dialog-info-size", "400:400");
    public static DoubleProperty PODCAST__INFO_DIVIDER = addDoubleProp("podcast--info-divider", ProgConst.GUI_DIVIDER_LOCATION);

    public static DoubleProperty PODCAST__FILTER_DIVIDER = addDoubleProp("podcast--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty PODCAST__FILTER_IS_SHOWING = addBoolProp("podcast--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty PODCAST__FILTER_IS_RIP = addBoolProp("podcast--filter-is-rip", Boolean.FALSE);
    public static StringProperty PODCAST__FILTER_DIALOG_SIZE = addStrProp("podcast--filter-dialog-size", "400:600");


    // ConfigDialog
    public static StringProperty CONFIG_DIALOG_SIZE = addStrProp("config-dialog-size");
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBoolProp("config_dialog-accordion", Boolean.TRUE);
    public static DoubleProperty CONFIG_DIALOG_SET_DIVIDER = addDoubleProp("config-dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static StringProperty CONFIG_DIALOG_IMPORT_SET_SIZE = addStrProp("config-dialog-import-set-size", "800:700");
    public static DoubleProperty CONFIG_DIALOG_SHORTCUT_DIVIDER = addDoubleProp("config-dialog-shortcut-divider", 0.1);

    //StartDialog
    public static StringProperty START_DIALOG_DOWNLOAD_PATH = addStrProp("start-dialog-download-path", P2SystemUtils.getStandardDownloadPath());

    //EpisodeInfoDialog
    public static BooleanProperty EPISODE_INFO_DIALOG_SHOW_BIG = addBoolProp("episode-info-dialog-show-big", Boolean.TRUE);

    //Shorcuts Hauptmenü
    public static final String SHORTCUT_QUIT_PROGRAM_INIT = "Ctrl+Q";
    public static StringProperty SHORTCUT_QUIT_PROGRAM = addStrProp("SHORTCUT_QUIT_PROGRAM", SHORTCUT_QUIT_PROGRAM_INIT);

    //Shortcuts Podcastmenü
    public static final String SHORTCUT_UPDATE_PODCAST_INIT = "Ctrl+P";
    public static StringProperty SHORTCUT_UPDATE_PODCAST = addStrProp("SHORTCUT_PLAY_STATION", SHORTCUT_UPDATE_PODCAST_INIT);
    public static final String SHORTCUT_SAVE_PODCAST_INIT = "Ctrl+S";
    public static StringProperty SHORTCUT_SAVE_PODCAST = addStrProp("SHORTCUT_SAVE_STATION", SHORTCUT_SAVE_PODCAST_INIT);

    // Shortcuts Episodenmenü
    public static final String SHORTCUT_EPISODE_START_INIT = "Ctrl+F";
    public static StringProperty SHORTCUT_EPISODE_START = addStrProp("SHORTCUT_EPISODE_START", SHORTCUT_EPISODE_START_INIT);
    public static final String SHORTCUT_EPISODE_STOP_INIT = "Ctrl+T";
    public static StringProperty SHORTCUT_EPISODE_STOP = addStrProp("SHORTCUT_EPISODE_STOP", SHORTCUT_EPISODE_STOP_INIT);
    public static final String SHORTCUT_EPISODE_CHANGE_INIT = "Ctrl+C";
    public static StringProperty SHORTCUT_EPISODE_CHANGE = addStrProp("SHORTCUT_EPISODE_CHANGE", SHORTCUT_EPISODE_CHANGE_INIT);

    public static IntegerProperty DOWNLOAD_MAX_DOWNLOADS = addIntProp("download-max-downloads", 1);


    private ProgConfig() {
        super("progConfig" + P2Data.TAGGER + "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        ProgConfig.SYSTEM_PROG_VERSION.set(P2ToolsFactory.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(P2ToolsFactory.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(P2ToolsFactory.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getInstance());
        configFile.addConfigs(ProgData.getInstance().setDataList);
        configFile.addConfigs(ProgData.getInstance().podcastList);
        configFile.addConfigs(ProgData.getInstance().downloadList);
        configFile.addConfigs(ProgData.getInstance().episodeList);
    }

    public static void getConfigLog(ArrayList<String> list) {
        list.add(P2Log.LILNE2);
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
}
