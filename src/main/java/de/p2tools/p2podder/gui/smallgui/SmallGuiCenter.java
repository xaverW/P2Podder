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

package de.p2tools.p2podder.gui.smallgui;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TableRowEpisode;
import de.p2tools.p2podder.gui.tools.table.TableSmallEpisode;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SmallGuiCenter extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableSmallEpisode tableView;

    private final ProgData progData;
    private SmallGuiPack smallGuiPack;


    public SmallGuiCenter(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();
        tableView = new TableSmallEpisode(Table.TABLE_ENUM.SMALL_EPISODE, progData);

        getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        initTable();
        initListener();
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

    public void playEpisode() {
        // bezieht sich auf die ausgewählten Episoden
        final ArrayList<Episode> ret = getSelList();
        EpisodeFactory.playEpisode(ret, null);
    }

    public void setNextEpisode() {
        P2TableFactory.selectNextRow(tableView);
    }

    public void setPreviousEpisode() {
        P2TableFactory.selectPreviousRow(tableView);
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
        progData.pEventHandler.addListener(new P2Listener(Events.EREIGNIS_SETDATA_CHANGED) {
            @Override
            public void pingGui(P2Event runEvent) {
                P2TableFactory.refreshTable(tableView);
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

        tableView.setRowFactory(tableView -> {
            TableRowEpisode<Episode> row = new TableRowEpisode<>();
            row.hoverProperty().addListener((observable) -> {
                final Episode episode = (Episode) row.getItem();
                if (row.isHover() && episode != null) {
                    progData.episodeInfoDialogController.setEpisode(episode);
                    smallGuiPack.setEpisodeInfoBox(episode);
                } else {
                    setSelectedEpisode();
                }
            });
            return row;
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedEpisode();
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

    private void setSelectedEpisode() {
        Episode episode = tableView.getSelectionModel().getSelectedItem();
        progData.episodeInfoDialogController.setEpisode(episode);
        smallGuiPack.setEpisodeInfoBox(episode);
    }
}
