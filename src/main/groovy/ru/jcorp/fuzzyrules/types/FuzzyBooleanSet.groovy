package ru.jcorp.fuzzyrules.types

/**
 * @author artamonov
 */
class FuzzyBooleanSet {
    private Set<FuzzyBoolean> values = new HashSet<>()

    FuzzyBooleanSet(Set<FuzzyBoolean> values) {
        this.values = values
    }

    FuzzyBooleanSet(FuzzyBoolean fuzzyBoolean) {
        this.values.add(fuzzyBoolean)
    }

    Set<FuzzyBoolean> getValues() {
        return values
    }

    boolean isEmpty() {
        return values.isEmpty()
    }

    FuzzyBooleanSet getActivated(double activation) {
        Set<FuzzyBoolean> values = new HashSet<>()

        for (FuzzyBoolean value : this.values) {
            if (value.isActive(activation))
                values.add(value)
        }

        return new FuzzyBooleanSet(values)
    }
}