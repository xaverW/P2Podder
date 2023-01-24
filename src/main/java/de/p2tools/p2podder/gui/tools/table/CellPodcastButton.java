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

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.parser.ParserThread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellPodcastButton<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<Podcast, Integer>, TableCell<Podcast, Integer>> cellFactory
            = (final TableColumn<Podcast, Integer> param) -> {

        final TableCell<Podcast, Integer> cell = new TableCell<Podcast, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                Podcast podcast = getTableView().getItems().get(getIndex());

                final Button btnUpdate;
                btnUpdate = new Button("");
                btnUpdate.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnUpdate.setTooltip(new Tooltip("Podcast aktualisieren"));
                btnUpdate.setGraphic(ProgIcons.Icons.IMAGE_TABLE_PODCAST_UPDATE.getImageView());
                btnUpdate.setOnAction(event -> {
                    new ParserThread(ProgData.getInstance()).parse(podcast);
                });

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnDel.setTooltip(new Tooltip("Podcast löschen"));
                btnDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_PODCAST_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    ProgData.getInstance().podcastList.removePodcast(podcast);
                });

                final Button btnActive;
                btnActive = new Button("");
                btnActive.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnActive.setTooltip(new Tooltip("Podcast ein-/ausschalten"));
                btnActive.setGraphic(ProgIcons.Icons.IMAGE_TABLE_PODCAST_SET_ACTIVE.getImageView());
                btnActive.setOnAction(event -> {
                    ProgData.getInstance().worker.setPodcastActive(podcast);
                });

                Table.setButtonSize(btnUpdate);
                Table.setButtonSize(btnDel);
                Table.setButtonSize(btnActive);

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));
                hbox.getChildren().addAll(btnUpdate, btnDel, btnActive);
                setGraphic(hbox);
            }
        };
        return cell;
    };
}
