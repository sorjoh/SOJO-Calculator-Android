package se.sojo.apps.maths.calculator.core

import android.util.Log
import se.sojo.apps.maths.calculator.MainActivity.Companion.DEBUG
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cbrt
import kotlin.math.pow
import kotlin.math.sqrt

class Calculator {
    enum class Operator {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MODULO,
        PERCENT,
        SQUARE,
        CUBE,
        PI,
        POWER,
        SQUARE_ROOT,
        CUBE_ROOT,
        RECIPROCAL,
        RND,
        EQUALS,
        NONE
    }

    @Suppress("DEPRECATION")
    companion object {
        var DECIMAL_SEPARATOR: String = "."
        var THOUSAND_SEPARATOR: String = ","

        // Max length of the display numbers
        const val MAX_LENGTH: Int = 24

        var numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("en", "US"))

        // Set the base font size
        const val BASE_FONT_SIZE: Float = 24f

        fun getOperatorSign(op: Operator): String {
            return when (op) {
                Operator.ADD -> "+"
                Operator.SUBTRACT -> "-"
                Operator.MULTIPLY -> "x"
                Operator.DIVIDE -> "÷"
                Operator.MODULO -> "≡"
                Operator.PERCENT -> "%"
                Operator.SQUARE -> "x²"
                Operator.CUBE -> "x³"
                Operator.PI ->  "π"
                Operator.POWER -> "^"
                Operator.SQUARE_ROOT -> "√"
                Operator.CUBE_ROOT -> "∛"
                Operator.RECIPROCAL -> "1/x"
                Operator.RND -> "rnd"
                Operator.EQUALS -> "="
                Operator.NONE -> ""
            }
        }

        fun getOperator(key: String): Operator {
            return when (key) {
                "+" -> Operator.ADD
                "−", "-" -> Operator.SUBTRACT
                "x", "×" -> Operator.MULTIPLY
                "÷", "/" -> Operator.DIVIDE
                "mod", "≡" -> Operator.MODULO
                "%" -> Operator.PERCENT
                "x²" -> Operator.SQUARE
                "x³" -> Operator.CUBE
                "π" -> Operator.PI
                "^" -> Operator.POWER
                "√" -> Operator.SQUARE_ROOT
                "∛" -> Operator.CUBE_ROOT
                "1/x"-> Operator.RECIPROCAL
                "rnd" -> Operator.RND
                "=" -> Operator.EQUALS
                else -> Operator.NONE
            }
        }

        fun formatValue(input: String, forceFormat: Boolean = false): String {
            if (DEBUG) Log.d("SOJO Debug:", "formatValue -> $input, $forceFormat")

            return if (input.length > 1 && input.take(1) == "0" && !input.contains(DECIMAL_SEPARATOR)) {
                if (DEBUG) Log.d("SOJO Debug:", "formatValue -> IF START")

                input.cleanUpMinusSign().substring(1).cleanUpZeroValue()
            } else if (input.contains(DECIMAL_SEPARATOR)) {
                if (DEBUG) Log.d("SOJO Debug:", "formatValue -> IF START -> ELSE IF")

                if (forceFormat) {
                    if (DEBUG) Log.d("SOJO Debug:", "formatValue -> IF 1")

                    val cleanupValue = input.replace(160.toChar().toString(),"")
                    numberFormat.format(cleanupValue.cleanUpMinusSign().removeThousandSeparator().cleanUpDecimalSeparator().toDouble()).cleanUpZeroValue()
                } else {
                    if (DEBUG) Log.d("SOJO Debug:", "formatValue -> IF START -> ELSE IF -> ELSE")

                    input.cleanUpMinusSign().cleanUpZeroValue()
                }
            } else {
                if (DEBUG) Log.d("SOJO Debug:", "formatValue -> IF START -> ELSE")

                val cleanupValue = input.replace(160.toChar().toString(),"")
                numberFormat.format(cleanupValue.cleanUpMinusSign().removeThousandSeparator().cleanUpDecimalSeparator().toDouble()).cleanUpZeroValue()
            }
        }

        fun calculate(firstValue: Double, secondValue: Double, operation: Operator): Double {
            var result = 0.0

            when (operation) {
                Operator.ADD -> result = firstValue + secondValue
                Operator.SUBTRACT -> result = firstValue - secondValue
                Operator.MULTIPLY -> result = firstValue * secondValue
                Operator.DIVIDE -> result = firstValue / secondValue
                Operator.MODULO -> result = firstValue % secondValue
                Operator.PERCENT -> result = firstValue / 100
                Operator.SQUARE -> result = firstValue * firstValue
                Operator.CUBE -> result = firstValue * firstValue * firstValue
                Operator.PI -> result = PI
                Operator.POWER -> result = firstValue.pow(secondValue)
                Operator.SQUARE_ROOT -> result = sqrt(firstValue)
                Operator.CUBE_ROOT -> result = cbrt(firstValue)
                Operator.RECIPROCAL -> result = 1 / firstValue

                else -> if (DEBUG) Log.d("SOJO Debug:", "calculate -> Case Else")
            }

            return result
        }
    }
}