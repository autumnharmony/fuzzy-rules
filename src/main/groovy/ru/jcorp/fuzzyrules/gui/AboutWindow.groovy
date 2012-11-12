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

import ru.jcorp.fuzzyrules.FuzzyRulesApp

import java.awt.BorderLayout
import javax.swing.JDialog
import javax.swing.border.EmptyBorder

/**
 * @author artamonov
 */
class AboutWindow extends JDialog {

    private FuzzyRulesApp app

    AboutWindow() {
        this.app = FuzzyRulesApp.instance

        this.size = [600, 400]
        this.title = app.getMessage('menu.help.about')
        this.iconImage = app.getResourceImage('application.png')

        buildContentPane()
    }

    def buildContentPane() {
        def panel = app.guiBuilder.panel(constraints: BorderLayout.CENTER) {
            borderLayout()
            vbox(constraints: PAGE_START, border: new EmptyBorder(3, 5, 3, 5)) {
                label(text: app.getMessage('authors'), border: new EmptyBorder(3, 5, 3, 5))
                label(text: app.getMessage('teacher'), border: new EmptyBorder(3, 5, 3, 5))
            }
            scrollPane(constraints: CENTER) {
                textArea(text: app.getResourceText('/TASK'),
                        editable: false, lineWrap: true, wrapStyleWord: true)
            }
            hbox(constraints: PAGE_END, border: new EmptyBorder(3, 5, 3, 5)) {
                hglue()
                button(text: app.getMessage('edit.ok'), actionPerformed: { this.dispose() })
                hglue()
            }
        }
        add(panel)
    }
}