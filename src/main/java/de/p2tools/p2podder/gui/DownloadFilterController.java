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

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.filter.FilterController;
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


public class DownloadFilterController extends FilterController {
    private TableView<Podcast> tableView = new TableView<>();
    private ComboBox<String> cboGenre = new ComboBox();
    private TextField txtTitle = new TextField();
    //    private VBox vBoxGuiClear;
    private ProgData progData;

    private final Button btnClearFilter = new Button("");
    private final Slider slTimeRange = new Slider();
    private final Label lblTimeRange = new Label("Zeitraum:");
    private final Label lblTimeRangeValue = new Label();

    public DownloadFilterController() {
        super(ProgConfig.PODCAST_GUI_FILTER_ON);
//        this.vBoxGuiClear = ProgData.getInstance().downloadGui.getDownloadFilterClear();
        this.progData = ProgData.getInstance();

        VBox vBoxTable = new VBox();
        vBoxTable.getChildren().addAll(/*new Label("Podcast:"),*/ tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(vBoxTable, Priority.ALWAYS);

        VBox vBoxTitle = new VBox();
        vBoxTitle.getChildren().addAll(new Label("Titel:"), txtTitle);

//        // Tage
//        VBox vBoxTimeRange = new VBox(2);
//        HBox h = new HBox();
//        HBox hh = new HBox();
//        h.getChildren().addAll(lblTimeRange, hh, lblTimeRangeValue);
//        HBox.setHgrow(hh, Priority.ALWAYS);
//        lblTimeRange.setMinWidth(0);
//        vBoxTimeRange.getChildren().addAll(h, slTimeRange);

        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        final VBox vBoxFilter = getVBoxTop();
        vBoxFilter.setPadding(new Insets(5, 5, 5, 5));
        vBoxFilter.setSpacing(15);
        vBoxFilter.getChildren().addAll(vBoxTable, /*vBoxTimeRange,*/ vBoxGenre, vBoxTitle);

        addButton();
        initDaysFilter();
        initTable();
        initFilter();
    }

    private void addButton() {
        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_CLEAR_FILTER);
        btnClearFilter.setOnAction(a -> clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Alle Filter löschen"));

        HBox hBoxAll = new HBox(5);
        hBoxAll.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hBoxAll, Priority.ALWAYS);
        hBoxAll.getChildren().addAll(btnClearFilter);

        Separator separator = new Separator();
        separator.getStyleClass().add("pseperator1");

        final VBox vBoxFilter = getVBoxTop();
        VBox vBoxSpace = new VBox(0);
        vBoxSpace.setPadding(new Insets(5));
        VBox.setVgrow(vBoxSpace, Priority.ALWAYS);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(vBoxSpace, separator, hBoxAll);
        vBoxFilter.getChildren().add(vBox);
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
        slTimeRange.valueProperty().addListener((o, oldV, newV) -> {
            setLabelSlider();
            if (!slTimeRange.isValueChanging()) {
                progData.downloadGui.getDownloadFilter().setTimeRange((int) slTimeRange.getValue());
            }
        });
        slTimeRange.valueChangingProperty().addListener((observable, oldvalue, newvalue) -> {
                    setLabelSlider();
                    if (!newvalue) {
                        progData.downloadGui.getDownloadFilter().setTimeRange((int) slTimeRange.getValue());
                    }
                }
        );
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
                progData.downloadGui.getDownloadFilter().setPodcastId(l);
            }
        });
        progData.downloadList.addListener((v, o, n) -> {
            tableView.getItems().clear();
            tableView.getItems().addAll(DownloadFactory.getPodcastList());
        });
        tableView.getItems().addAll(DownloadFactory.getPodcastList());

        ListChangeListener listChangeListener = (ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(progData.downloadList.getGenreList());
                cboGenre.getSelectionModel().select(sel);
                if (!cboGenre.getItems().contains(sel)) {
                    clearFilter();
                }
            });
        };
        progData.downloadList.getGenreList().addListener(listChangeListener);
        cboGenre.valueProperty().addListener((u, o, n) -> {
            if (n != null) {
                Platform.runLater(() -> {
                    progData.downloadGui.getDownloadFilter().setGenre(n);
                });
            }
        });
        cboGenre.getItems().addAll(progData.downloadList.getGenreList());

        txtTitle.textProperty().addListener((u, o, n) -> {
            progData.downloadGui.getDownloadFilter().setTitle(txtTitle.getText());
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

    public void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        progData.downloadList.filteredListSetPred(progData.downloadGui.getDownloadFilter().clearFilter());

        tableView.getSelectionModel().clearSelection();
        slTimeRange.setValue(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
        cboGenre.getSelectionModel().clearSelection();
        txtTitle.setText("");
    }
}
