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


import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.BigButton;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2podder.controller.data.ProgIcons;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class QuitDialogController extends PDialogExtra {

    private final StackPane stackPane = new StackPane();
    private final PMaskerPane maskerPane = new PMaskerPane();
    private boolean canQuit = false;

    public QuitDialogController(Stage stage) {
        super(stage, null, "Programm beenden", true, false);
        init(true);
    }

    @Override
    public void make() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(25);

        maskerPane.setMaskerVisible(false);
        maskerPane.setButtonText("Abbrechen");
        maskerPane.getButton().setOnAction(a -> close());

        Label headerLabel = new Label("Es laufen noch Episoden!");
        headerLabel.setStyle("-fx-font-size: 1.5em;");

        BigButton cancelButton = new BigButton(ProgIcons.Icons.ICON_BUTTON_QUIT.getImageView(),
                "Nicht beenden", "");
        cancelButton.setOnAction(e -> {
            close();
        });

        BigButton quitButton = new BigButton(ProgIcons.Icons.ICON_BUTTON_QUIT.getImageView(),
                "Beenden", "Alle laufenden Episoden abbrechen und das Programm beenden.");
        quitButton.setOnAction(e -> {
            canQuit = true;
            close();
        });

        gridPane.add(ProgIcons.Icons.ICON_DIALOG_QUIT.getImageView(), 0, 0, 1, 1);
        gridPane.add(headerLabel, 1, 0);
        gridPane.add(cancelButton, 1, 1);
        gridPane.add(quitButton, 1, 2);

        ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);
        stackPane.getChildren().addAll(gridPane, maskerPane);
        getVBoxCont().getChildren().addAll(stackPane);
    }

    public void close() {
        maskerPane.switchOffMasker();
        super.close();
    }

    public boolean canTerminate() {
        return canQuit;
    }
}