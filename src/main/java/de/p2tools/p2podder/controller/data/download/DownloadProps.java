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

package de.p2tools.p2podder.controller.data.download;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.guiTools.PDownloadSize;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.tools.Data;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.ArrayList;

public class DownloadProps extends PDataSample<Download> {
    public static final String TAG = "Download";

    private final IntegerProperty no = new SimpleIntegerProperty(DownloadConstants.DOWNLOAD_NUMBER_NOT_STARTED);

    private final IntegerProperty state = new SimpleIntegerProperty(DownloadConstants.STATE_INIT);
    private final IntegerProperty guiState = new SimpleIntegerProperty(DownloadConstants.STATE_INIT);
    private final StringProperty episodeTitle = new SimpleStringProperty("");
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty podcastName = new SimpleStringProperty("");
    private final StringProperty duration = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final BooleanProperty placedBack = new SimpleBooleanProperty(false);
    private final DoubleProperty progress = new SimpleDoubleProperty(DownloadConstants.PROGRESS_NOT_STARTED);
    private final DoubleProperty guiProgress = new SimpleDoubleProperty(DownloadConstants.PROGRESS_NOT_STARTED);
    private final PDownloadSize pdownloadSize = new PDownloadSize();

    private final StringProperty remaining = new SimpleStringProperty("");
    private final StringProperty bandwidth = new SimpleStringProperty("");
    private final StringProperty destFileName = new SimpleStringProperty("");
    private final StringProperty destPath = new SimpleStringProperty("");

    private final StringProperty episodeUrl = new SimpleStringProperty("");
    private long podcastId = 0;
    private final StringProperty episodeWebsite = new SimpleStringProperty("");

    private final PLocalDateProperty pubDate = new PLocalDateProperty();

    DownloadProps() {
//        pdownloadSize.addListener(new ChangeListener<Long>() {
//            @Override
//            public void changed(ObservableValue<? extends Long> observable, Long oldValue, Long newValue) {
//                System.out.println("downloadSize: " + pdownloadSize.getActFileSize());
//            }
//        });
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigIntPropExtra("no", DownloadFieldNames.DOWNLOAD_NO, no));
        list.add(new ConfigIntPropExtra("state", DownloadFieldNames.DOWNLOAD_STATE, state));
        list.add(new ConfigStringPropExtra("episodeTitle", DownloadFieldNames.DOWNLOAD_EPISODE_TITLE, episodeTitle));
        list.add(new ConfigStringPropExtra("genre", DownloadFieldNames.DOWNLOAD_GENRE, genre));
        list.add(new ConfigStringPropExtra("podcastName", DownloadFieldNames.DOWNLOAD_GENRE, podcastName));

        list.add(new ConfigStringPropExtra("duration", DownloadFieldNames.DOWNLOAD_DURATION, duration));
        list.add(new ConfigStringPropExtra("description", DownloadFieldNames.DOWNLOAD_DESCRIPTION, description));
        list.add(new ConfigBoolPropExtra("placedBack", DownloadFieldNames.DOWNLOAD_PLACED_BACK, placedBack));
        list.add(new ConfigDoublePropExtra("progress", DownloadFieldNames.DOWNLOAD_PROGRESS, progress));
        list.add(new ConfigPDownloadSize("pdownloadSize", DownloadFieldNames.DOWNLOAD_SIZE, pdownloadSize));

        list.add(new ConfigStringPropExtra("destFileName", DownloadFieldNames.DOWNLOAD_DEST_FILE_NAME, destFileName));
        list.add(new ConfigStringPropExtra("destPath", DownloadFieldNames.DOWNLOAD_DEST_PATH, destPath));

        list.add(new ConfigStringPropExtra("episodeUrl", DownloadFieldNames.DOWNLOAD_URL, episodeUrl));
        list.add(new ConfigLong("podcastId", podcastId) {
            public void setUsedValue(Long value) {
                podcastId = value;
            }
        });
        list.add(new ConfigStringPropExtra("episodeWebsite", DownloadFieldNames.DOWNLOAD_WEBSITE, episodeWebsite));

        list.add(new ConfigLocalDatePropExtra("pubDate", DownloadFieldNames.DOWNLOAD_PUB_DATE, pubDate));
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

    public int getState() {
        return state.get();
    }

    public IntegerProperty stateProperty() {
        return state;
    }

    public void setState(int state) {
        this.state.set(state);
        Platform.runLater(() -> {
            guiState.setValue(state);
            ProgData.getInstance().downloadFilter.setPredicate();
        });
    }

    public void setGuiState(int guiState) {
        this.guiState.set(guiState);
    }

    public int getGuiState() {
        return guiState.get();
    }

    public IntegerProperty guiStateProperty() {
        return guiState;
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

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
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

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        if (!duration.contains(":")) {
            try {
                int dur = Integer.parseInt(duration);
                this.duration.set(dur / 60 + ":" + dur % 60);
            } catch (Exception e) {
            }
        } else {
            if (duration.startsWith("00:")) {
                duration = duration.replaceFirst("00:", "");
            }
            this.duration.set(duration);
        }
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

    public boolean isPlacedBack() {
        return placedBack.get();
    }

    public BooleanProperty placedBackProperty() {
        return placedBack;
    }

    public void setPlacedBack(boolean placedBack) {
        this.placedBack.set(placedBack);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.setValue(progress);
        Platform.runLater(() -> guiProgress.setValue(progress));
    }

    public double getGuiProgress() {
        return guiProgress.get();
    }

    public DoubleProperty guiProgressProperty() {
        return guiProgress;
    }

    public void setGuiProgress(double guiProgress) {
        this.guiProgress.set(guiProgress);
    }


    public PDownloadSize getPdownloadSize() {
        return pdownloadSize;
    }

    public PDownloadSize pdownloadSizeProperty() {
        return pdownloadSize;
    }

    public String getRemaining() {
        return remaining.get();
    }

    public StringProperty remainingProperty() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining.set(remaining);
    }

    public String getBandwidth() {
        return bandwidth.get();
    }

    public StringProperty bandwidthProperty() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth.set(bandwidth);
    }

    public String getDestFileName() {
        return destFileName.get();
    }

    public StringProperty destFileNameProperty() {
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName.set(destFileName);
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

    public String getDestPathFile() {
        return PFileUtils.addsPath(destPath.getValueSafe(), destFileName.getValueSafe());
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
        return podcastId;
    }

    public void setPodcastId(long podcastId) {
        this.podcastId = podcastId;
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

    public int compareTo(DownloadProps arg0) {
        int ret;
        if ((ret = Data.sorter.compare(getEpisodeTitle(), arg0.getEpisodeTitle())) == 0) {
            return getEpisodeUrl().compareTo(arg0.getEpisodeUrl());
        }
        return ret;
    }
}
