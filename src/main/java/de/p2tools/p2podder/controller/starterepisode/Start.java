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

package de.p2tools.p2podder.controller.starterepisode;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.file.P2FileUtils;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Start extends StartProps {

    private final StartStatus startStatus = new StartStatus();
    private Episode episode = null;


    private int startCounter = 0;
    private int restartCounter = 0; //zählt die Anzahl der Neustarts bei einem Startfehler -> Summe Starts = erster Start + Restarts
    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    private Process process = null; //Prozess des Programms (VLC)
    private P2Date startTime = new P2Date();

    private SetData setData = null;
    private EpisodeStarterFactory episodeStarterFactory = null;


    public Start() {
    }

    public Start(SetData setData, Episode episode) {
        this.episode = episode;
        this.setData = setData;

        setEpisodeTitle(episode.getEpisodeTitle());
        String pathName = P2FileUtils.addsPath(episode.getFilePath(), episode.getFileName());
        setFilePathName(pathName);
        StartProgramFactory.makeProgParameter(this);
    }

    public P2Date getStartTime() {
        return startTime;
    }

    public void setStartTime(P2Date startTime) {
        this.startTime = startTime;
    }

    public int getStartCounter() {
        return startCounter;
    }

    public void incStartCounter() {
        ++this.startCounter;
    }

    public int getRestartCounter() {
        return restartCounter;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getProgramCall() {
        return programCall.get();
    }

    public StringProperty programCallProperty() {
        return programCall;
    }

    public void setProgramCall(String programCall) {
        this.programCall.set(programCall);
    }

    public String getProgramCallArray() {
        return programCallArray.get();
    }

    public StringProperty programCallArrayProperty() {
        return programCallArray;
    }

    public void setProgramCallArray(String programCallArray) {
        this.programCallArray.set(programCallArray);
    }


    public StartStatus getStartStatus() {
        return startStatus;
    }

    public Episode getEpisode() {
        return episode;
    }

    public SetData getSetData() {
        return setData;
    }

    public Start getCopy() {
        final Start ret = new Start();
        ret.setData = setData;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(Start start) {
        setData = start.setData;

        Config[] configs = start.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
