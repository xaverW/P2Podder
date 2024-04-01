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

package de.p2tools.p2podder.gui.startdialog;


import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
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
    private final P2ToggleSwitch tglSearch = new P2ToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    private final GridPane gridPane = new GridPane();
    private final TextField txtPlayer = new TextField();
    private TextField txtPodDest = new TextField();

    private int row = 0;

    public ConfigPane(Stage stage) {
        this.stage = stage;
    }

    public TitledPane makeStart() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow());

        makeDestPath();
        makeVLCPath();
        makeUpdate();

        TitledPane tpConfig = new TitledPane("Programmeinstellungen", gridPane);
        return tpConfig;
    }

    public void close() {
        tglSearch.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_UPDATE_SEARCH_ACT);
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);
    }

    private void makeDestPath() {
        Text text = new Text("Einen Ordner zum Speichern der Episoden auswählen");
        text.setStyle("-fx-font-weight: bold");

        gridPane.add(new Label(" "), 2, row);
        gridPane.add(text, 0, row);

        final Button btnHelp = P2Button.helpButton(stage, "Speicherordner Episoden", HelpText.DEST_DIR_EPISODES);
        if (ProgData.debug) {
            //dann einen anderen Pfad
            ProgConfig.SYSTEM_POD_DIR.setValue("/tmp/Podcast");
        }
        txtPodDest.textProperty().bindBidirectional(ProgConfig.SYSTEM_POD_DIR);
        if (ProgData.debug) {
            //dann einen anderen Downloadpfad
            ProgConfig.SYSTEM_POD_DIR.setValue("/tmp/Download");
        }
        if (txtPodDest.getText().isEmpty()) {
            txtPodDest.setText(ProgInfosFactory.getStandardPodDestString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für die Episoden auswählen"));
        btnFile.setOnAction(event -> {
            P2DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtPodDest);
        });
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());

        final Button btnReset = new Button();
        btnReset.setGraphic(ProgIcons.ICON_BUTTON_PROPOSE.getImageView());
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

        P2Hyperlink hyperlink = new P2Hyperlink(stage,
                ProgConst.ADRESSE_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());

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
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            P2DirFileChooser.FileChooserOpenFile(stage, txtPlayer);
        });
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Programmdatei auswählen"));

        final Button btnHelp = P2Button.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Website"), hyperlink);

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, ++row);
        gridPane.add(txtPlayer, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnFind, 2, row);
        GridPane.setHalignment(btnFind, HPos.RIGHT);

        gridPane.add(btnHelp, 3, row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }

    private void makeUpdate() {
        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_UPDATE_SEARCH_ACT);

        Text text = new Text("Suche nach einem Programmupdate");
        text.setStyle("-fx-font-weight: bold");

        final Button btnHelp = P2Button.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. Wenn es " +
                        "eine neue Version gibt, wird das mit einer Nachricht mitgeteilt. Es wird nicht " +
                        "automatisch das Programm verändert.");

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, ++row);
        gridPane.add(tglSearch, 0, ++row, 3, 1);
        GridPane.setHalignment(tglSearch, HPos.RIGHT);

        gridPane.add(btnHelp, 3, row);
    }
}
