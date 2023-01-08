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

import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DownloadMenu {
    final private VBox vBox;
    final private ProgData progData;
    BooleanProperty boolFilterOn = ProgConfig.DOWNLOAD_GUI_FILTER_ON;
    BooleanProperty boolInfoOn = ProgConfig.DOWNLOAD_GUI_INFO_ON;

    public DownloadMenu(VBox vBox) {
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
                "markierte Downloads starten", "markierten Download starten", ProgIcons.Icons.ICON_TOOLBAR_DOWNLOAD_START.getImageView());
        final ToolBarButton btStartAll = new ToolBarButton(vBox,
                "alle Downloads starten", "alle Downloads starten", ProgIcons.Icons.ICON_TOOLBAR_DOWNLOAD_START_ALL.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "alle laufenden Downloads stoppen", "alle laufenden Downloads stoppen", ProgIcons.Icons.ICON_TOOLBAR_DOWNLOAD_STOP.getImageView());

        final ToolBarButton btBack = new ToolBarButton(vBox,
                "markierte Downloads zurückstellen", "markierte Downloads zurückstellen", ProgIcons.Icons.ICON_TOOLBAR_DOWNLOAD_BACK.getImageView());
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "markierten Download löschen", "markierten Download löschen", ProgIcons.Icons.ICON_TOOLBAR_EPISODE_DEL.getImageView());
        final ToolBarButton btDownloadClear = new ToolBarButton(vBox,
                "Downloads aufräumen", "Liste der Downloads aufräumen", ProgIcons.Icons.ICON_TOOLBAR_DOWNLOAD_CLEAN.getImageView());

        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", ProgIcons.Icons.ICON_TOOLBAR_INFO.getImageView());

        btStart.setOnAction(a -> {
            List<DownloadData> downloadList = progData.downloadGui.getDownloadGuiController().getSelList();
            if (!downloadList.isEmpty()) {
                progData.downloadList.startDownloads(downloadList, false);
            }
        });
        btStartAll.setOnAction(a -> DownloadFactory.startAllDownloads());
        btStop.setOnAction(a -> progData.downloadList.stopAllDownloads());
        btBack.setOnAction(a -> DownloadListStartStopFactory.putBackDownloads());
        btDel.setOnAction(a -> {
            ArrayList<DownloadData> optionalDownload = progData.downloadGui.getDownloadGuiController().getSelList();
            if (!optionalDownload.isEmpty()) {
                DownloadListStartStopFactory.delDownloads(optionalDownload);
            }
        });
        btDownloadClear.setOnAction(a -> {
            DownloadFactory.cleanUpList();
        });
        btInfo.setOnAction(a -> progData.downloadGui.getDownloadGuiController().changeDownload());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Download-Menü anzeigen"));
        mb.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().addAll("btnFunction", "btnFunc-1");

        final MenuItem miDownloadStart = new MenuItem("Download starten");
        miDownloadStart.setOnAction(a -> progData.downloadGui.getDownloadGuiController().startDownload());

        final MenuItem miDownloadStop = new MenuItem("Download stoppen");
        miDownloadStop.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(false));

        final MenuItem miStopAll = new MenuItem("alle laufenden Download stoppen");
        miStopAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(true));

        final MenuItem miDownloadChange = new MenuItem("Download ändern");
        miDownloadChange.setOnAction(a -> progData.downloadGui.getDownloadGuiController().changeDownload());

        final MenuItem miDownloadDel = new MenuItem("Download löschen");
        miDownloadDel.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(false));

        final MenuItem miDownloadDelAll = new MenuItem("Alle Downloads löschen");
        miDownloadDelAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(true));

        MenuItem miStationInfo = new MenuItem("Download-Infos anzeigen");
        miStationInfo.setOnAction(a -> ProgConfig.DOWNLOAD_GUI_INFO_ON.setValue(!ProgConfig.DOWNLOAD_GUI_INFO_ON.get()));

        MenuItem miCopyUrl = new MenuItem("Download-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.downloadGui.getDownloadGuiController().copyUrl());

        MenuItem miCleanDownloadDir = new MenuItem("Downloadverzeichnis aufräumen");
        miCleanDownloadDir.setOnAction(a -> DownloadFactory.cleanUpDownloadDir());

        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(boolFilterOn);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(boolInfoOn);

        mb.getItems().addAll(miDownloadStart, miDownloadStop, miStopAll, miDownloadChange,
                miDownloadDel, miDownloadDelAll, miCopyUrl, miCleanDownloadDir);

        mb.getItems().add(new SeparatorMenuItem());
        mb.getItems().addAll(miShowFilter, miShowInfo);
        vBox.getChildren().add(mb);
    }
}
