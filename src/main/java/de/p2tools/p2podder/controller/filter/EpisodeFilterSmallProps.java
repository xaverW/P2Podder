/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.controller.filter;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigLongPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.ArrayList;

public class EpisodeFilterSmallProps extends PDataSample<EpisodeFilter> implements Comparable<EpisodeFilter> {

    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final BooleanProperty isAll = new SimpleBooleanProperty(true);//dient nur dazu, dass die anderen ausgeschaltet werden
    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);

    public BooleanProperty[] sfBooleanPropArr = {isAll, isNew, isRunning, wasShown};
    public LongProperty[] sfLongPropArr = {podcastId};

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigLongPropExtra("podcastId", EpisodeFilterToXml.SELECTED_FILTER_PODCAST_ID, podcastId));
        list.add(new ConfigBoolPropExtra("isAll", EpisodeFilterToXml.SELECTED_FILTER_IS_ALL, isAll));
        list.add(new ConfigBoolPropExtra("isRunning", EpisodeFilterToXml.SELECTED_FILTER_IS_RUNNING, isNew));
        list.add(new ConfigBoolPropExtra("finalized", EpisodeFilterToXml.SELECTED_FILTER_IS_FINALIZED, isRunning));
        list.add(new ConfigBoolPropExtra("finalized", EpisodeFilterToXml.SELECTED_FILTER_WAS_SHOWN, wasShown));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public long getPodcastId() {
        return podcastId.get();
    }

    public LongProperty podcastIdProperty() {
        return podcastId;
    }

    public void setPodcastId(long podcastId) {
        this.podcastId.set(podcastId);
    }

    public boolean isIsAll() {
        return isAll.get();
    }

    public BooleanProperty isAllProperty() {
        return isAll;
    }

    public void setIsAll(boolean isAll) {
        this.isAll.set(isAll);
    }

    public boolean isIsNew() {
        return isNew.get();
    }

    public BooleanProperty isNewProperty() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew.set(isNew);
    }

    public boolean isIsRunning() {
        return isRunning.get();
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }

    public boolean isWasShown() {
        return wasShown.get();
    }

    public BooleanProperty wasShownProperty() {
        return wasShown;
    }

    public void setWasShown(boolean wasShown) {
        this.wasShown.set(wasShown);
    }
}
