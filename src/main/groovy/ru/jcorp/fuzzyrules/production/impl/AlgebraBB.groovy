package ru.jcorp.fuzzyrules.production.impl

import ru.jcorp.fuzzyrules.production.Algebra
import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet
import ru.jcorp.fuzzyrules.types.FuzzyValue
import ru.jcorp.fuzzyrules.types.FuzzyValueSet

/**
 * @author artamonov
 */
class AlgebraBB implements Algebra {
    @Override
    FuzzyBoolean or(FuzzyBoolean oneValue, FuzzyBoolean twoValue) {
        if (oneValue.value && twoValue.value) {
            double fOne = oneValue.factor * 100
            double fTwo = twoValue.factor * 100

            double factor = (Math.max(fOne, fTwo) + Math.min(fOne, fTwo) * (1 - fOne / 100.0) * (1 - fTwo / 100.0)) / 100.0
            return new FuzzyBoolean(value: true, factor: factor)
        } else if (oneValue.value) {
            return new FuzzyBoolean(value: oneValue.value, factor: oneValue.factor)
        } else if (twoValue.value) {
            return new FuzzyBoolean(value: twoValue.value, factor: twoValue.factor)
        } else {
            double fOne = oneValue.factor * 100
            double fTwo = twoValue.factor * 100

            double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
            return new FuzzyBoolean(value: false, factor: factor)
        }
    }

    @Override
    FuzzyBoolean and(FuzzyBoolean oneValue, FuzzyBoolean twoValue) {
        if (oneValue.value && twoValue.value) {
            double fOne = oneValue.factor * 100
            double fTwo = twoValue.factor * 100

            double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
            return new FuzzyBoolean(value: true, factor: factor)
        } else if (oneValue.value) {
            return new FuzzyBoolean(value: oneValue.value, factor: twoValue.factor)
        } else if (twoValue.value) {
            return new FuzzyBoolean(value: twoValue.value, factor: oneValue.factor)
        } else {
            double fOne = oneValue.factor * 100
            double fTwo = twoValue.factor * 100

            double factor = (Math.max(fOne, fTwo) + Math.min(fOne, fTwo) * (1 - fOne / 100.0) * (1 - fTwo / 100.0)) / 100.0
            return new FuzzyBoolean(value: false, factor: factor)
        }
    }

    @Override
    FuzzyBooleanSet or(FuzzyBooleanSet one, FuzzyBooleanSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyBoolean oneValue : one.values) {
            for (FuzzyBoolean twoValue : two.values) {
                fuzzyBooleans.add(or(oneValue, twoValue));
            }
        }

        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyBooleanSet and(FuzzyBooleanSet one, FuzzyBooleanSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyBoolean oneValue : one.values) {
            for (FuzzyBoolean twoValue : two.values) {
                fuzzyBooleans.add(and(oneValue, twoValue));
            }
        }
        return new FuzzyBooleanSet(fuzzyBooleans)
    }

    @Override
    FuzzyBooleanSet greaterThan(FuzzyValueSet one, FuzzyValueSet two) {
        Set<FuzzyBoolean> fuzzyBooleans = new HashSet<>()

        for (FuzzyValue oneValue : one.values) {
            for (FuzzyValue twoValue : two.values) {
                double fOne = oneValue.factor * 100
                double fTwo = twoValue.factor * 100

                double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
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
                double fOne = oneValue.factor * 100
                double fTwo = twoValue.factor * 100

                double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
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
                double fOne = oneValue.factor * 100
                double fTwo = twoValue.factor * 100

                double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
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
                double fOne = oneValue.factor * 100
                double fTwo = twoValue.factor * 100

                double factor = (fOne * fTwo / 100.0 * (2 - Math.max(fOne, fTwo) / 100.0)) / 100.0
                fuzzyValues.add(new FuzzyValue(oneValue.value + twoValue.value, factor))
            }
        }

        return new FuzzyValueSet(fuzzyValues)
    }
}