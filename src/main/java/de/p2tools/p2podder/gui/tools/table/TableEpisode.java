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

import de.p2tools.p2Lib.guiTools.PFileSize;
import de.p2tools.p2Lib.tools.GermanStringIntSorter;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableEpisode {

    private final ProgData progData;
    private final BooleanProperty geoMelden;
    private final BooleanProperty small;

    public TableEpisode(ProgData progData) {
        this.progData = progData;
        geoMelden = ProgConfig.SYSTEM_MARK_GEO;
        small = ProgConfig.SYSTEM_SMALL_ROW_TABLE_EPISODE;
    }

    public TableColumn[] initEpisodeColumn(TableView table) {
        table.getColumns().clear();

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();
        ProgConfig.SYSTEM_SMALL_ROW_TABLE_EPISODE.addListener((observableValue, s, t1) -> Table.refresh_table(table));
        ProgColorList.EPISODE_RUN.colorProperty().addListener((a, b, c) -> Table.refresh_table(table));
        ProgColorList.EPISODE_ERROR.colorProperty().addListener((a, b, c) -> Table.refresh_table(table));
        ProgColorList.EPISODE_NEW.colorProperty().addListener((a, b, c) -> Table.refresh_table(table));

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

        final TableColumn<Episode, PDate> datumColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_DATE);
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
        datumColumn.getStyleClass().add("alignCenter");

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
//        startColumn.setPrefWidth(50);
        datumColumn.setPrefWidth(100);
        sizeColumn.setPrefWidth(120);
        fileColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(350);

        addRowFact(table);

        return new TableColumn[]{
                noColumn, podcastNameColumn, titleColumn, genreColumn,
                startColumn, datumColumn,
                sizeColumn, fileColumn, pathColumn
        };
    }

    private void addRowFact(TableView<Episode> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Episode episode, boolean empty) {
                super.updateItem(episode, empty);

                if (episode == null || empty) {
                    setStyle("");

                } else {
                    if (episode.getStart() != null && episode.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(episode.getStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);
                    } else if (episode.isNew()) {
                        //neue Episode
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.EPISODE_NEW.getCssFont());
                        }
                    } else {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle("");
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
                            l.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_EPISODE_GRADE.getUrl()));
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
                final boolean playing = EpisodeFactory.episodeIsRunning(episode);
                final boolean error = episode.getStart() != null ? episode.getStart().getStartStatus().isStateError() : false;
                final boolean set = progData.setDataList.size() > 1;

                if (playing) {
                    //dann stoppen
                    final Button btnStop;
                    btnStop = new Button("");
                    btnStop.setTooltip(new Tooltip("Episode abspielen"));
                    btnStop.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_EPISODE_STOP_PLAY));
                    btnStop.setOnAction((ActionEvent event) -> {
                        progData.episodeGui.getEpisodeGuiController().stopEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnStop.setMinHeight(18);
                        btnStop.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnStop);

                } else if (set) {
                    //läuft nix, mehre Sets
                    final ComboBox<SetData> cboSet;
                    cboSet = new ComboBox();
                    cboSet.getStyleClass().add("combo-box-icon");
                    cboSet.getItems().addAll(progData.setDataList);
                    cboSet.getSelectionModel().selectedItemProperty().addListener((v, ol, ne) -> {
                        EpisodeFactory.playEpisode(episode, ne);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        cboSet.setMinHeight(18);
                        cboSet.setMaxHeight(18);
                    }
                    hbox.getChildren().add(cboSet);

                } else {
                    //starten, nur ein Set
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.setTooltip(new Tooltip("Episode abspielen"));
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_EPISODE_PLAY));
                    btnPlay.setOnAction((ActionEvent event) -> {
                        EpisodeFactory.playEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnPlay.setMinHeight(18);
                        btnPlay.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnDel;
                btnDel = new Button("");
                btnDel.setTooltip(new Tooltip("Episode löschen"));
                btnDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_EPISODE_DEL));
                btnDel.setOnAction(event -> {
                    progData.episodeGui.getEpisodeGuiController().deleteEpisode(episode);
                });

                if (small.get()) {
                    btnDel.setMinHeight(18);
                    btnDel.setMaxHeight(18);
                }
                hbox.getChildren().add(btnDel);

                setGraphic(hbox);
                final boolean history = progData.historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl());
                setCellStyle(this, episode.isNew(), playing, error, history);
            }
        };
        return cell;
    };

    private void setCellStyle(TableCell<Episode, Integer> cell, boolean isNew, boolean playing, boolean error, boolean history) {
        TableRow<Episode> currentRow = cell.getTableRow();
        if (currentRow == null) {
            return;
        }

        if (playing) {
            currentRow.setStyle(ProgColorList.EPISODE_RUN.getCssBackgroundAndSel());
        } else if (error) {
            currentRow.setStyle(ProgColorList.EPISODE_ERROR.getCssBackgroundAndSel());
        } else {
            currentRow.setStyle("");
        }
        if (!error && !playing && history) {
            currentRow.setStyle(ProgColorList.EPISODE_HISTORY.getCssBackgroundAndSel());
        }
//        if (isNew) {
////            currentRow.getChildren().stream().forEach(c -> c.setStyle(ProgColorList.EPISODE_NEW.getCssFont()));
//            cell.setStyle(ProgColorList.EPISODE_NEW.getCssFont());
//            currentRow.setStyle(ProgColorList.EPISODE_NEW.getCssFont());
//        }
    }

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
                setText(item.getSizeS());
            }
        };
        return cell;
    };
}
