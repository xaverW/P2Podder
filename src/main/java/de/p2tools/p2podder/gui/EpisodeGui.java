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

import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
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
    private final EpisodeFilterController epiDownFilterController;
    private boolean bound = false;

    public EpisodeGui() {
        progData = ProgData.getInstance();
        progData.episodeGui = this;

        epiDownFilterController = new EpisodeFilterController();
        episodeGuiController = new EpisodeGuiController();
    }

    public EpisodeGuiController getEpisodeGuiController() {
        return episodeGuiController;
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.EPISODE);
        menuController.setId("episode-menu-pane");

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(epiDownFilterController, Boolean.FALSE);

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPane, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPane, menuController);

        ProgConfig.EPISODE_GUI_FILTER_ON.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        return hBox;
    }

    public void closeSplit() {
        ProgConfig.EPISODE_GUI_FILTER_ON.setValue(!ProgConfig.EPISODE_GUI_FILTER_ON.get());
    }

    private void setSplit() {
        System.out.println(ProgConfig.EPISODE_GUI_FILTER_DIVIDER.get());
        if (ProgConfig.EPISODE_GUI_FILTER_ON.getValue()) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(epiDownFilterController, episodeGuiController);
            bound = true;
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.EPISODE_GUI_FILTER_DIVIDER);
        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.EPISODE_GUI_FILTER_DIVIDER);
            }
            splitPane.getItems().clear();
            splitPane.getItems().addAll(episodeGuiController);
        }
    }
}
