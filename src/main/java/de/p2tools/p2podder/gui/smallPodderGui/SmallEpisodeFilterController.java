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

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class SmallEpisodeFilterController extends VBox {
    private TableView<Podcast> tableView = new TableView<>();
    private ProgData progData;

    public SmallEpisodeFilterController() {
        this.progData = ProgData.getInstance();

        //Tabelle
        getChildren().addAll(/*new Label("Podcast:"),*/ tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        setPadding(new Insets(5));

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

    private void initFilter() {
        tableView.setRowFactory(tv -> {
            TableRow<Podcast> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    tableView.getSelectionModel().clearSelection();
                }
            });
            return row;
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //ist für das Klicken in der Tabelle
            if (tableView.getSelectionModel().isEmpty()) {
                progData.episodeFilterSmall.setPodcastId(0);

            } else if (newValue != null && oldValue != newValue) {
                progData.episodeFilterSmall.setPodcastId(newValue.getId());
            }
        });

        progData.episodeList.addListener((v, o, n) -> {
            //ist für das Hinzufügen/Löschen von Episoden
            Platform.runLater(() -> {
                Podcast sel = tableView.getSelectionModel().getSelectedItem();
                tableView.getItems().setAll(progData.episodeList.getPodcastList());
                if (tableView.getItems().contains(sel)) {
                    tableView.getSelectionModel().select(sel);
                } else {
                    progData.episodeFilterSmall.setPodcastId(0);
                }
            });
        });

        tableView.getItems().addAll(progData.episodeList.getPodcastList());

        //beim zweiten Aufruf, falls es schon einen Filter gibt
        Podcast podcast = progData.podcastList.getPodcastById(progData.episodeFilterSmall.getPodcastId());
        if (podcast != null) {
            tableView.getSelectionModel().select(podcast);
        } else {
            tableView.getSelectionModel().clearSelection();
        }
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
                count = EpisodeFactory.countEpisode(podcast) + "";

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
}
