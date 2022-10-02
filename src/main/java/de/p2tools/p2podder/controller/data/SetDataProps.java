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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SetDataProps extends SetDataBase {

    private StringProperty id = new SimpleStringProperty("");
    private StringProperty visibleName = new SimpleStringProperty("");
    private BooleanProperty standardSet = new SimpleBooleanProperty(false);//ist das Standard-Set
    private StringProperty programPath = new SimpleStringProperty("");
    private StringProperty programSwitch = new SimpleStringProperty("");

    public SetDataProps() {
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("id", SetDataFieldNames.ID, id));
        list.add(new ConfigStringPropExtra("visibleName", SetDataFieldNames.VISIBLE_NAME, visibleName));
        list.add(new ConfigBoolPropExtra("standardSet", SetDataFieldNames.STANARD_SET, standardSet));
        list.add(new ConfigStringPropExtra("programPath", SetDataFieldNames.PROGRAM_PATH, programPath));
        list.add(new ConfigStringPropExtra("programSwitch", SetDataFieldNames.PROGRAM_SWITCH, programSwitch));

        return list.toArray(new Config[]{});
    }


    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getVisibleName() {
        return visibleName.get();
    }

    public StringProperty visibleNameProperty() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName.set(visibleName);
    }

    public boolean isStandardSet() {
        return standardSet.get();
    }

    public BooleanProperty standardSetProperty() {
        return standardSet;
    }

    public void setStandardSet(boolean standardSet) {
        this.standardSet.set(standardSet);
    }

    public String getProgramPath() {
        return programPath.get();
    }

    public StringProperty programPathProperty() {
        return programPath;
    }

    public void setProgramPath(String programPath) {
        this.programPath.set(programPath);
    }

    public String getProgramSwitch() {
        return programSwitch.get();
    }

    public StringProperty programSwitchProperty() {
        return programSwitch;
    }

    public void setProgramSwitch(String programSwitch) {
        this.programSwitch.set(programSwitch);
    }

    @Override
    public String toString() {
        return getVisibleName();
    }

    @Override
    public int compareTo(SetData setData) {
        return this.getVisibleName().compareTo(setData.getVisibleName());
    }
}
