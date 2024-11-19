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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.filter.FilterCheckRegEx;
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


public class DownloadFilterController extends VBox {
    private final ScrollPane scrollPane = new ScrollPane();
    private TableView<Podcast> tableView = new TableView<>();
    private ComboBox<String> cboGenre = new ComboBox();
    private TextField txtTitle = new TextField();
    private ProgData progData;

    public DownloadFilterController() {
        this.progData = ProgData.getInstance();

        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        VBox vBoxTitle = new VBox();
        vBoxTitle.getChildren().addAll(new Label("Titel:"), txtTitle);

        VBox.setVgrow(this, Priority.ALWAYS);
        setPadding(new Insets(P2LibConst.PADDING));
        setSpacing(P2LibConst.DIST_BUTTON);
        getChildren().addAll(scrollPane, vBoxGenre, vBoxTitle, P2GuiTools.getVBoxGrower());

        addButton();
        initTable();
        initFilter();
    }

    private void addButton() {
        Button btnClearFilter = new Button("");
        btnClearFilter.setGraphic(ProgIcons.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(a -> clearFilter(true));
        btnClearFilter.setTooltip(new Tooltip("Alle Filter löschen"));

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnClearFilter);

        Separator separator = new Separator();
        separator.getStyleClass().add("pseperator2");

        getChildren().addAll(separator, hBox);
    }

    private void initTable() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        final TableColumn<Podcast, String> episodeColumn = new TableColumn<>("Podcast");
        episodeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        episodeColumn.setCellFactory(cellFactory);
        episodeColumn.getStyleClass().add("alignCenterLeft");

        tableView.getColumns().addAll(episodeColumn);
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void initFilter() {
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
            if (tableView.getSelectionModel().isEmpty()) {
                progData.downloadFilter.setPodcastId(0);

            } else if (newValue != null) {
                long l = tableView.getSelectionModel().getSelectedItem().getId();
                progData.downloadFilter.setPodcastId(l);
            }
        });

        progData.downloadList.addListener((v, o, n) -> {
            Platform.runLater(() -> {
                Podcast sel = tableView.getSelectionModel().getSelectedItem();
                tableView.getItems().setAll(DownloadFactory.getPodcastList());
                if (tableView.getItems().contains(sel)) {
                    tableView.getSelectionModel().select(sel);
                } else {
                    clearFilter(false);
                }
            });
        });

        tableView.getItems().addAll(DownloadFactory.getPodcastList());

        cboGenre.getItems().addAll(progData.downloadList.getGenreList());
        progData.downloadList.getGenreList().addListener((ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(progData.downloadList.getGenreList());
                if (sel.isEmpty()) {
                } else if (cboGenre.getItems().contains(sel)) {
                    cboGenre.getSelectionModel().select(sel);
                } else {
                    clearFilter(false);//clearSelection in der Tabelle bräuchte es da nicht, aber..
                }
            });
        });
        cboGenre.valueProperty().bindBidirectional(progData.downloadFilter.genreProperty());

        txtTitle.textProperty().bindBidirectional(progData.downloadFilter.titleProperty());
        FilterCheckRegEx fT = new FilterCheckRegEx(txtTitle);
        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> fT.checkPattern());
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
                count = DownloadFactory.countDownload(podcast) + "";

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

    public void clearFilter(boolean clearTable) {
        P2Duration.onlyPing("Filter löschen");
        progData.downloadFilter.clearFilter();
        if (clearTable) {
            tableView.getSelectionModel().clearSelection();
        }
    }
}
