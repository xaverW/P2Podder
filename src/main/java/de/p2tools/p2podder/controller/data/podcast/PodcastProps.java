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

import de.p2tools.p2lib.configfile.config.*;
import de.p2tools.p2lib.configfile.pdata.PDataSample;
import de.p2tools.p2lib.tools.date.PLDateFactory;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class PodcastProps extends PDataSample<Podcast> {

    public static final String TAG = "Podcast";

    private final IntegerProperty no = new SimpleIntegerProperty();
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty("");
    private final BooleanProperty active = new SimpleBooleanProperty(true);
    private final StringProperty genre = new SimpleStringProperty("Nachrichten");
    private final StringProperty description = new SimpleStringProperty("");
    private final IntegerProperty amountEpisodes = new SimpleIntegerProperty();
    private final IntegerProperty maxAge = new SimpleIntegerProperty();
    private final StringProperty website = new SimpleStringProperty("");
    private final StringProperty url = new SimpleStringProperty("");
    private LocalDate genDate = LocalDate.now();

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_longProp("id", id));
        list.add(new Config_stringProp("name", name));
        list.add(new Config_boolProp("active", active));
        list.add(new Config_stringProp("genre", genre));
        list.add(new Config_stringProp("description", description));
        list.add(new Config_intProp("amountEpisodes", amountEpisodes));
        list.add(new Config_intProp("maxAge", maxAge));
        list.add(new Config_stringProp("website", website));
        list.add(new Config_stringProp("url", url));
        list.add(new Config_lDate("genDate", PLDateFactory.toString(genDate)) {
            @Override
            public void setUsedValue(LocalDate act) {
                genDate = act;
            }
        });

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public int getNo() {
        return no.get();
    }

    public IntegerProperty noProperty() {
        return no;
    }

    public void setNo(int no) {
        this.no.set(no);
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

    public int getAmountEpisodes() {
        return amountEpisodes.get();
    }

    public IntegerProperty amountEpisodesProperty() {
        return amountEpisodes;
    }

    public void setAmountEpisodes(int amountEpisodes) {
        this.amountEpisodes.set(amountEpisodes);
    }

    public int getMaxAge() {
        return maxAge.get();
    }

    public IntegerProperty maxAgeProperty() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge.set(maxAge);
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

    public LocalDate getGenDate() {
        return genDate;
    }

    public void setGenDate(LocalDate genDate) {
        this.genDate = genDate;
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
