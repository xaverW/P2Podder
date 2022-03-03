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


package de.p2tools.p2podder.controller.data.episode;

import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.starterEpisode.Start;
import de.p2tools.p2podder.gui.dialog.EpisodeDelDialogController;
import de.p2tools.p2podder.gui.dialog.NoSetDialogController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EpisodeFactory {

//    public static void delEpisodes(Episode episode) {
//        if (ProgConfig.SYSTEM_DELETE_EPISODE_FILE_DONT_ASK.getValue()) {
//            if (ProgConfig.SYSTEM_DELETE_EPISODE_FILE.getValue()) {
//                //dann automatisch alles machen
//                List<Episode> delList = new ArrayList<>();
//                delList.add(episode);
//                delFiles(delList);
//            }
//            return;
//        }
//
//        //dann will er erst mal gefragt werden
//        final String path = PFileUtils.addsPath(episode.getFilePath(), episode.getFileName());
//        if (PAlert.BUTTON.NO == new PAlert().alert_yes_no_remember(ProgData.getInstance().primaryStage,
//                "Episode", "Episode löschen?",
//                "Soll die Episode und die Datei: " + P2LibConst.LINE_SEPARATOR +
//                        path + P2LibConst.LINE_SEPARATOR
//                        + "gelöscht werden?", ProgConfig.SYSTEM_DELETE_EPISODE_FILE_DONT_ASK, "Nicht mehr anzeigen")) {
//            ProgConfig.SYSTEM_DELETE_EPISODE_FILE.setValue(false);
//            return;
//        } else {
//            ProgConfig.SYSTEM_DELETE_EPISODE_FILE.setValue(true);
//        }
//
//        List<Episode> delList = new ArrayList<>();
//        delList.add(episode);
//        delFiles(delList);
//    }


    public static void delEpisodes(Episode episode) {
        List<Episode> delList = new ArrayList<>();
        delList.add(episode);
        delEpisodes(delList);
    }

    public static void delShownEpisodes() {
        List<Episode> delList = new ArrayList<>();
        ProgData.getInstance().episodeStoredList.stream().forEach(episode -> {
            if (ProgData.getInstance().historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl())) {
                delList.add(episode);
            }
        });
        delEpisodes(delList);
    }

    public static void delEpisodes(List<Episode> episodeList) {
        if (!ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK.getValue()) {
            //dann wird schon mal nicht gefragt
            delFiles(episodeList);
            return;
        }

        //dann will er gefragt werden
        EpisodeDelDialogController episodeDelDialogController = new EpisodeDelDialogController(ProgData.getInstance(), episodeList);
        if (episodeDelDialogController.isOk()) {
            //ansonsten will wer nix
            delFiles(episodeList);
        }
    }

    private static void delFiles(List<Episode> delList) {
        delList.stream().forEach(episode ->
                //erst mal die Episode aus dem Programm löschen
                ProgData.getInstance().episodeStoredList.remove(episode)
        );

        if (ProgConfig.SYSTEM_DELETE_EPISODE_FILE.getValue()) {
            //und dann wenn gewünscht, auch noch die Datei löschen
            delList.stream().forEach(episode -> {
                final String path = PFileUtils.addsPath(episode.getFilePath(), episode.getFileName());
                final File file = new File(path);
                try {
                    if (file.exists()) {
                        PLog.sysLog("Episode (Datei) löschen: " + file.getAbsolutePath());
                        if (!file.delete()) {
                            throw new Exception();
                        }
                    }
                } catch (final Exception ex) {
                    PLog.errorLog(915200147, "Fehler beim löschen: " + file.getAbsolutePath());
                }
            });
        }
    }

    public static boolean episodeIsRunning(Episode episode) {
        if (episode.getStart() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static long countEpisode(Podcast podcast) {
        return ProgData.getInstance().episodeStoredList.stream().filter(p -> p.getPodcastId() == podcast.getId()).count();
    }

    public static void stopEpisode(Episode episode) {
        if (episodeIsRunning(episode)) {
            episode.getStart().stopStart();
        }
    }

    public static void stopAllEpisode() {
        ProgData.getInstance().episodeStoredList.stream().forEach(episode -> stopEpisode(episode));
    }

    public static void playEpisode(Episode episode) {
        playEpisode(episode, null);
    }

    public static void playEpisode(Episode episode, SetData setData) {
        if (setData == null) {
            setData = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (setData == null) {
            new NoSetDialogController(ProgData.getInstance());
            return;
        }

        // und starten
        startFileWithProgram(episode, setData);
    }

    private static synchronized void startFileWithProgram(Episode episode, SetData setData) {
        final String filePathName = episode.getFilePath();
        if (!filePathName.isEmpty()) {

            final Start start = new Start(setData, episode);
            episode.setStart(start);
            start.initStart();

            ProgData.getInstance().episodeStartingList.add(episode);
            ProgData.getInstance().episodeStarterFactory.startWaitingEpisodes();
            ProgData.getInstance().episodeGui.getEpisodeGuiController().tableRefresh();
        }
    }
}
