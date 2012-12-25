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

package ru.jcorp.fuzzyrules.model

import ru.jcorp.fuzzyrules.util.DslSupport

/**
 * @author artamonov
 */
@SuppressWarnings("GroovyUnusedDeclaration")
class RuleSet {

    private List<Rule> rules = new ArrayList<Rule>()

    private double activation

    List<Rule> getRules() {
        return rules
    }

    int getSize() {
        return rules.size()
    }

    void activation(Integer a) {
        this.activation = a / 100.0
    }

    void rule(String name, Closure definition) {
        def rule = DslSupport.build(definition, new Rule(name))
        rules.add(rule)
    }

    static RuleSet build(Closure setDefinition) {
        DslSupport.build(setDefinition, new RuleSet())
    }

    double getActivation() {
        return activation
    }
}