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

package de.p2tools.p2podder.gui.configDialog.setData;

import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.SetData;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SetPaneController extends AnchorPane {

    private final ProgData progData;
    private SetData setData = null;

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();

    private ProgramPane programPane;
    private ListPane listPane;

    private final Stage stage;
    DoubleProperty split = ProgConfig.CONFIG_DIALOG_SET_DIVIDER;

    public SetPaneController(ProgData progData, Stage stage) {
        this.stage = stage;
        this.progData = progData;

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        getChildren().addAll(splitPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        createPane();
    }

    public void close() {
    }

    private void createPane() {
        programPane = new ProgramPane(progData, stage);
        listPane = new ListPane(stage, this);

        splitPane.getItems().add(listPane);
        splitPane.getItems().add(programPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(split);
    }

    public void setSetData(SetData setData) {
        programPane.unBindProgData();

        this.setData = setData;
        if (setData != null) {
            programPane.bindProgData(setData);
            programPane.setSetDate(setData);
        }
        setDisable();
    }

    public void setDisable() {
        splitPane.setDisable(setData == null);
    }
}