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

import de.p2tools.p2lib.guitools.P2Color;
import de.p2tools.p2podder.controller.config.ProgConfig;

public class PodcastAddAllFactory {
    private PodcastAddAllFactory() {
    }

    public static void init(AddPodcastDto addPodcastDto) {
        if (addPodcastDto.addPodcastData.length == 1) {
            // wenns nur einen Download gibt, macht dann keinen Sinn
            addPodcastDto.btnAll.setVisible(false);
            addPodcastDto.btnAll.setManaged(false);

            addPodcastDto.chkActiveAll.setVisible(false);
            addPodcastDto.chkActiveAll.setManaged(false);
            addPodcastDto.chkGenreAll.setVisible(false);
            addPodcastDto.chkGenreAll.setManaged(false);
            addPodcastDto.chkDescriptionAll.setVisible(false);
            addPodcastDto.chkDescriptionAll.setManaged(false);
            addPodcastDto.chkMaxAgeAll.setVisible(false);
            addPodcastDto.chkMaxAgeAll.setManaged(false);
            addPodcastDto.chkWebsiteAll.setVisible(false);
            addPodcastDto.chkWebsiteAll.setManaged(false);
            addPodcastDto.chkUrlRssAll.setVisible(false);
            addPodcastDto.chkUrlRssAll.setManaged(false);

        } else {
            addPodcastDto.chkActiveAll.getStyleClass().add("checkBoxAll");
            addPodcastDto.chkGenreAll.getStyleClass().add("checkBoxAll");
            addPodcastDto.chkDescriptionAll.getStyleClass().add("checkBoxAll");
            addPodcastDto.chkMaxAgeAll.getStyleClass().add("checkBoxAll");
            addPodcastDto.chkWebsiteAll.getStyleClass().add("checkBoxAll");
            addPodcastDto.chkUrlRssAll.getStyleClass().add("checkBoxAll");

            addPodcastDto.chkActiveAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkActiveAll.isSelected()) {
                    addPodcastDto.initActiveDescription.setActive();
                }
            });
            addPodcastDto.chkGenreAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkGenreAll.isSelected()) {
                    addPodcastDto.initGenre.setGenre();
                }
            });
            addPodcastDto.chkDescriptionAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkDescriptionAll.isSelected()) {
                    addPodcastDto.initActiveDescription.setDescription();
                }
            });
            addPodcastDto.chkMaxAgeAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkMaxAgeAll.isSelected()) {
                    addPodcastDto.initMaxAge.setMaxAge();
                }
            });
            addPodcastDto.chkWebsiteAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkWebsiteAll.isSelected()) {
                    addPodcastDto.initUrl.setUrlWebsite();
                }
            });
            addPodcastDto.chkUrlRssAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addPodcastDto);
                if (addPodcastDto.chkUrlRssAll.isSelected()) {
                    addPodcastDto.initUrl.setUrlRss();
                }
            });

            addPodcastDto.btnAll.setOnAction(a -> changeAll(addPodcastDto));
            addCheckAllCss(addPodcastDto);
        }
    }

    private static void changeAll(AddPodcastDto addPodcastDto) {
        boolean isNotSelected = !isAllSelected(addPodcastDto);

        addPodcastDto.chkActiveAll.setSelected(isNotSelected);
        addPodcastDto.chkGenreAll.setSelected(isNotSelected);
        addPodcastDto.chkDescriptionAll.setSelected(isNotSelected);
        addPodcastDto.chkMaxAgeAll.setSelected(isNotSelected);
        addPodcastDto.chkWebsiteAll.setSelected(isNotSelected);
        addPodcastDto.chkUrlRssAll.setSelected(isNotSelected);

        addCheckAllCss(addPodcastDto);
    }

    private static void addCheckAllCss(AddPodcastDto addPodcastDto) {
        if (isAllSelected(addPodcastDto)) {
            final String c = P2Color.getCssColor(PodcastAddDialogFactory.getBlue(), false);
            addPodcastDto.btnAll.setStyle("-fx-text-fill: #" + c);

        } else {
            if (ProgConfig.SYSTEM_DARK_THEME.getValue()) {
                addPodcastDto.btnAll.setStyle("-fx-text-fill: white");
            } else {
                addPodcastDto.btnAll.setStyle("-fx-text-fill: black");
            }
        }
    }

    private static boolean isAllSelected(AddPodcastDto addPodcastDto) {
        return addPodcastDto.chkActiveAll.isSelected() ||
                addPodcastDto.chkGenreAll.isSelected() ||
                addPodcastDto.chkDescriptionAll.isSelected() ||
                addPodcastDto.chkMaxAgeAll.isSelected() ||
                addPodcastDto.chkWebsiteAll.isSelected() ||
                addPodcastDto.chkUrlRssAll.isSelected();
    }
}
