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

package de.p2tools.p2podder.controller.starterEpisode;

import de.p2tools.p2Lib.tools.log.PLog;


public class StartProgramFactory {

    private StartProgramFactory() {
    }

    public static boolean makeProgParameter(Start start) {
        try {
//            final ProgramData programData = start.getSetData().getProgForUrl(start.getUrl());
//            if (programData == null) {
//                return false; //todo ist das gut da wenn kein Set???
//            }
            String programName = start.getSetData().getVisibleName();
            String program = start.getSetData().getProgramPath();
            String programSwitch = start.getSetData().getProgramSwitch();

            start.setProgram(programName);
            buildProgParameter(start, program, programSwitch);
        } catch (final Exception ex) {
            PLog.errorLog(825600145, ex);
        }
        return true;
    }

    private static void buildProgParameter(Start start, String program, String programSwitch) {
        String callString = program + " " + programSwitch;
        callString = replaceExec(start, callString);
        start.setProgramCall(callString);

        String progArray = getProgrammAufrufArray(program, programSwitch);
        progArray = replaceExec(start, progArray);
        start.setProgramCallArray(progArray);
    }

    private static String getProgrammAufrufArray(String program, String programSwitch) {
        String ret;
        ret = program;
        final String[] ar = programSwitch.split(" ");
        for (final String s : ar) {
            ret = ret + StartRuntimeExec.TRENNER_PROG_ARRAY + s;
        }
        return ret;
    }

    private static String replaceExec(Start start, String execString) {
        execString = execString.replace("%f", start.getUrl());
        return execString;
    }
}
