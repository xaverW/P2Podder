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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.gui.dialog.DownloadFileDelDialogController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DownloadFactory {

    public static synchronized void startAllDownloads() {
        List<DownloadData> downloadList = ProgData.getInstance().downloadList;
        downloadList.stream().forEach(d -> {
            ProgData.getInstance().downloadList.startDownloads(downloadList, false);
        });
    }

    public static synchronized void cleanUpList() {
        // fertige Downloads löschen, fehlerhafte zurücksetzen
        boolean found = false;
        Iterator<DownloadData> it = ProgData.getInstance().downloadList.iterator();
        while (it.hasNext()) {
            DownloadData download = it.next();
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

    public static void cleanUpDownloadDir() {
        File destDir = new File(ProgConfig.SYSTEM_POD_DIR.getValueSafe());
        if (!destDir.exists()) {
            P2Alert.showInfoAlert("Downloads aufräumen",
                    "Dateien ohne Episode suchen",
                    "Das Verzeichnis mit den Downloads existiert nicht!");
            return;
        }

        ArrayList<File> fileList = new ArrayList<>();
        ArrayList<File> delList = new ArrayList<>();

        //====================================
        //search files
        try (Stream<Path> walk = Files.walk(destDir.toPath())) {
            fileList.addAll(walk.filter(Files::isRegularFile)
                    .map(path -> path.toFile())
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            P2Log.errorLog(456201247, e);
        }

        for (File file : fileList) {
            boolean found = false;

            for (Episode episode : ProgData.getInstance().episodeList) {
                String ePath = P2FileUtils.addsPath(episode.getFilePath(), episode.getFileName());
                String fPath = file.getPath();
                if (ePath.equals(fPath)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                delList.add(file);
            }
        }

        //==========================
        //delete Files
        if (delList.isEmpty()) {
            //alles sauber
            P2Alert.showInfoAlert("Downloads aufräumen", "Dateien ohne Episode suchen", "Es wurden keine " +
                    "Dateien ohne passende Episode, gefunden.");
            return;
        }

        DownloadFileDelDialogController downloadFileDelDialogController =
                new DownloadFileDelDialogController(ProgData.getInstance(), delList);
        if (downloadFileDelDialogController.isOk()) {

            delList.stream().forEach(delFile -> {
                try {
                    if (delFile.exists()) {
                        P2Log.sysLog("Episode (Datei) löschen: " + delFile.getAbsolutePath());
                        if (!delFile.delete()) {
                            throw new Exception();
                        }
                    }
                } catch (final Exception ex) {
                    P2Alert.showInfoAlert("Downloads aufräumen", "Dateien ohne Episode suchen",
                            "Das löschen der Datei: " + P2LibConst.LINE_SEPARATOR
                                    + delFile.getAbsolutePath() + P2LibConst.LINE_SEPARATOR +
                                    " hat nicht geklappt!");
                    P2Log.errorLog(901254123, "Fehler beim löschen: " + delFile.getAbsolutePath());
                }
            });
        }
    }

    public static synchronized long getDownloadsWaiting() {
        return ProgData.getInstance().downloadList.stream().filter(download -> download.isStateStartedWaiting()).count();
    }

    public static synchronized List<Podcast> getPodcastList() {
        P2Duration.counterStart("getPodcastList");

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

        P2Duration.counterStop("getPodcastList");
        return arrayList;
    }

    public static long countDownload(Podcast podcast) {
        return ProgData.getInstance().downloadList.stream().filter(p -> p.getPodcastId() == podcast.getId()).count();
    }
}
