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
import de.p2tools.p2Lib.configFile.config.ConfigExtra_longProp;
import de.p2tools.p2Lib.tools.PIndex;
import de.p2tools.p2Lib.tools.date.PLDateFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.time.LocalDate;

public class Podcast extends PodcastProps {

    public Podcast() {
        setId(PIndex.getIndex());
    }

    public void init() {
        setDate();
    }

    private void setDate() {
        podcastDate = LocalDate.now();
        if (!getDate().isEmpty()) {
            // nur dann gibts ein Datum
            try {
                podcastDate = PLDateFactory.fromString(getDate());

            } catch (final Exception ex) {
                PLog.errorLog(854121547, ex, new String[]{"Datum: " + getDate()});
                podcastDate = LocalDate.now();
                setDate("");
            }
        }
    }

    public void copyToMe(Podcast podcast) {
        Config[] conf = getConfigsArr();
        Config[] podConf = podcast.getConfigsArr();

        for (int i = 0; i < conf.length; ++i) {
            conf[i].setActValue(podConf[i].getActValue());
        }
        setNo(podcast.getNo());
        init(); //Datum und int-Werte setzen
    }

    public Podcast getCopy() {
        final Podcast ret = new Podcast();
        Config[] conf = getConfigsArr();
        Config[] retConf = ret.getConfigsArr();

        for (int i = 0; i < conf.length; ++i) {
            retConf[i].setActValue(conf[i].getActValue());

            if (!conf[i].getActValue().equals(retConf[i].getActValue())) {
                System.out.println("conf:    " + i + " - " + conf[i].getActValueString());
                System.out.println("retConf: " + i + " - " + retConf[i].getActValueString());
            }
        }

        LongProperty lp = new SimpleLongProperty(5);
        ConfigExtra_longProp clp = new ConfigExtra_longProp("test", "test", lp);
        clp.setActValue(123);
        System.out.println("clp: " + clp.getActValueString());


        ret.setNo(getNo());
        ret.init(); //Datum und int-Werte setzen
        return ret;
    }
}
