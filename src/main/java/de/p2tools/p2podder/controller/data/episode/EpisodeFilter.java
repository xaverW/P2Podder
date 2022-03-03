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


package de.p2tools.p2podder.controller.data.episode;

import de.p2tools.p2podder.controller.config.ProgData;

import java.util.Locale;
import java.util.function.Predicate;

public class EpisodeFilter {

    public EpisodeFilter() {
    }

    private long podcastId = 0;
    private String genre = "";
    private String title = "";
    private boolean isNew = false;
    private boolean isRunning = false;
    private boolean wasShown = false;

    public Predicate<Episode> clearFilter() {
        this.podcastId = 0;
        this.genre = "";
        this.title = "";

        return getPredicate();
    }

    public Predicate<Episode> getPredicate() {
        Predicate<Episode> predicate = episode -> true;

        if (podcastId > 0) {
            predicate = predicate.and(episode -> episode.podcastIdProperty().getValue().equals(podcastId));
        }
        if (!genre.isEmpty()) {
            predicate = predicate.and(episode -> episode.genreProperty().getValue().equals(genre));
        }
        if (!title.isEmpty()) {
            predicate = predicate.and(episode ->
                    episode.getEpisodeTitle().toLowerCase(Locale.ROOT).contains(title));
        }
        if (isNew) {
            predicate = predicate.and(episode -> episode.isNew());
        }
        if (isRunning) {
            predicate = predicate.and(episode -> EpisodeFactory.episodeIsRunning(episode));
        }
        if (wasShown) {
            predicate = predicate.and(episode -> ProgData.getInstance().historyEpisodes.
                    checkIfUrlAlreadyIn(episode.getEpisodeUrl()));
        }
        return predicate;
    }

    private void setPredicate() {
        ProgData.getInstance().episodeStoredList.filteredListSetPred(getPredicate());
    }

    public void setPodcastId(long podcastId) {
        this.podcastId = podcastId;
        setPredicate();
    }

    public void setGenre(String genre) {
        if (genre == null) {
            genre = "";
        }
        this.genre = genre;
        setPredicate();
    }

    public void setTitle(String title) {
        this.title = title.trim().toLowerCase(Locale.ROOT);
        setPredicate();
    }

    public void setAll() {
        this.isNew = false;
        this.isRunning = false;
        this.wasShown = false;
        setPredicate();
    }

    public void setNew() {
        this.isNew = true;
        this.isRunning = false;
        this.wasShown = false;
        setPredicate();
    }

    public void setRunning() {
        this.isNew = false;
        this.isRunning = true;
        this.wasShown = false;
        setPredicate();
    }

    public void setWasShown() {
        this.isNew = false;
        this.isRunning = false;
        this.wasShown = true;
        setPredicate();
    }
}
