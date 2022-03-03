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

package de.p2tools.p2podder.gui.filter;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PodcastFilterController extends FilterController {

    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final TextField txtName = new TextField();
    private final TextField txtUrl = new TextField();

    private ProgData progData;

    public PodcastFilterController() {
        super(ProgConfig.PODCAST_GUI_FILTER_ON);
        this.progData = ProgData.getInstance();

        VBox vBoxTitle = new VBox();
        vBoxTitle.getChildren().addAll(new Label("Name:"), txtName);

        VBox vBoxUrl = new VBox();
        vBoxUrl.getChildren().addAll(new Label("URL:"), txtUrl);

        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        VBox vBoxClear = new VBox();
        vBoxClear.setSpacing(5);
        vBoxClear.setPadding(new Insets(10, 0, 0, 0));
        Separator sp1 = new Separator();
        sp1.getStyleClass().add("pseperator3");
        vBoxClear.getChildren().addAll(sp1, progData.podcastGui.getPodcastFilterClear());

        VBox vBoxSpace = new VBox();
        VBox.setVgrow(vBoxSpace, Priority.ALWAYS);

        final VBox vBoxFilter = getVBoxTop();
        vBoxFilter.setPadding(new Insets(5, 5, 5, 5));
        vBoxFilter.setSpacing(15);
        vBoxFilter.getChildren().addAll(vBoxGenre, vBoxTitle, vBoxUrl, vBoxSpace, vBoxClear);

        initFilter();
    }

    private void initFilter() {
        cboGenre.getItems().addAll(progData.podcastList.getGenreList());
        ListChangeListener listChangeListener = (ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(progData.podcastList.getGenreList());
                cboGenre.getSelectionModel().select(sel);
                if (!cboGenre.getItems().contains(sel)) {
                    clearFilter();
                }
            });
        };
        progData.podcastList.getGenreList().addListener(listChangeListener);

//        progData.podcastList.addListener((v, o, n) -> {
//            cboGenre.getItems().setAll(progData.podcastList.getFilterGenreList());
//        });
        cboGenre.valueProperty().addListener((u, o, n) -> {
            if (n != null) {
                Platform.runLater(() -> {
                    //kommt sonst zu Laufzeitproblemen beim Setzen in der FilteredList
                    progData.podcastGui.getPodcastFilter().setGenre(n);
                });
            }
        });
        txtName.textProperty().addListener((u, o, n) -> {
            progData.podcastGui.getPodcastFilter().setName(txtName.getText());
        });
        txtUrl.textProperty().addListener((u, o, n) -> {
            progData.podcastGui.getPodcastFilter().setUrl(txtUrl.getText());
        });
    }

    public void clearFilter() {
        PDuration.onlyPing("Podcast-Filter löschen");
        progData.podcastList.filteredListSetPred(progData.podcastGui.getPodcastFilter().clearFilter());
        cboGenre.getSelectionModel().clearSelection();
        txtName.setText("");
        txtUrl.setText("");
    }
}
