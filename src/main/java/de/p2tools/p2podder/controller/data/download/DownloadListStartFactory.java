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


import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;

import java.net.URL;

public class DownloadListStartFactory {
    private final ProgData progData;
    private final DownloadList downloadList;

    public DownloadListStartFactory(ProgData progData, DownloadList downloadList) {
        this.progData = progData;
        this.downloadList = downloadList;
    }

    public static DownloadData getRestartDownload() {
        // Versuch einen Fehlgeschlagenen Download zu finden um ihn wieder zu starten
        // die Fehler laufen aber einzeln, vorsichtshalber

        if (!getDown(1)) {
            // dann läuft noch einer
            return null;
        }

        for (final DownloadData download : ProgData.getInstance().downloadList) {
            if (download.isStateInit()) {
                continue;
            }

            if (download.isStateError()
                    && download.getDownloadStart().getRestartCounter() < ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART.getValue()
                    && !maxChannelPlay(download, 1)) {

                int restarted = download.getDownloadStart().getRestartCounter();
                download.restartDownload();
                ProgData.getInstance().downloadList.startDownloads(download);
                // UND jetzt den Restartcounter wieder setzen!!
                download.getDownloadStart().setRestartCounter(++restarted);
                return download;
            }
        }
        return null;
    }

    public static synchronized DownloadData getNextStart() {
        //ersten passenden Download der Liste zurückgeben oder null
        //und versuchen, dass bei mehreren laufenden Downloads ein anderer Download gesucht wird
        DownloadData ret = null;
        if (ProgData.getInstance().downloadList.size() > 0 && getDown(ProgConfig.DOWNLOAD_MAX_DOWNLOADS.getValue())) {
            final DownloadData download = nextStart();
            if (download != null && download.isStateStartedWaiting()) {
                ret = download;
            }
        }
        return ret;
    }

    private static DownloadData nextStart() {
        // Download mit der kleinsten Nr finden der zu Starten ist
        // erster Versuch, Start mit einem anderen Download

        DownloadData tmpDownload = searchNextDownload(1);
        if (tmpDownload != null) {
            // einer wurde gefunden
            return tmpDownload;
        }

        if (ProgConfig.DOWNLOAD_MAX_ONE_PER_SERVER.getValue()) {
            // dann darf nur ein Download pro Server gestartet werden
            return null;
        }

        // zweiter Versuch, Start mit einem passenden Download
        tmpDownload = searchNextDownload(ProgConst.MAX_DOWNLOADS_LADEN);
        return tmpDownload;
    }

    private static DownloadData searchNextDownload(int maxProChannel) {
        DownloadData tmpDownload = null;
        int nr = -1;
        for (DownloadData download : ProgData.getInstance().downloadList) {
            if (download.isStateStartedWaiting() &&
                    !maxChannelPlay(download, maxProChannel) &&
                    (nr == -1 || download.getNo() < nr)) {

                tmpDownload = download;
                nr = tmpDownload.getNo();
            }
        }

        return tmpDownload;
    }

    private static boolean maxChannelPlay(DownloadData d, int max) {
        // true wenn bereits die maxAnzahl läuft
        try {
            int counter = 0;
            final String host = getHost(d);
            if (host.equals("akamaihd.net")) {
                // content delivery network
                return false;
            }

            for (final DownloadData download : ProgData.getInstance().downloadList) {
                if (download.isStateStartedRun() && getHost(download).equalsIgnoreCase(host)) {
                    counter++;
                    if (counter >= max) {
                        return true;
                    }
                }
            }
            return false;
        } catch (final Exception ex) {
            return false;
        }
    }

    private static String getHost(DownloadData download) {
        String host = "";
        try {
            try {
                String uurl = download.getEpisodeUrl();
                // die funktion "getHost()" kann nur das Protokoll "http" ??!??
                if (uurl.startsWith("rtmpt:")) {
                    uurl = uurl.toLowerCase().replace("rtmpt:", "http:");
                }
                if (uurl.startsWith("rtmp:")) {
                    uurl = uurl.toLowerCase().replace("rtmp:", "http:");
                }
                if (uurl.startsWith("mms:")) {
                    uurl = uurl.toLowerCase().replace("mms:", "http:");
                }
                final URL url = new URL(uurl);
                String tmp = url.getHost();
                if (tmp.contains(".")) {
                    host = tmp.substring(tmp.lastIndexOf('.'));
                    tmp = tmp.substring(0, tmp.lastIndexOf('.'));
                    if (tmp.contains(".")) {
                        host = tmp.substring(tmp.lastIndexOf('.') + 1) + host;
                    } else if (tmp.contains("/")) {
                        host = tmp.substring(tmp.lastIndexOf('/') + 1) + host;
                    } else {
                        host = "host";
                    }
                }
            } catch (final Exception ex) {
                // für die Hosts bei denen das nicht klappt
                // Log.systemMeldung("getHost 1: " + s.download.arr[DatenDownload.DOWNLOAD_URL_NR]);
                host = "host";
            } finally {
                if (host.isEmpty()) {
                    // Log.systemMeldung("getHost 3: " + s.download.arr[DatenDownload.DOWNLOAD_URL_NR]);
                    host = "host";
                }
            }
        } catch (final Exception ex) {
            // Log.systemMeldung("getHost 4: " + s.download.arr[DatenDownload.DOWNLOAD_URL_NR]);
            host = "exception";
        }
        return host;
    }

    private static boolean getDown(int max) {
        int count = 0;
        try {
            for (final DownloadData download : ProgData.getInstance().downloadList) {
                if (download.isStateStartedRun()) {
                    ++count;
                    if (count >= max) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            P2Log.errorLog(794519083, ex);
        }
        return false;
    }
}
