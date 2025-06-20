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

package de.p2tools.p2podder.controller.starterdownload;


import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfosFactory;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.tools.MLBandwidthTokenBucket;
import de.p2tools.p2podder.tools.MLInputStream;
import javafx.application.Platform;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DirectHttpDownload extends Thread {

    private static final int TIMEOUT_LENGTH = 3000; //HTTP Timeout in milliseconds

    private final ProgData progData;
    private final DownloadData download;
    private HttpURLConnection conn = null;
    private long downloaded = 0;
    private File file = null;
    private String responseCode;
    private String exMessage;
    private FileOutputStream fos = null;
    private final java.util.Timer bandwidthCalculationTimer;
    private boolean retBreak;
    private boolean dialogBreakIsVis;

    public DirectHttpDownload(ProgData progData, DownloadData d, java.util.Timer bandwidthCalculationTimer) {
        super();
        this.progData = progData;
        this.bandwidthCalculationTimer = bandwidthCalculationTimer;
        download = d;
        setName("DIRECT DL THREAD: " + d.getEpisodeTitle());
        download.setStateStartedRun();
    }

    @Override
    public synchronized void run() {
        DownloadStarterFactory.startMsg(download);
        try {
            Files.createDirectories(Paths.get(download.getDestPath()));
        } catch (final IOException ignored) {
        }

        int restartCount = 0;
        boolean restart = true;
        while (restart) {
            restart = false;
            try {
                final URL url = new URL(download.getEpisodeUrl());
                file = new File(download.getDestPathFile());

                if (!cancelDownload()) {
                    download.getDownloadSize().setTargetSize(getContentLength(url));
                    download.getDownloadSize().setActuallySize(0);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(1000 * ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_TIMEOUT_SECOND.getValue());
                    conn.setReadTimeout(1000 * ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_TIMEOUT_SECOND.getValue());

                    if (ProgConfig.SYSTEM_SSL_ALWAYS_TRUE.getValue() && conn instanceof HttpsURLConnection) {
                        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
                        httpsConn.setHostnameVerifier(
                                // Create all-trusting host name verifier
                                (hostname, session) -> true);
                    }

                    setupHttpConnection(conn);
                    conn.connect();
                    final int httpResponseCode = conn.getResponseCode();
                    if (httpResponseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                        // Range passt nicht, also neue Verbindung versuchen...
                        if (httpResponseCode == 416) {
                            conn.disconnect();
                            // Get a new connection and reset download param...
                            conn = (HttpURLConnection) url.openConnection();
                            downloaded = 0;
                            setupHttpConnection(conn);
                            conn.connect();
                            // hier war es dann nun wirklich...
                            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                                download.setStateError();
                            }
                        } else {
                            // ==================================
                            // dann wars das
                            responseCode = "Responsecode: " + conn.getResponseCode() + P2LibConst.LINE_SEPARATOR + conn.getResponseMessage();
                            P2Log.errorLog(915236798, "HTTP-Fehler: " + conn.getResponseCode() + ' ' + conn.getResponseMessage());
                            download.setErrorMessage(responseCode);
                            download.setStateError();
                        }
                    }
                }
                if (download.isStateStartedRun()) {
                    downloadContent();
                }
            } catch (final Exception ex) {
                if ((ex instanceof IOException)
                        && restartCount < ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART_HTTP.getValue()) {

                    if (ex instanceof java.net.SocketTimeoutException) {
                        // Timeout Fehlermeldung für zxd :)
                        final ArrayList<String> text = new ArrayList<>();
                        text.add("Timeout, Download Restarts: " + restartCount);
                        text.add("Ziel: " + download.getDestPathFile());
                        text.add("URL: " + download.getEpisodeUrl());
                        P2Log.sysLog(text.toArray(new String[text.size()]));
                    }

                    restartCount++;
                    restart = true;
                } else {
                    // dann weiß der Geier!
                    exMessage = ex.getMessage();
                    P2Log.errorLog(316598941, ex, "Fehler");
                    download.setErrorMessage(exMessage);
                    download.setStateError();
                }
            }
        }

        try {
            if (download.getDownloadStart().getInputStream() != null) {
                download.getDownloadStart().getInputStream().close();
            }
            if (fos != null) {
                fos.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        } catch (final Exception ignored) {
        }

        DownloadStarterFactory.finalizeDownload(download);
    }

    /**
     * Return the content length of the requested Url.
     *
     * @param url {@link URL} to the specified content.
     * @return Length in bytes or -1 on error.
     */
    private long getContentLength(final URL url) {
        long ret = -1;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", ProgInfosFactory.getUserAgent());
            connection.setReadTimeout(TIMEOUT_LENGTH);
            connection.setConnectTimeout(TIMEOUT_LENGTH);
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                ret = connection.getContentLengthLong();
            }
            // alles unter 300k sind Playlisten, ...
            if (ret < 300 * 1000) {
                ret = -1;
            }
        } catch (final Exception ex) {
            ret = -1;
            P2Log.errorLog(643298301, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ignore) {
                }
            }
        }
        return ret;
    }

    /**
     * Setup the HTTP connection common settings
     *
     * @param conn The active connection.
     */
    private void setupHttpConnection(HttpURLConnection conn) {
        conn.setRequestProperty("Range", "bytes=" + downloaded + '-');
        conn.setRequestProperty("User-Agent", ProgInfosFactory.getUserAgent());
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }

    /**
     * Start the actual download process here.
     *
     * @throws Exception
     */
    private void downloadContent() throws Exception {
        download.getDownloadStart().setInputStream(new MLInputStream(conn.getInputStream(),
                bandwidthCalculationTimer, ProgConfig.DOWNLOAD_MAX_BANDWIDTH_KBYTE));
        fos = new FileOutputStream(file, (downloaded != 0));
        download.getDownloadSize().addActFileSize(downloaded);
        final byte[] buffer = new byte[MLBandwidthTokenBucket.DEFAULT_BUFFER_SIZE];
        double percent, ppercent = DownloadConstants.PROGRESS_WAITING, startPercent = DownloadConstants.PROGRESS_NOT_STARTED;
        int len;
        long aktBandwidth = 0, aktSize = 0;

        while ((len = download.getDownloadStart().getInputStream().read(buffer)) != -1 && (!download.isStateStopped())) {
            downloaded += len;
            fos.write(buffer, 0, len);
            download.getDownloadSize().addActFileSize(len);

            // für die Anzeige prüfen ob sich was geändert hat
            if (aktSize != download.getDownloadSize().getActuallySize()) {
                aktSize = download.getDownloadSize().getActuallySize();
            }
            if (download.getDownloadSize().getTargetSize() > 0) {
                percent = 1.0 * aktSize / download.getDownloadSize().getTargetSize();
                if (startPercent == DownloadConstants.PROGRESS_NOT_STARTED) {
                    startPercent = percent;
                }

                // percent muss zwischen 0 und 1 liegen
                if (percent == DownloadConstants.PROGRESS_WAITING) {
                    percent = DownloadConstants.PROGRESS_STARTED;
                } else if (percent >= DownloadConstants.PROGRESS_FINISHED) {
                    percent = DownloadConstants.PROGRESS_NEARLY_FINISHED;
                }
                download.setProgress(percent);
                if (percent != ppercent) {
                    ppercent = percent;

                    // Restzeit ermitteln
                    if (percent > (DownloadConstants.PROGRESS_STARTED) && percent > startPercent) {
                        long timeLeft = 0;
                        long sizeLeft = download.getDownloadSize().getTargetSize() - download.getDownloadSize().getActuallySize();
                        if (sizeLeft <= 0) {
                            timeLeft = 0;
                        } else if (aktBandwidth > 0) {
                            timeLeft = sizeLeft / aktBandwidth;
                        }
                        download.getDownloadStart().setTimeLeftSeconds(timeLeft);
                    }
                }
            }
            aktBandwidth = download.getDownloadStart().getInputStream().getBandwidth(); // bytes per second
            if (aktBandwidth != download.getDownloadStart().getBandwidth()) {
                download.getDownloadStart().setBandwidth(aktBandwidth);
            }
        }

        if (!download.isStateStopped()) {
            if (DownloadStarterFactory.check(progData, download)) {
                // Anzeige ändern - fertig
                download.setStateFinished();
            } else {
                // Anzeige ändern - bei Fehler fehlt der Eintrag
                download.setStateError();
            }
        }
    }

    private boolean cancelDownload() {
        if (!file.exists()) {
            // dann ist alles OK
            return false;
        }
        dialogBreakIsVis = true;
        retBreak = true;
        Platform.runLater(() -> {
            retBreak = break_();
            dialogBreakIsVis = false;
        });
        while (dialogBreakIsVis) {
            try {
                wait(100);
            } catch (final Exception ignored) {

            }
        }
        return retBreak;
    }

    private boolean break_() {
        return false;
    }
}
