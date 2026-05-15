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
import se.sojo.apps.maths.calculator.core.cleanUpDecimalSeparator
import se.sojo.apps.maths.calculator.core.cleanUpMinusSign
import se.sojo.apps.maths.calculator.core.cleanUpZeroValue
import se.sojo.apps.maths.calculator.core.performHapticFeedback
import se.sojo.apps.maths.calculator.core.removeThousandSeparator
import se.sojo.apps.maths.calculator.core.tryParse
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

    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Initialize the result variable and set it to zero
            var result = "0"

            // Set the status of Clear Entry to false
            hasResult = false

            // Set previous operation
            previousOperator = currentOperator

            // Set current operation
            currentOperator = Calculator.getOperator(btn.text as String)

            //val oldFirstValue: Double = firstValue
            val oldSecondValue: Double = secondValue

            var input: String = tvCurrentResult?.text.toString().removeThousandSeparator().cleanUpDecimalSeparator()
            input = input.replace(160.toChar().toString(), "").cleanUpMinusSign()

            // Set the firstValue if it's zero or else set the second value
            if (firstValue == 0.0)
                firstValue = input.tryParse()
            else
                secondValue = input.tryParse()

            // If both no operator and second value or else both first value and second value are present
            if (currentOperator != Calculator.Operator.NONE && !tvCurrentResult?.text.isNullOrEmpty() && secondValue != 0.0) {
                if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF START")
                // Calculate and display the result

                // Percent is current operation
                if (currentOperator == Calculator.Operator.PERCENT) {
                    if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF PERCENT (current)")

                    secondValue = Calculator.calculate(secondValue, 0.0, currentOperator)
                    result = Calculator.calculate(firstValue, secondValue, previousOperator).toString()

                    // Use previous operator
                    when (previousOperator) {
                        // === ADD BUTTON ===
                        Calculator.Operator.ADD -> result = Calculator.calculate(firstValue, 1.0 + secondValue,Calculator.Operator.MULTIPLY).toString()

                        // === SUBTRACT BUTTON ===
                        Calculator.Operator.SUBTRACT -> result = Calculator.calculate(firstValue, 1.0 - secondValue,Calculator.Operator.MULTIPLY).toString()

                        // === MULTIPLY BUTTON ===
                        Calculator.Operator.MULTIPLY -> result = Calculator.calculate(firstValue, secondValue,Calculator.Operator.MULTIPLY).toString()

                        // === DIVIDE BUTTON ===
                        Calculator.Operator.DIVIDE -> result = Calculator.calculate(firstValue, secondValue,Calculator.Operator.DIVIDE).toString()

                        else -> if (DEBUG) Log.d("SOJO Debug:","operationButtonAction -> IF PERCENT (current) -> Case Else: firstValue=$firstValue, secondValue = $secondValue result=$result, currentOperation=" + Calculator.getOperatorSign(currentOperator) + ", previousOperation=" + Calculator.getOperatorSign(previousOperator))
                    }

                    // Display the result
                    displayResult(firstValue.toString(),(secondValue * 100.0).toString(),previousOperator, result.toDouble(),Calculator.getOperatorSign(currentOperator))

                    // FLag that a calculation has been done
                    hasResult = true

                    // Reset values
                    resetValues()

                    // Percent is previous operation
                } else if (previousOperator == Calculator.Operator.PERCENT) {
                    if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF PERCENT (previous)")

                    // Fix when calculating xx % xx - xx
                    if (currentOperator != Calculator.Operator.PERCENT) {
                        if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF PERCENT (previous->current)")

                        // Set the second value
                        secondValue = input.tryParse()

                        // Calculate and view the result
                        result = Calculator.calculate(firstValue, secondValue,Calculator.Operator.MULTIPLY).toString()
                        displayResult((firstValue * 100.0).toString(),secondValue.toString(),Calculator.Operator.MULTIPLY, result.toDouble(),"",Calculator.getOperatorSign(Calculator.Operator.PERCENT))

                        // Mark that a calculation result has been made
                        hasResult = true

                        // Reset variables
                        firstValue = result.toDouble()
                        secondValue = 0.0

                        // Other percent calculation when the percent sign shall be showed
                    } else {
                        if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF ELSE PERCENT (previous->previous)")

                        // Calculate and view the result
                        result = Calculator.calculate(firstValue, secondValue,Calculator.Operator.MULTIPLY).toString()
                        displayResult((firstValue * 100.0).toString(),secondValue.toString(),Calculator.Operator.MULTIPLY, result.toDouble(),"",Calculator.getOperatorSign(Calculator.Operator.PERCENT))

                        // Mark that a calculation has been made
                        hasResult = true

                        // Reset variables
                        resetValues()
                    }
                } else {
                    if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF ELSE PERCENT (previous)")

                    // Operations with Square, Square Root, Cube, Cube Root and Power, Reciprocal
                    when (currentOperator) {
                        // === SQUARE BUTTON ===
                        Calculator.Operator.SQUARE -> {
                            // Calculate and view the result
                            result = Calculator.calculate(firstValue, secondValue * secondValue,previousOperator).toString()
                            displayResult(firstValue.toString(),secondValue.toString(),previousOperator,result.toDouble(),"²","")

                            // Flag that a calculation has been made
                            hasResult = true

                            // Reset variables
                            resetValues()
                        }

                        // === CUBE BUTTON ===
                        Calculator.Operator.CUBE -> {
                            // Calculate and view the result
                            result = Calculator.calculate(firstValue,secondValue * secondValue * secondValue,previousOperator).toString()
                            displayResult(firstValue.toString(),secondValue.toString(),previousOperator,result.toDouble(),"³","")

                            // Flag that a calculation has been made
                            hasResult = true

                            // Reset variables
                            resetValues()
                        }

                        // === POWER BUTTON ===
                        Calculator.Operator.POWER -> {
                            // Show the value and the power sign in previous result
                            tvPreviousResult?.text = buildString {
                                append(formatValue(firstValue.toString()))
                                append(" ")
                                append(Calculator.getOperatorSign(previousOperator))
                                append(" ")
                                append(secondValue)
                                append("^")
                            }

                            // Set current result to zero
                            tvCurrentResult?.text = "0"

                            // Adjust the size of the labels depending on how many numbers is present
                            //adjustLabelFont(tvPreviousResult)
                            //adjustLabelFont(tvCurrentResult)
                        }

                        //=== SQUARE ROOT BUTTON ===
                        Calculator.Operator.SQUARE_ROOT -> {
                            // Calculate and view the result
                            result = Calculator.calculate(firstValue,sqrt(secondValue),previousOperator).toString()
                            tvPreviousResult?.text = buildString {
                                append(formatValue(firstValue.toString()))
                                append(" ")
                                append(Calculator.getOperatorSign(previousOperator))
                                append(" ")
                                append(Calculator.getOperatorSign(currentOperator))
                                append(secondValue)
                                append(" =")
                            }
                            tvCurrentResult?.text = formatValue(result)

                            // Adjust the size of the labels depending on how many numbers is present
                            //adjustLabelFont(tvPreviousResult)
                            //adjustLabelFont(tvCurrentResult)

                            // Flag that a calculation has been made
                            hasResult = true

                            // Reset variables
                            resetValues()
                        }

                        // === CUBE ROOT BUTTON ===
                        Calculator.Operator.CUBE_ROOT -> {
                            // Calculate and view the result
                            result = Calculator.calculate(firstValue,cbrt(secondValue),previousOperator).toString()
                            tvPreviousResult?.text = buildString {
                                append(formatValue(firstValue.toString()))
                                append(" ")
                                append(Calculator.getOperatorSign(previousOperator))
                                append(" ")
                                append(Calculator.getOperatorSign(currentOperator))
                                append(secondValue)
                                append(" =")
                            }
                            tvCurrentResult?.text = formatValue(result)

                            // Adjust the size of the labels depending on how many numbers is present
                            //adjustLabelFont(tvPreviousResult)
                            //adjustLabelFont(tvCurrentResult)

                            // Flag that a calculation has been made
                            hasResult = true

                            // Reset variables
                            resetValues()
                        }

                        // === RECIPROCAL BUTTON ===
                        Calculator.Operator.RECIPROCAL -> {
                            // Calculate and view the result
                            result = Calculator.calculate(firstValue,1.0 / secondValue,previousOperator).toString()
                            displayResult(firstValue.toString(),(1.0 / secondValue).toString(),previousOperator, result.toDouble())

                            // Flag that a calculation has been made
                            hasResult = true

                            // Reset variables
                            resetValues()
                        }

                        else -> {
                            if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF ELSE PERCENT (previous->CASE ELSE)")

                            // Power Button is previous operation
                            if (previousOperator == Calculator.Operator.POWER) {
                                // Only the first power number is present otherwise add the saved old second value for the second power number
                                if (oldSecondValue == 0.0) {
                                    // Calculate the result
                                    result = Calculator.calculate(firstValue, secondValue,previousOperator).toString()
                                } else {
                                    // Calculate the total calculation
                                    val powerValue: Double = Calculator.calculate(oldSecondValue, secondValue,previousOperator)
                                    result = Calculator.calculate(firstValue,powerValue,currentOperator).toString()
                                }
                            } else {
                                // Calculate other calculations
                                result = Calculator.calculate(firstValue, secondValue,previousOperator).toString()
                            }

                            // Display the result in previous result and set current result to zero
                            displayResult(result, "", currentOperator, 0.0)

                            // Set the result to the first value and reset the second value
                            firstValue = result.toDouble()
                            secondValue = 0.0
                        }
                    }
                }
                // Power is previous operation
            } else if (previousOperator == Calculator.Operator.POWER) {
                if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF START ELSE IF (previous->POWER)")

                // Calculate and display the result
                result = firstValue.pow(secondValue).toString()
                tvPreviousResult?.text = buildString {
                    append(formatValue(result))
                    append(" ")
                    append(Calculator.getOperatorSign(currentOperator))
                }

                // Adjust the font size depending on how many numbers
                //adjustLabelFont(tvPreviousResult)
            } else {
                if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF START ELSE")

                // Calculate the result
                result = Calculator.calculate(firstValue, secondValue, currentOperator).toString()

                // Display the result for current operation
                when (currentOperator) {
                    // === PERCENT OPERATION ===
                    Calculator.Operator.PERCENT -> {
                        displayResult(firstValue.toString(), "", currentOperator, result.toDouble())
                        resetValues()
                        hasResult = true
                    }

                    // === SQUARE OPERATION ===
                    Calculator.Operator.SQUARE -> {
                        if (firstValue == 0.0) {
                            tvCurrentResult?.text = "0"
                            tvPreviousResult?.text = ""
                        } else {
                            // Calculate and show the result
                            result = Calculator.calculate(
                                firstValue,
                                firstValue,
                                Calculator.Operator.SQUARE
                            ).toString()
                            displayResult(
                                firstValue.toString(),
                                firstValue.toString(),
                                Calculator.Operator.NONE,
                                result.toDouble(),
                                "",
                                "²"
                            )

                            // Flag that a calculation has been made
                            hasResult = true
                        }

                        // Reset variables
                        resetValues()
                    }

                    // === CUBE OPERATION ===
                    Calculator.Operator.CUBE -> {
                        if (firstValue == 0.0) {
                            tvCurrentResult?.text = "0"
                            tvPreviousResult?.text = ""
                        } else {
                            // Calculate and show the result
                            result = Calculator.calculate(
                                firstValue,
                                firstValue,
                                Calculator.Operator.CUBE
                            ).toString()
                            displayResult(
                                firstValue.toString(),
                                firstValue.toString(),
                                Calculator.Operator.NONE,
                                result.toDouble(),
                                "",
                                "³"
                            )

                            // Flag that a calculation has been made
                            hasResult = true
                        }

                        // Reset variables
                        resetValues()
                    }

                    // === SQUARE ROOT OPERATION ===
                    Calculator.Operator.SQUARE_ROOT, Calculator.Operator.CUBE_ROOT -> {
                        if (firstValue == 0.0) {
                            tvCurrentResult?.text = "0"
                            tvPreviousResult?.text = ""
                        } else {
                            // Calculate the result
                            result = Calculator.calculate(firstValue, firstValue, currentOperator)
                                .toString()

                            // Display the result
                            tvPreviousResult?.text = buildString {
                                append(Calculator.getOperatorSign(currentOperator))
                                append(formatValue(firstValue.toString()))
                                append(" =")
                            }
                            tvCurrentResult?.text = formatValue(result)

                            tvHistoryResult?.text = buildString {
                                append("\n")
                                append(history)
                            }
                            history = history + "\n" + (tvPreviousResult?.text.toString() + " " + tvCurrentResult?.text.toString()).trim('\n')

                            if (tvHistoryResult?.text.toString().isNotBlank()) {
                                tvDivider?.visibility = View.VISIBLE
                                tvHistoryResult?.visibility = View.GONE
                                tvHistoryResult?.visibility = View.VISIBLE
                            }

                            // Adjust the font size depending on how many numbers
                            //adjustLabelFont(tvPreviousResult)
                            //adjustLabelFont(tvCurrentResult)


                            // Flag that a calculation has been made
                            hasResult = true
                        }
                        // Reset variables
                        resetValues()
                    }

                    // === RECIPROCAL OPERATION ===
                    Calculator.Operator.RECIPROCAL -> {
                        if (firstValue == 0.0) {
                            tvCurrentResult?.text = "0"
                            tvPreviousResult?.text = ""
                        } else {
                            // Calculate and show the result
                            result =
                                Calculator.calculate(1.0, firstValue, Calculator.Operator.DIVIDE)
                                    .toString()
                            displayResult(
                                "1",
                                firstValue.toString(),
                                Calculator.Operator.DIVIDE,
                                result.toDouble()
                            )

                            // Flag that a calculation has been made
                            hasResult = true
                        }
                        // Reset variables
                        resetValues()
                    }

                    else -> {
                        if (DEBUG) Log.d("SOJO Debug:", "operationButtonAction -> IF START ELSE -> CASE ELSE")

                        if (firstValue == 0.0 && currentOperator == Calculator.Operator.POWER) {
                            resetValues()
                            tvCurrentResult?.text = "0"
                            tvPreviousResult?.text = ""
                        } else {
                            displayResult(firstValue.toString(),"",currentOperator, result.toDouble())
                        }
                    }
                }
            }
        }
    }

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

    when (motionEvent?.action) {
        MotionEvent.ACTION_UP -> {
            // Save the current values for later use
            //val oldFirstValue = firstValue
            val oldSecondValue = secondValue

            var input: String = tvCurrentResult?.text.toString().removeThousandSeparator().cleanUpDecimalSeparator()
            input = input.replace(160.toChar().toString(), "")

            // Replace all none minus characters with proper one
            input = input.cleanUpMinusSign()

            // Make the calculation if no calculation has been done and if there is no zero values
            if (!hasResult) {
                if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult)")

                var result: String // = "0"

                if (input.toDoubleOrNull() != 0.0 ) {
                    if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input)")

                    if (tvPreviousResult?.text.toString().trim() != "") {
                        if (tvPreviousResult?.text.toString()[0] == '0') {
                            secondValue = input.tryParse()
                        } else {
                            // Set the firstValue if it's zero or else set the second value
                            if (firstValue == 0.0) {
                                //firstValue = tvCurrentResult?.text.tryParse()
                                firstValue = input.tryParse()
                            } else {
                                //secondValue = tvCurrentResult?.text.tryParse()
                                secondValue = input.tryParse()
                            }
                        }
                    }

                    // Calculate and show the result
                    if (currentOperator == Calculator.Operator.PERCENT) {
                        if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> IF currentOperator = PERCENT")

                        result = Calculator.calculate(firstValue, secondValue,Calculator.Operator.MULTIPLY).toString()

                        if (previousOperator == Calculator.Operator.NONE)
                            displayResult((firstValue * 100).toString(),secondValue.toString(),Calculator.Operator.MULTIPLY,result.toDouble(),"",Calculator.getOperatorSign(Calculator.Operator.PERCENT)                            )
                        else
                            displayResult((firstValue * 100).toString(),secondValue.toString(),currentOperator,result.toDouble())

                    } else if (currentOperator == Calculator.Operator.POWER) {
                        if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> Else IF currentOperator = POWER")

                        if (oldSecondValue == 0.0) {
                            result = Calculator.calculate(firstValue, secondValue, currentOperator).toString()
                            tvPreviousResult?.text = buildString {
                                append(formatValue(firstValue.toString()))
                                append(" ")
                                append(Calculator.getOperatorSign(currentOperator))
                                append(" ")
                                append(formatValue(secondValue.toString()))
                                append(" = ")
                            }

                            tvHistoryResult?.text = buildString {
                                append("\n")
                                append(history)
                            }

                            val thisHistory: String = formatValue(firstValue.toString(), true) + " " + Calculator.getOperatorSign(currentOperator) + " " + formatValue(secondValue.toString(), true) + " = " + formatValue(result, true)

                            history = history + "\n" + thisHistory.trim('\n')

                            if (tvHistoryResult?.text.toString().isNotBlank()) {
                                tvDivider?.visibility = View.VISIBLE
                                tvHistoryResult?.visibility = View.GONE
                                tvHistoryResult?.visibility = View.VISIBLE
                            }
                        } else {
                            if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> IF currentOperator = POWER -> ELSE")

                            val powerValue = Calculator.calculate(oldSecondValue, secondValue,currentOperator).toString()
                            result = Calculator.calculate(firstValue,powerValue.toDouble(),previousOperator).toString()
                            tvPreviousResult?.text = buildString {
                                append(formatValue(firstValue.toString()))
                                append(" ")
                                append(Calculator.getOperatorSign(previousOperator))
                                append(" ")
                                append(formatValue(oldSecondValue.toString()))
                                append(Calculator.getOperatorSign(currentOperator))
                                append(formatValue(secondValue.toString()))
                                append(" = ")
                            }
                        }

                        tvCurrentResult?.text = formatValue(result)

                        //adjustLabelFont(tvPreviousResult)
                        //adjustLabelFont(tvCurrentResult)
                    } else {
                        if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> IF currentOperator -> ELSE")

                        if (currentOperator == Calculator.Operator.NONE) {
                            result = firstValue.toString()
                            displayResult(firstValue.toString(),"0",currentOperator,result.toDouble())
                        } else {
                            result = Calculator.calculate(firstValue, secondValue, currentOperator).toString()
                            displayResult(firstValue.toString(),secondValue.toString(),currentOperator,result.toDouble())
                        }
                    }
                } else {
                    if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> ELSE")

                    if (currentOperator == Calculator.Operator.POWER) {
                        if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> ELSE -> currentOperator = POWER")

                        result = firstValue.pow(secondValue).toString()
                        tvPreviousResult?.text = buildString {
                            append(formatValue(firstValue.toString()))
                            append(Calculator.getOperatorSign(currentOperator))
                            append(formatValue(secondValue.toString()))
                            append(" =")
                        }
                        tvCurrentResult?.text = formatValue(result)

                        //adjustLabelFont(tvPreviousResult)
                        //adjustLabelFont(tvCurrentResult)
                    } else {
                        if (DEBUG) Log.d("SOJO Debug:", "equalButtonAction -> IF START (!hasResult) -> IF START(input) -> ELSE -> currentOperator -> ELSE")

                        result = firstValue.toString()
                        if (currentOperator == Calculator.Operator.PERCENT)
                            displayResult((firstValue * 100).toString(),"0",Calculator.Operator.NONE,result.toDouble(),"","%")
                        else
                            displayResult(firstValue.toString(),"0",Calculator.Operator.NONE, result.toDouble())
                    }
                }

                // Mark that a calculation result has been made
                hasResult = true

                // Reset variables
                resetValues()
            }
        }
    }

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
    tvCurrentResult?.text = ""

    if (DEBUG) Log.d(
        "SOJO Debug:",
        "displayResult -> $firstDisplayValue, $secondDisplayValue, $operation, $result"
    )

    if (secondDisplayValue.isEmpty()) {
        if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START")

        if (operation == Calculator.Operator.POWER) {
            if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF (operation->POWER)")

            tvPreviousResult?.text = buildString {
                append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
                append(" ")
                append(Calculator.getOperatorSign(operation))
            }
            tvCurrentResult?.text = formatValue("0", true).cleanUpZeroValue()
        } else {
            if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF (operation->POWER->ELSE)")

            if (currentOperator == Calculator.Operator.PERCENT) {
                if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF (operation->POWER->ELSE) IF START")

                tvPreviousResult?.text =
                    buildString {
                        append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
                        append("% =")
                    }
                tvCurrentResult?.text =
                    formatValue(result.toString(), true).cleanUpZeroValue()

                tvHistoryResult?.text = buildString {
                    append("\n")
                    append(history)
                }
                history =
                    history + "\n" + (tvPreviousResult?.text.toString() + " " + tvCurrentResult?.text.toString()).trim(
                        '\n'
                    )

                if (tvHistoryResult?.text.toString().isNotBlank())
                    tvDivider?.visibility = View.VISIBLE
            } else {
                tvPreviousResult?.text = buildString {
                    if (DEBUG) Log.d(
                        "SOJO Debug:",
                        "displayResult -> IF (operation->POWER->else) IF START -> ELSE" + formatValue(
                            firstDisplayValue,
                            true
                        ).cleanUpZeroValue()
                    )
                    append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
                    append(" ")
                    append(Calculator.getOperatorSign(operation))
                }
                tvCurrentResult?.text = formatValue("0", true).cleanUpZeroValue()

                if (previousOperator != Calculator.Operator.NONE) {
                    val thisHistory: String =
                        formatValue(firstValue.toString(), true) + " " + Calculator.getOperatorSign(
                            previousOperator
                        ) + " " + formatValue(secondValue.toString(), true) + " = " + formatValue(
                            firstDisplayValue,
                            true
                        )

                    history = history + "\n" + thisHistory.trim('\n')

                    if (tvHistoryResult?.text.toString().isNotBlank())
                        tvDivider?.visibility = View.VISIBLE
                }
            }
        }
    } else if (hasResult) {
        if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START -> ELSE IF")

        tvPreviousResult?.text = buildString {
            append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
            append(" ")
            append(Calculator.getOperatorSign(operation))
            append(" ")
            append(formatValue(secondDisplayValue, true).cleanUpZeroValue())
        }
        tvCurrentResult?.text = formatValue(result.toString(), true).cleanUpZeroValue()

        tvHistoryResult?.text = buildString {
            append("\n")
            append(history)
        }
        history =
            history + "\n" + (tvPreviousResult?.text.toString() + " " + tvCurrentResult?.text.toString()).trim(
                '\n'
            )

        if (tvHistoryResult?.text.toString().isNotBlank())
            tvDivider?.visibility = View.VISIBLE
    } else {
        if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START -> ELSE")

        if (operation == Calculator.Operator.NONE) {
            if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START -> ELSE (operation->NONE")

            if (extraSignLeft == Calculator.getOperatorSign(Calculator.Operator.PI)) {
                if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START -> ELSE (ExtraSignLeft)")

                tvPreviousResult?.text = buildString {
                    append(Calculator.getOperatorSign(Calculator.Operator.PI))
                    append(" = ")
                }
            } else {
                if (DEBUG) Log.d(
                    "SOJO Debug:",
                    "displayResult -> IF START -> ELSE (operation->NONE->ExtraSignLeft->ELSE)"
                )
                tvPreviousResult?.text = buildString {
                    append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
                    append(extraSignLeft)
                    append(" =")
                }
            }
            tvCurrentResult?.text =
                formatValue(result.toString(), true).cleanUpZeroValue()
        } else {
            if (DEBUG) Log.d("SOJO Debug:", "displayResult -> IF START -> ELSE (operation->NONE->ELSE")

            tvPreviousResult?.text = buildString {
                append(formatValue(firstDisplayValue, true).cleanUpZeroValue())
                append(extraSignLeft)
                append(" ")
                append(Calculator.getOperatorSign(operation))
                append(" ")
                append(formatValue(secondDisplayValue, true).cleanUpZeroValue())
                append(extraSignRight)
                append(" =")
            }
            tvCurrentResult?.text =
                formatValue(result.toString(), true).cleanUpZeroValue()
        }

        if (tvPreviousResult?.text.toString().trim() != Calculator.getOperatorSign(Calculator.Operator.PI) + " =") {
            tvHistoryResult?.text = buildString {
                append("\n")
                append(history)
            }.trim('\n')
            history = history + "\n" + (tvPreviousResult?.text.toString() + " " + tvCurrentResult?.text.toString()).trim('\n')

            if (tvHistoryResult?.text.toString().isNotBlank())
                tvDivider?.visibility = View.VISIBLE
        }
    }

    if (tvHistoryResult?.text.toString().isNotBlank()) {
        tvDivider?.visibility = View.VISIBLE
        tvHistoryResult?.visibility = View.GONE
        tvHistoryResult?.visibility = View.VISIBLE
    }

    //adjustLabelFont(tvPreviousResult)
    //adjustLabelFont(tvCurrentResult)
}

// ========= RESET VALUES =========
private fun MainActivity.resetValues() {
    firstValue = 0.0
    secondValue = 0.0
    currentOperator = Calculator.Operator.NONE
    previousOperator = Calculator.Operator.NONE
}