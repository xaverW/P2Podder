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

package de.p2tools.p2podder.controller.filter;

public class EpisodeFilterToXml {

    public static final String SELECTED_FILTER_NAME = "name";
    public static final String SELECTED_FILTER_PODCAST_ID = "podcastId";
    public static final String SELECTED_FILTER_GENRE = "genre";
    public static final String SELECTED_FILTER_TITLE = "title";
    public static final String SELECTED_FILTER_DESCRIPTION = "description";
    public static final String SELECTED_FILTER_TIME_RANGE = "timeRange";
    public static final String SELECTED_FILTER_IS_ALL = "isAll";
    public static final String SELECTED_FILTER_IS_NEW = "isNew";
    public static final String SELECTED_FILTER_IS_STARTED = "isStarted";
    public static final String SELECTED_FILTER_IS_RUNNING = "isRunning";
    public static final String SELECTED_FILTER_WAS_SHOWN = "wasShown";

    public static final int FILTER_NAME = 0;
    public static final int FILTER_PODCAST_ID = 1;
    public static final int FILTER_GENRE = 2;
    public static final int FILTER_TITLE = 3;
    public static final int FILTER_DESCRIPTION = 4;
    public static final int FILTER_TIME_RANGE = 5;
    public static final int FILTER_IS_ALL = 6;
    public static final int FILTER_IS_NEW = 7;
    public static final int FILTER_IS_STATED = 8;
    public static final int FILTER_IS_RUNNING = 9;
    public static final int FILTER_WAS_SHOWN = 10;

    public static final String[] XML_NAMES = {
            "name",
            "podcastId",
            "genre",
            "title",
            "description",
            "timeRange",
            "isAll",
            "isNew",
            "isStarted",
            "isRunning",
            "wasShown"
    };

    public static final String TAG = "Filter";

    public static String[] getEmptyArray() {
        final String[] array = new String[XML_NAMES.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = "";
        }
        return array;
    }

    public static String[] getXmlArray() {
        // erstellt die XML-Namen fürs Lesen/Schreiben aus/ins Configfile
        final String[] array = getEmptyArray();
        for (int i = 0; i < XML_NAMES.length; ++i) {
            array[i] = XML_NAMES[i];
        }
        return array;
    }
}
