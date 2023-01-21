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

package de.p2tools.p2podder.gui.smallGui;

import de.p2tools.p2Lib.guiTools.PGuiTools;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class SmallGuiBottom extends HBox {

    private final Button btnClearFilter = new Button("");
    private final Button btnRandom = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnPlayNext = new Button("");
    private final Button btnStop = new Button("");
    private final ComboBox<Podcast> cboPodcast = new ComboBox<>();
    private final SmallGuiPack smallGuiPack;
    private final ProgData progData;


    public SmallGuiBottom(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();
        init();
        initStartButton();
        initCbo();
    }

    public void init() {
        setSpacing(5);
        setAlignment(Pos.CENTER);

        Separator sp = new Separator(Orientation.VERTICAL);
        sp.setPadding(new Insets(0, 5, 0, 5));

        HBox hBoxButton = new HBox(5);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().addAll(btnRandom, sp, btnPrev, btnNext, btnStart, btnPlayNext, btnStop);
        getChildren().addAll(cboPodcast, btnClearFilter, PGuiTools.getHBoxGrower(), hBoxButton);
    }

    private void initStartButton() {
        btnClearFilter.setTooltip(new Tooltip("Filter löschen"));
        btnClearFilter.getStyleClass().add("btnSmallGui");
        btnClearFilter.setGraphic(ProgIcons.Icons.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(event -> {
            cboPodcast.getSelectionModel().clearSelection();
        });

        btnRandom.setTooltip(new Tooltip("Eine Episode per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallGui");
        btnRandom.setGraphic(ProgIcons.Icons.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> smallGuiPack.playRandomStation());

        btnPrev.setTooltip(new Tooltip("vorherige Episode auswählen"));
        btnPrev.getStyleClass().add("btnSmallGui");
        btnPrev.setGraphic(ProgIcons.Icons.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> smallGuiPack.setPreviousEpisode());

        btnNext.setTooltip(new Tooltip("nächste Episode auswählen"));
        btnNext.getStyleClass().add("btnSmallGui");
        btnNext.setGraphic(ProgIcons.Icons.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> smallGuiPack.setNextEpisode());

        btnStart.setTooltip(new Tooltip("Episode abspielen"));
        btnStart.getStyleClass().add("btnSmallGui");
        btnStart.setGraphic(ProgIcons.Icons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> smallGuiPack.playEpisode());

        btnPlayNext.setTooltip(new Tooltip("Nächste gestartete Episode abspielen"));
        btnPlayNext.getStyleClass().add("btnSmallGui");
        btnPlayNext.setGraphic(ProgIcons.Icons.ICON_BUTTON_PLAY_NEXT.getImageView());
        btnPlayNext.setOnAction(event -> EpisodeFactory.playNextEpisode(progData.smallGuiPack.getStage()));

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.getStyleClass().add("btnSmallGui");
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());
    }

    private void initCbo() {
        progData.episodeList.addListener((u, o, n) -> {
            Platform.runLater(() -> {
                //kann durch Downloads außer der Reihe sein!!
                Podcast podcast = cboPodcast.getSelectionModel().getSelectedItem();
                cboPodcast.getItems().setAll(progData.episodeList.getPodcastList());
                if (podcast != null && cboPodcast.getItems().contains(podcast)) {
                    cboPodcast.getSelectionModel().select(podcast);
                } else {
                    cboPodcast.getSelectionModel().clearSelection();
                }
            });
        });

        cboPodcast.getItems().setAll(progData.episodeList.getPodcastList());
        cboPodcast.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            if (cboPodcast.getSelectionModel().isEmpty()) {
                progData.episodeFilterSmall.setPodcastId(0);

            } else if (n != null && o != n) {
                progData.episodeFilterSmall.setPodcastId(n.getId());
            }
        });
    }
}