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

package de.p2tools.p2podder.controller.worker;

import de.p2tools.p2podder.controller.config.ProgData;

public class Worker extends Thread {

    private final ProgData progData;

    public Worker(ProgData progData) {
        this.progData = progData;

//        progData.eventNotifyLoadPodcastList.addListenerLoadPodcastList(new EventListenerPodcastList() {
//            @Override
//            public void start(EventLoadPodcastList event) {
//                if (event.progress == EventListenerPodcastList.PROGRESS_INDETERMINATE) {
//                    progData.maskerPane.setMaskerVisible(true, false);
//                } else {
//                    progData.maskerPane.setMaskerVisible(true, true);
//                }
//                progData.maskerPane.setMaskerProgress(event.progress, event.text);
//            }
//
//            @Override
//            public void progress(EventLoadPodcastList event) {
//                progData.maskerPane.setMaskerProgress(event.progress, event.text);
//            }
//
//            @Override
//            public void loaded(EventLoadPodcastList event) {
//                progData.maskerPane.setMaskerVisible(true, false);
//                progData.maskerPane.setMaskerProgress(EventListenerPodcastList.PROGRESS_INDETERMINATE, "Podcasts verarbeiten");
//            }
//
//            @Override
//            public void finished(EventLoadPodcastList event) {
//                progData.maskerPane.setMaskerVisible(false);
//            }
//        });
    }
}