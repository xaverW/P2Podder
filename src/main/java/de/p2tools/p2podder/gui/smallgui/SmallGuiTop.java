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

package de.p2tools.p2podder.gui.smallgui;

import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIconsP2Podder;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class SmallGuiTop extends HBox {

    private final RadioButton rbAll = new RadioButton("alle");
    private final RadioButton rbStarted = new RadioButton("gestartete");
    private final RadioButton rbRunning = new RadioButton("läuft");
    private final RadioButton rbWasShown = new RadioButton("gehörte");
    private final RadioButton rbNew = new RadioButton("neue");
    private Button btnRadio = new Button("");
    private final Button btnClose = new Button();
    private final ProgData progData;

    private SmallGuiPack smallGuiPack;

    public SmallGuiTop(SmallGuiPack smallGuiPack) {
        this.smallGuiPack = smallGuiPack;
        progData = ProgData.getInstance();

        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> progData.smallGuiPack.changeGui());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnRadio.setGraphic(ProgIconsP2Podder.ICON_TOOLBAR_SMALL_PODDER_20.getImageView());

        btnClose.setTooltip(new Tooltip("Programm beenden"));
        btnClose.setOnAction(e -> {
            ProgQuitFactory.quit(smallGuiPack.getStage(), true);
        });
        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnClose.setGraphic(ProgIconsP2Podder.ICON_BUTTON_STOP.getImageView());

        ToggleGroup tg = new ToggleGroup();
        rbAll.setToggleGroup(tg);
        rbStarted.setToggleGroup(tg);
        rbRunning.setToggleGroup(tg);
        rbWasShown.setToggleGroup(tg);
        rbNew.setToggleGroup(tg);

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(15);
        getChildren().addAll(btnRadio, P2GuiTools.getHBoxGrower(),
                new Label("Episoden: "), rbAll, rbNew, rbStarted, rbRunning, rbWasShown, P2GuiTools.getHBoxGrower(), btnClose);

        initFilter();
    }

    private void initFilter() {
        rbAll.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isAllProperty());
        rbNew.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isNewProperty());
        rbStarted.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isStartedProperty());
        rbRunning.selectedProperty().bindBidirectional(progData.episodeFilterSmall.isRunningProperty());
        rbWasShown.selectedProperty().bindBidirectional(progData.episodeFilterSmall.wasShownProperty());
    }
}
