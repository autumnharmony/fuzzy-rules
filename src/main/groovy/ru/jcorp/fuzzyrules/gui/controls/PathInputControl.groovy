/*
 * This file is part of Fuzzy Rules.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.jcorp.fuzzyrules.gui.controls

import ru.jcorp.fuzzyrules.FuzzyRulesApp
import ru.jcorp.fuzzyrules.exceptions.ValidationException
import ru.jcorp.fuzzyrules.types.FuzzyValue
import ru.jcorp.fuzzyrules.types.FuzzyValueSet

import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*

/**
 * @author artamonov
 */
class PathInputControl implements InputControl<FuzzyValueSet> {

    private FuzzyRulesApp app

    private JTextField valueOne
    private JTextField valueTwo
    private JTextField cfOne
    private JTextField cfTwo

    private Action nextAction

    private volatile FuzzyValueSet value = null

    private JPanel component

    PathInputControl() {
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
        component.setLayout(new GridLayout(2, 2))

        valueOne = app.guiBuilder.textField(text: '0',
                size: [150, -1], minimumSize: [150, -1], preferredSize: [150, -1])
        valueTwo = app.guiBuilder.textField(text: '0',
                size: [150, -1], minimumSize: [150, -1], preferredSize: [150, -1])
        cfOne = app.guiBuilder.textField(text: '50',
                size: [150, -1], minimumSize: [150, -1], preferredSize: [150, -1])
        cfTwo = app.guiBuilder.textField(text: '50',
                size: [150, -1], minimumSize: [150, -1], preferredSize: [150, -1])

        component.add(valueOne)
        component.add(cfOne)
        component.add(valueTwo)
        component.add(cfTwo)

        component.setMaximumSize(new Dimension(300, 70))
        component.setPreferredSize(new Dimension(-1, 70))
        component.setMinimumSize(new Dimension(300, 70))
    }

    def apply() {
        try {
            double a = Double.parseDouble(valueOne.text)
            double b = Double.parseDouble(valueTwo.text)
            int ca = Integer.parseInt(cfOne.text)
            int cb = Integer.parseInt(cfTwo.text)

            if (ca + cb > 100)
                throw new ValidationException()

            value = new FuzzyValueSet(new FuzzyValue(a, ca / 100.0), new FuzzyValue(b, cb / 100.0))
        } catch (NumberFormatException ignored) {
            throw new ValidationException()
        }
    }

    @Override
    def FuzzyValueSet getValue() {
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
        valueOne.text = ''
        valueTwo.text = ''
        cfOne.text = ''
        cfTwo.text = ''
    }

    @Override
    void lock() {
        valueOne.editable = false
        valueTwo.editable = false
        cfTwo.editable = false
        cfOne.editable = false
    }
}