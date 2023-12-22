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
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneEpisode {

    private final ProgData progData;

    private P2ToggleSwitch tglDelFile = new P2ToggleSwitch("");
    private CheckBox chkAsk = new CheckBox("Vorher immer fragen");

    private final Stage stage;

    public PaneEpisode(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();
    }

    public void close() {
        tglDelFile.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE);
        chkAsk.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK);
    }

    public void makePane(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        TitledPane tpConfig = new TitledPane("Speicherordner", gridPane);
        result.add(tpConfig);

        tglDelFile.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE);
        chkAsk.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK);
        chkAsk.disableProperty().bind(ProgConfig.SYSTEM_DELETE_EPISODE_FILE.not());


        final Button btnCleanHelp = P2Button.helpButton(stage, "Speicherordner aufräumen", HelpText.DEST_DIR_CLEAN);
        Button btnClean = new Button("Aufräumen");
        btnClean.setTooltip(new Tooltip("Dateien ohne entsprechende Episode löschen."));
        btnClean.setOnAction(a -> DownloadFactory.cleanUpDownloadDir());

        int row = 0;
        gridPane.add(new Label("Beim Löschen von Episoden auch die zugehörige Datei löschen"), 0, row, 2, 1);
        gridPane.add(tglDelFile, 2, row, 2, 1);
        gridPane.add(chkAsk, 1, ++row);
        GridPane.setHalignment(tglDelFile, HPos.RIGHT);

        ++row;
        gridPane.add(new Label("Downloadordner jetzt aufräumen"), 0, ++row, 2, 1);
        gridPane.add(btnClean, 2, row);
        gridPane.add(btnCleanHelp, 3, row);
        GridPane.setHalignment(btnClean, HPos.RIGHT);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize());
    }
}