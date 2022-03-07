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


package de.p2tools.p2podder.tools.storedFilter;

import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.Download;

import java.util.Locale;
import java.util.function.Predicate;

public class DownloadFilter {

    private long podcastId = 0;
    private String genre = "";
    private String title = "";
    private int timeRange = ProgConst.FILTER_TIME_RANGE_ALL_VALUE;
    private boolean isRunning = false;
    private boolean finalized = false;

    public Predicate<Download> clearFilter() {
        this.podcastId = 0;
        this.genre = "";
        this.title = "";

        return getPredicate();
    }

    public Predicate<Download> getPredicate() {
        Predicate<Download> predicate = download -> true;

        if (podcastId > 0) {
            predicate = predicate.and(download -> download.getPodcastId() == podcastId);
        }
        if (!genre.isEmpty()) {
            predicate = predicate.and(download -> download.genreProperty().getValue().equals(genre));
        }
        if (timeRange != ProgConst.FILTER_TIME_RANGE_ALL_VALUE) {
            predicate = predicate.and(download -> download.checkDays(timeRange));
        }

        if (!title.isEmpty()) {
            predicate = predicate.and(download ->
                    FilterCheck.checkString(new Filter(title, true), download.getEpisodeTitle()));
        }
        if (isRunning) {
            predicate = predicate.and(download -> download.isStarted());
        }
        if (finalized) {
            predicate = predicate.and(download -> download.isStateFinalized());
        }
        return predicate;
    }

    private void setPredicate() {
        ProgData.getInstance().downloadList.filteredListSetPred(getPredicate());
    }

    public void setPodcastId(long podcastId) {
        this.podcastId = podcastId;
        setPredicate();
    }

    public void setTimeRange(int timeRange) {
        this.timeRange = timeRange;
        setPredicate();
    }

    public void setGenre(String genre) {
        if (genre == null) {
            return;
        }
        this.genre = genre;
        setPredicate();
    }

    public void setTitle(String title) {
        this.title = title.trim().toLowerCase(Locale.ROOT);
        setPredicate();
    }

    public void setAll() {
        this.isRunning = false;
        this.finalized = false;
        setPredicate();
    }

    public void setRunning() {
        this.isRunning = true;
        this.finalized = false;
        setPredicate();
    }

    public void setFinalized() {
        this.isRunning = false;
        this.finalized = true;
        setPredicate();
    }
}
