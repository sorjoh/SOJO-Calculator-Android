package se.sojo.apps.maths.calculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import se.sojo.apps.maths.calculator.core.Calculator
import se.sojo.apps.maths.calculator.core.Calculator.Companion.DECIMAL_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.THOUSAND_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.numberFormat
import se.sojo.apps.maths.calculator.core.getCurrentLocale
import se.sojo.apps.maths.calculator.core.getScreenHeight
import se.sojo.apps.maths.calculator.core.getScreenWidth
import se.sojo.apps.maths.calculator.core.margin
import se.sojo.apps.maths.calculator.core.physicalScreenRectDp
import se.sojo.apps.maths.calculator.ui.setMemoryButtons
import se.sojo.apps.maths.calculator.ui.setNumberButtons
import se.sojo.apps.maths.calculator.ui.setOperationButtons
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale


class MainActivity : ComponentActivity() {
    companion object {
        const val DEBUG = false
    }

    // Number buttons
    var btnOne: Button? = null
    var btnTwo: Button? = null
    var btnThree: Button? = null
    var btnFour: Button? = null
    var btnFive: Button? = null
    var btnSix: Button? = null
    var btnSeven: Button? = null
    var btnEight: Button? = null
    var btnNine: Button? = null
    var btnZero: Button? = null
    var btnDot: Button? = null

    // Display
    var tvCurrentResult: TextView? = null
    var tvHistoryResult: TextView? = null
    var tvPreviousResult: TextView? = null
    var tvMemoryStatus: TextView? = null
    var tvDivider: View? = null

    var layoutResult: LinearLayout? = null

    // Operation buttons
    var btnAdd: Button? = null
    var btnSubtract: Button? = null
    var btnMultiply: Button? = null
    var btnDivide: Button? = null
    var btnModulo: Button? = null
    var btnPercent: Button? = null
    var btnSquare: Button? = null
    var btnCube: Button? = null
    var btnPower: Button? = null
    var btnSquareRoot: Button? = null
    var btnCubeRoot: Button? = null
    var btnReciprocal: Button? = null

    // PI, RND, Toggle (+/-) Buttons
    var btnPi: Button? = null
    var btnRnd: Button? = null
    var btnToggle: Button? = null

    // DELETE, EQUAL, CLEAR Buttons
    var btnDelete: Button? = null
    var btnEqual: Button? = null
    var btnClear: Button? = null

    // Memory Button
    var btnMemoryClear: Button? = null
    var btnMemoryRecall: Button? = null
    var btnMemoryStore: Button? = null
    var btnMemoryAdd: Button? = null
    var btnMemorySubtract: Button? = null

    // Status indicator if equal button or another calculation has presented a total result
    var hasResult: Boolean = false

    // Status indicator if value is stored in memory
    var hasMemoryStatus: Boolean = false
    var hasMemoryRecall: Boolean = false

    // History variable
    var history: String = ""

    // Variables holding current and previous operations
    var currentOperator: Calculator.Operator = Calculator.Operator.NONE
    var previousOperator: Calculator.Operator = Calculator.Operator.NONE

    // Variables holding the first and second values
    var firstValue: Double = 0.0
    var secondValue: Double = 0.0

    // Variable holding the memory value and status
    var memoryValue: Double = 0.0

    // Variable if Clear Entry (CE) has been pressed
    var clearEntry: Boolean = false

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide top- and bottom bar
        hideSystemUI()

        setContentView(R.layout.activity_main)

        // Get decimal separator from the system
        DECIMAL_SEPARATOR = DecimalFormatSymbols.getInstance(getCurrentLocale(this)).decimalSeparator.toString()

        // Get thousand separator from the system
        val decimalFormat = DecimalFormat("#,###.##")
        decimalFormat.isDecimalSeparatorAlwaysShown = true
        THOUSAND_SEPARATOR = decimalFormat.decimalFormatSymbols.groupingSeparator.toString()

        // Get current language tags (eg sv-SE, en-US etc) and set min and max decimals and current languages number format
        val langTags = getCurrentLocale(this)?.toLanguageTag().toString().split('-')
        numberFormat = NumberFormat.getNumberInstance(Locale(langTags[0], langTags[1]))
        numberFormat.maximumFractionDigits = 11
        numberFormat.minimumFractionDigits = 0

        // Display
        tvCurrentResult = findViewById(R.id.tv_current_result)
        tvPreviousResult = findViewById(R.id.tv_previous_result)
        tvHistoryResult = findViewById(R.id.tv_history_result)
        tvMemoryStatus = findViewById(R.id.tv_memory_status)

        layoutResult = findViewById(R.id.Result)

        tvDivider = findViewById(R.id.tv_divider)

        // Number buttons
        setNumberButtons()
        // Operation buttons
        setOperationButtons()
        // Memory buttons
        setMemoryButtons()

        // Initial values
        tvCurrentResult?.text = "0"
        tvPreviousResult?.text = ""
        tvHistoryResult?.text = ""
        tvMemoryStatus?.visibility = View.GONE

        tvPreviousResult?.movementMethod = ScrollingMovementMethod()
        tvHistoryResult?.movementMethod = ScrollingMovementMethod()
        tvCurrentResult?.movementMethod = ScrollingMovementMethod()

        tvDivider?.visibility = View.GONE

        // Get current orientation and screen size in both px and dp
        val orientation = this.resources.configuration.orientation

        val screenWidthPx = getScreenWidth()
        val screenHeightPx = getScreenHeight()

        val screenWidthDp = physicalScreenRectDp.width()
        val screenHeightDp = physicalScreenRectDp.height()

        if (DEBUG) {
            Log.d("SOJO Debug:", "orientation: " + (if (orientation == 1) "Portrait" else "Landscape") + "\n" +
                "\tDP = " + screenWidthDp.toString() + " x " + screenHeightDp.toString() +
                "\t\tPX = " + screenWidthPx.toString() + " x " + screenHeightPx.toString()
            )
        }

        // Default values for screen layout
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // PORTRAIT
            when (screenHeightDp.toInt()) {
                // Lenovo Tab M10 Plus - TB-X606F (800x1280 / 1200x1848)
                in 1280 .. 1440 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Lenovo Tab M10 Plus (Portrait, 1280 -> 1440) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 28f,
                        previousResultTextSize = 40f,
                        currentResultTextSize = 72f,
                        previousResultBottomMargin = 20f,
                        dividerTopMargin = 20f,
                        dividerBottomMargin = -10f,
                        resultWeight = 5f
                    )
                }

                // Lenovo Tab M7 - TB-7306F (600x1024 / 600x1008)
                in 1024 .. 1279 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Lenovo Tab M7 (Portrait, 1024 -> 1279) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 24f,
                        previousResultTextSize = 32f,
                        currentResultTextSize = 64f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 10f,
                        dividerBottomMargin = 0f,
                        resultWeight = 3f
                    )
                }

                // Google Pixel 6a (411x914 / 1080x2400)
                // Asus Nexus 7 (600x961 / 800x1205)
                in 900 .. 1023 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Google Pixel 6a (Portrait, 900 -> 1023) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 24f,
                        previousResultTextSize = 32f,
                        currentResultTextSize = 64f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 10f,
                        dividerBottomMargin = 0f, //10f
                        resultWeight = 3f
                    )
                }

                // Samsung Galaxy A14 - SM-A145R (384x856 / 1080x2408)
                // Samsung Galaxy A16 5G - SM-A166B (384x832 / 1080x2340)
                // Google Pixel 3a (352x724 / 1080x2073)
                // Samsung Galaxy J3 (2017) - SM-J330F (360x640 / 720x1280)
                //in 640 .. 899 -> {
                else -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Google Pixel 3a (Portrait, 0 -> 899) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 16f,
                        previousResultTextSize = 28f, //24f 20f
                        currentResultTextSize = 48f, //48f 42f 40f 36f
                        previousResultBottomMargin = 5f,
                        dividerTopMargin = 10f,
                        dividerBottomMargin = 10f,
                        resultWeight = 3f,
                    )
                }

                /*in 480 .. 639 -> {
                    if (DEBUG) Log.d("SOJO Debug:", "???? (Portrait, 480 -> 639) -> DP = " + screenWidthDp.toString() + " x " + screenHeightDp.toString() + "\t\tPX = "+ screenWidthPx.toString() + " x " + screenHeightPx.toString())
                }

                else -> {
                    if (DEBUG) Log.d("SOJO Debug:", "???? (Portrait, 0 -> 479) -> DP = " + screenWidthDp.toString() + " x " + screenHeightDp.toString() + "\t\tPX = "+ screenWidthPx.toString() + " x " + screenHeightPx.toString())
                }*/
            }
        } else {
            // LANDSCAPE
            when (screenHeightDp.toInt()) {
                // Lenovo Tab M10 Plus, TB-X606F (1280x800 / 1920x1128)
                in 800 .. 1440 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Lenovo Tab M10 Plus (Landscape, 800 -> 1440) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 24f,
                        previousResultTextSize = 36f,
                        currentResultTextSize = 64f,
                        previousResultBottomMargin = 20f,
                        dividerTopMargin = 10f,
                        dividerBottomMargin = 20f,
                        resultWeight = 3f
                    )
                }

                // Lenovo Tab M7 - TB-7306F (1024x600 / 1024x584)
                // Asus Nexus 7 (961x600 / 1280x736)
                in 600 .. 799 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Lenovo Tab M7 (Landscape, 600 -> 799) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 18f,
                        previousResultTextSize = 24f,
                        currentResultTextSize = 48f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 10f,
                        dividerBottomMargin = 20f,
                        resultWeight = 2f,
                    )
                }

                // Google Pixel 6a (915x411 / 2400x1080)
                in 400 .. 599 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Google Pixel 6a (Landscape, 400 -> 599) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 14f,
                        previousResultTextSize = 15f,
                        currentResultTextSize = 36f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 5f,
                        dividerBottomMargin = 8f,
                        resultWeight = 2f
                    )
                }

                // Samsung Galaxy J3 (2017) - SM-J330F (640x360 / 1280x720)
                // Samsung Galaxy A14 - SM-A145R (856x384 / 2408x1080)
                // Samsung Galaxy A16 5G - SM-A166B (832x384 / 2340x1080)
                in 360 .. 399 -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Samsung Galaxy J3 (2017) (Landscape, 360 -> 399) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 12f,
                        previousResultTextSize = 14f,
                        currentResultTextSize = 28f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 5f,
                        dividerBottomMargin = 8f,
                        resultWeight = 2f,
                    )
                }

                // Google Pixel 3a (724x352 / 2073x1080)
                else -> {
                    if (DEBUG) Log.d("SOJO Debug:",
                        "Google Pixel 3a (Landscape, 0 -> 359) -> DP = $screenWidthDp x $screenHeightDp\t\tPX = $screenWidthPx x $screenHeightPx"
                    )

                    setScreenValues(
                        historyResultTextSize = 12f,
                        previousResultTextSize = 14f,
                        currentResultTextSize = 28f,
                        previousResultBottomMargin = 0f,
                        dividerTopMargin = 5f,
                        dividerBottomMargin = 5f,
                        resultWeight = 2f
                    )
                }
            }
        }

        //tvPreviousResult?.setTypeface(null, Typeface.BOLD)
        tvHistoryResult?.setTypeface(null, Typeface.ITALIC)

        tvHistoryResult?.setOnLongClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle(getString(R.string.clear_history))
            builder.setMessage(getString(R.string.do_you_really_want_to_delete_all_history))

            builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                tvHistoryResult?.text = ""
                history = ""
                tvDivider?.visibility = View.GONE
                dialog.dismiss()
            }

            builder.setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }

            val alert = builder.create()
            alert.show()

            return@setOnLongClickListener true
        }

        // Replace decimal separator with current from the system
        btnDot?.text = DECIMAL_SEPARATOR

        // Load saved values
        if(savedInstanceState != null) {
            tvPreviousResult?.text = savedInstanceState.getString("PreviousResult")
            tvCurrentResult?.text = savedInstanceState.getString("CurrentResult")
            tvHistoryResult?.text = savedInstanceState.getString("HistoryResult")

            hasResult = savedInstanceState.getBoolean("HasResult")
            hasMemoryStatus = savedInstanceState.getBoolean("HasMemoryStatus")
            hasMemoryRecall = savedInstanceState.getBoolean("HasMemoryRecall")
            clearEntry = savedInstanceState.getBoolean("ClearEntry")

            firstValue = savedInstanceState.getDouble("FirstValue")
            secondValue = savedInstanceState.getDouble("SecondValue")
            memoryValue = savedInstanceState.getDouble("MemoryValue")
            history = savedInstanceState.getString("History").toString()

            if (hasMemoryStatus) {
                tvMemoryStatus?.visibility = View.VISIBLE
            } else {
                tvMemoryStatus?.visibility = View.GONE
            }

            if (tvHistoryResult?.text.toString().isNotBlank()) {
                tvDivider?.visibility = View.VISIBLE
                tvHistoryResult?.visibility = View.VISIBLE
            } else {
                tvDivider?.visibility = View.GONE
                tvHistoryResult?.visibility = View.GONE
            }

            previousOperator = when (savedInstanceState.getString("PreviousOperator")) {
                "ADD" -> Calculator.Operator.ADD
                "SUBTRACT" -> Calculator.Operator.SUBTRACT
                "MULTIPLY" -> Calculator.Operator.MULTIPLY
                "DIVIDE" -> Calculator.Operator.DIVIDE
                "MODULO" -> Calculator.Operator.MODULO
                "PERCENT" -> Calculator.Operator.PERCENT
                "SQUARE" -> Calculator.Operator.SQUARE
                "CUBE" -> Calculator.Operator.CUBE
                "PI" -> Calculator.Operator.PI
                "POWER" -> Calculator.Operator.POWER
                "SQUARE_ROOT" -> Calculator.Operator.SQUARE_ROOT
                "CUBE_ROOT" -> Calculator.Operator.CUBE_ROOT
                "RECIPROCAL" -> Calculator.Operator.RECIPROCAL
                "RND" -> Calculator.Operator.RND
                "EQUALS" -> Calculator.Operator.EQUALS
                else -> Calculator.Operator.NONE
            }

            currentOperator = when (savedInstanceState.getString("CurrentOperator")) {
                "ADD" -> Calculator.Operator.ADD
                "SUBTRACT" -> Calculator.Operator.SUBTRACT
                "MULTIPLY" -> Calculator.Operator.MULTIPLY
                "DIVIDE" -> Calculator.Operator.DIVIDE
                "MODULO" -> Calculator.Operator.MODULO
                "PERCENT" -> Calculator.Operator.PERCENT
                "SQUARE" -> Calculator.Operator.SQUARE
                "CUBE" -> Calculator.Operator.CUBE
                "PI" -> Calculator.Operator.PI
                "POWER" -> Calculator.Operator.POWER
                "SQUARE_ROOT" -> Calculator.Operator.SQUARE_ROOT
                "CUBE_ROOT" -> Calculator.Operator.CUBE_ROOT
                "RECIPROCAL" -> Calculator.Operator.RECIPROCAL
                "RND" -> Calculator.Operator.RND
                "EQUALS" -> Calculator.Operator.EQUALS
                else -> Calculator.Operator.NONE
            }
        }

        if (tvCurrentResult?.text.toString().isBlank())
            tvCurrentResult?.text = "0"

        //adjustLabelFont(tvPreviousResult)
        //adjustLabelFont(tvCurrentResult)
    }

    // Set the size of components matching the screen size
    private fun setScreenValues(
        historyResultTextSize: Float = 24f,
        previousResultTextSize: Float = 32f,
        currentResultTextSize: Float = 64f,
        previousResultBottomMargin: Float = 0f,
        dividerTopMargin: Float = 0f,
        dividerBottomMargin: Float = 0f,
        resultWeight: Float = 3f
    ) {
        tvHistoryResult?.setTextSize(TypedValue.COMPLEX_UNIT_SP, historyResultTextSize)
        tvPreviousResult?.setTextSize(TypedValue.COMPLEX_UNIT_SP, previousResultTextSize)
        tvCurrentResult?.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentResultTextSize)
        tvPreviousResult?.margin(bottom = previousResultBottomMargin)
        tvDivider?.margin(top = dividerTopMargin)
        tvDivider?.margin(bottom = dividerBottomMargin)

        val paramsResult =  layoutResult?.layoutParams as LinearLayout.LayoutParams
        paramsResult.weight = resultWeight
    }

    // Save values when rotating the screen
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("PreviousResult", tvPreviousResult?.text.toString())
        outState.putString("CurrentResult", tvCurrentResult?.text.toString())
        outState.putString("HistoryResult", tvHistoryResult?.text.toString())

        outState.putBoolean("HasResult", hasResult)
        outState.putBoolean("HasMemoryStatus", hasMemoryStatus)
        outState.putBoolean("HasMemoryRecall", hasMemoryRecall)
        outState.putBoolean("ClearEntry", clearEntry)

        outState.putDouble("FirstValue", firstValue)
        outState.putDouble("SecondValue", secondValue)
        outState.putDouble("MemoryValue", memoryValue)
        outState.putString("History", history)

        outState.putString("PreviousOperator", previousOperator.toString())
        outState.putString("CurrentOperator", currentOperator.toString())
    }


    // Hide top- and bottom bar
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val actionBar = getActionBar()
        actionBar?.hide()
    }
}