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

package de.p2tools.p2podder.gui.configdialog;

import de.p2tools.p2lib.dialogs.accordion.P2AccordionPane;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerConfig extends P2AccordionPane {

    private PaneColor paneColor;
    private PaneShortcut paneShortcut;
    private PaneKeySize paneKeySize;
    private PaneTray paneTray;
    private PaneConfig paneConfig;
    private PaneLogFile paneLogFile;
    private PaneProg paneProg;
    private PaneUpdate paneUpdate;

    private final Stage stage;
    private final ProgData progData;

    public ControllerConfig(Stage stage) {
        super(ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_CONFIG);
        this.stage = stage;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        super.close();
        paneConfig.close();
        paneTray.close();
        paneColor.close();
        paneShortcut.close();
        paneKeySize.close();
        paneLogFile.close();
        paneProg.close();
        paneUpdate.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        paneConfig = new PaneConfig(stage);
        paneConfig.makeConfig(result);

        paneTray = new PaneTray(stage);
        paneTray.makeTray(result);

        paneLogFile = new PaneLogFile(stage);
        paneLogFile.makeLogfile(result);

        paneColor = new PaneColor(stage);
        paneColor.make(result);

        paneShortcut = new PaneShortcut(stage);
        paneShortcut.makeShortcut(result);

        paneKeySize = new PaneKeySize(stage, progData);
        paneKeySize.makeStyle(result);

        paneProg = new PaneProg(stage);
        paneProg.makeProg(result);

        paneUpdate = new PaneUpdate(stage);
        paneUpdate.makeUpdate(result);
        return result;
    }
}
