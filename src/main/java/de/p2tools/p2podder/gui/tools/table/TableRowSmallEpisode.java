/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.gui.tools.table;

import de.p2tools.p2podder.controller.config.ProgColorList;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.episode.Episode;
import de.p2tools.p2podder.controller.data.episode.EpisodeFactory;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

public class TableRowSmallEpisode<T extends Episode> extends TableRow {

    public TableRowSmallEpisode() {
    }

    @Override
    public void updateItem(Object f, boolean empty) {
        super.updateItem(f, empty);

        Episode episode = (Episode) f;
        setStyle("");
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setStyle("");
        }

        if (episode != null && !empty) {
            final boolean started = EpisodeFactory.episodeIsStarted(episode);
            final boolean running = EpisodeFactory.episodeIsRunning(episode);
            final boolean error = episode.getStart() != null ? episode.getStart().getStartStatus().isStateError() : false;
            final boolean history = ProgData.getInstance().historyEpisodes.checkIfUrlAlreadyIn(episode.getEpisodeUrl());

            if (episode.getStart() != null && episode.getStart().getStartStatus().isStateError()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(episode.getStart().getStartStatus().getErrorMessage());
                setTooltip(tooltip);
            }

            //und jetzt die Farben setzen
            if (error) {
                if (ProgColorList.EPISODE_ERROR_BG.isUse()) {
                    setStyle(ProgColorList.EPISODE_ERROR_BG.getCssBackground());
                }
                if (ProgColorList.EPISODE_ERROR.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.EPISODE_ERROR.getCssFont());
                    }
                }

            } else if (started && !running) {
                if (ProgColorList.EPISODE_STARTED_BG.isUse()) {
                    setStyle(ProgColorList.EPISODE_STARTED_BG.getCssBackground());
                }
                if (ProgColorList.EPISODE_STARTED.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.EPISODE_STARTED.getCssFont());
                    }
                }

            } else if (running) {
                if (ProgColorList.EPISODE_RUNNING_BG.isUse()) {
                    setStyle(ProgColorList.EPISODE_RUNNING_BG.getCssBackground());
                }
                if (ProgColorList.EPISODE_RUNNING.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.EPISODE_RUNNING.getCssFont());
                    }
                }

            } else if (!error && !started && !running && history) {
                if (ProgColorList.EPISODE_HISTORY_BG.isUse()) {
                    setStyle(ProgColorList.EPISODE_HISTORY_BG.getCssBackground());
                }
                if (ProgColorList.EPISODE_HISTORY.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.EPISODE_HISTORY.getCssFont());
                    }
                }

            } else if (episode.isNew()) {
                //neue Episode
                if (ProgColorList.EPISODE_NEW_BG.isUse()) {
                    setStyle(ProgColorList.EPISODE_NEW_BG.getCssBackground());
                }
                if (ProgColorList.EPISODE_NEW.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.EPISODE_NEW.getCssFont());
                    }
                }
            }
        }
    }
}
