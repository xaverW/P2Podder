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

package de.p2tools.p2podder.gui.filter;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class EpiDownFilterController extends FilterController {
    private TableView<Podcast> tableView = new TableView<>();
    private ComboBox<String> cboGenre = new ComboBox();
    private TextField txtTitle = new TextField();
    private VBox vBoxGuiClear;
    private ProgData progData;

    public enum FILTER {EPISODE, DOWNLOAD}

    FILTER filter;

    public EpiDownFilterController(FILTER filter) {
        super(filter == FILTER.EPISODE ? ProgConfig.EPISODE_GUI_FILTER_ON : ProgConfig.PODCAST_GUI_FILTER_ON);
        this.filter = filter;
        this.vBoxGuiClear = filter == FILTER.EPISODE ? ProgData.getInstance().episodeGui.getEpisodeFilterClear() :
                ProgData.getInstance().downloadGui.getDownloadFilterClear();
        this.progData = ProgData.getInstance();

        VBox vBoxTable = new VBox();
        vBoxTable.getChildren().addAll(/*new Label("Podcast:"),*/ tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(vBoxTable, Priority.ALWAYS);

        VBox vBoxTitle = new VBox();
        vBoxTitle.getChildren().addAll(new Label("Titel:"), txtTitle);

        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        VBox vBoxClear = new VBox();
        vBoxClear.setSpacing(5);
        vBoxClear.setPadding(new Insets(10, 0, 0, 0));
        Separator sp1 = new Separator();
        sp1.getStyleClass().add("pseperator3");
        vBoxClear.getChildren().addAll(sp1, vBoxGuiClear);

        final VBox vBoxFilter = getVBoxTop();
        vBoxFilter.setPadding(new Insets(5, 5, 5, 5));
        vBoxFilter.setSpacing(15);
        vBoxFilter.getChildren().addAll(vBoxTable, vBoxGenre, vBoxTitle, vBoxClear);

        initTable();
        initFilter();
    }

    private void initTable() {
        final TableColumn<Podcast, String> episodeColumn = new TableColumn<>("Podcast");
        episodeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        episodeColumn.setCellFactory(cellFactory);
        episodeColumn.getStyleClass().add("alignCenterLeft");

        tableView.getColumns().addAll(episodeColumn);
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void initFilter() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                long l = tableView.getSelectionModel().getSelectedItem().getId();
                if (filter == FILTER.EPISODE) {
                    progData.episodeGui.getEpisodeFilter().setPodcastId(l);
                } else {
                    progData.downloadGui.getDownloadFilter().setPodcastId(l);
                }
            }
        });
        if (filter == FILTER.EPISODE) {
            progData.episodeStoredList.addListener((v, o, n) -> {
                tableView.getItems().clear();
                tableView.getItems().addAll(filter == FILTER.EPISODE ? progData.episodeStoredList.getPodcastList() :
                        DownloadFactory.getPodcastList());
            });
        } else {
            progData.downloadList.addListener((v, o, n) -> {
                tableView.getItems().clear();
                tableView.getItems().addAll(DownloadFactory.getPodcastList());
            });
        }
        tableView.getItems().addAll(filter == FILTER.EPISODE ? progData.episodeStoredList.getPodcastList() : DownloadFactory.getPodcastList());

        ListChangeListener listChangeListener = (ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(filter == FILTER.EPISODE ? progData.episodeStoredList.getGenreList() : progData.downloadList.getGenreList());
                cboGenre.getSelectionModel().select(sel);
                if (!cboGenre.getItems().contains(sel)) {
                    clearFilter();
                }
            });
        };
        if (filter == FILTER.EPISODE) {
            progData.episodeStoredList.getGenreList().addListener(listChangeListener);
        } else {
            progData.downloadList.getGenreList().addListener(listChangeListener);
        }
        cboGenre.valueProperty().addListener((u, o, n) -> {
            if (n != null) {
                Platform.runLater(() -> {
                    if (filter == FILTER.EPISODE) {
                        progData.episodeGui.getEpisodeFilter().setGenre(n);
                    } else {
                        progData.downloadGui.getDownloadFilter().setGenre(n);
                    }
                });
            }
        });
        cboGenre.getItems().addAll(filter == FILTER.EPISODE ? progData.episodeStoredList.getGenreList() : progData.downloadList.getGenreList());

        txtTitle.textProperty().addListener((u, o, n) -> {
            if (filter == FILTER.EPISODE) {
                progData.episodeGui.getEpisodeFilter().setTitle(txtTitle.getText());
            } else {
                progData.downloadGui.getDownloadFilter().setTitle(txtTitle.getText());
            }
        });
    }

    private Callback<TableColumn<Podcast, String>, TableCell<Podcast, String>> cellFactory
            = (final TableColumn<Podcast, String> param) -> {

        final TableCell<Podcast, String> cell = new TableCell<Podcast, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final VBox vBox = new VBox();
                vBox.setSpacing(5);
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setPadding(new Insets(0, 2, 0, 2));

                Podcast podcast = getTableView().getItems().get(getIndex());
                String name = podcast.getName();
                String genre = podcast.getGenre();

                String count;
                if (filter == FILTER.EPISODE) {
                    count = EpisodeFactory.countEpisode(podcast) + "";
                } else {
                    count = DownloadFactory.countDownload(podcast) + "";
                }

                Label lblCount = new Label(count);
                Label lblName = new Label(name);
                Label lblGenre = new Label(genre);
                lblCount.getStyleClass().add("lblCount");
                lblCount.setMinWidth(Region.USE_PREF_SIZE);

                VBox vBox1 = new VBox();
                vBox1.getChildren().addAll(lblName, lblGenre);

                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(lblCount, vBox1);

                vBox.getChildren().add(hBox);
                setGraphic(vBox);
            }
        };
        return cell;
    };

    public void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        if (filter == FILTER.EPISODE) {
            progData.episodeStoredList.filteredListSetPred(progData.episodeGui.getEpisodeFilter().clearFilter());
        } else {
            progData.downloadList.filteredListSetPred(progData.downloadGui.getDownloadFilter().clearFilter());
        }

        tableView.getSelectionModel().clearSelection();
        cboGenre.getSelectionModel().clearSelection();
        txtTitle.setText("");
    }
}
