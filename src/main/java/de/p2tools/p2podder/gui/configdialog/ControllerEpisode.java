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

import de.p2tools.p2lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2podder.controller.config.ProgConfig;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerEpisode extends PAccordionPane {

    private final Stage stage;
    private PaneEpisode paneEpisode;

    public ControllerEpisode(Stage stage) {
        super(ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_POD);
        this.stage = stage;

        init();
    }

    public void close() {
        paneEpisode.close();
        super.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        paneEpisode = new PaneEpisode(stage);
        paneEpisode.makePane(result);
        return result;
    }
}