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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import de.p2tools.p2podder.gui.dialog.DownloadInfoDialogController;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TableDownload;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
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
    private final ScrollPane scrollPane = new ScrollPane();
    private final TableDownload tableView;
    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbStarted = new RadioButton("gestartet");
    private final RadioButton rbLoading = new RadioButton("lädt");
    private final RadioButton rbFinalized = new RadioButton("abgeschlossen");

    private DownloadGuiInfoController downloadGuiInfoController;

    private final ProgData progData;
    private boolean bound = false;

    private DoubleProperty downloadGuiDivider = ProgConfig.DOWNLOAD_GUI_DIVIDER;
    private BooleanProperty boolInfoOn = ProgConfig.DOWNLOAD_GUI_INFO_ON;

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

        HBox hBoxDown = new HBox();
        hBoxDown.setPadding(new Insets(5));
        hBoxDown.setSpacing(15);
        hBoxDown.getChildren().addAll(new Label("Downloads: "), rbAll, rbStarted, rbLoading, rbFinalized);

        vBox.getChildren().addAll(hBoxDown, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());
        downloadGuiInfoController = new DownloadGuiInfoController();

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
        final Optional<Download> download = getSel();
        if (!download.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(download.get().getEpisodeUrl());
    }

    private void setSelectedDownload() {
        Download download = tableView.getSelectionModel().getSelectedItem();
        if (download != null) {
            downloadGuiInfoController.setDownload(download);
        } else {
            downloadGuiInfoController.setDownload(null);
        }
    }

    public void startDownload() {
        // bezieht sich auf den ausgewählten Episoden
        final Optional<Download> download = getSel();
        if (download.isPresent()) {
            progData.downloadList.startDownloads(download.get());
        }
    }

    public void stopDownload(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Download
        if (all) {
            progData.downloadList.stream().forEach(download -> download.stopDownload());

        } else {
            final Optional<Download> download = getSel();
            if (download.isPresent()) {
                download.get().stopDownload();
            }
        }
    }

    public void deleteDownloads(boolean all) {
        if (all) {
            final ArrayList<Download> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Download gelöscht werden?";
            } else {
                text = "Sollen die Downloads gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Downloads löschen?", "Downloads löschen?", text)
                    .equals(PAlert.BUTTON.YES)) {
                DownloadListStartStopFactory.delDownloads(list);
            }

        } else {
            final Optional<Download> download = getSel();
            if (download.isPresent()) {
                deleteDownloads(download.get());
            }
        }
    }

    public void deleteDownloads(Download download) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Download löschen?", "Download löschen?",
                "Soll der Download gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            DownloadListStartStopFactory.delDownloads(download);
        }
    }

    public void changeDownload() {
        final Optional<Download> download = getSel();
        if (download.isPresent()) {
            new DownloadInfoDialogController(progData, download.get());
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.DOWNLOAD);
    }

    public ArrayList<Download> getSelList() {
        final ArrayList<Download> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<Download> getSel() {
        return getSel(true);
    }

    public Optional<Download> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        }

        if (show) {
            PAlert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.EREIGNIS_SETDATA_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
            }
        });
    }

    private void setInfoPane() {
        if (!boolInfoOn.getValue()) {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(downloadGuiDivider);
            }
            splitPane.getItems().clear();
            splitPane.getItems().add(vBox);
        } else {
            bound = true;
            splitPane.getItems().clear();
            splitPane.getItems().addAll(vBox, downloadGuiInfoController);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(downloadGuiDivider);
            SplitPane.setResizableWithParent(vBox, true);
        }
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
        PTableFactory.refreshTable(tableView);

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                changeDownload();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Download> optionalDownload = getSel(false);
                Download download;
                if (optionalDownload.isPresent()) {
                    download = optionalDownload.get();
                } else {
                    download = null;
                }
                ContextMenu contextMenu = new DownloadGuiTableContextMenu(progData, this, tableView).getContextMenu(download);
                tableView.setContextMenu(contextMenu);
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> setSelectedDownload());
        });
        tableView.getItems().addListener((ListChangeListener<Download>) c -> {
            if (tableView.getItems().size() == 1) {
                // wenns nur eine Zeile gibt, dann gleich selektieren
                tableView.getSelectionModel().select(0);
            }
        });
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (PTableFactory.SPACE.match(event)) {
                PTableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (PTableFactory.SPACE_SHIFT.match(event)) {
                PTableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }
        });
    }
}
