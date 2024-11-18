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

package de.p2tools.p2podder;

import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.DownloadGui;
import de.p2tools.p2podder.gui.EpisodeGui;
import de.p2tools.p2podder.gui.PodcastBarController;
import de.p2tools.p2podder.gui.PodcastGui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2PodderController extends StackPane {
    private final Button btnSmallGui = new Button("");
    private final Button btnEpisodes = new Button("Episoden");
    private final Button btnPodcasts = new Button("Podcasts");
    private final Button btnDownloads = new Button("Downloads");

    private final BorderPane borderPane = new BorderPane();
    private final StackPane stackPaneCont = new StackPane();

    private final P2MaskerPane maskerPane = new P2MaskerPane();
    private PodcastBarController podcastBarController;

    private Pane paneEpisodeGui;
    private Pane panePodcastGui;
    private Pane paneDownloadGui;

    private final ProgData progData;

    private final PodcastGui podcastGui = new PodcastGui();
    private final DownloadGui downloadGui = new DownloadGui();
    private final EpisodeGui episodeGui = new EpisodeGui();

    public P2PodderController() {
        progData = ProgData.getInstance();
        init();
    }

    private void init() {
        try {
            // Toolbar
            HBox hBoxTop = new HBox();
            hBoxTop.setPadding(new Insets(10, 10, 10, 10));
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);

            TilePane tilePaneStationFavourite = new TilePane();
            tilePaneStationFavourite.setHgap(25);
            tilePaneStationFavourite.setAlignment(Pos.CENTER);
            tilePaneStationFavourite.getChildren().addAll(btnEpisodes, btnPodcasts, btnDownloads);
            HBox.setHgrow(tilePaneStationFavourite, Priority.ALWAYS);
            hBoxTop.getChildren().addAll(btnSmallGui, tilePaneStationFavourite, new ProgMenu());

            // Center
            paneEpisodeGui = episodeGui.pack();
            panePodcastGui = podcastGui.pack();
            paneDownloadGui = downloadGui.pack();
            stackPaneCont.getChildren().addAll(paneEpisodeGui, panePodcastGui, paneDownloadGui);

            // Statusbar
            podcastBarController = new PodcastBarController(progData);

            // Gui zusammenbauen
            borderPane.setTop(hBoxTop);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(podcastBarController);
            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, maskerPane);

            initMaskerPane();
            initButton();
            initPanel();
        } catch (Exception ex) {
            P2Log.errorLog(597841023, ex);
        }
    }

    public void initPanel() {
        switch (ProgConfig.SYSTEM_LAST_TAB.get()) {
            case ProgConst.LAST_TAB_STATION_EPISODE:
            default:
                initPanelEpisode();
                break;
            case ProgConst.LAST_TAB_STATION_PODCAST:
                initPanelPodcast();
                break;
            case ProgConst.LAST_TAB_STATION_DOWNLOAD:
                initPanelDownload();
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        progData.maskerPane = maskerPane;
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();
        Button btnStop = maskerPane.getButton();
        maskerPane.setButtonText("");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());
    }

    private void initButton() {
        btnSmallGui.setTooltip(new Tooltip("kleine Übersicht der Episoden anzeigen"));
        btnSmallGui.setOnAction(e -> P2PodderFactory.selPanelSmallPodder());
        btnSmallGui.setMaxWidth(Double.MAX_VALUE);
        btnSmallGui.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnSmallGui.setGraphic(ProgIcons.ICON_TOOLBAR_SMALL_PODDER_24.getImageView());

        btnEpisodes.setTooltip(new Tooltip("Episoden anzeigen"));
        btnEpisodes.setOnAction(e -> selPanelEpisode());
        btnEpisodes.setMaxWidth(Double.MAX_VALUE);

        btnPodcasts.setTooltip(new Tooltip("Podcasts anzeigen"));
        btnPodcasts.setOnAction(e -> selPanelPodcast());
        btnPodcasts.setMaxWidth(Double.MAX_VALUE);

        btnDownloads.setTooltip(new Tooltip("Downloads anzeigen"));
        btnDownloads.setOnAction(e -> selPanelDownload());
        btnDownloads.setMaxWidth(Double.MAX_VALUE);

        btnEpisodes.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneEpisodeGui)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.EPISODE__INFO_IS_SHOWING.setValue(!ProgConfig.EPISODE__INFO_IS_SHOWING.get());
            }
        });
        btnPodcasts.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(panePodcastGui)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.PODCAST__INFO_IS_SHOWING.setValue(!ProgConfig.PODCAST__INFO_IS_SHOWING.get());
            }
        });
        btnDownloads.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneDownloadGui)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.DOWNLOAD__INFO_IS_SHOWING.setValue(!ProgConfig.DOWNLOAD__INFO_IS_SHOWING.get());
            }
        });
    }

    private void selPanelEpisode() {
        ProgConfig.SYSTEM_LAST_TAB.set(ProgConst.LAST_TAB_STATION_EPISODE);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneEpisodeGui)) {
            // dann ist der 2. Klick
            if (!ProgConfig.EPISODE__FILTER_IS_RIP.get()) {
                ProgConfig.EPISODE__FILTER_IS_SHOWING.setValue(!ProgConfig.EPISODE__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelEpisode();
    }

    private void initPanelEpisode() {
        ProgData.PODCAST_TAB_ON.setValue(Boolean.FALSE);
        ProgData.EPISODE_TAB_ON.setValue(Boolean.TRUE);
        ProgData.DOWNLOAD_TAB_ON.setValue(Boolean.FALSE);
        setButtonStyle(btnEpisodes);
        paneEpisodeGui.toFront();
        progData.episodeGui.getEpisodeGuiController().isShown();
        podcastBarController.setStatusbarIndex(PodcastBarController.StatusbarIndex.EPISODE);
    }

    private void selPanelPodcast() {
        ProgConfig.SYSTEM_LAST_TAB.set(ProgConst.LAST_TAB_STATION_PODCAST);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(panePodcastGui)) {
            // dann ist der 2. Klick
            if (!ProgConfig.PODCAST__FILTER_IS_RIP.get()) {
                ProgConfig.PODCAST__FILTER_IS_SHOWING.setValue(!ProgConfig.PODCAST__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelPodcast();
    }

    private void initPanelPodcast() {
        ProgData.PODCAST_TAB_ON.setValue(Boolean.TRUE);
        ProgData.EPISODE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.DOWNLOAD_TAB_ON.setValue(Boolean.FALSE);
        setButtonStyle(btnPodcasts);
        panePodcastGui.toFront();
        progData.podcastGui.getPodcastGuiController().isShown();
        podcastBarController.setStatusbarIndex(PodcastBarController.StatusbarIndex.PODCAST);
    }

    private void selPanelDownload() {
        ProgConfig.SYSTEM_LAST_TAB.set(ProgConst.LAST_TAB_STATION_DOWNLOAD);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneDownloadGui)) {
            // dann ist der 2. Klick
            if (!ProgConfig.DOWNLOAD__FILTER_IS_RIP.get()) {
                ProgConfig.DOWNLOAD__FILTER_IS_SHOWING.setValue(!ProgConfig.DOWNLOAD__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelDownload();
    }

    private void initPanelDownload() {
        ProgData.PODCAST_TAB_ON.setValue(Boolean.FALSE);
        ProgData.EPISODE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.DOWNLOAD_TAB_ON.setValue(Boolean.TRUE);
        setButtonStyle(btnDownloads);
        paneDownloadGui.toFront();
        progData.downloadGui.getDownloadGuiController().isShown();
        podcastBarController.setStatusbarIndex(PodcastBarController.StatusbarIndex.DOWNLOAD);
    }


    private void setButtonStyle(Button btnSel) {
        btnEpisodes.getStyleClass().clear();
        btnPodcasts.getStyleClass().clear();
        btnDownloads.getStyleClass().clear();

        if (btnSel.equals(btnEpisodes)) {
            btnEpisodes.getStyleClass().add("btnTabTop-sel");
        } else {
            btnEpisodes.getStyleClass().add("btnTabTop");
        }
        if (btnSel.equals(btnPodcasts)) {
            btnPodcasts.getStyleClass().add("btnTabTop-sel");
        } else {
            btnPodcasts.getStyleClass().add("btnTabTop");
        }
        if (btnSel.equals(btnDownloads)) {
            btnDownloads.getStyleClass().add("btnTabTop-sel");
        } else {
            btnDownloads.getStyleClass().add("btnTabTop");
        }
    }
}
