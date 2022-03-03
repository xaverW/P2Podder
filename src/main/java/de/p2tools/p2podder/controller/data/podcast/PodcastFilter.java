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

import de.p2tools.p2podder.controller.config.ProgData;

import java.util.Locale;
import java.util.function.Predicate;

public class PodcastFilter {

    public PodcastFilter() {
    }

    private String genre = "";
    private String name = "";
    private String url = "";

    public Predicate<Podcast> clearFilter() {
        this.genre = "";
        this.name = "";
        this.url = "";
        return getPredicate();
    }

    public Predicate<Podcast> getPredicate() {
        Predicate<Podcast> predicate = podcast -> true;

        if (!genre.isEmpty()) {
            predicate = predicate.and(podcast -> podcast.genreProperty().getValue().equals(genre));
        }
        if (!name.isEmpty()) {
            predicate = predicate.and(podcast ->
                    podcast.getName().toLowerCase(Locale.ROOT).contains(name));
        }
        if (!url.isEmpty()) {
            predicate = predicate.and(podcast ->
                    podcast.getUrl().toLowerCase(Locale.ROOT).contains(url));
        }
        return predicate;
    }

    private void setPredicate() {
        ProgData.getInstance().podcastList.filteredListSetPred(getPredicate());
    }

    public void setGenre(String genre) {
        if (genre == null) {
            genre = "";
        }
        this.genre = genre;
        setPredicate();
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase(Locale.ROOT);
        setPredicate();
    }

    public void setUrl(String url) {
        this.url = url.trim().toLowerCase(Locale.ROOT);
        setPredicate();
    }
}
