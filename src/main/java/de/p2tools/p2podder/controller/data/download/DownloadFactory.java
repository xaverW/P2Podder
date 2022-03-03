/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.controller.data.download;

import de.p2tools.p2Lib.guiTools.PSizeTools;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.tools.FileNameUtils;
import javafx.scene.control.Label;
import org.apache.commons.lang3.SystemUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class DownloadFactory {

    public static synchronized void startAllDownloads() {
        List<Download> downloadList = ProgData.getInstance().downloadList;
        downloadList.stream().forEach(d -> {
            ProgData.getInstance().downloadList.startDownloads(downloadList, false);
        });
    }

    public static synchronized void cleanUpList() {
        // fertige Downloads löschen, fehlerhafte zurücksetzen
        boolean found = false;
        Iterator<Download> it = ProgData.getInstance().downloadList.iterator();
        while (it.hasNext()) {
            Download download = it.next();
            if (download.isStateInit() ||
                    download.isStateStoped()) {
                continue;
            }
            if (download.isStateFinished()) {
                // alles was fertig/fehlerhaft ist, kommt beim Putzen weg
                it.remove();
                found = true;
            } else if (download.isStateError()) {
                // fehlerhafte werden zurückgesetzt
                download.resetDownload();
                found = true;
            }
        }

        if (found) {
            ProgData.getInstance().downloadList.setDownloadsChanged();
        }
    }

    public static synchronized long getDownloadsWaiting() {
        return ProgData.getInstance().downloadList.stream().filter(download -> download.isStateStartedWaiting()).count();
    }

    public static synchronized List<Podcast> getPodcastList() {
        PDuration.counterStart("getPodcastList");

        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(10);
        ArrayList<Podcast> arrayList = new ArrayList<>();
        ProgData.getInstance().downloadList.stream().forEach((download) -> {
            String podcastName = download.getPodcastName();
            Podcast podcast = ProgData.getInstance().podcastList.getPodcastById(download.getPodcastId());
            if (podcast != null && !hashSet.contains(podcastName)) {
                hashSet.add(podcastName);
                arrayList.add(podcast);
            }
        });

        PDuration.counterStop("getPodcastList");
        return arrayList;
    }

    public static long countDownload(Podcast podcast) {
        return ProgData.getInstance().downloadList.stream().filter(p -> p.getPodcastId() == podcast.getId()).count();
    }

    /**
     * Calculate free disk space on volume and check if the movies can be safely downloaded.
     */
    public static void calculateAndCheckDiskSpace(Download download, String path, Label lblSizeFree) {
        if (path == null || path.isEmpty()) {
            return;
        }
        try {
            String noSize = "";
            long usableSpace = PFileUtils.getFreeDiskSpace(path);
            String sizeFree = "";
            if (usableSpace == 0) {
                lblSizeFree.setText("");
            } else {
                sizeFree = PSizeTools.humanReadableByteCount(usableSpace, true);
            }

            // jetzt noch prüfen, obs auf die Platte passt
            usableSpace /= 1_000_000;
            if (usableSpace > 0) {
                long size = download.getPdownloadSize().getFileSize();
                size /= 1_000_000;
                if (size > usableSpace) {
                    noSize = " [ nicht genug Speicher: ";

                }
            }

            if (noSize.isEmpty()) {
                lblSizeFree.setText(" [ noch frei: " + sizeFree + " ]");
            } else {
                lblSizeFree.setText(noSize + sizeFree + " ]");
            }


        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getDownloadPath() {
        return ProgConfig.START_DIALOG_DOWNLOAD_PATH.get().isEmpty() ?
                PSystemUtils.getStandardDownloadPath() : ProgConfig.START_DIALOG_DOWNLOAD_PATH.get();
    }

    /**
     * Entferne verbotene Zeichen aus Dateiname.
     *
     * @param name        Dateiname
     * @param isPath
     * @param userReplace
     * @param onlyAscii
     * @return Bereinigte Fassung
     */
    public static String replaceEmptyFileName(String name, boolean isPath, boolean userReplace, boolean onlyAscii) {
        String ret = name;
        boolean isWindowsPath = false;
        if (SystemUtils.IS_OS_WINDOWS && isPath && ret.length() > 1 && ret.charAt(1) == ':') {
            // damit auch "d:" und nicht nur "d:\" als Pfad geht
            isWindowsPath = true;
            ret = ret.replaceFirst(":", ""); // muss zum Schluss wieder rein, kann aber so nicht ersetzt werden
        }

        // zuerst die Ersetzungstabelle mit den Wünschen des Users
//        if (userReplace) {
//            ret = ProgData.getInstance().replaceList.replace(ret, isPath);
//        }

        // und wenn gewünscht: "NUR Ascii-Zeichen"
        if (onlyAscii) {
            ret = FileNameUtils.convertToASCIIEncoding(ret, isPath);
        } else {
            ret = FileNameUtils.convertToNativeEncoding(ret, isPath);
        }

        if (isWindowsPath) {
            // c: wieder herstellen
            if (ret.length() == 1) {
                ret = ret + ":";
            } else if (ret.length() > 1) {
                ret = ret.charAt(0) + ":" + ret.substring(1);
            }
        }
        return ret;
    }


}
