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

package de.p2tools.p2podder.gui.configdialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Collection;

public class PanePodcast {

    private final ProgData progData;

    private TextField txtPodDest = new TextField();

    private final P2ToggleSwitch tglUpdatePodcastDaily = new P2ToggleSwitch("Die Podcasts einmal am Tage aktualisieren");
    private final P2ToggleSwitch tglStartDownload = new P2ToggleSwitch("und den Download der Episoden gleich starten");

    private final Stage stage;

    public PanePodcast(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();
    }

    public void close() {
        txtPodDest.textProperty().unbindBidirectional(ProgConfig.SYSTEM_POD_DIR);
        tglUpdatePodcastDaily.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_UPDATE_PODCAST_DAILY);
        tglStartDownload.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_START_DAILY_DOWNLOAD);
    }

    public void makePane(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        TitledPane tpConfig = new TitledPane("Speicherordner", gridPane);
        result.add(tpConfig);

        final Button btnHelp = P2Button.helpButton(stage, "Speicherordner Podcasts", HelpText.DEST_DIR);
        txtPodDest.textProperty().bindBidirectional(ProgConfig.SYSTEM_POD_DIR);
        if (txtPodDest.getText().isEmpty()) {
            txtPodDest.setText(ProgInfosFactory.getStandardPodDestString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für die Podcasts auswählen"));
        btnFile.setOnAction(event -> {
            PDirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtPodDest);
        });
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());

        final Button btnReset = new Button();
        btnReset.setGraphic(ProgIcons.ICON_BUTTON_PROPOSE.getImageView());
        btnReset.setTooltip(new Tooltip("Standardpfad für die Podcasts wieder herstellen"));
        btnReset.setOnAction(event -> {
            txtPodDest.setText(ProgInfosFactory.getStandardPodDestString());
        });

        tglUpdatePodcastDaily.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_UPDATE_PODCAST_DAILY);
        tglStartDownload.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_START_DAILY_DOWNLOAD);
        tglStartDownload.disableProperty().bind(ProgConfig.SYSTEM_UPDATE_PODCAST_DAILY.not());
        tglUpdatePodcastDaily.selectedProperty().addListener((u, o, n) -> {
            if (!tglUpdatePodcastDaily.isSelected()) {
                tglStartDownload.setSelected(false);
            }
        });
        final Button btnHelpLoadStationList = P2Button.helpButton(stage, "Nach neuen Episoden suchen",
                HelpText.LOAD_PODCASTS_EVERY_DAYS);
        GridPane.setHalignment(btnHelpLoadStationList, HPos.RIGHT);

        int row = 0;
        gridPane.add(new Label("Ordner:"), 0, row);
        gridPane.add(txtPodDest, 1, row);
        HBox hBox = new HBox(P2LibConst.DIST_BUTTON);
        hBox.getChildren().addAll(btnFile, btnReset);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(hBox, 2, row);
        gridPane.add(btnHelp, 3, row);

        gridPane.add(new Label(), 0, ++row);
        gridPane.add(tglUpdatePodcastDaily, 0, ++row, 3, 1);
        gridPane.add(btnHelpLoadStationList, 3, row);

        hBox = new HBox();
        hBox.getChildren().addAll(new Label("    "), tglStartDownload);
        HBox.setHgrow(tglStartDownload, Priority.ALWAYS);
        gridPane.add(hBox, 0, ++row, 3, 1);


        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize());
    }
}