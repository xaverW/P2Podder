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

import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.P2PodderShortCuts;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.podcast.PodcastFactory;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class PodcastMenu {
    final private VBox vBox;
    final private ProgData progData;

    BooleanProperty boolFilterOn = ProgConfig.PODCAST_GUI_FILTER_ON;
    BooleanProperty boolInfoOn = ProgConfig.PODCAST_GUI_INFO_ON;

    public PodcastMenu(VBox vBox) {
        this.vBox = vBox;
        progData = ProgData.getInstance();
    }


    public void init() {
        vBox.getChildren().clear();
        initPodcastMenu();
        initButton();
    }

    private void initButton() {
        VBox vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(0);
        vBoxSpace.setMinHeight(0);
        vBox.getChildren().add(vBoxSpace);

        final ToolBarButton btUpdate = new ToolBarButton(vBox,
                "Alle Podcasts aktualisieren", "Alle Podcasts aktualisieren", ProgIcons.Icons.ICON_TOOLBAR_PODCAST_UPDATE.getImageView());
        final ToolBarButton btnAdd = new ToolBarButton(vBox,
                "Einen neuen Podcast anlegen", "Einen neuen Podcast anlegen", ProgIcons.Icons.ICON_TOOLBAR_PODCAST_ADD.getImageView());
        final ToolBarButton btnDel = new ToolBarButton(vBox,
                "Markierten Podcast löschen", "Markierten Podcast löschen", ProgIcons.Icons.ICON_TOOLBAR_PODCAST_DEL.getImageView());
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Info-Dialog anzeigen", "Info-Dialog anzeigen", ProgIcons.Icons.ICON_TOOLBAR_INFO.getImageView());

        vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(10);
        vBoxSpace.setMinHeight(10);
        vBox.getChildren().add(vBoxSpace);

        btUpdate.setOnAction(a -> progData.worker.updatePodcast(true));
        btnAdd.setOnAction(a -> PodcastFactory.addNewPodcast());
        btnDel.setOnAction(a -> progData.podcastGui.getPodcastGuiController().delPodcast());
        btInfo.setOnAction(a -> progData.episodeInfoDialogController.toggleShowInfo());
    }

    private void initPodcastMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Podcastmenü anzeigen"));
        mb.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miUpdate = new MenuItem("markierte Podcasts aktualisieren");
        miUpdate.setOnAction(a -> progData.worker.updatePodcast(false));
        PShortcutWorker.addShortCut(miUpdate, P2PodderShortCuts.SHORTCUT_UPDATE_PODCAST);

        final MenuItem miUpdateAll = new MenuItem("alle Podcasts aktualisieren");
        miUpdateAll.setOnAction(a -> progData.worker.updatePodcast(true));

        final MenuItem miDel = new MenuItem("markierte Podcasts löschen");
        miDel.setOnAction(a -> progData.podcastGui.getPodcastGuiController().delSelPodcast(false));

        final MenuItem miDelAll = new MenuItem("alle Podcasts löschen");
        miDelAll.setOnAction(a -> progData.podcastGui.getPodcastGuiController().delSelPodcast(true));

        final MenuItem miSetActive = new MenuItem("markierte Podcasts einschalten");
        miSetActive.setOnAction(a -> progData.worker.setPodcastActive(true));
        final MenuItem miSetOffActive = new MenuItem("markierte Podcasts ausschalten");
        miSetOffActive.setOnAction(a -> progData.worker.setPodcastActive(false));

        mb.getItems().addAll(miUpdate, miUpdateAll, miDel, miDelAll, miSetActive, miSetOffActive);

        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(boolFilterOn);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(boolInfoOn);

        mb.getItems().add(new SeparatorMenuItem());
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
