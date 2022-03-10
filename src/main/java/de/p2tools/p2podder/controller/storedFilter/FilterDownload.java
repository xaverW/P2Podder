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
import de.p2tools.p2podder.controller.data.download.Download;
import javafx.beans.property.*;

import java.util.function.Predicate;

public class FilterDownload {

    private LongProperty podcastId = new SimpleLongProperty(0);
    private StringProperty genre = new SimpleStringProperty("");
    private StringProperty title = new SimpleStringProperty("");
    private BooleanProperty isAll = new SimpleBooleanProperty(true);
    private BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private BooleanProperty isFinalized = new SimpleBooleanProperty(false);
    private boolean running = false;
    private boolean finalized = false;

    public FilterDownload() {
        podcastId.addListener((observable, oldValue, newValue) -> setPredicate());
        genre.addListener((observable, oldValue, newValue) -> setPredicate());
        title.addListener((observable, oldValue, newValue) -> setPredicate());
        isAll.addListener((observable, oldValue, newValue) -> {
            running = false;
            finalized = false;
            setPredicate();
        });
        isRunning.addListener((observable, oldValue, newValue) -> {
            running = true;
            finalized = false;
            setPredicate();
        });
        isFinalized.addListener((observable, oldValue, newValue) -> {
            running = false;
            finalized = true;
            setPredicate();
        });
    }

    public LongProperty podcastIdProperty() {
        return podcastId;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public BooleanProperty isAllProperty() {
        return isAll;
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public BooleanProperty isFinalizedProperty() {
        return isFinalized;
    }

    public void clearFilter() {
        this.podcastId.setValue(0);
        this.genre.setValue("");
        this.title.setValue("");
        setPredicate();
    }

    public Predicate<Download> getPredicate() {
        Predicate<Download> predicate = download -> true;

        if (podcastId.getValue() > 0) {
            predicate = predicate.and(download -> download.getPodcastId() == podcastId.getValue());
        }
        if (!genre.getValueSafe().isEmpty()) {
            predicate = predicate.and(download -> download.genreProperty().getValue().equals(genre.getValueSafe()));
        }
        if (!title.getValueSafe().isEmpty()) {
            predicate = predicate.and(download ->
                    FilterCheck.checkString(new Filter(title.getValueSafe(), true), download.getEpisodeTitle()));
        }
        if (running) {
            predicate = predicate.and(download -> download.isStarted());
        }
        if (finalized) {
            predicate = predicate.and(download -> download.isStateFinalized());
        }
        return predicate;
    }

    public void setPredicate() {
        ProgData.getInstance().downloadList.filteredListSetPred(getPredicate());
    }

    public void setPodcastId(long podcastId) {
        this.podcastId.setValue(podcastId);
        setPredicate();
    }
}
