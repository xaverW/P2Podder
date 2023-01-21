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

import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2podder.controller.config.ProgConst;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Date;

public final class EpisodeFilterForwardBackward {

    private final EpisodeFilter actFilter;
    private final BooleanProperty backward = new SimpleBooleanProperty(false);
    private final BooleanProperty forward = new SimpleBooleanProperty(false);
    private boolean stopInput = false;
    private int idx = 0;
    private Date date = new Date();

    // ist die Liste der zuletzt verwendeten Filter
    private final ObservableList<EpisodeFilter> filterListBackward = FXCollections.observableArrayList();

    public EpisodeFilterForwardBackward(EpisodeFilter actFilter) {
        this.actFilter = actFilter;
        filterListBackward.add(new EpisodeFilter());
        filterListBackward.addListener((ListChangeListener<EpisodeFilter>) c -> {
            setIdx(filterListBackward.size() - 1);
        });
    }

    public BooleanProperty backwardProperty() {
        return backward;
    }

    public BooleanProperty forwardProperty() {
        return forward;
    }

    public void addNewFilter() {
        if (stopInput) {
            return;
        }

        while (filterListBackward.size() > ProgConst.EPISODE_FILTER_MAX_BACKWARD_SIZE) {
            filterListBackward.remove(0);
        }
        if (filterListBackward.isEmpty()) {
            filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter()));
            return;
        }

        EpisodeFilter ep = filterListBackward.get(filterListBackward.size() - 1); //ist der letzte
        if (!EpisodeFilterFactory.compareFilter(actFilter, ep)) {
            if (checkIsTheSame(ep, actFilter)) {
                //dann nur austauschen und nicht den gleichen wieder eintragen
                filterListBackward.remove(filterListBackward.size() - 1); //ist der letzte
                filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter(false)));
            } else {
                filterListBackward.add(EpisodeFilterFactory.copyFilter(actFilter, new EpisodeFilter()));
                setIdx(filterListBackward.size() - 1);//User hat neuen Filter geklickt
            }
        }
    }

    public void goBackward() {
        go(true);
    }

    public void goForward() {
        go(false);
    }

    private boolean checkIsTheSame(EpisodeFilter ep, EpisodeFilter act) {
        if (PDateFactory.diffInSeconds(date) > 3) {
            //dann wars ein gewollter Filter
            return false;
        }
        date = new Date();

        if (ep.getPodcastId() != act.getPodcastId()) {
            return false;
        }
        if (!ep.getGenre().equals(act.getGenre())) {
            return false;
        }
        if (ep.getTimeRange() != act.getTimeRange()) {
            if (ep.getTimeRange() == ProgConst.FILTER_TIME_RANGE_ALL_VALUE ||
                    act.getTimeRange() == ProgConst.FILTER_TIME_RANGE_ALL_VALUE) {
                //das erste Mal eintragen
                return false;
            } else {
                return true;
            }
        }
        if (!ep.getTitle().equals(act.getTitle())) {
            if (ep.getTitle().isEmpty() || act.getTitle().isEmpty()) {
                //das erste Mal eintragen
                return false;
            } else {
                return true;
            }
        }

        if (!ep.getDescription().equals(act.getDescription())) {
            if (ep.getDescription().isEmpty() || act.getDescription().isEmpty()) {
                //das erste Mal eintragen
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    private void go(boolean back) {
        if (filterListBackward.isEmpty()) {
            //dann gibts noch keine
            checkButton();
            return;
        }

        if (back) {
            if (idx >= filterListBackward.size()) {
                setIdx(filterListBackward.size() - 1);//auf den letzten Wert setzen
            }
        } else {
            if (idx <= 0) {
                setIdx(0);//ist der "erste" Wert
            }
        }

        while (back ? idx >= 0 : idx < filterListBackward.size()) {
            EpisodeFilter ep = filterListBackward.get(idx);
            if (EpisodeFilterFactory.compareFilter(actFilter, ep)) {
                //dann ist die aktuelle Einstellung
                setIdx(back ? --idx : ++idx);
                continue;
            }
            stopInput = true;
            setActFilterSettings(ep);
            stopInput = false;
            break;
        }
        checkButton();
    }


    private void setIdx(int i) {
        idx = i;
        checkButton();
    }

    private void checkButton() {
        backward.setValue(checkBackward());
        forward.setValue(checkForward());
    }

    private boolean checkBackward() {
        if (idx == 0) {
            //dann sind wir schon am Ende
            return false;
        }
        return check();
    }

    private boolean checkForward() {
        if (idx >= filterListBackward.size() - 1) {
            //dann sind wir schon wieder am Anfang
            return false;
        }
        return check();
    }

    private boolean check() {
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

    private void setActFilterSettings(EpisodeFilter sf) {
        EpisodeFilterFactory.copyFilter(sf, actFilter);
        actFilter.setPredicate(false);
    }
}
