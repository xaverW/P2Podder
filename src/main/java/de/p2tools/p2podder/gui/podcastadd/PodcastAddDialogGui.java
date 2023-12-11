/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2podder.gui.podcastadd;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PodcastAddDialogGui {

    private final AddPodcastDto addPodcastDto;
    private final VBox vBoxCont;
    private final HBox hBoxTop = new HBox();
    private final GridPane gridPane = new GridPane();

    public PodcastAddDialogGui(AddPodcastDto addPodcastDto, VBox vBoxCont) {
        this.addPodcastDto = addPodcastDto;
        this.vBoxCont = vBoxCont;
    }

    public void addCont() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(5));
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        int row = 0;

        if (addPodcastDto.addPodcastData.length > 1) {
            // Top, nur wenn es mehre gibt
            hBoxTop.getStyleClass().add("downloadDialog");
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);
            hBoxTop.setPadding(new Insets(5));
            hBoxTop.getChildren().addAll(addPodcastDto.btnPrev, addPodcastDto.lblSum, addPodcastDto.btnNext);
            vBoxCont.setPadding(new Insets(10));
            vBoxCont.getChildren().add(hBoxTop);

            Font font = Font.font(null, FontWeight.BOLD, -1);
            addPodcastDto.btnAll.setFont(font);
            addPodcastDto.btnAll.setWrapText(true);
            addPodcastDto.btnAll.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.add(addPodcastDto.btnAll, 3, row, 1, 2);
            GridPane.setValignment(addPodcastDto.btnAll, VPos.TOP);
            ++row;
        }

        // Nr
        gridPane.add(new Label("Nr:"), 0, row);
        gridPane.add(addPodcastDto.lblNo, 1, row, 2, 1);

        // Name
        gridPane.add(PodcastAddDialogFactory.getText("Name:"), 0, ++row);
        gridPane.add(addPodcastDto.txtName, 1, row, 2, 1);

        // Aktiv
        gridPane.add(PodcastAddDialogFactory.getText("Aktiv:"), 0, ++row);
        gridPane.add(addPodcastDto.chkActive, 1, row);
        gridPane.add(addPodcastDto.chkActiveAll, 3, row);

        // Genre
        addPodcastDto.cboGenre.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(PodcastAddDialogFactory.getText("Genre:"), 0, ++row);
        gridPane.add(addPodcastDto.cboGenre, 1, row, 2, 1);
        gridPane.add(addPodcastDto.chkGenreAll, 3, row);

        // Beschreibung
        gridPane.add(PodcastAddDialogFactory.getText("Beschreibung:"), 0, ++row);
        gridPane.add(addPodcastDto.textAreaDescription, 1, row, 2, 1);
        gridPane.add(addPodcastDto.chkDescriptionAll, 3, row);

        // Episoden
        gridPane.add(new Label("Episoden:"), 0, ++row);
        gridPane.add(addPodcastDto.lblSumEpisodes, 1, row, 2, 1);

        // maxAge
        VBox vBox = new VBox(2);
        vBox.getChildren().addAll(addPodcastDto.lblMaxAge, addPodcastDto.slMaxAge);
        vBox.setAlignment(Pos.CENTER_RIGHT);

        gridPane.add(PodcastAddDialogFactory.getText("Alter:"), 0, ++row);
        gridPane.add(vBox, 1, row, 2, 1);
        gridPane.add(addPodcastDto.chkMaxAgeAll, 3, row);

        // Website
        gridPane.add(PodcastAddDialogFactory.getText("Website:"), 0, ++row);
        gridPane.add(addPodcastDto.txtWebsite, 1, row, 2, 1);
        gridPane.add(addPodcastDto.chkWebsiteAll, 3, row);

        // UrlRss
        gridPane.add(PodcastAddDialogFactory.getText("RSS-URL:"), 0, ++row);
        gridPane.add(addPodcastDto.txtUrlRss, 1, row, 2, 1);
        gridPane.add(addPodcastDto.chkUrlRssAll, 3, row);

        // Erstellt
        gridPane.add(new Label("Erstellt:"), 0, ++row);
        gridPane.add(addPodcastDto.lblGenDate, 1, row, 2, 1);

        vBoxCont.getChildren().add(gridPane);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSizeCenter());
    }

    public void init() {
        if (addPodcastDto.addPodcastData.length == 1) {
            // wenns nur einen Download gibt, macht dann keinen Sinn
            hBoxTop.setVisible(false);
            hBoxTop.setManaged(false);
        }
        PodcastAddAllFactory.init(addPodcastDto);
    }
}
