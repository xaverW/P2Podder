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

import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.tools.file.PFileUtils;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;

public class DownloadFileDelDialogController extends PDialogExtra {

    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private boolean ok = false;

    private List<File> fileList;
    private final ProgData progData;

    public DownloadFileDelDialogController(ProgData progData, List<File> episodeList) {
        super(progData.primaryStage, ProgConfig.SYSTEM_SIZE_DIALOG_DOWNLOAD_DEL,
                "Datei löschen", true, true, DECO.BORDER_SMALL);

        this.progData = progData;
        this.fileList = episodeList;
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public void make() {
        addOkButton(btnOk);
        addCancelButton(btnCancel);

        btnOk.setOnAction(event -> {
            ok = true;
            close();
        });
        btnCancel.setOnAction(event -> {
            ok = false;
            close();
        });

        makeCont();
    }

    private void makeCont() {
        Label txt = new Label("Sollen die Dateien gelöscht werden?");
        ListView<String> listView = new ListView<>();

        fileList.stream().forEach(file -> {
            final String path = PFileUtils.addsPath(file.getPath(), file.getName());
            listView.getItems().add(path);
        });

        getVBoxCont().getChildren().addAll(txt, listView);
    }
}
