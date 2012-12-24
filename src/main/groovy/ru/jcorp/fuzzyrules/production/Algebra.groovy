package ru.jcorp.fuzzyrules.production

import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet
import ru.jcorp.fuzzyrules.types.FuzzyValueSet

/**
 * @author artamonov
 */
public interface Algebra {
    FuzzyBoolean or(FuzzyBoolean one, FuzzyBoolean two)

    FuzzyBooleanSet or(FuzzyBooleanSet one, FuzzyBooleanSet two)

    FuzzyBooleanSet and(FuzzyBooleanSet one, FuzzyBooleanSet two)

    FuzzyBooleanSet greaterThan(FuzzyValueSet one, FuzzyValueSet two)

    FuzzyBooleanSet lowerThan(FuzzyValueSet one, FuzzyValueSet two)

    FuzzyValueSet minus(FuzzyValueSet one, FuzzyValueSet two)

    FuzzyValueSet plus(FuzzyValueSet one, FuzzyValueSet two)
}