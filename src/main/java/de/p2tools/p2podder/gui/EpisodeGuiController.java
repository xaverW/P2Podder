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
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.tools.Listener;
import de.p2tools.p2podder.gui.tools.table.Table;
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

public class EpisodeGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final ScrollPane scrollPane = new ScrollPane();

    private final TableView<Episode> tableView = new TableView<>();
    private EpisodeGuiInfoController episodeGuiInfoController;

    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbRunning = new RadioButton("gestartete");
    private final RadioButton rbWasShown = new RadioButton("gehörte");
    private final RadioButton rbNew = new RadioButton("neue");

    private final ProgData progData;
    private boolean bound = false;

    private DoubleProperty splitPaneProperty = ProgConfig.EPISODE_GUI_DIVIDER;
    private BooleanProperty boolInfoOn = ProgConfig.EPISODE_GUI_DIVIDER_ON;

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
        rbRunning.setToggleGroup(tg);
        rbWasShown.setToggleGroup(tg);
        rbNew.setToggleGroup(tg);

        HBox hBoxDown = new HBox();
        hBoxDown.setPadding(new Insets(5));
        hBoxDown.setSpacing(15);
        hBoxDown.getChildren().addAll(new Label("Episoden: "), rbAll, rbNew, rbRunning, rbWasShown);

        vBox.getChildren().addAll(hBoxDown, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());
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

    public void copyUrl() {
        final Optional<Episode> episode = getSel();
        if (!episode.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(episode.get().getEpisodeUrl());
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

    public void playEpisode() {
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            EpisodeFactory.playEpisode(episode.get());
        }
    }

    public void stopEpisode(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten
        if (all) {
            EpisodeFactory.stopAllEpisode();

        } else {
            final Optional<Episode> episode = getSel();
            if (episode.isPresent()) {
                EpisodeFactory.stopEpisode(episode.get());
            }
        }
    }

    public void stopEpisode(Episode episode) {
        EpisodeFactory.stopEpisode(episode);
    }

    public void deleteEpisode(boolean all) {
        if (all) {
            final ArrayList<Episode> list = getSelList();
            if (list.isEmpty()) {
                return;
            }
            EpisodeFactory.delEpisodes(list);

        } else {
            final Optional<Episode> episode = getSel();
            if (episode.isPresent()) {
                EpisodeFactory.delEpisodes(episode.get());
            }
        }
    }

    public void deleteEpisode(Episode episode) {
        EpisodeFactory.delEpisodes(episode);
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
        Listener.addListener(new Listener(Listener.EREIGNIS_SETDATA_CHANGED, EpisodeGuiController.class.getSimpleName()) {
            @Override
            public void pingFx() {
                tableView.refresh();
            }
        });
    }

    private void setInfoPane() {
        if (!boolInfoOn.getValue()) {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }
            splitPane.getItems().clear();
            splitPane.getItems().add(vBox);
        } else {
            bound = true;
            splitPane.getItems().clear();
            splitPane.getItems().addAll(vBox, episodeGuiInfoController);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
            SplitPane.setResizableWithParent(vBox, true);
        }
    }

    private void initFilter() {
        rbAll.setSelected(true);
        rbAll.selectedProperty().addListener((u, o, n) -> {
            progData.storedFilters.getActFilterSettings().setAll();
        });
        rbNew.selectedProperty().addListener((u, o, n) -> {
            progData.storedFilters.getActFilterSettings().setNew();
        });
        rbRunning.selectedProperty().addListener((u, o, n) -> {
            progData.storedFilters.getActFilterSettings().setRunning();
        });
        rbWasShown.selectedProperty().addListener((u, o, n) -> {
            progData.storedFilters.getActFilterSettings().setWasShown();
        });
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        new Table().setTable(tableView, Table.TABLE.EPISODE);

        tableView.setItems(progData.episodeStoredList.getEpisodeSortedList());
        progData.episodeStoredList.getEpisodeSortedList().comparatorProperty().bind(tableView.comparatorProperty());

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
