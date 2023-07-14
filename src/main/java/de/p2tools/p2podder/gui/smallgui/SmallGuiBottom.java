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
import de.p2tools.p2lib.guitools.PGuiTools;
import de.p2tools.p2lib.tools.date.PLDateFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIconsP2Podder;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SmallGuiBottom extends HBox {

    private final Button btnClearFilter = new Button("");
    private final Button btnShowFilter = new Button("");
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
        setSpacing(P2LibConst.DIST_HBOX);
        setAlignment(Pos.CENTER);

        Separator sp1 = new Separator(Orientation.VERTICAL);
        sp1.setPadding(new Insets(0, 5, 0, 5));
        Separator sp2 = new Separator(Orientation.VERTICAL);
        sp2.setPadding(new Insets(0, 5, 0, 5));

        HBox hBoxButton = new HBox(P2LibConst.DIST_BUTTON);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().addAll(btnRandom, sp1, btnPrev, btnNext, sp2, btnStart, btnPlayNext, btnStop);
        getChildren().addAll(btnShowFilter, btnClearFilter, PGuiTools.getVDistance(20),
                getInfoBox(), PGuiTools.getHBoxGrower(), hBoxButton);
    }

    private GridPane getInfoBox() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(0));
//        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(),
//                PColumnConstraints.getCcPrefSize(),
//                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(lblTitle, 0, ++row);
        gridPane.add(lblDate, 0, ++row);

        return gridPane;
    }

    void setInfoBox(Episode episode) {
        if (episode == null) {
            lblDate.setText("");
            lblPodcast.setText("");
            lblTitle.setText("");
        } else {
            String date = PLDateFactory.toString(episode.getPubDate());
            String dur = episode.getDuration();
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
        btnClearFilter.setTooltip(new Tooltip("Filter löschen"));
        btnClearFilter.getStyleClass().add("btnSmallGui");
        btnClearFilter.setGraphic(ProgIconsP2Podder.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(event -> {
            smallGuiPack.clearFilter();
        });

        btnShowFilter.setTooltip(new Tooltip("Filter anzeigen und ausblenden"));
        btnShowFilter.getStyleClass().add("btnSmallGui");
        btnShowFilter.setOnAction(event -> {
            ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.setValue(!ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.getValue());
            smallGuiPack.showFilter();
        });
        ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.addListener((u, o, n) -> setBtnIcon());
        setBtnIcon();

        btnRandom.setTooltip(new Tooltip("Eine Episode per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallGui");
        btnRandom.setGraphic(ProgIconsP2Podder.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> smallGuiPack.playRandomStation());

        btnPrev.setTooltip(new Tooltip("vorherige Episode auswählen"));
        btnPrev.getStyleClass().add("btnSmallGui");
        btnPrev.setGraphic(ProgIconsP2Podder.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> smallGuiPack.setPreviousEpisode());

        btnNext.setTooltip(new Tooltip("nächste Episode auswählen"));
        btnNext.getStyleClass().add("btnSmallGui");
        btnNext.setGraphic(ProgIconsP2Podder.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> smallGuiPack.setNextEpisode());

        btnStart.setTooltip(new Tooltip("Markierte Episoden abspielen"));
        btnStart.getStyleClass().add("btnSmallGui");
        btnStart.setGraphic(ProgIconsP2Podder.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> smallGuiPack.playEpisode());

        btnPlayNext.setTooltip(new Tooltip("Nächste gestartete Episode abspielen"));
        btnPlayNext.getStyleClass().add("btnSmallGui");
        btnPlayNext.setGraphic(ProgIconsP2Podder.ICON_BUTTON_PLAY_NEXT.getImageView());
        btnPlayNext.setOnAction(event -> EpisodeFactory.playNextEpisode(progData.smallGuiPack.getStage()));

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.getStyleClass().add("btnSmallGui");
        btnStop.setGraphic(ProgIconsP2Podder.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());
    }

    private void setBtnIcon() {
        if (ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.getValue()) {
            btnShowFilter.setGraphic(ProgIconsP2Podder.ICON_BUTTON_BACKWARD.getImageView());
        } else {
            btnShowFilter.setGraphic(ProgIconsP2Podder.ICON_BUTTON_FORWARD.getImageView());
        }
    }
}
