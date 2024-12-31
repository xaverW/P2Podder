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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PanePodcastInfo extends VBox {
    private final Label lblTitle = new Label("");
    private final Label lblGenre = new Label("");
    private final Label lblSumEpisodes = new Label("");
    private final Label lblDate = new Label("");
    private final CheckBox chkActive = new CheckBox();

    private final P2Hyperlink hyperlinkWebsite = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);
    private final P2Hyperlink hyperlinkUrl = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);

    public PanePodcastInfo() {
        initInfo();
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    public void initInfo() {
        final GridPane gridPaneLeft = new GridPane();
        final GridPane gridPaneRight = new GridPane();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(gridPaneLeft, gridPaneRight);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.PODCAST_GUI_INFO_DIVIDER);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        getChildren().add(splitPane);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));
        chkActive.setDisable(true);

        gridPaneLeft.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPaneLeft.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPaneLeft.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPaneLeft.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPaneLeft.add(new Label("Titel:"), 0, row);
        gridPaneLeft.add(lblTitle, 1, row);
        gridPaneLeft.add(new Label("Website:"), 0, ++row);
        gridPaneLeft.add(hyperlinkWebsite, 1, row);
        gridPaneLeft.add(new Label("Podcast-URL:"), 0, ++row);
        gridPaneLeft.add(hyperlinkUrl, 1, row);

        gridPaneRight.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPaneRight.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPaneRight.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPaneRight.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        row = 0;
        gridPaneRight.add(new Label("Aktiv:"), 0, row);
        gridPaneRight.add(chkActive, 1, row);
        gridPaneRight.add(new Label("Genre:"), 0, ++row);
        gridPaneRight.add(lblGenre, 1, row);
        gridPaneRight.add(new Label("Episoden:"), 0, ++row);
        gridPaneRight.add(lblSumEpisodes, 1, row);
        gridPaneRight.add(new Label("Datum:"), 0, ++row);
        gridPaneRight.add(lblDate, 1, row);
    }

    public void setStation(Podcast podcast) {
        if (podcast == null) {
            lblTitle.setText("");
            lblGenre.setText("");
            lblSumEpisodes.setText("");
            lblDate.setText("");
            chkActive.selectedProperty().unbind();
            chkActive.setSelected(false);

            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            return;
        }

        lblTitle.setText(podcast.getName());
        lblGenre.setText(podcast.getGenre());
        lblSumEpisodes.setText(podcast.getAmountEpisodes() + "");
        lblDate.setText(P2LDateFactory.toString(podcast.getGenDate()));
        chkActive.selectedProperty().bind(podcast.activeProperty());

        hyperlinkWebsite.setUrl(podcast.getWebsite());
        hyperlinkUrl.setUrl(podcast.getUrl());
    }
}
