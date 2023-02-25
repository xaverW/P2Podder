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

import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PHyperlink;
import de.p2tools.p2lib.guitools.pclosepane.PClosePaneH;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PodcastGuiInfoController extends PClosePaneH {
    private final GridPane gridPane = new GridPane();
    private final Label lblTitle = new Label("Titel: ");
    private final Label title = new Label("");
    private final Label lblWebsite = new Label("Website: ");
    private final Label lblUrl = new Label("Podcast-URL: ");
    private final PHyperlink hyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
    private final PHyperlink hyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

    private Podcast podcast = null;

    public PodcastGuiInfoController() {
        super(ProgConfig.PODCAST_GUI_INFO_ON, true);
        initInfo();
    }

    public void initInfo() {
        getVBoxAll().getChildren().addAll(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        title.setFont(Font.font(null, FontWeight.BOLD, -1));
        lblWebsite.setMinWidth(Region.USE_PREF_SIZE);
        lblUrl.setMinWidth(Region.USE_PREF_SIZE);

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(lblTitle, 0, row);
        gridPane.add(title, 1, row);

        gridPane.add(lblWebsite, 0, ++row);
        gridPane.add(hyperlinkWebsite, 1, row);

        gridPane.add(lblUrl, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);
    }

    public void setStation(Podcast podcast) {
        this.podcast = podcast;
        if (podcast == null) {
            title.setText("");
            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            return;
        }

        title.setText(podcast.getName() /*+ "  -  " + podAbo.arr[PodAboXml.STATION_COUNTRY]*/);
        hyperlinkWebsite.setUrl(podcast.getWebsite());
        hyperlinkUrl.setUrl(podcast.getUrl());
    }
}
