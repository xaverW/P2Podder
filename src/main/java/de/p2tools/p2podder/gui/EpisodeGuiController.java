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
import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneController;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneDto;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2podder.controller.config.PEvents;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TableEpisode;
import de.p2tools.p2podder.gui.tools.table.TableRowEpisode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

public class EpisodeGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final ScrollPane scrollPane = new ScrollPane();

    private final TableEpisode tableView;
    private P2ClosePaneController infoController;
    private PaneEpisodeInfo paneEpisodeInfo;

    private final RadioButton rbAll = new RadioButton("Alle");
    private final RadioButton rbNew = new RadioButton("Neu");
    private final RadioButton rbStarted = new RadioButton("Gestartet");
    private final RadioButton rbRunning = new RadioButton("Läuft");
    private final RadioButton rbWasShown = new RadioButton("Gehört");

    private final ProgData progData;
    private BooleanProperty bound = new SimpleBooleanProperty(false);

    public EpisodeGuiController() {
        progData = ProgData.getInstance();
        tableView = new TableEpisode(Table.TABLE_ENUM.EPISODE, progData);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        ToggleGroup tg = new ToggleGroup();
        rbAll.setToggleGroup(tg);
        rbStarted.setToggleGroup(tg);
        rbRunning.setToggleGroup(tg);
        rbWasShown.setToggleGroup(tg);
        rbNew.setToggleGroup(tg);

        HBox hBoxDown = new HBox();
        hBoxDown.setPadding(new Insets(5));
        hBoxDown.setSpacing(15);
        hBoxDown.getChildren().addAll(new Label("Episoden: "), rbAll, rbNew, rbStarted, rbRunning, rbWasShown);

        vBox.getChildren().addAll(hBoxDown, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        paneEpisodeInfo = new PaneEpisodeInfo();
        ArrayList<P2ClosePaneDto> list = new ArrayList<>();
        P2ClosePaneDto infoDto = new P2ClosePaneDto(paneEpisodeInfo,
                ProgConfig.EPISODE__PANE_INFO_IS_RIP,
                ProgConfig.EPISODE__DIALOG_INFO_SIZE, ProgData.EPISODE_TAB_ON,
                "Info", "Beschreibung", false);
        list.add(infoDto);
        infoController = new P2ClosePaneController(list, ProgConfig.EPISODE__INFO_IS_SHOWING);

        ProgConfig.EPISODE__INFO_IS_SHOWING.addListener((observable, oldValue, newValue) -> setInfoPane());
        ProgConfig.EPISODE__PANE_INFO_IS_RIP.addListener((observable, oldValue, newValue) -> setInfoPane());

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
        setSelectedEpisode();
    }

    public int getEpisodesShown() {
        return tableView.getItems().size();
    }

    private void setSelectedEpisode() {
        Episode episode = tableView.getSelectionModel().getSelectedItem();
        if (episode != null) {
            paneEpisodeInfo.setEpisode(episode);
            progData.episodeInfoDialogController.setEpisode(episode);
        } else {
            paneEpisodeInfo.setEpisode(null);
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.EPISODE);
    }

    public ArrayList<Episode> getSelList() {
        final ArrayList<Episode> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            P2Alert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<Episode> getSel() {
        return getSel(true);
    }

    public Optional<Episode> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        }

        if (show) {
            P2Alert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    public void setNextStation() {
        P2TableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        P2TableFactory.selectPreviousRow(tableView);
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
    }

    private void setInfoPane() {
        // hier wird das InfoPane ein- ausgeblendet
        P2ClosePaneFactory.setSplit(bound, splitPane,
                infoController, false,
                scrollPane, ProgConfig.EPISODE__INFO_DIVIDER, ProgConfig.EPISODE__INFO_IS_SHOWING);
    }

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.episodeFilter.isAllProperty());
        rbNew.selectedProperty().bindBidirectional(progData.episodeFilter.isNewProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.episodeFilter.isStartetProperty());
        rbRunning.selectedProperty().bindBidirectional(progData.episodeFilter.isRunningProperty());
        rbWasShown.selectedProperty().bindBidirectional(progData.episodeFilter.wasShownProperty());
    }

    private void initTable() {
        Table.setTable(tableView);

        tableView.setItems(progData.episodeList.getSortedList());
        progData.episodeList.getSortedList().comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.episodeInfoDialogController.toggleShowInfo();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Episode> optionalDownload = getSel(false);
                Episode episode;
                if (optionalDownload.isPresent()) {
                    episode = optionalDownload.get();
                } else {
                    episode = null;
                }
                ContextMenu contextMenu = new EpisodeGuiTableContextMenu(progData, this, tableView).getContextMenu(episode);
                tableView.setContextMenu(contextMenu);
            }
        });
        tableView.setRowFactory(tableView -> {
            TableRowEpisode<Episode> row = new TableRowEpisode<>();
            row.hoverProperty().addListener((observable) -> {
                final Episode episode = (Episode) row.getItem();
                if (row.isHover() && episode != null) {
                    paneEpisodeInfo.setEpisode(episode);
                    progData.episodeInfoDialogController.setEpisode(episode);
                } else {
                    setSelectedEpisode();
                }
            });
            return row;
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedEpisode();
        });
        tableView.getItems().addListener((ListChangeListener<Episode>) c -> {
            if (tableView.getItems().size() == 1) {
                // wenns nur eine Zeile gibt, dann gleich selektieren
                tableView.getSelectionModel().select(0);
            }
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
