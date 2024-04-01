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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.PSystemUtils;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.starterepisode.Start;
import de.p2tools.p2podder.gui.dialog.EpisodeDelDialogController;
import de.p2tools.p2podder.gui.dialog.NoSetDialogController;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EpisodeFactory {

    public static Optional<Episode> getSel() {
        if (ProgData.getInstance().smallGuiPack == null) {
            return ProgData.getInstance().episodeGui.getEpisodeGuiController().getSel();
        } else {
            return ProgData.getInstance().smallGuiPack.getSel();
        }
    }

    public static ArrayList<Episode> getSelList() {
        if (ProgData.getInstance().smallGuiPack == null) {
            return ProgData.getInstance().episodeGui.getEpisodeGuiController().getSelList();
        } else {
            return ProgData.getInstance().smallGuiPack.getSelList();
        }
    }

    public static void refreshTable() {
        if (ProgData.getInstance().smallGuiPack == null) {
            ProgData.getInstance().episodeGui.getEpisodeGuiController().tableRefresh();
        } else {
            ProgData.getInstance().smallGuiPack.tableRefresh();
        }
    }

    public static boolean episodeIsStarted(Episode episode) {
        if (episode.getStart() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean episodeIsRunning(Episode episode) {
        if (episode.getStart() != null && episode.getStart().getStartStatus().isStatedRunning()) {
            return true;
        } else {
            return false;
        }
    }

    public static long countEpisode(Podcast podcast) {
        return ProgData.getInstance().episodeList.stream().filter(p -> p.getPodcastId() == podcast.getId()).count();
    }

    public static void copyUrl() {
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(episode.get().getEpisodeUrl());
    }

    public static void playSelEpisode() {
        playEpisode(getSelList(), null);
    }

    public static void playEpisode(SetData setData) {
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            playEpisode(episode.get(), setData);
        }
    }

    public static void playEpisode() {
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            playEpisode(episode.get());
        }
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

        //und starten
        startFileWithProgram(episode, setData);
    }

    public static void playEpisode(List<Episode> episodeList, SetData setData) {
        if (setData == null) {
            setData = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (setData == null) {
            new NoSetDialogController(ProgData.getInstance());
            return;
        }

        //und starten
        startFileWithProgram(episodeList, setData);
    }

    public static void stopEpisode() {
        //bezieht sich auf die markierte Episode
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            stopEpisode(episode.get());
        }
    }

    public static void stopEpisode(Episode episode) {
        //eine Episode stoppen
        if (episodeIsStarted(episode)) {
            if (episode.getStart().getStartStatus().isStatedRunning()) {
                episode.getStart().getStartStatus().setStateStopped();
                episode.getStart().setNo(P2LibConst.NUMBER_NOT_STARTED);

            } else if (episode.getStart().getStartStatus().isStateInit()) {
                //dann ist er angelegt aber noch nicht gestartet
                episode.setStart(null);
            }
        }
    }


    public static void playNextEpisode() {
        //alle laufenden Episoden stoppen
        playNextEpisode(ProgData.getInstance().primaryStage);
    }

    public static void playNextEpisode(Stage stage) {
        //alle laufenden Episoden stoppen
        stopRunningEpisode();

        if (ProgData.getInstance().episodeStartingList.isEmpty()) {
            PAlert.showErrorAlert(stage, "Keine Episoden", "Es sind keine weiteren " +
                    "Episoden gestartet");
        }
    }

    public static void stopRunningEpisode() {
        //alle laufenden Episoden stoppen
        if (ProgData.getInstance().episodeStarterFactory.getRunningEpisode() != null) {
            stopEpisode(ProgData.getInstance().episodeStarterFactory.getRunningEpisode());
        }
    }


    public static void stopAllEpisode() {
        //alle laufenden Episoden stoppen
        if (ProgData.getInstance().episodeStarterFactory.getRunningEpisode() != null) {
            stopEpisode(ProgData.getInstance().episodeStarterFactory.getRunningEpisode());
        }
        ProgData.getInstance().episodeList.stream().forEach(episode -> stopEpisode(episode));
        ProgData.getInstance().episodeStartingList.clear();//die wartenden entfernen
    }


    public static void delEpisode() {
        //Menü: markierte Episoden löschen
        final Optional<Episode> episode = getSel();
        if (episode.isPresent()) {
            delEpisode(episode.get());
        }
    }

    public static void delEpisode(Episode episode) {
        List<Episode> delList = new ArrayList<>();
        delList.add(episode);
        delEpisodes(delList);
    }

    public static void delAllShownEpisodes() {
        List<Episode> delList = new ArrayList<>();
        ProgData.getInstance().episodeList.stream().forEach(episode -> {
            if (ProgData.getInstance().historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl())) {
                delList.add(episode);
            }
        });
        delEpisodes(delList);
    }

    public static void delSelEpisode() {
        //Menü: Alle markierten Episoden löschen
        delEpisodes(getSelList());
    }

    private static void delEpisodes(List<Episode> episodeList) {
        if (!ProgConfig.SYSTEM_DELETE_EPISODE_FILE_ASK.getValue()) {
            //dann wird schon mal nicht gefragt
            delEpisodeAndFiles(episodeList);
            return;
        }

        //dann will er gefragt werden
        EpisodeDelDialogController episodeDelDialogController = new EpisodeDelDialogController(ProgData.getInstance(), episodeList);
        if (episodeDelDialogController.isOk()) {
            //ansonsten will wer nix
            delEpisodeAndFiles(episodeList);
        }
    }

    private static void delEpisodeAndFiles(List<Episode> delList) {
        delList.stream().forEach(episode -> {
                    //erst mal die Episode aus dem Programm löschen
                    ProgData.getInstance().episodeList.remove(episode);
                }
        );

        if (ProgConfig.SYSTEM_DELETE_EPISODE_FILE.getValue()) {
            //und dann wenn gewünscht, auch noch die Datei löschen
            delList.stream().forEach(episode -> {
                final String path = P2FileUtils.addsPath(episode.getFilePath(), episode.getFileName());
                final File file = new File(path);
                try {
                    if (file.exists()) {
                        P2Log.sysLog("Episode (Datei) löschen: " + file.getAbsolutePath());
                        if (!file.delete()) {
                            throw new Exception();
                        }
                    }
                } catch (final Exception ex) {
                    P2Log.errorLog(915200147, "Fehler beim löschen: " + file.getAbsolutePath());
                }
            });
        }
    }

    private static synchronized void startFileWithProgram(Episode episode, SetData setData) {
        final String pathName = P2FileUtils.addsPath(episode.getFilePath(), episode.getFileName());
        if (episode.getFilePath().isEmpty() || episode.getFileName().isEmpty()) {
            return;
        }
        if (!P2FileUtils.fileExist(pathName)) {
            PAlert.showErrorAlert(ProgData.getInstance().primaryStage,
                    "Episode abspielen",
                    "Die Datei: \n" + pathName + "\nexistiert nicht mehr!");
            return;
        }

        if (!ProgData.getInstance().episodeStartingList.contains(episode) &&
                episode.getStart() == null) {
            //sonst läuft er eh schon, sind ja nach dem Start nicht mehr in der Liste,
            //ABER wenn episode beim Stoppen ist, wird "START" im gleichen episode auf NULL gesetzt
            final Start start = new Start(setData, episode);
            episode.setStart(start);
            ProgData.getInstance().episodeStartingList.add(episode);
        }

        ProgData.getInstance().episodeStarterFactory.startWaitingEpisodes();
        refreshTable();
    }

    private static synchronized void startFileWithProgram(List<Episode> episodeList, SetData setData) {
        episodeList.stream().forEach(episode -> {
            final String pathName = P2FileUtils.addsPath(episode.getFilePath(), episode.getFileName());
            if (episode.getFilePath().isEmpty() || episode.getFileName().isEmpty()) {
                return;
            }
            if (!P2FileUtils.fileExist(pathName)) {
                PAlert.showErrorAlert(ProgData.getInstance().primaryStage,
                        "Episode abspielen",
                        "Die Datei: \n" + pathName + "\nexistiert nicht mehr!");
                return;
            }

            if (!ProgData.getInstance().episodeStartingList.contains(episode) &&
                    episode.getStart() == null) {
                //sonst läuft er eh schon, sind ja nach dem Start nicht mehr in der Liste,
                //ABER wenn episode beim Stoppen ist, wird "START" im gleichen episode auf NULL gesetzt
                final Start start = new Start(setData, episode);
                episode.setStart(start);
                ProgData.getInstance().episodeStartingList.add(episode);
            }
        });

        ProgData.getInstance().episodeStarterFactory.startWaitingEpisodes();
        refreshTable();
    }
}
