/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2podder.gui.filter.SelectedFilter;

import java.util.ArrayList;

public class SelectedFilterFactory {

//    private final LongProperty podcastId = new SimpleLongProperty(0);
//    private final StringProperty genre = new SimpleStringProperty("");
//    private final StringProperty title = new SimpleStringProperty("");
//    private final IntegerProperty timeRange = new SimpleIntegerProperty(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
//    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
//    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
//    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);

    public static SelectedFilter getFilterCopy(SelectedFilter sfFrom) {
        SelectedFilter sf = new SelectedFilter();
        copyFilter(sfFrom, sf);
        return sf;
    }

    public static void copyFilter(SelectedFilter sfFrom, SelectedFilter sfTo) {
        sfTo.setName(sfFrom.getName());
        sfTo.setPodcastId(sfFrom.getPodcastId());
        sfTo.setGenre(sfFrom.getGenre());
        sfTo.setTitle(sfFrom.getTitle());
        sfTo.setTimeRange(sfFrom.getTimeRange());
        sfTo.setIsNew(sfFrom.isIsNew());
        sfTo.setIsRunning(sfFrom.isIsRunning());
        sfTo.setWasShown(sfFrom.isWasShown());
    }

    public static boolean compareFilterWithoutNameOfFilter(SelectedFilter sfFrom, SelectedFilter sfTo) {
        if (sfFrom == null || sfTo == null) {
            return false;
        }

        for (int i = 0; i < sfFrom.sfBooleanPropArr.length; i++) {
            if (sfFrom.sfBooleanPropArr[i].get() != sfTo.sfBooleanPropArr[i].get()) {
                return false;
            }
        }
        for (int i = 0; i < sfFrom.sfIntegerPropArr.length; i++) {
            if (sfFrom.sfIntegerPropArr[i].get() != sfTo.sfIntegerPropArr[i].get()) {
                return false;
            }
        }
        for (int i = 0; i < sfFrom.sfLongPropArr.length; i++) {
            if (sfFrom.sfLongPropArr[i].get() != sfTo.sfLongPropArr[i].get()) {
                return false;
            }
        }
        // nur die Filter (nicht den Namen) vergleichen
        for (int i = 1; i < sfFrom.sfStringPropArr.length; i++) {
            if (!sfFrom.sfStringPropArr[i].getValueSafe().equals(sfTo.sfStringPropArr[i].getValueSafe())) {
                return false;
            }
        }

        return true;
    }

//    private final LongProperty podcastId = new SimpleLongProperty(0);
//    private final StringProperty genre = new SimpleStringProperty("");
//    private final StringProperty title = new SimpleStringProperty("");
//    private final IntegerProperty timeRange = new SimpleIntegerProperty(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
//    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
//    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
//    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);


    public static ArrayList<String> printFilter(SelectedFilter sf) {
        ArrayList<String> list = new ArrayList<>();
        list.add("name " + sf.getName());
        list.add(" ");
        list.add("podcastId " + sf.getPodcastId());
        list.add("genre " + sf.getGenre());
        list.add("title " + sf.getTitle());
        list.add("timeRange " + sf.getTimeRange());
        list.add("isNew " + sf.isIsNew());
        list.add("isRunning " + sf.isIsRunning());
        list.add("wasShown " + sf.isWasShown());
        return list;
    }
}
