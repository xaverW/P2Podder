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

package de.p2tools.p2podder.tools.storedFilter;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SelectedFilterProps extends PDataSample<SelectedFilter> {
    public static final String TAG = "SelectedFilter";

    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty titleVis = new SimpleBooleanProperty(false);
    private final StringProperty title = new SimpleStringProperty();
    private final BooleanProperty genreVis = new SimpleBooleanProperty(true);
    private final StringProperty genre = new SimpleStringProperty();
    private final BooleanProperty urlVis = new SimpleBooleanProperty(false);
    private final StringProperty url = new SimpleStringProperty();

    public BooleanProperty[] sfBooleanPropArr = {genreVis, titleVis,
            urlVis};

    public StringProperty[] sfStringPropArr = {name, title, genre, url};

    @Override
    public String getComment() {
        return "aktuelle Filtereinstellungen";
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("name", SelectedFilterFieldNames.NAME, name));
        list.add(new ConfigBoolPropExtra("episodeTitleVis", SelectedFilterFieldNames.STATION_NAME_VIS, titleVis));
        list.add(new ConfigStringPropExtra("episodeTitle", SelectedFilterFieldNames.STATION_NAME, title));
        list.add(new ConfigBoolPropExtra("genreVis", SelectedFilterFieldNames.GENRE_VIS, genreVis));
        list.add(new ConfigStringPropExtra("genre", SelectedFilterFieldNames.GENRE, genre));
        list.add(new ConfigBoolPropExtra("urlVis", SelectedFilterFieldNames.URL_VIS, urlVis));
        list.add(new ConfigStringPropExtra("url", SelectedFilterFieldNames.URL, url));

        return list.toArray(new Config[]{});
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

    public boolean getTitleVis() {
        return titleVis.get();
    }

    public BooleanProperty titleVisProperty() {
        return titleVis;
    }

    public void setTitleVis(boolean titleVis) {
        this.titleVis.set(titleVis);
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

    public boolean isGenreVis() {
        return genreVis.get();
    }

    public BooleanProperty genreVisProperty() {
        return genreVis;
    }

    public void setGenreVis(boolean genreVis) {
        this.genreVis.set(genreVis);
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

    public boolean isUrlVis() {
        return urlVis.get();
    }

    public BooleanProperty urlVisProperty() {
        return urlVis;
    }

    public void setUrlVis(boolean urlVis) {
        this.urlVis.set(urlVis);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    @Override
    public int compareTo(SelectedFilter setData) {
        return this.getName().compareTo(setData.getName());
    }
}
