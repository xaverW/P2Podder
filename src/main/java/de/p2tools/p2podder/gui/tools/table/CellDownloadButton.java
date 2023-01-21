/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class CellDownloadButton<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<DownloadData, Integer>, TableCell<DownloadData, Integer>> cellFactory
            = (final TableColumn<DownloadData, Integer> param) -> {

        final TableCell<DownloadData, Integer> cell = new TableCell<DownloadData, Integer>() {

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
                    btnDownStart.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownStart.setTooltip(new Tooltip("Download starten"));
                    btnDownStart.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_START.getImageView());
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        ProgData.getInstance().downloadList.startDownloads(download);
                    });

                    btnDownBack = new Button("");
                    btnDownBack.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownBack.setTooltip(new Tooltip("Download zurückstellen, beim nächsten Suchen wieder anzeigen"));
                    btnDownBack.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_BACK.getImageView());
                    btnDownBack.setOnAction(event -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        ProgData.getInstance().downloadList.putBackDownloads(download);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_DEL.getImageView());
                    btnDownDel.setOnAction(event -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        DownloadListStartStopFactory.delDownloads(download);
                    });

                    Table.setButtonSize(btnDownStart);
                    Table.setButtonSize(btnDownBack);
                    Table.setButtonSize(btnDownDel);
                    hbox.getChildren().addAll(btnDownStart, btnDownBack, btnDownDel);
                    setGraphic(hbox);

                } else if (item < DownloadConstants.STATE_FINISHED) {
                    btnDownStop = new Button("");
                    btnDownStop.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownStop.setTooltip(new Tooltip("Download stoppen"));
                    btnDownStop.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_STOP.getImageView());
                    btnDownStop.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        download.stopDownload();
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_DEL.getImageView());
                    btnDownDel.setOnAction(event -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        DownloadListStartStopFactory.delDownloads(download);
                    });

                    Table.setButtonSize(btnDownStop);
                    Table.setButtonSize(btnDownDel);
                    hbox.getChildren().addAll(btnDownStop, btnDownDel);
                    setGraphic(hbox);

                } else if (item == DownloadConstants.STATE_FINISHED) {
                    btnOpenDirectory = new Button();
                    btnOpenDirectory.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnOpenDirectory.setTooltip(new Tooltip("Ordner mit gespeichertem Film öffnen"));
                    btnOpenDirectory.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_OPEN_DIR.getImageView());
                    btnOpenDirectory.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        POpen.openDir(download.getDestPath(), ProgConfig.SYSTEM_PROG_OPEN_DIR, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
                    });

                    Table.setButtonSize(btnOpenDirectory);
                    hbox.getChildren().addAll(btnOpenDirectory);
                    setGraphic(hbox);

                } else if (item == DownloadConstants.STATE_ERROR) {
                    btnDownStart = new Button("");
                    btnDownStart.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownStart.setTooltip(new Tooltip("Download wider starten"));
                    btnDownStart.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_START.getImageView());
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        List<DownloadData> list = new ArrayList<>();
                        list.add(download);
                        ProgData.getInstance().downloadList.startDownloads(list, true);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_DOWNLOAD_DEL.getImageView());
                    btnDownDel.setOnAction(event -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
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
}
