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
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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

    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    BooleanProperty showBigProperty = ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG;
    private Episode episode;
    private final ProgData progData;

    public EpisodeInfoDialogController(ProgData progData) {
        super(progData.primaryStage, null,
                "Episoden-Infos", false, false, DECO.SMALL);

        this.progData = progData;
        init(false);
    }

    public void close() {
        final StringProperty sp = ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL;
        PGuiSize.getSizeWindow(sp, getStage());
        super.close();
    }

    public void showDialog() {
        PGuiSize.setPos(ProgConfig.EPISODE_INFO_DIALOG_SHOW_BIG.get() ?
                ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL, getStage());
        getStage().show();
        setSize();
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
            showBigProperty.setValue(!showBigProperty.getValue());
            makeGridPane(true);
            setBtnUpDownToolTip();
        });

        btnPrev.setTooltip(new Tooltip("weniger Informationen zur Episode anzeigen"));
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> {
            progData.episodeGui.getEpisodeGuiController().setPreviousStation();
        });

        btnNext.setTooltip(new Tooltip("weniger Informationen zur Episode anzeigen"));
        btnNext.setGraphic(new ProgIcons().ICON_BUTTON_NEXT);
        btnNext.setOnAction(event -> {
            progData.episodeGui.getEpisodeGuiController().setNextStation();
        });

        btnStart.setTooltip(new Tooltip("Episode abspielen"));
        btnStart.setGraphic(new ProgIcons().ICON_BUTTON_PLAY);
        btnStart.setOnAction(event -> {
            progData.episodeGui.getEpisodeGuiController().playEpisode();
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Episoden stoppen"));
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP_PLAY);
        btnStop.setOnAction(event -> EpisodeFactory.stopAllEpisode());

        initUrl();
        makeGridPane(false);
    }

    private void setBtnUpDownToolTip() {
        btnUpDown.setTooltip(showBigProperty.getValue() ? new Tooltip("weniger Informationen zur Episode anzeigen") :
                new Tooltip("mehr Informationen zur Episode anzeigen"));
        btnUpDown.setGraphic(showBigProperty.getValue() ? new ProgIcons().ICON_BUTTON_UP : new ProgIcons().ICON_BUTTON_DOWN);
    }

    private void setEpisode() {
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            if (episode == null) {
                lblCont[i].setText("");
                ivNew.setImage(null);
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
                    case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
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
//                    case PodAboXml.STATION_NEW:
//                        if (podAbo.isNewStation()) {
//                            ivNew.setImage(new ProgIcons().ICON_DIALOG_EIN_SW);
//                        } else {
//                            ivNew.setImage(null);
//                        }
//                        break;

                    default:
//                        lblCont[i].setText(podAbo.arr[i]);
                }
            }
        }
    }

    private void makeGridPane(boolean setSize) {
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
            lblCont[i].setWrapText(false);
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______
        }

        makeGridPane(gridPane);

        setEpisode();
        if (setSize) {
            setSize();
        }
    }

    private void setSize() {
        if (showBigProperty.getValue()) {
            int w = PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO);
            int h = PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO);
            if (w > 0 && h > 0) {
                this.getStage().getScene().getWindow().setHeight(h);
                this.getStage().getScene().getWindow().setWidth(w);
            }

        } else {
            int w = PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL);
            int h = PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_INFO_SMALL);
            if (w > 0 && h > 0) {
                this.getStage().getScene().getWindow().setHeight(h);
                this.getStage().getScene().getWindow().setWidth(w);
            }
        }
    }

    private void makeGridPane(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < EpisodeFieldNames.MAX_ELEM; ++i) {
            switch (i) {
                case EpisodeFieldNames.EPISODE_NO_NO:
                    if (!showBigProperty.getValue()) break;
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
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_GENRE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_GENRE_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_COUNTRY_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_COUNTRY_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_COUNTRY_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_DESCRIPTION_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_DESCRIPTION_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_DESCRIPTION_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_FILE_NAME_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_FILE_NAME_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_FILE_NAME_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_FILE_PATH_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_FILE_PATH_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_FILE_PATH_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_URL_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_URL_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_URL_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_WEBSITE_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_WEBSITE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_WEBSITE_NO], 1, row++);
                    break;
                case EpisodeFieldNames.EPISODE_DATE_NO:
                    if (!showBigProperty.getValue()) break;
                    gridPane.add(textTitle[EpisodeFieldNames.EPISODE_DATE_NO], 0, row);
                    gridPane.add(lblCont[EpisodeFieldNames.EPISODE_DATE_NO], 1, row++);
                    break;
            }
        }
    }

    private void initUrl() {
        pHyperlinkUrl.setWrapText(true);
        pHyperlinkUrl.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //------------

        pHyperlinkWebsite.setWrapText(true);
        pHyperlinkWebsite.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //----------
    }

    private ContextMenu getMenu(String url) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("kopieren");
        resetTable.setOnAction(a -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
        });
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }
}
