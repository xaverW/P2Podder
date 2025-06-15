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

import de.p2tools.p2lib.tools.shortcut.P2ShortcutWorker;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.PShortCut;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.SetData;
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
                "Markierte Episoden abspielen", "Markierte Episoden abspielen", ProgIcons.ICON_TOOLBAR_START.getImageView());
        final ToolBarButton btPlayNext = new ToolBarButton(vBox,
                "Nächste gestartete Episoden abspielen", "Nächste gestartete Episoden abspielen",
                ProgIcons.ICON_TOOLBAR_PLAY_NEXT.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "Alle laufenden Episoden stoppen", "Alle laufenden Episoden stoppen", ProgIcons.ICON_TOOLBAR_STOP.getImageView());
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "Markierte Episoden löschen", "Markierte Episoden löschen", ProgIcons.ICON_TOOLBAR_DEL.getImageView());
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", ProgIcons.ICON_TOOLBAR_INFO.getImageView());

        btStart.setOnAction(a -> EpisodeFactory.playSelEpisode());
        btPlayNext.setOnAction(a -> EpisodeFactory.playNextEpisode());
        btStop.setOnAction(a -> EpisodeFactory.stopAllEpisode());
        btDel.setOnAction(a -> EpisodeFactory.delSelEpisode());
        btInfo.setOnAction(a -> progData.episodeInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Episoden-Menü anzeigen"));
        mb.setGraphic(ProgIcons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().addAll("btnFunction", "btnFunc-0");

        final boolean moreSets = progData.setDataList.size() > 1;
        if (moreSets) {
            Menu miStartWithSet = new Menu("Episode abspielen, Programm auswählen");
            for (SetData set : progData.setDataList) {
                MenuItem miStart = new MenuItem(set.getName());
                miStart.setOnAction(a -> EpisodeFactory.playEpisode(set));
                miStartWithSet.getItems().add(miStart);
            }
            mb.getItems().addAll(miStartWithSet);

        } else {
            MenuItem miStart = new MenuItem("Episode abspielen");
            miStart.setOnAction(a -> EpisodeFactory.playEpisode());
            P2ShortcutWorker.addShortCut(miStart, PShortCut.SHORTCUT_EPOSODE_START);
            mb.getItems().addAll(miStart);
        }

        final MenuItem miEpisodePlayNext = new MenuItem("Nächste Episode abspielen");
        miEpisodePlayNext.setOnAction(a -> EpisodeFactory.playNextEpisode());

        final MenuItem miEpisodeStop = new MenuItem("Episode stoppen");
        miEpisodeStop.setOnAction(a -> EpisodeFactory.stopEpisode());

        final MenuItem miStopAll = new MenuItem("Alle laufenden Episoden stoppen");
        miStopAll.setOnAction(a -> EpisodeFactory.stopAllEpisode());
        P2ShortcutWorker.addShortCut(miStopAll, PShortCut.SHORTCUT_EPISODE_STOP);

        final MenuItem miEpisodeDel = new MenuItem("Episode löschen");
        miEpisodeDel.setOnAction(a -> EpisodeFactory.delEpisode());

        MenuItem miDelShown = new MenuItem("Alle gespielten Episoden löschen");
        miDelShown.setOnAction(a -> EpisodeFactory.delAllShownEpisodes());

        MenuItem miCopyUrl = new MenuItem("Episoden-URL kopieren");
        miCopyUrl.setOnAction(a -> EpisodeFactory.copyUrl());

        mb.getItems().addAll(miEpisodePlayNext, miEpisodeStop, miStopAll, miEpisodeDel,
                miDelShown, new SeparatorMenuItem(), miCopyUrl);

        mb.getItems().add(new SeparatorMenuItem());
        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.disableProperty().bind(ProgConfig.EPISODE__FILTER_IS_RIP);
        miShowFilter.selectedProperty().bindBidirectional(ProgConfig.EPISODE__FILTER_IS_SHOWING);

        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.disableProperty().bind(ProgConfig.EPISODE__PANE_INFO_IS_RIP);
        miShowInfo.selectedProperty().bindBidirectional(ProgConfig.EPISODE__INFO_IS_SHOWING);
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
