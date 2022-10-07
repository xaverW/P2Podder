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

package de.p2tools.p2podder.gui.startDialog;


import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class ConfigPane {
    private final Stage stage;
    private final PToggleSwitch tglSearch = new PToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    private final GridPane gridPane = new GridPane();
    private final TextField txtPlayer = new TextField();
    private TextField txtPodDest = new TextField();

    BooleanProperty updateProp = ProgConfig.SYSTEM_UPDATE_SEARCH_ACT;
    StringProperty vlcProp = ProgConfig.SYSTEM_PATH_VLC;
    StringProperty propPodDir = ProgConfig.SYSTEM_POD_DIR;

    private int row = 0;

    public ConfigPane(Stage stage) {
        this.stage = stage;
    }

    public TitledPane makeStart() {
        makeDestPath();
        makeVLCPath();
        makeUpdate();

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());

        TitledPane tpConfig = new TitledPane("Programmeinstellungen", gridPane);
        return tpConfig;
    }

    public void close() {
        tglSearch.selectedProperty().unbindBidirectional(updateProp);
        txtPlayer.textProperty().bindBidirectional(vlcProp);
    }

    private void makeDestPath() {
        Text text = new Text("Einen Ordner zum Speichern der Episoden auswählen");
        text.setStyle("-fx-font-weight: bold");

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);

        final Button btnHelp = PButton.helpButton(stage, "Speicherordner Episoden", HelpText.DEST_DIR_EPISODES);
        if (ProgData.debug) {
            //dann einen anderen Pfad
            propPodDir.setValue("/tmp/Podcast");
        }
        txtPodDest.textProperty().bindBidirectional(propPodDir);
        if (txtPodDest.getText().isEmpty()) {
            txtPodDest.setText(ProgInfosFactory.getStandardPodDestString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für die Episoden auswählen"));
        btnFile.setOnAction(event -> {
            PDirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtPodDest);
        });
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

        final Button btnReset = new Button();
        btnReset.setGraphic(ProgIcons.Icons.ICON_BUTTON_PROPOSE.getImageView());
        btnReset.setTooltip(new Tooltip("Standardpfad für die Episoden wieder herstellen"));
        btnReset.setOnAction(event -> {
            txtPodDest.setText(ProgInfosFactory.getStandardPodDestString());
        });

        gridPane.add(txtPodDest, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnReset, 2, row);
        gridPane.add(btnHelp, 3, row);
        GridPane.setHalignment(btnReset, HPos.RIGHT);
    }

    private void makeVLCPath() {
        Text text = new Text("Pfad zum Media-Player auswählen");
        text.setStyle("-fx-font-weight: bold");

        PHyperlink hyperlink = new PHyperlink(stage,
                ProgConst.ADRESSE_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

        final Button btnFind = new Button("suchen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtPlayer.setText(SetFactory.getTemplatePathVlc());
        });

        txtPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtPlayer.getText());
            if (!file.exists() || !file.isFile()) {
                txtPlayer.setStyle(ProgColorList.EPISODE_ERROR_BG.getCssBackground());
            } else {
                txtPlayer.setStyle("");
            }
        });
        txtPlayer.textProperty().bindBidirectional(vlcProp);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            PDirFileChooser.FileChooserOpenFile(stage, txtPlayer);
        });
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Programmdatei auswählen"));

        final Button btnHelp = PButton.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Website"), hyperlink);

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);
        gridPane.add(txtPlayer, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnFind, 2, row);
        GridPane.setHalignment(btnFind, HPos.RIGHT);

        gridPane.add(btnHelp, 3, row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }

    private void makeUpdate() {
        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(updateProp);

        Text text = new Text("Suche nach einem Programmupdate");
        text.setStyle("-fx-font-weight: bold");

        final Button btnHelp = PButton.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. Wenn es " +
                        "eine neue Version gibt, wird das mit einer Nachricht mitgeteilt. Es wird nicht " +
                        "automatisch das Programm verändert.");

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);
        gridPane.add(tglSearch, 0, ++row, 3, 1);
        GridPane.setHalignment(tglSearch, HPos.RIGHT);

        gridPane.add(btnHelp, 3, row);
    }
}
