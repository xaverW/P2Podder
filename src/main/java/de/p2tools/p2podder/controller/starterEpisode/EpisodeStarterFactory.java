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

package de.p2tools.p2podder.controller.starterEpisode;

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.application.Platform;

import java.awt.*;
import java.util.ArrayList;

/**
 * Start Episode via an external program.
 */
public class EpisodeStarterFactory {

    private final int stat_start = 0;
    private final int stat_running = 1;
    private final int stat_restart = 3;
    private final int stat_finished_ok = 10; //ab hier ist schluss
    private final int stat_finished_error = 11;
    private final int stat_end = 99;

    private final ProgData progData;
    private StarterThread starterThread;
    private Episode runningEpisode = null;//ist die gerade laufende Episode

    public EpisodeStarterFactory(ProgData progData) {
        super();
        this.progData = progData;
    }

    public void startWaitingEpisodes() {
        if (starterThread == null || starterThread != null && !starterThread.isAlive()) {
            //dann wieder starten
            starterThread = new StarterThread();
            starterThread.start();
        }
    }

    public Episode getRunningEpisode() {
        return runningEpisode;
    }

    //*******************************************
    //Hier wird dann gestartet
    //der Thread läuft so lange, wie Episoden
    //vorhanden sind
    //*******************************************
    private class StarterThread extends Thread {
        private Episode episode;

        public StarterThread() {
            super();
            setName("Episode Daemon Thread");
            setDaemon(true);
        }

        @Override
        public synchronized void run() {
            try {
                while ((episode = getNextStart()) != null) {
                    startEpisode(episode);
                    sleep(1 * 1000);
                }

            } catch (final Exception ex) {
                PLog.errorLog(903273049, ex);
            }
        }

        private synchronized Episode getNextStart() {
            Episode nextStart = progData.episodeStartingList.getNextStart();
            return nextStart;
        }

        private void startEpisode(Episode episode) {
            runningEpisode = episode;
            progData.historyEpisodes.addHistoryDataToHistory(episode);

            Start start = episode.getStart();
            int stat = stat_start;
            refreshTable();

            try {
                while (stat < stat_end) {
                    stat = runningLoop(start, stat);
                }
            } catch (final Exception ex) {
                final String exMessage = ex.getLocalizedMessage();
                PLog.errorLog(987989569, ex);
                if (start.getRestartCounter() == 0) {
                    //nur beim ersten Mal melden -> nervt sonst
                    Platform.runLater(() -> new StartEpisodeErrorDialogController(start, exMessage));
                }
                start.getStartStatus().setState(StartStatus.STATE_ERROR);
                start.getStartStatus().setErrorMessage(exMessage);
            }

            runningEpisode = null;
            finalizePlaying(episode);
        }

        private int runningLoop(Start start, int stat) {
            switch (stat) {
                case stat_start:
                    // versuch das Programm zu Starten
                    stat = startProgram(start);
                    break;

                case stat_running:
                    // hier läuft der Download bis zum Abbruch oder Ende
                    stat = runProgram(start);
                    break;

                case stat_restart:
                    stat = restartProgram(start);
                    break;

                case stat_finished_error:
                    start.getStartStatus().setStateError();
                    stat = stat_end;
                    break;

                case stat_finished_ok:
                    start.getStartStatus().setStateFinished();
                    stat = stat_end;
                    break;
            }

            return stat;
        }

        private int startProgram(Start start) {
            //versuch das Programm zu Starten
            //die Reihenfolge: startCounter - startmeldung ist wichtig!
            int retStat;
            start.incStartCounter();
            startMsg(start);
            final StartRuntimeExec runtimeExec = new StartRuntimeExec(start);
            final Process process = runtimeExec.exec();
            start.setProcess(process);

            if (process != null) {
                start.getStartStatus().setStateRunning();
                retStat = stat_running;
            } else {
                retStat = stat_restart;
            }
            return retStat;
        }

        private int runProgram(Start start) {
            //hier läufts bis zum Abbruch oder Ende
            int retStatus = stat_running;
            try {
                if (start.getStartStatus().isStateStopped()) {
                    //soll abgebrochen werden
                    retStatus = stat_finished_ok;
                    if (start.getProcess() != null) {
                        start.getProcess().destroy();
                    }

                } else {
                    final int exitV = start.getProcess().exitValue(); //liefert ex wenn noch nicht fertig
                    if (exitV != 0) {
                        retStatus = stat_restart;
                    } else {
                        retStatus = stat_finished_ok;
                    }
                }
            } catch (final Exception ex) {
                try {
                    this.wait(2000);
                } catch (final InterruptedException ignored) {
                }
            }
            return retStatus;
        }

        private int restartProgram(Start start) {
            int retStatus;
            // counter prüfen und starten bis zu einem MaxWert, sonst endlos
            if (start.getStartCounter() < StartStatus.START_COUNTER_MAX) {
                // dann nochmal von vorne
                retStatus = stat_start;
            } else {
                // dann wars das
                retStatus = stat_finished_error;
            }
            return retStatus;
        }

        private void startMsg(Start starter) {
            final ArrayList<String> list = new ArrayList<>();
            list.add(PLog.LILNE3);
            list.add("Episode abspielen");

            list.add("URL: " + starter.getFilePathName());
            list.add("Startzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(starter.getStartTime()));
            list.add("Programmaufruf: " + starter.getProgramCall());
            list.add("Programmaufruf[]: " + starter.getProgramCallArray());

            list.add(PLog.LILNE_EMPTY);
            PLog.sysLog(list.toArray(new String[list.size()]));
        }

        private void finalizePlaying(Episode episode) {
            //Aufräumen
            Start start = episode.getStart();
            finishedMsg(start);

            start.setProcess(null);
            episode.setStart(null);
            refreshTable();
        }

        private void finishedMsg(final Start start) {
            if (ProgConfig.DOWNLOAD_BEEP.get()) {
                try {
                    Toolkit.getDefaultToolkit().beep();
                } catch (final Exception ignored) {
                }
            }

            final ArrayList<String> list = new ArrayList<>();
            list.add(PLog.LILNE3);
            list.add("Episode abspielen beendet");
            list.add("Startzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(start.getStartTime()));
            list.add("Endzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(new PDate().getTime()));

            if (start.getRestartCounter() > 0) {
                list.add("Restarts: " + start.getRestartCounter());
            }

            final long dauer = start.getStartTime().diffInMinutes();
            if (dauer == 0) {
                list.add("Dauer: " + start.getStartTime().diffInSeconds() + " s");
                //list.add("Dauer: <1 Min.");
            } else {
                list.add("Dauer: " + start.getStartTime().diffInMinutes() + " Min");
            }

            list.add("URL: " + start.getFilePathName());
            list.add("Programmaufruf: " + start.getProgramCall());
            list.add("Programmaufruf[]: " + start.getProgramCallArray());

            list.add(PLog.LILNE_EMPTY);
            PLog.sysLog(list);
        }

        private void refreshTable() {
            ProgData.getInstance().episodeGui.getEpisodeGuiController().tableRefresh();
            if (ProgData.getInstance().smallEpisodeGuiController != null) {
                ProgData.getInstance().smallEpisodeGuiController.tableRefresh();
            }
        }
    }
}