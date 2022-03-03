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

package de.p2tools.p2podder.gui.dialog;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextAreaIgnoreTab;
import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.data.podcast.PodcastNames;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class PodcastEditDialogController extends PDialogExtra {

    private final GridPane gridPane = new GridPane();
    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");

    private final Label[] lbl = new Label[PodcastNames.MAX_ELEM];
    private final TextField[] txt = new TextField[PodcastNames.MAX_ELEM];
    private final TextArea textArea = new PTextAreaIgnoreTab(false, true);
    private final ComboBox<String> cboGenre = new ComboBox<>();

    private boolean addNewPodcast;
    private BooleanProperty okProp = new SimpleBooleanProperty(true);
    private boolean ok = false;

    private final Podcast podcast;
    private ProgData progData;

    public PodcastEditDialogController(ProgData progData, Podcast podcast, boolean addNewPodcast) {
        //hier wird ein neues Abo angelegt!
        super(progData.primaryStage, ProgConfig.PODCAST_EDIT_DIALOG_SIZE,
                "Podcast bearbeiten", true, true);

        this.progData = progData;
        this.addNewPodcast = addNewPodcast;

        this.podcast = podcast;

        init(true);
    }

    private boolean checkChanges() {
        if (addNewPodcast && progData.podcastList.podcastExistsAlready(podcast)) {
            // dann gibts den Podcast schon
            PAlert.showErrorAlert(getStage(), "Fehler", "Podcast anlegen",
                    "Ein Podcast mit den Einstellungen existiert bereits");
            return false;
        }

        if (podcast.getUrl().isEmpty()) {
            // dann ist die RSS-URL leer
            PAlert.showErrorAlert(getStage(), "Fehler", "Podcast anlegen",
                    "Der Podcast enthält keine RSS-URL!");
            return false;
        }
        ok = true;
        return true;
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public void make() {
        getvBoxCont().getChildren().add(gridPane);
        addOkCancelButtons(btnOk, btnCancel);

        btnOk.disableProperty().bind(podcast.nameProperty().isEmpty().or(okProp.not()));
        btnOk.setOnAction(a -> {
            if (checkChanges()) {
                close();
            }
        });
        btnCancel.setOnAction(a -> close());
        cboGenre.setEditable(true);
        cboGenre.getItems().addAll(progData.podcastList.getGenreList());

        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setMinWidth(Control.USE_PREF_SIZE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        int grid = 0;
        for (int i = 0; i < PodcastNames.MAX_ELEM; ++i) {
            if (i == PodcastNames.PODCAST_ID_NO || i == PodcastNames.PODCAST_NUMBER_NO ||
                    i == PodcastNames.PODCAST_DATE_NO) {
                continue;
            }

            lbl[i] = new Label(PodcastNames.COLUMN_NAMES[i] + ":");
            txt[i] = new TextField();
            gridPane.add(lbl[i], 0, grid);

            switch (i) {
                case PodcastNames.PODCAST_NO_NO:
                    txt[i].setEditable(false);
                    txt[i].setDisable(true);
                    txt[i].setText(podcast.getNo() + "");
                    gridPane.add(txt[i], 1, grid);
                    break;
                case PodcastNames.PODCAST_DESCRIPTION_NO:
                    textArea.setWrapText(true);
                    textArea.setPrefRowCount(4);
                    textArea.setMinHeight(60);
                    textArea.setPrefColumnCount(1);
                    textArea.textProperty().bindBidirectional(podcast.properties[i]);
                    gridPane.add(textArea, 1, grid);
                    break;
                case PodcastNames.PODCAST_GENRE_NO:
                    cboGenre.valueProperty().bindBidirectional(podcast.properties[i]);
                    cboGenre.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setHgrow(cboGenre, Priority.ALWAYS);
                    gridPane.add(cboGenre, 1, grid);
                    break;
                case PodcastNames.PODCAST_NAME_NO:
                case PodcastNames.PODCAST_URL_NO:
                    addCheck(txt[i]);
                    txt[i].textProperty().bindBidirectional(podcast.properties[i]);
                    gridPane.add(txt[i], 1, grid);
                    break;
                default:
                    txt[i].textProperty().bindBidirectional(podcast.properties[i]);
                    gridPane.add(txt[i], 1, grid);
                    break;
            }
            grid++;
        }
        btnOk.requestFocus();
    }

    private void addCheck(TextField txtF) {
        txtF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtF.getText().isEmpty()) {
                txtF.setStyle(ProgColorList.PODCAST_NAME_ERROR.getCssBackground());
            } else {
                txtF.setStyle("");
            }
        });
        if (txtF.getText().isEmpty()) {
            txtF.setStyle(ProgColorList.PODCAST_NAME_ERROR.getCssBackground());
        } else {
            txtF.setStyle("");
        }
    }
}
