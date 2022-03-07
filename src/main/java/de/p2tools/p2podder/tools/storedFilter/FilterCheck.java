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

package de.p2tools.p2podder.tools.storedFilter;

import java.util.Locale;
import java.util.regex.Pattern;

public class FilterCheck {
    public static boolean checkString(Filter title, String string) {
        if (!check(title, string)) {
            return false;
        }
        return true;
    }

    private static boolean check(Filter filter, String im) {
        // wenn einer passt, dann ists gut
        if (filter.filterArr.length == 1) {
            return check(filter.filterArr[0], filter.pattern, im);
        }

        if (filter.filterAnd) {
            // Suchbegriffe müssen alle passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (!im.toLowerCase().contains(s)) {
                    return false;
                }
            }
            return true;

        } else {
            // nur ein Suchbegriff muss passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (im.toLowerCase().contains(s)) {
                    return true;
                }
            }
        }

        // nix wars
        return false;
    }

    private static boolean check(String filter, Pattern pattern, String im) {
        if (pattern != null) {
            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }
        if (im.toLowerCase(Locale.ROOT).contains(filter)) {
            // wenn einer passt, dann ists gut
            return true;
        }

        // nix wars
        return false;
    }
}
