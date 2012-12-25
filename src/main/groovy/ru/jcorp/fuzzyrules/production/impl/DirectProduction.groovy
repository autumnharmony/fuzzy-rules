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

package ru.jcorp.fuzzyrules.production.impl

import ru.jcorp.fuzzyrules.exceptions.*
import ru.jcorp.fuzzyrules.model.*
import ru.jcorp.fuzzyrules.production.*
import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet

import static ru.jcorp.fuzzyrules.util.DslSupport.linkClosureToDelegate

/**
 * @author artamonov
 */
class DirectProduction implements ProductionMethod {

    private final DomainObject domainObject

    DirectProduction(DomainObject domainObject) {
        this.domainObject = domainObject
    }

    @Override
    void perform(RuleSet ruleSet) {
        int runCount = 0

        while (!domainObject.isResolved() &&
                runCount != ruleSet.size) {

            for (Rule rule : ruleSet.rules) {
                FuzzyBooleanSet conjValue = null
                boolean allValuesResolved = true

                def conjIter = rule.ifStatements.iterator()
                while (conjIter.hasNext()) {
                    Closure conj = linkClosureToDelegate(conjIter.next(), domainObject)

                    def conjResult
                    try {
                        conjResult = conj.call()
                    } catch (CannotInputVariableException ignored) {
                        allValuesResolved = false
                        break
                    }

                    if (conjResult instanceof FuzzyBooleanSet) {
                        if (conjValue != null)
                            conjValue = Math.algebra.and(conjResult, conjValue)
                        else
                            conjValue = conjResult
                    } else if (conjResult != null)
                        throw new RuleStatementException(rule.name)
                }

                FuzzyBooleanSet activated = conjValue.getActivated(ruleSet.getActivation())
                if (allValuesResolved && !activated.isEmpty()) {
                    def iterator = activated.values.iterator()
                    FuzzyBoolean resultBoolean = iterator.next()
                    while (iterator.hasNext())
                        resultBoolean = Math.algebra.or(resultBoolean, iterator.next())

                    def resultObject = new ResultObject(resultBoolean, Math.algebra)

                    Closure thenClosure = linkClosureToDelegate(rule.thenStatement, resultObject)
                    thenClosure.call()

                    domainObject.addActivatedRule(rule, resultObject)
                }
            }
            runCount++
        }

        if (!domainObject.isResolved())
            throw new UnresolvedRuleSystemException()
        else
            domainObject.printResult()
    }
}