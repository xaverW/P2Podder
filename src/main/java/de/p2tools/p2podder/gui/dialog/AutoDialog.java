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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2BigButton;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.PEvents;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AutoDialog extends P2DialogExtra {

    private final ProgData progData;
    private long maxDownload = 0;
    private final Label lblParse = new Label();
    private final Label lblDownload = new Label();
    private final ProgressBar progressBarParse = new ProgressBar();
    private final ProgressBar progressBarDownload = new ProgressBar();

    public AutoDialog(ProgData progData) {
        super(progData.primaryStage, null,
                "Auto-Mode", true, false, DECO.BORDER_SMALL);
        this.progData = progData;
        init(true);
    }

    public void make() {
        initCont();
    }

    private void initCont() {
        Label headerLabel = new Label("Downloads suchen und laden!");
        headerLabel.setStyle("-fx-font-size: 1.5em;");

        // Auto-Mode beenden
        P2BigButton cancelButton = new P2BigButton(ProgIcons.ICON_BUTTON_QUIT.getImageView(),
                "Auto-Mode abbrechen", "Das Programm läuft dann normal weiter");
        cancelButton.setOnAction(e -> {
            ProgData.auto = false;
            close();
        });

        VBox vBox = getVBoxCont();
        vBox.setPadding(new Insets(15));
        vBox.setSpacing(2);

        vBox.getChildren().add(headerLabel);
        vBox.getChildren().add(P2GuiTools.getHDistance(15));
        vBox.getChildren().add(cancelButton);
        vBox.getChildren().add(P2GuiTools.getHDistance(25));

        vBox.getChildren().add(progressBarParse);
        HBox hBox = new HBox(P2LibConst.SPACING_HBOX);
        hBox.getChildren().addAll(new Label("Suchen"), P2GuiTools.getHBoxGrower(), lblParse);
        vBox.getChildren().add(hBox);

        vBox.getChildren().add(P2GuiTools.getHDistance(15));

        vBox.getChildren().add(progressBarDownload);
        hBox = new HBox(P2LibConst.SPACING_HBOX);
        hBox.getChildren().addAll(new Label("Laden"), P2GuiTools.getHBoxGrower(), lblDownload);
        vBox.getChildren().add(hBox);
        GridPane.setHalignment(lblDownload, HPos.RIGHT);

        addProgress();
    }

    private void addProgress() {
        progressBarParse.setMaxWidth(Double.MAX_VALUE);
        progressBarParse.setProgress(0);
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_PARSE_PODCAST) {
            @Override
            public void pingGui(P2Event runEvent) {
                setProgressParse(runEvent);
            }
        });

        progressBarDownload.setMaxWidth(Double.MAX_VALUE);
        progressBarDownload.setProgress(0);
        setProgressDownload();
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
            @Override
            public void pingGui(P2Event runEvent) {
                setProgressDownload();
            }
        });
    }

    private void setProgressParse(P2Event events) {
        int countParse = (int) events.getAct();
        long podcastSize = progData.podcastList.size();
        double progress;

        progress = 1.0 * podcastSize / countParse;
        progress = 100 / progress / 100;

        lblParse.setText("Anzahl: " + countParse + " von " + podcastSize);
        progressBarParse.setProgress(progress);
    }

    private void setProgressDownload() {
        long sum = DownloadFactory.getDownloadsWaiting();
        double progress = 0;

        if (maxDownload < sum) {
            maxDownload = sum;
        }
        if (maxDownload > 0) {
            progress = 100.0 * sum / maxDownload;
            progress = 100 - progress;
            progress = progress / 100;
        }

        lblDownload.setText("Anzahl: " + (maxDownload - sum) + " von " + maxDownload);
        progressBarDownload.setProgress(progress);
    }
}