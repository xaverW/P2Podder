package de.p2tools.p2podder.gui.tools.table;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2lib.tools.file.P2FileSize;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TableEpisodeFactory {
    private TableEpisodeFactory() {
    }

    public static void columnFactoryString(Table.TABLE_ENUM tableEnum, TableColumn<Episode, String> column) {
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
                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryInteger(Table.TABLE_ENUM tableEnum, TableColumn<Episode, Integer> column) {
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

                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryIntegerMax(Table.TABLE_ENUM tableEnum, TableColumn<Episode, Integer> column) {
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

                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }


    public static void columnFactoryBoolean(Table.TABLE_ENUM tableEnum, TableColumn<Episode, Boolean> column) {
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

                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryLocalDate(Table.TABLE_ENUM tableEnum, TableColumn<Episode, LocalDate> column) {
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
                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryLocalDateTime(Table.TABLE_ENUM tableEnum, TableColumn<Episode, LocalDateTime> column) {
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
                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryP2FileSize(Table.TABLE_ENUM tableEnum, TableColumn<Episode, P2FileSize> column) {
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
                Episode data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryButton(Table.TABLE_ENUM tableEnum, TableColumn<Episode, String> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                Episode episode = getTableView().getItems().get(getIndex());
                final boolean started = EpisodeFactory.episodeIsStarted(episode);
                final boolean running = EpisodeFactory.episodeIsRunning(episode);
                if (started || running) {
                    //dann stoppen
                    final Button btnStop;
                    btnStop = new Button("");
                    btnStop.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnStop.setTooltip(new Tooltip("Episode stoppen"));
                    btnStop.setGraphic(ProgIcons.IMAGE_TABLE_EPISODE_STOP_PLAY.getImageView());
                    btnStop.setOnAction((ActionEvent event) -> {
                        EpisodeFactory.stopEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    Table.setButtonSize(btnStop);
                    hbox.getChildren().add(btnStop);

                } else {
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnPlay.setTooltip(new Tooltip("Episode abspielen"));
                    btnPlay.setGraphic(ProgIcons.IMAGE_TABLE_EPISODE_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        EpisodeFactory.playEpisode(episode);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    Table.setButtonSize(btnPlay);
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnDel.setTooltip(new Tooltip("Episode löschen"));
                btnDel.setGraphic(ProgIcons.IMAGE_TABLE_EPISODE_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    EpisodeFactory.delEpisode(episode);
                });

                Table.setButtonSize(btnDel);
                hbox.getChildren().add(btnDel);
                setGraphic(hbox);

                set(tableEnum, episode, this);
            }
        });
    }

    private static void set(Table.TABLE_ENUM tableEnum, Episode episode, TableCell tableCell) {

        final boolean started = EpisodeFactory.episodeIsStarted(episode);
        final boolean running = EpisodeFactory.episodeIsRunning(episode);
        final boolean error = episode.getStart() != null ? episode.getStart().getStartStatus().isStateError() : false;
        final boolean history = ProgData.getInstance().historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl());

        tableCell.setStyle("");
        //und jetzt die Farben setzen
        if (error) {
            if (ProgColorList.EPISODE_ERROR.isUse()) {
                tableCell.setStyle(ProgColorList.EPISODE_ERROR.getCssFont());
            }

        } else if (started && !running) {
            if (ProgColorList.EPISODE_STARTED.isUse()) {
                tableCell.setStyle(ProgColorList.EPISODE_STARTED.getCssFont());
            }

        } else if (running) {
            if (ProgColorList.EPISODE_RUNNING.isUse()) {
                tableCell.setStyle(ProgColorList.EPISODE_RUNNING.getCssFont());
            }

        } else if (!error && !started && !running && history) {
            if (ProgColorList.EPISODE_HISTORY.isUse()) {
                tableCell.setStyle(ProgColorList.EPISODE_HISTORY.getCssFont());
            }

        } else if (episode.isNew()) {
            //neue Episode
            if (ProgColorList.EPISODE_NEW.isUse()) {
                tableCell.setStyle(ProgColorList.EPISODE_NEW.getCssFont());
            }
        }
    }
}
