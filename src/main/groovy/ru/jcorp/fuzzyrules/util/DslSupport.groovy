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

package ru.jcorp.fuzzyrules.util

/**
 * @author artamonov
 */
class DslSupport {
    static <T> T build(Closure closure, T delegate) {
        closure.delegate = delegate
        closure.resolveStrategy = groovy.lang.Closure.DELEGATE_FIRST
        closure.call()
        return delegate
    }

    static Closure linkClosureToDelegate(Closure closure, Object delegate) {
        closure.delegate = delegate
        closure.resolveStrategy = groovy.lang.Closure.DELEGATE_ONLY
        return closure
    }

    static Closure loadClosureFromResource(String resource) {
        return loadClosureFromStream(DslSupport.class.getResourceAsStream(resource))
    }

    static String getResourceAsString(String resource) {
        return getResourceAsString(DslSupport.class.getResourceAsStream(resource))
    }

    static String getResourceAsString(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, 'UTF-8'))
        StringBuilder builder = new StringBuilder()

        String line
        while ((line = reader.readLine()) != null)
            builder.append(line).append("\n")

        return builder.toString()
    }

    static Closure loadClosureFromStream(InputStream closureStream) {
        GroovyShell sh = new GroovyShell()

        StringBuilder builder = new StringBuilder()
        builder.append('{it->')
        builder.append(getResourceAsString(closureStream))
        builder.append('\n}')

        String script = builder.toString()
        return sh.evaluate(script) as Closure
    }
}