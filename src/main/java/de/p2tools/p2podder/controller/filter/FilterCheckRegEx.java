/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.controller.filter;

import de.p2tools.p2lib.data.PColorData;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


public class FilterCheckRegEx {


    // Filter wenn RegEx
    private static final PColorData FILTER_REGEX = new PColorData("COLOR_FILTER_REGEX",
            Color.rgb(206, 255, 202), Color.rgb(79, 129, 74));
    private static final PColorData FILTER_REGEX_ERROR = new PColorData("COLOR_FILTER_REGEX_ERROR",
            Color.rgb(255, 230, 230), Color.rgb(170, 0, 0));

    private final int COUNTER_MAX = 2_500;

    private TextField tf;
    private boolean colorRed = false;
    private int counter = 0;
    private ColorThread colorThread = null;


    public FilterCheckRegEx(TextField tf) {
        this.tf = tf;
        checkPattern();
    }

    public void checkPattern() {
        //Hintergrund ändern, wenn eine RegEx
        final String text = tf.getText();
        if (!Filter.isPattern(text)) {
            // kein RegEx
            colorRed = false;
            tf.setStyle("");

        } else {
            // RegEx
            if (Filter.makePattern(text) == null) {
                // aber falsch
                colorRed = true;
                tf.setStyle("");
                tf.setStyle("-fx-control-inner-background: " + FILTER_REGEX_ERROR.getColorLightToWeb() + ";");

            } else {
                // RegEx OK
                colorRed = false;
                tf.setStyle("");
                tf.setStyle("-fx-control-inner-background: " + FILTER_REGEX.getColorLightToWeb() + ";");
            }

            if (colorThread != null) {
                counter = COUNTER_MAX;
            } else {
                colorThread = new ColorThread();
                colorThread.start();
            }
        }
    }

    private class ColorThread extends Thread {

        public ColorThread() {
            setName("ColorThread");
            counter = COUNTER_MAX;
        }

        @Override
        public void run() {
            try {
                while (counter > 0) {
                    counter -= 500;
                    sleep(500);
                }
                if (!colorRed) {
                    Platform.runLater(() -> {
                        tf.setStyle("");
                    });
                }
                colorThread = null;
            } catch (final Exception ignored) {
            }
        }
    }
}

