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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.PStringUtils;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneConfig {

    private final ProgData progData;

    private final PToggleSwitch tglSmallButton = new PToggleSwitch("In den Tabellen nur kleine Buttons anzeigen");

    private TextField txtUserAgent;
    private final Stage stage;

    public PaneConfig(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();
    }

    public void close() {
        tglSmallButton.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW);
        txtUserAgent.textProperty().unbindBidirectional(ProgConfig.SYSTEM_USERAGENT);
    }

    public void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);

        tglSmallButton.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_SMALL_BUTTON_TABLE_ROW);
        final Button btnHelpSize = PButton.helpButton(stage, "Nur kleine Button anzeigen",
                HelpText.SMALL_BUTTON);
        GridPane.setHalignment(btnHelpSize, HPos.RIGHT);
        final Button btnHelpUserAgent = PButton.helpButton(stage, "User Agent festlegen",
                HelpText.USER_AGENT);
        GridPane.setHalignment(btnHelpUserAgent, HPos.RIGHT);
        txtUserAgent = new TextField() {

            @Override
            public void replaceText(int start, int end, String text) {
                if (check(text)) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                if (check(text)) {
                    super.replaceSelection(text);
                }
            }

            private boolean check(String text) {
                String str = PStringUtils.convertToASCIIEncoding(text);
                final int size = getText().length() + text.length();

                if (text.isEmpty() || (size < ProgConst.MAX_USER_AGENT_SIZE) && text.equals(str)) {
                    return true;
                }
                return false;
            }
        };
        txtUserAgent.textProperty().bindBidirectional(ProgConfig.SYSTEM_USERAGENT);

        int row = 0;
        gridPane.add(tglSmallButton, 0, row, 2, 1);
        gridPane.add(btnHelpSize, 2, row);

        gridPane.add(new Label(), 0, ++row);
        gridPane.add(new Label("User Agent:"), 0, ++row);
        gridPane.add(txtUserAgent, 1, row);
        gridPane.add(btnHelpUserAgent, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());
    }
}
