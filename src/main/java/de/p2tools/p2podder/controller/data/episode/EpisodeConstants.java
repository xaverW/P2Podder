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

package de.p2tools.p2podder.controller.data.episode;

public class EpisodeConstants {

    //EpisodeNr wenn kein Podcast mehr gefunden wird
    public static final int PODCAST_NO = Integer.MAX_VALUE;

    //Stati
    public static final int STATE_INIT = 0; //nicht gestart
    public static final int STATE_STARTED_RUN = 1; //Episode läuft
    public static final int STATE_STOPPED = 4; //abgebrochen
    public static final int STATE_ERROR = 5; // fertig, fehlerhaft

    public static final String ALL = "";
    public static final String SRC_BUTTON = "Button"; //sind im Tab Episode mit Button gestartete
    public static final int MAX_EPISODE_GRADE = 3;
}
