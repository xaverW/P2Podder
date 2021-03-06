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

package de.p2tools.p2podder.gui.tools;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import de.p2tools.p2podder.controller.config.ProgConfig;
import de.p2tools.p2podder.controller.config.ProgConst;
import de.p2tools.p2podder.controller.config.ProgData;
import de.p2tools.p2podder.controller.config.ProgInfos;
import de.p2tools.p2podder.controller.data.ProgramData;
import de.p2tools.p2podder.controller.data.SetData;
import de.p2tools.p2podder.controller.data.SetDataList;
import de.p2tools.p2podder.controller.starterEpisode.StartRuntimeExec;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SetsPrograms {

    private static final ArrayList<String> winPath = new ArrayList<>();

    private static void setWinProgPath() {
        String pfad;
        if (System.getenv("ProgramFiles") != null) {
            pfad = System.getenv("ProgramFiles");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        if (System.getenv("ProgramFiles(x86)") != null) {
            pfad = System.getenv("ProgramFiles(x86)");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        final String[] PATH = {"C:\\Program Files", "C:\\Programme", "C:\\Program Files (x86)"};
        for (final String s : PATH) {
            if (new File(s).exists() && !winPath.contains(s)) {
                winPath.add(s);
            }
        }
    }

    public static String getTemplatePathVlc() {
        // liefert den Standardpfad f??r das entsprechende BS
        // Programm muss auf dem Rechner instelliert sein
        final String PATH_LINUX_VLC = "/usr/bin/vlc";
        final String PATH_FREEBSD = "/usr/local/bin/vlc";
        final String PATH_WIN = "\\VideoLAN\\VLC\\vlc.exe";
        String path = "";
        try {
            switch (ProgramTools.getOs()) {
                case LINUX:
                    if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
                        path = PATH_FREEBSD;
                    } else {
                        path = PATH_LINUX_VLC;
                    }
                    break;
                default:
                    setWinProgPath();
                    for (final String s : winPath) {
                        path = s + PATH_WIN;
                        if (new File(path).exists()) {
                            break;
                        }
                    }
            }
            if (!new File(path).exists() && System.getenv("PATH_VLC") != null) {
                path = System.getenv("PATH_VLC");
            }
            if (!new File(path).exists()) {
                path = "";
            }
        } catch (final Exception ignore) {
        }
        return path;
    }

    public static String getTemplatePathFlv() {
        // liefert den Standardpfad f??r das entsprechende BS
        // bei Win+Mac wird das Programm mitgeliefert und liegt
        // im Ordner "bin" der mit dem Programm mitgeliefert wird
        // bei Linux muss das Programm auf dem Rechner instelliert sein
        final String PATH_LINUX_FLV = "/usr/bin/flvstreamer";
        final String PATH_FREEBSD = "/usr/local/bin/flvstreamer";
        final String PATH_WINDOWS_FLV = "bin\\flvstreamer_win32_latest.exe";
        String path = "";
        try {
            switch (ProgramTools.getOs()) {
                case LINUX:
                    if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
                        path = PATH_FREEBSD;
                    } else {
                        path = PATH_LINUX_FLV;
                    }
                    break;
                default:
                    path = PATH_WINDOWS_FLV;
            }
            if (!new File(path).exists() && System.getenv("PATH_FLVSTREAMER") != null) {
                path = System.getenv("PATH_FLVSTREAMER");
            }
            if (!new File(path).exists()) {
                path = "";
            }
        } catch (final Exception ignore) {
        }
        return path;
    }

    public static String getTemplatePathFFmpeg() {
        // liefert den Standardpfad f??r das entsprechende BS
        // bei Win+Mac wird das Programm mitgeliefert und liegt
        // im Ordner "bin" der mit dem Programm mitgeliefert wird
        // bei Linux muss das Programm auf dem Rechner installiert sein
        final String PATH_LINUX_FFMPEG = "/usr/bin/ffmpeg";
        final String PATH_FREEBSD_FFMPEG = "/usr/local/bin/ffmpeg";
        final String PATH_WINDOWS_FFMPEG = "bin\\ffmpeg.exe";
        String path = "";
        try {
            switch (ProgramTools.getOs()) {
                case LINUX:
                    if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
                        path = PATH_FREEBSD_FFMPEG;
                    } else {
                        path = PATH_LINUX_FFMPEG;
                    }
                    break;
                default:
                    path = PATH_WINDOWS_FFMPEG;
            }
            if (!new File(path).exists() && System.getenv("PATH_FFMPEG") != null) {
                path = System.getenv("PATH_FFMPEG");
            }
            if (!new File(path).exists()) {
                path = "";
            }
        } catch (final Exception ignore) {
        }
        return path;
    }

    public static String getPathScript() {
        // liefert den Standardpfad zum Script "Ansehen" f??r das entsprechende BS
        // liegt im Ordner "bin" der mit dem Programm mitgeliefert wird
        String path;
        final String PATH_LINUX_SCRIPT = "bin/flv.sh";
        final String PATH_WINDOWS_SCRIPT = "bin\\flv.bat";
        switch (ProgramTools.getOs()) {
            case LINUX:
                path = ProgInfos.getPathJar() + PATH_LINUX_SCRIPT;
                break;
            default:
                path = PATH_WINDOWS_SCRIPT;
        }
        return path;
    }

    public static boolean addSetTemplate(SetDataList pSet) {
        if (pSet == null) {
            return false;
        }
        for (final SetData ps : pSet) {
            if (!ps.getAdOn().isEmpty() && !addOnZip(ps.getAdOn())) {
                // und Tsch??ss
                return false;
            }
        }

        if (ProgData.getInstance().setDataList.addSetData(pSet)) {
            ProgConfig.SYSTEM_UPDATE_PROGSET_VERSION.setValue(pSet.version);
            return true;
        } else {
            return false;
        }
    }

    private static boolean addOnZip(String file) {
        final String destPath = PFileUtils.addsPath(ProgInfos.getPathJar(), "bin");
        File zipFile;
        final int timeout = 10_000; //10 Sekunden
        int n;
        HttpURLConnection conn;
        try {
            if (!PUrlTools.isUrl(file)) {
                zipFile = new File(file);
                if (!zipFile.exists()) {
                    // und Tsch??ss
                    return false;
                }
                if (file.endsWith(ProgConst.FORMAT_ZIP)) {
                    if (!unpack(zipFile, new File(destPath))) {
                        // und Tsch??ss
                        return false;
                    }
                } else {
                    try (FileInputStream in = new FileInputStream(file);
                         FileOutputStream fOut = new FileOutputStream(PFileUtils.addsPath(destPath, file))) {
                        final byte[] buffer = new byte[1024];
                        while ((n = in.read(buffer)) != -1) {
                            fOut.write(buffer, 0, n);
                        }
                    }
                }
            } else {
                conn = (HttpURLConnection) new URL(file).openConnection();
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                conn.setRequestProperty("User-Agent", ProgInfos.getUserAgent());
                if (file.endsWith(ProgConst.FORMAT_ZIP)) {

                    final File tmpFile = File.createTempFile("mtplayer", null);
                    tmpFile.deleteOnExit();
                    try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                         FileOutputStream fOut = new FileOutputStream(tmpFile)) {
                        final byte[] buffer = new byte[1024];
                        while ((n = in.read(buffer)) != -1) {
                            fOut.write(buffer, 0, n);
                        }
                    }
                    if (!unpack(tmpFile, new File(destPath))) {
                        // und Tsch??ss
                        return false;
                    }

                } else {
                    final String fileName = PUrlTools.getFileName(file);
                    final File f = new File(PFileUtils.addsPath(destPath, fileName));
                    try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                         FileOutputStream fOut = new FileOutputStream(f)) {
                        final byte[] buffer = new byte[1024];
                        while ((n = in.read(buffer)) != -1) {
                            fOut.write(buffer, 0, n);
                        }
                    }
                }
            }
        } catch (final Exception ignored) {
        }
        return true;
    }

    private static boolean unpack(File archive, File destDir) throws Exception {
        if (!destDir.exists()) {
            return false;
        }


        try (ZipFile zipFile = new ZipFile(archive)) {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();

            final byte[] buffer = new byte[16384];
            int len;
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();

                final String entryFileName = entry.getName();

                final File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                if (!entry.isDirectory()) {
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entryFileName)));
                         BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry))) {
                        while ((len = bis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                        bos.flush();
                    }
                }
            }
        }

        return true;
    }

    private static File buildDirectoryHierarchyFor(String entryName, File destDir) {
        final int lastIndex = entryName.lastIndexOf('/');
        final String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

    public static boolean testPrefix(String str, String uurl, boolean prefix) {
        //pr??fen ob url beginnt/endet mit einem Argument in str
        //wenn str leer dann true
        boolean ret = false;
        final String url = uurl.toLowerCase();
        String s1 = "";
        if (str.isEmpty()) {
            ret = true;
        } else {
            for (int i = 0; i < str.length(); ++i) {
                if (str.charAt(i) != ',') {
                    s1 += str.charAt(i);
                }
                if (str.charAt(i) == ',' || i >= str.length() - 1) {
                    if (prefix) {
                        //Pr??fix pr??fen
                        if (url.startsWith(s1.toLowerCase())) {
                            ret = true;
                            break;
                        }
                    } else //Suffix pr??fen
                        if (url.endsWith(s1.toLowerCase())) {
                            ret = true;
                            break;
                        }
                    s1 = "";
                }
            }
        }
        return ret;
    }

    public static boolean checkPathWritable(String path) {
        boolean ret = false;
        final File testPath = new File(path);
        try {
            if (!testPath.exists()) {
                testPath.mkdirs();
            }
            if (path.isEmpty()) {
            } else if (!testPath.isDirectory()) {
            } else if (testPath.canWrite()) {
                final File tmpFile = File.createTempFile("mtplayer", "tmp", testPath);
                tmpFile.delete();
                ret = true;
            }
        } catch (final Exception ignored) {
        }
        return ret;
    }

    public static boolean checkPrograms(ProgData data) {
        // pr??fen ob die eingestellten Programmsets passen
        final String PIPE = "| ";
        final String LEER = "      ";
        final String PFEIL = " -> ";
        boolean ret = true;
        String text = "";

        for (final SetData setData : data.setDataList) {
            ret = true;
            if (!setData.isFreeLine() && !setData.isLable()) {
                // nur wenn kein Lable oder freeline
                text += "++++++++++++++++++++++++++" + P2LibConst.LINE_SEPARATOR;
                text += PIPE + "Programmgruppe: " + setData.getVisibleName() + P2LibConst.LINE_SEPARATOR;
                final String destPath = setData.getDestPath();
                if (setData.progsContainPath()) {
                    // beim nur Abspielen wird er nicht gebraucht
                    if (destPath.isEmpty()) {
                        ret = false;
                        text += PIPE + LEER + "Zielpfad fehlt!" + P2LibConst.LINE_SEPARATOR;
                    } else // Pfad beschreibbar?
                        if (!checkPathWritable(destPath)) {
                            //da Pfad-leer und "kein" Pfad schon abgepr??ft
                            ret = false;
                            text += PIPE + LEER + "Falscher Zielpfad!" + P2LibConst.LINE_SEPARATOR;
                            text += PIPE + LEER + PFEIL + "Zielpfad \"" + destPath + "\" nicht beschreibbar!" + P2LibConst.LINE_SEPARATOR;
                        }
                }
                for (final ProgramData progData : setData.getProgramList()) {
                    // Programmpfad pr??fen
                    if (progData.getProgPath().isEmpty()) {
                        ret = false;
                        text += PIPE + LEER + "Kein Programm angegeben!" + P2LibConst.LINE_SEPARATOR;
                        text += PIPE + LEER + PFEIL + "Programmname: " + progData.getName() + P2LibConst.LINE_SEPARATOR;
                        text += PIPE + LEER + LEER + "Pfad: " + progData.getProgPath() + P2LibConst.LINE_SEPARATOR;
                    } else if (!new File(progData.getProgPath()).canExecute()) {
                        // dann noch mit RuntimeExec versuchen
                        final StartRuntimeExec r = new StartRuntimeExec(progData.getProgPath());
                        final Process pr = r.exec(false /*log*/);
                        if (pr != null) {
                            // dann passts ja
                            pr.destroy();
                        } else {
                            // l????t sich nicht starten
                            ret = false;
                            text += PIPE + LEER + "Falscher Programmpfad!" + P2LibConst.LINE_SEPARATOR;
                            text += PIPE + LEER + PFEIL + "Programmname: " + progData.getName() + P2LibConst.LINE_SEPARATOR;
                            text += PIPE + LEER + LEER + "Pfad: " + progData.getProgPath() + P2LibConst.LINE_SEPARATOR;
                            if (!progData.getProgPath().contains(File.separator)) {
                                text += PIPE + LEER + PFEIL + "Wenn das Programm nicht im Systempfad liegt, " + P2LibConst.LINE_SEPARATOR;
                                text += PIPE + LEER + LEER + "wird der Start nicht klappen!" + P2LibConst.LINE_SEPARATOR;
                            }
                        }
                    }
                }
                if (ret) {
                    //sollte alles passen
                    text += PIPE + PFEIL + "Ok!" + P2LibConst.LINE_SEPARATOR;
                }
                text += "++++++++++++++++++++++++++" + "" + P2LibConst.LINE_SEPARATORx3;
            }
        }
        PAlert.showInfoAlert("Set", "Sets pr??fen", text);
        return ret;
    }
}
