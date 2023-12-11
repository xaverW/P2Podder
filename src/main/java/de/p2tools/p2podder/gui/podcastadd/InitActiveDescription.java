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

public class InitActiveDescription {

    private final AddPodcastDto addPodcastDto;

    public InitActiveDescription(AddPodcastDto addPodcastDto) {
        this.addPodcastDto = addPodcastDto;
        init();
    }

    private void init() {
        addPodcastDto.textAreaDescription.setWrapText(true);
        addPodcastDto.txtName.textProperty().addListener((u, o, n) -> setName());
        addPodcastDto.chkActive.selectedProperty().addListener((u, o, n) -> setActive());
        addPodcastDto.textAreaDescription.textProperty().addListener((u, o, n) -> setDescription());
    }

    public void makeAct() {
        addPodcastDto.txtName.setText(addPodcastDto.getAct().podcast.getName());
        addPodcastDto.chkActive.setSelected(addPodcastDto.getAct().podcast.isActive());
        addPodcastDto.textAreaDescription.setText(addPodcastDto.getAct().podcast.getDescription());
    }

    public void setName() {
        // Name
        addPodcastDto.getAct().podcast.setName(addPodcastDto.txtName.getText());
    }

    public void setActive() {
        // active
        if (addPodcastDto.chkActiveAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(addPodcastData ->
                    addPodcastData.podcast.setActive(addPodcastDto.chkActive.isSelected()));
        } else {
            addPodcastDto.getAct().podcast.setActive(addPodcastDto.chkActive.isSelected());
        }
    }

    public void setDescription() {
        // description
        if (addPodcastDto.chkDescriptionAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(addPodcastData -> {
                addPodcastData.podcast.setDescription(addPodcastDto.textAreaDescription.getText());
            });

        } else {
            addPodcastDto.getAct().podcast.setDescription(addPodcastDto.textAreaDescription.getText());
        }
    }
}
