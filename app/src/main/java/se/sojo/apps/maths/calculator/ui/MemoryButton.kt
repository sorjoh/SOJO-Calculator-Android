package se.sojo.apps.maths.calculator.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import se.sojo.apps.maths.calculator.MainActivity
import se.sojo.apps.maths.calculator.R
import se.sojo.apps.maths.calculator.core.Calculator
import se.sojo.apps.maths.calculator.core.Calculator.Companion.DECIMAL_SEPARATOR
import se.sojo.apps.maths.calculator.core.adjustLabelFont
import se.sojo.apps.maths.calculator.core.cleanUpDecimalSeparator
import se.sojo.apps.maths.calculator.core.cleanUpMinusSign
import se.sojo.apps.maths.calculator.core.performHapticFeedback
import se.sojo.apps.maths.calculator.core.removeThousandSeparator

// Memory buttons
@SuppressLint("ClickableViewAccessibility")
fun MainActivity.setMemoryButtons() {
    btnMemoryClear = findViewById(R.id.btn_memory_clear)
    btnMemoryRecall = findViewById(R.id.btn_memory_recall)
    btnMemoryStore = findViewById(R.id.btn_memory_store)
    btnMemoryAdd = findViewById(R.id.btn_memory_add)
    btnMemorySubtract = findViewById(R.id.btn_memory_subtract)

    /******************************************************
     * Memory Buttons onClick
     */
    btnMemoryClear?.setOnTouchListener { _, motionEvent -> memoryButtonAction(btnMemoryClear!!, motionEvent) }
    btnMemoryRecall?.setOnTouchListener { _, motionEvent -> memoryButtonAction(btnMemoryRecall!!, motionEvent) }
    btnMemoryStore?.setOnTouchListener { _, motionEvent -> memoryButtonAction(btnMemoryStore!!, motionEvent) }
    btnMemoryAdd?.setOnTouchListener { _, motionEvent -> memoryButtonAction(btnMemoryAdd!!, motionEvent) }
    btnMemorySubtract?.setOnTouchListener { _, motionEvent -> memoryButtonAction(btnMemorySubtract!!, motionEvent) }
}

// ========= MEMORY BUTTON ACTIONS =========
private fun MainActivity.memoryButtonAction(btn: Button, motionEvent: MotionEvent): Boolean {
    when (motionEvent.action) {
        MotionEvent.ACTION_UP -> {
            // Get the current value
            var input: String = tvCurrentResult?.text.toString().removeThousandSeparator().cleanUpDecimalSeparator()
            input = input.replace(160.toChar().toString(), "").cleanUpMinusSign()

            val currentValue: String = input

            hasMemoryRecall = false

            when (btn.text) {
                // === MEMORY CLEAR ===
                "MC" -> {
                    // Clear the memory and hide the memory label
                    memoryValue = 0.0
                    hasMemoryStatus = false
                    tvMemoryStatus?.visibility = View.GONE
                }

                // === MEMORY RECALL ===
                "MR" -> {
                    hasMemoryRecall = true
                    
                    // Show the memory value
                    if (memoryValue == 0.0) {
                        tvCurrentResult?.text = "0"
                    } else {
                        tvCurrentResult?.text = Calculator.formatValue(memoryValue.toString(), true)
                        //if (currentOperator != Calculator.Operator.NONE || previousOperator != Calculator.Operator.NONE) {
                        //    tvPreviousResult?.text = ""
                        //    hasResult = true
                        //}
                    }

                    if (hasResult)
                        tvPreviousResult?.text = ""

                    // Check and add a zero if leading zero is missing when decimal is used
                    if (tvCurrentResult?.text?.substring(0, 1) == DECIMAL_SEPARATOR) {
                        tvCurrentResult?.text = buildString {
                            append("0")
                            append(tvCurrentResult?.text)
                        }
                    }
                    // Replace 0.0 and -0 with zero only
                    if (tvCurrentResult?.text.toString() == "0" + DECIMAL_SEPARATOR + "0" || tvCurrentResult?.text.toString() == "−0")
                        tvCurrentResult?.text = "0"

                    // Replace all none minus characters with proper one
                    tvCurrentResult?.text = tvCurrentResult?.text.toString().cleanUpMinusSign()

                    // Adjust the font size depending on how many numbers
                    adjustLabelFont(tvCurrentResult)
                }

                // === MEMORY STORE ===
                "MS" -> {
                    // Store the current value in memory and show the memory label
                    memoryValue = currentValue.toDouble()
                    hasMemoryStatus = true
                    tvMemoryStatus?.visibility = View.VISIBLE
                }

                // === MEMORY ADD ===
                "M+" -> {
                    // Add the current value to the memory and show the memory label
                    memoryValue += currentValue.toDouble()
                    hasMemoryStatus = true
                    tvMemoryStatus?.visibility = View.VISIBLE
                }

                // === MEMORY SUBTRACT
                "M-" -> {
                    // Subtract the current value to the memory and show the memory label
                    memoryValue -= currentValue.toDouble()
                    hasMemoryStatus = true
                    tvMemoryStatus?.visibility = View.VISIBLE
                }
            }
        }
    }

    return performHapticFeedback(btn, motionEvent)
}