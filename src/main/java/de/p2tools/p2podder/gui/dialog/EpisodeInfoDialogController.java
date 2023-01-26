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
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.tools.date.PLDateFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EpisodeInfoDialogController extends PDialogExtra {

    private final int FREE = 220;
    private final Text[] textTitle = new Text[EpisodeFieldNames.MAX_ELEM];
    private final Label[] lblCont = new Label[EpisodeFieldNames.MAX_ELEM];

    private final Button btnUpDown = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final Button btnOk = new Button("_Ok");
    private final ImageView ivNew = new ImageView();
    private final TextArea taDescription = new TextArea();
    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

    private Episode episode;
    private final ProgData progData;

    public EpisodeInfoDialogController(ProgData progData) {
        super(progData.primaryStage, null,
                "Episoden-Infos", false, false, DECO.SMALL);

        this.progData = progData;
        init(false);
    }

    @Override
    public void showDialog() {
        //damit die Größe im ausgeklappten/eingeklappten Modus getrennt gespeichert wird
        setSizeConfiguration(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL);
        PGuiSize.setSizePos(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                        ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL,
                this.getStage(), null);
        getStage().show();
    }

    public void toggleShowInfo() {
        if (getStage().isShowing()) {
            close();
        } else {
            showEpisodeInfo();
        }
    }

    public void showEpisodeInfo() {
        showDialog();
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
        setEpisode();
    }

    @Override
    public void make() {
        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
        getHboxLeft().getChildren().addAll(btnUpDown, new HBox(), btnPrev, btnNext, new HBox(), btnStart, btnStop);
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        setBtnUpDownToolTip();
        btnUpDown.setOnAction(event -> {
            if (ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get()) {
                PGuiSize.getSizeStage(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO, getStage());
                ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.setValue(!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue());
                makePane();
                PGuiSize.setOnlySize(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL, this.getStage());

            } else {
                PGuiSize.getSizeStage(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL, getStage());
                ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.setValue(!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue());
                makePane();
                PGuiSize.setOnlySize(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO, this.getStage());
            }
            setBtnUpDownToolTip();
        });

        btnPrev.setTooltip(new Tooltip("weniger Informationen zur Episode anzeigen"));
        btnPrev.setGraphic(ProgIcons.Icons.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> {
            progData.episodeGui.getEpisodeGuiController().setPreviousStation();
        });

        btnNext.setTooltip(new Tooltip("weniger Informationen zur Episode anzeigen"));
        btnNext.setGraphic(ProgIcons.Icons.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> {
            progData.episodeGui.getEpisodeGuiController().setNextStation();
        });

        btnStart.setTooltip(new Tooltip("Episode abspielen"));
        btnStart.setGraphic(ProgIcons.Icons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> EpisodeFactory.playEpisode());

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());

        initUrl();
        addStageListener();
        makePane();
    }

    private void addStageListener() {
        this.getStage().widthProperty().addListener((v, o, n) -> {
            addStageSize();
        });
        this.getStage().heightProperty().addListener((v, o, n) -> {
            addStageSize();
        });
        this.getStage().xProperty().addListener((v, o, n) -> {
            addStageSize();
        });
        this.getStage().yProperty().addListener((v, o, n) -> {
            addStageSize();
        });
    }

    private void addStageSize() {
        if (this.getStage().isShowing()) {
            PGuiSize.getSizeStage(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                            ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL,
                    this.getStage());
            PGuiSize.getSizeStage(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                            ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL,
                    this.getStage());
            PGuiSize.getSizeStage(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                            ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL,
                    this.getStage());
            PGuiSize.getSizeStage(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                            ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL,
                    this.getStage());
        }
    }

    private void makePane() {
        final GridPane gridPane = new GridPane();
        getvBoxCont().getChildren().clear();
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            textTitle[i] = new Text(EpisodeFieldNames.COLUMN_NAMES[i] + ":");
            lblCont[i] = new Label("");
            if (i == EpisodeFieldNames.EPISODE_DESCRIPTION_NO) {
                lblCont[i].setWrapText(true);
            } else {
                lblCont[i].setWrapText(false);
            }
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______
        }
        makeGridPane(gridPane);
        setEpisode();
    }

    private void makeGridPane(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            switch (i) {
                case EpisodeFieldNames.EPISODE_NO_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_NO_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_NO_NO], 1, row++);
                    break;

                case EpisodeFieldNames.EPISODE_TITLE_NO:
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_TITLE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_TITLE_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_PODCAST_NAME_NO:
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_PODCAST_NAME_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_PODCAST_NAME_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_GENRE_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_GENRE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_GENRE_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_DURATION_NO:
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_DURATION_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_DURATION_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    taDescription.setMinHeight(25);
                    GridPane.setVgrow(taDescription, Priority.ALWAYS);
                    taDescription.setEditable(false);
                    taDescription.setWrapText(true);
                    taDescription.setBlendMode(BlendMode.DARKEN);

                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_DESCRIPTION_NO], 0, row);
                    gridPane.add(taDescription, 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_FILE_NAME_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_FILE_NAME_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_FILE_NAME_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_FILE_PATH_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_FILE_PATH_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_FILE_PATH_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_URL_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_URL_NO], 0, row);
                    gridPane.add(pHyperlinkUrl, 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_WEBSITE_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_WEBSITE_NO], 0, row);
                    gridPane.add(pHyperlinkWebsite, 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_DATE_NO:
                    if (!ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_DATE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_DATE_NO], 1, row++);
                    break;
            }
        }
    }

    private void setEpisode() {
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            if (episode == null) {
                lblCont[i].setText("");
                ivNew.setImage(null);
                taDescription.setText("");
                pHyperlinkUrl.setUrl("");
                pHyperlinkWebsite.setUrl("");
            } else {
                switch (i) {
                    case EpisodeFieldNames.EPISODE_NO_NO:
                        lblCont[i].setText(episode.getNo() + "");
                        break;
                    case EpisodeFieldNames.EPISODE_TITLE_NO:
                        lblCont[i].setText(episode.getEpisodeTitle());
                        break;
                    case EpisodeFieldNames.EPISODE_PODCAST_NAME_NO:
                        Podcast podcast = progData.podcastList.getPodcastById(episode.getPodcastId());
                        if (podcast != null) {
                            lblCont[i].setText(podcast.getName());
                        }
                        break;
                    case EpisodeFieldNames.EPISODE_GENRE_NO:
                        lblCont[i].setText(episode.getGenre());
                        break;
                    case EpisodeFieldNames.EPISODE_DURATION_NO:
                        lblCont[i].setText(episode.getDuration());
                        break;
                    case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
                        taDescription.setText(episode.getDescription());
                        lblCont[i].setText(episode.getDescription());
                        break;
                    case EpisodeFieldNames.EPISODE_FILE_NAME_NO:
                        lblCont[i].setText(episode.getFileName());
                        break;
                    case EpisodeFieldNames.EPISODE_FILE_PATH_NO:
                        lblCont[i].setText(episode.getFilePath());
                        break;
                    case EpisodeFieldNames.EPISODE_URL_NO:
                        pHyperlinkUrl.setUrl(episode.getEpisodeUrl());
                        break;
                    case EpisodeFieldNames.EPISODE_WEBSITE_NO:
                        pHyperlinkWebsite.setUrl(episode.getEpisodeWebsite());
                        break;
                    case EpisodeFieldNames.EPISODE_DATE_NO:
                        lblCont[i].setText(PLDateFactory.toString(episode.getPubDate()));
                        break;
                }
            }
        }
    }

    private void setBtnUpDownToolTip() {
        btnUpDown.setTooltip(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue() ? new Tooltip("weniger Informationen zur Episode anzeigen") :
                new Tooltip("mehr Informationen zur Episode anzeigen"));
        btnUpDown.setGraphic(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.getValue() ?
                ProgIcons.Icons.ICON_BUTTON_UP.getImageView() : ProgIcons.Icons.ICON_BUTTON_DOWN.getImageView());
    }

    private void initUrl() {
        pHyperlinkUrl.setWrapText(true);
        pHyperlinkUrl.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //------------

        pHyperlinkWebsite.setWrapText(true);
        pHyperlinkWebsite.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //----------
    }
}
