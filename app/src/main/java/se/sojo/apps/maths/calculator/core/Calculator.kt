package se.sojo.apps.maths.calculator.core

import android.util.Log
import se.sojo.apps.maths.calculator.MainActivity.Companion.DEBUG
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cbrt
import kotlin.math.pow
import kotlin.math.round
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
        const val maxDecimalsAndZeros = 10

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

            var result = if (input.length > 1 && input.take(1) == "0" && !input.contains(DECIMAL_SEPARATOR)) {
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

            //Log.d("SOJO Debug:", round(result.tryParse()).toString())
            //result = BigDecimal(result.tryParse()).setScale(10, RoundingMode.HALF_EVEN).toString()


            //result = BigDecimal(result.tryParse()).setScale(2, RoundingMode.HALF_EVEN).toString()

            return result
        }

        fun calculate(firstValue: Double, secondValue: Double, operation: Operator): Double {
            var result = BigDecimal(0) //0.0

            try {
                when (operation) {
                    Operator.ADD -> result = BigDecimal(firstValue + secondValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.SUBTRACT -> result = BigDecimal(firstValue - secondValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.MULTIPLY -> result = BigDecimal(firstValue * secondValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.DIVIDE -> result = BigDecimal(firstValue / secondValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.MODULO -> result = BigDecimal(firstValue % secondValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.PERCENT -> result = BigDecimal(firstValue / 100).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.SQUARE -> result = BigDecimal(firstValue * firstValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.CUBE -> result =
                        BigDecimal(firstValue * firstValue * firstValue).setScale(
                            maxDecimalsAndZeros,
                            RoundingMode.HALF_EVEN
                        ).stripTrailingZeros()

                    Operator.PI -> result =
                        BigDecimal(PI).setScale(maxDecimalsAndZeros, RoundingMode.HALF_EVEN)
                            .stripTrailingZeros()

                    Operator.POWER -> result = BigDecimal(firstValue.pow(secondValue)).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.SQUARE_ROOT -> result = BigDecimal(sqrt(firstValue)).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.CUBE_ROOT -> result = BigDecimal(cbrt(firstValue)).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    Operator.RECIPROCAL -> result = BigDecimal(1 / firstValue).setScale(
                        maxDecimalsAndZeros,
                        RoundingMode.HALF_EVEN
                    ).stripTrailingZeros()

                    else -> if (DEBUG) Log.d("SOJO Debug:", "calculate -> Case Else")
                }
            } catch (e: Exception) {
                if (DEBUG) Log.d("SOJO Debug:", "calculate -> catch")
            }

            return result.toDouble()
        }
    }
}