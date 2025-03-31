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

import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.file.P2FileSize;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TableEpisode extends PTable<Episode> {

    private final ProgData progData;

    public TableEpisode(Table.TABLE_ENUM table_enum, ProgData progData) {
        super(table_enum);
        this.table_enum = table_enum;
        this.progData = progData;

        initColumn();
        addListener();
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initColumn();
        Table.resetTable(this);
    }

    private void refreshTable() {
        P2TableFactory.refreshTable(this);
    }

    private void addListener() {
        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> this.refresh());

        ProgData.getInstance().pEventHandler.addListener(new P2Listener(Events.REFRESH_TABLE) {
            @Override
            public void pingGui(P2Event runEvent) {
                refreshTable();
            }
        });
    }

    private void initColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<Episode, Integer> noColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_NO);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryIntegerMax(this.table_enum, noColumn);

        final TableColumn<Episode, String> podcastNameColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_PODCAST_NAME);
        podcastNameColumn.setCellValueFactory(new PropertyValueFactory<>("podcastName"));
        podcastNameColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryString(this.table_enum, podcastNameColumn);

        final TableColumn<Episode, String> titleColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_TITLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("episodeTitle"));
        titleColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryString(this.table_enum, titleColumn);

        final TableColumn<Episode, String> genreColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryString(this.table_enum, genreColumn);

        final TableColumn<Episode, String> startColumn = new TableColumn<>("");
        startColumn.getStyleClass().add("alignCenter");
        TableEpisodeFactory.columnFactoryButton(this.table_enum, startColumn);

        final TableColumn<Episode, LocalDate> dateColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
        dateColumn.getStyleClass().add("alignCenter");
        TableEpisodeFactory.columnFactoryLocalDate(this.table_enum, dateColumn);

        final TableColumn<Episode, String> durationColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_DURATION);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.getStyleClass().add("alignCenter");
        TableEpisodeFactory.columnFactoryString(this.table_enum, durationColumn);

        final TableColumn<Episode, P2FileSize> sizeColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_SIZE);
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("pFileSize"));
        sizeColumn.getStyleClass().add("alignCenter");
        TableEpisodeFactory.columnFactoryP2FileSize(this.table_enum, sizeColumn);

        final TableColumn<Episode, String> fileColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_NAME);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryString(this.table_enum, fileColumn);

        final TableColumn<Episode, String> pathColumn = new TableColumn<>(EpisodeFieldNames.EPISODE_FILE_PATH);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        pathColumn.getStyleClass().add("alignCenterLeft");
        TableEpisodeFactory.columnFactoryString(this.table_enum, pathColumn);

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
    }
}
