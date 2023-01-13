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

package de.p2tools.p2podder.controller.data.podcast;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PIndex;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;

public class Podcast extends PodcastProps {

    public Podcast() {
        setId(PIndex.getIndex());
    }

    public void copyToMe(Podcast podcast) {
        Config[] conf = getConfigsArr();
        Config[] podConf = podcast.getConfigsArr();

        for (int i = 0; i < conf.length; ++i) {
            conf[i].setActValue(podConf[i].getActValue());
        }
        setNo(podcast.getNo());
    }

    public Podcast getCopy() {
        final Podcast ret = new Podcast();
        Config[] conf = getConfigsArr();
        Config[] retConf = ret.getConfigsArr();

        for (int i = 0; i < conf.length; ++i) {
            retConf[i].setActValue(conf[i].getActValue());
        }
        ret.setNo(getNo());
        return ret;
    }

    @Override
    public String toString() {
        String count;
        count = EpisodeFactory.countEpisode(this) + "";
        return getGenre() + "\n" + "[" + count + "]  -  " + getName();
    }
}
