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


package de.p2tools.p2podder.controller.storedFilter;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Locale;
import java.util.function.Predicate;

public class FilterPodcast {

    private StringProperty genre = new SimpleStringProperty("");
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty url = new SimpleStringProperty("");

    public FilterPodcast() {
        genre.addListener((observable, oldValue, newValue) -> setPredicate());
        name.addListener((observable, oldValue, newValue) -> setPredicate());
        url.addListener((observable, oldValue, newValue) -> setPredicate());
    }

    public void clearFilter() {
        this.genre.setValue("");
        this.name.setValue("");
        this.url.setValue("");
        setPredicate();
    }


    public StringProperty genreProperty() {
        return genre;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty urlProperty() {
        return url;
    }

    public Predicate<Podcast> getPredicate() {
        Predicate<Podcast> predicate = podcast -> true;

        if (!genre.getValueSafe().isEmpty()) {
            predicate = predicate.and(podcast -> podcast.genreProperty().getValue().equals(genre.getValueSafe()));
        }
        if (!name.getValueSafe().isEmpty()) {
            predicate = predicate.and(podcast ->
                    FilterCheck.checkString(new Filter(name.getValueSafe(), true), podcast.getName()));
        }
        if (!url.getValueSafe().isEmpty()) {
            predicate = predicate.and(podcast ->
                    FilterCheck.checkString(new Filter(url.getValueSafe(), true), podcast.getUrl()));
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
        this.genre.setValue(genre);
        setPredicate();
    }

    public void setName(String name) {
        this.name.setValue(name.trim().toLowerCase(Locale.ROOT));
        setPredicate();
    }

    public void setUrl(String url) {
        this.url.setValue(url.trim().toLowerCase(Locale.ROOT));
        setPredicate();
    }
}
