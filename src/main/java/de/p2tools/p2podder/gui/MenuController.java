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

package de.p2tools.p2podder.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MenuController extends ScrollPane {

    public enum StartupMode {
        EPISODE, PODCAST, DOWNLOAD
    }

    public MenuController(StartupMode startupMode) {
        VBox vb = new VBox();

        setMinWidth(Region.USE_PREF_SIZE);
        setFitToHeight(true);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setContent(vb);

        vb.setPadding(new Insets(5));
        vb.setSpacing(15);
        vb.setAlignment(Pos.TOP_CENTER);

        switch (startupMode) {
            case EPISODE:
                new EpisodeMenu(vb).init();
                break;
            case PODCAST:
                new PodcastMenu(vb).init();
                break;
            case DOWNLOAD:
                new DownloadMenu(vb).init();
                break;
        }
    }
}
