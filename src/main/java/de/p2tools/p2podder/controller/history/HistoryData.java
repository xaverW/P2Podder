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

package de.p2tools.p2podder.controller.history;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.GermanStringSorter;
import de.p2tools.p2lib.tools.P2StringUtils;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2podder.gui.tools.DownloadDate;
import org.apache.commons.lang3.time.FastDateFormat;

public class HistoryData implements Comparable<HistoryData> {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String fName) {
        url = fName;
    }

    public DownloadDate getDate() {
        return date;
    }

    public void setDate(DownloadDate fName) {
        date = fName;
    }

    private static final FastDateFormat sdf_datum = FastDateFormat.getInstance("dd.MM.yyyy");
    private static final GermanStringSorter sorter = GermanStringSorter.getInstance();
    private final static String SEPARATOR_1 = " |#| ";
    private final static String SEPARATOR_2 = "  |###|  ";

    public String title = "";
    public String theme = "";
    public String url = "";
    public DownloadDate date = new DownloadDate();

    public HistoryData(String date, String theme, String title, String url) {
        setTitle(title);
        setTheme(theme);
        setUrl(url);
        try {
            setDate(new DownloadDate(sdf_datum.parse(date).getTime()));
        } catch (final Exception ignore) {
            setDate(new DownloadDate(0));
        }
    }

    public String getLine() {
        String dateStr = getDate().toString();
        String theme = getTheme();
        String title = getTitle();
        String url = getUrl();

        final int MAX_THEME = 25;
        final int MAX_TITLE = 40;

        if (dateStr.isEmpty() && theme.isEmpty() && title.isEmpty()) {
            // ist das alte Format
            return url + P2LibConst.LINE_SEPARATOR;
        }

        if (theme.length() < MAX_THEME) {
            // nur wenn zu kurz, dann anpassen, so bleibt das Log ~lesbar
            // und Titel werden nicht abgeschnitten
            theme = P2StringUtils.shortenString(MAX_THEME, theme, false /* mitte */, false /*addVorne*/);
        }
        if (title.length() < MAX_TITLE) {
            // nur wenn zu kurz, dann anpassen, so bleibt das Log ~lesbar
            // und Titel werden nicht abgeschnitten
            title = P2StringUtils.shortenString(MAX_TITLE, title, false /* mitte */, false /*addVorne*/);
        }

        return dateStr + SEPARATOR_1
                + cleanUp(theme) + SEPARATOR_1
                + cleanUp(title) + SEPARATOR_2
                + url + P2LibConst.LINE_SEPARATOR;
    }

    public static HistoryData getHistoryDataFromLine(String line) {
        // 29.05.2014 |#| Abendschau                |#| Patenkind trifft Groß                     |###|  http://cdn-storage.br.de/iLCpbHJGNLT6NK9HsLo6s61luK4C_2rc5U1S/_-OS/5-8y9-NP/5bb33365-038d-46f7-914b-eb83fab91448_E.mp4
        String url = "", theme = "", title = "", date = "";
        int a1;
        try {
            if (line.contains(SEPARATOR_2)) {
                //neues Logfile-Format
                a1 = line.lastIndexOf(SEPARATOR_2);
                a1 += SEPARATOR_2.length();
                url = line.substring(a1).trim();
                // titel
                title = line.substring(line.lastIndexOf(SEPARATOR_1) + SEPARATOR_1.length(), line.lastIndexOf(SEPARATOR_2)).trim();
                date = line.substring(0, line.indexOf(SEPARATOR_1)).trim();
                theme = line.substring(line.indexOf(SEPARATOR_1) + SEPARATOR_1.length(), line.lastIndexOf(SEPARATOR_1)).trim();
            } else {
                url = line;
            }
        } catch (final Exception ex) {
            P2Log.errorLog(398853224, ex);
        }
        return new HistoryData(date, theme, title, url);
    }

    private static String cleanUp(String s) {
        s = s.replace("\n", ""); // zur Vorsicht bei Win
        s = s.replace("\r\n", ""); // zur Vorsicht bei Ux
        s = s.replace(P2LibConst.LINE_SEPARATOR, "");
        s = s.replace("|", "");
        s = s.replace(SEPARATOR_1, "");
        s = s.replace(SEPARATOR_2, "");
        return s;
    }

    @Override
    public int compareTo(HistoryData arg0) {
        return sorter.compare(getTitle(), arg0.getTitle());
    }
}
