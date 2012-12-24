package ru.jcorp.fuzzyrules.gui

import ru.jcorp.fuzzyrules.FuzzyRulesApp
import ru.jcorp.fuzzyrules.production.impl.ResultObject

import javax.swing.*
import javax.swing.border.LineBorder
import java.awt.*
import java.text.DecimalFormat

/**
 * @author artamonov
 */
class ResultWindow extends JDialog {

    private FuzzyRulesApp app

    ResultWindow() {
        this.size = [640, 480]
        this.minimumSize = [640, 480]

        app = FuzzyRulesApp.instance

        this.title = app.getMessage('application.title')
        this.iconImage = app.getResourceImage('application.png')
        this.modal = true
        this.locationRelativeTo = null
        this.resizable = false
        this.defaultCloseOperation = HIDE_ON_CLOSE

        JPanel contentPane = new JPanel()
        contentPane.setLayout(new GridLayout(-1, 1))
        setContentPane(contentPane)
    }

    void show(Collection<ResultObject> results) {
        for (ResultObject resultObject : results) {
            JPanel panel = new JPanel()

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
            panel.setBorder(new LineBorder(Color.BLACK))

            JLabel title = new JLabel(app.getMessage('result.caption') + ': ' + resultObject.X)
            panel.add(title)

            JLabel cfLabel = new JLabel(app.getMessage('result.cf') + ': ' +
                    new DecimalFormat('#,##0').format(resultObject.activation.factor * 100.0))
            panel.add(cfLabel)

            JLabel otLabel = new JLabel(app.getMessage('result.otList') + ': ' + resultObject.getOtList())
            panel.add(otLabel)

            JLabel onLabel = new JLabel(app.getMessage('result.notList') + ': ' + resultObject.getNotList())
            panel.add(onLabel)

            JLabel orLabel = new JLabel(app.getMessage('result.orList') + ': ' + resultObject.getOrList())
            panel.add(orLabel)

            contentPane.add(panel)
        }

        setVisible(true)
    }
}