package se.sojo.apps.maths.calculator.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.Button
import se.sojo.apps.maths.calculator.core.Calculator.Companion.DECIMAL_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.MAX_LENGTH
import se.sojo.apps.maths.calculator.MainActivity
import se.sojo.apps.maths.calculator.R
import se.sojo.apps.maths.calculator.core.Calculator
import se.sojo.apps.maths.calculator.core.adjustLabelFont
import se.sojo.apps.maths.calculator.core.performHapticFeedback

// Number buttons
@SuppressLint("ClickableViewAccessibility")
fun MainActivity.setNumberButtons() {
    btnOne = findViewById(R.id.btn_one)
    btnTwo = findViewById(R.id.btn_two)
    btnThree = findViewById(R.id.btn_three)
    btnFour = findViewById(R.id.btn_four)
    btnFive = findViewById(R.id.btn_five)
    btnSix = findViewById(R.id.btn_six)
    btnSeven = findViewById(R.id.btn_seven)
    btnEight = findViewById(R.id.btn_eight)
    btnNine = findViewById(R.id.btn_nine)
    btnZero = findViewById(R.id.btn_zero)
    btnDot = findViewById(R.id.btn_dot)

    /******************************************************
     * Number Buttons onClick
     */
    btnOne?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnOne!!, motionEvent) }
    btnTwo?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnTwo!!, motionEvent) }
    btnThree?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnThree!!, motionEvent) }
    btnFour?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnFour!!, motionEvent) }
    btnFive?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnFive!!, motionEvent) }
    btnSix?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnSix!!, motionEvent) }
    btnSeven?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnSeven!!, motionEvent) }
    btnEight?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnEight!!, motionEvent) }
    btnNine?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnNine!!, motionEvent) }
    btnZero?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnZero!!, motionEvent) }
    btnDot?.setOnTouchListener { _, motionEvent -> numberButtonAction(btnDot!!, motionEvent) }
}

// ========= NUMBER BUTTON ACTIONS =========
private fun MainActivity.numberButtonAction(btn: Button, motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Reset the screen if a result has been calculated
            if (hasResult || hasMemoryRecall) {
                tvCurrentResult?.text = "0"
                hasResult = false
                hasMemoryRecall = false

                if (currentOperator == Calculator.Operator.NONE && previousOperator == Calculator.Operator.NONE) {
                    tvPreviousResult?.text = ""
                } else {
                    tvPreviousResult?.text = buildString {
                        append(Calculator.formatValue(firstValue.toString()))
                        append(" ")
                        append(Calculator.getOperatorSign(currentOperator))
                    }
                }
            }

            // Check if the current number is not longer than the MAX_LENGTH
            tvCurrentResult?.text?.length?.let {
                if (it >= MAX_LENGTH) return performHapticFeedback(btn, motionEvent)
            }

            // Check if a period exist and if not check if its a empty string and if so add a zero before the period
            if (btn.text == DECIMAL_SEPARATOR) {
                if (tvCurrentResult?.text?.contains(DECIMAL_SEPARATOR) == true)
                    return performHapticFeedback(btn, motionEvent)

                if (tvCurrentResult?.text == "")
                    tvCurrentResult!!.text = "0"
            }

            // Add key to the value
            tvCurrentResult?.append(btn.text)

            // Format the current result
            tvCurrentResult?.text = Calculator.formatValue(tvCurrentResult!!.text.toString())

            // Adjust the font and decrease it if needed
            adjustLabelFont(tvCurrentResult)

            // Set the status of Clear Entry to false
            clearEntry = false
        }
    }

    return performHapticFeedback(btn, motionEvent)
}