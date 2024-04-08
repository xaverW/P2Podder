/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.gui.podcastadd;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class PodcastAddDialogController extends P2DialogExtra {

    private final ProgData progData;
    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private boolean ok = false;
    private final boolean setAll;

    private final AddPodcastDto addPodcastDto;

    public PodcastAddDialogController(ProgData progData, ArrayList<Podcast> list, boolean addNew) {
        super(progData.primaryStage,
                list.size() > 1 ? ProgConfig.PODCAST_DIALOG_ADD_MORE_SIZE :
                        ProgConfig.PODCAST_DIALOG_ADD_SIZE,
                addNew ? "Podcast anlegen" : "Podcast ändern", true, false, DECO.BORDER_SMALL);

        // neue Podcast anlegen / ändern
        this.progData = progData;
        this.setAll = true;
        this.addPodcastDto = new AddPodcastDto(progData, list, addNew);
        initAll();

        init(true);
    }

    @Override
    public void make() {
        initGui();
        initButton();
        addPodcastDto.updateAct();
    }

    private void initAll() {
        if (setAll) {
        } else {
            addPodcastDto.chkMaxAgeAll.setSelected(true);
        }
    }

    private void initGui() {
        PodcastAddDialogGui podcastAddDialogGui = new PodcastAddDialogGui(addPodcastDto, getVBoxCont());
        podcastAddDialogGui.addCont();
        podcastAddDialogGui.init();
        addOkCancelButtons(btnOk, btnCancel);
    }

    private void initButton() {
        addPodcastDto.btnPrev.setOnAction(event -> {
            addPodcastDto.actFilmIsShown.setValue(addPodcastDto.actFilmIsShown.getValue() - 1);
            addPodcastDto.updateAct();
        });
        addPodcastDto.btnNext.setOnAction(event -> {
            addPodcastDto.actFilmIsShown.setValue(addPodcastDto.actFilmIsShown.getValue() + 1);
            addPodcastDto.updateAct();
        });
        btnOk.setOnAction(event -> {
            if (check()) {
                quit();
            }
        });
        btnCancel.setOnAction(event -> {
            ok = false;
            quit();
        });
    }

    private boolean check() {
        ok = false;
        for (AddPodcastData d : addPodcastDto.addPodcastData) {
            if (d.podcast == null) {
                P2Alert.showErrorAlert("Fehlerhafter Download!", "Fehlerhafter Download!",
                        "Download konnte nicht erstellt werden.");

            } else {
                ok = true;
            }
        }
        return ok;
    }

    private void quit() {
        if (!ok) {
            close();
            return;
        }

        if (addPodcastDto.addNewDownloads) {
            // dann neue Downloads anlegen
            addNewPodcasts();
        } else {
            // oder die bestehenden ändern
            changePodcasts();
        }

        // und jetzt noch die Einstellungen speichern
        ProgQuitFactory.saveAll();
        progData.podcastGui.getPodcastGuiController().tableRefresh();
        close();
    }

    private void addNewPodcasts() {
        List<Podcast> list = new ArrayList<>();
        for (AddPodcastData addPodcastData : addPodcastDto.addPodcastData) {
            final Podcast podcast = addPodcastData.podcast;
            list.add(podcast);
        }

        progData.podcastList.addNewItem(list);
    }

    private void changePodcasts() {
        for (AddPodcastData addPodcastData : addPodcastDto.addPodcastData) {
            addPodcastData.podcastOrg.copyToMe(addPodcastData.podcast);
        }
    }
}
