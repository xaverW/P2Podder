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
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.gui.tools.HelpTextPset;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgramPane extends TitledPane {
    private SetData setData = null;

    private final TextField txtVisibleName = new TextField("");
    private final PToggleSwitch tglStandarSet = new PToggleSwitch();
    private ChangeListener changeListener;
    private ChangeListener standardSetListener;

    private final GridPane gridPane = new GridPane();
    private final TextField txtProgPath = new TextField();
    private final TextField txtProgSwitch = new TextField();
    private final Stage stage;

    public ProgramPane(Stage stage) {
        this.stage = stage;

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
        addConfigs(vBox);
    }

    private void addSetData(VBox vBox) {
        changeListener = (observable, oldValue, newValue) -> ProgData.getInstance().setDataList.setListChanged();
        standardSetListener = (observable, oldValue, newValue) -> {
            if (setData != null) {
                ProgData.getInstance().setDataList.setPlay(setData);
            }
        };

        // Name, Beschreibung
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        int row = 0;
        gridPane.add(new Label("Set Name:"), 0, row);
        gridPane.add(txtVisibleName, 1, row, 2, 1);

        final Button btnHelp = PButton.helpButton(stage, "Standardset", HelpTextPset.PSET_STANDARD);
        gridPane.add(new Label("Standardset:"), 0, ++row);
        gridPane.add(tglStandarSet, 1, row);
        gridPane.add(btnHelp, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());
        vBox.getChildren().add(gridPane);
    }

    private void addConfigs(VBox vBox) {
        gridPane.getStyleClass().add("extra-pane");
        gridPane.setHgap(15);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(20));

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> PDirFileChooser.FileChooserOpenFile(ProgData.getInstance().primaryStage, txtProgPath));
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Ein Programm zum verarbeiten der URL auswählen"));

        int row = 0;
        gridPane.add(new Label("Programm: "), 0, ++row);
        gridPane.add(txtProgPath, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(new Label("Schalter: "), 0, ++row);
        gridPane.add(txtProgSwitch, 1, row, 2, 1);

        gridPane.getColumnConstraints().addAll(new ColumnConstraints(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        vBox.getChildren().add(gridPane);
    }

    public void bindProgData(SetData setData) {
        unBindProgData();

        this.setData = setData;
        if (setData != null) {
            txtVisibleName.textProperty().bindBidirectional(setData.visibleNameProperty());
            txtVisibleName.textProperty().addListener(changeListener);

            txtProgPath.textProperty().bindBidirectional(setData.programPathProperty());
            txtProgPath.textProperty().addListener(changeListener);

            txtProgSwitch.textProperty().bindBidirectional(setData.programSwitchProperty());
            txtProgSwitch.textProperty().addListener(changeListener);

            tglStandarSet.selectedProperty().bindBidirectional(setData.standardSetProperty());
            tglStandarSet.selectedProperty().addListener(standardSetListener);
        }
    }

    void unBindProgData() {
        if (setData != null) {
            txtVisibleName.textProperty().unbindBidirectional(setData.visibleNameProperty());
            txtVisibleName.textProperty().removeListener(changeListener);

            txtProgPath.textProperty().unbindBidirectional(setData.programPathProperty());
            txtProgPath.textProperty().removeListener(changeListener);

            txtProgSwitch.textProperty().unbindBidirectional(setData.programSwitchProperty());
            txtProgSwitch.textProperty().removeListener(changeListener);

            tglStandarSet.selectedProperty().unbindBidirectional(setData.standardSetProperty());
            tglStandarSet.selectedProperty().removeListener(standardSetListener);
        }
    }

//    private void setActProgramData() {
//        ProgramData programDataAct = tableView.getSelectionModel().getSelectedItem();
//        if (programDataAct == programData) {
//            return;
//        }
//
//        unbind();
//
//        programData = programDataAct;
//        gridPane.setDisable(programData == null);
//        if (programData != null) {
//            txtName.textProperty().bindBidirectional(programData.nameProperty());
//            txtProgPath.textProperty().bindBidirectional(programData.progPathProperty());
//            txtProgSwitch.textProperty().bindBidirectional(programData.progSwitchProperty());
//        }
//    }

//    private void unbind() {
////            txtName.textProperty().unbindBidirectional(programData.nameProperty());
////            txtProgPath.textProperty().unbindBidirectional(programData.progPathProperty());
////            txtProgSwitch.textProperty().unbindBidirectional(programData.progSwitchProperty());
//    }

//    private int getSelectedLine() {
//        final int sel = tableView.getSelectionModel().getSelectedIndex();
//        if (sel < 0) {
//            PAlert.showInfoNoSelection();
//        }
//        return sel;
//    }
}
