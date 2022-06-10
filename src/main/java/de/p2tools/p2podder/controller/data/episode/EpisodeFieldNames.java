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


public class EpisodeFieldNames {

    public static final String EPISODE_NO = "Nr";
    public static final String EPISODE_PODCAST_NO = "Nr";
    public static final String EPISODE_TITLE = "Titel";
    public static final String EPISODE_PODCAST_NAME = "Podcast";
    public static final String EPISODE_PODCAST_ID = "Podcast-Id";
    public static final String EPISODE_GENRE = "Genre";
    public static final String EPISODE_BUTTON1 = "";
    public static final String EPISODE_BUTTON2 = "";
    public static final String EPISODE_DURATION = "Dauer";
    public static final String EPISODE_DESCRIPTION = "Beschreibung";

    public static final String EPISODE_FILE_NAME = "Dateiname";
    public static final String EPISODE_FILE_PATH = "Pfad";
    public static final String EPISODE_FILE_SIZE = "Größe [MB]";
    public static final String EPISODE_URL = "Url";
    public static final String EPISODE_WEBSITE = "Website";
    public static final String EPISODE_DATE = "Datum";
    public static final String EPISODE_DATE_LONG = "Datum";


    public static final int EPISODE_NO_NO = 0;
    public static final int EPISODE_PODCAST_NO_NO = 1;
    public static final int EPISODE_TITLE_NO = 2;
    public static final int EPISODE_PODCAST_NAME_NO = 3;
    public static final int EPISODE_PODCAST_ID_NO = 4;
    public static final int EPISODE_GENRE_NO = 5;
    public static final int EPISODE_BUTTON1_NO = 6;
    public static final int EPISODE_BUTTON2_NO = 7;
    public static final int EPISODE_DURATION_NO = 8;
    public static final int EPISODE_DESCRIPTION_NO = 9;

    public static final int EPISODE_FILE_NAME_NO = 10;
    public static final int EPISODE_FILE_PATH_NO = 11;
    public static final int EPISODE_FILE_SIZE_NO = 12;
    public static final int EPISODE_URL_NO = 13;
    public static final int EPISODE_WEBSITE_NO = 14;
    public static final int EPISODE_DATE_NO = 15;
    public static final int EPISODE_DATE_LONG_NO = 16;

    public static final String[] COLUMN_NAMES = {
            EPISODE_NO,
            EPISODE_PODCAST_NO,
            EPISODE_TITLE,
            EPISODE_PODCAST_NAME,
            EPISODE_PODCAST_ID,
            EPISODE_GENRE,
            EPISODE_BUTTON1, EPISODE_BUTTON2,
            EPISODE_DURATION,
            EPISODE_DESCRIPTION,

            EPISODE_FILE_NAME,
            EPISODE_FILE_PATH,
            EPISODE_FILE_SIZE,
            EPISODE_URL,
            EPISODE_WEBSITE,
            EPISODE_DATE,
            EPISODE_DATE_LONG
    };

    public static int MAX_ELEM = COLUMN_NAMES.length;
}
