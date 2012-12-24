package ru.jcorp.fuzzyrules.production.impl

import org.apache.commons.lang.StringUtils
import ru.jcorp.fuzzyrules.exceptions.CannotInputVariableException
import ru.jcorp.fuzzyrules.gui.controls.BooleanInputControl
import ru.jcorp.fuzzyrules.gui.controls.DateInputControl
import ru.jcorp.fuzzyrules.gui.controls.InputProvider
import ru.jcorp.fuzzyrules.gui.controls.PathInputControl
import ru.jcorp.fuzzyrules.model.Math
import ru.jcorp.fuzzyrules.model.Rule
import ru.jcorp.fuzzyrules.production.DomainObject
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet
import ru.jcorp.fuzzyrules.types.FuzzyString
import ru.jcorp.fuzzyrules.types.FuzzyStringSet
import ru.jcorp.fuzzyrules.types.FuzzyValue
import ru.jcorp.fuzzyrules.types.FuzzyValueSet

/**
 * @author artamonov
 */
@SuppressWarnings(["GrMethodMayBeStatic", "GroovyUnusedDeclaration"])
class DomainObjectIml extends GroovyObjectSupport implements DomainObject {

    private Set<Rule> activatedRules = new HashSet<>()
    private Set<ResultObject> resultObjects = new HashSet<>()

    protected Map<String, Object> miscVariables = new HashMap<String, Object>()

    private FuzzyValueSet[] dates = new FuzzyValueSet[100]

    private Date currentDate = new Date()

    private FuzzyValueSet _P_ = null
    private FuzzyBooleanSet _CR_ = null
    private FuzzyBooleanSet _KR_ = null
    private FuzzyBooleanSet _DR_ = null
    private FuzzyBooleanSet _NB_ = null
    private FuzzyBooleanSet _CB_ = null
    private FuzzyBooleanSet _CO_ = null

    protected final InputProvider inputProvider

    DomainObjectIml(InputProvider inputProvider) {
        this.inputProvider = inputProvider
    }

    FuzzyValue fuzzy(Double value, Integer factor) {
        return new FuzzyValue(value: value, factor: factor / 100.0)
    }

    FuzzyString fuzzy(String value, Integer factor) {
        return new FuzzyString(value: value, factor: factor / 100.0)
    }

    FuzzyValueSet fuzzySet(FuzzyValue one, FuzzyValue two) {
        return new FuzzyValueSet(one, two)
    }

    FuzzyStringSet fuzzySet(FuzzyString one, FuzzyString two) {
        return new FuzzyStringSet(one, two)
    }

    FuzzyValueSet days(int dayCount) {
        return new FuzzyValueSet(new FuzzyValue(((double)dayCount) * 24 * 3600 * 1000, 1.0))
    }

    FuzzyBooleanSet or(FuzzyBooleanSet one, FuzzyBooleanSet two) {
        Math.algebra.or(one, two)
    }

    FuzzyBooleanSet or(FuzzyBooleanSet one, FuzzyBooleanSet two, FuzzyBooleanSet three) {
        or(or(one, two), three)
    }

    FuzzyValueSet D(Integer index) {
        if (dates[index] == null) {
            dates[index] = inputProvider.showInputControl(new DateInputControl(), 'variable.input.D' + index)
        }
        return dates[index]
    }

    FuzzyValueSet getDX() {
        return new FuzzyValueSet(new FuzzyValue(currentDate.getTime(), 1.0))
    }

    FuzzyValueSet getP() {
        if (_P_ == null ) {
            _P_ = inputProvider.showInputControl(new PathInputControl(), 'variable.input.P')
        }
        return _P_
    }

    FuzzyBooleanSet getCR() {
        if (_CR_ == null) {
            _CR_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.CR')
        }
        return _CR_
    }

    FuzzyBooleanSet getKR() {
        if (_KR_ == null) {
            _KR_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.KR')
        }
        return _KR_
    }

    FuzzyBooleanSet getDR() {
        if (_DR_ == null) {
            _DR_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.DR')
        }
        return _DR_
    }

    FuzzyBooleanSet getNB() {
        if (_NB_ == null) {
            _NB_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.NB')
        }
        return _NB_
    }

    FuzzyBooleanSet getCB() {
        if (_CB_ == null) {
            _CB_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.CB')
        }
        return _CB_
    }

    FuzzyBooleanSet getCO() {
        if (_CO_ == null) {
            _CO_ = inputProvider.showInputControl(new BooleanInputControl(), 'variable.input.CO')
        }
        return _CO_
    }

    @Override
    boolean isResolved() {
        return activatedRules.size() > 0
    }

    @Override
    void addActivatedRule(Rule rule, ResultObject resultObject) {
        activatedRules.add(rule)
        resultObjects.add(resultObject)
    }

    @Override
    Set<Rule> getActivatedRules() {
        return activatedRules
    }

    @Override
    void printResult() {

    }

    @Override
    Object getProperty(String property) {
        if (hasProperty(property))
            return super.getProperty(property)
        else {
            String lcProperty = StringUtils.lowerCase(property)
            if (hasProperty(lcProperty) && !StringUtils.equals(property, lcProperty))
                return super.getProperty(lcProperty)
            else if (miscVariables.containsKey(property))
                return miscVariables.get(property)
            else
                throw new CannotInputVariableException(property)
        }
    }

    @Override
    void setProperty(String property, Object newValue) {
        if (hasProperty(property))
            super.setProperty(property, newValue)
        else {
            String lcProperty = StringUtils.lowerCase(property)
            if (hasProperty(lcProperty) && !StringUtils.equals(property, lcProperty))
                super.setProperty(lcProperty, newValue)
            else
                miscVariables.put(property, newValue)
        }
    }
}