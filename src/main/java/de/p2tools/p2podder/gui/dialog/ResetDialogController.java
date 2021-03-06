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
import de.p2tools.p2Lib.guiTools.BigButton;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ResetDialogController extends PDialogExtra {

    final ProgData progData;
    final StackPane stackPane;

    public ResetDialogController(ProgData progData) {
        super(progData.primaryStage, null, "Programm zurücksetzen",
                true, false);

        this.progData = progData;
        stackPane = new StackPane();

        init(true);
    }

    @Override
    public void make() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(25);

        Label headerLabel = new Label("Einstellungen können komplett oder" + P2LibConst.LINE_SEPARATOR +
                "nur die Sets zum Abspielen" + P2LibConst.LINE_SEPARATOR +
                "zurückgesetzt werden!");
        headerLabel.setStyle("-fx-font-size: 1.5em;");

        // Set zurücksetzen
        BigButton cancelButton = new BigButton(new ProgIcons().ICON_BUTTON_QUIT,
                "Nichts ändern", "");
        cancelButton.setOnAction(e -> close());

        final Button btnHelp = PButton.helpButton(this.getStage(), "Programm zurücksetzen",
                HelpText.RESET_DIALOG);

        BigButton setButton = new BigButton(new ProgIcons().ICON_BUTTON_QUIT,
                "Einstellungen zum Abspielen zurücksetzen",
                "Es werden alle Programmsets (auch eigene) gelöscht" + P2LibConst.LINE_SEPARATOR +
                        "und die Standardsets wieder angelegt.");
        setButton.setOnAction(e -> {
            Platform.runLater(() -> {
                ImportSetDialogController importSetDialogController = new ImportSetDialogController(progData);
                importSetDialogController.close();
            });
            close();
        });

        // alle Einstellungen
        BigButton allButton = new BigButton(new ProgIcons().ICON_BUTTON_QUIT, "" +
                "Alle Einstellungen zurücksetzen!",
                "ACHTUNG!" + P2LibConst.LINE_SEPARATOR +
                        "Alle Einstellungen gehen verloren!");
        allButton.setOnAction(e -> {
            Text t = new Text("ALLE");
            t.setFont(Font.font(null, FontWeight.BOLD, -1));

            TextFlow tf = new TextFlow();
            tf.getChildren().addAll(new Text("Es werden "), t, new Text(" von Ihnen erzeugten Änderungen gelöscht." + P2LibConst.LINE_SEPARATORx2 +
                    "Möchten Sie wirklich alle Einstellungen zurücksetzen?"));

            if (PAlert.showAlert_yes_no_cancel("Einstellungen zurücksetzen",
                    "alle Einstellungen zurücksetzen!", tf, false) == PAlert.BUTTON.YES) {
                // damit wird vor dem Beenden das Konfig-Verzeichnis umbenannt und so startet das
                // Programm wie beim ersten Start
                ProgData.reset = true;
                Stage stage = null;
                if (progData.smallEpisodeGuiController != null) {
                    stage = progData.smallEpisodeGuiController.getSmallRadioGuiPack().getStage();

                } else if (progData.primaryStage.isShowing()) {
                    stage = progData.primaryStage;
                }
                ProgQuitFactory.quit(stage, false);
            }
        });

        gridPane.add(new ProgIcons().ICON_DIALOG_QUIT, 0, 0, 1, 1);
        gridPane.add(headerLabel, 1, 0);
        gridPane.add(cancelButton, 1, 1);
        gridPane.add(btnHelp, 2, 1);
        gridPane.add(setButton, 1, 2);
        gridPane.add(allButton, 1, 3);

        ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);
        getvBoxCont().getChildren().addAll(gridPane);
    }
}