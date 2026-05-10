package se.sojo.apps.maths.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import se.sojo.apps.maths.calculator.core.Calculator
import se.sojo.apps.maths.calculator.core.Calculator.Companion.DECIMAL_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.THOUSAND_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.numberFormat
import se.sojo.apps.maths.calculator.core.adjustLabelFont
import se.sojo.apps.maths.calculator.core.getCurrentLocale
import se.sojo.apps.maths.calculator.ui.setMemoryButtons
import se.sojo.apps.maths.calculator.ui.setNumberButtons
import se.sojo.apps.maths.calculator.ui.setOperationButtons
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
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
    var tvPreviousResult: TextView? = null
    var tvMemoryStatus: TextView? = null

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
    var hasMemoryRecall: Boolean = false

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
        val decimalFormat: DecimalFormat = DecimalFormat("#,###.##")
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
        tvMemoryStatus = findViewById(R.id.tv_memory_status)

        // Number buttons
        setNumberButtons()
        // Operation buttons
        setOperationButtons()
        // Memory buttons
        setMemoryButtons()

        // Initial values
        tvCurrentResult?.text = "0"
        tvPreviousResult?.text = ""
        tvMemoryStatus?.visibility = View.GONE

        // Replace decimal separator with current from the system
        btnDot?.text = DECIMAL_SEPARATOR

        adjustLabelFont(tvPreviousResult)
        adjustLabelFont(tvCurrentResult)
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