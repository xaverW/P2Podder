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

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.starterepisode.Start;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class Episode extends EpisodeProps {

    private Start start = null;
    private boolean isNew = false;

    public Episode() {
    }

    public Episode(DownloadData download) {
        de.p2tools.p2podder.controller.data.podcast.Podcast podcast = ProgData.getInstance().podcastList.getPodcastById(download.getPodcastId());

        setDuration(download.getDuration());
        setDescription(download.getDescription());
        setEpisodeWebsite(download.getEpisodeWebsite());
        setEpisodeUrl(download.getEpisodeUrl());
        setPodcastId(download.getPodcastId());
        setEpisodeTitle(download.getEpisodeTitle());
        setGenre(download.getGenre());
        setFileName(download.getDestFileName());
        setFilePath(download.getDestPath());
        getPFileSize().setSizeL(download.getDownloadSize().getTargetSize());
        setPubDate(download.getPubDate());
        if (podcast != null) {
            setPodcastName(podcast.getName());
        }
        setNew(true); //aus Downloads angelegte, sind NEW
    }

    //==============================================
    // Get/Set
    //==============================================
    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean checkDays(long days) {
        if (days == 0) {
            return true;
        }
        final long timeRange = DAYS.between(getPubDate(), LocalDate.now());
        if (timeRange <= days) {
            return true;
        }

        return false;
    }

    public void setThePodcast(de.p2tools.p2podder.controller.data.podcast.Podcast podcast) {
        if (podcast == null) {
            // bei gespeicherten Episoden kann es den Podcast nicht mehr geben
            return;
        }

        setPodcastId(podcast.getId());
    }


    public Episode getCopy() {
        final Episode ret = new Episode();
        ret.start = start;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(Episode episode) {
        start = episode.start;

        Config[] configs = episode.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
