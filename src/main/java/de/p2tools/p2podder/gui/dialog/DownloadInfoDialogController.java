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
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.download.DownloadFieldNames;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DownloadInfoDialogController extends PDialogExtra {

    private Button btnOk = new Button("_Ok");

    private final GridPane gridPane = new GridPane();
    private final VBox vBoxAllEpisode = new VBox();

    private final HBox hBoxTop = new HBox();
    private Download download;
    private final ProgData progData;
    private boolean stopGradeListener = false;

    public DownloadInfoDialogController(ProgData progData, Download dowdownloadloadArrayList) {
        super(progData.primaryStage, ProgConfig.EPISODE_DIALOG_EDIT_SIZE,
                "Download ändern", true, false);

        this.progData = progData;
        this.download = dowdownloadloadArrayList;

        if (download == null) {
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
        getvBoxCont().getChildren().add(gridPane);

        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;

        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_NO + ":"), 0, row);
        gridPane.add(new Label(download.getNo() + ""), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_EPISODE_TITLE + ":"), 0, row);
        gridPane.add(new Label(download.getEpisodeTitle()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_GENRE + ":"), 0, row);
        gridPane.add(new Label(download.getGenre()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_PODCAST_NAME + ":"), 0, row);
        gridPane.add(new Label(download.getPodcastName()), 1, row);
        ++row;
        TextArea taDescription = new TextArea();
        taDescription.setEditable(false);
        taDescription.setText(download.getDescription());
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_DESCRIPTION + ":"), 0, row);
        gridPane.add(taDescription, 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_SIZE + ":"), 0, row);
        gridPane.add(new Label(download.getPdownloadSize().getFileSize() + ""), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME + ":"), 0, row);
        gridPane.add(new Label(download.getDestFileName()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_DEST_PATH + ":"), 0, row);
        gridPane.add(new Label(download.getDestPath()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_URL + ":"), 0, row);
        gridPane.add(new PHyperlink(download.getEpisodeUrl()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_WEBSITE + ":"), 0, row);
        gridPane.add(new PHyperlink(download.getEpisodeWebsite()), 1, row);
        ++row;
        gridPane.add(new Label(DownloadFieldNames.DOWNLOAD_DATE + ":"), 0, row);
        gridPane.add(new Label(download.getPubDate().toString()), 1, row);
        ++row;
    }

    private void initButton() {
        btnOk.setOnAction(event -> {
            close();
        });
    }
}
