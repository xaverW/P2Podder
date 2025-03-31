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
import de.p2tools.p2lib.mtdownload.DownloadSizeData;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadFieldNames;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TableDownload extends PTable<DownloadData> {

    private final ProgData progData;

    public TableDownload(Table.TABLE_ENUM table_enum, ProgData progData) {
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

        final TableColumn<DownloadData, Integer> noColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_NO);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryIntegerMax(this.table_enum, noColumn);

        final TableColumn<DownloadData, String> genreColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, genreColumn);

        final TableColumn<DownloadData, String> podcastNameColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_PODCAST_NAME);
        podcastNameColumn.setCellValueFactory(new PropertyValueFactory<>("podcastName"));
        podcastNameColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, podcastNameColumn);

        final TableColumn<DownloadData, String> titleColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_EPISODE_TITLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("episodeTitle"));
        titleColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, titleColumn);

        final TableColumn<DownloadData, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("guiState"));
//        startColumn.setCellFactory(new CellDownloadButton<>().cellFactory);
        startColumn.getStyleClass().add("alignCenter");
        TableDownloadFactory.columnFactoryButton(this.table_enum, startColumn);

        final TableColumn<DownloadData, Double> progressColumn = new TableColumn<>("Fortschritt"); //müssen sich unterscheiden!!
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("guiProgress"));
        progressColumn.setCellFactory(new CellProgress<>().cellFactory);
        progressColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryProgress(this.table_enum, progressColumn);

        final TableColumn<DownloadData, String> remainingColumn = new TableColumn<>("Restzeit");
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        remainingColumn.getStyleClass().add("alignCenterRightPadding_25");
        TableDownloadFactory.columnFactoryString(this.table_enum, remainingColumn);

        final TableColumn<DownloadData, String> speedColumn = new TableColumn<>("Geschwindigkeit");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("bandwidth"));
        speedColumn.getStyleClass().add("alignCenterRightPadding_25");
        TableDownloadFactory.columnFactoryString(this.table_enum, speedColumn);

        final TableColumn<DownloadData, DownloadSizeData> downloadSizeColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_FILESIZE);
        downloadSizeColumn.setCellValueFactory(new PropertyValueFactory<>("downloadSize"));
        downloadSizeColumn.getStyleClass().add("alignCenter");
        TableDownloadFactory.columnFactoryDownloadSizeData(this.table_enum, downloadSizeColumn);

        final TableColumn<DownloadData, String> durationColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DURATION);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.getStyleClass().add("alignCenter");
        TableDownloadFactory.columnFactoryString(this.table_enum, durationColumn);

        final TableColumn<DownloadData, LocalDate> dateColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
//        dateColumn.setCellFactory(new P2CellLocalDate().cellFactory);
        dateColumn.getStyleClass().add("alignCenter");
        TableDownloadFactory.columnFactoryLocalDate(this.table_enum, dateColumn);

        final TableColumn<DownloadData, String> fileColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("destFileName"));
        fileColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, fileColumn);

        final TableColumn<DownloadData, String> pathColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_PATH);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("destPath"));
        pathColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, pathColumn);

        final TableColumn<DownloadData, String> urlColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("episodeUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");
        TableDownloadFactory.columnFactoryString(this.table_enum, urlColumn);

        noColumn.setPrefWidth(50);
        genreColumn.setPrefWidth(120);
        podcastNameColumn.setPrefWidth(200);
        titleColumn.setPrefWidth(250);
        startColumn.setPrefWidth(120);
        remainingColumn.setPrefWidth(120);
        speedColumn.setPrefWidth(100);
        downloadSizeColumn.setPrefWidth(100);
        fileColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(250);
        urlColumn.setPrefWidth(350);

        getColumns().addAll(noColumn, genreColumn, podcastNameColumn, titleColumn,
                startColumn, progressColumn, remainingColumn, speedColumn,
                downloadSizeColumn, durationColumn, dateColumn, fileColumn, pathColumn, urlColumn);
    }
}
