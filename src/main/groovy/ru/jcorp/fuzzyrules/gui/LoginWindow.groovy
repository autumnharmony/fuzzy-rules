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

import javax.swing.JDialog
import ru.jcorp.fuzzyrules.FuzzyRulesApp
import java.awt.BorderLayout
import javax.swing.border.EmptyBorder
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JTextField

/**
 * @author artamonov
 */
class LoginWindow extends JDialog {

    private FuzzyRulesApp app
    private boolean activated = false

    private JTextField loginField
    private JTextField passwordField

    LoginWindow() {
        this.app = FuzzyRulesApp.instance

        this.size = [320, -1]
        this.minimumSize = [320, -1]
        this.title = app.getMessage('menu.login')
        this.iconImage = app.getResourceImage('application.png')
        this.modal = true
        this.locationRelativeTo = null
        this.resizable = false
        this.defaultCloseOperation = HIDE_ON_CLOSE
        this.alwaysOnTop = true

        buildContentPane()
    }

    def buildContentPane() {
        JButton okBtn = null
        def panel = app.guiBuilder.panel(constraints: BorderLayout.CENTER, border: new EmptyBorder(5, 5, 5, 5)) {
            boxLayout(axis: BoxLayout.Y_AXIS)
            panel() {
                gridLayout(cols: 2, rows: 2)

                label(text: app.getMessage('user.login'))
                loginField = textField()

                label(text: app.getMessage('user.password'))
                passwordField = passwordField()
            }
            hbox(border: new EmptyBorder(3, 5, 3, 5)) {
                hglue()
                okBtn = button(text: app.getMessage('edit.ok'), actionPerformed: { this.close() })
                hglue()
            }
        }
        add(panel)

        getRootPane().setDefaultButton(okBtn)

        pack()

        requestFocus()
    }

    @Override
    void setVisible(boolean b) {
        this.activated = false
        super.setVisible(b)
    }

    def close() {
        this.visible = false
        this.activated = true
    }

    def isActivated() {
        return activated
    }

    def getUser() {
        return loginField.text
    }

    def getPassword() {
        return passwordField.text
    }
}