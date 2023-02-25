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


package de.p2tools.p2podder.controller.data;

import de.p2tools.p2podder.controller.config.ProgConfig;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProgIcons {
    private final static String ICON_PATH = "/de/p2tools/p2podder/res/program/";


    public enum Icons {
        ICON_DIALOG_EIN_SW(ProgConfig.SYSTEM_DARK_THEME.get() ? "dialog-ein.png" : "dialog-ein-sw.png"),
        IMAGE_ACHTUNG_64("achtung_64.png"),

        ICON_TOOLBAR_SMALL_PODDER_24("toolbar-menu-smallPodder-24.png", 24, 24),
        ICON_TOOLBAR_SMALL_PODDER_20("toolbar-menu-smallPodder-20.png", 20, 20),
        ICON_BUTTON_FILTER("button-filter.png", 25, 20),
        ICON_BUTTON_CLEAR_FILTER("button-clear-filter.png", 25, 20),
        ICON_BUTTON_RESET("button-reset.png", 14, 14),
        ICON_BUTTON_PROPOSE("button-propose.png", 16, 16),
        ICON_BUTTON_BACKWARD("button-backward.png", 16, 16),
        ICON_BUTTON_FORWARD("button-forward.png", 16, 16),
        ICON_BUTTON_QUIT("button-quit.png", 16, 16),
        ICON_BUTTON_FILE_OPEN("button-file-open.png", 16, 16),
        ICON_BUTTON_PLAY("button-play.png", 16, 16),
        ICON_BUTTON_PLAY_NEXT("button-play-next.png", 16, 16),
        ICON_BUTTON_STOP_PLAY("button-stop-play.png", 16, 16),
        ICON_BUTTON_STOP("button-stop.png", 16, 16),
        ICON_BUTTON_NEXT("button-next.png", 16, 16),
        ICON_BUTTON_PREV("button-prev.png", 16, 16),
        ICON_BUTTON_RANDOM("button-random.png", 16, 16),
        ICON_BUTTON_REMOVE("button-remove.png", 16, 16),
        ICON_BUTTON_ADD("button-add.png", 16, 16),
        ICON_BUTTON_MOVE_DOWN("button-move-down.png", 16, 16),
        ICON_BUTTON_MOVE_UP("button-move-up.png", 16, 16),
        ICON_BUTTON_DOWN("button-down.png", 16, 16),
        ICON_BUTTON_UP("button-up.png", 16, 16),
        ICON_DIALOG_QUIT("dialog-quit.png", 64, 64),

        //table
        IMAGE_TABLE_EPISODE_DEL("table-episode-del.png", 14, 14),
        IMAGE_TABLE_EPISODE_GRADE("table-episode-grade.png", 14, 14),
        IMAGE_TABLE_DOWNLOAD_START("table-download-start.png", 14, 14),
        IMAGE_TABLE_DOWNLOAD_STOP("table-download-stop.png", 14, 14),
        IMAGE_TABLE_DOWNLOAD_BACK("table-download-back.png", 14, 14),
        IMAGE_TABLE_DOWNLOAD_DEL("table-download-del.png", 14, 14),
        IMAGE_TABLE_DOWNLOAD_OPEN_DIR("table-download-open-dir.png", 14, 14),
        IMAGE_TABLE_PODCAST_UPDATE("table-podcast-update.png", 14, 14),
        IMAGE_TABLE_PODCAST_DEL("table-podcast-del.png", 14, 14),
        IMAGE_TABLE_PODCAST_SET_ACTIVE("table-podcast-set-active.png", 14, 14),
        IMAGE_TABLE_EPISODE_PLAY("table-episode-play.png", 14, 14),
        IMAGE_TABLE_EPISODE_STOP_PLAY("table-episode-stop-play.png", 14, 14),


        //toolbar
        ICON_TOOLBAR_MENU("toolbar-menu.png", 18, 15),
        ICON_TOOLBAR_MENU_TOP("toolbar-menu-top.png", 32, 18),
        ICON_TOOLBAR_EPISODE_DEL("toolbar-episode-del.png", 32, 32),
        ICON_TOOLBAR_DOWNLOAD_CLEAN("toolbar-download-clean.png", 32, 32),
        ICON_TOOLBAR_DOWNLOAD_BACK("toolbar-download-back.png", 32, 32),
        ICON_TOOLBAR_DOWNLOAD_START("toolbar-download-start.png", 32, 32),
        ICON_TOOLBAR_DOWNLOAD_START_ALL("toolbar-download-start-all.png", 32, 32),
        ICON_TOOLBAR_DOWNLOAD_STOP("toolbar-download-stop.png", 32, 32),
        ICON_TOOLBAR_EPISODE_START("toolbar-episode-start.png", 32, 32),
        ICON_TOOLBAR_EPISODE_PLAY_NEXT("toolbar-episode-play-next.png", 32, 32),
        ICON_TOOLBAR_EPISODE_STOP("toolbar-episode-stop.png", 32, 32),
        ICON_TOOLBAR_PODCAST_UPDATE("toolbar-podcast-update.png", 32, 32),
        ICON_TOOLBAR_PODCAST_DEL("toolbar-podcast-del.png", 32, 32),
        ICON_TOOLBAR_PODCAST_ADD("toolbar-podcast-add.png", 32, 32),

        ICON_TOOLBAR_INFO("toolbar-info.png", 32, 32);


        private String fileName;
        private String fileNameDark = "";
        private int w = 0;
        private int h = 0;

        Icons(String fileName, int w, int h) {
            this.fileName = fileName;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName, String fileNameDark, int w, int h) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName) {
            this.fileName = fileName;
        }

        Icons(String fileName, String fileNameDark) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
        }

        public ImageView getImageView() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return de.p2tools.p2lib.icons.GetIcon.getImageView(fileNameDark, ICON_PATH, w, h);
            }
            return de.p2tools.p2lib.icons.GetIcon.getImageView(fileName, ICON_PATH, w, h);
        }

        public Image getImage() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return de.p2tools.p2lib.icons.GetIcon.getImage(fileNameDark, ICON_PATH, w, h);
            }
            return de.p2tools.p2lib.icons.GetIcon.getImage(fileName, ICON_PATH, w, h);
        }
    }
}
