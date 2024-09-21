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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.filter.FilterCheckRegEx;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PodcastFilterController extends VBox {

    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final TextField txtName = new TextField();
    private final TextField txtUrl = new TextField();


    private ProgData progData;

    public PodcastFilterController() {
//        super(ProgConfig.PODCAST_GUI_FILTER_ON);
        this.progData = ProgData.getInstance();

        VBox vBoxTxt = new VBox();
        vBoxTxt.getChildren().addAll(new Label("Name:"), txtName);
        vBoxTxt.getChildren().addAll(new Label("URL:"), txtUrl);

        VBox vBoxGenre = new VBox();
        cboGenre.setMaxWidth(Double.MAX_VALUE);
        vBoxGenre.getChildren().addAll(new Label("Genre: "), cboGenre);

        setPadding(new Insets(P2LibConst.PADDING));
        setSpacing(P2LibConst.DIST_BUTTON);
        getChildren().addAll(vBoxGenre, vBoxTxt);

        addButton();
        initFilter();
    }

    private void addButton() {
        Button btnClearFilter = new Button("");
        btnClearFilter.setGraphic(ProgIcons.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(a -> progData.podcastFilter.clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Alle Filter löschen"));

        HBox hBoxAll = new HBox(5);
        hBoxAll.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hBoxAll, Priority.ALWAYS);
        hBoxAll.getChildren().addAll(btnClearFilter);

        Separator separator = new Separator();
        separator.getStyleClass().add("pseperator2");

        getChildren().addAll(P2GuiTools.getHDistance(10), separator, hBoxAll);
    }

    private void initFilter() {
        cboGenre.getItems().addAll(progData.podcastList.getGenreList());
        progData.podcastList.getGenreList().addListener((ListChangeListener<String>) change -> {
            Platform.runLater(() -> {
                String sel = cboGenre.getSelectionModel().getSelectedItem();
                cboGenre.getItems().setAll(progData.podcastList.getGenreList());
                if (cboGenre.getItems().contains(sel)) {
                    cboGenre.getSelectionModel().select(sel);
                } else {
                    progData.podcastFilter.clearFilter();
                }
            });
        });
        cboGenre.valueProperty().bindBidirectional(progData.podcastFilter.genreProperty());

        txtName.textProperty().bindBidirectional(progData.podcastFilter.nameProperty());
        FilterCheckRegEx fName = new FilterCheckRegEx(txtName);
        txtName.textProperty().addListener((observable, oldValue, newValue) -> fName.checkPattern());

        txtUrl.textProperty().bindBidirectional(progData.podcastFilter.urlProperty());
        FilterCheckRegEx fUrl = new FilterCheckRegEx(txtUrl);
        txtUrl.textProperty().addListener((observable, oldValue, newValue) -> fUrl.checkPattern());
    }
}
