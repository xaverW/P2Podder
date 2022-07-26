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
import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.application.Platform;
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

    private final TableView<Episode> tableView = new TableView<>();
    private EpisodeGuiInfoController episodeGuiInfoController;

    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbStarted = new RadioButton("gestartete");
    private final RadioButton rbRunning = new RadioButton("läuft");
    private final RadioButton rbWasShown = new RadioButton("gehörte");
    private final RadioButton rbNew = new RadioButton("neue");

    private final ProgData progData;
    private boolean bound = false;

    public EpisodeGuiController() {
        progData = ProgData.getInstance();

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

        ProgConfig.EPISODE_GUI_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setInfoPane());
        episodeGuiInfoController = new EpisodeGuiInfoController();

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
            episodeGuiInfoController.setEpisode(episode);
            progData.episodeInfoDialogController.setEpisode(episode);
        } else {
            episodeGuiInfoController.setEpisode(null);
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.EPISODE);
    }

    public ArrayList<Episode> getSelList() {
        final ArrayList<Episode> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
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
            PAlert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    public void setNextStation() {
        PTableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        PTableFactory.selectPreviousRow(tableView);
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.EREIGNIS_SETDATA_CHANGED) {
            @Override
            public void ping(Event runEvent) {
                Table.refresh_table(tableView);
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void ping(Event runEvent) {
                Table.refresh_table(tableView);
            }
        });
    }

    private void setInfoPane() {
        if (!ProgConfig.EPISODE_GUI_DIVIDER_ON.getValue()) {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.EPISODE_GUI_DIVIDER);
            }
            splitPane.getItems().clear();
            splitPane.getItems().add(vBox);
        } else {
            bound = true;
            splitPane.getItems().clear();
            splitPane.getItems().addAll(vBox, episodeGuiInfoController);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.EPISODE_GUI_DIVIDER);
            SplitPane.setResizableWithParent(vBox, true);
        }
    }

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.episodeFilter.isAllProperty());
        rbNew.selectedProperty().bindBidirectional(progData.episodeFilter.isNewProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.episodeFilter.isStartetProperty());
        rbRunning.selectedProperty().bindBidirectional(progData.episodeFilter.isRunningProperty());
        rbWasShown.selectedProperty().bindBidirectional(progData.episodeFilter.wasShownProperty());
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        new Table().setTable(tableView, Table.TABLE.EPISODE);

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
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> setSelectedEpisode());
        });
        tableView.getItems().addListener((ListChangeListener<Episode>) c -> {
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
