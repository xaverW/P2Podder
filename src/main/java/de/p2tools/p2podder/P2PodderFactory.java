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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;

public class P2PodderFactory {
    private P2PodderFactory() {
    }

    public static void changeGui() {
        if (ProgConfig.SYSTEM_SMALL_PODDER.getValue() && ProgData.getInstance().smallGuiPack != null) {
            ProgData.getInstance().smallGuiPack.close();
        } else {
            selPanelSmallPodder();
        }
    }

    public static void selPanelSmallPodder() {
        ProgData.EPISODE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.PODCAST_TAB_ON.setValue(Boolean.FALSE);
        ProgData.DOWNLOAD_TAB_ON.setValue(Boolean.FALSE);

        ProgConfig.SYSTEM_SMALL_PODDER.set(true);
    }

    public static void centerGui() {
        ProgData.getInstance().primaryStage.centerOnScreen();
    }

    public static void minimizeGui() {
        ProgData.getInstance().primaryStage.setIconified(true);
        P2DialogExtra.getDialogList().forEach(p2Dialog -> p2Dialog.getStage().setIconified(true));
    }
}
