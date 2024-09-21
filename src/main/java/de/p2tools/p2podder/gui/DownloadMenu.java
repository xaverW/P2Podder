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

import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DownloadMenu {
    final private VBox vBox;
    final private ProgData progData;

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
                "Markierte Downloads starten", "Markierten Download starten", ProgIcons.ICON_TOOLBAR_DOWNLOAD_START.getImageView());
        final ToolBarButton btStartAll = new ToolBarButton(vBox,
                "Alle Downloads starten", "Alle Downloads starten", ProgIcons.ICON_TOOLBAR_DOWNLOAD_START_ALL.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "Alle laufenden Downloads stoppen", "Alle laufenden Downloads stoppen", ProgIcons.ICON_TOOLBAR_DOWNLOAD_STOP.getImageView());

        final ToolBarButton btBack = new ToolBarButton(vBox,
                "Markierte Downloads zurückstellen", "Markierte Downloads zurückstellen", ProgIcons.ICON_TOOLBAR_DOWNLOAD_BACK.getImageView());
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "Markierten Download löschen", "Markierten Download löschen", ProgIcons.ICON_TOOLBAR_EPISODE_DEL.getImageView());
        final ToolBarButton btDownloadClear = new ToolBarButton(vBox,
                "Downloads aufräumen", "Liste der Downloads aufräumen", ProgIcons.ICON_TOOLBAR_DOWNLOAD_CLEAN.getImageView());

        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", ProgIcons.ICON_TOOLBAR_INFO.getImageView());

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
        btInfo.setOnAction(a -> progData.downloadGui.getDownloadGuiController().showDownloadInfoDialog());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Download-Menü anzeigen"));
        mb.setGraphic(ProgIcons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().addAll("btnFunction", "btnFunc-0");

        final MenuItem miDownloadStart = new MenuItem("Download starten");
        miDownloadStart.setOnAction(a -> progData.downloadGui.getDownloadGuiController().startDownload());

        final MenuItem miDownloadStop = new MenuItem("Download stoppen");
        miDownloadStop.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(false));

        final MenuItem miStopAll = new MenuItem("Alle laufenden Download stoppen");
        miStopAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(true));

        final MenuItem miDownloadChange = new MenuItem("Download ändern");
        miDownloadChange.setOnAction(a -> progData.downloadGui.getDownloadGuiController().showDownloadInfoDialog());

        final MenuItem miDownloadDel = new MenuItem("Download löschen");
        miDownloadDel.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(false));

        final MenuItem miDownloadDelAll = new MenuItem("Alle Downloads löschen");
        miDownloadDelAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(true));

        MenuItem miStationInfo = new MenuItem("Download-Infos anzeigen");
        miStationInfo.setOnAction(a -> ProgConfig.DOWNLOAD__INFO_IS_SHOWING.setValue(!ProgConfig.DOWNLOAD__INFO_IS_SHOWING.get()));

        MenuItem miCopyUrl = new MenuItem("Download-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.downloadGui.getDownloadGuiController().copyUrl());

        MenuItem miCleanDownloadDir = new MenuItem("Downloadverzeichnis aufräumen");
        miCleanDownloadDir.setOnAction(a -> DownloadFactory.cleanUpDownloadDir());

        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.disableProperty().bind(ProgConfig.DOWNLOAD__FILTER_IS_RIP);
        miShowFilter.selectedProperty().bindBidirectional(ProgConfig.DOWNLOAD__FILTER_IS_SHOWING);

        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.disableProperty().bind(ProgConfig.DOWNLOAD__PANE_INFO_IS_RIP);
        miShowInfo.selectedProperty().bindBidirectional(ProgConfig.DOWNLOAD__INFO_IS_SHOWING);

        mb.getItems().addAll(miDownloadStart, miDownloadStop, miStopAll, miDownloadChange,
                miDownloadDel, miDownloadDelAll, miCopyUrl, miCleanDownloadDir);

        mb.getItems().add(new SeparatorMenuItem());
        mb.getItems().addAll(miShowFilter, miShowInfo);
        vBox.getChildren().add(mb);
    }
}
