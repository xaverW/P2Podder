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
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ImportSetDataFactory;
import de.p2tools.p2podder.controller.data.SetFactory;
import de.p2tools.p2podder.gui.configDialog.setData.PathPane;
import de.p2tools.p2podder.gui.configDialog.setData.SetPanePack;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ImportSetDialogController extends PDialogExtra {

    private final ProgData progData;
    Button btnCancel = new Button("_Abbrechen");
    Button btnImport = new Button("_Set importieren");
    private boolean im = false;
    private StackPane stackPane;
    private VBox vBoxPath = new VBox();
    private SetPanePack setPanePack;


    public ImportSetDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.CONFIG_DIALOG_IMPORT_SET_SIZE,
                "Set importieren", true, false, DECO.SMALL);

        this.progData = progData;
        init(true);
    }

    public void close() {
        setPanePack.close();
        super.close();
    }

    @Override
    public void make() {
        btnCancel.setOnAction(a -> close());

        final Button btnHelp = PButton.helpButton("Set zurücksetzen",
                "\"Bestehende Sets durch neue ersetzen\"" +
                        P2LibConst.LINE_SEPARATORx2 +
                        "Damit werden alle Sets (auch eigene), die zum Abspielen" + P2LibConst.LINE_SEPARATOR +
                        "der Episoden gebraucht werden, gelöscht." + P2LibConst.LINE_SEPARATOR +
                        "Anschließend werden die aktuellen Standardsets eingerichtet." + P2LibConst.LINE_SEPARATOR +
                        "Damit kann dann direkt weitergearbeitet werden.");

        btnImport.setOnAction(event -> {
            importSet();
        });


        // vor import
        TitledPane tpDownPath = new DownPathPane(progData.primaryStage).makePath();
        tpDownPath.setMaxHeight(Double.MAX_VALUE);
        tpDownPath.setCollapsible(false);

        TitledPane tpPath = new PathPane(progData.primaryStage).makePath();
        tpPath.setMaxHeight(Double.MAX_VALUE);
        tpPath.setCollapsible(false);

        vBoxPath.setSpacing(10);
        vBoxPath.getChildren().addAll(tpDownPath, tpPath);
        vBoxPath.setStyle("-fx-background-color: -fx-background;");

        // nach Import
        setPanePack = new SetPanePack(P2LibConst.primaryStage);
        setPanePack.setMaxWidth(Double.MAX_VALUE);
        setPanePack.setMaxHeight(Double.MAX_VALUE);
        setPanePack.setStyle("-fx-background-color: -fx-background;");

        stackPane = new StackPane();
        stackPane.getChildren().addAll(vBoxPath, setPanePack);
        vBoxPath.toFront();
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(stackPane);

        addOkButton(btnImport);
        addCancelButton(btnCancel);
        addHlpButton(btnHelp);
    }

    private void importSet() {
        im = true;
        btnCancel.setText("Ok");
        btnImport.setDisable(true);

        progData.setDataList.clear();

        if (SetFactory.addSetTemplate(ImportSetDataFactory.getStandarset())) {
            PAlert.showInfoAlert("Set", "Set importieren", "Sets wurden importiert!", false);
        } else {
            PAlert.showErrorAlert("Set importieren", "Sets konnten nicht importiert werden!");
        }

        setPanePack.toFront();
    }
}
