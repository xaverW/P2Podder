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


package de.p2tools.p2podder.controller.data.podcast;

public class PodcastNames {
    public static final String PODCAST_NO = "Nr";
    public static final String PODCAST_ID = "Id";
    public static final String PODCAST_NAME = "Name";
    public static final String PODCAST_GENRE = "Genre";
    public static final String PODCAST_DESCRIPTION = "Beschreibung";
    public static final String PODCAST_NUMBER = "Anzahl";
    public static final String PODCAST_DATE = "Datum";
    public static final String PODCAST_WEBSITE = "Website";
    public static final String PODCAST_URL = "Url";

    public static final int PODCAST_NO_NO = 0;
    public static final int PODCAST_ID_NO = 1;
    public static final int PODCAST_NAME_NO = 2;
    public static final int PODCAST_GENRE_NO = 3;
    public static final int PODCAST_DESCRIPTION_NO = 4;
    public static final int PODCAST_NUMBER_NO = 5;
    public static final int PODCAST_DATE_NO = 6;
    public static final int PODCAST_WEBSITE_NO = 7;
    public static final int PODCAST_URL_NO = 8;

    public static final String[] COLUMN_NAMES = {
            PodcastNames.PODCAST_NO,
            PodcastNames.PODCAST_ID,
            PodcastNames.PODCAST_NAME,
            PodcastNames.PODCAST_GENRE,
            PodcastNames.PODCAST_DESCRIPTION,
            PodcastNames.PODCAST_NUMBER,
            PodcastNames.PODCAST_DATE,
            PodcastNames.PODCAST_WEBSITE,
            PodcastNames.PODCAST_URL,
    };
    public static final int MAX_ELEM = COLUMN_NAMES.length;
}
