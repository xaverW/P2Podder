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

import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.data.podcast.Podcast;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.*;

import java.util.ArrayList;

public class AddPodcastDto {

    public boolean addNewDownloads = true;

    public InitConstValues initConstValues;
    public InitActiveDescription initActiveDescription;
    public InitGenre initGenre;
    public InitMaxAge initMaxAge;
    public InitUrl initUrl;

    public ProgData progData;
    public AddPodcastData[] addPodcastData;

    public IntegerProperty actFilmIsShown = new SimpleIntegerProperty(0);

    public final Button btnPrev = new Button("<");
    public final Button btnNext = new Button(">");
    public final Label lblSum = new Label("");
    public Button btnAll = new Button("Für alle\nändern");

    public CheckBox chkActiveAll = new CheckBox();
    public CheckBox chkGenreAll = new CheckBox();
    public CheckBox chkDescriptionAll = new CheckBox();
    public CheckBox chkMaxAgeAll = new CheckBox();
    public CheckBox chkWebsiteAll = new CheckBox();
    public CheckBox chkUrlRssAll = new CheckBox();

    // const
    public final Label lblNo = new Label();
    public final Label lblSumEpisodes = new Label();
    public final Label lblGenDate = new Label();

    // Name
    public final TextField txtName = new TextField();
    // active
    public final CheckBox chkActive = new CheckBox("");
    // genre
    public final ComboBox<String> cboGenre = new ComboBox<>();
    // Beschreibung
    public final TextArea textAreaDescription = new TextArea();
    // maxAge
    public final Slider slMaxAge = new Slider();
    public final Label lblMaxAge = new Label();
    // URL
    public final TextField txtWebsite = new TextField();
    public final TextField txtUrlRss = new TextField();

    public AddPodcastDto(ProgData progData, ArrayList<Podcast> filmsToDownloadList, boolean addNew) {
        // einen neuen Download anlegen
        this.progData = progData;

        if (addNew) {
            addPodcastData = InitPodcastAddArray.initDownloadInfoArrayNewPodcast(filmsToDownloadList, this);
            initConstValues = new InitConstValues(this);
            initActiveDescription = new InitActiveDescription(this);
            initGenre = new InitGenre(this);
            initMaxAge = new InitMaxAge(this);
            initUrl = new InitUrl(this);
        } else {
            this.addNewDownloads = false;

            addPodcastData = InitPodcastAddArray.initDownloadInfoArrayPodcast(filmsToDownloadList, this);
            initConstValues = new InitConstValues(this);
            initActiveDescription = new InitActiveDescription(this);
            initGenre = new InitGenre(this);
            initMaxAge = new InitMaxAge(this);
            initUrl = new InitUrl(this);
        }
    }

    public AddPodcastData getAct() {
        return addPodcastData[actFilmIsShown.getValue()];
    }

    public void updateAct() {
        final int nr = actFilmIsShown.getValue() + 1;
        lblSum.setText("Film " + nr + " von " + addPodcastData.length + " Filmen");

        if (actFilmIsShown.getValue() == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(false);
        } else if (actFilmIsShown.getValue() == addPodcastData.length - 1) {
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
        } else {
            btnPrev.setDisable(false);
            btnNext.setDisable(false);
        }

        initConstValues.makeAct();
        initActiveDescription.makeAct();
        initGenre.makeAct();
        initMaxAge.makeAct();
        initUrl.makeAct();
    }
}
