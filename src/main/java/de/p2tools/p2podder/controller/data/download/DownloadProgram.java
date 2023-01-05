/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.controller.data.download;

import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.date.DateFactory;
import de.p2tools.p2Lib.tools.file.PFileUtils;

import java.io.File;
import java.util.Date;

public class DownloadProgram {

    private DownloadProgram() {
    }

    public static void makeProgParameter(DownloadData download, String name, String path) {
        // zieldatei und pfad bauen und eintragen

        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.isEmpty()) {
            path = PSystemUtils.getStandardDownloadPath();
        }
        if (name.isEmpty()) {
            name = getToday_yyyyMMdd() + "_" + download.getGenre() + "-" + download.getEpisodeTitle() + ".mp4";
        }

        // in Win dürfen die Pfade nicht länger als 255 Zeichen haben
        final String[] pathName = {path, name};
        PFileUtils.checkLengthPath(pathName);

        download.setDestFileName(pathName[1]);
        download.setDestPath(pathName[0]);
    }

    private static String getToday_yyyyMMdd() {
        return DateFactory.F_FORMAT_yyyy_MM_dd.format(new Date());
    }
}
