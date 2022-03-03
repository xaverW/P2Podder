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

import java.text.DecimalFormat;

public class DownloadConstants {

    // Startnummer (Reihenfolge) der Downloads
    public static final int DOWNLOAD_NUMBER_NOT_STARTED = Integer.MAX_VALUE;

    public static final double PROGRESS_NOT_STARTED = -1;
    public static final double PROGRESS_WAITING = 0;
    public static final double PROGRESS_STARTED = 0.001; // 0,1%

    public static final double PROGRESS_NEARLY_FINISHED = 0.995; //99,5%
    public static final double PROGRESS_FINISHED = 1; //100%

    // Stati
    public static final int STATE_INIT = 0; // noch nicht gestart
    public static final int STATE_STOPPED = 1; // gestartet und wieder abgebrochen
    public static final int STATE_STARTED_WAITING = 2; // gestartet, warten auf das Downloaden
    public static final int STATE_STARTED_RUN = 3; //Download läuft
    public static final int STATE_FINISHED = 4; // fertig, Ok
    public static final int STATE_ERROR = 5; // fertig, fehlerhaft

    public static final int MAX_EPISODE_GRADE = 3;
    private static final DecimalFormat df = new DecimalFormat("###,##0.0");

    public static String getTextProgress(boolean dManager, int status, double progress) {
        String ret = "";

        if (progress == PROGRESS_NOT_STARTED) {
            if (status == STATE_STOPPED) {
                ret = "abgebrochen";
            } else if (status == STATE_STARTED_WAITING) {
//                ret = "wartend";
                ret = "wartet";
            } else if (status == STATE_ERROR) {
                ret = dManager ? "extern:fehler" : "fehlerhaft";
            } else {
                ret = "nicht gestartet";
            }

        } else if (progress == PROGRESS_WAITING) {
            //todo tritt eigentlich  nie auf??
            ret = dManager ? "extern" : "warten";

        } else if (progress == PROGRESS_STARTED) {
            ret = dManager ? "extern" : "gestartet";
        } else if (progress > PROGRESS_STARTED && progress < PROGRESS_FINISHED) {
            if (dManager) {
                ret = "extern";
            } else {
                ret = df.format(progress * 100) + " %";
//                System.out.println("progress: " + progress);
//                ret = Double.toString(progress / 10.0) + '%';
            }

        } else if (progress == PROGRESS_FINISHED && status == STATE_ERROR) {
            ret = dManager ? "extern:fehler" : "fehlerhaft";
        } else if (progress == PROGRESS_FINISHED) {
            ret = dManager ? "extern:fertig" : "fertig";
        }

        return ret;
    }

    public static String getTimeLeft(long timeLeftSeconds) {
        if (timeLeftSeconds > 300) {
            return Math.round(timeLeftSeconds / 60.0) + " Min.";

        } else if (timeLeftSeconds > 230) {
            return "5 Min.";
        } else if (timeLeftSeconds > 170) {
            return "4 Min.";
        } else if (timeLeftSeconds > 110) {
            return "3 Min.";
        } else if (timeLeftSeconds > 60) {
            return "2 Min.";
        } else if (timeLeftSeconds > 30) {
            return "1 Min.";
        } else if (timeLeftSeconds > 20) {
            return "30 s";
        } else if (timeLeftSeconds > 10) {
            return "20 s";
        } else if (timeLeftSeconds > 0) {
            return "10 s";
        } else {
            return "";
        }
    }
}
