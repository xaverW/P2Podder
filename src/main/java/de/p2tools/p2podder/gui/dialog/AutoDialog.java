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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2BigButton;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AutoDialog extends P2DialogExtra {

    private final GridPane gridPane = new GridPane();
    private final ProgData progData;
    private long max = 0;
    private final Label lbl = new Label();
    private final ProgressBar progressBar = new ProgressBar();

    public AutoDialog(ProgData progData) {
        super(progData.primaryStage, null,
                "Auto-Mode abbrechen", true, false, DECO.BORDER_SMALL);
        this.progData = progData;
        init(true);
    }

    public void make() {
        initCont();
    }

    private void initCont() {
        getVBoxCont().getChildren().add(gridPane);

        Label headerLabel = new Label("Es laufen noch Downloads!");
        headerLabel.setStyle("-fx-font-size: 1.5em;");

        // Auto-Mode beenden
        P2BigButton cancelButton = new P2BigButton(ProgIcons.ICON_BUTTON_QUIT.getImageView(),
                "Auto-Mode abbrechen", "Das Programm läuft dann normal weiter");
        cancelButton.setOnAction(e -> {
            ProgData.auto = false;
            close();
        });

        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));

        gridPane.add(headerLabel, 0, 0);
        gridPane.add(cancelButton, 0, 1);
        gridPane.add(progressBar, 0, 2);
        gridPane.add(lbl, 0, 3);
        GridPane.setHalignment(lbl, HPos.RIGHT);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize());
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        TextArea taDescription = new TextArea();
        taDescription.setMinHeight(25);
        GridPane.setVgrow(taDescription, Priority.ALWAYS);
        taDescription.setEditable(false);
        taDescription.setWrapText(true);
        taDescription.setBlendMode(BlendMode.DARKEN);
        taDescription.setText("Auto-Mode abbrechen");

        addProgress();
    }

    private void addProgress() {
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setProgress(0);
        setProgress();
        progData.pEventHandler.addListener(new P2Listener(Events.EREIGNIS_TIMER) {
            @Override
            public void pingGui(P2Event runEvent) {
                setProgress();
            }
        });
    }

    private void setProgress() {
        long sum = DownloadFactory.getDownloadsWaiting();
        double progress = 0;

        if (max < sum) {
            max = sum;
        }
        if (max > 0) {
            progress = (double) (100 * sum / max);
            progress = 100 - progress;
            progress = progress / 100;
        }

        lbl.setText("Anzahl: " + sum + " von " + max);
        progressBar.setProgress(progress);
    }
}