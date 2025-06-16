package de.p2tools.p2podder.gui.tools.table;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2Open;
import de.p2tools.p2lib.mediathek.download.DownloadSizeData;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2lib.tools.file.P2FileSize;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadListStartStopFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableDownloadFactory {
    private TableDownloadFactory() {
    }

    public static void columnFactoryString(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, String> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setText(item);
                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryProgress(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, Double> column) {
        column.setCellFactory(c -> new ProgressBarTableCell<>() {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                DownloadData data = getTableView().getItems().get(getIndex());
                if (item <= DownloadConstants.PROGRESS_STARTED || item >= DownloadConstants.PROGRESS_FINISHED) {
                    String text = DownloadConstants.getTextProgress(data.getState(), item.doubleValue());
                    Label label = new Label(text);
                    setGraphic(label);
                }

                set(data, this);
            }
        });
    }

    public static void columnFactoryDownloadSizeData(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, DownloadSizeData> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(DownloadSizeData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setGraphic(null);
                setText(item + "");

                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryInteger(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
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

                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryIntegerMax(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == P2LibConst.NUMBER_NOT_STARTED) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }

                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }


    public static void columnFactoryBoolean(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, Boolean> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                CheckBox box = new CheckBox();
                box.setMaxHeight(6);
                box.setMinHeight(6);
                box.setPrefSize(6, 6);
                box.setDisable(true);
                box.getStyleClass().add("checkbox-table");
                box.setSelected(item);
                setGraphic(box);

                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryLocalDate(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, LocalDate> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setText(P2LDateFactory.toString(item));
                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryLocalDateTime(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, LocalDateTime> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setText(P2LDateTimeFactory.toString(item));
                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryP2FileSize(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, P2FileSize> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(P2FileSize item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setGraphic(null);
                setText(item.getSizeStr());
                DownloadData data = getTableView().getItems().get(getIndex());
                set(data, this);
            }
        });
    }

    public static void columnFactoryButton(Table.TABLE_ENUM tableEnum, TableColumn<DownloadData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
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
                    btnDownStart.setGraphic(ProgIcons.IMAGE_TABLE_DOWNLOAD_START.getImageView());
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        ProgData.getInstance().downloadList.startDownloads(download);
                    });

                    btnDownBack = new Button("");
                    btnDownBack.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownBack.setTooltip(new Tooltip("Download zurückstellen, beim nächsten Suchen wieder anzeigen"));
                    btnDownBack.setGraphic(ProgIcons.IMAGE_TABLE_BACK.getImageView());
                    btnDownBack.setOnAction(event -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        ProgData.getInstance().downloadList.putBackDownloads(download);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.IMAGE_TABLE_DEL.getImageView());
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
                    btnDownStop.setGraphic(ProgIcons.IMAGE_TABLE_STOP.getImageView());
                    btnDownStop.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        download.stopDownload();
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.IMAGE_TABLE_DEL.getImageView());
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
                    btnOpenDirectory.setGraphic(ProgIcons.IMAGE_TABLE_FILE_OPEN.getImageView());
                    btnOpenDirectory.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        P2Open.openDir(download.getDestPath(), ProgConfig.SYSTEM_PROG_OPEN_DIR, ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
                    });

                    Table.setButtonSize(btnOpenDirectory);
                    hbox.getChildren().addAll(btnOpenDirectory);
                    setGraphic(hbox);

                } else if (item == DownloadConstants.STATE_ERROR) {
                    btnDownStart = new Button("");
                    btnDownStart.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownStart.setTooltip(new Tooltip("Download wider starten"));
                    btnDownStart.setGraphic(ProgIcons.IMAGE_TABLE_DOWNLOAD_START.getImageView());
                    btnDownStart.setOnAction((ActionEvent event) -> {
                        DownloadData download = getTableView().getItems().get(getIndex());
                        List<DownloadData> list = new ArrayList<>();
                        list.add(download);
                        ProgData.getInstance().downloadList.startDownloads(list, true);
                    });

                    btnDownDel = new Button("");
                    btnDownDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnDownDel.setTooltip(new Tooltip("Download dauerhaft löschen, Pod als gehört markieren"));
                    btnDownDel.setGraphic(ProgIcons.IMAGE_TABLE_DEL.getImageView());
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

                DownloadData download = getTableView().getItems().get(getIndex());
                set(download, this);
            }
        });
    }

    private static void set(DownloadData download, TableCell tableCell) {
        switch (download.getState()) {
            case DownloadConstants.STATE_STARTED_WAITING:
                if (ProgColorList.DOWNLOAD_WAIT.isUse()) {
                    tableCell.setStyle(ProgColorList.DOWNLOAD_WAIT.getCssFont());
                } else {
                    tableCell.setStyle("");
                }
                break;

            case DownloadConstants.STATE_STARTED_RUN:
                if (ProgColorList.DOWNLOAD_RUN.isUse()) {
                    tableCell.setStyle(ProgColorList.DOWNLOAD_RUN.getCssFont());
                } else {
                    tableCell.setStyle("");
                }
                break;

            case DownloadConstants.STATE_FINISHED:
                if (ProgColorList.DOWNLOAD_FINISHED.isUse()) {
                    tableCell.setStyle(ProgColorList.DOWNLOAD_FINISHED.getCssFont());
                } else {
                    tableCell.setStyle("");
                }
                break;

            case DownloadConstants.STATE_ERROR:
                if (ProgColorList.DOWNLOAD_ERROR.isUse()) {
                    tableCell.setStyle(ProgColorList.DOWNLOAD_ERROR.getCssFont());
                } else {
                    tableCell.setStyle("");
                }
                break;

            default:
                tableCell.setStyle("");
        }
    }
}
