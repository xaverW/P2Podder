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

package de.p2tools.p2podder.controller.data.episode;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.guiTools.PFileSize;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.beans.property.*;

import java.util.ArrayList;

public class EpisodeProps extends PDataSample<Episode> {
    public static final String TAG = "Episode";

    private final IntegerProperty no = new SimpleIntegerProperty(EpisodeConstants.EPISODE_NUMBER_NOT_STARTED);

    private final StringProperty episodeTitle = new SimpleStringProperty("");
    private final StringProperty podcastName = new SimpleStringProperty("");
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty country = new SimpleStringProperty("");
    private final StringProperty language = new SimpleStringProperty("");
    private final StringProperty duration = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty fileName = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");
    private final PFileSize pFileSize = new PFileSize();

    private final StringProperty episodeUrl = new SimpleStringProperty("");
    private final LongProperty podcastId = new SimpleLongProperty(0);
    private final StringProperty episodeWebsite = new SimpleStringProperty("");

    private final PLocalDateProperty pubDate = new PLocalDateProperty();

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigIntPropExtra("no", EpisodeFieldNames.EPISODE_NO, no));

        list.add(new ConfigStringPropExtra("episodeTitle", EpisodeFieldNames.EPISODE_TITLE, episodeTitle));
        list.add(new ConfigStringPropExtra("podcastName", EpisodeFieldNames.EPISODE_TITLE, podcastName));
        list.add(new ConfigStringPropExtra("genre", EpisodeFieldNames.EPISODE_GENRE, genre));

        list.add(new ConfigStringPropExtra("country", EpisodeFieldNames.EPISODE_COUNTRY, country));
        list.add(new ConfigStringPropExtra("language", EpisodeFieldNames.EPISODE_COUNTRY, language));
        list.add(new ConfigStringPropExtra("duration", EpisodeFieldNames.EPISODE_DESCRIPTION, duration));
        list.add(new ConfigStringPropExtra("description", EpisodeFieldNames.EPISODE_DESCRIPTION, description));
        list.add(new ConfigStringPropExtra("fileName", EpisodeFieldNames.EPISODE_FILE_NAME, fileName));
        list.add(new ConfigStringPropExtra("filePath", EpisodeFieldNames.EPISODE_FILE_PATH, filePath));
        list.add(new ConfigPFileSize("pFileSize", EpisodeFieldNames.EPISODE_FILE_SIZE, pFileSize));

        list.add(new ConfigStringPropExtra("episodeUrl", EpisodeFieldNames.EPISODE_URL, episodeUrl));
        list.add(new ConfigLongPropExtra("podcastId", EpisodeFieldNames.EPISODE_PODCAST_ID, podcastId));
        list.add(new ConfigStringPropExtra("episodeWebsite", EpisodeFieldNames.EPISODE_WEBSITE, episodeWebsite));
        list.add(new ConfigLocalDatePropExtra("pubDate", EpisodeFieldNames.EPISODE_DATE, pubDate));

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

    public String getEpisodeTitle() {
        return episodeTitle.get();
    }

    public StringProperty episodeTitleProperty() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle.set(episodeTitle);
    }

    public String getPodcastName() {
        return podcastName.get();
    }

    public StringProperty podcastNameProperty() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName.set(podcastName);
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

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getLanguage() {
        return language.get();
    }

    public StringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
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

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public PFileSize getPFileSize() {
        return pFileSize;
    }

    public String getEpisodeUrl() {
        return episodeUrl.get();
    }

    public StringProperty episodeUrlProperty() {
        return episodeUrl;
    }

    public void setEpisodeUrl(String episodeUrl) {
        this.episodeUrl.set(episodeUrl);
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

    public String getEpisodeWebsite() {
        return episodeWebsite.get();
    }

    public StringProperty episodeWebsiteProperty() {
        return episodeWebsite;
    }

    public void setEpisodeWebsite(String episodeWebsite) {
        this.episodeWebsite.set(episodeWebsite);
    }

    public PLocalDate getPubDate() {
        return pubDate.get();
    }

    public PLocalDateProperty pubDateProperty() {
        return pubDate;
    }

    public void setPubDate(PLocalDate pubDate) {
        this.pubDate.setValue(pubDate);
    }

    public void setPubDate(String date) {
        this.pubDate.setPLocalDate(date);
    }

    public int compareTo(EpisodeProps arg0) {
        int ret;
        if ((ret = sorter.compare(getEpisodeTitle(), arg0.getEpisodeTitle())) == 0) {
            return getEpisodeUrl().compareTo(arg0.getEpisodeUrl());
        }
        return ret;
    }
}
