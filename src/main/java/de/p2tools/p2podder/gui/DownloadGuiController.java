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

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.guitools.P2RowMoveFactory;
import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneController;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneDto;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2podder.controller.config.PEvents;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import de.p2tools.p2podder.gui.dialog.DownloadInfoDialog;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TableDownload;
import de.p2tools.p2podder.gui.tools.table.TableRowDownload;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class DownloadGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final TableDownload tableView;
    private final RadioButton rbAll = new RadioButton("Alle");
    private final RadioButton rbStarted = new RadioButton("Gestartet");
    private final RadioButton rbLoading = new RadioButton("Lädt");
    private final RadioButton rbFinalized = new RadioButton("Abgeschlossen");

    private final P2ClosePaneController infoController;
    private final PaneDownloadInfo paneDownloadInfo;

    private final ProgData progData;
    private final BooleanProperty bound = new SimpleBooleanProperty(false);

    public DownloadGuiController() {
        progData = ProgData.getInstance();
        tableView = new TableDownload(Table.TABLE_ENUM.DOWNLOAD, progData);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        ToggleGroup tg = new ToggleGroup();
        rbAll.setToggleGroup(tg);
        rbStarted.setToggleGroup(tg);
        rbLoading.setToggleGroup(tg);
        rbFinalized.setToggleGroup(tg);

        HBox hBoxFilter = new HBox();
        hBoxFilter.setPadding(new Insets(5));
        hBoxFilter.setSpacing(15);
        hBoxFilter.getChildren().addAll(new Label("Downloads: "), rbAll, rbStarted, rbLoading, rbFinalized);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        vBox.getChildren().addAll(scrollPane, hBoxFilter);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        paneDownloadInfo = new PaneDownloadInfo();
        P2ClosePaneDto infoDto = new P2ClosePaneDto(paneDownloadInfo,
                ProgConfig.DOWNLOAD__PANE_INFO_IS_RIP,
                ProgConfig.DOWNLOAD__DIALOG_INFO_SIZE, ProgData.DOWNLOAD_TAB_ON,
                "Info", "Beschreibung", false);
        infoController = new P2ClosePaneController(infoDto, ProgConfig.DOWNLOAD__INFO_IS_SHOWING);

        ProgConfig.DOWNLOAD__INFO_IS_SHOWING.addListener((observable, oldValue, newValue) -> setInfoPane());
        ProgConfig.DOWNLOAD__PANE_INFO_IS_RIP.addListener((observable, oldValue, newValue) -> setInfoPane());

        setInfoPane();
        initFilter();
        initTable();
        initListener();
    }

    public void tableRefresh() {
        tableView.refresh();
    }

    public void isShown() {
        tableView.requestFocus();
        setSelectedDownload();
    }

    public int getDownloadsShown() {
        return tableView.getItems().size();
    }

    public void copyUrl() {
        final Optional<DownloadData> download = getSel();
        if (download.isEmpty()) {
            return;
        }
        P2ToolsFactory.copyToClipboard(download.get().getEpisodeUrl());
    }

    private void setSelectedDownload() {
        DownloadData download = tableView.getSelectionModel().getSelectedItem();
        if (download != null) {
            paneDownloadInfo.setDownload(download);
        } else {
            paneDownloadInfo.setDownload(null);
        }
    }

    public void startDownload() {
        // bezieht sich auf den ausgewählten Episoden
        final Optional<DownloadData> download = getSel();
        download.ifPresent(downloadData -> progData.downloadList.startDownloads(downloadData));
    }

    public void stopDownload(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Download
        if (all) {
            progData.downloadList.forEach(DownloadData::stopDownload);

        } else {
            final Optional<DownloadData> download = getSel();
            download.ifPresent(DownloadData::stopDownload);
        }
    }

    public void deleteDownloads(boolean all) {
        if (all) {
            final ArrayList<DownloadData> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Download gelöscht werden?";
            } else {
                text = "Sollen die Downloads gelöscht werden?";
            }
            if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Downloads löschen?", "Downloads löschen?", text)
                    .equals(P2Alert.BUTTON.YES)) {
                DownloadListStartStopFactory.delDownloads(list);
            }

        } else {
            final Optional<DownloadData> download = getSel();
            download.ifPresent(this::deleteDownloads);
        }
    }

    public void deleteDownloads(DownloadData download) {
        if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Download löschen?", "Download löschen?",
                "Soll der Download gelöscht werden?").equals(P2Alert.BUTTON.YES)) {
            DownloadListStartStopFactory.delDownloads(download);
        }
    }

    public void showDownloadInfoDialog() {
        final Optional<DownloadData> download = getSel();
        download.ifPresent(downloadData -> new DownloadInfoDialog(progData, downloadData));
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.DOWNLOAD);
    }

    public ArrayList<DownloadData> getSelList() {
        final ArrayList<DownloadData> ret = new ArrayList<>(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            P2Alert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<DownloadData> getSel() {
        return getSel(true);
    }

    public Optional<DownloadData> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        }

        if (show) {
            P2Alert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    private void initListener() {
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_SETDATA_CHANGED) {
            @Override
            public void pingGui(P2Event runEvent) {
                P2TableFactory.refreshTable(tableView);
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_REFRESH_TABLE) {
            @Override
            public void pingGui(P2Event runEvent) {
                P2TableFactory.refreshTable(tableView);
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_DOWNLOAD_REFRESH_TABLE) {
            @Override
            public void pingGui(P2Event runEvent) {
                P2TableFactory.refreshTable(tableView);
            }
        });
    }

    private void setInfoPane() {
        P2ClosePaneFactory.setSplit(bound, splitPane,
                infoController, false,
                vBox, ProgConfig.DOWNLOAD__INFO_DIVIDER, ProgConfig.DOWNLOAD__INFO_IS_SHOWING);
    }

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.downloadFilter.isAllProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.downloadFilter.isStartedProperty());
        rbLoading.selectedProperty().bindBidirectional(progData.downloadFilter.isLoadingProperty());
        rbFinalized.selectedProperty().bindBidirectional(progData.downloadFilter.isFinalizedProperty());
    }

    private void initTable() {
        Table.setTable(tableView);
        tableView.setItems(progData.downloadList.getSortedList());
        progData.downloadList.getSortedList().comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                showDownloadInfoDialog();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<DownloadData> optionalDownload = getSel(false);
                DownloadData download;
                download = optionalDownload.orElse(null);
                ContextMenu contextMenu = new DownloadGuiTableContextMenu(progData, this, tableView).getContextMenu(download);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.setRowFactory(new P2RowMoveFactory<>(tv -> {
            TableRowDownload<DownloadData> row = new TableRowDownload<>();
            row.hoverProperty().addListener((observable) -> {
                final DownloadData download = row.getItem();
                if (row.isHover() && download != null) {
                    paneDownloadInfo.setDownload(download);
                } else {
                    setSelectedDownload();
                }
            });
            return row;
        }));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedDownload();
        });
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (P2TableFactory.SPACE.match(event)) {
                P2TableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (P2TableFactory.SPACE_SHIFT.match(event)) {
                P2TableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }
        });
    }
}
