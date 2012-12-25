package ru.jcorp.fuzzyrules.gui.controls

import ru.jcorp.fuzzyrules.FuzzyRulesApp
import ru.jcorp.fuzzyrules.exceptions.ValidationException
import ru.jcorp.fuzzyrules.types.FuzzyBoolean
import ru.jcorp.fuzzyrules.types.FuzzyBooleanSet

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

/**
 * @author artamonov
 */
class BooleanInputControl implements InputControl<FuzzyBooleanSet> {

    private FuzzyRulesApp app

    private Action nextAction

    private JPanel component
    private JCheckBox checkBox

    private FuzzyBooleanSet value = null

    BooleanInputControl() {
        this.app = FuzzyRulesApp.instance

        this.nextAction = new AbstractAction() {
            @Override
            void actionPerformed(ActionEvent e) {
                try {
                    apply()
                } catch (NumberFormatException ignored) {
                    throw new ValidationException()
                }
            }
        }

        component = new JPanel()
        component.setLayout(new BorderLayout())
        checkBox = new JCheckBox()
        component.add(checkBox, BorderLayout.CENTER)

        component.setMaximumSize(new Dimension(50, 30))
        component.setPreferredSize(new Dimension(-1, 30))
    }

    def apply() {
        try {
            value = new FuzzyBooleanSet(new FuzzyBoolean(value: checkBox.isSelected(), factor: 1.0))
        } catch (Exception ignored) {
            throw new ValidationException()
        }
    }

    @Override
    FuzzyBooleanSet getValue() {
        return value
    }

    @Override
    JComponent getComponent() {
        return component
    }

    @Override
    Action getNextAction() {
        return nextAction
    }

    @Override
    void clear() {

    }

    @Override
    void lock() {
        checkBox.enabled = false
    }
}