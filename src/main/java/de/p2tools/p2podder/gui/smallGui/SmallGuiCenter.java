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

package de.p2tools.p2podder.gui.smallGui;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TableSmallEpisode;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SmallGuiCenter extends VBox {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final TableSmallEpisode tableView;
    private final ListView<Podcast> listView = new ListView<>();

    private final ProgData progData;
    private SmallGuiPack smallGuiPack;


    public SmallGuiCenter(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();
        tableView = new TableSmallEpisode(Table.TABLE_ENUM.SMALL_EPISODE, progData);

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        getChildren().addAll(splitPane);

        initTable();
        initListView();
        initListener();
    }

    public void clearFilter() {
        listView.getSelectionModel().clearSelection();
    }

    public void showFilter() {
        if (splitPane.getDividers().size() > 0) {
            splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.SMALL_EPISODE_GUI_FILTER_DIVIDER);
        }
        splitPane.getItems().clear();

        if (ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.getValue()) {
            splitPane.getItems().addAll(listView, scrollPane);
            SplitPane.setResizableWithParent(listView, Boolean.FALSE);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.SMALL_EPISODE_GUI_FILTER_DIVIDER);
        } else {
            splitPane.getItems().addAll(scrollPane);
        }
    }

    public void tableRefresh() {
        tableView.refresh();
    }

    public void isShown() {
        tableView.requestFocus();
    }

    public int getFavouritesShown() {
        return tableView.getItems().size();
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.SMALL_EPISODE);
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

    public void playEpisode() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            EpisodeFactory.playEpisode(episode.get());
        }
    }

    public void setNextEpisode() {
        PTableFactory.selectNextRow(tableView);
    }

    public void setPreviousEpisode() {
        PTableFactory.selectPreviousRow(tableView);
    }

    public void playRandomStation() {
        Random r = new Random();
        Episode episode = tableView.getItems().get(r.nextInt(tableView.getItems().size()));
        if (episode != null) {
            EpisodeFactory.playEpisode(episode);
            tableView.getSelectionModel().select(episode);
            tableView.scrollTo(episode);
        }
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.EREIGNIS_SETDATA_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
            }
        });
    }

    private void initTable() {
        Table.setTable(tableView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        tableView.setItems(progData.episodeList.getSmallSortedList());
        progData.episodeList.getSmallSortedList().comparatorProperty().bind(tableView.comparatorProperty());

        tableView.getItems().addListener((ListChangeListener<Episode>) c -> {
            if (tableView.getItems().size() == 1) {
                // wenns nur eine Zeile gibt, dann gleich selektieren
                tableView.getSelectionModel().select(0);
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
                ContextMenu contextMenu =
                        new SmallGuiTableContextMenu(progData, this, tableView).getContextMenu(episode);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.setOnMouseClicked(m -> {
            final Optional<Episode> optionalDownload = getSel(false);
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.episodeInfoDialogController.toggleShowInfo();
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedEpisode();
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

    private void setSelectedEpisode() {
        Episode episode = tableView.getSelectionModel().getSelectedItem();
        progData.episodeInfoDialogController.setEpisode(episode);
    }

    private void initListView() {
        listView.setMinWidth(10);
        progData.episodeList.addListener((u, o, n) -> {
            Platform.runLater(() -> {
                //kann durch Downloads außer der Reihe sein!!
                Podcast podcast = listView.getSelectionModel().getSelectedItem();
                listView.getItems().setAll(progData.episodeList.getPodcastList());
                if (podcast != null && listView.getItems().contains(podcast)) {
                    listView.getSelectionModel().select(podcast);
                } else {
                    listView.getSelectionModel().clearSelection();
                }
            });
        });

        listView.getItems().setAll(progData.episodeList.getPodcastList());
        listView.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            if (listView.getSelectionModel().isEmpty()) {
                progData.episodeFilterSmall.setPodcastId(0);

            } else if (n != null && o != n) {
                progData.episodeFilterSmall.setPodcastId(n.getId());
            }
        });
    }
}
