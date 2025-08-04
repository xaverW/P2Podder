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

import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.guitools.prange.P2RangeBox;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class SmallGuiTop extends HBox {


    private final ComboBox<Podcast> cboPodcast = new ComboBox<>();
    private final P2RangeBox slDur = new P2RangeBox("Länge:", true, 0, ProgConst.FILTER_DURATION_MAX_MINUTE);
    private final Button btnClearFilter = new Button();

    private final Button btnRadio = new Button("");
    private final Button btnClose = new Button();
    private final ProgData progData;

    private SmallGuiPack smallGuiPack;

    public SmallGuiTop(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();

        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> progData.smallGuiPack.close());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnRadio.setGraphic(ProgIcons.ICON_MENU_SMALL_PODDER_20.getImageView());

        btnClose.setTooltip(new Tooltip("Programm beenden"));
        btnClose.setOnAction(e -> {
            ProgQuitFactory.quit();
        });
        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnClose.setGraphic(ProgIcons.ICON_BUTTON_CLOSE.getImageView());

        btnClearFilter.setTooltip(new Tooltip("Filter löschen"));
        btnClearFilter.setOnAction(e -> {
            clearFilter();
        });
        btnClearFilter.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnClearFilter.setGraphic(ProgIcons.ICON_BUTTON_CLEAR_FILTER.getImageView());


        setAlignment(Pos.CENTER_LEFT);
        setSpacing(15);
        setAlignment(Pos.CENTER);
        getChildren().addAll(btnRadio, P2GuiTools.getHDistance(25), cboPodcast, slDur, btnClearFilter, P2GuiTools.getHBoxGrower(),
                P2GuiTools.getHBoxGrower(), btnClose);
        initFilter();
        initDurFilter();
    }

    public void clearFilter() {
        cboPodcast.getSelectionModel().clearSelection();
        progData.episodeFilterSmall.setDurationMin(0);
        progData.episodeFilterSmall.setDurationMax(ProgConst.FILTER_DURATION_MAX_MINUTE);
//        progData.episodeFilterSmall.clearFilter();
    }

    private void initFilter() {
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
        cboPodcast.getSelectionModel().select(progData.podcastList.getPodcastById(progData.episodeFilterSmall.getPodcastId()));
    }

    private void initDurFilter() {
        slDur.minValueProperty().set(progData.episodeFilterSmall.getDurationMin());
        slDur.maxValueProperty().set(progData.episodeFilterSmall.getDurationMax());

        progData.episodeFilterSmall.durationMinProperty().addListener(l ->
                slDur.minValueProperty().set(progData.episodeFilterSmall.getDurationMin()));
        progData.episodeFilterSmall.durationMaxProperty().addListener(l ->
                slDur.maxValueProperty().set(progData.episodeFilterSmall.getDurationMax()));

        slDur.getSlider().lowValueChangingProperty().addListener((u, o, n) -> {
            if (!n) {
                // dann ist er fertig
                progData.episodeFilterSmall.setDurationMin(slDur.getActMinValue());
            }
        });
        slDur.getSlider().highValueChangingProperty().addListener((u, o, n) -> {
            if (!n) {
                // dann ist er fertig
                progData.episodeFilterSmall.setDurationMax(slDur.getActMaxValue());
            }
        });
    }
}
