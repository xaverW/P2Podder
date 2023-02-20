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

package de.p2tools.p2podder.gui.dialog;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ImportSetDialogPanePath {
    private GridPane gridPane = new GridPane();
    private int row = 0;
    private final Stage stage;
    private final TextField txtPlayer = new TextField();

    public ImportSetDialogPanePath(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        txtPlayer.textProperty().unbindBidirectional(ProgConfig.SYSTEM_PATH_VLC);
    }

    public TitledPane makePath() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());

        Button btnEmpty = new Button(" "); // ist nur für die Zeilenhöhe
        btnEmpty.setVisible(false);
        gridPane.add(btnEmpty, 2, row);
        addPlayer();

        TitledPane tpConfig = new TitledPane("Programmpfade", gridPane);
        return tpConfig;
    }

    private void addPlayer() {
        Text text;
        PHyperlink hyperlink;
        final Button btnFind = new Button("suchen");

        text = new Text("Pfad zum VLC-Player auswählen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtPlayer.setText(SetFactory.getTemplatePathVlc());
        });
        hyperlink = new PHyperlink(stage,
                ProgConst.ADRESSE_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

        text.setStyle("-fx-font-weight: bold");

        txtPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
//            File file = new File(txtPlayer.getText());
//            if (!file.exists() || !file.isFile()) {
////                txtPlayer.setStyle(ProgColorList.STATION_NAME_ERROR.getCssBackground());
//            } else {
//                txtPlayer.setStyle("");
//            }
        });
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);

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

        gridPane.add(text, 0, row);
        gridPane.add(txtPlayer, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnFind, 2, row);
        gridPane.add(btnHelp, 3, row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }
}
