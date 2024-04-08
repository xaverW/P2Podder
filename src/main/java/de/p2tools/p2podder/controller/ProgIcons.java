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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.P2ProgIcons;
import de.p2tools.p2lib.icons.P2Icon;
import de.p2tools.p2podder.P2PodderController;
import de.p2tools.p2podder.controller.config.ProgConst;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProgIcons {
    private final static String ICON_PATH = "res/program/";
    public static String ICON_PATH_LONG = "de/p2tools/p2podder/res/program/";


    private static final List<P2IconPodder> iconList = new ArrayList<>();

    //    public static P2IconPodder ICON_DIALOG_EIN_SW = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, ProgConfig.SYSTEM_DARK_THEME.get() ? "dialog-ein.png" : "dialog-ein-sw.png");
    public static P2IconPodder IMAGE_ACHTUNG_64 = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "achtung_64.png", 64, 64);
    public static P2IconPodder ICON_TOOLBAR_SMALL_PODDER_24 = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-smallPodder-24.png", 24, 24);
    public static P2IconPodder ICON_TOOLBAR_SMALL_PODDER_20 = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-smallPodder-20.png", 20, 20);
    public static P2IconPodder ICON_BUTTON_FILTER = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-filter.png", 25, 20);
    public static P2IconPodder ICON_BUTTON_CLEAR_FILTER = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-clear-filter.png", 25, 20);
    public static P2IconPodder ICON_BUTTON_RESET = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-reset.png", 14, 14);
    public static P2IconPodder ICON_BUTTON_PROPOSE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-propose.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_BACKWARD = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-backward.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_FORWARD = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-forward.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_QUIT = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-quit.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_FILE_OPEN = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-file-open.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_PLAY = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-play.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_PLAY_NEXT = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-play-next.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_STOP_PLAY = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-stop-play.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_STOP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-stop.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_NEXT = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-next.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_PREV = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-prev.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_RANDOM = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-random.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_REMOVE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-remove.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_ADD = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-add.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_MOVE_DOWN = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-move-down.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_MOVE_UP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-move-up.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_DOWN = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-down.png", 16, 16);
    public static P2IconPodder ICON_BUTTON_UP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "button-up.png", 16, 16);
    public static P2IconPodder ICON_DIALOG_QUIT = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "dialog-quit.png", 64, 64);


    public static P2IconPodder IMAGE_TABLE_EPISODE_DEL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-episode-del.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_EPISODE_GRADE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-episode-grade.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_DOWNLOAD_START = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-download-start.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_DOWNLOAD_STOP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-download-stop.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_DOWNLOAD_BACK = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-download-back.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_DOWNLOAD_DEL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-download-del.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_DOWNLOAD_OPEN_DIR = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-download-open-dir.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_PODCAST_UPDATE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-podcast-update.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_PODCAST_DEL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-podcast-del.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_PODCAST_SET_ACTIVE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-podcast-set-active.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_EPISODE_PLAY = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-episode-play.png", 14, 14);
    public static P2IconPodder IMAGE_TABLE_EPISODE_STOP_PLAY = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "table-episode-stop-play.png", 14, 14);


    public static P2IconPodder ICON_TOOLBAR_MENU = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-menu.png", 18, 15);
    public static P2IconPodder ICON_TOOLBAR_MENU_TOP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-top.png", 32, 18);
    public static P2IconPodder ICON_TOOLBAR_EPISODE_DEL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-episode-del.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_DOWNLOAD_CLEAN = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-download-clean.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_DOWNLOAD_BACK = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-download-back.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_DOWNLOAD_START = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-download-start.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_DOWNLOAD_START_ALL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-download-start-all.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_DOWNLOAD_STOP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-download-stop.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_EPISODE_START = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-episode-start.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_EPISODE_PLAY_NEXT = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-episode-play-next.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_EPISODE_STOP = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-episode-stop.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_PODCAST_UPDATE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-podcast-update.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_PODCAST_DEL = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-podcast-del.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_PODCAST_CHANGE = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-episode-change.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_PODCAST_ADD = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-podcast-add.png", 32, 32);
    public static P2IconPodder ICON_TOOLBAR_INFO = new P2IconPodder(ICON_PATH_LONG, ICON_PATH, "toolbar-info.png", 32, 32);

    public static void initIcons() {
        iconList.forEach(p -> {
            String url = p.genUrl(P2PodderController.class, P2ProgIcons.class, ProgConst.class, P2ProgIcons.class, P2LibConst.class);
            if (url.isEmpty()) {
                // dann wurde keine gefunden
                System.out.println("ProgIconsInfo: keine URL, icon: " + p.getPathFileNameDark() + " - " + p.getFileName());
            }
        });
    }

    public static class P2IconPodder extends P2Icon {
        public P2IconPodder(String longPath, String path, String fileName, int w, int h) {
            super(longPath, path, fileName, w, h);
            iconList.add(this);
        }

        public boolean searchUrl(String p, Class<?>... clazzAr) {
            URL url;
            url = P2PodderController.class.getResource(p);
            if (set(url, p, "P2PodderController.class.getResource")) return true;
            url = ProgConst.class.getResource(p);
            if (set(url, p, "ProgConst.class.getResource")) return true;
            url = P2ProgIcons.class.getResource(p);
            if (set(url, p, "P2ProgIcons.class.getResource")) return true;
            url = this.getClass().getResource(p);
            if (set(url, p, "this.getClass().getResource")) return true;

            url = ClassLoader.getSystemResource(p);
            if (set(url, p, "ClassLoader.getSystemResource")) return true;
            url = P2LibConst.class.getClassLoader().getResource(p);
            if (set(url, p, "P2LibConst.class.getClassLoader().getResource")) return true;
            url = ProgConst.class.getClassLoader().getResource(p);
            if (set(url, p, "ProgConst.class.getClassLoader().getResource")) return true;
            url = this.getClass().getClassLoader().getResource(p);
            if (set(url, p, "this.getClass().getClassLoader().getResource")) return true;

            return false;
        }
    }
}
