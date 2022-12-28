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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SetPanePack extends AnchorPane {

    private final ProgData progData;
    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBox = new VBox();
    private final Stage stage;
    private final SetListPane setListPane;
    private final SetDataPane setDataPane;
    private final ObjectProperty<SetData> aktSetDate = new SimpleObjectProperty<>();

    public SetPanePack(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();

        setListPane = new SetListPane(this);
        setDataPane = new SetDataPane(this);
        setDataPane.makePane();

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        VBox.setVgrow(setListPane, Priority.ALWAYS);
        vBox.getChildren().addAll(setListPane);
        splitPane.getItems().addAll(vBox, scrollPane);
        SplitPane.setResizableWithParent(vBox, Boolean.FALSE);
        getChildren().addAll(splitPane);

        scrollPane.setContent(setDataPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.CONFIG_DIALOG_SET_DIVIDER);
    }

    public Stage getStage() {
        return stage;
    }

    public SetData getAktSetDate() {
        return aktSetDate.get();
    }

    public void setAktSetDate(SetData aktSetDate) {
        this.aktSetDate.set(aktSetDate);
    }

    public ObjectProperty<SetData> aktSetDateProperty() {
        return aktSetDate;
    }

    public void close() {
        splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.CONFIG_DIALOG_SET_DIVIDER);
    }
}