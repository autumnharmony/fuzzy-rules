package ru.jcorp.fuzzyrules.production.impl

import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyString
import ru.jcorp.fuzzyrules.types.FuzzyStringSet

/**
 * @author artamonov
 */
@SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
class ResultObject extends GroovyObjectSupport {

    FuzzyBoolean activation

    ResultObject(FuzzyBoolean activation) {
        this.activation = activation
    }

    String X = ''

    String[] OT = new String[100]

    Object[] OR = new Object[100]

    List<String>[] ON = new List<String>[100]

    FuzzyString fuzzy(String value, Integer factor) {
        return new FuzzyString(value: value, factor: factor / 100.0)
    }

    FuzzyStringSet fuzzySet(FuzzyString one, FuzzyString two) {
        return new FuzzyStringSet(one, two)
    }
}