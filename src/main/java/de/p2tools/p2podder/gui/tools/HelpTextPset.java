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

package de.p2tools.p2podder.gui.tools;

public class HelpTextPset {

    public static final String HELP_PSET =
            "Ein Set ist ein Hilfsprogramm (z.B. VLC), mit dem die Episoden " +
                    "abgespielt werden können." +
                    "\n" +
                    "\n" +
                    "Das Standardset ist immer das erste in der Tabelle. Ist nur ein Set angelegt, " +
                    "ist es immer das Standardset. " +
                    "Das Standardset wird beim Abspielen einer Episode über die Buttons zum Abspielen verwendet. " +
                    "Sind weitere Sets vorhanden, " +
                    "können über das Menü damit Episoden gestartet werden. " +
                    "\n" +
                    "\n" +
                    "==================================================" +
                    "\n" +
                    "\n" +
                    "Set Einstellungen: " +
                    "\n" +
                    "\n" +
                    "\"Name\"\n" +
                    "Hier kann ein Name für das Set angegeben werden. Er ist nur zur eigenen Info." +
                    "\n" +
                    "\n" +
                    "\"Beschreibung\"\n" +
                    "Hier können Infos zum Set angegeben werden die auch nur zur eigenen Info dienen." +
                    "\n" +
                    "\n" +
                    "\"Programm\"\n" +
                    "In dem Feld steht NUR!! das Programm: \"Pfad/Programmdatei\"" +
                    "\n" +
                    "\n" +
                    "\"Schalter\"\n" +
                    "In diesem Feld werden die Programmschalter angegeben, die das Programm zum Start braucht. " +
                    "Der Parameter %f steht für den Pfad und Dateinamen der Episode und wird beim Start der " +
                    "Episode ersetzt." +
                    "\n" +
                    "\n" +
                    "==================================================" +
                    "\n" +
                    "\n" +
                    "Beispiel für den VLC:" +
                    "\n" +
                    "\n" +
                    "Programm (Linux):  \"/usr/bin/vlc\"\n" +
                    "Programm (Windows):  \"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"" +
                    "\n" +
                    "\n" +
                    "Schalter: \"%f --play-and-exit" +
                    "\n" +
                    "\n" +
                    "Hier wird %f durch den Pfad zur Datei ersetzt. Der Programmaufruf sieht dann so aus:" +
                    "\n" +
                    "\n" +
                    "/usr/bin/vlc PFAD-DATEINAME --play-and-exit" +
                    "\n";
}
