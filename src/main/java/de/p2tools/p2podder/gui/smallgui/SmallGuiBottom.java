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

package de.p2tools.p2podder.gui.smallgui;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SmallGuiBottom extends VBox {

    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbStarted = new RadioButton("gestartete");
    private final RadioButton rbRunning = new RadioButton("läuft");
    private final RadioButton rbWasShown = new RadioButton("gehörte");
    private final RadioButton rbNew = new RadioButton("neue");

    private final Button btnRandom = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnPlayNext = new Button("");
    private final Button btnStop = new Button("");
    private final Label lblDate = new Label("");
    private final Label lblPodcast = new Label("");
    private final Label lblTitle = new Label("");
    private final SmallGuiPack smallGuiPack;
    private final ProgData progData;


    public SmallGuiBottom(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();
        init();
        initStartButton();
    }

    public void init() {
        setSpacing(0);
        setAlignment(Pos.CENTER);

        ToggleGroup tg = new ToggleGroup();
        rbAll.setToggleGroup(tg);
        rbStarted.setToggleGroup(tg);
        rbRunning.setToggleGroup(tg);
        rbWasShown.setToggleGroup(tg);
        rbNew.setToggleGroup(tg);

        Separator sp1 = new Separator(Orientation.VERTICAL);
        sp1.setPadding(new Insets(0, 5, 0, 5));
        Separator sp2 = new Separator(Orientation.VERTICAL);
        sp1.setPadding(new Insets(0, 5, 0, 5));
        Separator sp3 = new Separator(Orientation.VERTICAL);
        sp2.setPadding(new Insets(0, 5, 0, 5));

        HBox hBoxButton = new HBox(P2LibConst.DIST_BUTTON);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().addAll(
                new Label("Episoden: "), rbAll, rbNew, rbStarted, rbRunning, rbWasShown,
                P2GuiTools.getHBoxGrower(),
                btnRandom,
                sp2,
                btnPrev, btnNext,
                sp3,
                btnStart, btnPlayNext, btnStop);
        getChildren().addAll(getInfoBox(), hBoxButton);

        initFilter();
    }

    private GridPane getInfoBox() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setPadding(new Insets(0));
        gridPane.add(lblTitle, 0, 0);
        gridPane.add(lblDate, 1, 0);
        return gridPane;
    }

    void setInfoBox(Episode episode) {
        if (episode == null) {
            lblDate.setText("");
            lblPodcast.setText("");
            lblTitle.setText("");
        } else {
            String date = P2LDateFactory.toString(episode.getPubDate());
            String dur = episode.getDurationStr();
            if (!date.isEmpty() && !dur.isEmpty()) {
                lblDate.setText(date + "  /  " + dur);
            } else {
                lblDate.setText(date + dur);
            }
            lblPodcast.setText(episode.getPodcastName());
            lblTitle.setText(episode.getEpisodeTitle());
        }
    }

    private void initStartButton() {
        btnRandom.setTooltip(new Tooltip("Eine Episode per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallGui");
        btnRandom.setGraphic(ProgIcons.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> smallGuiPack.playRandomStation());

        btnPrev.setTooltip(new Tooltip("vorherige Episode auswählen"));
        btnPrev.getStyleClass().add("btnSmallGui");
        btnPrev.setGraphic(ProgIcons.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> smallGuiPack.setPreviousEpisode());

        btnNext.setTooltip(new Tooltip("nächste Episode auswählen"));
        btnNext.getStyleClass().add("btnSmallGui");
        btnNext.setGraphic(ProgIcons.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> smallGuiPack.setNextEpisode());

        btnStart.setTooltip(new Tooltip("Markierte Episoden abspielen"));
        btnStart.getStyleClass().add("btnSmallGui");
        btnStart.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> smallGuiPack.playEpisode());

        btnPlayNext.setTooltip(new Tooltip("Nächste gestartete Episode abspielen"));
        btnPlayNext.getStyleClass().add("btnSmallGui");
        btnPlayNext.setGraphic(ProgIcons.ICON_BUTTON_PLAY_NEXT.getImageView());
        btnPlayNext.setOnAction(event -> EpisodeFactory.playNextEpisode(progData.smallGuiPack.getStage()));

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.getStyleClass().add("btnSmallGui");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());
    }

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isAllProperty());
        rbNew.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isNewProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isStartedProperty());
        rbRunning.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isRunningProperty());
        rbWasShown.selectedProperty().bindBidirectional(progData.episodeFilterSmall.wasShownProperty());
    }
}
