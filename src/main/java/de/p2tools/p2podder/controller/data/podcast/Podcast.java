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

import de.p2tools.p2Lib.tools.PIndex;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.log.PLog;

public class Podcast extends PodcastProps {

    public Podcast() {
        setId(PIndex.getIndex());
    }

    public void init() {
        setDate();
    }

    private void setDate() {
        podcastDate.setPLocalDateNow();
        if (!getDate().isEmpty()) {
            // nur dann gibts ein Datum
            try {
                PDate pd = new PDate(sdf_date.parse(getDate()));
                podcastDate.setPLocalDate(pd);

            } catch (final Exception ex) {
                PLog.errorLog(854121547, ex, new String[]{"Datum: " + getDate()});
                podcastDate = new PLocalDate();
                setDate("");
            }
        }
    }

    public void copyToMe(Podcast podcast) {
        for (int i = 0; i < getConfigsArr().length; ++i) {
            getConfigsArr()[i].setActValue(podcast.getConfigsArr()[i].getActValue());
        }

        no = podcast.no;
        init(); //Datum und int-Werte setzen
    }

    public Podcast getCopy() {
        final Podcast ret = new Podcast();
        for (int i = 0; i < getConfigsArr().length; ++i) {
            ret.getConfigsArr()[i].setActValue(getConfigsArr()[i].getActValue());
        }

        ret.no = no;
        ret.init(); //Datum und int-Werte setzen
        return ret;
    }
}
