package ru.jcorp.fuzzyrules.production.impl

import org.apache.commons.lang.StringUtils
import ru.jcorp.fuzzyrules.production.Algebra
import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyString
import ru.jcorp.fuzzyrules.types.FuzzyStringSet

import java.text.DecimalFormat

/**
 * @author artamonov
 */
@SuppressWarnings(["GroovyUnusedDeclaration", "GrMethodMayBeStatic"])
class ResultObject extends GroovyObjectSupport {

    FuzzyBoolean activation
    Algebra algebra

    private static DecimalFormat df = new DecimalFormat('#,##0')

    ResultObject(FuzzyBoolean activation, Algebra algebra) {
        this.activation = activation
        this.algebra = algebra
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

    @Override
    String toString() {
        return X + ' : ' + df.format(activation.factor * 100);
    }

    String getNotList() {
        StringBuilder b = new StringBuilder()
        for (List<String> notList : ON) {
            if (notList != null)
                b.append(StringUtils.join(notList, ',')).append('\n')
        }
        return b.toString()
    }

    String getOtList() {
        List<String> ot = new ArrayList<>()
        for (String o : OT)
            if (o != null)
                ot.add(o)
        return StringUtils.join(ot, ', ')
    }

    String getOrList() {
        List<Object> values = new ArrayList<>()
        for (Object o : OR) {
            if (o != null)
                values.add(o)
        }

        StringBuilder b = new StringBuilder()
        for (Object o : values) {
            if (o instanceof Collection) {
                b.append(StringUtils.join(o, ', '))
            } else if (o in FuzzyStringSet) {
                List<String> used = new ArrayList<>()

                for (FuzzyString fs : o.values) {
                    FuzzyBoolean ba = algebra.and(activation, new FuzzyBoolean(value: true, factor: fs.factor))
                    if (ba.isActive())
                        used.add(fs.value + ':' + df.format(ba.factor * 100))
                }

                b.append(StringUtils.join(used, ', '))
            }
        }

        return b.toString()
    }
}