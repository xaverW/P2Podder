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


package de.p2tools.p2podder.controller.data;

import de.p2tools.p2lib.tools.shortcut.PShortcut;
import de.p2tools.p2podder.controller.config.ProgConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class P2PodderShortCuts {
    // Menü
    public static final PShortcut SHORTCUT_QUIT_PROGRAM =
            new PShortcut(ProgConfig.SHORTCUT_QUIT_PROGRAM, ProgConfig.SHORTCUT_QUIT_PROGRAM_INIT,
                    "Programm beenden",
                    "Das Programm wird beendet.");

    // Tabelle Episode
    public static final PShortcut SHORTCUT_UPDATE_PODCAST =
            new PShortcut(ProgConfig.SHORTCUT_UPDATE_PODCAST, ProgConfig.SHORTCUT_UPDATE_PODCAST_INIT,
                    "Podcast aktualisieren",
                    "Die markierten Podcasts in der Tabelle \"Podcast\" wirden aktualisiert");

    public static final PShortcut SHORTCUT_SAVE_PODCAST =
            new PShortcut(ProgConfig.SHORTCUT_SAVE_PODCAST, ProgConfig.SHORTCUT_SAVE_PODCAST_INIT,
                    "Podcast speichern",
                    "Der markierte Podcast in der Tabelle \"Podcast\" wird gespeichert.");

    public static final PShortcut SHORTCUT_EPOSODE_START =
            new PShortcut(ProgConfig.SHORTCUT_EPISODE_START, ProgConfig.SHORTCUT_EPISODE_START_INIT,
                    "Episode abspielen",
                    "Die markierte Episode in der Tabelle \"Episoden\" wird gestartet.");
    public static final PShortcut SHORTCUT_EPISODE_STOP =
            new PShortcut(ProgConfig.SHORTCUT_EPISODE_STOP, ProgConfig.SHORTCUT_EPISODE_STOP_INIT,
                    "Episode stoppen",
                    "Die markierte Episode in der Tabelle \"Episoden\" wird gestoppt.");
    public static final PShortcut SHORTCUT_EPISODE_CHANGE =
            new PShortcut(ProgConfig.SHORTCUT_EPISODE_CHANGE, ProgConfig.SHORTCUT_EPISODE_CHANGE_INIT,
                    "Episode ändern",
                    "Die markierte Episode in der Tabelle \"Episoden\" kann geändert werden.");

    private static ObservableList<PShortcut> shortcutList = FXCollections.observableArrayList();

    public P2PodderShortCuts() {
        shortcutList.add(SHORTCUT_QUIT_PROGRAM);

        shortcutList.add(SHORTCUT_UPDATE_PODCAST);
        shortcutList.add(SHORTCUT_SAVE_PODCAST);

        shortcutList.add(SHORTCUT_EPOSODE_START);
        shortcutList.add(SHORTCUT_EPISODE_STOP);
        shortcutList.add(SHORTCUT_EPISODE_CHANGE);
    }

    public static synchronized ObservableList<PShortcut> getShortcutList() {
        return shortcutList;
    }
}
