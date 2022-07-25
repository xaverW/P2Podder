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

import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadFieldNames;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class TableDownload {

    private final ProgData progData;

    public TableDownload(ProgData progData) {
        this.progData = progData;
    }

    public TableColumn[] initDownloadColumn(TableView table) {
        table.getColumns().clear();

        //todo
        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> Table.refresh_table(table));

        final TableColumn<Download, Integer> noColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_NO);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.setCellFactory(cellFactoryNo);
        noColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, String> genreColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, Long> podcastNameColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_PODCAST_NAME);
        podcastNameColumn.setCellValueFactory(new PropertyValueFactory<>("podcastName"));
        podcastNameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, String> titleColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_EPISODE_TITLE);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("episodeTitle"));
        titleColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("guiState"));
        startColumn.setCellFactory(cellFactoryButton);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Download, Double> progressColumn = new TableColumn<>("Fortschritt"); //müssen sich unterscheiden!!
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("guiProgress"));
        progressColumn.setCellFactory(cellFactoryProgress);
        progressColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, Integer> remainingColumn = new TableColumn<>("Restzeit");
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        remainingColumn.getStyleClass().add("alignCenterRightPadding_25");

        final TableColumn<Download, Integer> speedColumn = new TableColumn<>("Geschwindigkeit");
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("bandwidth"));
        speedColumn.getStyleClass().add("alignCenterRightPadding_25");

        final TableColumn<Download, Long> pdownloadSizeColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_FILESIZE);
        pdownloadSizeColumn.setCellValueFactory(new PropertyValueFactory<>("pdownloadSize"));
        pdownloadSizeColumn.setCellFactory(cellFactorySize);
        pdownloadSizeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Download, String> durationColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DURATION);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.getStyleClass().add("alignCenter");

        final TableColumn<Download, PDate> datumColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DATE);
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("pubDate"));
        datumColumn.getStyleClass().add("alignCenter");

        final TableColumn<Download, String> fileColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("destFileName"));
        fileColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, String> pathColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_DEST_PATH);
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("destPath"));
        pathColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Download, String> urlColumn = new TableColumn<>(DownloadFieldNames.DOWNLOAD_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("episodeUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        noColumn.setPrefWidth(50);
        genreColumn.setPrefWidth(120);
        podcastNameColumn.setPrefWidth(200);
        titleColumn.setPrefWidth(250);
        startColumn.setPrefWidth(120);
        remainingColumn.setPrefWidth(120);
        speedColumn.setPrefWidth(100);
        pdownloadSizeColumn.setPrefWidth(100);
        fileColumn.setPrefWidth(200);
        pathColumn.setPrefWidth(250);
        urlColumn.setPrefWidth(350);

        addRowFact(table);

        return new TableColumn[]{
                noColumn, genreColumn, podcastNameColumn, titleColumn,
                startColumn, progressColumn, remainingColumn, speedColumn,
                pdownloadSizeColumn, durationColumn, datumColumn, fileColumn, pathColumn, urlColumn
        };
    }

    private void addRowFact(TableView<Download> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Download download, boolean empty) {
                super.updateItem(download, empty);

                setStyle("");
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (download != null && !empty) {

                    if (download.getDownloadStart() != null && download.getDownloadStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(download.getDownloadStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);
                    }

                    switch (download.getState()) {
                        case DownloadConstants.STATE_STARTED_WAITING:
                            if (ProgColorList.DOWNLOAD_WAIT_BG.isUse()) {
                                setStyle(ProgColorList.DOWNLOAD_WAIT_BG.getCssBackgroundAndSel());
                            }
                            if (ProgColorList.DOWNLOAD_WAIT.isUse()) {
                                for (int i = 0; i < getChildren().size(); i++) {
                                    getChildren().get(i).setStyle(ProgColorList.DOWNLOAD_WAIT.getCssFont());
                                }
                            }
                            break;
                        case DownloadConstants.STATE_STARTED_RUN:
                            if (ProgColorList.DOWNLOAD_RUN_BG.isUse()) {
                                setStyle(ProgColorList.DOWNLOAD_RUN_BG.getCssBackgroundAndSel());
                            }
                            if (ProgColorList.DOWNLOAD_RUN.isUse()) {
                                for (int i = 0; i < getChildren().size(); i++) {
                                    getChildren().get(i).setStyle(ProgColorList.DOWNLOAD_RUN.getCssFont());
                                }
                            }
                            break;
                        case DownloadConstants.STATE_FINISHED:
                            if (ProgColorList.DOWNLOAD_FINISHED_BG.isUse()) {
                                setStyle(ProgColorList.DOWNLOAD_FINISHED_BG.getCssBackgroundAndSel());
                            }
                            if (ProgColorList.DOWNLOAD_FINISHED.isUse()) {
                                for (int i = 0; i < getChildren().size(); i++) {
                                    getChildren().get(i).setStyle(ProgColorList.DOWNLOAD_FINISHED.getCssFont());
                                }
                            }
                            break;
                        case DownloadConstants.STATE_ERROR:
                            if (ProgColorList.DOWNLOAD_ERROR_BG.isUse()) {
                                setStyle(ProgColorList.DOWNLOAD_ERROR_BG.getCssBackgroundAndSel());
                            }
                            if (ProgColorList.DOWNLOAD_ERROR.isUse()) {
                                for (int i = 0; i < getChildren().size(); i++) {
                                    getChildren().get(i).setStyle(ProgColorList.DOWNLOAD_ERROR.getCssFont());
                                }
                            }
                            break;
                    }
                }
            }
        });
    }

    private Callback<TableColumn<Download, Integer>, TableCell<Download, Integer>> cellFactoryButton
            = (final TableColumn<Download, Integer> param) -> {

        final TableCell<Download, Integer> cell = new TableCell<Download, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button btnDownStart;
                final Button btnDownBack;
                final Button btnDownDel;
                final Button btnDownStop;
                final Button btnOpenDirectory;

                if (item <= DownloadConstants.STATE_STOPPED) {
                    btnDownStart = new Button("");
                    btnDownStart.getStyleClass().add("btnSmallPodder");
                    btnDownStart.setTooltip(new Tooltip("Download starten"));
                    btnDownStart.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_START));
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        Download download = getTableView().getItems().get(getIndex());
                        progData.downloadList.startDownloads(download);
                    });

                    btnDownBack = new Button("");
                    btnDownBack.getStyleClass().add("btnSmallPodder");
                    btnDownBack.setTooltip(new Tooltip("Download zurückstellen, beim nächsten Suchen wieder anzeigen"));
                    btnDownBack.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_BACK));
                    btnDownBack.setOnAction(event -> {
                        Download download = getTableView().getItems().get(getIndex());
                        progData.downloadList.putBackDownloads(download);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().add("btnSmallPodder");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_DEL));
                    btnDownDel.setOnAction(event -> {
                        Download download = getTableView().getItems().get(getIndex());
                        DownloadListStartStopFactory.delDownloads(download);
                    });

                    Table.setButtonSize(btnDownStart);
                    Table.setButtonSize(btnDownBack);
                    Table.setButtonSize(btnDownDel);
                    hbox.getChildren().addAll(btnDownStart, btnDownBack, btnDownDel);
                    setGraphic(hbox);

                } else if (item < DownloadConstants.STATE_FINISHED) {
                    btnDownStop = new Button("");
                    btnDownStop.getStyleClass().add("btnSmallPodder");
                    btnDownStop.setTooltip(new Tooltip("Download stoppen"));
                    btnDownStop.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_STOP));
                    btnDownStop.setOnAction((ActionEvent event) -> {
                        Download download = getTableView().getItems().get(getIndex());
                        download.stopDownload();
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().add("btnSmallPodder");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_DEL));
                    btnDownDel.setOnAction(event -> {
                        Download download = getTableView().getItems().get(getIndex());
                        DownloadListStartStopFactory.delDownloads(download);
                    });

                    Table.setButtonSize(btnDownStop);
                    Table.setButtonSize(btnDownDel);
                    hbox.getChildren().addAll(btnDownStop, btnDownDel);
                    setGraphic(hbox);

                } else if (item == DownloadConstants.STATE_FINISHED) {
                    btnOpenDirectory = new Button();
                    btnOpenDirectory.getStyleClass().add("btnSmallPodder");
                    btnOpenDirectory.setTooltip(new Tooltip("Ordner mit gespeichertem Film öffnen"));
                    btnOpenDirectory.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_OPEN_DIR));
                    btnOpenDirectory.setOnAction((ActionEvent event) -> {
                        Download download = getTableView().getItems().get(getIndex());
                        POpen.openDir(download.getDestPath(), ProgConfig.SYSTEM_PROG_OPEN_DIR, new ProgIcons().ICON_BUTTON_FILE_OPEN);
                    });

                    Table.setButtonSize(btnOpenDirectory);
                    hbox.getChildren().addAll(btnOpenDirectory);
                    setGraphic(hbox);

                } else if (item == DownloadConstants.STATE_ERROR) {
                    btnDownStart = new Button("");
                    btnDownStart.getStyleClass().add("btnSmallPodder");
                    btnDownStart.setTooltip(new Tooltip("Download wider starten"));
                    btnDownStart.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_START));
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        Download download = getTableView().getItems().get(getIndex());
                        List<Download> list = new ArrayList<>();
                        list.add(download);
                        ProgData.getInstance().downloadList.startDownloads(list, true);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().add("btnSmallPodder");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_DOWNLOAD_DEL));
                    btnDownDel.setOnAction(event -> {
                        Download download = getTableView().getItems().get(getIndex());
                        DownloadListStartStopFactory.delDownloads(download);
                    });

                    Table.setButtonSize(btnDownStart);
                    Table.setButtonSize(btnDownDel);
                    hbox.getChildren().addAll(btnDownStart, btnDownDel);
                    setGraphic(hbox);

                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<Download, Double>, TableCell<Download, Double>> cellFactoryProgress
            = (final TableColumn<Download, Double> param) -> {

        final ProgressBarTableCell<Download> cell = new ProgressBarTableCell<>() {

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    Download download = getTableView().getItems().get(getIndex());
                    if (item <= DownloadConstants.PROGRESS_STARTED || item >= DownloadConstants.PROGRESS_FINISHED) {
                        String text = DownloadConstants.getTextProgress(false, download.getState(), item.doubleValue());
                        Label label = new Label(text);
                        setGraphic(label);
                    }
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<Download, Long>, TableCell<Download, Long>> cellFactorySize
            = (final TableColumn<Download, Long> param) -> {

        final TableCell<Download, Long> cell = new TableCell<>() {

            @Override
            public void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                setGraphic(null);
                if (item == null || empty) {
                    setText(null);
                    return;
                }

                Download download = getTableView().getItems().get(getIndex());
                setText(download.getPdownloadSize().getActSizeString());
            }
        };
        return cell;
    };

    private Callback<TableColumn<Download, Integer>, TableCell<Download, Integer>> cellFactoryBitrate
            = (final TableColumn<Download, Integer> param) -> {

        final TableCell<Download, Integer> cell = new TableCell<>() {

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
    private Callback<TableColumn<Download, Integer>, TableCell<Download, Integer>> cellFactoryGrade
            = (final TableColumn<Download, Integer> param) -> {

        final TableCell<Download, Integer> cell = new TableCell<>() {

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
                    for (int i = 0; i < DownloadConstants.MAX_EPISODE_GRADE; ++i) {
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

    private Callback<TableColumn<Download, Integer>, TableCell<Download, Integer>> cellFactoryNo
            = (final TableColumn<Download, Integer> param) -> {

        final TableCell<Download, Integer> cell = new TableCell<Download, Integer>() {

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
}
