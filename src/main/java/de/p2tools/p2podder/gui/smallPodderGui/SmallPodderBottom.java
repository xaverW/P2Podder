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

package de.p2tools.p2podder.gui.smallPodderGui;

import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SmallPodderBottom {

    ProgData progData;
    private final Button btnShowFilter = new Button("");
    private final Button btnClearFilter = new Button("");
    private final Button btnRandom = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private Button btnRadio;
    private final HBox hBoxBottom;
    private SmallEpisodeGuiController smallEpisodeGuiController;

    public SmallPodderBottom(SmallEpisodeGuiController smallEpisodeGuiController) {
        progData = ProgData.getInstance();
        this.hBoxBottom = new HBox();
        this.smallEpisodeGuiController = smallEpisodeGuiController;
        initStartButton();
    }

    public HBox init() {
        HBox hBoxSpace1 = new HBox();
        HBox.setHgrow(hBoxSpace1, Priority.ALWAYS);

        HBox hBoxButton = new HBox(5);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        Separator sp = new Separator(Orientation.VERTICAL);
        sp.setPadding(new Insets(0, 5, 0, 5));
        hBoxButton.getChildren().addAll(btnRandom, sp, btnPrev, btnNext, btnStart, btnStop);
        hBoxBottom.getChildren().addAll(btnRadio, btnShowFilter, btnClearFilter, hBoxSpace1, hBoxButton);
        return hBoxBottom;
    }

    private void initStartButton() {
        btnRadio = new Button("");
        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> progData.smallPodderGuiPack.changeGui());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().add("btnTab");
        btnRadio.setGraphic(new ProgIcons().ICON_TOOLBAR_SMALL_PODDER_20);

        btnShowFilter.setTooltip(new Tooltip("Filter anzeigen/ausblenden"));
        btnShowFilter.getStyleClass().add("btnSmallPodder");
        btnShowFilter.setGraphic(new ProgIcons().ICON_BUTTON_FILTER);
        btnShowFilter.setOnAction(event -> ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.setValue(!ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.get()));

        btnClearFilter.setTooltip(new Tooltip("Filter löschen"));
        btnClearFilter.getStyleClass().add("btnSmallPodder");
        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_CLEAR_FILTER);
        btnClearFilter.setOnAction(event -> progData.episodeFilterSmall.clearFilter());

        btnRandom.setTooltip(new Tooltip("Eine Episode per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallPodder");
        btnRandom.setGraphic(new ProgIcons().ICON_BUTTON_RANDOM);
        btnRandom.setOnAction(event -> smallEpisodeGuiController.playRandomStation());

        btnPrev.setTooltip(new Tooltip("vorherige Episode auswählen"));
        btnPrev.getStyleClass().add("btnSmallPodder");
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> smallEpisodeGuiController.setPreviousEpisode());

        btnNext.setTooltip(new Tooltip("nächste Episode auswählen"));
        btnNext.getStyleClass().add("btnSmallPodder");
        btnNext.setGraphic(new ProgIcons().ICON_BUTTON_NEXT);
        btnNext.setOnAction(event -> smallEpisodeGuiController.setNextEpisode());

        btnStart.setTooltip(new Tooltip("Episode abspielen"));
        btnStart.getStyleClass().add("btnSmallPodder");
        btnStart.setGraphic(new ProgIcons().ICON_BUTTON_PLAY);
        btnStart.setOnAction(event -> smallEpisodeGuiController.playEpisode());

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.getStyleClass().add("btnSmallPodder");
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP_PLAY);
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());
    }
}
