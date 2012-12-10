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

package ru.jcorp.fuzzyrules

import groovy.swing.SwingBuilder
import ru.jcorp.fuzzyrules.gui.MainWindow
import ru.jcorp.fuzzyrules.sys.Utf8ResourceBundle

import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.SwingUtilities
import javax.swing.UIManager
import ru.jcorp.fuzzyrules.gui.LoginWindow
import ru.jcorp.fuzzyrules.gui.ApplicationMode
import javax.swing.JOptionPane

/**
 * @author artamonov
 */
class FuzzyRulesApp {

    private MainWindow mainWindow

    private SwingBuilder guiBuilder

    private ResourceBundle resourceBundle

    private static FuzzyRulesApp instance

    FuzzyRulesApp() {
        // load localization
        resourceBundle = Utf8ResourceBundle.getBundle('locale.messages')

        // create GUI factory
        guiBuilder = new SwingBuilder()
    }

    private void run() {
        System.setProperty('file.encoding', 'UTF-8')
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel('com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel')
                } catch (Exception e) {
                    e.printStackTrace()
                    return
                }

                LoginWindow loginWindow = new LoginWindow()
                loginWindow.visible = true

                ApplicationMode mode = null

                while (loginWindow.isActivated() && mode == null) {
                    if ('expert'.equals(loginWindow.user) && 'expert'.equals(loginWindow.password))
                        mode = ApplicationMode.EXPERT
                    else if ('user'.equals(loginWindow.user) && 'user'.equals(loginWindow.password))
                        mode = ApplicationMode.USER
                    else {
                        JOptionPane.showMessageDialog(loginWindow, instance.getMessage('application.loginError'))
                        loginWindow.visible = true
                    }
                }

                if (!loginWindow.isActivated())
                    System.exit(0);

                mainWindow = new MainWindow(mode)
                mainWindow.locationByPlatform = true
                mainWindow.visible = true
            }
        })
    }

    public static void main(String[] args) {
        instance = new FuzzyRulesApp()
        instance.run()
    }

    SwingBuilder getGuiBuilder() {
        return guiBuilder
    }

    static FuzzyRulesApp getInstance() {
        return instance
    }

    String getMessage(String key) {
        return resourceBundle.getString(key)
    }

    String getResourceText(String resource) {
        InputStream sourceStream = getClass().getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(sourceStream, 'UTF-8'));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null)
            builder.append(line).append("\n");

        return builder.toString()
    }

    ImageIcon getResourceIcon(String name) {
        return new ImageIcon(FuzzyRulesApp.class.getResource('/icons/' + name))
    }

    Image getResourceImage(String name) {
        return getResourceIcon(name).getImage()
    }
}