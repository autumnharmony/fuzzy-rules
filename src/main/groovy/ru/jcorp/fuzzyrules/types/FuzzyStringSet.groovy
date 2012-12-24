package ru.jcorp.fuzzyrules.types

/**
 * @author artamonov
 */
class FuzzyStringSet {

    private Set<FuzzyString> values = new HashSet<>()

    FuzzyStringSet(Set<FuzzyString> values) {
        this.values = values
    }

    FuzzyStringSet(FuzzyString one, FuzzyString two) {
        values.add(one)
        values.add(two)
    }

    Set<FuzzyString> getValues() {
        return values
    }
}