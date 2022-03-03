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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class EpisodeDelDialogController extends PDialogExtra {

    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private final CheckBox chkDontAsk = new CheckBox("Einstellung merken, nicht mehr fragen");
    private final CheckBox chkDelFile = new CheckBox("Auch die Dateien löschen");
    private boolean ok = false;

    private List<Episode> episodeList;
    private final ProgData progData;

    public EpisodeDelDialogController(ProgData progData, List<Episode> episodeList) {
        super(progData.primaryStage, ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_DEL,
                "Episoden löschen", true, true, DECO.SMALL);

        this.progData = progData;
        this.episodeList = episodeList;
        init(true);
    }

    public void close() {
        PGuiSize.getSizeWindow(ProgConfig.SYSTEM_SIZE_DIALOG_EPISODE_DEL, getStage());
        super.close();
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

        getHboxLeft().getChildren().addAll(chkDontAsk);
        getHBoxOverButtons().getChildren().add(chkDelFile);
        getHBoxOverButtons().setAlignment(Pos.CENTER_RIGHT);
        makeCont();
    }

    private void makeCont() {
        chkDontAsk.setSelected(ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK.not().getValue());
        chkDontAsk.selectedProperty().addListener((observable, oldValue, newValue) ->
                ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK.setValue(!chkDontAsk.isSelected()));
        chkDelFile.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DELETE_EPISODE_FILE);
        HBox.setHgrow(chkDontAsk, Priority.ALWAYS);

        Label txt = new Label("Sollen die Episoden und die Dateien gelöscht werden?");
        ListView<String> listView = new ListView<>();

        episodeList.stream().forEach(episode -> {
            final String path = episode.getEpisodeTitle() + P2LibConst.LINE_SEPARATOR +
                    PFileUtils.addsPath(episode.getFilePath(), episode.getFileName());
            listView.getItems().add(path);
        });

        getvBoxCont().getChildren().addAll(txt, listView);
    }
}
