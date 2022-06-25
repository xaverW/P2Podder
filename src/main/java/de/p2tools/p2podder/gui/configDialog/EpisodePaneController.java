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

package de.p2tools.p2podder.gui.configDialog;

import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class EpisodePaneController extends PAccordionPane {

    private final ProgData progData;

    private PToggleSwitch tglDelFile = new PToggleSwitch("");
    private CheckBox chkAsk = new CheckBox("Vorher immer fragen");

    private final Stage stage;

    public EpisodePaneController(Stage stage) {
        super(stage, ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_POD);
        this.stage = stage;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        super.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        makeConfig(result);
        return result;
    }

    private void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Speicherordner", gridPane);
        result.add(tpConfig);

        tglDelFile.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE);
        chkAsk.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK);
        chkAsk.disableProperty().bind(ProgConfig.SYSTEM_DELETE_EPISODE_FILE.not());


        final Button btnCleanHelp = PButton.helpButton(stage, "Speicherordner aufräumen", HelpText.DEST_DIR_CLEAN);
        Button btnClean = new Button("Aufräumen");
        btnClean.setTooltip(new Tooltip("Dateien ohne entsprechende Episode löschen."));
        btnClean.setOnAction(a -> DownloadFactory.cleanUpDownloadDir());

        int row = 0;
        gridPane.add(new Label("Beim Löschen von Episoden auch die zugehörige Datei löschen"), 0, row, 2, 1);
        gridPane.add(tglDelFile, 2, row, 2, 1);
        gridPane.add(chkAsk, 1, ++row);
        GridPane.setHalignment(tglDelFile, HPos.RIGHT);
        gridPane.add(new Label(), 0, ++row);

        gridPane.add(new Label("Downloadordner jetzt aufräumen"), 0, ++row, 2, 1);
        gridPane.add(btnClean, 2, row);
        gridPane.add(btnCleanHelp, 3, row);
        GridPane.setHalignment(btnClean, HPos.RIGHT);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());
    }
}