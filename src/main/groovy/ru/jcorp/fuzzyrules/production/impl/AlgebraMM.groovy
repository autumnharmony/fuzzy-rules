package ru.jcorp.fuzzyrules.production.impl

import ru.jcorp.fuzzyrules.production.Algebra
import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet
import ru.jcorp.fuzzyrules.types.FuzzyValueSet
import ru.jcorp.fuzzyrules.types.FuzzyValue

/**
 * @author artamonov
 */
class AlgebraMM implements Algebra {

    @Override
    FuzzyBoolean or(FuzzyBoolean oneValue, FuzzyBoolean twoValue) {
        if (oneValue.value && twoValue.value) {
            double factor = Math.max(oneValue.factor, twoValue.factor)
            return new FuzzyBoolean(value: true, factor: factor)
        } else if (oneValue.value) {
            return new FuzzyBoolean(value: oneValue.value, factor: oneValue.factor)
        } else if (twoValue.value) {
            return new FuzzyBoolean(value: twoValue.value, factor: twoValue.factor)
        } else {
            double factor = Math.min(oneValue.factor, twoValue.factor)
            return new FuzzyBoolean(value: false, factor: factor)
        }
    }

    @Override
    FuzzyBooleanSet or(FuzzyBooleanSet one, FuzzyBooleanSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyBoolean oneValue : one.values) {
            for (FuzzyBoolean twoValue : two.values) {
                if (oneValue.value && twoValue.value) {
                    double factor = Math.max(oneValue.factor, twoValue.factor)
                    fuzzyBooleans.add(new FuzzyBoolean(value: true, factor: factor))
                } else if (oneValue.value) {
                    fuzzyBooleans.add(new FuzzyBoolean(value: oneValue.value, factor: oneValue.factor))
                } else if (twoValue.value) {
                    fuzzyBooleans.add(new FuzzyBoolean(value: twoValue.value, factor: twoValue.factor))
                } else {
                    double factor = Math.min(oneValue.factor, twoValue.factor)
                    fuzzyBooleans.add(new FuzzyBoolean(value: false, factor: factor))
                }
            }
        }

        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyBooleanSet and(FuzzyBooleanSet one, FuzzyBooleanSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyBoolean oneValue : one.values) {
            for (FuzzyBoolean twoValue : two.values) {
                if (oneValue.value && twoValue.value) {
                    double factor = Math.min(oneValue.factor, twoValue.factor)
                    fuzzyBooleans.add(new FuzzyBoolean(value: true, factor: factor))
                } else if (oneValue.value) {
                    fuzzyBooleans.add(new FuzzyBoolean(value: twoValue.value, factor: twoValue.factor))
                } else if (twoValue.value) {
                    fuzzyBooleans.add(new FuzzyBoolean(value: oneValue.value, factor: oneValue.factor))
                } else {
                    double factor = Math.max(oneValue.factor, twoValue.factor)
                    fuzzyBooleans.add(new FuzzyBoolean(value: false, factor: factor))
                }
            }
        }

        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyBooleanSet greaterThan(FuzzyValueSet one, FuzzyValueSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyValue oneValue : one.values) {
            for (FuzzyValue twoValue : two.values) {
                double factor = Math.min(oneValue.factor, twoValue.factor)
                fuzzyBooleans.add(new FuzzyBoolean(value: oneValue.value >= twoValue.value, factor: factor))
            }
        }

        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyBooleanSet lowerThan(FuzzyValueSet one, FuzzyValueSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyValue oneValue : one.values) {
            for (FuzzyValue twoValue : two.values) {
                double factor = Math.min(oneValue.factor, twoValue.factor)
                fuzzyBooleans.add(new FuzzyBoolean(value: oneValue.value <= twoValue.value, factor: factor))
            }
        }

        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyValueSet minus(FuzzyValueSet one, FuzzyValueSet two) {
        Set<FuzzyValue> fuzzyValues = new HashSet<>()

        for (FuzzyValue oneValue : one.values) {
            for (FuzzyValue twoValue : two.values) {
                double factor = Math.min(oneValue.factor, twoValue.factor)
                fuzzyValues.add(new FuzzyValue(oneValue.value - twoValue.value, factor))
            }
        }

        return new FuzzyValueSet(fuzzyValues)
    }

    @Override
    FuzzyValueSet plus(FuzzyValueSet one, FuzzyValueSet two) {
        Set<FuzzyValue> fuzzyValues = new HashSet<>()

        for (FuzzyValue oneValue : one.values) {
            for (FuzzyValue twoValue : two.values) {
                double factor = Math.min(oneValue.factor, twoValue.factor)
                fuzzyValues.add(new FuzzyValue(oneValue.value + twoValue.value, factor))
            }
        }

        return new FuzzyValueSet(fuzzyValues)
    }
}