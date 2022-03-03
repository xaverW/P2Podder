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

package de.p2tools.p2podder.controller.starterEpisode;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.guiTools.PSizeTools;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.download.Download;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.tools.MLInputStream;

public final class Start extends StartProps {

    private final StartStatus startStatus = new StartStatus();
    private Starter starter = new Starter(this);
    private int restartCounter = 0; // zählt die Anzahl der Neustarts bei einem Downloadfeheler->Summe Starts = erster Start + Restarts
    private long bandwidth = -1; // Downloadbandbreite: bytes per second
    private long timeLeftSeconds = -1; // restliche Laufzeit [s] des Downloads
    private MLInputStream inputStream = null;
    private PDate startTime = null;
    private Process process = null; //Prozess des Download

    private Podcast podcast = null;
    private Download download = null;
    private Episode episode = null;
    private SetData setData = null;

    public Start() {
    }

    public Start(Download download) {
        this.download = download;
    }

    public Start(SetData setData, Podcast podcast) {
        if (podcast == null) {
            setPodcastNo(EpisodeConstants.PODCAST_NO);
        }

        this.podcast = podcast;
        setPodcastNo(podcast.getNo());
        setEpisodeTitle(podcast.getName());
        setUrl(podcast.getUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public Start(SetData setData, Download download) {
        this.download = download;
        setEpisodeTitle(download.getEpisodeTitle());
        setUrl(download.getEpisodeUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }


    public Start(SetData setData, Episode episode) {
        this.episode = episode;
        setEpisodeTitle(episode.getEpisodeTitle());
        setUrl(episode.getEpisodeUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
        download.setBandwidth(PSizeTools.humanReadableByteCount(bandwidth, true));
    }

    public PDate getStartTime() {
        return startTime;
    }

    public void setStartTime(PDate startTime) {
        this.startTime = startTime;
    }

    public StartStatus getStartStatus() {
        return startStatus;
    }

    public Starter getStarter() {
        return starter;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void initStart() {
        getStarter().setStartTime();
        getStarter().setRestartCounter(0);
        getStartStatus().setState(EpisodeConstants.STATE_INIT);
        getStartStatus().setErrorMessage("");
    }

    public void stopStart() {
        if (getStartStatus().isStateError()) {
            // damit fehlerhafte nicht wieder starten
//            getStart().setRestartCounter(ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART.get());
        } else {
            getStartStatus().setStateStopped();
        }

        setNo(EpisodeConstants.EPISODE_NUMBER_NOT_STARTED);
    }

    public MLInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(MLInputStream inputStream) {
        this.inputStream = inputStream;
    }
    //==============================================
    // Get/Set
    //==============================================

    public String getStationUrl() {
        if (podcast != null) {
            return podcast.getUrl();
        } else {
            return getUrl();
        }
    }

    public void startDownload() {
        setStartTime(new PDate());
    }

    public void setRestartCounter(int restartCounter) {
        this.restartCounter = restartCounter;
    }

    public Podcast getStation() {
        return podcast;
    }

    public Episode getEpisode() {
        return episode;
    }

    public SetData getSetData() {
        return setData;
    }

    public void setSetData(SetData setData) {
        this.setData = setData;
        setSetDataId(setData.getId());
    }

    public int getRestartCounter() {
        return restartCounter;
    }

    public long getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    public void setTimeLeftSeconds(long timeLeftSeconds) {
        this.timeLeftSeconds = timeLeftSeconds;
        if (download.isStateStartedRun() && getTimeLeftSeconds() > 0) {
            download.setRemaining(DownloadConstants.getTimeLeft(timeLeftSeconds));
        } else {
            download.setRemaining("");
        }
    }

    public Start getCopy() {
        final Start ret = new Start();
        ret.starter = starter;
        ret.podcast = podcast;
        ret.setData = setData;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(Start start) {
        starter = start.starter;
        podcast = start.podcast;
        setData = start.setData;

        Config[] configs = start.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
