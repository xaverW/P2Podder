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
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneH;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2podder.controller.ProgIcons;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DownloadGuiInfoController extends P2ClosePaneH {
    private final Label lblTitle_ = new Label("Titel:");
    private final Label lblTitle = new Label("");
    private final Label lblDate = new Label("");
    private final Label lblLength = new Label("");
    private final Label lblSize = new Label("");

    private final P2Hyperlink hyperlinkWebsite = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
    private final P2Hyperlink hyperlinkUrl = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
    private final Label lblDescription = new Label("Beschreibung: ");
    private final TextArea taDescription = new TextArea();

    private DownloadData downloadData = null;

    public DownloadGuiInfoController() {
        super(ProgConfig.DOWNLOAD_GUI_INFO_ON, true);
        initInfo();
    }

    public void initInfo() {
        final GridPane gridPaneRight = new GridPane();
        final GridPane gridPaneLeft = new GridPane();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(gridPaneLeft, gridPaneRight);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.DOWNLOAD_GUI_INFO_DIVIDER);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        getVBoxAll().getChildren().add(splitPane);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));

        taDescription.setEditable(true);
        taDescription.setWrapText(true);
        taDescription.setPrefRowCount(2);

        gridPaneLeft.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPaneLeft.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPaneLeft.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPaneLeft.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPaneLeft.add(lblTitle_, 0, row);
        gridPaneLeft.add(lblTitle, 1, row);
        gridPaneLeft.add(new Label("Website:"), 0, ++row);
        gridPaneLeft.add(hyperlinkWebsite, 1, row);
        gridPaneLeft.add(new Label("Download-URL:"), 0, ++row);
        gridPaneLeft.add(hyperlinkUrl, 1, row);
        gridPaneLeft.add(lblDescription, 0, ++row);
        gridPaneLeft.add(taDescription, 1, row);
        GridPane.setVgrow(taDescription, Priority.ALWAYS);

        gridPaneRight.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPaneRight.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPaneRight.setPadding(new Insets(P2LibConst.PADDING_GRIDPANE));
        gridPaneRight.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        row = 0;
        gridPaneRight.add(new Label("Datum:"), 0, row);
        gridPaneRight.add(lblDate, 1, row);
        gridPaneRight.add(new Label("Dauer:"), 0, ++row);
        gridPaneRight.add(lblLength, 1, row);
        gridPaneRight.add(new Label("Größe:"), 0, ++row);
        gridPaneRight.add(lblSize, 1, row);
    }

    public void setDownload(DownloadData down) {
        if (this.downloadData != null) {
            taDescription.textProperty().unbindBidirectional(this.downloadData.descriptionProperty());
        }

        this.downloadData = down;
        if (downloadData == null) {
            lblTitle.setText("");
            lblDate.setText("");
            lblLength.setText("");
            lblSize.setText("");

            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            taDescription.setText("");
            return;
        }

        lblTitle.setText(downloadData.getEpisodeTitle() + "  -  " + downloadData.getGenre());
        lblDate.setText(P2LDateFactory.toString(downloadData.getPubDate()));
        lblLength.setText(downloadData.getDuration());
        lblSize.setText(downloadData.getDownloadSize().toString());

        hyperlinkWebsite.setUrl(downloadData.getEpisodeWebsite());
        hyperlinkUrl.setUrl(downloadData.getEpisodeUrl());
        taDescription.textProperty().bindBidirectional(downloadData.descriptionProperty());
    }
}
