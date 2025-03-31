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

import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.tools.table.TablePodcast;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class PodcastGuiTableContextMenu {

    private final ProgData progData;
    private final PodcastGuiController podcastGuiController;
    private final TablePodcast tableView;

    public PodcastGuiTableContextMenu(ProgData progData, PodcastGuiController podcastGuiController, TablePodcast tableView) {
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
        miStart.setOnAction(a -> progData.worker.updateSelectedPodcast());

        MenuItem miDel = new MenuItem("Podcast löschen");
        miDel.setOnAction(a -> podcastGuiController.delPodcast());

        final MenuItem miSetActive = new MenuItem("Podcasts einschalten");
        miSetActive.setOnAction(a -> progData.worker.setPodcastActive(true));
        final MenuItem miSetOffActive = new MenuItem("Podcasts ausschalten");
        miSetOffActive.setOnAction(a -> progData.worker.setPodcastActive(false));

        MenuItem miCopyUrl = new MenuItem("Podcast-URL kopieren");
        miCopyUrl.setOnAction(a -> P2ToolsFactory.copyToClipboard(podcast.getUrl()));

        MenuItem miStationInfo = new MenuItem("Podcast-Info anzeigen");
        // todo ??
//        miStationInfo.setOnAction(a -> progData.episodeInfoDialogController.showStationInfo());

        miStart.setDisable(podcast == null);
        miDel.setDisable(podcast == null);
        miSetActive.setDisable(podcast == null);
        miSetOffActive.setDisable(podcast == null);
        miCopyUrl.setDisable(podcast == null);
        miStationInfo.setDisable(podcast == null);
        boolean isActive = podcast != null && podcast.isActive();

        contextMenu.getItems().addAll(miStart, miDel, isActive ? miSetOffActive : miSetActive, miCopyUrl, miStationInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
