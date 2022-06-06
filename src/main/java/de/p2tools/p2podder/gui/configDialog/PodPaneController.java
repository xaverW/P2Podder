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

import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfos;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class PodPaneController extends PAccordionPane {

    private final ProgData progData;

    private TextField txtPodDest = new TextField();
    private PToggleSwitch tglDelFile = new PToggleSwitch("");
    private CheckBox chkAsk = new CheckBox("Vorher immer fragen");

    private final Stage stage;

    public PodPaneController(Stage stage) {
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

        final Button btnHelp = PButton.helpButton(stage, "Speicherordner Podcasts", HelpText.DEST_DIR);
        txtPodDest.textProperty().bindBidirectional(ProgConfig.SYSTEM_POD_DIR);
        if (txtPodDest.getText().isEmpty()) {
            txtPodDest.setText(ProgInfos.getStandardPodDestString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für die Podcasts auswählen"));
        btnFile.setOnAction(event -> {
            PDirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtPodDest);
        });
        btnFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);

        final Button btnReset = new Button();
        btnReset.setGraphic(new ProgIcons().ICON_BUTTON_PROPOSE);
        btnReset.setTooltip(new Tooltip("Standardpfad für die Podcasts wieder herstellen"));
        btnReset.setOnAction(event -> {
            txtPodDest.setText(ProgInfos.getStandardPodDestString());
        });

        tglDelFile.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE);
        chkAsk.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK);
        chkAsk.disableProperty().bind(ProgConfig.SYSTEM_DELETE_EPISODE_FILE.not());


        final Button btnCleanHelp = PButton.helpButton(stage, "Speicherordner aufräumen", HelpText.DEST_DIR_CLEAN);
        Button btnClean = new Button("Aufräumen");
        btnClean.setTooltip(new Tooltip("Dateien ohne entsprechende Episode löschen."));
        btnClean.setOnAction(a -> DownloadFactory.cleanUpDownloadDir());

        int row = 0;
        gridPane.add(new Label("Ordner:"), 0, ++row);
        gridPane.add(txtPodDest, 1, row);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(btnFile, btnReset);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(hBox, 2, row);
        gridPane.add(btnHelp, 3, row);
        gridPane.add(new Label(), 0, ++row);

        gridPane.add(new Label("Beim Löschen von Episoden auch die zugehörige Datei löschen"), 0, ++row, 2, 1);
        gridPane.add(tglDelFile, 2, row, 2, 1);
        gridPane.add(chkAsk, 1, ++row);
        GridPane.setHalignment(tglDelFile, HPos.RIGHT);
        gridPane.add(new Label(), 0, ++row);

        gridPane.add(new Label("Downloadordner jetzt aufräumen"), 0, ++row, 2, 1);
        gridPane.add(btnClean, 2, row);
        gridPane.add(btnCleanHelp, 3, row);
        GridPane.setHalignment(btnClean, HPos.RIGHT);


//        gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());
    }
}