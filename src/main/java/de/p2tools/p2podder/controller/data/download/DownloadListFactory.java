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

package de.p2tools.p2podder.controller.data.download;

import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2podder.controller.config.ProgData;
import org.apache.commons.io.FilenameUtils;

import java.util.*;

public class DownloadListFactory {
    private static boolean found = false;

    private DownloadListFactory() {
    }

    /**
     * Return a List of all loading but not yet finished downloads.
     *
     * @return A list with all station objects.
     */
//    static synchronized List<Download> getListOfDownloadsNotFinished() {
//        final List<Download> downloads = new ArrayList<>();
//
//        downloads.addAll(ProgData.getInstance().downloadList.stream()
//                .filter(download -> download.getDownloadStart() != null && download.getDownloadStart().getStartStatus().isStatedRunning())
//                .collect(Collectors.toList()));
//
//        return downloads;
//    }
    public static synchronized long countRunningDownloads() {
        long count = ProgData.getInstance().downloadList.stream()
                .filter(download -> download.isStarted()).count();
        return count;
    }

    public static synchronized void addNewDownloads(ArrayList<Download> list) {
        Set<String> syncDownloadsAlreadyInTheListHash = Collections.synchronizedSet(new HashSet<>(150)); //todo f??r 90% ??bertrieben, f??r 10% immer noch zu wenig???
        ProgData.getInstance().downloadList.forEach((download) -> syncDownloadsAlreadyInTheListHash.add(download.getEpisodeUrl()));

        found = false;
        List<Download> syncDownloadArrayList = Collections.synchronizedList(new ArrayList<>());
        list.stream().filter(download -> {
            if (ProgData.getInstance().historyDownloads.checkIfUrlAlreadyIn(download.getEpisodeUrl())) {
                // ist schon mal geladen worden
                return false;
            } else {
                return true;
            }
        }).forEach(download -> {
            // mit der tats??chlichen URL pr??fen, ob die URL schon in der Downloadliste ist
            final String urlDownload = download.getEpisodeUrl();
            if (!syncDownloadsAlreadyInTheListHash.add(urlDownload)) {
                return;
            }

            // dann in die Liste schreiben
            syncDownloadArrayList.add(download);
            found = true;
        });

        if (found) {
            checkDoubleNames(syncDownloadArrayList, ProgData.getInstance().downloadList);
            ProgData.getInstance().downloadList.addAll(syncDownloadArrayList);
            setNumbersInList();
        }
        syncDownloadArrayList.clear();
        syncDownloadsAlreadyInTheListHash.clear();
    }

    public static void checkDoubleNames(List<Download> foundDownloads, List<Download> downloadList) {
        // pr??fen, ob schon ein Download mit dem Zieldateinamen in der Downloadliste existiert
        try {
            final List<Download> alreadyDone = new ArrayList<>();

            foundDownloads.stream().forEach(download -> {
                final String oldName = download.getDestFileName();
                String newName = oldName;
                int i = 1;
                while (searchName(downloadList, newName) || searchName(alreadyDone, newName)) {
                    newName = getNewName(oldName, ++i);
                }

                if (!oldName.equals(newName)) {
                    download.setDestFileName(newName);
                }

                alreadyDone.add(download);
            });
        } catch (final Exception ex) {
            PLog.errorLog(303021458, ex);
        }
    }

    private static String getNewName(String oldName, int i) {
        String base = FilenameUtils.getBaseName(oldName);
        String suff = FilenameUtils.getExtension(oldName);
        return base + "_" + i + "." + suff;
    }

    private static boolean searchName(List<Download> searchDownloadList, String name) {
        return searchDownloadList.stream().filter(download -> download.getDestFileName().equals(name)).findAny().isPresent();
    }

    private static synchronized void setNumbersInList() {
        int i = ProgData.getInstance().downloadList.getNextNumber();
        for (final Download download : ProgData.getInstance().downloadList) {
            if (download.isStarted()) {
                // gestartete Downloads ohne!! Nummer nummerieren
                if (download.getNo() == DownloadConstants.DOWNLOAD_NUMBER_NOT_STARTED) {
                    download.setNo(i++);
                }

            } else {
                // nicht gestartete Downloads
                download.setNo(DownloadConstants.DOWNLOAD_NUMBER_NOT_STARTED);
            }
        }
    }
}