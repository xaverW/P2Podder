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

public class DownloadGui {

    private ProgData progData;
    private final SplitPane splitPane = new SplitPane();
    private final HBox hBox = new HBox();
    private final DownloadGuiController downloadGuiController;
    private final DownloadFilterController downloadFilterController;
    private boolean bound = false;

    public DownloadGui() {
        progData = ProgData.getInstance();
        progData.downloadGui = this;

        downloadFilterController = new DownloadFilterController();
        downloadGuiController = new DownloadGuiController();
    }

    public DownloadGuiController getDownloadGuiController() {
        return downloadGuiController;
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.DOWNLOAD);
        menuController.setId("download-menu-pane");

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(downloadFilterController, Boolean.FALSE);

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPane, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPane, menuController);

        ProgConfig.DOWNLOAD_GUI_FILTER_ON.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        return hBox;
    }

    public void closeSplit() {
        ProgConfig.DOWNLOAD_GUI_FILTER_ON.setValue(!ProgConfig.DOWNLOAD_GUI_FILTER_ON.get());
    }

    private void setSplit() {
        if (ProgConfig.DOWNLOAD_GUI_FILTER_ON.getValue()) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(downloadFilterController, downloadGuiController);
            bound = true;
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.DOWNLOAD_GUI_FILTER_DIVIDER);
        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.DOWNLOAD_GUI_FILTER_DIVIDER);
            }
            splitPane.getItems().clear();
            splitPane.getItems().addAll(downloadGuiController);
        }
    }
}
