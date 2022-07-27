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

package de.p2tools.p2podder.gui.smallPodderGui;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SmallEpisodeGuiController extends AnchorPane {

    private final VBox vBoxAll = new VBox();
    private final SplitPane splitPane = new SplitPane();
    private final VBox vBoxRight = new VBox();
    private SmallEpisodeFilterController smallEpisodeFilterController;

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableView<Episode> tableView = new TableView<>();

    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbStarted = new RadioButton("gestartete");
    private final RadioButton rbRunning = new RadioButton("läuft");
    private final RadioButton rbWasShown = new RadioButton("gehörte");
    private final RadioButton rbNew = new RadioButton("neue");

    private final ProgData progData;
    private boolean bound = false;

    private SmallPodderGuiPack smallPodderGuiPack;


    public SmallEpisodeGuiController(SmallPodderGuiPack smallPodderGuiPack) {
        this.smallPodderGuiPack = smallPodderGuiPack;
        progData = ProgData.getInstance();
        smallEpisodeFilterController = new SmallEpisodeFilterController();

        AnchorPane.setLeftAnchor(vBoxAll, 0.0);
        AnchorPane.setBottomAnchor(vBoxAll, 0.0);
        AnchorPane.setRightAnchor(vBoxAll, 0.0);
        AnchorPane.setTopAnchor(vBoxAll, 0.0);
        getChildren().addAll(vBoxAll);

        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(smallEpisodeFilterController, vBoxRight);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.SMALL_EPISODE_GUI_FILTER_DIVIDER);
        SplitPane.setResizableWithParent(smallEpisodeFilterController, Boolean.FALSE);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        ToggleGroup tg = new ToggleGroup();
        rbAll.setToggleGroup(tg);
        rbStarted.setToggleGroup(tg);
        rbRunning.setToggleGroup(tg);
        rbWasShown.setToggleGroup(tg);
        rbNew.setToggleGroup(tg);

        HBox hBoxTopFilter = new HBox();
        hBoxTopFilter.setPadding(new Insets(5));
        hBoxTopFilter.setSpacing(15);
        hBoxTopFilter.getChildren().addAll(new Label("Episoden: "), rbAll, rbNew, rbStarted, rbRunning, rbWasShown);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        HBox hBoxBottom = new SmallPodderBottom(this).init();
        hBoxBottom.setAlignment(Pos.CENTER_LEFT);
        hBoxBottom.setSpacing(15);
        hBoxBottom.setPadding(new Insets(5, 10, 5, 10));

        vBoxRight.getChildren().addAll(hBoxTopFilter, scrollPane);
        vBoxRight.setSpacing(5);
        vBoxAll.getChildren().addAll(splitPane, hBoxBottom);

        initFilter();
        initTable();
        initListener();

        ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.addListener((observable, oldValue, newValue) -> setSplit());
        setSplit();
    }

    private void setSplit() {
        if (ProgConfig.SMALL_EPISODE_GUI_FILTER_ON.getValue()) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(smallEpisodeFilterController, vBoxRight);
            bound = true;
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.SMALL_EPISODE_GUI_FILTER_DIVIDER);
        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.SMALL_EPISODE_GUI_FILTER_DIVIDER);
            }
            splitPane.getItems().clear();
            splitPane.getItems().addAll(vBoxRight);
        }
    }


    public PMaskerPane getMaskerPane() {
        return smallPodderGuiPack.getMaskerPane();
    }

    public SmallPodderGuiPack getSmallRadioGuiPack() {
        return smallPodderGuiPack;
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

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isAllProperty());
        rbNew.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isNewProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isStartedProperty());
        rbRunning.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isRunningProperty());
        rbWasShown.selectedProperty().bindBidirectional(progData.episodeFilterSmall.wasShownProperty());
    }

    public void playEpisode() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            EpisodeFactory.playEpisode(episode.get());
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.SMALL_EPISODE);
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


    public void setNextEpisode() {
        PTableFactory.selectNextRow(tableView);
    }

    public void setPreviousEpisode() {
        PTableFactory.selectPreviousRow(tableView);
    }

    public void playRandomStation() {
        Random r = new Random();
        Episode episode = tableView.getItems().get(r.nextInt(tableView.getItems().size()));
        tableView.getSelectionModel().clearSelection();
        if (episode != null) {
            EpisodeFactory.playEpisode(episode);
            tableView.getSelectionModel().select(episode);
            tableView.scrollTo(episode);
        }
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.EREIGNIS_SETDATA_CHANGED) {
            @Override
            public void pingGui(Event runEvent) {
                Table.refresh_table(tableView);
            }
        });
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        new Table().setTable(tableView, Table.TABLE.SMALL_EPISODE);

        tableView.setItems(progData.episodeList.getSmallSortedList());
        progData.episodeList.getSmallSortedList().comparatorProperty().bind(tableView.comparatorProperty());
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
