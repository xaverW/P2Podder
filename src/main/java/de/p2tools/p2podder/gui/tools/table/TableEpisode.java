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

package de.p2tools.p2podder.gui.tools.table;

import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.file.PFileSize;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;

public class TableEpisode extends PTable<Episode> {

    private final ProgData progData;

    public TableEpisode(Table.TABLE_ENUM table_enum, ProgData progData) {
        super(table_enum);
        this.table_enum = table_enum;
        this.progData = progData;

        initFileRunnerColumn();
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initFileRunnerColumn();
        Table.resetTable(this);
    }

    private void initFileRunnerColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> this.refresh());

        ProgColorList.EPISODE_NEW_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_NEW.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_STARTED_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_STARTED.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_RUNNING_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_RUNNING.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_ERROR_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_ERROR.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_HISTORY_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.EPISODE_HISTORY.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));

        final TableColumn<Episode, Integer> noColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_NO);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.setCellFactory(cellFactoryNo);
        noColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Episode, String> podcastNameColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_PODCAST_NAME);
        podcastNameColumn.setCellValueFactory(new PropertyValueFactory<>("podcastName"));
        podcastNameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Episode, String> titleColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_TITLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("episodeTitle"));
        titleColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Episode, String> genreColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Episode, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryButton);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Episode, LocalDate> dateColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
        dateColumn.setCellFactory(new CellLocalDate().cellFactory);
        dateColumn.getStyleClass().add("alignCenter");

        final TableColumn<Episode, String> durationColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_DURATION);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.getStyleClass().add("alignCenter");

        final TableColumn<Episode, PFileSize> sizeColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_SIZE);
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("pFileSize"));
        sizeColumn.setCellFactory(cellFactorySize);
        sizeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Episode, String> fileColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_NAME);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Episode, String> pathColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_PATH);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        pathColumn.getStyleClass().add("alignCenterLeft");

        noColumn.setPrefWidth(50);
        podcastNameColumn.setPrefWidth(180);
        titleColumn.setPrefWidth(180);
        genreColumn.setPrefWidth(100);
        dateColumn.setPrefWidth(100);
        sizeColumn.setPrefWidth(120);
        fileColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(350);

        getColumns().addAll(
                noColumn, podcastNameColumn, titleColumn, genreColumn,
                startColumn, dateColumn, durationColumn,
                sizeColumn, fileColumn, pathColumn);
        addRowFact();
    }

    private void addRowFact() {
        setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Episode episode, boolean empty) {
                super.updateItem(episode, empty);

                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        getSelectionModel().clearSelection();
                    }
                });

                setStyle("");
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (episode != null && !empty) {
                    final boolean started = EpisodeFactory.episodeIsStarted(episode);
                    final boolean running = EpisodeFactory.episodeIsRunning(episode);
                    final boolean error = episode.getStart() != null ? episode.getStart().getStartStatus().isStateError() : false;
                    final boolean history = progData.historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl());

                    if (episode.getStart() != null && episode.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(episode.getStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);
                    }

                    //und jetzt die Farben setzen
                    if (error) {
                        if (ProgColorList.EPISODE_ERROR_BG.isUse()) {
                            setStyle(ProgColorList.EPISODE_ERROR_BG.getCssBackground());
                        }
                        if (ProgColorList.EPISODE_ERROR.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.EPISODE_ERROR.getCssFont());
                            }
                        }

                    } else if (started && !running) {
                        if (ProgColorList.EPISODE_STARTED_BG.isUse()) {
                            setStyle(ProgColorList.EPISODE_STARTED_BG.getCssBackground());
                        }
                        if (ProgColorList.EPISODE_STARTED.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.EPISODE_STARTED.getCssFont());
                            }
                        }

                    } else if (running) {
                        if (ProgColorList.EPISODE_RUNNING_BG.isUse()) {
                            setStyle(ProgColorList.EPISODE_RUNNING_BG.getCssBackground());
                        }
                        if (ProgColorList.EPISODE_RUNNING.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.EPISODE_RUNNING.getCssFont());
                            }
                        }

                    } else if (!error && !started && !running && history) {
                        if (ProgColorList.EPISODE_HISTORY_BG.isUse()) {
                            setStyle(ProgColorList.EPISODE_HISTORY_BG.getCssBackground());
                        }
                        if (ProgColorList.EPISODE_HISTORY.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.EPISODE_HISTORY.getCssFont());
                            }
                        }

                    } else if (episode.isNew()) {
                        //neue Episode
                        if (ProgColorList.EPISODE_NEW_BG.isUse()) {
                            setStyle(ProgColorList.EPISODE_NEW_BG.getCssBackground());
                        }
                        if (ProgColorList.EPISODE_NEW.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.EPISODE_NEW.getCssFont());
                            }
                        }
                    }
                }
            }
        });
    }

    private Callback<TableColumn<Episode, Integer>, TableCell<Episode, Integer>> cellFactoryBitrate
            = (final TableColumn<Episode, Integer> param) -> {

        final TableCell<Episode, Integer> cell = new TableCell<>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<Episode, Integer>, TableCell<Episode, Integer>> cellFactoryGrade
            = (final TableColumn<Episode, Integer> param) -> {

        final TableCell<Episode, Integer> cell = new TableCell<>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);

                } else {
                    HBox hBox = new HBox(3);
                    hBox.setAlignment(Pos.CENTER);
                    for (int i = 0; i < EpisodeConstants.MAX_EPISODE_GRADE; ++i) {
                        if (item.longValue() > i) {
                            Label l = new Label();
                            l.setGraphic(ProgIcons.Icons.IMAGE_TABLE_EPISODE_GRADE.getImageView());
                            hBox.getChildren().add(l);
                        }
                    }
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<Episode, Integer>, TableCell<Episode, Integer>> cellFactoryButton
            = (final TableColumn<Episode, Integer> param) -> {

        final TableCell<Episode, Integer> cell = new TableCell<Episode, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                Episode episode = getTableView().getItems().get(getIndex());
                final boolean started = EpisodeFactory.episodeIsStarted(episode);
                final boolean running = EpisodeFactory.episodeIsRunning(episode);
                if (started || running) {
                    //dann stoppen
                    final Button btnStop;
                    btnStop = new Button("");
                    btnStop.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnStop.setTooltip(new Tooltip("Episode stoppen"));
                    btnStop.setGraphic(ProgIcons.Icons.IMAGE_TABLE_EPISODE_STOP_PLAY.getImageView());
                    btnStop.setOnAction((ActionEvent event) -> {
                        EpisodeFactory.stopEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    Table.setButtonSize(btnStop);
                    hbox.getChildren().add(btnStop);

                } else {
                    //starten, nur ein Set
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnPlay.setTooltip(new Tooltip("Episode abspielen"));
                    btnPlay.setGraphic(ProgIcons.Icons.IMAGE_TABLE_EPISODE_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        EpisodeFactory.playEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    Table.setButtonSize(btnPlay);
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnDel.setTooltip(new Tooltip("Episode löschen"));
                btnDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_EPISODE_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    EpisodeFactory.delEpisode(episode);
                });

                Table.setButtonSize(btnDel);
                hbox.getChildren().add(btnDel);

                setGraphic(hbox);
            }
        };
        return cell;
    };

    private Callback<TableColumn<Episode, Integer>, TableCell<Episode, Integer>> cellFactoryNo
            = (final TableColumn<Episode, Integer> param) -> {

        final TableCell<Episode, Integer> cell = new TableCell<Episode, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == EpisodeConstants.EPISODE_NUMBER_NOT_STARTED) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<Episode, PFileSize>, TableCell<Episode, PFileSize>> cellFactorySize
            = (final TableColumn<Episode, PFileSize> param) -> {

        final TableCell<Episode, PFileSize> cell = new TableCell<>() {

            @Override
            public void updateItem(PFileSize item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setGraphic(null);
                setText(item.getSizeStr());
            }
        };
        return cell;
    };
}
