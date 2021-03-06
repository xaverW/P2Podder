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

package de.p2tools.p2podder.gui;

import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.P2PodderShortCuts;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class EpisodeMenu {
    final private VBox vBox;
    final private ProgData progData;

    public EpisodeMenu(VBox vBox) {
        this.vBox = vBox;
        progData = ProgData.getInstance();
    }

    public void init() {
        vBox.getChildren().clear();
        initMenu();
        initButton();
    }

    private void initButton() {
        VBox vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(0);
        vBoxSpace.setMinHeight(0);
        vBox.getChildren().add(vBoxSpace);

        final ToolBarButton btStart = new ToolBarButton(vBox,
                "Markierte Episoden abspielen", "Markierte Episoden abspielen", new ProgIcons().ICON_TOOLBAR_EPISODE_START);
        final ToolBarButton btPlayNext = new ToolBarButton(vBox,
                "Nächste gestartete Episoden abspielen", "Nächste gestartete Episoden abspielen",
                new ProgIcons().ICON_TOOLBAR_EPISODE_PLAY_NEXT);
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "Alle laufenden Episoden stoppen", "Alle laufenden Episoden stoppen", new ProgIcons().ICON_TOOLBAR_EPISODE_STOP);
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "Markierte Episoden löschen", "Markierte Episoden löschen", new ProgIcons().ICON_TOOLBAR_EPISODE_DEL);
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", new ProgIcons().ICON_TOOLBAR_INFO);

        btStart.setOnAction(a -> EpisodeFactory.playSelEpisode());
        btPlayNext.setOnAction(a -> EpisodeFactory.playNextEpisode());
        btStop.setOnAction(a -> EpisodeFactory.stopAllEpisode());
        btDel.setOnAction(a -> EpisodeFactory.delSelEpisode());
        btInfo.setOnAction(a -> progData.episodeInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Episoden-Menü anzeigen"));
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miEpisodeStart = new MenuItem("Episode abspielen");
        miEpisodeStart.setOnAction(a -> EpisodeFactory.playEpisode());
        PShortcutWorker.addShortCut(miEpisodeStart, P2PodderShortCuts.SHORTCUT_EPOSODE_START);

        final MenuItem miEpisodePlayNext = new MenuItem("Nächste Episode abspielen");
        miEpisodePlayNext.setOnAction(a -> EpisodeFactory.playNextEpisode());

        final MenuItem miEpisodeStop = new MenuItem("Episode stoppen");
        miEpisodeStop.setOnAction(a -> EpisodeFactory.stopEpisode());

        final MenuItem miStopAll = new MenuItem("Alle laufenden Episoden stoppen");
        miStopAll.setOnAction(a -> EpisodeFactory.stopAllEpisode());
        PShortcutWorker.addShortCut(miStopAll, P2PodderShortCuts.SHORTCUT_EPISODE_STOP);

        final MenuItem miEpisodeDel = new MenuItem("Episode löschen");
        miEpisodeDel.setOnAction(a -> EpisodeFactory.delEpisode());

        MenuItem miDelShown = new MenuItem("Alle gespielten Episoden löschen");
        miDelShown.setOnAction(a -> EpisodeFactory.delAllShownEpisodes());

        MenuItem miCopyUrl = new MenuItem("Episoden-URL kopieren");
        miCopyUrl.setOnAction(a -> EpisodeFactory.copyUrl());

        mb.getItems().addAll(miEpisodeStart, miEpisodePlayNext, miEpisodeStop, miStopAll, miEpisodeDel,
                miDelShown, new SeparatorMenuItem(), miCopyUrl);

        mb.getItems().add(new SeparatorMenuItem());
        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(ProgConfig.EPISODE_GUI_FILTER_ON);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(ProgConfig.EPISODE_GUI_DIVIDER_ON);
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
