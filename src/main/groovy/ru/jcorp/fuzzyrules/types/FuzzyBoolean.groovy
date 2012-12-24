/*
 * This file is part of Fuzzy Rules.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.jcorp.fuzzyrules.types

import java.text.DecimalFormat

/**
 * @author artamonov
 */
@SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
class FuzzyBoolean {
    boolean value
    double factor

    boolean isActive() {
        return value && factor > 0.20
    }

    static FuzzyBoolean TRUE = new FuzzyBoolean(value: true, factor: 1.0)

    static FuzzyBoolean FALSE = new FuzzyBoolean(value: true, factor: 0.0)

    @Override
    String toString() {
        return Boolean.toString(value) + ' : ' + new DecimalFormat('#,##0').format(factor * 100.0)
    }
}