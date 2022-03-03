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

import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.gui.tools.HelpText;
import de.p2tools.p2podder.tools.storedFilter.StoredFilters;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DownloadFilterClear extends VBox {

    private final Button btnClearFilter = new Button("");
    private final Button btnGoBack = new Button("");
    private final Button btnGoForward = new Button("");

    private final ProgData progData;

    public DownloadFilterClear() {
        super();
        progData = ProgData.getInstance();

        setSpacing(20);
        addButton();
    }

    private void addButton() {
        StoredFilters storedFiltersDownload = progData.downloadGui.getStoredFiltersDownload();

        btnGoBack.setGraphic(new ProgIcons().ICON_BUTTON_BACKWARD);
        btnGoBack.setOnAction(a -> storedFiltersDownload.getStoredFiltersForwardBackward().goBackward());
        btnGoBack.disableProperty().bind(storedFiltersDownload.getStoredFiltersForwardBackward().backwardProperty().not());
        btnGoBack.setTooltip(new Tooltip("Letzte Filtereinstellung wieder herstellen"));
        btnGoForward.setGraphic(new ProgIcons().ICON_BUTTON_FORWARD);
        btnGoForward.setOnAction(a -> storedFiltersDownload.getStoredFiltersForwardBackward().goForward());
        btnGoForward.disableProperty().bind(storedFiltersDownload.getStoredFiltersForwardBackward().forwardProperty().not());
        btnGoForward.setTooltip(new Tooltip("Letzte Filtereinstellung wieder herstellen"));

        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_RESET);
        btnClearFilter.setOnAction(a -> clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Alle Filter löschen"));

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(btnGoBack, btnGoForward);
        HBox.setHgrow(hBox, Priority.ALWAYS);

        final Button btnHelp = PButton.helpButton("Filter", HelpText.GUI_STATION_FILTER);

        HBox hBoxAll = new HBox(5);
        hBoxAll.getChildren().addAll(hBox, btnClearFilter, btnHelp);
        getChildren().addAll(hBoxAll);
    }

    private void clearFilter() {
        progData.downloadGui.getPodcastFilter().clearFilter();
    }
}
