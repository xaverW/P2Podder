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
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.parser.ParserThread;
import de.p2tools.p2podder.gui.dialog.PodcastEditDialogController;
import de.p2tools.p2podder.gui.tools.table.Table;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.Optional;

public class PodcastGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();

    private final TilePane tilePaneButton = new TilePane();
    private PodcastGuiInfoController podcastGuiInfoController;
    private final TableView<Podcast> tableView = new TableView<>();

    private final ProgData progData;
    private boolean bound = false;
    private final SortedList<Podcast> sortedList;
    private final KeyCombination STRG_A = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
    private final KeyCombination SPACE = new KeyCodeCombination(KeyCode.SPACE);

    DoubleProperty splitPaneProperty = ProgConfig.PODCAST_GUI_DIVIDER;
    BooleanProperty boolInfoOn = ProgConfig.PODCAST_GUI_INFO_ON;

    public PodcastGuiController() {
        progData = ProgData.getInstance();
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

        initInfoPane();
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
        final Optional<Podcast> optPodcast = getSel();
        if (optPodcast.isPresent()) {
            Podcast podcast = optPodcast.get();
            Podcast podcastTmp = podcast.getCopy();
            PodcastEditDialogController podcastEditDialogController =
                    new PodcastEditDialogController(ProgData.getInstance(), podcastTmp, false);
            if (podcastEditDialogController.isOk()) {
                podcast.copyToMe(podcastTmp);
            }
        }
    }

    private void setPodcast() {
        Podcast podcast = tableView.getSelectionModel().getSelectedItem();
        podcastGuiInfoController.setStation(podcast);
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

    public void updateSelectedPodcast() {
        // Men??/Button podcast aktualisieren
        final Optional<Podcast> podcast = getSel();
        if (podcast.isPresent()) {
            new ParserThread(progData).parse(podcast.get());
        }
    }

    public void updatePodcast(boolean all) {
        // Men??/Button podcast aktualisieren
        if (all) {
            new ParserThread(progData).parse(progData.podcastList);
        } else {
            ArrayList<Podcast> list = getSelList();
            if (!list.isEmpty()) {
                new ParserThread(progData).parse(list);
            }
        }
    }

    public void delPodcast() {
        // Men??/Button Podcast l??schen
        final Optional<Podcast> sel = getSel();
        if (sel.isPresent()) {
            progData.podcastList.removePodcast(sel.get());
        }
    }

    public void delSelPodcast(boolean all) {
        // Men??/Button podcast aktualisieren
        if (all) {
            progData.podcastList.removeAllPodcast();
        } else {
            progData.podcastList.removePodcast(getSelList());
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.PODCAST);
    }

    public void refreshTable() {
        Table.refresh_table(tableView);
    }

    public ArrayList<Podcast> getSelList() {
        final ArrayList<Podcast> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
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
                PAlert.showInfoNoSelection();
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

    private void initInfoPane() {
        podcastGuiInfoController = new PodcastGuiInfoController();
        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());

        tilePaneButton.setVgap(15);
        tilePaneButton.setHgap(15);
        tilePaneButton.setPadding(new Insets(10));
        tilePaneButton.setStyle("-fx-border-color: -fx-text-box-border; " +
                "-fx-border-radius: 5px; " +
                "-fx-border-width: 1;");
    }

    private void setInfoPane() {
        if (boolInfoOn.getValue()) {
            bound = true;
            setInfoTabPane();
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);

        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }

            if (splitPane.getItems().size() != 1) {
                splitPane.getItems().clear();
                splitPane.getItems().add(scrollPane);
            }
        }
    }

    private void setInfoTabPane() {
        if (splitPane.getItems().size() != 2 || splitPane.getItems().get(1) != podcastGuiInfoController) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(scrollPane, podcastGuiInfoController);
            SplitPane.setResizableWithParent(podcastGuiInfoController, false);
        }
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);

        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new Table().setTable(tableView, Table.TABLE.PODCAST);

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
                PTableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (PTableFactory.SPACE_SHIFT.match(event)) {
                PTableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }

            if (STRG_A.match(event)) {
                if (tableView.getItems().size() > 3_000) {
                    // bei sehr langen Listen dauert das seeeeeehr lange
                    PLog.sysLog("STRG-A: lange Liste -> verhindern");
                    event.consume();
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(this::setPodcast)
        );
    }
}
