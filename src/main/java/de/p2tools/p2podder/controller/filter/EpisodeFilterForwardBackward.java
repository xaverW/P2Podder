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

package de.p2tools.p2podder.controller.filter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public final class EpisodeFilterForwardBackward {

    private final EpisodeFilter actFilter;
    private final BooleanProperty backward = new SimpleBooleanProperty(false);
    private final BooleanProperty forward = new SimpleBooleanProperty(false);
    private boolean stopInput = false;

    // ist die Liste der zuletzt verwendeten Filter
    private final ObservableList<EpisodeFilter> filterListBackward = FXCollections.observableArrayList();
    private final ObservableList<EpisodeFilter> filterListForward = FXCollections.observableArrayList();

    public EpisodeFilterForwardBackward(EpisodeFilter actFilter) {
        this.actFilter = actFilter;
        filterListBackward.add(new EpisodeFilter());
        filterListBackward.addListener((ListChangeListener<EpisodeFilter>) c ->
                backward.setValue(checkBack()));

        filterListForward.addListener((ListChangeListener<EpisodeFilter>) c ->
                forward.setValue(!filterListForward.isEmpty()));
    }

    private boolean checkBack() {
        if (filterListBackward.isEmpty()) {
            return false;
        }

        if (filterListBackward.size() == 1) {
            if (!EpisodeFilterFactory.compareFilter(actFilter, filterListBackward.get(0))) {
                return true;
            } else {
                return false;
            }
        }

        //dann wird schon ein passender dabei sein :)
        return true;
    }


    public void addNewFilter() {
        if (stopInput) {
            return;
        }

        if (filterListBackward.isEmpty()) {
//            filterListBackward.add(new EpisodeFilter(false));
            filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter()));
            return;
        }

        EpisodeFilter ep = filterListBackward.get(filterListBackward.size() - 1); //ist der letzte
        if (!EpisodeFilterFactory.compareFilter(actFilter, ep)) {

            //nicht den gleichen wieder eintragen
//            if (checkB(ep, actFilter)) {
//                //dann nur austauschen
//                filterListBackward.remove(filterListBackward.size() - 1); //ist der letzte
//                filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter(false)));
//            } else {
            filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter()));
//            }
        }
    }

    public BooleanProperty backwardProperty() {
        return backward;
    }

    public BooleanProperty forwardProperty() {
        return forward;
    }

    private boolean checkB(EpisodeFilter ep, EpisodeFilter act) {
        if (ep.getPodcastId() != act.getPodcastId()) {
            return false;
        }
        if (!ep.getGenre().equals(act.getGenre())) {
            return false;
        }
        if (ep.getTimeRange() != act.getTimeRange()) {
            return true;
        }
        if (!ep.getTitle().equals(act.getTitle())) {
            return true;
        }
        if (!ep.getDescription().equals(act.getDescription())) {
            return true;
        }
        return false;
    }

    public void goBackward() {
        if (filterListBackward.isEmpty()) {
            //dann gibts noch keine
            return;
        }

        while (!filterListBackward.isEmpty()) {
            EpisodeFilter ep = filterListBackward.remove(filterListBackward.size() - 1);//ist die letzte Einstellung, wahrscheinlich die aktuelle
            if (EpisodeFilterFactory.compareFilter(actFilter, ep)) {
                //dann ist die aktuelle Einstellung
                continue;
            }
//            stopInput = true;
            filterListForward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter()));
            setActFilterSettings(ep);
//            stopInput = false;
            break;
        }
    }

    public void goForward() {
        if (filterListForward.isEmpty()) {
            // dann gibts noch keine
            return;
        }

        final EpisodeFilter sf = filterListForward.remove(filterListForward.size() - 1);//ist die letzte Einstellung
        stopInput = true;
        setActFilterSettings(sf);
        stopInput = false;
    }

    private void setActFilterSettings(EpisodeFilter sf) {
        EpisodeFilterFactory.copyFilter(sf, actFilter);
        actFilter.setPredicate(true);
    }
}
