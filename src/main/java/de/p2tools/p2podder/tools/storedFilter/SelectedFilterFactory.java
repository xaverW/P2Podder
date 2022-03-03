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

import java.util.ArrayList;

public class SelectedFilterFactory {

    public static SelectedFilter getFilterCopy(SelectedFilter sfFrom) {
        SelectedFilter sf = new SelectedFilter();
        copyFilter(sfFrom, sf);
        return sf;
    }

    public static void copyFilter(SelectedFilter sfFrom, SelectedFilter sfTo) {
        sfTo.setName(sfFrom.getName());

        sfTo.setGenreVis(sfFrom.isGenreVis());
        sfTo.setGenre(sfFrom.getGenre());
        sfTo.setTitleVis(sfFrom.getTitleVis());
        sfTo.setTitle(sfFrom.getTitle());
        sfTo.setUrlVis(sfFrom.isUrlVis());
        sfTo.setUrl(sfFrom.getUrl());
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
        // nur die Filter (nicht den Namen) vergleichen
        for (int i = 1; i < sfFrom.sfStringPropArr.length; i++) {
            if (!sfFrom.sfStringPropArr[i].getValueSafe().equals(sfTo.sfStringPropArr[i].getValueSafe())) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<String> printFilter(SelectedFilter sf) {
        ArrayList<String> list = new ArrayList<>();
        list.add("getName " + sf.getName());
        list.add("");
        list.add("isGenre " + sf.isGenreVis());
        list.add("getGenre " + sf.getGenre());
        list.add("isStationNameVis " + sf.getTitleVis());
        list.add("getStationName " + sf.getTitle());
        list.add("isUrlVis " + sf.isUrlVis());
        list.add("getUrl " + sf.getUrl());
        return list;
    }
}
