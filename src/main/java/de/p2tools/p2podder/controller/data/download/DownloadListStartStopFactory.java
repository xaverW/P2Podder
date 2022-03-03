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


import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2podder.controller.config.ProgData;

import java.util.ArrayList;
import java.util.Collection;

public class DownloadListStartStopFactory {

    private DownloadListStartStopFactory() {
    }


    /**
     * eine Liste Downloads aus der "Dwonloadliste"  zurückstellen
     *
     * @param list
     */
    public static synchronized boolean putBackDownloads(ArrayList<Download> list) {
        boolean found = false;

        if (list == null || list.isEmpty()) {
            return false;
        }

        // das Starten von neuen Downloads etwas Pausieren
        ProgData.getInstance().downloadStarterFactory.setPaused();

        for (final Download download : list) {
            if (download.isStateInit() || download.isStateStoped()) {
                download.putBack();
                found = true;
            } else {
                list.remove(download);
            }
        }

        if (found) {
            ProgData.getInstance().downloadList.removeAll(list);
        }
        return found;
    }

    /**
     * einen Download aus der "Dwonloadliste"  stoppen und dann entfernen
     *
     * @param download
     */
    public static synchronized void delDownloads(Download download) {
        ArrayList<Download> list = new ArrayList<>();
        list.add(download);
        delDownloads(list);
    }


    /**
     * eine Liste Downloads aus der "Dwonloadliste"  stoppen und dann entfernen
     *
     * @param list
     */

    public static synchronized boolean delDownloads(ArrayList<Download> list) {
        PDuration.counterStart("delDownloads");
        if (list == null || list.isEmpty()) {
            return false;
        }

        // das Starten von neuen Downloads etwas Pausieren
        ProgData.getInstance().downloadStarterFactory.setPaused();

        final ArrayList<Download> historyList = new ArrayList<>();
        for (final Download download : list) {
            // ein Abo wird zusätzlich ins Logfile geschrieben
            historyList.add(download);
        }
        if (!historyList.isEmpty()) {
            ProgData.getInstance().historyDownloads.addDownloadDataListToHistory(historyList);
        }
        list.stream().filter(download -> download.isStateStartedRun()).forEach(download -> download.stopDownload());
        boolean found = ProgData.getInstance().downloadList.removeAll(list);

        PDuration.counterStop("delDownloads");
        return found;
    }

    /**
     * eine Liste Downloads stoppen
     */
    public static synchronized boolean stopDownloads() {
        boolean found = false;

        if (ProgData.getInstance().downloadList.isEmpty()) {
            return false;
        }

        // das Starten von neuen Downloads etwas Pausieren
        ProgData.getInstance().downloadStarterFactory.setPaused();

        for (Download download : ProgData.getInstance().downloadList) {
            if (download.isStateStartedWaiting() || download.isStateStartedRun() || download.isStateError()) {
                // nur dann läuft er
                download.stopDownload();
                found = true;
            }
        }

        return found;
    }


    private static PAlert.BUTTON restartDownload(int size, String title, PAlert.BUTTON answer) {
        if (answer.equals(PAlert.BUTTON.UNKNOWN)) {
            // nur einmal fragen
            String text;
            if (size > 1) {
                text = "Es sind auch fehlerhafte Filme dabei," + P2LibConst.LINE_SEPARATOR + "diese nochmal starten?";
            } else {
                text = "Film nochmal starten?  ==> " + title;
            }
            answer = new PAlert().showAlert_yes_no_cancel("Download", "Fehlerhafte Downloads", text);
        }
        return answer;
    }

    public static void startDownloads(Download download) {
        // Download starten
        ArrayList<Download> list = new ArrayList<>();
        list.add(download);
        start(list);
    }


    /**
     * eine Liste Downloads starten
     *
     * @param list
     * @param alsoFinished
     */

    public static boolean startDownloads(Collection<Download> list, boolean alsoFinished) {
        if (list == null || list.isEmpty()) {
            return false;
        }

        PDuration.counterStart("startDownloads");
        final ArrayList<Download> listStartDownloads = new ArrayList<>();

        // das Starten von neuen Downloads etwas Pausieren
        ProgData.getInstance().downloadStarterFactory.setPaused();

        // nicht gestartete einfach starten
        list.stream().filter(download -> download.isNotStarted()).forEach(download ->
                listStartDownloads.add(download));

        if (alsoFinished) {
            if (!startAlsoFinishedDownloads(list, listStartDownloads)) {
                return false;
            }
        }

        // alle Downloads starten/wiederstarten
        start(listStartDownloads);

        PDuration.counterStop("startDownloads");
        return true;
    }

    private static void start(ArrayList<Download> downloads) {
        if (downloads.isEmpty()) {
            return;
        }

        downloads.stream().forEach(download -> download.initStartDownload());
        ProgData.getInstance().downloadList.addNumber(downloads);
        ProgData.getInstance().downloadStarterFactory.startWaitingDownloads();
    }

    private static boolean startAlsoFinishedDownloads(Collection<Download> list, ArrayList<Download> listStartDownloads) {

        PAlert.BUTTON answer = PAlert.BUTTON.UNKNOWN;
        final ArrayList<Download> listDelDownloads = new ArrayList<>();
        final ArrayList<Download> listDownloadsRemovePodcastHistory = new ArrayList<>();

        // bereits gestartete erst vorbehandeln: wenn er noch läuft/fertig ist gibts nix
        // fehlerhafte nur wenn gewollt
        for (Download download : list) {

            // abgebrochene starten
            if (download.isStateStoped()) {
                listDelDownloads.add(download);
                // wenn er schon feritg ist und ein Abos ist, Url auch aus dem Logfile löschen, der Film ist damit wieder auf "Anfang"
                listDownloadsRemovePodcastHistory.add(download);
                listStartDownloads.add(download);
            }

            //fehlerhafte nur wenn gewollt wieder starten
            if (download.isStateError()) {
                if (answer.equals(PAlert.BUTTON.UNKNOWN)) {
                    answer = restartDownload(list.size(), download.getEpisodeTitle(), answer);
                }

                switch (answer) {
                    case CANCEL:
                        break;
                    case NO:
                        // weiter mit der nächsten URL
                        continue;
                    case YES:
                    default:
                        listDelDownloads.add(download);
                        // wenn er schon feritg ist und ein Abos ist, Url auch aus dem Logfile löschen, der Film ist damit wieder auf "Anfang"
                        listDownloadsRemovePodcastHistory.add(download);
                        listStartDownloads.add(download);
                }
            }
        }

        if (answer.equals(PAlert.BUTTON.CANCEL)) {
            // dann machmer nix
            return false;
        }

        //aus der AboHistory löschen
        ProgData.getInstance().historyDownloads.removeDownloadDataFromHistory(listDownloadsRemovePodcastHistory);

        // jetzt noch die Starts stoppen
        listDelDownloads.stream().forEach(download -> download.stopDownload());

        return true;
    }
}
