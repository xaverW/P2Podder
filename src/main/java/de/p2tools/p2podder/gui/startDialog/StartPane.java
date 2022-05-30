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

package de.p2tools.p2podder.gui.startDialog;


import de.p2tools.p2Lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StartPane {
    private final Stage stage;

    public StartPane(Stage stage) {
        this.stage = stage;
    }

    public void close() {
    }

    public TitledPane makeDescription() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getImageDescription();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("" +
                "1) Podcasts" + P2LibConst.LINE_SEPARATOR +
                "Ein Podcast ist eine Serie von meist " + P2LibConst.LINE_SEPARATOR +
                "abonnierbaren Mediendateien " + P2LibConst.LINE_SEPARATOR +
                "(Audio oder Video) im Internet." + P2LibConst.LINE_SEPARATOR +
                "Hier werden alle abonnierten Podcasts" + P2LibConst.LINE_SEPARATOR +
                "angezeigt." + P2LibConst.LINE_SEPARATORx2 +

                "2) Episoden" + P2LibConst.LINE_SEPARATOR +
                "Das sind die einzelnen \"Sendungen\" " + P2LibConst.LINE_SEPARATOR +
                "eines Podcasts. Alle gespeicherten Episoden " + P2LibConst.LINE_SEPARATOR +
                "sind hier gelistet." + P2LibConst.LINE_SEPARATORx2 +

                "3) Downloads" + P2LibConst.LINE_SEPARATOR +
                "Hier werden die Episoden die " + P2LibConst.LINE_SEPARATOR +
                "geladen werden können (und noch nicht " + P2LibConst.LINE_SEPARATOR +
                "geladen wurden) angezeigt." +
                "");

        hBox.getChildren().addAll(iv, text);
        TitledPane tpConfig = new TitledPane("Was ist was?", hBox);
        return tpConfig;
    }

    public TitledPane makeStart1() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getImageHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Hier können die angezeigten" + P2LibConst.LINE_SEPARATOR +
                "Episoden gefiltert werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) Die Ansicht der Episoden, Podcasts und" + P2LibConst.LINE_SEPARATOR +
                "der Downloads wird hier umgeschaltet." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) In dem Menü befinden sich" + P2LibConst.LINE_SEPARATOR +
                "die Programmeinstellungen." +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Mit dem Pluszeichen können" + P2LibConst.LINE_SEPARATOR +
                "Spalten in der Tabelle" + P2LibConst.LINE_SEPARATOR +
                "ein- und ausgeblendet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "5) In dem Menü können Episoden" + P2LibConst.LINE_SEPARATOR +
                "gestartet und verarbeitet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "6) Damit können Episoden gestartet" + P2LibConst.LINE_SEPARATOR +
                "und gestoppt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "7) Damit können Episoden" + P2LibConst.LINE_SEPARATOR +
                "gelöscht werden.");

        hBox.getChildren().addAll(iv, text);
        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche der Episoden", hBox);
        return tpConfig;
    }

    public TitledPane makeStart2() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getImageHelpScreen2();
        iv.setSmooth(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);

        Label text = new Label("1) Damit können die angezeigten" + P2LibConst.LINE_SEPARATOR +
                "Podcasts gefiltert werden." +
                P2LibConst.LINE_SEPARATORx2 +

                "2) Damit werden neue Episoden" + P2LibConst.LINE_SEPARATOR +
                "für den Podcast gesucht." +
                P2LibConst.LINE_SEPARATORx2 +

                "3) Hier können die Podcasts" + P2LibConst.LINE_SEPARATOR +
                "gelöscht (\"X\") werden.");
        hBox.getChildren().add(text);

        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche der Podcasts", hBox);
        return tpConfig;
    }

    public TitledPane makeStart3() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getImageHelpScreen3();
        iv.setSmooth(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);

        Label text = new Label("1) Damit können die Downloads" + P2LibConst.LINE_SEPARATOR +
                "gefiltert werden." +
                P2LibConst.LINE_SEPARATORx2 +

                "2) Hier werden die Downloads" + P2LibConst.LINE_SEPARATOR +
                "gestartet (einzeln oder alle)." +
                P2LibConst.LINE_SEPARATORx2 +

                "3) Hier wird der Zielordner" + P2LibConst.LINE_SEPARATOR +
                "der Episoden geöffnet." +
                P2LibConst.LINE_SEPARATORx2 +

                "4) Hier können Downloads gestoppt," + P2LibConst.LINE_SEPARATOR +
                "zurückgestellt oder gelöscht (\"X\") werden.");
        hBox.getChildren().add(text);

        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche der Downloads", hBox);
        return tpConfig;
    }

    private javafx.scene.image.Image getImageDescription() {
        final String path = "/de/p2tools/p2podder/res/p2Podder-startpage-description.png";
        return new Image(path, 600, 600, true, true);
    }

    private javafx.scene.image.Image getImageHelpScreen1() {
        final String path = "/de/p2tools/p2podder/res/p2Podder-startpage-1.png";
        return new Image(path, 600, 600, true, true);
    }

    private javafx.scene.image.Image getImageHelpScreen2() {
        final String path = "/de/p2tools/p2podder/res/p2Podder-startpage-2.png";
        return new Image(path, 600, 600, true, true);
    }

    private javafx.scene.image.Image getImageHelpScreen3() {
        final String path = "/de/p2tools/p2podder/res/p2Podder-startpage-3.png";
        return new Image(path, 600, 600, true, true);
    }
}
