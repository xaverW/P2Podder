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
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PButtonClearFilterFactory;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class EpisodeFilterControllerClearFilter extends HBox {

    private final ProgData progData;

    public EpisodeFilterControllerClearFilter() {
        super(5);
        progData = ProgData.getInstance();
        addButton();
    }

    private void addButton() {
        Button btnClearFilter = PButtonClearFilterFactory.getPButtonClear();
        Button btnGoBack = new Button("");
        Button btnGoForward = new Button("");
        Button btnHelp = PButton.helpButton("Filter", HelpText.GUI_STATION_FILTER);

        btnGoBack.setGraphic(ProgIcons.Icons.ICON_BUTTON_BACKWARD.getImageView());
        btnGoBack.setOnAction(a -> progData.episodeFilter.getEpisodeFilterForwardBackward().goBackward());
        btnGoBack.disableProperty().bind(progData.episodeFilter.getEpisodeFilterForwardBackward().backwardProperty().not());
        btnGoBack.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnGoForward.setGraphic(ProgIcons.Icons.ICON_BUTTON_FORWARD.getImageView());
        btnGoForward.setOnAction(a -> progData.episodeFilter.getEpisodeFilterForwardBackward().goForward());
        btnGoForward.disableProperty().bind(progData.episodeFilter.getEpisodeFilterForwardBackward().forwardProperty().not());
        btnGoForward.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnClearFilter.setGraphic(ProgIcons.Icons.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(a -> clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Textfilter löschen, ein zweiter Klick löscht alle Filter"));

        btnGoBack.setMaxHeight(Double.MAX_VALUE);
        btnGoForward.setMaxHeight(Double.MAX_VALUE);
        btnClearFilter.setMaxHeight(Double.MAX_VALUE);
        btnHelp.setMaxHeight(Double.MAX_VALUE);

        HBox hBox = new HBox(P2LibConst.DIST_BUTTON);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(btnGoBack, btnGoForward);
        HBox.setHgrow(hBox, Priority.ALWAYS);

        setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(hBox, btnClearFilter, btnHelp);
    }

    private void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        progData.episodeFilter.clearFilter();
    }
}
