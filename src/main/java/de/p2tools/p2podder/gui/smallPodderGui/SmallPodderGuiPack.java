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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.configFile.IoReadWriteStyle;
import de.p2tools.p2Lib.dialogs.dialog.PDialogFactory;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.icon.GetIcon;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.ProgStartFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.ProgIcons;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Path;

public class SmallPodderGuiPack extends StackPane {

    private Scene scene = null;
    private Stage stage = null;
    private VBox vBoxComplete = new VBox(0);
    private final SmallEpisodeGuiController smallEpisodeGuiController;

    private final StringProperty sizeConfiguration;
    private final Stage ownerForCenteringDialog;

    private PMaskerPane maskerPane = new PMaskerPane();
    private double stageWidth = 0;
    private double stageHeight = 0;
    private final ProgData progData;

    public SmallPodderGuiPack(ProgData progData) {
        this.progData = progData;
        this.ownerForCenteringDialog = progData.primaryStage;
        this.sizeConfiguration = ProgConfig.SMALL_PODDER_SIZE;
        progData.smallPodderGuiPack = this;

        smallEpisodeGuiController = new SmallEpisodeGuiController(this);
        progData.smallEpisodeGuiController = smallEpisodeGuiController;
        ProgConfig.SYSTEM_SMALL_PODDER.setValue(true);
        init();
    }

    void init() {
        try {
            createNewScene();
            if (scene == null) {
                PException.throwPException(912012458, "no scene");
            }

            updateCss();
            stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            ProgStartFactory.setTitle(stage);
            GetIcon.addWindowP2Icon(stage);

            vBoxComplete.getChildren().add(smallEpisodeGuiController);
            VBox.setVgrow(smallEpisodeGuiController, Priority.ALWAYS);
            this.getChildren().addAll(vBoxComplete, maskerPane);

            make();
            initMaskerPane();

            if (sizeConfiguration.get().isEmpty()) {
                scene.getWindow().sizeToScene();
            }

            showDialog();
        } catch (final Exception exc) {
            PLog.errorLog(858484821, exc);
        }
    }

    public PMaskerPane getMaskerPane() {
        return maskerPane;
    }

    private void updateCss() {
        P2LibInit.addP2LibCssToScene(scene);

        if (P2LibConst.styleFile != null && !P2LibConst.styleFile.isEmpty() && scene != null) {
            final Path path = Path.of(P2LibConst.styleFile);
            IoReadWriteStyle.readStyle(path, scene);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();
        Button btnStop = maskerPane.getButton();
        maskerPane.setButtonText("");
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP);
    }

    private void createNewScene() {
        int w = PGuiSize.getWidth(sizeConfiguration);
        int h = PGuiSize.getHeight(sizeConfiguration);
        if (w > 0 && h > 0) {
            this.scene = new Scene(this, w, h);
        } else {
            this.scene = new Scene(this, 750, 300);
        }
    }

    public void hide() {
        // close/hide are the same
        close();
    }

    public void close() {
        stage.close();
    }

    public void showDialog() {
        if (stageHeight > 0 && stageWidth > 0) {
            //bei wiederkehrenden Dialogen die pos/size setzen
            stage.setHeight(stageHeight);
            stage.setWidth(stageWidth);
        }

        if (!PGuiSize.setPos(sizeConfiguration, stage)) {
            PDialogFactory.setInFrontOfPrimaryStage(ownerForCenteringDialog, stage);
        }

        stage.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public VBox getvBoxComplete() {
        return vBoxComplete;
    }

    private void make() {
        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                changeGui();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            getSize();
            ProgQuitFactory.quit(getStage(), true);
        });
    }

    public void changeGui() {
        ProgConfig.SYSTEM_SMALL_PODDER.setValue(false);
        progData.smallEpisodeGuiController = null;
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

    protected void getSize() {
        smallEpisodeGuiController.saveTable();
        PGuiSize.getSizeStage(ProgConfig.SMALL_PODDER_SIZE, getStage());
    }
}
