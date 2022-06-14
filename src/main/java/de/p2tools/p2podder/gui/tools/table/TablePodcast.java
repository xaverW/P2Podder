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

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.data.podcast.PodcastNames;
import de.p2tools.p2podder.controller.parser.ParserThread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TablePodcast {

    private final ProgData progData;

    public TablePodcast(ProgData progData) {
        this.progData = progData;
    }

    public TableColumn[] initStationColumn(TableView table) {
        table.getColumns().clear();

        // bei Farbänderung der Schriftfarbe klappt es damit besser: Table.refresh_table(table)
        ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW.addListener((observableValue, s, t1) -> table.refresh());
        ProgColorList.EPISODE_ERROR.colorProperty().addListener((a, b, c) -> table.refresh());

        final TableColumn<Podcast, Integer> nrColumn = new TableColumn<>(PodcastNames.PODCAST_NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, String> nameColumn = new TableColumn<>(PodcastNames.PODCAST_NAME);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, String> genreColumn = new TableColumn<>(PodcastNames.PODCAST_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Podcast, String> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryStart);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Podcast, PDate> dateColumn = new TableColumn<>(PodcastNames.PODCAST_DATE);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
        websiteColumn.setPrefWidth(350);
        urlColumn.setPrefWidth(350);
        addRowFact(table);

        return new TableColumn[]{
                nrColumn, nameColumn, genreColumn, startColumn, dateColumn, websiteColumn, urlColumn/*, urlrColumn*/
        };
    }

    private void addRowFact(TableView<Podcast> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Podcast podcast, boolean empty) {
                super.updateItem(podcast, empty);

                if (podcast == null || empty) {
                    setStyle("");
                }
            }
        });
    }

    private Callback<TableColumn<Podcast, String>, TableCell<Podcast, String>> cellFactoryStart
            = (final TableColumn<Podcast, String> param) -> {

        final TableCell<Podcast, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(4);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                Podcast podcast = getTableView().getItems().get(getIndex());

                final Button btnUpdate;
                btnUpdate = new Button("");
                btnUpdate.getStyleClass().add("btnSmallPodder");
                btnUpdate.setTooltip(new Tooltip("Podcast aktualisieren"));
                btnUpdate.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_PODCAST_UPDATE));
                btnUpdate.setOnAction(event -> {
                    new ParserThread(progData).parse(podcast);
                });

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().add("btnSmallPodder");
                btnDel.setTooltip(new Tooltip("Podcast löschen"));
                btnDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_PODCAST_DEL));
                btnDel.setOnAction(event -> {
                    progData.podcastList.removePodcast(podcast);
                });

                Table.setButtonSize(btnUpdate);
                Table.setButtonSize(btnDel);
                hbox.getChildren().addAll(btnUpdate, btnDel);
                setGraphic(hbox);
            }
        };
        return cell;
    };
}
