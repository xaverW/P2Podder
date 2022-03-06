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

package de.p2tools.p2podder.gui.filter;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2podder.controller.config.ProgConst;
import javafx.beans.property.*;

import java.util.ArrayList;

public class SelectedFilterProps extends PDataSample<SelectedFilter> implements Comparable<SelectedFilter> {

    public static String TAG = "SelectedFilter";

    private final StringProperty name = new SimpleStringProperty();

    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final IntegerProperty timeRange = new SimpleIntegerProperty(ProgConst.FILTER_TIME_RANGE_ALL_VALUE);
    private final BooleanProperty isNew = new SimpleBooleanProperty(false);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty wasShown = new SimpleBooleanProperty(false);


    public BooleanProperty[] sfBooleanPropArr = {isNew, isRunning, wasShown};
    public StringProperty[] sfStringPropArr = {name, genre, title};
    public IntegerProperty[] sfIntegerPropArr = {timeRange};
    public LongProperty[] sfLongPropArr = {podcastId};

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("name", FilterToXml.SELECTED_FILTER_GENRE, name));
        list.add(new ConfigLongPropExtra("podcastId", FilterToXml.SELECTED_FILTER_PODCAST_ID, podcastId));
        list.add(new ConfigStringPropExtra("genre", FilterToXml.SELECTED_FILTER_GENRE, genre));
        list.add(new ConfigStringPropExtra("title", FilterToXml.SELECTED_FILTER_TITLE, title));
        list.add(new ConfigIntPropExtra("timeRange", FilterToXml.SELECTED_FILTER_TIME_RANGE, timeRange));
        list.add(new ConfigBoolPropExtra("isRunning", FilterToXml.SELECTED_FILTER_IS_RUNNING, isNew));
        list.add(new ConfigBoolPropExtra("finalized", FilterToXml.SELECTED_FILTER_IS_FINALIZED, isRunning));
        list.add(new ConfigBoolPropExtra("finalized", FilterToXml.SELECTED_FILTER_IS_FINALIZED, wasShown));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
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

    public int getTimeRange() {
        return timeRange.get();
    }

    public IntegerProperty timeRangeProperty() {
        return timeRange;
    }

    public void setTimeRange(int timeRange) {
        this.timeRange.set(timeRange);
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

    @Override
    public String toString() {
        return title.getValue();
    }

    @Override
    public int compareTo(SelectedFilter o) {
        return title.getValue().compareTo(o.getTitle());
    }
}
