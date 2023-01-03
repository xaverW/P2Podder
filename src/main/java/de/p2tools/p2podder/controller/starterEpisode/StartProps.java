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
import de.p2tools.p2Lib.configFile.config.Config_intProp;
import de.p2tools.p2Lib.configFile.config.Config_stringProp;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2podder.controller.data.episode.EpisodeConstants;
import de.p2tools.p2podder.controller.data.episode.EpisodeFieldNames;
import de.p2tools.p2podder.tools.Data;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class StartProps extends PDataSample<Start> {

    private final IntegerProperty no = new SimpleIntegerProperty(EpisodeConstants.EPISODE_NUMBER_NOT_STARTED);
    private final IntegerProperty podcastNo = new SimpleIntegerProperty(EpisodeConstants.PODCAST_NO);
    private final StringProperty episodeTitle = new SimpleStringProperty("");
    private final StringProperty filePathName = new SimpleStringProperty("");


    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_intProp("no", EpisodeFieldNames.EPISODE_NO, no));
        list.add(new Config_intProp("podcastNo", EpisodeFieldNames.EPISODE_PODCAST_NO, podcastNo));
        list.add(new Config_stringProp("episodeTitle", EpisodeFieldNames.EPISODE_TITLE, episodeTitle));
        list.add(new Config_stringProp("filePathName", EpisodeFieldNames.EPISODE_URL, filePathName));

        return list.toArray(new Config[]{});
    }

    private final StringProperty program = new SimpleStringProperty("");
    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");


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

    public int getPodcastNo() {
        return podcastNo.get();
    }

    public IntegerProperty podcastNoProperty() {
        return podcastNo;
    }

    public void setPodcastNo(int podcastNo) {
        this.podcastNo.set(podcastNo);
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

    public String getFilePathName() {
        return filePathName.get();
    }

    public StringProperty filePathNameProperty() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName.set(filePathName);
    }

    public String getProgram() {
        return program.get();
    }

    public StringProperty programProperty() {
        return program;
    }

    public void setProgram(String program) {
        this.program.set(program);
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

    public int compareTo(StartProps arg0) {
        return Data.sorter.compare(getEpisodeTitle(), arg0.getEpisodeTitle());
    }

}
