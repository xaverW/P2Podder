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
import de.p2tools.p2lib.guitools.prange.P2RangeBox;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
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
import javafx.util.StringConverter;


public class EpisodeFilterController extends VBox {
    private final ScrollPane scrollPane = new ScrollPane();
    private final TableView<Podcast> tableView = new TableView<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final TextField txtTitle = new TextField();
    private final TextField txtDescription = new TextField();
    private final ProgData progData;
    private final P2RangeBox slDur = new P2RangeBox("Länge:", true, 0, ProgConst.FILTER_DURATION_MAX_MINUTE);


    private final Slider slTimeRange = new Slider();
    private final Label lblTimeRangeValue = new Label();

    public EpisodeFilterController() {
        this.progData = ProgData.getInstance();

        //Tage
        VBox vBoxTimeRange = new VBox(2);
        HBox h = new HBox();
        HBox hh = new HBox();
        Label lblTimeRange = new Label("Zeitraum:");
        h.getChildren().addAll(lblTimeRange, hh, lblTimeRangeValue);
        HBox.setHgrow(hh, Priority.ALWAYS);
        lblTimeRange.setMinWidth(0);
        vBoxTimeRange.getChildren().addAll(h, slTimeRange);

        //Genre
        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        //Textfelder
        VBox vBoxTxt = new VBox();
        vBoxTxt.getChildren().addAll(new Label("Titel:"), txtTitle);
        vBoxTxt.getChildren().addAll(new Label("Beschreibung:"), txtDescription);

        VBox.setVgrow(this, Priority.ALWAYS);
        setPadding(new Insets(P2LibConst.PADDING));
        setSpacing(P2LibConst.DIST_BUTTON);
        getChildren().addAll(scrollPane, vBoxGenre, vBoxTxt, vBoxTimeRange, slDur, P2GuiTools.getVBoxGrower());

        Separator separator = new Separator();
        separator.getStyleClass().add("pseperator2");
        getChildren().addAll(P2GuiTools.getHDistance(10), separator, new EpisodeFilterControllerClearFilter());

        initDurFilter();
        initDaysFilter();
        initTable();
        initFilter();
    }

    private void initDurFilter() {
        slDur.minValueProperty().set(progData.episodeFilter.getDurationMin());
        slDur.maxValueProperty().set(progData.episodeFilter.getDurationMax());

        progData.episodeFilter.durationMinProperty().addListener(l ->
                slDur.minValueProperty().set(progData.episodeFilter.getDurationMin()));
        progData.episodeFilter.durationMaxProperty().addListener(l ->
                slDur.maxValueProperty().set(progData.episodeFilter.getDurationMax()));

        slDur.getSlider().lowValueChangingProperty().addListener((u, o, n) -> {
            if (!n) {
                // dann ist er fertig
                progData.episodeFilter.setDurationMin(slDur.getActMinValue());
            }
        });
        slDur.getSlider().highValueChangingProperty().addListener((u, o, n) -> {
            if (!n) {
                // dann ist er fertig
                progData.episodeFilter.setDurationMax(slDur.getActMaxValue());
            }
        });
    }

    private void initDaysFilter() {
        slTimeRange.setMin(ProgConst.FILTER_TIME_RANGE_MIN_VALUE);
        slTimeRange.setMax(ProgConst.FILTER_TIME_RANGE_MAX_VALUE);
        slTimeRange.setShowTickLabels(true);
        slTimeRange.setMajorTickUnit(10);
        slTimeRange.setBlockIncrement(5);

        slTimeRange.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double x) {
                if (x == ProgConst.FILTER_TIME_RANGE_ALL_VALUE) return "alles";
                return x.intValue() + "";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });

        slTimeRange.setValue(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
        setLabelSlider();
        progData.episodeFilter.timeRangeProperty().addListener(l ->
                slTimeRange.setValue(progData.episodeFilter.timeRangeProperty().get()));

        slTimeRange.valueProperty().addListener((o, oldV, newV) -> {
            setLabelSlider();
        });
        slTimeRange.valueChangingProperty().addListener((u, o, n) -> {
            if (!n) {
                progData.episodeFilter.setTimeRange((int) slTimeRange.getValue());
            }
        });
    }

    private void setLabelSlider() {
        final String txtAll = "alles";
        int i = (int) slTimeRange.getValue();
        String tNr = i + "";
        if (i == ProgConst.FILTER_TIME_RANGE_ALL_VALUE) {
            lblTimeRangeValue.setText(txtAll);
        } else {
            lblTimeRangeValue.setText(tNr + (i == 1 ? " Tag" : " Tage"));
        }
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
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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
                progData.episodeFilter.setPodcastId(0);

            } else if (newValue != null && oldValue != newValue) {
                progData.episodeFilter.setPodcastId(newValue.getId());
            }
        });

        progData.episodeFilter.podcastIdProperty().addListener((u, o, n) -> {
            //ist für den Wechsel gespeicherter Filter
            Podcast podcast = progData.podcastList.getPodcastById(n.longValue());
            if (podcast != null) {
                tableView.getSelectionModel().select(podcast);
            } else {
                tableView.getSelectionModel().clearSelection();
            }
        });

        progData.episodeList.getPodcastList().addListener((ListChangeListener<Podcast>) c -> {
            //ist für das Hinzufügen/Löschen von Episoden
            Platform.runLater(() -> {
                //kann durch Downloads außer der Reihe sein!!
                Podcast sel = tableView.getSelectionModel().getSelectedItem();
                tableView.getItems().setAll(progData.episodeList.getPodcastList());
                if (sel != null && tableView.getItems().contains(sel)) {
                    tableView.getSelectionModel().select(sel);
                } else {
                    progData.episodeFilter.setPodcastId(0);
                }
            });
        });
        tableView.getItems().addAll(progData.episodeList.getPodcastList());

        cboGenre.valueProperty().bindBidirectional(progData.episodeFilter.genreProperty());
        progData.episodeList.getGenreList().addListener((ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                //kann durch Downloads außer der Reihe sein!!
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(progData.episodeList.getGenreList());
                if (sel != null && !sel.isEmpty() && cboGenre.getItems().contains(sel)) {
                    cboGenre.getSelectionModel().select(sel);
                } else {
                    progData.episodeFilter.setGenre("");
                }
            });
        });
        cboGenre.getItems().addAll(progData.episodeList.getGenreList());

        txtTitle.textProperty().bindBidirectional(progData.episodeFilter.titleProperty());
        FilterCheckRegEx fT = new FilterCheckRegEx(txtTitle);
        txtTitle.textProperty().addListener((observable, oldValue, newValue) -> fT.checkPattern());

        txtDescription.textProperty().bindBidirectional(progData.episodeFilter.descriptionProperty());
        FilterCheckRegEx fD = new FilterCheckRegEx(txtDescription);
        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> fD.checkPattern());
    }

    private final Callback<TableColumn<Podcast, String>, TableCell<Podcast, String>> cellFactory
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
