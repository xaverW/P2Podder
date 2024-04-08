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

package de.p2tools.p2podder.gui;

import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.worker.InfoFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;

public class PodcastBarController extends AnchorPane {

    public enum StatusbarIndex {NONE, EPISODE, DOWNLOAD, PODCAST}

    private final StackPane stackPane = new StackPane();
    //Episoden
    private final Label lblLeftEpisode = new Label();
    private final Label lblRightEpisode = new Label();
    //Podcast
    private final Label lblLeftPodcast = new Label();
    private final Label lblRightPodcast = new Label();
    //Download
    private final Label lblLeftDownload = new Label();
    private final Label lblRightDownload = new Label();

    private final Pane nonePane;
    private final Pane episodePane;
    private final Pane podcastPane;
    private final Pane downloadPane;
    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean loadList = false;

    private final ProgData progData;
    private boolean stopTimer = false;

    public PodcastBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);
        nonePane = new HBox();
        episodePane = getHbox(lblLeftEpisode, lblRightEpisode);
        podcastPane = getHbox(lblLeftPodcast, lblRightPodcast);
        downloadPane = getHbox(lblLeftDownload, lblRightDownload);
        make();
    }

    private HBox getHbox(Label lblLeft, Label lblRight) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        lblLeft.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeft, Priority.ALWAYS);

        hBox.getChildren().addAll(lblLeft, lblRight);
        hBox.setStyle("-fx-background-color: -fx-background ;");
        return hBox;
    }


    private void make() {
        stackPane.getChildren().addAll(nonePane, episodePane, podcastPane, downloadPane);
        stackPane.setPadding(new Insets(2, 5, 2, 5));
        nonePane.toFront();
        progData.pEventHandler.addListener(new P2Listener(Events.EREIGNIS_TIMER) {
            @Override
            public void pingGui(P2Event runEvent) {
                try {
                    if (!stopTimer) {
                        setStatusbarIndex(statusbarIndex);
                    }
                } catch (final Exception ex) {
                    P2Log.errorLog(936251087, ex);
                }
            }
        });
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;

        switch (statusbarIndex) {
            case EPISODE:
                episodePane.toFront();
                setInfoEpisode();
                setRightText();
                break;
            case PODCAST:
                podcastPane.toFront();
                setInfoPodcast();
                setRightText();
                break;
            case DOWNLOAD:
                downloadPane.toFront();
                setInfoDownload();
                setRightText();
                break;
            case NONE:
            default:
                nonePane.toFront();
                break;
        }
    }


    private void setInfoEpisode() {
        lblLeftEpisode.setText(InfoFactory.getInfosEpisode());
    }

    private void setInfoPodcast() {
        lblLeftPodcast.setText(InfoFactory.getInfosPodcasts());
    }

    private void setInfoDownload() {
        lblLeftDownload.setText(InfoFactory.getInfosDownloads());
    }

    private void setRightText() {
        // Text rechts: alter der Podcastliste
        String strText = "";

        final int day = getAge();
        if (day == 0) {
            strText += "Podcasts heute geladen";
        } else if (day > 0) {
            strText = "Podcasts geladen: ";
            strText += P2LDateTimeFactory.toStringDate(ProgConfig.META_PODCAST_LIST_DATE.getValue());
            strText += "  ";
            strText += "||  Alter: ";
            String strDay = String.valueOf(day);
            strText += strDay;
            strText += " Tage";
        }

        // Infopanel setzen
        lblRightEpisode.setText(strText);
        lblRightPodcast.setText(strText);
        lblRightDownload.setText(strText);
    }

    private int getAge() {
        int days = -1;
        final LocalDateTime stationDate = ProgConfig.META_PODCAST_LIST_DATE.getValue();// meta.podcastDate.getValue();
        if (stationDate != null && !stationDate.isEqual(LocalDateTime.MIN)) {
            days = (int) DAYS.between(ProgConfig.META_PODCAST_LIST_DATE.getValue(), LocalDateTime.now());
            if (days < 0) {
                days = 0;
            }
        }
        return days;
    }

}
