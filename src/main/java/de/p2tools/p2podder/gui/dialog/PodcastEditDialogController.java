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

import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.data.PColorData;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2TextAreaIgnoreTab;
import de.p2tools.p2lib.tools.date.PLDateFactory;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PodcastEditDialogController extends PDialogExtra {

    // DialogPodcast
    private static final PColorData PODCAST_NAME_ERROR = new PColorData("COLOR_PODCAST_NAME_ERROR",
            Color.rgb(255, 233, 233), Color.rgb(200, 183, 183));

    private final GridPane gridPane = new GridPane();
    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");

    private final Label[] lbl = new Label[PodcastNames.MAX_ELEM];
    private final TextField[] txt = new TextField[PodcastNames.MAX_ELEM];
    private final TextArea textArea = new P2TextAreaIgnoreTab(false, true);
    private final ComboBox<String> cboGenre = new ComboBox<>();

    private boolean addNewPodcast;
    private BooleanProperty okProp = new SimpleBooleanProperty(true);
    private boolean ok = false;

    private final Podcast podcast;
    private ProgData progData;

    public PodcastEditDialogController(ProgData progData, Podcast podcast, boolean addNewPodcast) {
        //hier wird ein neues Abo angelegt!
        super(progData.primaryStage, ProgConfig.PODCAST_EDIT_DIALOG_SIZE,
                "Podcast bearbeiten", true, true, DECO.BORDER_SMALL);

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
        getVBoxCont().getChildren().add(gridPane);
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

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize());
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        int grid = 0;
        for (int i = 0; i < PodcastNames.MAX_ELEM; ++i) {
            lbl[i] = new Label(PodcastNames.COLUMN_NAMES[i] + ":");
            txt[i] = new TextField();

            switch (i) {
                case PodcastNames.PODCAST_ID_NO:
                    break;
                case PodcastNames.PODCAST_NO_NO:
                    gridPane.add(lbl[i], 0, grid);
                    gridPane.add(new Label(podcast.getNo() + ""), 1, grid++);
                    break;
                case PodcastNames.PODCAST_DATE_NO:
                    gridPane.add(lbl[i], 0, grid);
                    gridPane.add(new Label(PLDateFactory.toString(podcast.getDate())), 1, grid++);
                    break;
                case PodcastNames.PODCAST_WEBSITE_NO:
                    gridPane.add(lbl[i], 0, grid);
                    txt[i].textProperty().bindBidirectional(podcast.websiteProperty());
                    gridPane.add(txt[i], 1, grid++);
                    break;
                case PodcastNames.PODCAST_AMOUNT_EPISODES_NO:
                    gridPane.add(lbl[i], 0, grid);
                    gridPane.add(new Label(podcast.getAmountEpisodes() + ""), 1, grid++);
                    break;
                case PodcastNames.PODCAST_DESCRIPTION_NO:
                    gridPane.add(lbl[i], 0, grid);
                    textArea.setWrapText(true);
                    textArea.setPrefRowCount(4);
                    textArea.setMinHeight(60);
                    textArea.setPrefColumnCount(1);
                    textArea.textProperty().bindBidirectional(podcast.descriptionProperty());
                    gridPane.add(textArea, 1, grid++);
                    GridPane.setVgrow(textArea, Priority.ALWAYS);
                    break;
                case PodcastNames.PODCAST_GENRE_NO:
                    gridPane.add(lbl[i], 0, grid);
                    cboGenre.valueProperty().bindBidirectional(podcast.genreProperty());
                    cboGenre.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setHgrow(cboGenre, Priority.ALWAYS);
                    gridPane.add(cboGenre, 1, grid++);
                    break;
                case PodcastNames.PODCAST_NAME_NO:
                    gridPane.add(lbl[i], 0, grid);
                    addCheck(txt[i]);
                    txt[i].textProperty().bindBidirectional(podcast.nameProperty());
                    gridPane.add(txt[i], 1, grid++);
                    break;
                case PodcastNames.PODCAST_URL_NO:
                    gridPane.add(lbl[i], 0, grid);
                    addCheck(txt[i]);
                    txt[i].textProperty().bindBidirectional(podcast.urlProperty());
                    gridPane.add(txt[i], 1, grid++);
                    break;
                case PodcastNames.PODCAST_ACTIVE_NO:
                    gridPane.add(lbl[i], 0, grid);
                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().bindBidirectional(podcast.activeProperty());
                    gridPane.add(checkBox, 1, grid++);
                    break;
                default:
                    break;
            }
        }
        btnOk.requestFocus();
    }

    private void addCheck(TextField txtF) {
        txtF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtF.getText().isEmpty()) {
                txtF.setStyle(PODCAST_NAME_ERROR.getCssBackground());
            } else {
                txtF.setStyle("");
            }
        });
        if (txtF.getText().isEmpty()) {
            txtF.setStyle(PODCAST_NAME_ERROR.getCssBackground());
        } else {
            txtF.setStyle("");
        }
    }
}
