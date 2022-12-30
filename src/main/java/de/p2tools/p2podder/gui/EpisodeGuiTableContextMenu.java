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

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.tools.table.TableEpisode;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class EpisodeGuiTableContextMenu {

    private final ProgData progData;
    private final EpisodeGuiController episodeGuiController;
    private final TableEpisode tableView;

    public EpisodeGuiTableContextMenu(ProgData progData, EpisodeGuiController episodeGuiController, TableEpisode tableView) {
        this.progData = progData;
        this.episodeGuiController = episodeGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Episode episode) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, episode);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Episode episode) {
        final boolean moreSets = progData.setDataList.size() > 1;
        if (moreSets) {
            Menu miStartWithSet = new Menu("Episode abspielen, Programm auswählen");
            miStartWithSet.setDisable(episode == null);
            for (SetData set : progData.setDataList) {
                MenuItem miStart = new MenuItem(set.getName());
                miStart.setOnAction(a -> EpisodeFactory.playEpisode(episode, set));
                miStartWithSet.getItems().add(miStart);
                miStart.setDisable(episode == null);
            }
            contextMenu.getItems().addAll(miStartWithSet);

        } else {
            MenuItem miStart = new MenuItem("Episode abspielen");
            miStart.setOnAction(a -> EpisodeFactory.playEpisode(episode));
            miStart.setDisable(episode == null);
            contextMenu.getItems().addAll(miStart);
        }

        MenuItem miStop = new MenuItem("Episode stoppen");
        miStop.setOnAction(a -> EpisodeFactory.stopEpisode(episode));
        MenuItem miStopAll = new MenuItem("alle Episoden stoppen");
        miStopAll.setOnAction(a -> EpisodeFactory.stopAllEpisode());
        MenuItem miCopyUrl = new MenuItem("Episode (URL) kopieren");
        miCopyUrl.setOnAction(a -> EpisodeFactory.copyUrl());

        MenuItem miChange = new MenuItem("Episode ändern");
        miChange.setOnAction(a -> progData.episodeInfoDialogController.toggleShowInfo());
        MenuItem miRemove = new MenuItem("Episode löschen");
        miRemove.setOnAction(a -> EpisodeFactory.delEpisode());

        miStop.setDisable(episode == null);
        miStopAll.setDisable(episode == null);
        miCopyUrl.setDisable(episode == null);
        miChange.setDisable(episode == null);
        miRemove.setDisable(episode == null);
        contextMenu.getItems().addAll(miStop, miStopAll, miCopyUrl, miChange, miRemove);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
