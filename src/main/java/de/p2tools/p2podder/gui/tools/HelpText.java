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

import de.p2tools.p2lib.P2LibConst;

public class HelpText {
    
    public static final String SHORTCUT =
            "Zum Ändern eines Tastenkürzels, seinen \"Ändern\"-Button klicken und dann " +
                    "die gewünschten neuen Tasten drücken.\n" +
                    "\n" +
                    "Der \"Zurücksetzen\"-Button stellt den Originalzustand wieder her.\n" +
                    "\n" +
                    "Damit die Änderungen wirksam werden, muss das Programm neu gestartet werden." +
                    "\n";

    public static final String PROG_PATHS = "Hiermit kann das Standardprogramm zum \"Abspielen\" " +
            "der Episoden eingetragen werden. Wird der Pfad nicht automatisch erkannt, " +
            "kann man ihn auch per Hand auswählen." +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Um alle Podcasts/Episoden abspielen zu können, muss " +
            "ein Programm installiert sein, z.B. VLC. " +
            "Der VLC-Player ist zum Abspielen der Episoden gut geeignet und kann so geladen werden:" +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Linux:" + P2LibConst.LINE_SEPARATOR +
            "VLC kann über die Paketverwaltung eingespielt werden." + P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Windows:" + P2LibConst.LINE_SEPARATOR +
            "VLC ist hier zu finden: " + "http://www.videolan.org" + P2LibConst.LINE_SEPARATOR;

    public static final String FILTER_EXACT =
            P2LibConst.LINE_SEPARATOR +
                    "Groß- und Kleinschreibung wird beim Filtern " +
                    "nicht beachtet." +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "In allen Feldern " +
                    "kann auch nach mehreren Begriffen gesucht werden (diese " +
                    "werden durch \"Komma\" oder \"Doppelpunkt\" getrennt angegeben " +
                    "und können auch Leerzeichen enthalten)." +
                    P2LibConst.LINE_SEPARATOR +
                    "\"Sport,Fussball\" sucht nach Episoden die im jeweiligen Feld den " +
                    "Begriff \"Sport\" ODER \"Fussball\" haben." +
                    P2LibConst.LINE_SEPARATOR +
                    "\"Sport:Fussball\" sucht nach Episoden die im jeweiligen Feld den " +
                    "Begriff \"Sport\" UND \"Fussball\" haben." +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "In allen Feldern mit Texteingabe " +
                    "kann auch mit regulären Ausdrücken gesucht " +
                    "werden. Diese müssen mit \"#:\" eingeleitet werden. " +
                    "Auch bei den regulären Ausdrücken wird nicht zwischen " +
                    "Groß- und Kleinschreibung unterschieden. " +
                    P2LibConst.LINE_SEPARATOR +
                    "#:Abend.*" + P2LibConst.LINE_SEPARATOR +
                    "Das bedeutet z.B.: Es werden alle Episoden gefunden, die " + P2LibConst.LINE_SEPARATOR +
                    "im jeweiligen Feld mit \"Abend\" beginnen." + P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "https://de.wikipedia.org/wiki/Regul%C3%A4rer_Ausdruck" + P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR;

    public static final String GUI_STATION_FILTER =
            FILTER_EXACT +
                    P2LibConst.LINE_SEPARATOR +
                    "Filterprofile:" + P2LibConst.LINE_SEPARATOR +
                    "Mit den Buttons unten (Pfeil nach rechts/links), kann man die verwendeten Filter " +
                    "\"vor- und zurückspulen\"." +
                    "\n";

    public static final String GUI_STATIONS_EDIT_FILTER = "Hier können die angezeigten Filter " +
            "ein- und ausgeschaltet werden. " +
            "Ausgeschaltete Filter werden beim Suchen auch nicht berücksichtigt. " +
            "Mit weniger Filtern ist auch der Suchvorgang schneller" +
            "\n";

    public static final String CONFIG_STYLE = "Hier kann die Schriftgröße des Programms angepasst werden." + P2LibConst.LINE_SEPARATORx2 +
            "Die sollte sich automatisch an die vorgegebene Größe im Betriebssystem einstellen. " +
            "Wenn die Automatik nicht korrekt funktioniert oder eine andere gewünscht wird, kann " +
            "sie hier angepasst werden." +
            P2LibConst.LINE_SEPARATORx2 +
            "Damit die Änderungen wirksam werden, kann ein Programmneustart notwendig sein." +
            "\n";

    public static final String LOAD_PODCASTS_EVERY_DAYS = "Einmal am Tag (beim ersten Programmstart) werden die Podcasts nach " +
            "neuen Episoden durchsucht. " + P2LibConst.LINE_SEPARATOR +
            "Wenn auch vorgegeben, werden die neuen Episoden " +
            "gleich gespeichert und in der Liste der Episoden angezeigt." +
            "\n";

    public static final String SMALL_BUTTON = "In den Tabellen (Episoden, Podcasts und Downloads) können auch " +
            "kleine Buttons angezeigt werden. Die Zeilenhöhe wird dadurch kleiner." +
            "\n";

    public static final String TRAY =
            "Im System Tray wird für das Programm ein Symbol angezeigt. " +
                    "Damit kann das Programm auf dem Desktop ausgeblendet werden." +
                    "\n";

    public static final String TRAY_OWN_ICON =
            "Im System Tray wird für das Programm ein Symbol angezeigt. " +
                    "Damit kann ein eigens Bild dafür verwendet werden." +
                    "\n";

    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein." +
            "\n";

    public static final String USER_AGENT = "Hier kann ein User Agent angegeben werden. " +
            "Beim Laden der Podcasts/Episoden wird er dann als Programmname verwendet. Es sollte der Name des Programms " +
            " enthalten sein. Wird kein User Agent angegeben, wird auch keiner verwendet." +
            P2LibConst.LINE_SEPARATORx2 +
            "(Es sind nur ASCII-Zeichen erlaubt und die Textlänge ist begrenzt auf 100 Zeichen)" +
            "\n";

    public static final String LOGFILE = "Hier kann ein Ordner angegeben werden " +
            "in dem ein Logfile erstellt wird. Darin wird der Programmverlauf skizziert. " +
            "Das kann hilfreich sein, wenn das Programm nicht wie erwartet funktioniert." + P2LibConst.LINE_SEPARATORx2 +
            "Der Standardordner ist \"Log\" im Konfigordner des Programms." + P2LibConst.LINE_SEPARATORx2 +
            "Wird der Pfad zum Logfile geändert, wirkt sich das erst beim Neustart des Programms " +
            "aus. Mit dem Button \"Pfad zum Logfile jetzt schon verwenden\" wird die Programmausgabe ab " +
            "Klick darauf ins neue Logfile geschrieben." +
            "\n";

    public static final String DEST_DIR = "Hier wird der Speicherort für die Episoden der Podcasts angegeben." +
            P2LibConst.LINE_SEPARATORx2 +
            "Episoden werden geladen und in diesem Ordner gespeichert. Dort werden dann auch die Episoden " +
            "zum Abspielen gesucht." +
            "\n";

    public static final String DEST_DIR_CLEAN = "Im Ordner (Speicherort der Episoden der Podcasts) werden alle " +
            "Dateien gelöscht, für die es keine Episode gibt." +
            "\n";

    public static final String DEST_DIR_EPISODES = "Hier wird der Speicherort der Episoden angegeben." +
            P2LibConst.LINE_SEPARATORx2 +
            "Podcasts werden geladen und in diesem Ordner werden die Episoden gespeichert. " +
            "Dort werden dann auch die Episoden zum Abspielen gesucht." +
            "\n";

    public static final String WEBBROWSER = "Wenn das Programm versucht, einen Link zu öffnen " +
            "(z.B. den Link im Menüpunkt \"Hilfe\" zu den \"Hilfeseiten\") " +
            "und die Standardanwendung (z.B. \"Firefox\") nicht startet, " +
            "kann damit ein Programm ausgewählt und " +
            "fest zugeordnet werden (z.B. der Browser \"Firefox\")." +
            "\n";

    public static final String RESET_DIALOG =
            "==> Einstellungen zum Abspielen zurücksetzen" +

                    P2LibConst.LINE_SEPARATORx2 +
                    "Damit werden alle Sets (auch eigene), die zum Abspielen der " +
                    "Episoden gebraucht werden, gelöscht. Anschließend wird das aktuelle Standardset eingerichtet. " +
                    "Es kann dann direkt damit weitergearbeitet werden." +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Das sollte vor dem kompletten Zurücksetzen des Programms versucht werden. " +

                    P2LibConst.LINE_SEPARATORx3 +
                    "=====   ODER   =====" +
                    P2LibConst.LINE_SEPARATORx3 +

                    "==> Alle Einstellungen zurücksetzen" +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Damit wird das Programm in den Ursprungszustand zurückgesetzt. Es gehen " +
                    "ALLE Einstellungen verloren. Das Programm beendet sich " +
                    "und muss neu gestartet werden. Der neue Start beginnt " +
                    "mit dem Einrichtungsdialog." +
                    P2LibConst.LINE_SEPARATOR;
    public static final String PSET_STANDARD = "Das Standardset wird zum Abspielen der Episoden " +
            "verwendet. Es können auch weitere Sets angelegt werden. Beim Start kann man dann auswählen, " +
            "mit welchem Set die Episode abgespielt werden soll. " +
            "Ist nur ein Set angelegt, ist es immer das Standardset." +
            "\n";
    public static final String HELP_PSET = "\n" +
            "Ein Set ist ein Satz von Hilfsprogrammen, an welches ein Pfad zu einer Episode übergeben wird. " +
            "Mit dem Programm wird dann die Episode abgespielt.\n" +
            "\n" +
            "\n" +
            "====================\n" +
            "Hilfsprogramme:\n" +
            "===\n" +
            "\n" +
            "Hier werden die Programme zum jeweiligen Set eingetragen. Sind mehrere Programme eingetragen, " +
            "kann man zu jedem Programm über die Felder Präfix und Suffix wählen, für welchen Dateinamen ein Programm " +
            "zuständig ist.\n" +
            "\n" +
            "\"Programm\": In dem Feld steht NUR!! das Programm: \"Pfad/Programmdatei\"\n" +
            "\n" +
            "\"Schalter\": In diesem Feld werden die Programmschalter angegeben, die das Programm zum Start braucht. " +
            "Mögliche Parameter sind:\n" +
            "\n" +
            "Diese Angaben werden durch den Pfad ersetzt:\n" +
            "%f ist der Pfad/Datei der Episode\n" +
            "\n" +
            "==================================================\n" +
            "Beispiel für den VLC:\n" +
            "Programm: \"/usr/bin/vlc\"\n" +
            "Schalter: \"%f --play-and-exit :http-user-agent=\"Mozilla/5.0\"\n" +
            "\n" +
            "Hier wird %f durch dem Pfad/Datei der Episode ersetzt. " +
            "\n";
}
