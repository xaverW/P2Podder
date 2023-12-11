/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2podder.gui.podcastadd;

import java.util.Arrays;

public class InitUrl {
    AddPodcastDto addPodcastDto;

    public InitUrl(AddPodcastDto addPodcastDto) {
        this.addPodcastDto = addPodcastDto;
        init();
    }

    private void init() {
        addPodcastDto.txtWebsite.textProperty().addListener((u, o, n) -> setUrlWebsite());
        addPodcastDto.txtUrlRss.textProperty().addListener((u, o, n) -> setUrlRss());
    }

    public void makeAct() {
        addPodcastDto.txtWebsite.setText(addPodcastDto.getAct().podcast.getWebsite());
        addPodcastDto.txtUrlRss.setText(addPodcastDto.getAct().podcast.getUrl());
    }

    public void setUrlWebsite() {
        // website
        if (addPodcastDto.chkWebsiteAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(addPodcastData ->
                    addPodcastData.podcast.setWebsite(addPodcastDto.txtWebsite.getText()));
        } else {
            addPodcastDto.getAct().podcast.setWebsite(addPodcastDto.txtWebsite.getText());
        }
    }

    public void setUrlRss() {
        // rss
        if (addPodcastDto.chkUrlRssAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(addPodcastData -> {
                addPodcastData.podcast.setUrl(addPodcastDto.txtUrlRss.getText());
            });

        } else {
            addPodcastDto.getAct().podcast.setUrl(addPodcastDto.txtUrlRss.getText());
        }
    }
}
