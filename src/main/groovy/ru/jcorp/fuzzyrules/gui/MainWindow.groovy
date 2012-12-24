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

package ru.jcorp.fuzzyrules.gui

import org.apache.commons.lang.StringUtils
import ru.jcorp.fuzzyrules.FuzzyRulesApp
import ru.jcorp.fuzzyrules.gui.controls.InputProvider
import ru.jcorp.fuzzyrules.model.RuleSet
import ru.jcorp.fuzzyrules.production.Algebra
import ru.jcorp.fuzzyrules.production.DomainObject
import ru.jcorp.fuzzyrules.production.Executor
import ru.jcorp.fuzzyrules.production.ProductionMethod
import ru.jcorp.fuzzyrules.production.impl.AlgebraBB
import ru.jcorp.fuzzyrules.production.impl.AlgebraMM
import ru.jcorp.fuzzyrules.production.impl.DirectProduction
import ru.jcorp.fuzzyrules.production.impl.DomainObjectIml
import ru.jcorp.fuzzyrules.util.DslSupport

import javax.swing.AbstractAction
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.JTextArea
import javax.swing.border.EmptyBorder
import java.awt.event.ActionEvent

import static javax.swing.JOptionPane.*

/**
 * @author artamonov
 */
class MainWindow extends JFrame {

    private FuzzyRulesApp app
    private JTabbedPane tabbedPane

    private Map<Long, Executor> executorMap = new WeakHashMap<Long, Executor>()

    private RuleSet ruleSet
    private ApplicationMode mode

    private File rulesFile

    private JPanel editPanel
    private JTextArea editor

    MainWindow(ApplicationMode mode) {
        this.app = FuzzyRulesApp.instance
        this.mode = mode

        this.size = [640, 480]
        this.minimumSize = [480, 320]
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.title = app.getMessage('application.title')

        if (mode == ApplicationMode.EXPERT)
            this.title += ' [' + app.getMessage('application.expert') + ']'
        else
            this.title += ' [' + app.getMessage('application.consultation') + ']'

        this.iconImage = app.getResourceImage('application.png')

        buildMenu()
        buildContentPane()

        rulesFile = new File('rules.groovy')
        if (!rulesFile.exists()) {
            rulesFile.withDataOutputStream {
                dos ->
                    dos.write(DslSupport.getResourceAsString('/rules/set.groovy').getBytes('UTF-8'))
                    dos.close()
            }
            ruleSet = RuleSet.build DslSupport.loadClosureFromResource('/rules/set.groovy')
        } else {
            ruleSet = RuleSet.build DslSupport.loadClosureFromStream(new FileInputStream(rulesFile))
        }
    }

    def buildMenu() {
        def menuBar = app.guiBuilder.menuBar() {
            menu(text: app.getMessage('menu.file')) {
                if (this.mode == ApplicationMode.EXPERT) {
                    menuItem(text: app.getMessage('menu.file.rules'), icon: app.getResourceIcon('menu/wand-hat.png'),
                            actionPerformed: {
                                editRules()
                            })
                }
                menuItem(text: app.getMessage('menu.file.new'), icon: app.getResourceIcon('menu/lightning.png'),
                        actionPerformed: {
                            newConsultation()
                        })
                separator()
                menuItem(text: app.getMessage('menu.file.exit'), icon: app.getResourceIcon('menu/exit.png'),
                        actionPerformed: { System.exit(0) })
            }
            menu(text: app.getMessage('menu.help')) {
                menuItem(text: app.getMessage('menu.help.about'), icon: app.getResourceIcon('menu/about.png'),
                        actionPerformed: {
                            AboutWindow about = new AboutWindow()
                            about.locationRelativeTo = this
                            about.visible = true
                        })
            }
        }
        setJMenuBar(menuBar)
    }

    def buildContentPane() {
        def contentPane = app.guiBuilder.panel() {
            borderLayout()
            tabbedPane = tabbedPane(constraints: CENTER) {
            }
        }
        setContentPane(contentPane)
    }

    def newConsultation() {
        final Long execNumber = new Date().time

        JPanel dialogPane = null
        JButton nextBtn = null
        JButton reasonBtn = null
        JPanel resultContainer = null
        def consultationPanel = app.guiBuilder.panel(border: new EmptyBorder(3, 5, 3, 5)) {
            borderLayout()

            hbox(constraints: PAGE_START) {
                hglue()
                resultContainer = panel() {
                    boxLayout(axis: BoxLayout.Y_AXIS)
                }
                hglue()
            }

            scrollPane(constraints: CENTER, border: new EmptyBorder(2, 2, 2, 2), verticalScrollBarPolicy: JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) {
                panel {
                    dialogPane = panel() {
                        gridLayout(columns: 2, rows: -1)
                    }
                }
            }

            hbox(constraints: PAGE_END) {
                nextBtn = button(text: app.getMessage('edit.next'))
                reasonBtn = button(text: app.getMessage('edit.reason'), visible: false)

                hglue()

                button(text: app.getMessage('edit.finish'), actionPerformed: {
                    executorMap.get(execNumber)?.cancel()
                    // stop consulation
                    tabbedPane.remove(tabbedPane.selectedComponent)
                })
            }
        }
        InputProvider inputProvider = new InputProvider(dialogPane, nextBtn, reasonBtn, resultContainer)

        def options = [app.getMessage('algebra.mm'), app.getMessage('algebra.bb')]
        def option = showOptionDialog(this,
                app.getMessage('edit.selectAlgebra'),
                app.getMessage('edit.options'), DEFAULT_OPTION,
                INFORMATION_MESSAGE, null,
                options.toArray(), '')

        DomainObject domainObject = new DomainObjectIml(inputProvider)
        ProductionMethod method = new DirectProduction(domainObject)

        Algebra algebra
        if (option == 0) {
            algebra = new AlgebraMM()
        } else if (option == 1) {
            algebra = new AlgebraBB()
        } else {
            return
        }

        reasonBtn.setAction(new AbstractAction(reasonBtn.text) {
            @Override
            void actionPerformed(ActionEvent e) {
                String message = ''
                String rules = StringUtils.join(domainObject.activatedRules.iterator(), ',')

                if (StringUtils.isNotEmpty(rules)) {
                    message += '\n\n' + app.getMessage('edit.rules') + ' ' + rules
                }

                JOptionPane.showMessageDialog(MainWindow.this, message,
                        app.getMessage('edit.reason'), JOptionPane.INFORMATION_MESSAGE)
            }
        })

        Executor executor = new Executor(method, inputProvider, algebra, ruleSet)
        executor.performProduction()

        executorMap.put(execNumber, executor)

        def consultationTitle = String.format(app.getMessage('consultation.title'), tabbedPane.tabCount + 1, options.get(option))
        tabbedPane.add(consultationTitle, consultationPanel)
        tabbedPane.selectedComponent = consultationPanel
    }

    def editRules() {
        if (editPanel == null) {
            editPanel = app.guiBuilder.panel(border: new EmptyBorder(3, 5, 3, 5)) {
                borderLayout()

                hbox(constraints: CENTER) {
                    scrollPane(border: new EmptyBorder(2, 2, 2, 2)) {
                        editor = textArea(text: DslSupport.getResourceAsString(new FileInputStream(rulesFile)))
                    }
                }

                hbox(constraints: PAGE_END) {
                    button(text: app.getMessage('edit.save'), actionPerformed: {
                        try {
                            def testCode = new ByteArrayInputStream(editor.text.getBytes('UTF-8'))
                            RuleSet.build DslSupport.loadClosureFromStream(testCode)
                        } catch (Exception ignored) {
                            JOptionPane.showMessageDialog(this,
                                    app.getMessage('application.parseError'),
                                    app.getMessage('application.error'),
                                    JOptionPane.WARNING_MESSAGE)
                            return
                        }

                        rulesFile.withDataOutputStream {
                            dos ->
                                dos.write(editor.text.getBytes('UTF-8'))
                                dos.close()
                        }
                        ruleSet = RuleSet.build DslSupport.loadClosureFromStream(new FileInputStream(rulesFile))
                        tabbedPane.remove(editPanel)
                        editPanel = null
                        editor = null
                    })
                    button(text: app.getMessage('edit.cancel'), actionPerformed: {
                        tabbedPane.remove(editPanel)
                        editPanel = null
                        editor = null
                    })
                    hglue()
                }
            }

            tabbedPane.add(app.getMessage('edit.base'), editPanel)
        }
        tabbedPane.selectedComponent = editPanel
    }
}