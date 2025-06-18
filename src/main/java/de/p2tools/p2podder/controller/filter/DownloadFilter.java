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


package de.p2tools.p2podder.controller.filter;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import javafx.beans.property.*;

import java.util.function.Predicate;

public class DownloadFilter {

    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final BooleanProperty isAll = new SimpleBooleanProperty(true);
    private final BooleanProperty isStarted = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final BooleanProperty isFinalized = new SimpleBooleanProperty(false);
    private boolean running = false;
    private boolean loading = false;
    private boolean finalized = false;

    public DownloadFilter() {
        podcastId.addListener((observable, oldValue, newValue) -> setPredicate());
        genre.addListener((observable, oldValue, newValue) -> setPredicate());
        title.addListener((observable, oldValue, newValue) -> setPredicate());

        isAll.addListener((observable, oldValue, newValue) -> {
            running = false;
            loading = false;
            finalized = false;
            setPredicate();
        });
        isStarted.addListener((observable, oldValue, newValue) -> {
            running = true;
            loading = false;
            finalized = false;
            setPredicate();
        });
        isLoading.addListener((observable, oldValue, newValue) -> {
            running = false;
            loading = true;
            finalized = false;
            setPredicate();
        });
        isFinalized.addListener((observable, oldValue, newValue) -> {
            running = false;
            loading = false;
            finalized = true;
            setPredicate();
        });
    }

    public void setPodcastId(long podcastId) {
        this.podcastId.setValue(podcastId);
        setPredicate();
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

    public BooleanProperty isStartedProperty() {
        return isStarted;
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }

    public BooleanProperty isFinalizedProperty() {
        return isFinalized;
    }

    public void setPredicate() {
        ProgData.getInstance().downloadList.filteredListSetPred(getPredicate());
    }

    public void clearFilter() {
        this.podcastId.setValue(0);
        this.genre.setValue("");
        this.title.setValue("");
        setPredicate();
    }

    private Predicate<DownloadData> getPredicate() {
        Predicate<DownloadData> predicate = download -> true;

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
        if (loading) {
            predicate = predicate.and(download -> download.isStateStartedRun());
        }
        if (finalized) {
            predicate = predicate.and(download -> download.isStateFinalized());
        }
        return predicate;
    }
}
