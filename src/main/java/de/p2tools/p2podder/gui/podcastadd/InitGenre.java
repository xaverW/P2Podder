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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class InitGenre {
    private final ObservableList<String> genreList;
    AddPodcastDto addPodcastDto;

    public InitGenre(AddPodcastDto addPodcastDto) {
        this.addPodcastDto = addPodcastDto;
        this.genreList = FXCollections.observableArrayList();
        init();
    }

    private void init() {
        ProgData.getInstance().podcastList.forEach(podcast -> {
            String genre = podcast.getGenre();
            if (!genre.isEmpty() && !genreList.contains(genre)) {
                genreList.add(genre);
            }
        });

        if (genreList.isEmpty() ||
                genreList.size() == 1 && genreList.get(0).isEmpty()) {
            //leer oder und nur ein leerer Eintrag
            genreList.clear();
            genreList.add("Nachrichten");
        }

        addPodcastDto.cboGenre.setEditable(true);
        addPodcastDto.cboGenre.setItems(genreList);

        addPodcastDto.cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Genre: getEditor " + addPodcastDto.cboGenre.getEditor().getText());
            if (!addPodcastDto.cboGenre.isFocused()) {
                System.out.println("not");
                return;
            }
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                setGenre();
            }
        });
    }

    public void makeAct() {
        if (!addPodcastDto.cboGenre.getItems().contains(addPodcastDto.getAct().podcast.getGenre())) {
            addPodcastDto.cboGenre.getItems().add(addPodcastDto.getAct().podcast.getGenre());
        }
        addPodcastDto.cboGenre.getSelectionModel().select(addPodcastDto.getAct().podcast.getGenre());
    }

    public void setGenre() {
        // beim Ändern der cbo oder manuellem Eintragen
        String genre = addPodcastDto.cboGenre.getEditor().getText();
        if (!addPodcastDto.cboGenre.getItems().contains(genre)) {
            addPodcastDto.cboGenre.getItems().add(genre);
        }

        if (addPodcastDto.chkGenreAll.isSelected()) {
            Arrays.stream(addPodcastDto.addPodcastData).forEach(addPodcastData -> {
                addPodcastData.podcast.setGenre(genre);
            });
        } else {
            addPodcastDto.getAct().podcast.setGenre(genre);
        }
    }
}
