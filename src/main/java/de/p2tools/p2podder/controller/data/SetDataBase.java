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

package de.p2tools.p2podder.controller.data;

import de.p2tools.p2Lib.configFile.pData.PDataSample;

public class SetDataBase extends PDataSample<SetData> {
    public static final int SET_ID = 0;
    public static final int SET_NAME = 1;
    public static final int SET_DESCRIPTION = 2;
    public static final int SET_STANDARD_SET = 3;
    public static final int SET_PROGRAM_PATH = 4;
    public static final int SET_PROGRAM_SWITCH = 5;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STANDARD_SET = "standard";
    public static final String PROGRAM_PATH = "programPath";
    public static final String PROGRAM_SWITCH = "programSwitch";

    public static final int MAX_ELEM = 6;
    public static final String[] XML_NAMES = {ID, NAME, DESCRIPTION, STANDARD_SET, PROGRAM_PATH,
            PROGRAM_SWITCH}; //ist im Set zum Download so festgesetzt

    public String[] arr;
    public static final String TAG = "setData"; //ist im Set zum Download so festgesetzt

    public SetDataBase() {
        makeArray();
    }

    void makeArray() {
        arr = new String[MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = "";
        }
        arr[SET_STANDARD_SET] = Boolean.toString(false);
    }
}
