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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PIndex;

public class SetData extends SetDataProps {

    public SetData() {
        setId(PIndex.getIndexStr());
    }

    public SetData(String name) {
        //neue Pset sind immer gleich Button
        setId(PIndex.getIndexStr());
        setVisibleName(name);
    }

    public boolean isEmpty() {
        boolean ret = true;
        Config[] configs = getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            if (!configs[i].getActValueString().isEmpty()) {
                ret = false;
            }
        }
        return ret;
    }

    public SetData copy() {
        final SetData ret = new SetData();
        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        ret.setId(PIndex.getIndexStr()); //es darf nur einen geben!
        ret.setVisibleName("Kopie-" + getVisibleName());
        return ret;
    }

    public String setDataToString() {
        String ret = "";
        ret += "================================================" + P2LibConst.LINE_SEPARATOR;
        ret += "| Programmset" + P2LibConst.LINE_SEPARATOR;

        Config[] configs = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            ret += "     | " + configs[i].getName() + ": " + configs[i].getActValueString() + P2LibConst.LINE_SEPARATOR;
        }
        ret += "     |_______________________________________________" + P2LibConst.LINE_SEPARATOR;
        return ret;
    }

//    public void setPropsFromXml() {
//        setId(arr[SET_ID]);
//        setName(arr[SET_NAME]);
//        setDescription(arr[SET_DESCRIPTION]);
//        setProgramPath(arr[SET_PROGRAM_PATH]);
//        setProgramSwitch(arr[SET_PROGRAM_SWITCH]);
//    }
}
