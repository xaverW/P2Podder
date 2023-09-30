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

import de.p2tools.p2lib.guitools.ptable.P2CellCheckBox;
import de.p2tools.p2lib.guitools.ptable.P2CellLocalDate;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.data.podcast.PodcastNames;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TablePodcast extends PTable<Podcast> {

    private final ProgData progData;

    public TablePodcast(Table.TABLE_ENUM table_enum, ProgData progData) {
        super(table_enum);
        this.table_enum = table_enum;
        this.progData = progData;

        addListener();
        initColumn();
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initColumn();
        Table.resetTable(this);
    }

    private void addListener() {
        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> this.refresh());
    }

    private void initColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<Podcast, Integer> nrColumn = new TableColumn<>(PodcastNames.PODCAST_NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, String> nameColumn = new TableColumn<>(PodcastNames.PODCAST_NAME);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, Boolean> activeColumn = new TableColumn<>(PodcastNames.PODCAST_ACTIVE);
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeColumn.setCellFactory(new P2CellCheckBox().cellFactory);
        activeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, String> genreColumn = new TableColumn<>(PodcastNames.PODCAST_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(new CellPodcastButton<>().cellFactory);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, Integer> amountColumn = new TableColumn<>(PodcastNames.PODCAST_AMOUNT_EPISODES);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountEpisodes"));
        amountColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, LocalDate> dateColumn = new TableColumn<>(PodcastNames.PODCAST_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(new P2CellLocalDate().cellFactory);
        dateColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, String> websiteColumn = new TableColumn<>(PodcastNames.PODCAST_WEBSITE);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, String> urlColumn = new TableColumn<>(PodcastNames.PODCAST_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        nameColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(120);
        startColumn.setPrefWidth(120);
        websiteColumn.setPrefWidth(350);
        urlColumn.setPrefWidth(350);

        getColumns().addAll(
                nrColumn, nameColumn, activeColumn, genreColumn, startColumn, amountColumn,
                dateColumn, websiteColumn, urlColumn);
    }
}
