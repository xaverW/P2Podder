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

import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellEpisodeButton<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<Episode, Integer>, TableCell<Episode, Integer>> cellFactory
            = (final TableColumn<Episode, Integer> param) -> {

        final TableCell<Episode, Integer> cell = new TableCell<Episode, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
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
            }
        };
        return cell;
    };
}
