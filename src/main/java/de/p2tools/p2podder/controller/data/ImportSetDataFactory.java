/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2podder.controller.data;

import de.p2tools.p2Lib.tools.log.PLog;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ImportSetDataFactory {

    private static final String PATH_PSET = "/de/p2tools/p2podder/controller/data/standard_pset.xml";
    private static final String PATH_PSET_DUMMY = "/de/p2tools/p2podder/controller/data/standard_pset_dummy.xml";

    private ImportSetDataFactory() {
    }

    public static SetDataList getStandarset() {
        SetDataList setDataList = new SetDataList();


        // liefert das Standardprogrammset für das entsprechende BS
        InputStreamReader inReader;
        inReader = getPsetTamplate();
        importPset(setDataList, inReader);

        //und jetzt noch die dummy
        inReader = getPsetTamplateDummy();
        importPset(setDataList, inReader);

        if (setDataList.size() > 0) {
            // damit die Variablen ersetzt werden
            SetDataList.progReplacePattern(setDataList);
        }

        return setDataList;
    }

    private static InputStreamReader getPsetTamplate() {
        try {
            return new InputStreamReader(ImportSetDataFactory.class.getResource(PATH_PSET).openStream(), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            PLog.errorLog(469691002, ex);
        }
        return null;
    }

    private static InputStreamReader getPsetTamplateDummy() {
        try {
            return new InputStreamReader(ImportSetDataFactory.class.getResource(PATH_PSET_DUMMY).openStream(), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            PLog.errorLog(894512049, ex);
        }
        return null;
    }

    private static void importPset(SetDataList list, InputStreamReader in) {
        SetData psetData;
        try {
            int event;
            final XMLInputFactory inFactory = XMLInputFactory.newInstance();
            inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            XMLStreamReader parser;
            parser = inFactory.createXMLStreamReader(in);
            while (parser.hasNext()) {
                event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (parser.getLocalName()) {
                        case SetData.TAG:
                            psetData = new SetData();
                            if (!get(parser, SetData.TAG, SetData.XML_NAMES, psetData.arr)) {
                                psetData = null;
                            } else {
                                if (!psetData.isEmpty()) {
                                    //kann beim Einlesen der Konfigdatei vorkommen
                                    psetData.setPropsFromXml();
                                    list.add(psetData);
                                }
                            }
                            break;
                    }
                }
            }
            in.close();
        } catch (final Exception ex) {
            PLog.errorLog(467810360, ex);
        }
    }

    private static boolean get(XMLStreamReader parser, String xmlElem, String[] xmlNames, String[] strRet) {
        boolean ret = true;
        final int maxElem = strRet.length;
        for (int i = 0; i < maxElem; ++i) {
            strRet[i] = "";
        }
        try {
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(xmlElem)) {
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    for (int i = 0; i < maxElem; ++i) {
                        if (parser.getLocalName().equals(xmlNames[i])) {
                            strRet[i] = parser.getElementText();
                            break;
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(467256394, ex);
        }
        return ret;
    }
}
