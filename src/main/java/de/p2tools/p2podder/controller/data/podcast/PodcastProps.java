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

package de.p2tools.p2podder.controller.data.podcast;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import javafx.beans.property.*;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.ArrayList;

public class PodcastProps extends PDataSample<Podcast> {

    public static final String TAG = "Podcast";

    static final FastDateFormat sdf_date = FastDateFormat.getInstance("dd.MM.yyyy");
    private final IntegerProperty no = new SimpleIntegerProperty();
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty("");
    private final BooleanProperty active = new SimpleBooleanProperty(true);
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final IntegerProperty number = new SimpleIntegerProperty();
    private final StringProperty date = new SimpleStringProperty("");
    private final StringProperty website = new SimpleStringProperty("");
    private final StringProperty url = new SimpleStringProperty("");


    public PLocalDate podcastDate = new PLocalDate();

    @Override
    public ConfigExtra[] getConfigsArr() {
        ArrayList<ConfigExtra> list = new ArrayList<>();
        list.add(new ConfigLongPropExtra("id", PodcastNames.PODCAST_ID, id));
        list.add(new ConfigStringPropExtra("name", PodcastNames.PODCAST_NAME, name));
        list.add(new ConfigBoolPropExtra("active", PodcastNames.PODCAST_ACTIVE, active));
        list.add(new ConfigStringPropExtra("genre", PodcastNames.PODCAST_GENRE, genre));
        list.add(new ConfigStringPropExtra("description", PodcastNames.PODCAST_DESCRIPTION, description));
        list.add(new ConfigIntPropExtra("number", PodcastNames.PODCAST_NUMBER, number));
        list.add(new ConfigStringPropExtra("date", PodcastNames.PODCAST_DATE, date));
        list.add(new ConfigStringPropExtra("website", PodcastNames.PODCAST_WEBSITE, website));
        list.add(new ConfigStringPropExtra("url", PodcastNames.PODCAST_URL, url));

        return list.toArray(new ConfigExtra[]{});
    }

    public final Property[] properties = {no, id, name, active, genre,
            description, number, date, website, url};

    @Override
    public String getTag() {
        return TAG;
    }

    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.setValue(no);
    }

    public IntegerProperty noProperty() {
        return no;
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
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

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getWebsite() {
        return website.get();
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
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
    public int compareTo(Podcast arg0) {
        int ret;
        if ((ret = sorter.compare(getName(), arg0.getName())) == 0) {
            return sorter.compare(getUrl(), arg0.getUrl());
        }
        return ret;
    }

}
