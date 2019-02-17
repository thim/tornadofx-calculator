package net.thm.calc

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import net.thm.calc.Operator.*
import tornadofx.View

class Calculator : View() {
    override val root: VBox by fxml("/Calculator.fxml")

    @FXML
    lateinit var display: Label

    init {
        title = "Calculator"

        root.lookupAll(".button").forEach { b ->
            b.setOnMouseClicked {
                operator((b as Button).text)
            }
        }

        root.addEventFilter(KeyEvent.KEY_TYPED) {
            operator(it.character.toUpperCase().replace("\r", "="))
        }
    }

    private var state: Operator = Add(0)

    private fun onAction(fn: Operator) {
        state = fn
        display.text = ""
    }

    private val displayValue: Long
        get() = when (display.text) {
            "" -> 0
            else -> display.text.toLong()
        }

    private fun operator(x: String) {
        if (Regex("[0-9]").matches(x)) {
            display.text += x
        } else {
            when (x) {
                "+" -> onAction(Add(displayValue))
                "-" -> onAction(Sub(displayValue))
                "/" -> onAction(Div(displayValue))
                "%" -> {
                    onAction(Add(displayValue / 100))
                    operator("=")
                }
                "X" -> onAction(Mult(displayValue))
                "C" -> onAction(Add(0))
                "+/-" -> {
                    onAction(Add(-1 * displayValue))
                    operator("=")
                }
                "=" -> display.text = state.calculate(displayValue).toString()
            }
        }
    }
}