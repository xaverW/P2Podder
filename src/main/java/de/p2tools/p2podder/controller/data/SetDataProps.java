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
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.PIndex;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SetDataProps extends PDataSample<SetData> {

    public static final String TAG = SetDataFieldNames.TAG;

    private final StringProperty id = new SimpleStringProperty(PIndex.getIndexStr());
    private final StringProperty name = new SimpleStringProperty("Name");
    private final StringProperty progPath = new SimpleStringProperty("");
    private final StringProperty progSwitch = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public SetDataProps() {
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra(SetDataFieldNames.PROGRAMSET_ID, id));
        list.add(new ConfigStringPropExtra(SetDataFieldNames.PROGRAMSET_NAME, name));
        list.add(new ConfigStringPropExtra(SetDataFieldNames.PROGRAMSET_PROGRAM_PATH, progPath));
        list.add(new ConfigStringPropExtra(SetDataFieldNames.PROGRAMSET_PROGRAM_SWITCH, progSwitch));
        list.add(new ConfigStringPropExtra(SetDataFieldNames.PROGRAMSET_DESCRIPTION, description));
        return list.toArray(new Config[]{});
    }


    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getProgPath() {
        return progPath.get();
    }

    public void setProgPath(String progPath) {
        this.progPath.set(progPath);
    }

    public StringProperty progPathProperty() {
        return progPath;
    }

    public String getProgSwitch() {
        return progSwitch.get();
    }

    public void setProgSwitch(String progSwitch) {
        this.progSwitch.set(progSwitch);
    }

    public StringProperty progSwitchProperty() {
        return progSwitch;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(SetData setData) {
        return this.getName().compareTo(setData.getName());
    }
}
