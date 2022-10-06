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

import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.gui.tools.HelpTextPset;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgramPane extends TitledPane {
    private SetData setData = null;

    private final TextField txtName = new TextField("");
    private final TextArea taDescription = new TextArea();
    private ChangeListener changeListener;

    private final TextField txtProgPath = new TextField();
    private final TextField txtProgSwitch = new TextField();
    private final CheckBox cbxIsStandard = new CheckBox();
    private final Stage stage;
    private ProgData progData;

    public ProgramPane(ProgData progData, Stage stage) {
        this.stage = stage;
        this.progData = progData;

        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox(10);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBox);

        this.setText("Einstellungen");
        this.setCollapsible(false);
        this.setContent(scrollPane);
        this.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(this, Priority.ALWAYS);

        makeProgs(vBox);
    }

    public void setSetDate(SetData setData) {
        this.setData = setData;
    }

    public void close() {
        unBindProgData();
    }

    private void makeProgs(VBox vBox) {
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10));

        addSetData(vBox);
    }

    private void addSetData(VBox vBox) {
        changeListener = (observable, oldValue, newValue) -> progData.setDataList.setListChanged();
        Button btnStandardSet = new Button("Standard");
        btnStandardSet.setOnAction(a -> {
            progData.setDataList.setStandardSet(setData);
            cbxIsStandard.setSelected(setData.isStandardSet());
        });

        cbxIsStandard.setDisable(true);
        cbxIsStandard.getStyleClass().add("checkbox-table");

        // Name, Beschreibung
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(20));
//        gridPane.setGridLinesVisible(true);

        int row = 0;
        gridPane.add(new Label("Set Name:"), 0, row);
        gridPane.add(txtName, 1, row, 2, 1);

        taDescription.setPrefRowCount(4);
        taDescription.setWrapText(true);
        taDescription.setEditable(false);
        gridPane.add(new Label("Beschreibung:"), 0, ++row);
        gridPane.add(taDescription, 1, row, 2, 1);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> PDirFileChooser.FileChooserOpenFile(progData.primaryStage, txtProgPath));
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Ein Programm zum verarbeiten der URL auswählen"));
        gridPane.add(new Label("Programm: "), 0, ++row);
        gridPane.add(txtProgPath, 1, row, 2, 1);
        gridPane.add(btnFile, 3, row);
        txtProgPath.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(txtProgPath, Priority.ALWAYS);

        gridPane.add(new Label("Schalter: "), 0, ++row);
        gridPane.add(txtProgSwitch, 1, row, 2, 1);

        final Button btnHelp = PButton.helpButton(stage, "Standardset", HelpTextPset.PSET_STANDARD);
        gridPane.add(new Label("Standardset:"), 0, ++row);
        gridPane.add(cbxIsStandard, 1, row);
        gridPane.add(btnStandardSet, 2, row);
        gridPane.add(btnHelp, 3, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(), PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrowRight(), PColumnConstraints.getCcPrefSize());
        vBox.getChildren().add(gridPane);
    }

    public void bindProgData(SetData setData) {
        unBindProgData();

        this.setData = setData;
        if (setData != null) {
            txtName.textProperty().bindBidirectional(setData.nameProperty());
            txtName.textProperty().addListener(changeListener);
            taDescription.textProperty().bindBidirectional(setData.descriptionProperty());
            taDescription.textProperty().addListener(changeListener);

            txtProgPath.textProperty().bindBidirectional(setData.programPathProperty());
            txtProgPath.textProperty().addListener(changeListener);
            txtProgSwitch.textProperty().bindBidirectional(setData.programSwitchProperty());
            txtProgSwitch.textProperty().addListener(changeListener);

            cbxIsStandard.setSelected(setData.isStandardSet());
        }
    }

    void unBindProgData() {
        if (setData != null) {
            txtName.textProperty().unbindBidirectional(setData.nameProperty());
            txtName.textProperty().removeListener(changeListener);
            taDescription.textProperty().unbindBidirectional(setData.descriptionProperty());
            taDescription.textProperty().removeListener(changeListener);

            txtProgPath.textProperty().unbindBidirectional(setData.programPathProperty());
            txtProgPath.textProperty().removeListener(changeListener);
            txtProgSwitch.textProperty().unbindBidirectional(setData.programSwitchProperty());
            txtProgSwitch.textProperty().removeListener(changeListener);
        }
        cbxIsStandard.setSelected(false);
    }
}
