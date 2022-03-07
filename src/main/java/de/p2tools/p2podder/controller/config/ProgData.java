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


package de.p2tools.p2podder.controller.config;

import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.P2PodderController;
import de.p2tools.p2podder.controller.config.pEvent.EventNotifyLoadPodcastList;
import de.p2tools.p2podder.controller.config.pEvent.PEventFactory;
import de.p2tools.p2podder.controller.data.P2PodderShortCuts;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.controller.data.download.DownloadList;
import de.p2tools.p2podder.controller.data.episode.EpisodeList;
import de.p2tools.p2podder.controller.data.podcast.PodcastList;
import de.p2tools.p2podder.controller.history.HistoryList;
import de.p2tools.p2podder.controller.starterDownload.DownloadStarterFactory;
import de.p2tools.p2podder.controller.starterEpisode.EpisodeStarterFactory;
import de.p2tools.p2podder.controller.worker.EpisodeInfos;
import de.p2tools.p2podder.controller.worker.StationInfos;
import de.p2tools.p2podder.controller.worker.Worker;
import de.p2tools.p2podder.gui.DownloadGui;
import de.p2tools.p2podder.gui.EpisodeFilterControllerClearFilter;
import de.p2tools.p2podder.gui.EpisodeGui;
import de.p2tools.p2podder.gui.PodcastGui;
import de.p2tools.p2podder.gui.dialog.EpisodeInfoDialogController;
import de.p2tools.p2podder.gui.tools.Listener;
import de.p2tools.p2podder.tools.storedFilter.DownloadFilter;
import de.p2tools.p2podder.tools.storedFilter.FilterWorker;
import de.p2tools.p2podder.tools.storedFilter.PodcastFilter;
import de.p2tools.p2podder.tools.storedFilter.StoredFilters;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {
    private static ProgData instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean duration = false; // Duration ausgeben
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen

    // Infos
    public static String configDir = ""; // Verzeichnis zum Speichern der Programmeinstellungen

    // zentrale Klassen
    public EventNotifyLoadPodcastList eventNotifyLoadPodcastList;
    public P2PodderShortCuts pShortcut; // verwendete Shortcuts

    public PEventFactory pEventFactory;
    public DownloadStarterFactory downloadStarterFactory; // Klasse zum Ausführen der Programme (für die Downloads): VLC, flvstreamer, ...

    // Gui
    public Stage primaryStage = null;
    public PMaskerPane maskerPane = null;
    public P2PodderController p2PodderController = null;

    public EpisodeGui episodeGui = null;
    public PodcastGui podcastGui = null;
    public DownloadGui downloadGui = null;
    public EpisodeFilterControllerClearFilter stationFilterControllerClearFilter = null;
    public final DownloadFilter downloadFilter;
    public final PodcastFilter podcastFilter;

    public EpisodeInfoDialogController episodeInfoDialogController = null;
    public FilterWorker filterWorker;
    public EpisodeInfos episodeInfos;
    public StationInfos stationInfos;

    // Worker
    public Worker worker;
    public EpisodeStarterFactory episodeStarterFactory;
    public final ProgTray progTray;
    public StoredFilters storedFilters; // gespeicherte Filterprofile

    // Programmdaten
    public EpisodeList episodeStoredList; //sind die gespeicherten Episoden
    public EpisodeList episodeStartingList; //sind die Episoden die gestartet werden sollen
    public PodcastList podcastList; //sind die Podcasts
    public DownloadList downloadList; //sind die, die zum Download anstehen

    public SetDataList setDataList;
    public HistoryList historyDownloads; //erfolgreich geladenen Downloads
    public HistoryList historyEpisodes; //gehörte Episoden

    private ProgData() {
        pShortcut = new P2PodderShortCuts();
        eventNotifyLoadPodcastList = new EventNotifyLoadPodcastList();

        storedFilters = new StoredFilters(this);
        downloadFilter = new DownloadFilter();
        podcastFilter = new PodcastFilter();

        episodeStoredList = new EpisodeList(this);
        episodeStartingList = new EpisodeList(this);
        podcastList = new PodcastList();
        downloadList = new DownloadList(this);
        setDataList = new SetDataList();

        pEventFactory = new PEventFactory();

        worker = new Worker(this);
        episodeStarterFactory = new EpisodeStarterFactory(this);
        downloadStarterFactory = new DownloadStarterFactory(this);

        filterWorker = new FilterWorker(this);
        episodeInfos = new EpisodeInfos(this);
        stationInfos = new StationInfos(this);
        progTray = new ProgTray(this);

        historyDownloads = new HistoryList(ProgConst.FILE_FINISHED_DOWNLOADS,
                ProgInfos.getSettingsDirectoryString());
        historyEpisodes = new HistoryList(ProgConst.FILE_PLAYED_EPISODES,
                ProgInfos.getSettingsDirectoryString());
    }

    boolean oneSecond = false;

    public void startTimer() {
        // extra starten, damit er im Einrichtungsdialog nicht dazwischen funkt
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
            oneSecond = !oneSecond;
            if (oneSecond) {
                doTimerWorkOneSecond();
            }
            doTimerWorkHalfSecond();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
        PDuration.onlyPing("Timer gestartet");
    }

    private void doTimerWorkOneSecond() {
        Listener.notify(Listener.EREIGNIS_TIMER, ProgData.class.getName());
    }

    private void doTimerWorkHalfSecond() {
        Listener.notify(Listener.EREIGNIS_TIMER_HALF_SECOND, ProgData.class.getName());
    }

    public synchronized static final ProgData getInstance(String dir) {
        if (!dir.isEmpty()) {
            configDir = dir;
        }
        return getInstance();
    }

    public synchronized static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }
}
