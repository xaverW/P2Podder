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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadFieldNames;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DownloadInfoDialog extends P2DialogExtra {

    private Button btnOk = new Button("_Ok");

    private final GridPane gridPane = new GridPane();
    private DownloadData downloadData;

    public DownloadInfoDialog(ProgData progData, DownloadData downloadData) {
        super(progData.primaryStage, ProgConfig.EPISODE_DIALOG_EDIT_SIZE,
                "Download ändern", true, false, DECO.BORDER_SMALL);

        this.downloadData = downloadData;
        if (this.downloadData == null) {
            // Satz mit x, war wohl nix
            close();
            return;
        }

        init(true);
    }

    public void make() {
        addOkButton(btnOk);
        initCont();
        initButton();
    }

    private void initCont() {
        getVBoxCont().getChildren().add(gridPane);

        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        int row = 0;
        gridAdd(DownloadFieldNames.DOWNLOAD_NO, downloadData.getNo() + "", row);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_EPISODE_TITLE, downloadData.getEpisodeTitle(), row);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_GENRE, downloadData.getGenre(), row);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_PODCAST_NAME, downloadData.getPodcastName(), row);

        ++row;
        TextArea taDescription = new TextArea();
        taDescription.setMinHeight(25);
        GridPane.setVgrow(taDescription, Priority.ALWAYS);
        taDescription.setEditable(false);
        taDescription.setWrapText(true);
        taDescription.setBlendMode(BlendMode.DARKEN);
        taDescription.setText(downloadData.getDescription());
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_DESCRIPTION + ":"), 0, row);
        gridPane.add(taDescription, 1, row, 3, 1);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_SIZE, downloadData.getDownloadSize().toString() + " MB",
                DownloadFieldNames.DOWNLOAD_DURATION, downloadData.getDuration(), row);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME, downloadData.getDestFileName(), row);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_DEST_PATH, downloadData.getDestPath(), row);

        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_URL + ":"), 0, row);
        gridPane.add(new P2Hyperlink(downloadData.getEpisodeUrl()), 1, row, 3, 1);

        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_WEBSITE + ":"), 0, row);
        gridPane.add(new P2Hyperlink(downloadData.getEpisodeWebsite()), 1, row, 3, 1);

        ++row;
        gridAdd(DownloadFieldNames.DOWNLOAD_DATE, P2LDateFactory.toString(downloadData.getPubDate()), row);
    }

    private void gridAdd(String name, String value, int row) {
        gridPane.add(new Label(name + ":"), 0, row);
        gridPane.add(new Label(value), 1, row, 3, 1);
    }

    private void gridAdd(String name1, String value1, String name2, String value2, int row) {
        gridPane.add(new Label(name1 + ":"), 0, row);
        gridPane.add(new Label(value1), 1, row);
        gridPane.add(new Label(name2 + ":"), 2, row);
        gridPane.add(new Label(value2), 3, row);
    }

    private void initButton() {
        btnOk.setOnAction(event -> {
            close();
        });
    }
}
