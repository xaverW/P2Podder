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
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class EpisodeGuiTableContextMenu {

    private final ProgData progData;
    private final EpisodeGuiController episodeGuiController;
    private final TableView tableView;

    public EpisodeGuiTableContextMenu(ProgData progData, EpisodeGuiController episodeGuiController, TableView tableView) {
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
        MenuItem miStart = new MenuItem("Episode abspielen");
        miStart.setOnAction(a -> EpisodeFactory.playEpisode(episode));
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

        miStart.setDisable(episode == null);
        miStop.setDisable(episode == null);
        miStopAll.setDisable(episode == null);
        miCopyUrl.setDisable(episode == null);
        miChange.setDisable(episode == null);
        miRemove.setDisable(episode == null);

        contextMenu.getItems().addAll(miStart, miStop, miStopAll, miCopyUrl, miChange, miRemove);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.EPISODE));

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
