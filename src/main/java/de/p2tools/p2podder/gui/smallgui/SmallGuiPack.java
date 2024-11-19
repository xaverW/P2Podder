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

package de.p2tools.p2podder.gui.smallgui;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.P2DialogOnly;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.guitools.P2SmallGuiFactory;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class SmallGuiPack extends P2DialogOnly {

    private final SmallGuiTop smallGuiTop;
    private final SmallGuiCenter smallGuiCenter;
    private final SmallGuiBottom smallGuiBottom;
    private final ProgData progData;
    private final P2Listener listener = new P2Listener(Events.REFRESH_TABLE) {
        public void pingGui(P2Event event) {
            tableRefresh();
        }
    };

    public SmallGuiPack() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_PODDER_SIZE,
                "Podder", false, false, false);

        this.progData = ProgData.getInstance();
        smallGuiTop = new SmallGuiTop(this);
        smallGuiCenter = new SmallGuiCenter(this);
        smallGuiBottom = new SmallGuiBottom(this);

        progData.smallGuiPack = this;
        init(false);

        if (!ProgConfig.SYSTEM_SMALL_GUI_SHOW_START_HELP.getValue()) {
            new SmallGuiHelpDialogController(ProgData.getInstance().primaryStage);
            ProgConfig.SYSTEM_SMALL_GUI_SHOW_START_HELP.setValue(true);
        }
    }

    @Override
    public void make() {
        P2SmallGuiFactory.addBorderListener(getStage());
        getVBoxCompleteDialog().getStyleClass().add("smallGui");
        VBox vAll = getVBoxCompleteDialog();
        vAll.setPadding(new Insets(25));
        vAll.setSpacing(P2LibConst.DIST_BUTTON);
        vAll.getChildren().addAll(smallGuiTop, smallGuiCenter, smallGuiBottom);
        VBox.setVgrow(smallGuiCenter, Priority.ALWAYS);
        VBox.setVgrow(super.getVBoxCompleteDialog(), Priority.ALWAYS);

        progData.pEventHandler.addListener(listener);

        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            close();
        });
    }

    @Override
    public void close() {
        saveTable();
        P2GuiSize.getSizeStage(ProgConfig.SMALL_PODDER_SIZE, getStage());
        ProgData.getInstance().pEventHandler.removeListener(listener);

        progData.smallGuiPack = null;
        ProgConfig.SYSTEM_SMALL_PODDER.setValue(false);
        super.close();
    }

    private void saveTable() {
        smallGuiCenter.saveTable();
    }

    public void tableRefresh() {
        smallGuiCenter.tableRefresh();
    }

    public void clearFilter() {
        smallGuiTop.clearFilter();
    }

    public void playEpisode() {
        smallGuiCenter.playEpisode();
    }

    public ArrayList<Episode> getSelList() {
        return smallGuiCenter.getSelList();
    }

    public Optional<Episode> getSel() {
        return smallGuiCenter.getSel();
    }

    public void setNextEpisode() {
        smallGuiCenter.setNextEpisode();
    }

    public void setPreviousEpisode() {
        smallGuiCenter.setPreviousEpisode();
    }

    public void playRandomStation() {
        smallGuiCenter.playRandomStation();
    }

    public void setEpisodeInfoBox(Episode episode) {
        smallGuiBottom.setInfoBox(episode);
    }
}
