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

package de.p2tools.p2podder.controller.config.pEvent;

import java.util.EventListener;

public class EventListenerPodcastList implements EventListener {

    public static final double PROGRESS_MIN = 0.0;
    public static final double PROGRESS_MAX = 1.0;
    public static final double PROGRESS_INDETERMINATE = -1.0;

    public void start(EventLoadPodcastList e) {
    }

    public void progress(EventLoadPodcastList e) {
    }

    public void loaded(EventLoadPodcastList e) {
    }

    public void finished(EventLoadPodcastList e) {
    }
}