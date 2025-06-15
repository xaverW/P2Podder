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
import de.p2tools.p2lib.mediathek.tools.P2SizeTools;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.ProgQuitFactory;
import de.p2tools.p2podder.controller.config.PEvents;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.data.download.DownloadFactory;
import de.p2tools.p2podder.controller.data.download.DownloadListStartFactory;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;

public class DownloadStarterFactory {
    private final ProgData progData;
    private StarterThread starterThread = null;
    private boolean paused = false;

    // ===================================
    // Public
    // ===================================
    public DownloadStarterFactory(ProgData progData) {
        this.progData = progData;
    }

    public void startWaitingDownloads() {
        if (starterThread == null || !starterThread.isAlive()) {
            //dann wieder starten
            starterThread = new StarterThread();
            starterThread.start();
        }
    }

    public void setPaused() {
        paused = true;
    }

    static boolean check(ProgData progData, DownloadData download) {
        // prüfen ob der Download geklappt hat und die Datei existiert und eine min. Größe hat
        boolean ret = false;
        final double progress = download.getProgress();

        if (progress > DownloadConstants.PROGRESS_NOT_STARTED && progress < DownloadConstants.PROGRESS_NEARLY_FINISHED) {
            // *progress* Prozent werden berechnet und es wurde vor 99,5% abgebrochen
            P2Log.errorLog(696510258, "Download fehlgeschlagen: 99,5% wurden nicht erreicht: " + progress + "%, " + download.getDestPathFile());
            return false;
        }

        final File file = new File(download.getDestPathFile());
        if (!file.exists()) {
            P2Log.errorLog(550236231, "Download fehlgeschlagen: Datei existiert nicht: " + download.getDestPathFile());
        } else if (file.length() < ProgConst.MIN_DATEI_GROESSE_FILM) {
            P2Log.errorLog(457892323, "Download fehlgeschlagen: Datei zu klein: " + download.getDestPathFile());
        } else {
            progData.historyDownloads.addHistoryDataToHistory(download);
            ret = true;
        }
        return ret;
    }

    /**
     * Delete the file if filesize is less that a constant value.
     *
     * @param file The file which is to be deleted.
     */
    static void deleteIfEmpty(File file) {
        try {
            if (file.exists()) {
                // zum Wiederstarten/Aufräumen die leer/zu kleine Datei löschen, alles auf Anfang
                if (file.length() == 0) {
                    // zum Wiederstarten/Aufräumen die leer/zu kleine Datei löschen, alles auf Anfang
                    P2Log.sysLog(new String[]{"Restart/Aufräumen: leere Datei löschen", file.getAbsolutePath()});
                    if (!file.delete()) {
                        throw new Exception();
                    }
                } else if (file.length() < ProgConst.MIN_DATEI_GROESSE_FILM) {
                    P2Log.sysLog(new String[]{"Restart/Aufräumen: Zu kleine Datei löschen", file.getAbsolutePath()});
                    if (!file.delete()) {
                        throw new Exception();
                    }
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(768123400, "Fehler beim löschen" + file.getAbsolutePath());
        }
    }

    static void startMsg(DownloadData download) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Download starten");
        list.add("Ziel: " + download.getDestPathFile());
        list.add("URL: " + download.getEpisodeUrl());
        list.add("Startzeit: " + P2DateConst.F_FORMAT_HH__mm__ss.format(download.getDownloadStart().getStartTime()));
        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list.toArray(new String[list.size()]));
    }

    private void restartMsg(DownloadData download) {
        final ArrayList<String> text = new ArrayList<>();
        text.add("Fehlerhaften Download neu starten - Restart (Summe Starts: " + download.getDownloadStart().getRestartCounter() + ')');
        text.add("Ziel: " + download.getDestPathFile());
        text.add("URL: " + download.getEpisodeUrl());
        P2Log.sysLog(text.toArray(new String[text.size()]));
    }

    private static void finishedMsg(final DownloadData download) {
        DownloadStart start = download.getDownloadStart();

        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        if (download.isStateStopped()) {
            list.add("Download wurde abgebrochen");

        } else {
            if (download.isStateFinished()) {
                // dann ists gut
                list.add("Download ist fertig und hat geklappt");
            } else if (download.isStateError()) {
                list.add("Download ist fertig und war fehlerhaft");
            }
            list.add("Ziel: " + download.getDestPathFile());
        }

        list.add("Startzeit: " + P2DateConst.F_FORMAT_HH__mm__ss.format(download.getDownloadStart().getStartTime()));
        list.add("Endzeit: " + P2DateConst.F_FORMAT_HH__mm__ss.format(new P2Date().getTime()));

        if (start.getRestartCounter() > 0) {
            list.add("Restarts: " + start.getRestartCounter());
        }

        final long dauer = download.getDownloadStart().getStartTime().diffInMinutes();
        if (dauer == 0) {
            list.add("Dauer: " + download.getDownloadStart().getStartTime().diffInSeconds() + " s");
            //list.add("Dauer: <1 Min.");
        } else {
            list.add("Dauer: " + download.getDownloadStart().getStartTime().diffInMinutes() + " Min");
        }

        if (download.getDownloadStart().getInputStream() != null) {
            list.add("Bytes gelesen: " + P2SizeTools.humanReadableByteCount(download.getDownloadStart().getInputStream().getSumByte(), true));
            list.add("Bandbreite: " + P2SizeTools.humanReadableByteCount(download.getDownloadStart().getInputStream().getSumBandwidth(), true));
        }
        list.add("URL: " + download.getEpisodeUrl());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list);

    }


    static void finalizeDownload(DownloadData download) {

        final DownloadStart start = download.getDownloadStart();
        deleteIfEmpty(new File(download.getDestPathFile()));
        setFileSize(download);

        finishedMsg(download);

        if (download.isStateError()) {
            download.setProgress(DownloadConstants.PROGRESS_NOT_STARTED);

        } else if (!download.isStateStopped()) {
            //dann ist er gelaufen
            start.setTimeLeftSeconds(0);
            download.setProgress(DownloadConstants.PROGRESS_FINISHED);
            download.getDownloadSize().setActuallySize(-1);

            if (start.getInputStream() != null) {
                download.setBandwidth("Ø " + P2SizeTools.humanReadableByteCount(start.getInputStream().getSumBandwidth(), true));
            }

            final long dauer = start.getStartTime().diffInMinutes();
            if (dauer == 0) {
                download.setRemaining("Dauer: " + start.getStartTime().diffInSeconds() + " s");
            } else {
                download.setRemaining("Dauer: " + start.getStartTime().diffInMinutes() + " Min");
            }

            //fertigen Download in die PosListe eintragen
            Episode episode = new Episode(download);
            Platform.runLater(() -> ProgData.getInstance().episodeList.add(episode));
        }

        download.setNo(P2LibConst.NUMBER_NOT_STARTED);
        Platform.runLater(() -> ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.EVENT_DOWNLOAD_REFRESH_TABLE)));

        start.setProcess(null);
        start.setInputStream(null);
        start.setStartTime(null);
    }

    /**
     * tatsächliche Dateigröße eintragen
     *
     * @param download {@link DownloadData} with the info of the file
     */

    static void setFileSize(DownloadData download) {
        try {
            final File destFile = new File(download.getDestPathFile());
            if (destFile.exists()) {
                final long length = destFile.length();
                if (length > 0)
                    download.getDownloadSize().setActuallySize(length);
            }
        } catch (
                final Exception ex) {
            P2Log.errorLog(461204780,
                    "Fehler beim Ermitteln der Dateigröße: " + download.getDestPathFile());
        }
    }

    // ********************************************
    // Hier wird dann gestartet
    // ewige Schleife die die Downloads startet
    // ********************************************
    private class StarterThread extends Thread {

        private DownloadData download;
        private final java.util.Timer bandwidthCalculationTimer;

        public StarterThread() {
            super();
            setName("DownloadStarter Daemon Thread");
            setDaemon(true);
            bandwidthCalculationTimer = new java.util.Timer("BandwidthCalculationTimer");
        }

        @Override
        public synchronized void run() {
            try {
                while (DownloadFactory.getDownloadsWaiting() > 0) {
                    //solange versuchen, einen zu Starten
                    while ((download = getNextStart()) != null) {
                        startDownload(download);
                        sleep(2 * 1000);
                    }
                    //dann Pause
                    sleep(5 * 1000);
                }

                if (ProgData.auto) {
                    // dann haben wir den "Automodus", Beenden
                    while (DownloadFactory.getDownloadsSaving() > 0) {
                        sleep(2 * 1000);
                    }
                    ProgQuitFactory.quit();
                }
            } catch (final Exception ex) {
                P2Log.errorLog(201201478, ex);
            }
        }

        private synchronized DownloadData getNextStart() throws InterruptedException {
            //ersten passenden Download der Liste zurückgeben oder null und versuchen,
            //dass bei mehreren laufenden Downloads ein anderer gesucht wird
            if (paused) {
                //beim Löschen der Downloads kann das Starten etwas "pausiert" werden
                //damit ein zu löschender Download nicht noch schnell gestartet wird
                sleep(5 * 1000);
                paused = false;
            }

            DownloadData download = DownloadListStartFactory.getNextStart();
            if (download == null) {
                // dann versuchen einen Fehlerhaften nochmal zu starten
                download = DownloadListStartFactory.getRestartDownload();
                if (download != null) {
                    restartMsg(download);
                }
            }
            return download;
        }

        /**
         * This will start the download process.
         *
         * @param download The {@link DownloadData} info object for download.
         */
        private void startDownload(DownloadData download) {
            download.getDownloadStart().startDownload();
            Thread downloadThread;
            downloadThread = new DirectHttpDownload(progData, download, bandwidthCalculationTimer);
            downloadThread.start();
        }
    }
}
