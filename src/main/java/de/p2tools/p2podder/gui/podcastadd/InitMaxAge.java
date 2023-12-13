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

import de.p2tools.p2lib.mtfilter.FilterCheck;
import javafx.util.StringConverter;

import java.util.Arrays;

public class InitMaxAge {

    private final AddPodcastDto addPodcastDto;

    public InitMaxAge(AddPodcastDto addPodcastDto) {
        this.addPodcastDto = addPodcastDto;
        init();
    }

    private void init() {
        addPodcastDto.slMaxAge.setMin(FilterCheck.FILTER_ALL_OR_MIN);
        addPodcastDto.slMaxAge.setMax(100);
        addPodcastDto.slMaxAge.setShowTickLabels(true);
        addPodcastDto.slMaxAge.setMinorTickCount(9);
        addPodcastDto.slMaxAge.setMajorTickUnit(20);
        addPodcastDto.slMaxAge.setBlockIncrement(25);
        addPodcastDto.slMaxAge.setSnapToTicks(true);
        addPodcastDto.slMaxAge.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double x) {
                if (x == FilterCheck.FILTER_ALL_OR_MIN) {
                    return "alles";
                }
                return x.intValue() + "";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        if (addPodcastDto.addNewDownloads) {
            // Vorgabe nur für neue Podcasts
            addPodcastDto.slMaxAge.setValue(30);
            setMaxAge();
        } else {
            addPodcastDto.slMaxAge.setValue(addPodcastDto.getAct().podcast.getMaxAge());
        }

        // kein direktes binding wegen: valueChangingProperty, nur melden wenn "steht"
        addPodcastDto.slMaxAge.valueChangingProperty().addListener((observable, oldvalue, newvalue) -> {
            if (!newvalue) {
                setMaxAge();
            }
        });
        addPodcastDto.slMaxAge.valueProperty().addListener((observable, oldValue, newValue) -> {
            setLabelSlider();
        });
        setLabelSlider();
    }

    public void makeAct() {
        addPodcastDto.slMaxAge.setValue(addPodcastDto.getAct().podcast.getMaxAge());
    }

    public void setMaxAge() {
        if (addPodcastDto.chkMaxAgeAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(this::set);
        } else {
            set(addPodcastDto.getAct());
        }
    }

    private void set(AddPodcastData addPodcastData) {
        addPodcastData.podcast.setMaxAge((int) addPodcastDto.slMaxAge.getValue());
    }

    private void setLabelSlider() {
        final String txtAll = "Alles";
        int i = (int) addPodcastDto.slMaxAge.getValue();
        String tNr = i + "";
        if (i == FilterCheck.FILTER_ALL_OR_MIN) {
            addPodcastDto.lblMaxAge.setText(txtAll);
        } else {
            addPodcastDto.lblMaxAge.setText(tNr + (i == 1 ? " Tag" : " Tage"));
        }
    }


}
