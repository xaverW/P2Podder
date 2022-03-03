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

import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pClosePane.PClosePaneH;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.ProgIcons;
import de.p2tools.p2podder.controller.data.download.Download;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DownloadGuiInfoController extends PClosePaneH {
    private final GridPane gridPane = new GridPane();
    private final Label lblTitle = new Label("");
    private final Label lblWebsite = new Label("Website: ");
    private final Label lblUrl = new Label("Download-URL: ");
    private final PHyperlink hyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final PHyperlink hyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final Label lblDescription = new Label("Beschreibung: ");
    private final TextArea taDescription = new TextArea();

    private Download download = null;

    public DownloadGuiInfoController() {
        super(ProgConfig.DOWNLOAD_GUI_INFO_ON, true);
        initInfo();
    }

    public void initInfo() {
        getVBoxAll().getChildren().add(gridPane);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));
        lblWebsite.setMinWidth(Region.USE_PREF_SIZE);
        lblUrl.setMinWidth(Region.USE_PREF_SIZE);

        taDescription.setEditable(true);
        taDescription.setWrapText(true);
        taDescription.setPrefRowCount(2);

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(lblTitle, 0, row, 2, 1);

        gridPane.add(lblWebsite, 0, ++row);
        gridPane.add(hyperlinkWebsite, 1, row);

        gridPane.add(lblUrl, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDescription, 0, ++row);
        gridPane.add(taDescription, 1, row);

        GridPane.setVgrow(taDescription, Priority.ALWAYS);
        VBox.setVgrow(gridPane, Priority.ALWAYS);
    }

    public void setDownload(Download down) {
        if (this.download != null) {
            taDescription.textProperty().unbindBidirectional(this.download.descriptionProperty());
        }

        this.download = down;
        if (download == null) {
            lblTitle.setText("");
            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            taDescription.setText("");
            return;
        }

        lblTitle.setText(download.getEpisodeTitle() + "  -  " + download.getGenre());
        hyperlinkWebsite.setUrl(download.getEpisodeWebsite());
        hyperlinkUrl.setUrl(download.getEpisodeUrl());
        taDescription.textProperty().bindBidirectional(download.descriptionProperty());
    }
}
