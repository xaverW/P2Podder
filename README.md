[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)

# P2Podder

P2Podder ist ein Podcatcher. Mit dem Programm können Podcasts verwaltet werden. Es können Podcasts angelegt werden,
diese können dann immer wieder auf neue Episoden geprüft werden. Die neuen Episoden werden heruntergeladen und können
abgespielt werden.
<br />

Das Programm ist noch in der Entwicklung. Wer es Testen will, ist herzlich eingeladen. Anregungen, Vorschläge und 
Hinweise gerne per Mail. Da das Programm noch in der Entwicklung steckt, kann es sich noch deutlich verändern.
Gedacht sind die jetzigen Veröffentlichungen zum Testen.

## Infos

Das Programm nutzt den Ordner ".p2Podder" unter Linux und den versteckten Ordner "p2Podder" unter Windows als
Konfig-Ordner. Man kann dem Programm auch einen Ordner für die Einstellungen mitgeben (und es z.B. auf einem USB-Stick
verwenden):

```
java -jar P2Podder.jar ORDNER
```

Weitere Infos über das Programm können auf der Website nachgelesen werden.
https://www.p2tools.de/p2podder/
<br />

## Systemvoraussetzungen

Unterstützt wird Windows und Linux.

Das Programm benötigt unter Windows und Linux eine aktuelle Java-VM ab Version: Java 11. Für Linux-Benutzer wird
OpenJDK11 empfohlen. (FX-Runtime bringt das Programm bereits mit und muss nicht installiert werden).
<br />

## Download

Das Programm wird in drei Paketen angeboten. Diese unterscheiden sich nur im "Zubehör", das Programm selbst ist in allen
Paketen identisch:

- **P2Podder-XX.zip**  
  Das Programmpaket bringt nur das Programm und die benötigten "Hilfsprogramme" aber kein Java mit. Auf dem Rechner muss
  eine Java-Laufzeitumgebung ab Java11 installiert sein. Dieses Programmpaket kann auf allen Betriebssystemen verwendet
  werden. Es bringt Startdateien für Linux und Windows mit.

- **P2Podder-XX__Linux+Java.zip**  
  **P2Podder-XX__Win+Java.zip**  
  Diese Programmpakete bringen die Java-Laufzeitumgebung mit und sind nur für das angegebene Betriebssystem: Linux oder
  Windows. Es muss kein Java auf dem System installiert sein. (Die Java-Laufzeitumgebung liegt im Ordner "Java" und
  kommt von jdk.java.net).

zum Download: [github.com/xaverW/P2Podder/releases](https://github.com/xaverW/P2Podder/releases)
<br />

## Website

[www.p2tools.de/p2podder/]( https://www.p2tools.de/p2podder/)
