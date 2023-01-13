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

package de.p2tools.p2podder.gui.smallPodderGui;

import de.p2tools.p2Lib.dialogs.dialog.PDialogOnly;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2podder.controller.config.Events;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Optional;

public class SmallPodderGuiPack extends PDialogOnly {

    private final SmallPodderTop smallPodderTop;
    private final SmallPodderCenter smallPodderCenter;
    private final SmallPodderBottom smallPodderBottom;
    private final ProgData progData;
    private final PListener listener = new PListener(Events.REFRESH_TABLE) {
        public void pingGui(PEvent event) {
            tableRefresh();
        }
    };

    public SmallPodderGuiPack() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_PODDER_SIZE,
                "Podder", false, false, false);

        this.progData = ProgData.getInstance();
        smallPodderTop = new SmallPodderTop(this);
        smallPodderCenter = new SmallPodderCenter(this);
        smallPodderBottom = new SmallPodderBottom(this);

        progData.smallPodderGuiPack = this;
        ProgConfig.SYSTEM_SMALL_PODDER.setValue(true);
        init(true);
    }

    @Override
    public void make() {
        SmallPodderFactory.addBorderListener(getStage());
        getStage().initStyle(StageStyle.TRANSPARENT);
        getVBoxCompleteDialog().getStyleClass().add("smallGui");
        VBox vAll = getVBoxCompleteDialog();
        vAll.setPadding(new Insets(25));
        vAll.setSpacing(10);
        vAll.getChildren().addAll(smallPodderTop, smallPodderCenter, smallPodderBottom);

        VBox.setVgrow(smallPodderCenter, Priority.ALWAYS);
        VBox.setVgrow(super.getVBoxCompleteDialog(), Priority.ALWAYS);

        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                changeGui();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            close();
        });
        progData.pEventHandler.addListener(listener);
    }

    @Override
    public void close() {
        saveMe();
        ProgData.getInstance().pEventHandler.removeListener(listener);
        super.close();
    }

    private void saveMe() {
//        progData.smallEpisodeGuiController.saveTable();
        PGuiSize.getSizeStage(ProgConfig.SMALL_PODDER_SIZE, getStage());
    }

    public void saveTable() {
        smallPodderCenter.saveTable();
    }

    public void tableRefresh() {
        smallPodderCenter.tableRefresh();
    }

    public void changeGui() {
        ProgConfig.SYSTEM_SMALL_PODDER.setValue(false);
        progData.smallPodderGuiPack = null;
        getSize();
        close();

        Platform.runLater(() -> {
                    PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage);
                    progData.primaryStage.setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.show();
                }
        );
    }

    public void playEpisode() {
        smallPodderCenter.playEpisode();
    }

    public ArrayList<Episode> getSelList() {
        return smallPodderCenter.getSelList();
    }

    public Optional<Episode> getSel() {
        return smallPodderCenter.getSel();
    }

    public void setNextEpisode() {
        smallPodderCenter.setNextEpisode();
    }

    public void setPreviousEpisode() {
        smallPodderCenter.setPreviousEpisode();
    }

    public void playRandomStation() {
        smallPodderCenter.playRandomStation();
    }

    protected void getSize() {
        smallPodderCenter.saveTable();
        PGuiSize.getSizeStage(ProgConfig.SMALL_PODDER_SIZE, getStage());
    }
}
