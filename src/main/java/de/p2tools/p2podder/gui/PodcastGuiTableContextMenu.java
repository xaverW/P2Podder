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

import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class PodcastGuiTableContextMenu {

    private final ProgData progData;
    private final PodcastGuiController podcastGuiController;
    private final TableView tableView;

    public PodcastGuiTableContextMenu(ProgData progData, PodcastGuiController podcastGuiController, TableView tableView) {
        this.progData = progData;
        this.podcastGuiController = podcastGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Podcast podcast) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, podcast);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Podcast podcast) {
        // Start/Save
        MenuItem miStart = new MenuItem("Podcast aktualisieren");
        miStart.setOnAction(a -> podcastGuiController.updateSelectedPodcast());

        MenuItem miDel = new MenuItem("Podcast löschen");
        miDel.setOnAction(a -> podcastGuiController.delPodcast());

        MenuItem miCopyUrl = new MenuItem("Podcast-URL kopieren");
        miCopyUrl.setOnAction(a -> PSystemUtils.copyToClipboard(podcast.getUrl()));

        MenuItem miStationInfo = new MenuItem("Podcast-Info anzeigen");
//        miStationInfo.setOnAction(a -> progData.episodeInfoDialogController.showStationInfo());

        miStart.setDisable(podcast == null);
        miDel.setDisable(podcast == null);
        miCopyUrl.setDisable(podcast == null);
        miStationInfo.setDisable(podcast == null);
        contextMenu.getItems().addAll(miStart, miDel, miCopyUrl, miStationInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.PODCAST));
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
