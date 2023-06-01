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

import de.p2tools.p2lib.guitools.PTableFactory;
import de.p2tools.p2lib.guitools.ptable.CellIntMax;
import de.p2tools.p2lib.guitools.ptable.CellLocalDate;
import de.p2tools.p2lib.mtdownload.DownloadSizeData;
import de.p2tools.p2podder.controller.config.ProgColorList;
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

    private void addListener() {
        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> this.refresh());

        ProgColorList.DOWNLOAD_WAIT_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_WAIT.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_RUN_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_RUN.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_FINISHED_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_FINISHED_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_ERROR_BG.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
        ProgColorList.DOWNLOAD_ERROR.colorProperty().addListener((a, b, c) -> PTableFactory.refreshTable(this));
    }

    private void initColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<DownloadData, Integer> noColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_NO);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.setCellFactory(new CellIntMax<DownloadData, Integer>().cellFactory);
        noColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, String> genreColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, Long> podcastNameColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_PODCAST_NAME);
        podcastNameColumn.setCellValueFactory(new PropertyValueFactory<>("podcastName"));
        podcastNameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, String> titleColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_EPISODE_TITLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("episodeTitle"));
        titleColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("guiState"));
        startColumn.setCellFactory(new CellDownloadButton<>().cellFactory);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<DownloadData, Double> progressColumn = new TableColumn<>("Fortschritt"); //müssen sich unterscheiden!!
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("guiProgress"));
        progressColumn.setCellFactory(new CellProgress<>().cellFactory);
        progressColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, Integer> remainingColumn = new TableColumn<>("Restzeit");
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        remainingColumn.getStyleClass().add("alignCenterRightPadding_25");

        final TableColumn<DownloadData, Integer> speedColumn = new TableColumn<>("Geschwindigkeit");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("bandwidth"));
        speedColumn.getStyleClass().add("alignCenterRightPadding_25");

        final TableColumn<DownloadData, DownloadSizeData> downloadSizeColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_FILESIZE);
        downloadSizeColumn.setCellValueFactory(new PropertyValueFactory<>("downloadSize"));
        downloadSizeColumn.getStyleClass().add("alignCenter");

        final TableColumn<DownloadData, String> durationColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DURATION);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.getStyleClass().add("alignCenter");

        final TableColumn<DownloadData, LocalDate> dateColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
        dateColumn.setCellFactory(new CellLocalDate().cellFactory);
        dateColumn.getStyleClass().add("alignCenter");

        final TableColumn<DownloadData, String> fileColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("destFileName"));
        fileColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, String> pathColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_PATH);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("destPath"));
        pathColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<DownloadData, String> urlColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("episodeUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");

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

//        setRowFactory(tableRow -> {
//            TableRowDownload row = new TableRowDownload();
//            return row;
//        });
    }
}
