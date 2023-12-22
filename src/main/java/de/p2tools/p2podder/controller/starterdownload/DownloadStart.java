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

package de.p2tools.p2podder.controller.starterdownload;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.guitools.P2SizeTools;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.download.DownloadConstants;
import de.p2tools.p2podder.controller.data.download.DownloadData;
import de.p2tools.p2podder.controller.starterepisode.StartProps;
import de.p2tools.p2podder.controller.starterepisode.StartStatus;
import de.p2tools.p2podder.tools.MLInputStream;

public final class DownloadStart extends StartProps {

    private final StartStatus startStatus = new StartStatus();
    private int restartCounter = 0; // zählt die Anzahl der Neustarts bei einem Downloadfeheler->Summe Starts = erster Start + Restarts
    private long bandwidth = -1; // Downloadbandbreite: bytes per second
    private long timeLeftSeconds = -1; // restliche Laufzeit [s] des Downloads
    private MLInputStream inputStream = null;
    private P2Date startTime = null;
    private Process process = null; //Prozess des Download

    private DownloadData downloadData = null;
    private SetData setData = null;

    public DownloadStart() {
    }

    public DownloadStart(DownloadData downloadData) {
        this.downloadData = downloadData;
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
        downloadData.setBandwidth(P2SizeTools.humanReadableByteCount(bandwidth, true));
    }

    public P2Date getStartTime() {
        return startTime;
    }

    public void setStartTime(P2Date startTime) {
        this.startTime = startTime;
    }

    public StartStatus getStartStatus() {
        return startStatus;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
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

    public void startDownload() {
        setStartTime(new P2Date());
    }

    public void setRestartCounter(int restartCounter) {
        this.restartCounter = restartCounter;
    }

    public int getRestartCounter() {
        return restartCounter;
    }

    public long getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    public void setTimeLeftSeconds(long timeLeftSeconds) {
        this.timeLeftSeconds = timeLeftSeconds;
        if (downloadData.isStateStartedRun() && getTimeLeftSeconds() > 0) {
            downloadData.setRemaining(DownloadConstants.getTimeLeft(timeLeftSeconds));
        } else {
            downloadData.setRemaining("");
        }
    }

    public DownloadStart getCopy() {
        final DownloadStart ret = new DownloadStart();
        ret.setData = setData;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(DownloadStart start) {
        setData = start.setData;

        Config[] configs = start.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
