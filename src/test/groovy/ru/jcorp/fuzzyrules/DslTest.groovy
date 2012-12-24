package ru.jcorp.fuzzyrules

import ru.jcorp.fuzzyrules.model.RuleSet
import ru.jcorp.fuzzyrules.util.DslSupport

/**
 * @author artamonov
 */
class DslTest extends GroovyTestCase {
    public void testLoadRules() {
        def closure = DslSupport.loadClosureFromResource('/rules/set.groovy')
        RuleSet ruleSet = RuleSet.build(closure)

        assertNotNull(ruleSet)
    }
}