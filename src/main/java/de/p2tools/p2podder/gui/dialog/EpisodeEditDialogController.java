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

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EpisodeEditDialogController extends PDialogExtra {

    private Button btnOk = new Button("_Ok");
    private Button btnCancel = new Button("_Abbrechen");

    private boolean ok = false;
    private final GridPane gridPane = new GridPane();
    private final Label[] lbl = new Label[EpisodeFieldNames.MAX_ELEM];
    private final Label[] lblCont = new Label[EpisodeFieldNames.MAX_ELEM];
    private final CheckBox[] cbx = new CheckBox[EpisodeFieldNames.MAX_ELEM];
    private final TextField[] txt = new TextField[EpisodeFieldNames.MAX_ELEM];
    private final TextArea taDescription = new TextArea();
    private final Button btnPrev = new Button("<");
    private final Button btnNext = new Button(">");
    private final Label lblSum = new Label("");
    private final VBox vBoxAllEpisode = new VBox();
    private final CheckBox[] cbxGrade = new CheckBox[EpisodeConstants.MAX_EPISODE_GRADE];

    private ArrayList<Episode> episodeArrayList;

    private final HBox hBoxTop = new HBox();
    private Episode episode;
    private int actEpisode = 0;
    private final ProgData progData;
    private boolean stopGradeListener = false;

    public EpisodeEditDialogController(ProgData progData, ArrayList<Episode> episodeArrayList) {
        super(progData.primaryStage, ProgConfig.EPISODE_DIALOG_EDIT_SIZE,
                "Episode ändern", true, false);

        this.progData = progData;
        this.episodeArrayList = episodeArrayList;

        if (episodeArrayList.size() == 0) {
            // Satz mit x, war wohl nix
            ok = false;
            close();
            return;
        }

        episode = episodeArrayList.get(actEpisode).getCopy();
        init(true);
    }


    public void make() {
        taDescription.setEditable(true);

        addOkCancelButtons(btnOk, btnCancel);
        initCont();
        initButton();
        initGridPane();
    }

    public boolean isOk() {
        return ok;
    }

    private void initCont() {
        if (episodeArrayList.size() > 1) {
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);
            hBoxTop.setPadding(new Insets(10));
            hBoxTop.getChildren().addAll(btnPrev, lblSum, btnNext);

            vBoxAllEpisode.getStyleClass().add("downloadDialog");
            vBoxAllEpisode.getChildren().addAll(hBoxTop);
            getvBoxCont().getChildren().add(vBoxAllEpisode);
        }
        getvBoxCont().getChildren().add(gridPane);
    }

    private void initButton() {
        btnPrev.setOnAction(event -> {
            saveAct();
            --actEpisode;
            changeAct(actEpisode);
            setStationNo();
        });
        btnNext.setOnAction(event -> {
            saveAct();
            ++actEpisode;
            changeAct(actEpisode);
            setStationNo();
        });
        setStationNo();

        btnOk.setOnAction(event -> {
            saveAct();
            ok = true;
            close();
        });
        btnCancel.setOnAction(event -> {
            ok = false;
            close();
        });
    }

    private void changeAct(int newPos) {
        Episode fNew = episodeArrayList.get(newPos);
        episode.copyToMe(fNew);
        initGrade();
    }

    private void saveAct() {
        Episode f = episodeArrayList.get(actEpisode);
        f.copyToMe(episode);
    }

    private void setStationNo() {
        final int nr = actEpisode + 1;
        lblSum.setText("Episode " + nr + " von " + episodeArrayList.size() + " Episoden");

        if (actEpisode == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(false);
        } else if (actEpisode == episodeArrayList.size() - 1) {
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
        } else {
            btnPrev.setDisable(false);
            btnNext.setDisable(false);
        }
    }

    private void initGridPane() {
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            lbl[i] = new Label(EpisodeFieldNames.COLUMN_NAMES[i] + ":");
            lbl[i].setPadding(new Insets(2, 0, 2, 0));
            lblCont[i] = new Label("");

            txt[i] = new TextField("");
            txt[i].setEditable(false);
            txt[i].setMaxWidth(Double.MAX_VALUE);
            txt[i].setPrefWidth(Control.USE_COMPUTED_SIZE);

            cbx[i] = new CheckBox();
            cbx[i].setDisable(true);
        }
        for (int i = 0; i < EpisodeConstants.MAX_EPISODE_GRADE; ++i) {
            cbxGrade[i] = new CheckBox();
        }
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            row = setGrid(i, row);
        }
    }

    private int setGrid(int i, int row) {
        PHyperlink hyperlink;
        switch (i) {
            case EpisodeFieldNames.EPISODE_NO_NO:
                // bis hier nicht anzeigen
                break;
            case EpisodeFieldNames.EPISODE_GENRE_NO:
                lblCont[i].textProperty().bind(episode.genreProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row, 3, 1);
                ++row;
                break;

            case EpisodeFieldNames.EPISODE_BUTTON1_NO:
            case EpisodeFieldNames.EPISODE_BUTTON2_NO:
                break;

            case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
                lbl[i].setTextFill(Color.BLUE);
                taDescription.textProperty().bindBidirectional(episode.descriptionProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(taDescription, 1, row, 3, 1);
                if (episodeArrayList.size() > 1) {
                    gridPane.add(addAllButton(i), 5, row);
                }
                ++row;
                break;
            case EpisodeFieldNames.EPISODE_URL_NO:
                hyperlink = new PHyperlink(this.getStage(), episode.episodeUrlProperty().getValueSafe(),
                        ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
                hyperlink.setChangeable();
                hyperlink.textProperty().bindBidirectional(episode.episodeUrlProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(hyperlink, 1, row, 3, 1);
                ++row;
                break;
            case EpisodeFieldNames.EPISODE_WEBSITE_NO:
                hyperlink = new PHyperlink(this.getStage(), episode.episodeWebsiteProperty().getValueSafe(),
                        ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
                hyperlink.setChangeable();
                hyperlink.textProperty().bindBidirectional(episode.episodeWebsiteProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(hyperlink, 1, row, 3, 1);
                ++row;
                break;
            case EpisodeFieldNames.EPISODE_DATE_NO:
                lblCont[i].textProperty().bind(episode.pubDateProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
        }

        if (txt[i].isEditable() || !cbx[i].isDisabled()) {
            lbl[i].setTextFill(Color.BLUE);
        }
        return row;
    }

    private void initGrade() {
        stopGradeListener = true;
        for (int i = 0; i < EpisodeConstants.MAX_EPISODE_GRADE; ++i) {
//            cbxGrade[i].setSelected(actFavourite.getGrade() > i);
        }
        stopGradeListener = false;
    }

    private void controlGrade() {
        int g = 0;
        for (int i = 0; i < EpisodeConstants.MAX_EPISODE_GRADE; ++i) {
            if (cbxGrade[i].isSelected()) {
                ++g;
            }
        }
//        actFavourite.setGrade(g);
    }

    private Button addAllButton(int i) {
        Button btn = new Button("alle");
        btn.setOnAction(e -> {
            saveAct();
            episodeArrayList.stream().forEach(f -> {
                switch (i) {
                    case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
                        f.setDescription(episode.getDescription());
                        break;
                }
            });
        });
        return btn;
    }
}
