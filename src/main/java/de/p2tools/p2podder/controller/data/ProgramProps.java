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
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ProgramProps extends PDataSample<ProgramData> {

    public static final String TAG = "Programm"; //ist im Set zum Download so festgesetzt

    public static final int PROGRAM_NAME = 0;
    public static final int PROGRAM_PROGRAMPATH = 1;
    public static final int PROGRAM_SCHALTER = 2;
    public static final int PROGRAM_PRAEFIX = 3;
    public static final int PROGRAM_SUFFIX = 4;
    public static final int PROGRAM_RESTART = 5;

    public static final int MAX_ELEM = 6;
    public static final String[] COLUMN_NAMES = {"Beschreibung", "Programm",
            "Schalter", "Präfix", "Suffix", "Restart"};
    public static final String[] XML_NAMES = {"Programmname", "Programmpfad",
            "Programmschalter", "Praefix", "Suffix", "Restart"}; //ist im Set zum Download so festgesetzt

    public String[] arr;

    private StringProperty name = new SimpleStringProperty("");
    private StringProperty progPath = new SimpleStringProperty("");
    private StringProperty progSwitch = new SimpleStringProperty("");
    private StringProperty praefix = new SimpleStringProperty("");
    private StringProperty suffix = new SimpleStringProperty("");
    //    private BooleanProperty restart = new SimpleBooleanProperty(false);
    private StringProperty destName = new SimpleStringProperty("");


    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        return new Config[]{
                new ConfigStringPropExtra("name", ProgramDataFieldNames.PROGRAM_NAME, name),
                new ConfigStringPropExtra("progPath", ProgramDataFieldNames.PROGRAM_PROGRAM_PATH, progPath),
                new ConfigStringPropExtra("progSwitch", ProgramDataFieldNames.PROGRAM_SWITCH, progSwitch),
                new ConfigStringPropExtra("praefix", ProgramDataFieldNames.PROGRAM_PRAEFIX, praefix),
                new ConfigStringPropExtra("suffix", ProgramDataFieldNames.PROGRAM_SUFFIX, suffix),
                new ConfigStringPropExtra("destName", ProgramDataFieldNames.PROGRAM_DEST_NAME, destName),
//                new ConfigBoolPropExtra("restart", ProgramDataFieldNames.PROGRAM_RESTART, restart),
        };
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getProgPath() {
        return progPath.get();
    }

    public StringProperty progPathProperty() {
        return progPath;
    }

    public void setProgPath(String progPath) {
        this.progPath.set(progPath);
    }

    public String getProgSwitch() {
        return progSwitch.get();
    }

    public StringProperty progSwitchProperty() {
        return progSwitch;
    }

    public void setProgSwitch(String progSwitch) {
        this.progSwitch.set(progSwitch);
    }

    public String getPraefix() {
        return praefix.get();
    }

    public StringProperty praefixProperty() {
        return praefix;
    }

    public void setPraefix(String praefix) {
        this.praefix.set(praefix);
    }

    public String getSuffix() {
        return suffix.get();
    }

    public StringProperty suffixProperty() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix.set(suffix);
    }

    public String getDestName() {
        return destName.get();
    }

    public StringProperty destNameProperty() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName.set(destName);
    }
//    public boolean isRestart() {
//        return restart.get();
//    }
//
//    public BooleanProperty restartProperty() {
//        return restart;
//    }
//
//    public void setRestart(boolean restart) {
//        this.restart.set(restart);
//    }

    public ProgramProps() {
        makeArr();
    }

    @Override
    public String toString() {
        setXmlFromProps();
        String ret = "";
        for (int i = 0; i < MAX_ELEM; ++i) {
            if (i == 0) {
                ret += "| ***|" + COLUMN_NAMES[i] + ": " + arr[i] + P2LibConst.LINE_SEPARATOR;
            } else {
                ret += "|    |" + COLUMN_NAMES[i] + ": " + arr[i] + P2LibConst.LINE_SEPARATOR;
            }
        }
        return ret;
    }

    public void getStringDescription(ArrayList<String> list) {
        setXmlFromProps();
        for (int i = 0; i < MAX_ELEM; ++i) {
            if (i == 0) {
                list.add("     | " + COLUMN_NAMES[i] + ": " + arr[i]);
            } else {
                list.add("     | " + COLUMN_NAMES[i] + ": " + arr[i]);
            }
        }
    }

    //===================================
    // Private
    //===================================
    private void makeArr() {
        arr = new String[MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = "";
        }
    }

    public void setPropsFromXml() {
        setName(arr[PROGRAM_NAME]);
        setProgPath(arr[PROGRAM_PROGRAMPATH]);
        setProgSwitch(arr[PROGRAM_SCHALTER]);
        setPraefix(arr[PROGRAM_PRAEFIX]);
        setSuffix(arr[PROGRAM_SUFFIX]);
//        setRestart(Boolean.parseBoolean(arr[PROGRAM_RESTART]));
    }

    public void setXmlFromProps() {
        arr[PROGRAM_NAME] = getName();
        arr[PROGRAM_PROGRAMPATH] = getProgPath();
        arr[PROGRAM_SCHALTER] = getProgSwitch();
        arr[PROGRAM_PRAEFIX] = getPraefix();
        arr[PROGRAM_SUFFIX] = getSuffix();
//        arr[PROGRAM_RESTART] = String.valueOf(isRestart());
    }
}
