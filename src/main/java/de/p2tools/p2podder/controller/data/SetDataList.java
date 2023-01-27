/*
 *  Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.p2Lib.dialogs.PDialogFileChosser;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgData;

import java.util.ArrayList;
import java.util.regex.Matcher;

@SuppressWarnings("serial")
public class SetDataList extends SetDataListProps {

    public boolean addSetData(SetData setData) {
        //add and notify
        final boolean ret = super.add(setData);
        setListChanged();
        return ret;
    }

    public boolean addSetData(SetDataList setDataList) {
        //add and notify
        boolean ret = true;
        for (final SetData entry : setDataList) {
            if (!addSetData(entry)) {
                ret = false;
            }
        }
        setListChanged();
        return ret;
    }

    public static boolean progReplacePattern(SetDataList list) {
        boolean ret = true;
        for (final SetData pSet : list) {
            if (!progReplacePattern(pSet)) {
                ret = false;
            }
        }
        return ret;
    }

    public boolean removeSetData(Object obj) {
        //remove and notify
        boolean ret = super.remove(obj);
        setListChanged();
        return ret;
    }

    private static boolean progReplacePattern(SetData pSet) {
        String vlc = "";
        // damit nur die Variablen abgefragt werden, die auch verwendet werden
        if (pSet.getProgPath().contains(PATTERN_PATH_VLC) || pSet.getProgSwitch().contains(PATTERN_PATH_VLC)) {
            vlc = getPathVlc();

            pSet.setProgPath(pSet.getProgPath().replaceAll(PATTERN_PATH_VLC, Matcher.quoteReplacement(vlc)));
            pSet.setProgSwitch(pSet.getProgSwitch().replaceAll(PATTERN_PATH_VLC, Matcher.quoteReplacement(vlc)));
        }
        return true;
    }

    private static String getPathVlc() {
        //liefert den Pfad, wenn vorhanden, wenn nicht, wird er in einem Dialog abgefragt
        if (ProgConfig.SYSTEM_PATH_VLC.get().isEmpty()) {
            ProgConfig.SYSTEM_PATH_VLC.setValue(PDialogFileChosser.showFileChooser(ProgData.getInstance().primaryStage, "VLC",
                    "VLC wird nicht gefunden.", "Bitte den Pfad zum" + P2LibConst.LINE_SEPARATOR +
                            "VLC-Player angeben.", false, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView()));
        }
        return ProgConfig.SYSTEM_PATH_VLC.get();
    }

    public SetData getSetDataPlay() {
        //liefert die Standard-Programmgruppe zum Abspielen
        if (!this.isEmpty()) {
            SetData setData = this.get(0);
            return setData;
        }
        return null;
    }

    public SetDataList getSetDataListButton() {
        //liefert eine Liste Programmsets, die als Button angelegt sind
        //sind jetzt alle
        if (this.isEmpty()) {
            return new SetDataList();
        }
        return this;
    }

    public int up(int idx, boolean up) {
        final SetData prog = this.remove(idx);
        int newIdx = idx;
        if (up) {
            if (newIdx > 0) {
                --newIdx;
            }
        } else if (newIdx < size()) {
            ++newIdx;
        }
        this.add(newIdx, prog);
        setListChanged();
        return newIdx;
    }

    public ArrayList<String> getStringListSetData() {
        final ArrayList<String> list = new ArrayList<>();
        for (SetData setData : this) {
            list.add("     |======================================");
            Config[] configs = setData.getConfigsArr();
            for (int i = 0; i < configs.length; ++i) {
                list.add("     | " + configs[i].getName() + ": " + configs[i].getActValueString());
            }
            list.add("     |_____________________________");
            list.add("");
        }
        return list;
    }
}
