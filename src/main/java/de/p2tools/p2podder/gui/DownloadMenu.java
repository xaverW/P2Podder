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
import de.p2tools.p2podder.controller.data.download.Download;
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
                "markierte Downloads starten", "markierten Download starten", new ProgIcons().ICON_TOOLBAR_DOWNLOAD_START);
        final ToolBarButton btStartAll = new ToolBarButton(vBox,
                "alle Downloads starten", "alle Downloads starten", new ProgIcons().ICON_TOOLBAR_DOWNLOAD_START_ALL);
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "alle laufenden Downloads stoppen", "alle laufenden Downloads stoppen", new ProgIcons().ICON_TOOLBAR_DOWNLOAD_STOP);

        final ToolBarButton btBack = new ToolBarButton(vBox,
                "markierte Downloads zur??ckstellen", "markierte Downloads zur??ckstellen", new ProgIcons().ICON_TOOLBAR_DOWNLOAD_BACK);
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "markierten Download l??schen", "markierten Download l??schen", new ProgIcons().ICON_TOOLBAR_EPISODE_DEL);
        final ToolBarButton btDownloadClear = new ToolBarButton(vBox,
                "Downloads aufr??umen", "Liste der Downloads aufr??umen", new ProgIcons().ICON_TOOLBAR_DOWNLOAD_CLEAN);

        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", new ProgIcons().ICON_TOOLBAR_INFO);

        btStart.setOnAction(a -> {
            List<Download> downloadList = progData.downloadGui.getDownloadGuiController().getSelList();
            if (!downloadList.isEmpty()) {
                progData.downloadList.startDownloads(downloadList, false);
            }
        });
        btStartAll.setOnAction(a -> DownloadFactory.startAllDownloads());
        btStop.setOnAction(a -> progData.downloadList.stopAllDownloads());
        btBack.setOnAction(a -> DownloadListStartStopFactory.putBackDownloads());
        btDel.setOnAction(a -> {
            ArrayList<Download> optionalDownload = progData.downloadGui.getDownloadGuiController().getSelList();
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
        mb.setTooltip(new Tooltip("Download-Men?? anzeigen"));
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miDownloadStart = new MenuItem("Download starten");
        miDownloadStart.setOnAction(a -> progData.downloadGui.getDownloadGuiController().startDownload());

        final MenuItem miDownloadStop = new MenuItem("Download stoppen");
        miDownloadStop.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(false));

        final MenuItem miStopAll = new MenuItem("alle laufenden Download stoppen");
        miStopAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().stopDownload(true));

        final MenuItem miDownloadChange = new MenuItem("Download ??ndern");
        miDownloadChange.setOnAction(a -> progData.downloadGui.getDownloadGuiController().changeDownload());

        final MenuItem miDownloadDel = new MenuItem("Download l??schen");
        miDownloadDel.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(false));

        final MenuItem miDownloadDelAll = new MenuItem("Alle Downloads l??schen");
        miDownloadDelAll.setOnAction(a -> progData.downloadGui.getDownloadGuiController().deleteDownloads(true));

        MenuItem miStationInfo = new MenuItem("Download-Infos anzeigen");
        miStationInfo.setOnAction(a -> ProgConfig.DOWNLOAD_GUI_INFO_ON.setValue(!ProgConfig.DOWNLOAD_GUI_INFO_ON.get()));

        MenuItem miCopyUrl = new MenuItem("Download-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.downloadGui.getDownloadGuiController().copyUrl());

        MenuItem miCleanDownloadDir = new MenuItem("Downloadverzeichnis aufr??umen");
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
