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


package de.p2tools.p2podder.controller.data;

public class SetDataFieldNames {

    public static final String TAG = "programSet";

    public static final String PROGRAMSET_ID = "setId";
    public static final String PROGRAMSET_NAME = "name";
    public static final String PROGRAMSET_PROGRAM_PATH = "progPath";
    public static final String PROGRAMSET_PROGRAM_SWITCH = "progSwitch";
    public static final String PROGRAMSET_DESCRIPTION = "description";

    public static final int PROGRAMSET_ID_INT = 0;
    public static final int PROGRAMSET_VISIBLE_NAME_INT = 1;
    public static final int PROGRAMSET_PROGRAM_PATH_INT = 2;
    public static final int PROGRAMSET_PROGRAM_SWITCH_INT = 3;
    public static final int PROGRAMSET_DESCRIPTION_INT = 4;

    public static final int MAX_ELEM = 5;

    public static final String[] COLUMN_NAMES = {PROGRAMSET_ID, PROGRAMSET_NAME,
            PROGRAMSET_PROGRAM_PATH, PROGRAMSET_PROGRAM_SWITCH,
            PROGRAMSET_DESCRIPTION};


    private SetDataFieldNames() {
    }
}
