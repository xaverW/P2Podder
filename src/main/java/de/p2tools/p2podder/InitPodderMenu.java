/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

import de.p2tools.p2lib.guitools.POpen;
import de.p2tools.p2lib.tools.log.PLogger;
import de.p2tools.p2lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.P2PodderShortCuts;
import de.p2tools.p2podder.controller.data.ProgIconsP2Podder;
import de.p2tools.p2podder.gui.configdialog.ConfigDialogController;
import de.p2tools.p2podder.gui.dialog.AboutDialogController;
import de.p2tools.p2podder.gui.dialog.ResetDialogController;
import de.p2tools.p2podder.tools.update.SearchProgramUpdate;
import javafx.scene.control.*;

public class InitPodderMenu {

    public MenuButton getMenu() {
        MenuButton menuButton = new MenuButton();
        initMenu(menuButton);
        return menuButton;
    }

    private void initMenu(MenuButton menuButton) {
        ProgData progData = ProgData.getInstance();

        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen des Programms");
        miConfig.setOnAction(e -> new ConfigDialogController(progData));

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quit(progData.primaryStage, true));
        PShortcutWorker.addShortCut(miQuit, P2PodderShortCuts.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(progData).showDialog());

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            PLogger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            POpen.openURL(ProgConst.URL_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIconsP2Podder.ICON_BUTTON_FILE_OPEN.getImageView());
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final MenuItem miSearchUpdate = new MenuItem("Gibt's ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));


        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, miSearchUpdate, new SeparatorMenuItem(), miAbout);
        if (ProgData.debug) {
            final MenuItem miSave = new MenuItem("Debug: Alles Speichern");
            miSave.setOnAction(a -> ProgQuitFactory.saveAll());

            mHelp.getItems().addAll(miSave);
        }

        menuButton.setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        menuButton.getStyleClass().addAll("btnFunction", "btnFunc-1");
        menuButton.setGraphic(ProgIconsP2Podder.ICON_TOOLBAR_MENU_TOP.getImageView());
        menuButton.getItems().addAll(miConfig, mHelp,
                new SeparatorMenuItem(), miQuit);
    }
}
