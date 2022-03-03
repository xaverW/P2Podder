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
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class DownloadGuiTableContextMenu {

    private final ProgData progData;
    private final DownloadGuiController downloadGuiController;
    private final TableView tableView;

    public DownloadGuiTableContextMenu(ProgData progData, DownloadGuiController downloadGuiController, TableView tableView) {
        this.progData = progData;
        this.downloadGuiController = downloadGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Download download) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, download);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Download download) {
        MenuItem miStart = new MenuItem("Download starten");
        miStart.setOnAction(a -> downloadGuiController.startDownload());
        MenuItem miStop = new MenuItem("Download stoppen");
        miStop.setOnAction(a -> downloadGuiController.stopDownload(false));
        MenuItem miStopAll = new MenuItem("alle Downloads stoppen");
        miStopAll.setOnAction(a -> downloadGuiController.stopDownload(true /* alle */));
        MenuItem miCopyUrl = new MenuItem("Download (URL) kopieren");
        miCopyUrl.setOnAction(a -> downloadGuiController.copyUrl());

        MenuItem miChange = new MenuItem("Download ändern");
        miChange.setOnAction(a -> downloadGuiController.changeDownload());
        MenuItem miRemove = new MenuItem("Download löschen");
        miRemove.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(false));

        miStart.setDisable(download == null);
        miStop.setDisable(download == null);
        miStopAll.setDisable(download == null);
        miCopyUrl.setDisable(download == null);
        miChange.setDisable(download == null);
        miRemove.setDisable(download == null);

        contextMenu.getItems().addAll(miStart, miStop, miStopAll, miCopyUrl, miChange, miRemove);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.DOWNOAD));

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
