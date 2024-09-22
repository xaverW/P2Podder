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

import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2lib.guitools.pclosepane.P2InfoController;
import de.p2tools.p2lib.guitools.pclosepane.P2InfoDto;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.filter.DownloadFilterController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class DownloadGui {

    private ProgData progData;
    private final SplitPane splitPane = new SplitPane();
    private final HBox hBox = new HBox();
    private final DownloadGuiController downloadGuiController;
    private final P2InfoController infoController;

    private BooleanProperty bound = new SimpleBooleanProperty(false);

    public DownloadGui() {
        progData = ProgData.getInstance();
        progData.downloadGui = this;

        ArrayList<P2InfoDto> list = new ArrayList<>();
        P2InfoDto infoDto = new P2InfoDto(new DownloadFilterController(),
                ProgConfig.DOWNLOAD__FILTER_IS_RIP,
                ProgConfig.DOWNLOAD__FILTER_DIALOG_SIZE, ProgData.DOWNLOAD_TAB_ON,
                "Filter", "Download", true);
        list.add(infoDto);
        infoController = new P2InfoController(list, ProgConfig.DOWNLOAD__FILTER_IS_SHOWING);

        downloadGuiController = new DownloadGuiController();
    }

    public DownloadGuiController getDownloadGuiController() {
        return downloadGuiController;
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.DOWNLOAD);
        menuController.setId("download-menu-pane");

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(infoController, Boolean.FALSE);

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPane, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPane, menuController);

        ProgConfig.DOWNLOAD__FILTER_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
        return hBox;
    }

    private void setSplit() {
        // hier wird der Filter ein- ausgeblendet
        P2ClosePaneFactory.setSplit(bound, splitPane,
                infoController, true,
                downloadGuiController, ProgConfig.DOWNLOAD__FILTER_DIVIDER, ProgConfig.DOWNLOAD__FILTER_IS_SHOWING);
    }
}
