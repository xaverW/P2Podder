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
package de.p2tools.p2podder;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.configFile.IoReadWriteStyle;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.ProgLoadFactory;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.ProgStartFactory;
import de.p2tools.p2podder.controller.config.*;
import de.p2tools.p2podder.gui.dialog.EpisodeInfoDialogController;
import de.p2tools.p2podder.gui.smallPodderGui.SmallPodderGuiPack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2Podder extends Application {

    private Stage primaryStage;
    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    protected ProgData progData;
    Scene scene = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        boolean firstProgramStart;
        PDuration.counterStart(LOG_TEXT_PROGRAM_START);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initP2lib();
        firstProgramStart = ProgStartFactory.workBeforeGui(progData);
        initRootLayout();
        ProgStartFactory.workAfterGui(progData, firstProgramStart);
        ProgLoadFactory.loadPodcastListProgStart(firstProgramStart);

        PDuration.onlyPing("Gui steht!");
        PDuration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initP2lib() {
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME,
                "", ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(P2LibConst.CSS_GUI);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void initRootLayout() {
        try {
            addThemeCss(); // damit es f??r die 2 schon mal stimmt
            progData.episodeInfoDialogController = new EpisodeInfoDialogController(progData);
            progData.p2PodderController = new P2PodderController();

            scene = new Scene(progData.p2PodderController,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));

            addThemeCss(); // und jetzt noch f??r die neue Scene
            ProgColorList.setColorTheme();

            if (ProgConfig.SYSTEM_STYLE.get()) {
                P2LibInit.setStyleFile(ProgInfos.getStyleFile().toString());
                IoReadWriteStyle.readStyle(ProgInfos.getStyleFile(), scene);
            }

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                addThemeCss();
                //erst css ??ndern, dann
                ProgColorList.setColorTheme();
                ProgConfig.SYSTEM_THEME_CHANGED.setValue(!ProgConfig.SYSTEM_THEME_CHANGED.get());
            });

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuitFactory.quit(primaryStage, true);
            });

            scene.heightProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            scene.widthProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            primaryStage.xProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            primaryStage.yProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));

            //Pos setzen
            if (!PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, primaryStage)) {
                primaryStage.centerOnScreen();
            }

            if (ProgConfig.SYSTEM_SMALL_PODDER.getValue()) {
                //dann gleich mit smallRadio starten
                Platform.runLater(() -> new SmallPodderGuiPack(progData));
            } else {
                primaryStage.show();
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2LibCssToScene(scene);
    }
}
