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
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2lib.guitools.pclosepane.P2InfoController;
import de.p2tools.p2lib.guitools.pclosepane.P2InfoDto;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.podcastadd.PodcastAddDialogController;
import de.p2tools.p2podder.gui.tools.table.Table;
import de.p2tools.p2podder.gui.tools.table.TablePodcast;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableRow;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Optional;

public class PodcastGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();

    private PanePodcastInfo panePodcastInfo;
    private P2InfoController infoController;

    private final TablePodcast tableView;

    private final ProgData progData;
    private BooleanProperty bound = new SimpleBooleanProperty(false);
    private final SortedList<Podcast> sortedList;
    private final KeyCombination STRG_A = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
    private final KeyCombination SPACE = new KeyCodeCombination(KeyCode.SPACE);

    BooleanProperty boolInfoOn = ProgConfig.PODCAST__INFO_IS_SHOWING;

    public PodcastGuiController() {
        progData = ProgData.getInstance();
        tableView = new TablePodcast(Table.TABLE_ENUM.PODCAST, progData);

        sortedList = progData.podcastList.getSortedList();
        sortedList.addListener((ListChangeListener<Podcast>) c -> {
            selectPodcast();
        });

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        panePodcastInfo = new PanePodcastInfo();
        ArrayList<P2InfoDto> list = new ArrayList<>();
        P2InfoDto infoDto = new P2InfoDto(panePodcastInfo,
                ProgConfig.PODCAST__PANE_INFO_IS_RIP,
                ProgConfig.PODCAST__DIALOG_INFO_SIZE, ProgData.PODCAST_TAB_ON,
                "Info", "Beschreibung", false);
        list.add(infoDto);
        infoController = new P2InfoController(list, ProgConfig.PODCAST__INFO_IS_SHOWING);

        ProgConfig.PODCAST__INFO_IS_SHOWING.addListener((observable, oldValue, newValue) -> setInfoPane());
        ProgConfig.PODCAST__PANE_INFO_IS_RIP.addListener((observable, oldValue, newValue) -> setInfoPane());

        setInfoPane();
        initTable();
        initListener();
    }

    public void isShown() {
        tableView.requestFocus();
        setPodcast();
    }

    public void tableRefresh() {
        tableView.refresh();
    }

    public int getPodcastShown() {
        return tableView.getItems().size();
    }

    public int getSelCount() {
        return tableView.getSelectionModel().getSelectedItems().size();
    }

    public void changePodcast() {
        ArrayList<Podcast> list = getSelList();
        if (list.isEmpty()) {
            return;
        }
        new PodcastAddDialogController(progData, list, false);
    }

    private void setPodcast() {
        Podcast podcast = tableView.getSelectionModel().getSelectedItem();
        panePodcastInfo.setStation(podcast);
    }

    private void selectPodcast() {
        Platform.runLater(() -> {
            if ((tableView.getItems().size() == 0)) {
                return;
            }

            Podcast selPodcast = tableView.getSelectionModel().getSelectedItem();
            if (selPodcast != null) {
                tableView.scrollTo(selPodcast);
            } else {
                tableView.scrollTo(0);
                tableView.getSelectionModel().select(0);
            }
        });
    }

    public void delPodcast() {
        // Menü/Button Podcast löschen
        final Optional<Podcast> sel = getSel();
        if (sel.isPresent()) {
            progData.podcastList.removePodcast(sel.get());
        }
    }

    public void delSelPodcast(boolean all) {
        // Menü/Button podcast aktualisieren
        if (all) {
            progData.podcastList.removeAllPodcast();
        } else {
            progData.podcastList.removePodcast(getSelList());
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.PODCAST);
    }

    public void refreshTable() {
        P2TableFactory.refreshTable(tableView);
    }

    public ArrayList<Podcast> getSelList() {
        final ArrayList<Podcast> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            P2Alert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<Podcast> getSel() {
        return getSel(true);
    }

    public Optional<Podcast> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            if (show) {
                P2Alert.showInfoNoSelection();
            }
            return Optional.empty();
        }
    }

    private void initListener() {
        progData.setDataList.listChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (progData.setDataList.getSetDataListButton().size() > 1) {
                boolInfoOn.set(true);
            }
            setInfoPane();
        });
    }

    private void setInfoPane() {
        // hier wird das InfoPane ein- ausgeblendet
        P2ClosePaneFactory.setSplit(bound, splitPane,
                infoController, false,
                scrollPane, ProgConfig.PODCAST__INFO_DIVIDER, ProgConfig.PODCAST__INFO_IS_SHOWING);
    }

    private void initTable() {
        Table.setTable(tableView);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                changePodcast();
            }
        });

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Podcast> optionalStation = getSel(false);
                Podcast podcast;
                if (optionalStation.isPresent()) {
                    podcast = optionalStation.get();
                } else {
                    podcast = null;
                }
                ContextMenu contextMenu = new PodcastGuiTableContextMenu(progData, this, tableView).getContextMenu(podcast);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (SPACE.match(event)) {
                P2TableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (P2TableFactory.SPACE_SHIFT.match(event)) {
                P2TableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }

            if (STRG_A.match(event)) {
                if (tableView.getItems().size() > 3_000) {
                    // bei sehr langen Listen dauert das seeeeeehr lange
                    P2Log.sysLog("STRG-A: lange Liste -> verhindern");
                    event.consume();
                }
            }
        });
        tableView.setRowFactory(tableView -> {
            TableRow<Podcast> row = new TableRow<>();
            row.hoverProperty().addListener((observable) -> {
                final Podcast filmDataMTP = (Podcast) row.getItem();
                if (row.isHover() && filmDataMTP != null) {
                    panePodcastInfo.setStation(filmDataMTP);
                } else {
                    setPodcast();
                }
            });
            return row;
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(this::setPodcast)
        );
    }
}
