package ru.jcorp.fuzzyrules

import ru.jcorp.fuzzyrules.types.FuzzyBoolean

/**
 * @author artamonov
 */
class FuzzyBooleanTest extends GroovyTestCase {

    public void testToString() {
        println(new FuzzyBoolean(value: true, factor: 0.235).toString())
    }
}