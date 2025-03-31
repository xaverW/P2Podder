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

import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.p2event.P2EventHandler;
import de.p2tools.p2podder.P2PodderController;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.controller.data.download.DownloadList;
import de.p2tools.p2podder.controller.data.episode.EpisodeList;
import de.p2tools.p2podder.controller.data.podcast.PodcastList;
import de.p2tools.p2podder.controller.filter.DownloadFilter;
import de.p2tools.p2podder.controller.filter.EpisodeFilter;
import de.p2tools.p2podder.controller.filter.EpisodeFilterSmall;
import de.p2tools.p2podder.controller.filter.PodcastFilter;
import de.p2tools.p2podder.controller.history.HistoryList;
import de.p2tools.p2podder.controller.starterdownload.DownloadStarterFactory;
import de.p2tools.p2podder.controller.starterepisode.EpisodeStarterFactory;
import de.p2tools.p2podder.controller.worker.EpisodeInfos;
import de.p2tools.p2podder.controller.worker.Worker;
import de.p2tools.p2podder.gui.DownloadGui;
import de.p2tools.p2podder.gui.EpisodeGui;
import de.p2tools.p2podder.gui.PodcastGui;
import de.p2tools.p2podder.gui.dialog.EpisodeInfoDialogController;
import de.p2tools.p2podder.gui.smallgui.SmallGuiPack;
import de.p2tools.p2podder.gui.tools.ProgTray;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class ProgData {
    private static ProgData instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean auto = false; // Automode
    public static boolean duration = false; // Duration ausgeben
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen
    public static boolean firstProgramStart = false; // ist der allererste Programmstart: Init wird gemacht

    // Infos
    public static String configDir = ""; // Verzeichnis zum Speichern der Programmeinstellungen

    // zentrale Klassen
    public PShortCut pShortcut; // verwendete Shortcuts
    public DownloadStarterFactory downloadStarterFactory; // Klasse zum Ausführen der Programme (für die Downloads): VLC, flvstreamer, ...

    // Gui
    public Stage primaryStage = null;
    public Stage primaryStageBig = null;
    public Stage primaryStageSmall = null;

    public P2MaskerPane maskerPane = null;
    public P2PodderController p2PodderController = null;

    public EpisodeGui episodeGui = null;
    public PodcastGui podcastGui = null;
    public DownloadGui downloadGui = null;

    public final EpisodeFilter episodeFilter;
    public final EpisodeFilterSmall episodeFilterSmall;
    public final DownloadFilter downloadFilter;
    public final PodcastFilter podcastFilter;

    public EpisodeInfoDialogController episodeInfoDialogController = null;
    public EpisodeInfos episodeInfos;

    //SmallGui
    public SmallGuiPack smallGuiPack = null;

    // Worker
    public Worker worker;
    public EpisodeStarterFactory episodeStarterFactory;
    public final ProgTray progTray;

    // Programmdaten
    public EpisodeList episodeList; //sind die gespeicherten Episoden
    public EpisodeList episodeStartingList; //sind die Episoden die gestartet werden sollen
    public PodcastList podcastList; //sind die Podcasts
    public DownloadList downloadList; //sind die, die zum Download anstehen

    public SetDataList setDataList;
    public HistoryList historyDownloads; //erfolgreich geladenen Downloads
    public HistoryList historyEpisodes; //gehörte Episoden

    public static BooleanProperty PODCAST_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);
    public static BooleanProperty DOWNLOAD_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);
    public static BooleanProperty EPISODE_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);

    // Programmdaten
    public P2EventHandler pEventHandler;

    private ProgData() {
        pEventHandler = new P2EventHandler(false);
        pShortcut = new PShortCut();

        episodeFilter = new EpisodeFilter(true);
        episodeFilterSmall = new EpisodeFilterSmall();
        downloadFilter = new DownloadFilter();
        podcastFilter = new PodcastFilter();

        episodeList = new EpisodeList(this);
        episodeStartingList = new EpisodeList(this);
        podcastList = new PodcastList();
        downloadList = new DownloadList(this);
        setDataList = new SetDataList();

        worker = new Worker(this);
        episodeStarterFactory = new EpisodeStarterFactory(this);
        downloadStarterFactory = new DownloadStarterFactory(this);

        episodeInfos = new EpisodeInfos(this);
        progTray = new ProgTray(this);

        historyDownloads = new HistoryList(ProgConst.FILE_FINISHED_DOWNLOADS,
                ProgInfosFactory.getSettingsDirectoryString());
        historyEpisodes = new HistoryList(ProgConst.FILE_PLAYED_EPISODES,
                ProgInfosFactory.getSettingsDirectoryString());
    }

    public void initProgData() {
        pEventHandler.startTimer();
        progTray.initProgTray();
    }

    public synchronized static ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }
}
