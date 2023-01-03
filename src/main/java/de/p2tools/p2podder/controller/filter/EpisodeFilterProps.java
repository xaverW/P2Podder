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

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2podder.controller.config.ProgConst;
import javafx.beans.property.*;

import java.util.ArrayList;

public class EpisodeFilterProps extends PDataSample<EpisodeFilter> implements Comparable<EpisodeFilter> {

    public static String TAG = "SelectedFilter";
    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final IntegerProperty timeRange = new SimpleIntegerProperty(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
    private final BooleanProperty isAll = new SimpleBooleanProperty(true);//dient nur dazu, dass die anderen ausgeschaltet werden
    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
    private final BooleanProperty isStartet = new SimpleBooleanProperty(false);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);

    public BooleanProperty[] sfBooleanPropArr = {isAll, isNew, isStartet, isRunning, wasShown};
    public StringProperty[] sfStringPropArr = {genre, title, description};
    public IntegerProperty[] sfIntegerPropArr = {timeRange};
    public LongProperty[] sfLongPropArr = {podcastId};

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_longProp("podcastId", EpisodeFilterToXml.SELECTED_FILTER_PODCAST_ID, podcastId));
        list.add(new Config_stringProp("genre", EpisodeFilterToXml.SELECTED_FILTER_GENRE, genre));
        list.add(new Config_stringProp("title", EpisodeFilterToXml.SELECTED_FILTER_TITLE, title));
        list.add(new Config_stringProp("description", EpisodeFilterToXml.SELECTED_FILTER_DESCRIPTION, description));
        list.add(new Config_intProp("timeRange", EpisodeFilterToXml.SELECTED_FILTER_TIME_RANGE, timeRange));
        list.add(new Config_boolProp("isAll", EpisodeFilterToXml.SELECTED_FILTER_IS_ALL, isAll));
        list.add(new Config_boolProp("isNew", EpisodeFilterToXml.SELECTED_FILTER_IS_NEW, isNew));
        list.add(new Config_boolProp("isStarted", EpisodeFilterToXml.SELECTED_FILTER_IS_STARTED, isStartet));
        list.add(new Config_boolProp("isRunning", EpisodeFilterToXml.SELECTED_FILTER_IS_RUNNING, isRunning));
        list.add(new Config_boolProp("wasShown", EpisodeFilterToXml.SELECTED_FILTER_WAS_SHOWN, wasShown));

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

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
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

    public int getTimeRange() {
        return timeRange.get();
    }

    public IntegerProperty timeRangeProperty() {
        return timeRange;
    }

    public void setTimeRange(int timeRange) {
        this.timeRange.set(timeRange);
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

    public boolean isIsStartet() {
        return isStartet.get();
    }

    public BooleanProperty isStartetProperty() {
        return isStartet;
    }

    public void setIsStartet(boolean isStartet) {
        this.isStartet.set(isStartet);
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

    @Override
    public String toString() {
        return title.getValue();
    }

    @Override
    public int compareTo(EpisodeFilter o) {
        return title.getValue().compareTo(o.getTitle());
    }
}
