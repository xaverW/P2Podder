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
import de.p2tools.p2podder.res.GetIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProgIcons {
    public final Image ICON_DIALOG_EIN_SW = GetIcon.getImage(ProgConfig.SYSTEM_DARK_THEME.get() ? "dialog-ein.png" : "dialog-ein-sw.png");
    public final Image IMAGE_ACHTUNG_64 = GetIcon.getImage("achtung_64.png");

    public final ImageView ICON_TOOLBAR_SMALL_PODDER_24 = GetIcon.getImageView("toolbar-menu-smallPodder-24.png", 24, 24);
    public final ImageView ICON_TOOLBAR_SMALL_PODDER_20 = GetIcon.getImageView("toolbar-menu-smallPodder-20.png", 20, 20);
    public final ImageView ICON_BUTTON_FILTER = GetIcon.getImageView("button-filter.png", 25, 20);
    public final ImageView ICON_BUTTON_CLEAR_FILTER = GetIcon.getImageView("button-clear-filter.png", 25, 20);
    public final ImageView ICON_BUTTON_RESET = GetIcon.getImageView("button-reset.png", 14, 14);
    public final ImageView ICON_BUTTON_EDIT_FILTER = GetIcon.getImageView("button-edit-filter.png", 16, 16);
    public final ImageView ICON_BUTTON_PROPOSE = GetIcon.getImageView("button-propose.png", 16, 16);
    public final ImageView ICON_BUTTON_BACKWARD = GetIcon.getImageView("button-backward.png", 16, 16);
    public final ImageView ICON_BUTTON_FORWARD = GetIcon.getImageView("button-forward.png", 16, 16);
    public final ImageView ICON_BUTTON_MENU = GetIcon.getImageView("button-menu.png", 18, 15);
    public final ImageView ICON_BUTTON_QUIT = GetIcon.getImageView("button-quit.png", 16, 16);
    public final ImageView ICON_BUTTON_FILE_OPEN = GetIcon.getImageView("button-file-open.png", 16, 16);
    public final ImageView ICON_BUTTON_PLAY = GetIcon.getImageView("button-play.png", 16, 16);
    public final ImageView ICON_BUTTON_STOP_PLAY = GetIcon.getImageView("button-stop-play.png", 16, 16);
    public final ImageView ICON_BUTTON_STOP = GetIcon.getImageView("button-stop.png", 16, 16);
    public final ImageView ICON_BUTTON_NEXT = GetIcon.getImageView("button-next.png", 16, 16);
    public final ImageView ICON_BUTTON_PREV = GetIcon.getImageView("button-prev.png", 16, 16);
    public final ImageView ICON_BUTTON_RANDOM = GetIcon.getImageView("button-random.png", 16, 16);
    public final ImageView ICON_BUTTON_REMOVE = GetIcon.getImageView("button-remove.png", 16, 16);
    public final ImageView ICON_BUTTON_ADD = GetIcon.getImageView("button-add.png", 16, 16);
    public final ImageView ICON_BUTTON_MOVE_DOWN = GetIcon.getImageView("button-move-down.png", 16, 16);
    public final ImageView ICON_BUTTON_MOVE_UP = GetIcon.getImageView("button-move-up.png", 16, 16);
    public final ImageView ICON_BUTTON_DOWN = GetIcon.getImageView("button-down.png", 16, 16);
    public final ImageView ICON_BUTTON_UP = GetIcon.getImageView("button-up.png", 16, 16);
    public final ImageView ICON_DIALOG_QUIT = GetIcon.getImageView("dialog-quit.png", 64, 64);
    public final ImageView ICON_FILTER_EPISODE_LOAD = GetIcon.getImageView("filter-episode-load.png", 15, 15);
    public final ImageView ICON_FILTER_EPISODE_SAVE = GetIcon.getImageView("filter-episode-save.png", 15, 15);
    public final ImageView ICON_FILTER_EPISODE_NEW = GetIcon.getImageView("filter-episode-new.png", 15, 15);

    //table
    public static final Image IMAGE_TABLE_EPISODE_DEL = GetIcon.getImage("table-episode-del.png", 14, 14);
    public static final Image IMAGE_TABLE_EPISODE_GRADE = GetIcon.getImage("table-episode-grade.png", 14, 14);
    public static final Image IMAGE_TABLE_DOWNLOAD_START = GetIcon.getImage("table-download-start.png", 14, 14);
    public static final Image IMAGE_TABLE_DOWNLOAD_STOP = GetIcon.getImage("table-download-stop.png", 14, 14);
    public static final Image IMAGE_TABLE_DOWNLOAD_BACK = GetIcon.getImage("table-download-back.png", 14, 14);
    public static final Image IMAGE_TABLE_DOWNLOAD_DEL = GetIcon.getImage("table-download-del.png", 14, 14);
    public static final Image IMAGE_TABLE_DOWNLOAD_OPEN_DIR = GetIcon.getImage("table-download-open-dir.png", 14, 14);
    public static final Image IMAGE_TABLE_PODCAST_UPDATE = GetIcon.getImage("table-podcast-update.png", 14, 14);
    public static final Image IMAGE_TABLE_PODCAST_DEL = GetIcon.getImage("table-podcast-del.png", 14, 14);
    public static final Image IMAGE_TABLE_EPISODE_PLAY = GetIcon.getImage("table-episode-play.png", 14, 14);
    public static final Image IMAGE_TABLE_EPISODE_STOP_PLAY = GetIcon.getImage("table-episode-stop-play.png", 14, 14);


    //toolbar
    public final ImageView ICON_TOOLBAR_MENU = GetIcon.getImageView("toolbar-menu.png", 18, 15);
    public final ImageView ICON_TOOLBAR_MENU_TOP = GetIcon.getImageView("toolbar-menu-top.png", 32, 18);
    public final ImageView ICON_TOOLBAR_EPISODE_DEL = GetIcon.getImageView("toolbar-episode-del.png", 32, 32);
    public final ImageView ICON_TOOLBAR_DOWNLOAD_CLEAN = GetIcon.getImageView("toolbar-download-clean.png", 32, 32);
    public final ImageView ICON_TOOLBAR_DOWNLOAD_BACK = GetIcon.getImageView("toolbar-download-back.png", 32, 32);
    public final ImageView ICON_TOOLBAR_DOWNLOAD_START = GetIcon.getImageView("toolbar-download-start.png", 32, 32);
    public final ImageView ICON_TOOLBAR_DOWNLOAD_START_ALL = GetIcon.getImageView("toolbar-download-start-all.png", 32, 32);
    public final ImageView ICON_TOOLBAR_DOWNLOAD_STOP = GetIcon.getImageView("toolbar-download-stop.png", 32, 32);
    public final ImageView ICON_TOOLBAR_EPISODE_START = GetIcon.getImageView("toolbar-episode-start.png", 32, 32);
    public final ImageView ICON_TOOLBAR_EPISODE_STOP = GetIcon.getImageView("toolbar-episode-stop.png", 32, 32);
    public final ImageView ICON_TOOLBAR_PODCAST_UPDATE = GetIcon.getImageView("toolbar-podcast-update.png", 32, 32);
    public final ImageView ICON_TOOLBAR_PODCAST_DEL = GetIcon.getImageView("toolbar-podcast-del.png", 32, 32);
    public final ImageView ICON_TOOLBAR_PODCAST_ADD = GetIcon.getImageView("toolbar-podcast-add.png", 32, 32);

    public final ImageView ICON_TOOLBAR_INFO = GetIcon.getImageView("toolbar-info.png", 32, 32);

}
