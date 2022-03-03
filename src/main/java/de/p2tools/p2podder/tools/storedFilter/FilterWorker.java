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

package de.p2tools.p2podder.tools.storedFilter;

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.pEvent.EventListenerPodcastList;
import de.p2tools.p2podder.controller.config.pEvent.EventLoadPodcastList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class FilterWorker {

    private ObservableList<String> allGenreList = FXCollections.observableArrayList("");

    final SelectedFilter sfTemp = new SelectedFilter();
    private final ProgData progData;

    public FilterWorker(ProgData progData) {
        this.progData = progData;

        progData.eventNotifyLoadPodcastList.addListenerLoadPodcastList(new EventListenerPodcastList() {
            @Override
            public void start(EventLoadPodcastList event) {
                // the station combo will be reseted, therefore save the filter
                saveFilter();
            }

            @Override
            public void progress(EventLoadPodcastList event) {
            }

            @Override
            public void loaded(EventLoadPodcastList event) {
            }

            @Override
            public void finished(EventLoadPodcastList event) {
                createFilterLists();
                // activate the saved filter
                resetFilter();
            }
        });
    }

    private void saveFilter() {
        SelectedFilterFactory.copyFilter(progData.podcastGui.getStoredFiltersPodcast().getActFilterSettings(), sfTemp);
    }

    private void resetFilter() {
        SelectedFilterFactory.copyFilter(sfTemp, progData.podcastGui.getStoredFiltersPodcast().getActFilterSettings());
    }

    public void createFilterLists() {
        // alle laden
        allGenreList.setAll(Arrays.asList(progData.podcastList.genres));
    }

    public ObservableList<String> getAllGenreList() {
        return allGenreList;
    }
}
