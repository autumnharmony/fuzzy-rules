package ru.jcorp.fuzzyrules.types

import ru.jcorp.fuzzyrules.model.Math

/**
 * @author artamonov
 */
@SuppressWarnings("GrMethodMayBeStatic")
class FuzzyValueSet {
    private Set<FuzzyValue> values = new HashSet<>()

    FuzzyValueSet(FuzzyValue one, FuzzyValue two) {
        values.add(one)
        values.add(two)
    }

    FuzzyValueSet(Set<FuzzyValue> values) {
        this.values = values
    }

    FuzzyValueSet(FuzzyValue one) {
        values.add(one)
    }

    FuzzyBooleanSet greaterThan(FuzzyValueSet other) {
        Math.algebra.greaterThan(this, other)
    }

    FuzzyBooleanSet lowerThan(FuzzyValueSet other) {
        Math.algebra.lowerThan(this, other)
    }

    FuzzyValueSet minus(FuzzyValueSet other) {
        Math.algebra.minus(this, other)
    }

    FuzzyValueSet plus(FuzzyValueSet other) {
        Math.algebra.plus(this, other)
    }

    Set<FuzzyValue> getValues() {
        return values
    }
}