package se.sojo.apps.maths.calculator.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import se.sojo.apps.maths.calculator.core.Calculator
import se.sojo.apps.maths.calculator.MainActivity
import se.sojo.apps.maths.calculator.MainActivity.Companion.DEBUG
import se.sojo.apps.maths.calculator.R
import se.sojo.apps.maths.calculator.core.Calculator.Companion.formatValue
import se.sojo.apps.maths.calculator.core.Calculator.Companion.maxDecimalsAndZeros
import se.sojo.apps.maths.calculator.core.cleanUpDecimalSeparator
import se.sojo.apps.maths.calculator.core.cleanUpMinusSign
import se.sojo.apps.maths.calculator.core.cleanUpZeroValue
import se.sojo.apps.maths.calculator.core.performHapticFeedback
import se.sojo.apps.maths.calculator.core.removeThousandSeparator
import se.sojo.apps.maths.calculator.core.tryParse
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cbrt
import kotlin.math.pow
import kotlin.math.sqrt

// Operation buttons
@SuppressLint("ClickableViewAccessibility")
fun MainActivity.setOperationButtons() {
    btnAdd = findViewById(R.id.btn_add)
    btnSubtract = findViewById(R.id.btn_subtract)
    btnMultiply = findViewById(R.id.btn_multiply)
    btnDivide = findViewById(R.id.btn_divide)
    btnModulo = findViewById(R.id.btn_modulo)
    btnPercent = findViewById(R.id.btn_percent)
    btnSquare = findViewById(R.id.btn_square)
    btnCube = findViewById(R.id.btn_cube)
    btnPower = findViewById(R.id.btn_power)
    btnSquareRoot = findViewById(R.id.btn_square_root)
    btnCubeRoot = findViewById(R.id.btn_cube_root)
    btnReciprocal = findViewById(R.id.btn_reciprocal)

    // PI, RND, Toggle (+/-) buttons
    btnPi = findViewById(R.id.btn_pi)
    btnRnd = findViewById(R.id.btn_rnd)
    btnToggle = findViewById(R.id.btn_toggle)

    // Delete, Equal, CLear buttons
    btnDelete = findViewById(R.id.btn_delete)
    btnEqual = findViewById(R.id.btn_equal)
    btnClear = findViewById(R.id.btn_clear)

    /******************************************************
     * Operation Buttons onClick
     */
    btnAdd?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnAdd!!, motionEvent) }
    btnSubtract?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnSubtract!!, motionEvent) }
    btnMultiply?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnMultiply!!, motionEvent) }
    btnDivide?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnDivide!!, motionEvent) }
    btnModulo?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnModulo!!, motionEvent) }
    btnPercent?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnPercent!!, motionEvent) }
    btnSquare?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnSquare!!, motionEvent) }
    btnCube?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnCube!!, motionEvent) }
    btnPower?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnPower!!, motionEvent) }
    btnSquareRoot?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnSquareRoot!!, motionEvent) }
    btnCubeRoot?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnCubeRoot!!, motionEvent) }
    btnReciprocal?.setOnTouchListener { _, motionEvent -> operationButtonAction(btnReciprocal!!, motionEvent) }

    /******************************************************
     * Other Buttons onClick
     */
    btnPi?.setOnTouchListener { _, motionEvent -> piButtonAction(motionEvent) }
    btnRnd?.setOnTouchListener { _, motionEvent -> rndButtonAction(motionEvent) }
    btnToggle?.setOnTouchListener { _, motionEvent -> toggleButtonAction(motionEvent) }
    btnDelete?.setOnTouchListener { _, motionEvent -> deleteButtonAction(motionEvent) }
    btnEqual?.setOnTouchListener { _, motionEvent -> equalButtonAction(motionEvent) }
    btnClear?.setOnTouchListener { _, motionEvent -> clearButtonAction(motionEvent) }
}

// ========= OPERATOR BUTTON ACTIONS =========
private fun MainActivity.operationButtonAction(btn: Button, motionEvent: MotionEvent): Boolean {
    if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> " + btn.text.toString() + ", " + firstValue.toString() + ", " + secondValue.toString() + ", " + currentOperator.toString() + ", " + previousOperator.toString())



    return performHapticFeedback(btn, motionEvent)
}


// ========= CLEAR (C) BUTTON =========
private fun MainActivity.clearButtonAction(motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Clear everything if Clear Entry (CE) is set to true
            if (clearEntry) {
                // Set CE to false
                clearEntry = false

                // Reset all values
                tvPreviousResult?.text = ""
                tvCurrentResult?.text = "0"
                //adjustLabelFont(tvCurrentResult, true)
                //adjustLabelFont(tvPreviousResult, true)

                resetValues()

                hasResult = false
            } else {
                // Clear the entry only

                // Reset variable and previous result if calculation result is set to true
                if (hasResult) {
                    tvPreviousResult?.text = ""
                    hasResult = false
                }

                // Set CE to true
                clearEntry = true

                // Reset values
                tvCurrentResult?.text = "0"
                hasResult = false
            }
        }
    }

    return performHapticFeedback(btnClear, motionEvent)
}

// ========= DELETE BUTTON =========
private fun MainActivity.deleteButtonAction(motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Reset variable and previous result if calculation result is set to true
            if (hasResult) {
                tvPreviousResult?.text = ""
                hasResult = false
            }

            // Delete number from the end if no calculation has been made
            //if (!hasResult && tvCurrentResult?.text.toString().trim().isNotEmpty()) {
            if (tvCurrentResult?.text.toString().trim().isNotEmpty()) {
                if (tvCurrentResult?.text.toString() == "NaN") {
                    tvCurrentResult?.text = "0"
                } else {
                    val output = tvCurrentResult?.text?.substring(0, tvCurrentResult?.text.toString().length - 1)
                    tvCurrentResult?.text = output?.trim()
                }
                //adjustLabelFont(tvCurrentResult)
            }

            // If the value is empty replaces it with a zero
            if (tvCurrentResult?.text.toString().trim() == "")
                tvCurrentResult?.text = "0"

            // Set CE to false
            clearEntry = false
        }
    }

    return performHapticFeedback(btnDelete, motionEvent)
}


// ========= EQUAL BUTTON =========
private fun MainActivity.equalButtonAction(motionEvent: MotionEvent?): Boolean {
    if (DEBUG) Log.d("SOJO Debug:",
        "equalButtonAction -> $firstValue, $secondValue, $currentOperator, $previousOperator"
    )

    return performHapticFeedback(btnEqual, motionEvent)
}

// ========= PI BUTTON =========
private fun MainActivity.piButtonAction(motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Set the firstValue if it's zero or else set the second value to PI (3.14159...)
            if (firstValue == 0.0) {
                if (!hasResult) {
                //if (!hasResult || tvPreviousResult?.text.toString() == "rnd =") {
                    firstValue = Math.PI
                    displayResult(firstValue.toString(),"0",currentOperator,firstValue,"",Calculator.getOperatorSign(Calculator.Operator.PI))
                    firstValue = 0.0

                    // Flag that a calculation has been made
                    hasResult = true
                } else {
                    if (tvPreviousResult?.text.toString() == "rnd =") {
                        firstValue = Math.PI
                        tvPreviousResult?.text = "π ="
                        tvCurrentResult?.text = formatValue(firstValue.toString())
                        firstValue = 0.0

                        //adjustLabelFont(tvCurrentResult)
                    } else if (hasResult) {
                        firstValue = Math.PI
                        tvPreviousResult?.text = "π ="
                        tvCurrentResult?.text = formatValue(firstValue.toString())
                        firstValue = 0.0

                        //adjustLabelFont(tvCurrentResult)
                    }
                }
            } else {
                secondValue = Math.PI
                tvCurrentResult?.text = formatValue(secondValue.toString())
                //adjustLabelFont(tvCurrentResult)
            }
        }
    }

    return performHapticFeedback(btnPi, motionEvent)
}

// ========= RND BUTTON =========
private fun MainActivity.rndButtonAction(motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Get a random integer between 1 and 9
            val rndValue: Double = (1..9).random().toDouble()

            // Set the firstValue if it's zero or else set the second value to the created random value
            if (firstValue == 0.0) {
                firstValue = rndValue

                // Display the random value
                tvPreviousResult?.text = buildString {
                    append(Calculator.getOperatorSign(Calculator.Operator.RND))
                    append(" =")
                }
                tvCurrentResult?.text = formatValue(firstValue.toString())

                // Adjust the font size depending on the number size
                //adjustLabelFont(tvPreviousResult)
                //adjustLabelFont(tvCurrentResult)

                firstValue = 0.0

                // Flag that a calculation has been made
                hasResult = true
            } else {
                secondValue = rndValue
                tvCurrentResult?.text = formatValue(secondValue.toString())
                //adjustLabelFont(tvCurrentResult)
            }
        }
    }

    return performHapticFeedback(btnRnd, motionEvent)
}

// ========= TOGGLE (+/-) BUTTON =========
private fun MainActivity.toggleButtonAction(motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Don't toggle the value if empty or zero
            if (tvCurrentResult?.text == "") {
                return performHapticFeedback(btnToggle, motionEvent)
            }

            // Reset variable and previous result if calculation result is set to true, but save status for hasResult
            if (hasResult)
                tvPreviousResult?.text = ""

            // Remove any whitespaces
            val value: String = tvCurrentResult?.text.toString().cleanUpMinusSign().trim()

            // Toggle the value
            if (value.startsWith("-"))
                tvCurrentResult?.text = value.substring(1)
            else
                tvCurrentResult?.text = buildString {
                    append("-")
                    append(value)
                }

            // Set CE to false
            clearEntry = false
        }
    }

    return performHapticFeedback(btnToggle, motionEvent)
}

// ========= DISPLAY RESULT =========
private fun MainActivity.displayResult(firstDisplayValue: String, secondDisplayValue: String, operation: Calculator.Operator, result: Double, extraSignRight: String = "", extraSignLeft: String = "") {

}

// ========= RESET VALUES =========
private fun MainActivity.resetValues() {
    firstValue = 0.0
    secondValue = 0.0
    currentOperator = Calculator.Operator.NONE
    previousOperator = Calculator.Operator.NONE
}