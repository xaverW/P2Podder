/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.gui.podcastadd;

import de.p2tools.p2podder.controller.config.ProgConfig;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PodcastAddDialogFactory {

    private PodcastAddDialogFactory() {
    }

    public static Color getBlue() {
        if (ProgConfig.SYSTEM_DARK_THEME.getValue()) {
            return Color.rgb(31, 162, 206);
        } else {
            return Color.BLUE;
        }
    }

    public static Text getText(String text) {
        Text t = new Text(text);
        t.setFont(Font.font(null, FontWeight.BOLD, -1));
        t.setFill(getBlue());
        return t;
    }
}
