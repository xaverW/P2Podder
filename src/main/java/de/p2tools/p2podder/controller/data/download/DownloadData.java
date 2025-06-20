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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.mediathek.download.DownloadSize;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2lib.tools.net.PUrlTools;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import de.p2tools.p2podder.controller.starterdownload.DownloadStart;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class DownloadData extends DownloadDataProps {

    private DownloadStart downloadStart = new DownloadStart(this);
    private String errorMessage = "";

    public DownloadData() {
    }

    public DownloadData(String episodeUri, String title, String website, LocalDate pubDate, String description, Podcast podcast) {
        setEpisodeUrl(episodeUri);
        setEpisodeTitle(title);
        setEpisodeWebsite(website);
        setPubDate(pubDate);
        setDescription(description);

        setPodcastId(podcast.getId());
        setPodcastName(podcast.getName());
        setGenre(podcast.getGenre());

        // und endlich Aufruf bauen :)
        String path = P2FileUtils.addsPath(ProgConfig.SYSTEM_POD_DIR.getValueSafe(), podcast.getGenre());
        String name = PUrlTools.getFileName(episodeUri);
        DownloadProgram.makeProgParameter(this, name, path);
    }

    //==============================================
    // Get/Set
    //==============================================
    public DownloadStart getDownloadStart() {
        return downloadStart;
    }

    public void setDownloadStart(DownloadStart downloadStart) {
        this.downloadStart = downloadStart;
    }

    public void setPodcast(Podcast podcast) {
        if (podcast == null) {
            // bei gespeicherten Downloads kann es den Podcast nicht mehr geben
            setPodcastName("");
            return;
        }

        setPodcastId(podcast.getId());
        setPodcastName(podcast.getName());
    }

    public void putBack() {
        // download resetten, und als "zurückgestellt" markieren
        setPlacedBack(true);
        resetDownload();
    }

    public void resetDownload() {
        // stoppen und alles zurücksetzen
        stopDownload();
        setState(DownloadConstants.STATE_INIT);
    }

    public void stopDownload() {
        if (isStateError()) {
            // damit fehlerhafte nicht wieder starten
            getDownloadStart().setRestartCounter(ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART.getValue());
        } else {
            setState(DownloadConstants.STATE_STOPPED);
            setProgress(DownloadConstants.PROGRESS_NOT_STARTED);
        }

        final DownloadSize downSize = getDownloadSize();
        downSize.resetActFileSize();
        setRemaining("");
        setBandwidth("");
        getDownloadStart().setBandwidth(0);
        setNo(P2LibConst.NUMBER_NOT_STARTED);
    }

    public void initStartDownload() {
        getDownloadStart().setRestartCounter(0);
        getDownloadStart().setBandwidth(0);
        setStateStartedWaiting();
        setErrorMessage("");
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

    //==============================================
    // Downloadstatus
    //==============================================

    public boolean isStateInit() {
        return getState() == DownloadConstants.STATE_INIT;
    }

    public boolean isStarted() {
        return getState() > DownloadConstants.STATE_STOPPED && !isStateFinished();
    }

    public boolean isNotStarted() {
        return getState() <= DownloadConstants.STATE_STOPPED;
    }

    public boolean isStateStopped() {
        return getState() == DownloadConstants.STATE_STOPPED;
    }

    public boolean isStateStartedWaiting() {
        return getState() == DownloadConstants.STATE_STARTED_WAITING;
    }

    public boolean isStateStartedRun() {
        return getState() == DownloadConstants.STATE_STARTED_RUN;
    }

    public boolean isStateFinished() {
        return getState() == DownloadConstants.STATE_FINISHED;
    }

    public boolean isStateFinalized() {
        return getState() >= DownloadConstants.STATE_FINISHED;
    }

    public boolean isStateError() {
        return getState() == DownloadConstants.STATE_ERROR;
    }

    public void setStateInit() {
        setState(DownloadConstants.STATE_INIT);
    }

    public void setStateStartedWaiting() {
        setState(DownloadConstants.STATE_STARTED_WAITING);
    }

    public void setStateStartedRun() {
        setState(DownloadConstants.STATE_STARTED_RUN);
    }

    public void setStateFinished() {
        setState(DownloadConstants.STATE_FINISHED);
    }

    public void setStateError() {
        setState(DownloadConstants.STATE_ERROR);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        final String s = "Der Download hatte einen Fehler:\n\n";
        this.errorMessage = s + errorMessage;
    }

    public void restartDownload() {
        // stoppen und alles zurücksetzen
        final DownloadSize downSize = getDownloadSize();
        downSize.resetActFileSize();
        setRemaining("");
        setBandwidth("");
        getDownloadStart().setBandwidth(0);
        setNo(P2LibConst.NUMBER_NOT_STARTED);

        setState(DownloadConstants.STATE_INIT);
        setProgress(DownloadConstants.PROGRESS_NOT_STARTED);
    }

    public DownloadData getCopy() {
        final DownloadData ret = new DownloadData();

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        ret.setGuiState(getGuiState());
        ret.setDownloadStart(getDownloadStart());
        return ret;
    }

    public void copyToMe(DownloadData download) {
        Config[] configs = download.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        setGuiState(download.getGuiState());
        setDownloadStart(download.getDownloadStart());
    }
}
