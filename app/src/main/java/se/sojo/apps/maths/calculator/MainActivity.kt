package se.sojo.apps.maths.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


class MainActivity : ComponentActivity() {

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

    var tvCurrentResult: TextView? = null
    var tvPreviousResult: TextView? = null
    var tvMemoryStatus: TextView? = null

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

    companion object {
        // Max length of the display numbers
        const val MAX_LENGTH: Int = 17
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //actionBar?.hide()
        //WindowCompat.enableEdgeToEdge(window)
        //requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //hideStatusBar()
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        hideSystemUI()
        setContentView(R.layout.activity_main)

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

        tvCurrentResult = findViewById(R.id.tv_current_result)
        tvPreviousResult = findViewById(R.id.tv_previous_result)
        tvMemoryStatus = findViewById(R.id.tv_memory_status)

        tvCurrentResult?.text = "0"
        tvPreviousResult?.text = ""
        tvMemoryStatus?.visibility = View.GONE

        /******************************************************
         * Number Buttons onClick
         */
        btnOne!!.setOnClickListener { numberButtonAction(btnOne!!) }
        //processor = tvProcessor.getText().toString()
        //tvProcessor.setText(processor + "1")

        btnTwo!!.setOnClickListener { numberButtonAction(btnTwo!!) }
        btnThree!!.setOnClickListener { numberButtonAction(btnThree!!) }
        btnFour!!.setOnClickListener { numberButtonAction(btnFour!!) }
        btnFive!!.setOnClickListener { numberButtonAction(btnFive!!) }
        btnSix!!.setOnClickListener { numberButtonAction(btnSix!!) }
        btnSeven!!.setOnClickListener { numberButtonAction(btnSeven!!) }
        btnEight!!.setOnClickListener { numberButtonAction(btnEight!!) }
        btnNine!!.setOnClickListener { numberButtonAction(btnNine!!) }
        btnZero!!.setOnClickListener { numberButtonAction(btnZero!!) }
    }

    private fun numberButtonAction(btn: Button) {
        //Toast.makeText(this, "test", Toast.LENGTH_LONG).show()

        // Reset the screen if a result has been calculated
        if (hasResult || hasMemoryRecall) {
            tvCurrentResult?.text = "0"
            hasResult = false
            hasMemoryRecall = false

            if (currentOperator == Calculator.Operator.NONE && previousOperator == Calculator.Operator.NONE) {
                tvPreviousResult?.text = ""
            } else {
                // TODO: Calculator.FormatValue(firstValue) & " " & Calculator.getOperatorSign(currentOperation)
                tvPreviousResult?.text = Calculator.formatValue(("$firstValue +"))
            }
        }

        // Check if the current number is not longer than the MAX_LENGTH
        tvCurrentResult?.text?.length?.let {
            if (it >= MAX_LENGTH) {
                return
            }
        }

        // Check if a period exist and if not check if its a empty string and if so add a zero before the period
        if (btn.text == Calculator.DOT_SIGN) {
            if (tvCurrentResult?.text?.contains(Calculator.DOT_SIGN) == true) {
                return
            }

            if (tvCurrentResult?.text == "") {
                tvCurrentResult!!.text = "0"
            }
        }

        // Add key to the value
        tvCurrentResult?.append(btn.text)

        // Format the current result
        tvCurrentResult?.text = Calculator.formatValue(tvCurrentResult!!.text.toString())

        // Adjust the font and decrease it if needed
        // TODO: AdjustLabelFont(lblCurrentResult)

        // Set the status of Clear Entry to false
        clearEntry = false
    }

    private fun hideStatusBar() {
        WindowCompat.getInsetsController(window,window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.statusBars())
        }
    }

    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}