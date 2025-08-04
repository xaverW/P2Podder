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

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_boolProp;
import de.p2tools.p2lib.configfile.config.Config_intProp;
import de.p2tools.p2lib.configfile.config.Config_longProp;
import de.p2tools.p2lib.configfile.pdata.P2DataSample;
import de.p2tools.p2podder.controller.config.ProgConst;
import javafx.beans.property.*;

import java.util.ArrayList;

public class EpisodeFilterSmallProps extends P2DataSample<EpisodeFilter> implements Comparable<EpisodeFilter> {

    public static String TAG = "SelectedSmallFilter";

    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final BooleanProperty isAll = new SimpleBooleanProperty(true);//dient nur dazu, dass die anderen ausgeschaltet werden
    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
    private final BooleanProperty isStarted = new SimpleBooleanProperty(false);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);
    private final IntegerProperty durationMin = new SimpleIntegerProperty(ProgConst.FILTER_DURATION_MIN_VALUE);
    private final IntegerProperty durationMax = new SimpleIntegerProperty(ProgConst.FILTER_DURATION_MAX_MINUTE);

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_longProp("podcastId", EpisodeFilterToXml.SELECTED_FILTER_PODCAST_ID, podcastId));
        list.add(new Config_boolProp("isAll", EpisodeFilterToXml.SELECTED_FILTER_IS_ALL, isAll));
        list.add(new Config_boolProp("isNew", EpisodeFilterToXml.SELECTED_FILTER_IS_NEW, isNew));
        list.add(new Config_boolProp("isStarted", EpisodeFilterToXml.SELECTED_FILTER_IS_STARTED, isStarted));
        list.add(new Config_boolProp("isRunning", EpisodeFilterToXml.SELECTED_FILTER_IS_RUNNING, isRunning));
        list.add(new Config_boolProp("wasShown", EpisodeFilterToXml.SELECTED_FILTER_WAS_SHOWN, wasShown));
        list.add(new Config_intProp("durationMin", durationMin));
        list.add(new Config_intProp("durationMax", durationMax));
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

    public boolean isIsStarted() {
        return isStarted.get();
    }

    public BooleanProperty isStartedProperty() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted.set(isStarted);
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

    public int getDurationMin() {
        return durationMin.get();
    }

    public IntegerProperty durationMinProperty() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin.set(durationMin);
    }

    public int getDurationMax() {
        return durationMax.get();
    }

    public IntegerProperty durationMaxProperty() {
        return durationMax;
    }

    public void setDurationMax(int durationMax) {
        this.durationMax.set(durationMax);
    }
}
