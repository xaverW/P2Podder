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

package de.p2tools.p2podder.controller.storedFilter;

import de.p2tools.p2podder.controller.config.ProgData;

public class InitStoredFilter {

    public static void initFilter() {
        ProgData progData = ProgData.getInstance();

        //========================================================
        SelectedFilter sf = new SelectedFilter("Nachrichten");
        sf.setTitle("Nachrichten");
        progData.storedFilters.getStoredFilterList().add(sf);

        //========================================================
        sf = new SelectedFilter("Linux");
        sf.setTitle("Linux");
        progData.storedFilters.getStoredFilterList().add(sf);
    }
}