/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.gui.tools.table;

import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

public class TableRowDownload<T extends DownloadData> extends TableRow {

    public TableRowDownload() {
    }

    @Override
    public void updateItem(Object f, boolean empty) {
        super.updateItem(f, empty);

        if (f == null || empty) {
            setStyle("");
            setTooltip(null);

        } else {
            DownloadData download = (DownloadData) f;
            if (download.getDownloadStart() != null && download.getDownloadStart().getStartStatus().isStateError()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(download.getDownloadStart().getStartStatus().getErrorMessage());
                setTooltip(tooltip);
            }

            setStyle("");
            switch (download.getState()) {
                case DownloadConstants.STATE_STARTED_WAITING:
                    if (ProgColorList.DOWNLOAD_WAIT_BG.isUse()) {
                        setStyle(ProgColorList.DOWNLOAD_WAIT_BG.getCssBackground());
                    }
                    break;
                case DownloadConstants.STATE_STARTED_RUN:
                    if (ProgColorList.DOWNLOAD_RUN_BG.isUse()) {
                        setStyle(ProgColorList.DOWNLOAD_RUN_BG.getCssBackground());
                    }
                    break;
                case DownloadConstants.STATE_FINISHED:
                    if (ProgColorList.DOWNLOAD_FINISHED_BG.isUse()) {
                        setStyle(ProgColorList.DOWNLOAD_FINISHED_BG.getCssBackground());
                    }
                    break;
                case DownloadConstants.STATE_ERROR:
                    if (ProgColorList.DOWNLOAD_ERROR_BG.isUse()) {
                        setStyle(ProgColorList.DOWNLOAD_ERROR_BG.getCssBackground());
                    }
                    break;
            }
        }
    }
}
