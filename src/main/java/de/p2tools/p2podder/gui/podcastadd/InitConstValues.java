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

import de.p2tools.p2lib.tools.date.P2LDateFactory;

public class InitConstValues {
    AddPodcastDto addPodcastDto;

    public InitConstValues(AddPodcastDto addPodcastDto) {
        this.addPodcastDto = addPodcastDto;
    }

    public void makeAct() {
        addPodcastDto.lblNo.setText(addPodcastDto.getAct().podcast.getNo() + "");
        addPodcastDto.lblSumEpisodes.setText(addPodcastDto.getAct().podcast.getAmountEpisodes() + "");
        addPodcastDto.lblGenDate.setText(P2LDateFactory.toString(addPodcastDto.getAct().podcast.getGenDate()));
    }
}
