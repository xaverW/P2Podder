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

import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.ProgSaveFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.P2PodderShortCuts;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.gui.DownloadGui;
import de.p2tools.p2podder.gui.EpisodeGui;
import de.p2tools.p2podder.gui.PodcastBarController;
import de.p2tools.p2podder.gui.PodcastGui;
import de.p2tools.p2podder.gui.configDialog.ConfigDialogController;
import de.p2tools.p2podder.gui.dialog.AboutDialogController;
import de.p2tools.p2podder.gui.dialog.ResetDialogController;
import de.p2tools.p2podder.gui.smallPodderGui.SmallPodderGuiPack;
import de.p2tools.p2podder.tools.update.SearchProgramUpdate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2PodderController extends StackPane {
    private Button btnSmallPodder = new Button("");
    private Button btnEpisodes = new Button("Episoden");
    private Button btnPodcasts = new Button("Podcasts");
    private Button btnDownloads = new Button("Downloads");
    private MenuButton menuButton = new MenuButton("");

    private BorderPane borderPane = new BorderPane();
    private StackPane stackPaneCont = new StackPane();

    private PMaskerPane maskerPane = new PMaskerPane();
    private PodcastBarController podcastBarController;

    private Pane paneEpisodeGui;
    private Pane panePodcastGui;
    private Pane paneDownloadGui;

    private final ProgData progData;

    private PodcastGui podcastGui = new PodcastGui();
    private DownloadGui downloadGui = new DownloadGui();
    private EpisodeGui episodeGui = new EpisodeGui();

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
            hBoxTop.getChildren().addAll(btnSmallPodder, tilePaneStationFavourite, menuButton);

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
            initMenu();
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
        } catch (Exception ex) {
            PLog.errorLog(597841023, ex);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        progData.maskerPane = maskerPane;
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();
        Button btnStop = maskerPane.getButton();
        maskerPane.setButtonText("");
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP.getImageView());
    }

    private void initButton() {
        btnSmallPodder.setTooltip(new Tooltip("kleine Übersicht der Episoden anzeigen"));
        btnSmallPodder.setOnAction(e -> selPanelSmallRadio());
        btnSmallPodder.setMaxWidth(Double.MAX_VALUE);
        btnSmallPodder.getStyleClass().add("btnTab");
        btnSmallPodder.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_SMALL_PODDER_24.getImageView());

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
                ProgConfig.EPISODE_GUI_DIVIDER_ON.setValue(!ProgConfig.EPISODE_GUI_DIVIDER_ON.get());
            }
        });
        btnPodcasts.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(panePodcastGui)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.PODCAST_GUI_INFO_ON.setValue(!ProgConfig.PODCAST_GUI_INFO_ON.get());
            }
        });
        btnDownloads.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneDownloadGui)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.DOWNLOAD_GUI_INFO_ON.setValue(!ProgConfig.DOWNLOAD_GUI_INFO_ON.get());
            }
        });
    }

    private void initMenu() {
        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen des Programms");
        miConfig.setOnAction(e -> ConfigDialogController.getInstanceAndShow());

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quit(progData.primaryStage, true));
        PShortcutWorker.addShortCut(miQuit, P2PodderShortCuts.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(progData).showDialog());

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            PLogger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            POpen.openURL(ProgConst.URL_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final MenuItem miSearchUpdate = new MenuItem("Gibt's ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));


        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, miSearchUpdate, new SeparatorMenuItem(), miAbout);
        if (ProgData.debug) {
            final MenuItem miSave = new MenuItem("Debug: Alles Speichern");
            miSave.setOnAction(a -> ProgSaveFactory.saveAll());

            mHelp.getItems().addAll(miSave);
        }

        menuButton.setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        menuButton.getStyleClass().add("btnFunctionWide");
        menuButton.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_MENU_TOP.getImageView());
        menuButton.getItems().addAll(miConfig, mHelp,
                new SeparatorMenuItem(), miQuit);
    }

    private void selPanelSmallRadio() {
        if (maskerPane.isVisible()) {
            return;
        }

        progData.primaryStage.close();
        new SmallPodderGuiPack(progData);
    }

    private void selPanelEpisode() {
        ProgConfig.SYSTEM_LAST_TAB.set(ProgConst.LAST_TAB_STATION_EPISODE);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneEpisodeGui)) {
            // dann ist der 2. Klick
            episodeGui.closeSplit();
            return;
        }
        initPanelEpisode();
    }

    private void initPanelEpisode() {
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
            podcastGui.closeSplit();
            return;
        }
        initPanelPodcast();
    }

    private void initPanelPodcast() {
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
            downloadGui.closeSplit();
            return;
        }
        initPanelDownload();
    }

    private void initPanelDownload() {
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
