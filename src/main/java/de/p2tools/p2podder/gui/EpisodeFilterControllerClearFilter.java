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

package de.p2tools.p2podder.gui;

import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class EpisodeFilterControllerClearFilter extends HBox {

    private final Button btnClearFilter = new Button("");
    private final Button btnGoBack = new Button("");
    private final Button btnGoForward = new Button("");

    private final ProgData progData;

    public EpisodeFilterControllerClearFilter() {
        super(5);
        progData = ProgData.getInstance();
        progData.stationFilterControllerClearFilter = this;
        addButton();
    }

    private void addButton() {
        btnGoBack.setGraphic(new ProgIcons().ICON_BUTTON_BACKWARD);
        btnGoBack.setOnAction(a -> progData.episodeFilter.getEpisodeFilterForwardBackward().goBackward());
        btnGoBack.disableProperty().bind(progData.episodeFilter.getEpisodeFilterForwardBackward().backwardProperty().not());
        btnGoBack.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnGoForward.setGraphic(new ProgIcons().ICON_BUTTON_FORWARD);
        btnGoForward.setOnAction(a -> progData.episodeFilter.getEpisodeFilterForwardBackward().goForward());
        btnGoForward.disableProperty().bind(progData.episodeFilter.getEpisodeFilterForwardBackward().forwardProperty().not());
        btnGoForward.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_CLEAR_FILTER);
        btnClearFilter.setOnAction(a -> clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Textfilter löschen, ein zweiter Klick löscht alle Filter"));

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(btnGoBack, btnGoForward);
        HBox.setHgrow(hBox, Priority.ALWAYS);

        final Button btnHelp = PButton.helpButton("Filter", HelpText.GUI_STATION_FILTER);

        getChildren().addAll(hBox, btnClearFilter, btnHelp);
    }

    private void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        progData.episodeFilter.clearFilter();
    }
}
