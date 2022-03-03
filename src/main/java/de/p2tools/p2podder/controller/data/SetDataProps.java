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

import de.p2tools.p2Lib.configFile.config.*;
import javafx.beans.property.*;

import java.util.ArrayList;

public class SetDataProps extends SetDataBase {

    final ProgramList programList = new ProgramList();

    private StringProperty id = new SimpleStringProperty("");
    private StringProperty visibleName = new SimpleStringProperty("");
    private StringProperty prefix = new SimpleStringProperty("");
    private StringProperty suffix = new SimpleStringProperty("");
    private BooleanProperty play = new SimpleBooleanProperty(false);//ist das Standard-Set
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty adOn = new SimpleStringProperty("");
    private StringProperty destPath = new SimpleStringProperty("");
    private StringProperty destName = new SimpleStringProperty("");
    private IntegerProperty maxSize = new SimpleIntegerProperty(0);
    private IntegerProperty maxField = new SimpleIntegerProperty(0);

    public SetDataProps() {
    }

    public boolean addProg(ProgramData prog) {
        return programList.add(prog);
    }

    public ProgramList getProgramList() {
        return programList;
    }

    public ProgramData getProg(int i) {
        return programList.get(i);
    }

    public boolean progsContainPath() {
        // ein Programmschalter mit
        // "**" (Pfad/Datei) oder %a (Pfad) oder %b (Datei)
        // damit ist es ein Set zum Speichern
        boolean ret = false;

        for (ProgramData progData : programList) {
            if (progData.getProgSwitch().contains("**")
                    || progData.getProgSwitch().contains("%a")
                    || progData.getProgSwitch().contains("%b")) {
                ret = true;
                break;
            }
        }
        return ret;
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
        list.add(new ConfigStringPropExtra("praefixDirect", SetDataFieldNames.PRAEFIX_DIRECT, prefix));
        list.add(new ConfigStringPropExtra("suffixDirect", SetDataFieldNames.SUFFIX_DIRECT, suffix));
        list.add(new ConfigBoolPropExtra("isPlay", SetDataFieldNames.IS_PLAY, play));
        list.add(new ConfigStringPropExtra("description", SetDataFieldNames.DESCRIPTION, description));
        list.add(new ConfigStringPropExtra("adOn", SetDataFieldNames.ADD_ON, adOn));
        list.add(new ConfigStringPropExtra("destPath", SetDataFieldNames.DEST_PATH, destPath));
        list.add(new ConfigStringPropExtra("destName", SetDataFieldNames.DEST_NAME, destName));
        list.add(new ConfigIntPropExtra("maxSize", SetDataFieldNames.MAX_SIZE, maxSize));
        list.add(new ConfigIntPropExtra("maxField", SetDataFieldNames.MAX_FIELD, maxField));
        list.add(new ConfigPDataList(programList));

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

    public String getPrefix() {
        return prefix.get();
    }

    public StringProperty prefixProperty() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix.set(prefix);
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

    public boolean isPlay() {
        return play.get();
    }

    public BooleanProperty playProperty() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play.set(play);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getAdOn() {
        return adOn.get();
    }

    public StringProperty adOnProperty() {
        return adOn;
    }

    public void setAdOn(String adOn) {
        this.adOn.set(adOn);
    }

    public String getDestPath() {
        return destPath.get();
    }

    public StringProperty destPathProperty() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath.set(destPath);
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

    public int getMaxSize() {
        return maxSize.get();
    }

    public IntegerProperty maxSizeProperty() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize.set(maxSize);
    }

    public int getMaxField() {
        return maxField.get();
    }

    public IntegerProperty maxFieldProperty() {
        return maxField;
    }

    public void setMaxField(int maxField) {
        this.maxField.set(maxField);
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
