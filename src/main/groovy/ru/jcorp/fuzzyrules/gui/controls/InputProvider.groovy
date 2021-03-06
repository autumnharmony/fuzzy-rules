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
import ru.jcorp.fuzzyrules.gui.ResultWindow
import ru.jcorp.fuzzyrules.production.impl.ResultObject

import java.awt.Dimension
import java.awt.EventQueue
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import ru.jcorp.fuzzyrules.exceptions.ValidationException
import javax.swing.JOptionPane
import javax.swing.JList
import javax.swing.border.LineBorder
import java.awt.Color

/**
 * @author artamonov
 */
class InputProvider {

    private JPanel dialogPane
    private JButton nextButton
    private JButton reasonBtn
    private JPanel resultContainer

    InputProvider(JPanel dialogPane, JButton nextButton, JButton reasonBtn, JPanel resultContainer) {
        this.dialogPane = dialogPane
        this.nextButton = nextButton
        this.reasonBtn = reasonBtn
        this.resultContainer = resultContainer
    }

    def <T> T showInputControl(InputControl<T> inputControl, String messageCode) {
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            void run() {
                def label = new JLabel(FuzzyRulesApp.instance.getMessage(messageCode))
                label.setSize(300, 30)
                label.setPreferredSize(new Dimension(300, 30))
                label.setMaximumSize(new Dimension(300, 30))
                label.setMinimumSize(new Dimension(300, 30))

                dialogPane.add(label)
                dialogPane.add(inputControl.component)

                nextButton.setAction(new AbstractAction(nextButton.text) {
                    @Override
                    void actionPerformed(ActionEvent e) {
                        try {
                            inputControl.nextAction.actionPerformed(e)
                        } catch (ValidationException ignored) {
                            def app = FuzzyRulesApp.instance
                            JOptionPane.showMessageDialog(inputControl.component,
                                    app.getMessage('edit.validation'),
                                    app.getMessage('edit.warning'),
                                    JOptionPane.WARNING_MESSAGE)
                            return
                        }

                        inputControl.component.enabled = false
                        inputControl.lock()

                        synchronized (inputControl) {
                            inputControl.notifyAll()
                        }
                    }
                })

                inputControl.component.requestFocus()
            }
        })
        dialogPane.getRootPane()?.repaint()
        synchronized (inputControl) {
            inputControl.wait()
        }
        return inputControl.value
    }

    void printResult(Collection<ResultObject> result) {
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            void run() {
                nextButton.visible = false
                if (result != null) {
                    JList resultList = new JList((result as Collection).toArray())
                    resultList.setBorder(new LineBorder(Color.BLACK))
                    resultContainer.add(resultList)
                    reasonBtn.visible = true

                    new ResultWindow().show(result)
                } else {
                    JLabel resultLabel = new JLabel()
                    resultLabel.setText(FuzzyRulesApp.instance.getMessage('result.unresolved'))
                    resultContainer.add(resultLabel)
                }
            }
        })
        dialogPane.getRootPane()?.repaint()
    }

    void printUnresolvedSystemMessage() {
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            void run() {
                JLabel resultLabel = new JLabel()
                nextButton.visible = false
                resultLabel.setText(FuzzyRulesApp.instance.getMessage('result.unresolved'))
                resultContainer.add(resultLabel)
            }
        })
        dialogPane.getRootPane()?.repaint()
    }

    void showProductionError(Exception e) {
        e.printStackTrace()
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            void run() {
                JOptionPane.showMessageDialog(dialogPane,
                        FuzzyRulesApp.instance.getMessage('edit.productionError'),
                        FuzzyRulesApp.instance.getMessage('edit.error'),
                        JOptionPane.ERROR_MESSAGE)
            }
        })
    }
}