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

import de.p2tools.p2lib.guitools.pclosepane.InfoController;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.filter.EpisodeFilterController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class EpisodeGui {

    private ProgData progData;
    private final SplitPane splitPane = new SplitPane();
    private final HBox hBox = new HBox();
    private final EpisodeGuiController episodeGuiController;
    private final InfoController infoController;
    private BooleanProperty bound = new SimpleBooleanProperty(false);

    public EpisodeGui() {
        progData = ProgData.getInstance();
        progData.episodeGui = this;

        infoController = new InfoController(new EpisodeFilterController(),
                ProgConfig.EPISODE__FILTER_IS_SHOWING, ProgConfig.EPISODE__FILTER_IS_RIP,
                ProgConfig.EPISODE__FILTER_DIALOG_SIZE, ProgData.EPISODE_TAB_ON,
                "Filter", "Episoden", true);

        episodeGuiController = new EpisodeGuiController();
    }

    public EpisodeGuiController getEpisodeGuiController() {
        return episodeGuiController;
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.EPISODE);
        menuController.setId("episode-menu-pane");

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(infoController, Boolean.FALSE);

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPane, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPane, menuController);

        ProgConfig.EPISODE__FILTER_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        return hBox;
    }

    private void setSplit() {
        P2ClosePaneFactory.setSplit(bound, splitPane,
                infoController, true,
                episodeGuiController, ProgConfig.EPISODE__FILTER_DIVIDER, ProgConfig.EPISODE__FILTER_IS_SHOWING);
    }
}
