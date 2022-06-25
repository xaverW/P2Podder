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

package de.p2tools.p2podder.gui.configDialog;

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.configDialog.setData.SetPaneController;
import de.p2tools.p2podder.gui.tools.Listener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends PDialogExtra {
    private static ConfigDialogController instance;
    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("_Ok");
    private BooleanProperty blackChanged = new SimpleBooleanProperty(false);

    IntegerProperty propSelectedTab = ProgConfig.SYSTEM_CONFIG_DIALOG_TAB;
    private final ProgData progData;

    ConfigPaneController configPane;
    EpisodePaneController episodePaneController;
    PodPaneController podPane;
    SetPaneController setPane;

    private ConfigDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.CONFIG_DIALOG_SIZE, "Einstellungen",
                true, false, DECO.NONE);

        this.progData = ProgData.getInstance();
        init(false);//!!!!!!!!!!!!!!!!!!!!!
    }

    public synchronized static final ConfigDialogController getInstanceAndShow() {
        if (instance == null) {
            instance = new ConfigDialogController(ProgData.getInstance());
        }

        if (!instance.isShowing()) {
            instance.showDialog();
        }
        instance.getStage().toFront();

        return instance;
    }

    @Override
    public void make() {
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getvBoxCont().getChildren().add(tabPane);
        getvBoxCont().setPadding(new Insets(0));

        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
        initPanel();
    }

    public void close() {
        configPane.close();
        episodePaneController.close();
        podPane.close();
        setPane.close();

        Listener.notify(Listener.EREIGNIS_SETDATA_CHANGED, ConfigDialogController.class.getSimpleName());
        super.close();
    }

    private void initPanel() {
        try {
            configPane = new ConfigPaneController(getStage());
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(configPane);
            tabPane.getTabs().add(tab);

            episodePaneController = new EpisodePaneController(getStage());
            tab = new Tab("Episoden");
            tab.setClosable(false);
            tab.setContent(episodePaneController);
            tabPane.getTabs().add(tab);

            podPane = new PodPaneController(getStage());
            tab = new Tab("Podcast");
            tab.setClosable(false);
            tab.setContent(podPane);
            tabPane.getTabs().add(tab);

            setPane = new SetPaneController(getStage());
            tab = new Tab("Abspielen");
            tab.setClosable(false);
            tab.setContent(setPane);
            tabPane.getTabs().add(tab);

            tabPane.getSelectionModel().select(propSelectedTab.get());
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                // readOnlyBinding!!
                propSelectedTab.setValue(newValue);
            });
        } catch (final Exception ex) {
            PLog.errorLog(784459510, ex);
        }
    }
}
