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

package de.p2tools.p2podder.tools.update;

import de.p2tools.p2lib.checkforactinfos.FoundAll;
import de.p2tools.p2lib.checkforactinfos.FoundSearchData;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import javafx.application.Platform;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class SearchProgramUpdate {

    private static final String TITLE_TEXT_PROGRAM_VERSION_IS_UP_TO_DATE = "Programmversion ist aktuell";
    private static final String TITLE_TEXT_PROGRAMMUPDATE_EXISTS = "Ein Programmupdate ist verfügbar";
    private final ProgData progData;
    private final Stage stage;
    private String title = "";

    public SearchProgramUpdate(ProgData progData) {
        this.progData = progData;
        this.stage = progData.primaryStage;
    }

    public SearchProgramUpdate(ProgData progData, Stage stage) {
        this.progData = progData;
        this.stage = stage;
    }

    /**
     * @return
     */
    public void searchNewProgramVersion(boolean showAllways) {
        final String SEARCH_URL;
        final String SEARCH_URL_DOWNLOAD;
        if (ProgData.debug) {
            showAllways = true;
            SEARCH_URL = "http://localhost:1313/";
            SEARCH_URL_DOWNLOAD = "http://localhost:1313/download/";
        } else {
            SEARCH_URL = "https://www.p2tools.de";
            SEARCH_URL_DOWNLOAD = "https://www.p2tools.de/download/";
        }

//        final PDate pd = new PDate(ProgConfig.SYSTEM_PROG_BUILD_DATE.get());
        final P2Date pd = new P2Date(P2ToolsFactory.getCompileDate());
        String buildDate = pd.get_yyyy_MM_dd();

        FoundSearchData foundSearchData = new FoundSearchData(
                stage,
                SEARCH_URL,
                SEARCH_URL_DOWNLOAD,

                ProgConfig.SYSTEM_UPDATE_SEARCH_ACT,
                ProgConfig.SYSTEM_UPDATE_SEARCH_BETA,
                ProgConfig.SYSTEM_UPDATE_SEARCH_DAILY,

                ProgConfig.SYSTEM_UPDATE_LAST_INFO,
                ProgConfig.SYSTEM_UPDATE_LAST_ACT,
                ProgConfig.SYSTEM_UPDATE_LAST_BETA,
                ProgConfig.SYSTEM_UPDATE_LAST_DAILY,

                ProgConst.URL_WEBSITE,
                ProgConst.URL_WEBSITE_DOWNLOAD,
                ProgConst.PROGRAM_NAME,
                P2ToolsFactory.getProgVersion(),
                P2ToolsFactory.getBuild(),
                buildDate,
                ProgConfig.SYSTEM_DOWNLOAD_DIR_NEW_VERSION,
                showAllways
        );

        new Thread(() -> {
            FoundAll.foundAll(foundSearchData);
            setTitleInfo(foundSearchData.foundNewVersionProperty().getValue());
        }).start();
    }

    private void setTitleInfo(boolean newVersion) {
        title = progData.primaryStage.getTitle();
        if (newVersion) {
            Platform.runLater(this::setUpdateTitle);
        } else {
            Platform.runLater(this::setNoUpdateTitle);
        }
        try {
            sleep(10_000);
        } catch (Exception ignore) {
        }
        Platform.runLater(this::setOrgTitle);
    }

    private void setUpdateTitle() {
        progData.primaryStage.setTitle(TITLE_TEXT_PROGRAMMUPDATE_EXISTS);
    }

    private void setNoUpdateTitle() {
        progData.primaryStage.setTitle(TITLE_TEXT_PROGRAM_VERSION_IS_UP_TO_DATE);
    }

    private void setOrgTitle() {
        progData.primaryStage.setTitle(title);
    }
}
