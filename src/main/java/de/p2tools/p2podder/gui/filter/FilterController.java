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

import de.p2tools.p2Lib.guiTools.pClosePane.PClosePaneV;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilterController extends PClosePaneV {


    private final VBox vBoxAll = new VBox();
    private final VBox vBoxTop = new VBox(20);
    private final VBox vBoxBottom = new VBox();

    public FilterController(BooleanProperty divOn) {
        super(divOn, true);

        getVBoxAll().getChildren().add(vBoxAll);
        initVBox();
    }

    public VBox getVBoxTop() {
        return vBoxTop;
    }

    public VBox getVBoxBottom() {
        return vBoxBottom;
    }

    private void initVBox() {
        VBox.setVgrow(vBoxAll, Priority.ALWAYS);

        vBoxTop.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(vBoxTop, Priority.ALWAYS);

        vBoxBottom.getStyleClass().add("extra-pane");
        vBoxBottom.setPadding(new Insets(10));
        vBoxBottom.setSpacing(20);

        Separator sp = new Separator();
        sp.setVisible(false);
        sp.setMinHeight(10);

        vBoxAll.getChildren().addAll(vBoxTop, sp, vBoxBottom);
    }
}
