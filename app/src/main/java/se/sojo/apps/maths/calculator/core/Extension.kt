package se.sojo.apps.maths.calculator.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import se.sojo.apps.maths.calculator.core.Calculator.Companion.DECIMAL_SEPARATOR
import se.sojo.apps.maths.calculator.core.Calculator.Companion.MAX_LENGTH
import se.sojo.apps.maths.calculator.core.Calculator.Companion.THOUSAND_SEPARATOR
import java.util.Locale

fun CharSequence?.tryParse(): Double {
    return try {
        this.toString().toDouble()
    } catch (_: NumberFormatException) {
        0.0
    }
}

// Replace all none minus characters with proper one
fun String.cleanUpMinusSign(): String {
    return this.replace("-", "-").replace("−", "-").replace("–", "-").replace("—", "-")
}

fun String.removeThousandSeparator(): String {
    return this.replace(THOUSAND_SEPARATOR, "")
}

fun String.cleanUpDecimalSeparator(): String {
    return this.replace(DECIMAL_SEPARATOR, ".")
}

fun String.cleanUpZeroValue(): String {
    return if (this == ".0")
            "0"
        else if (this.substring(0, 1) == ".")
            "0" + DECIMAL_SEPARATOR + this.substring(1)
        else
            this
}

// ========= FONT AUTO-RESIZE =========
fun adjustLabelFont(targetLabel: TextView?, reset: Boolean = false) {
    var baseFontSize: Float = Calculator.BASE_FONT_SIZE * 2 // Default for CurrentResultLabel
    val minFontSize: Float = Calculator.BASE_FONT_SIZE / 2 // Smallest allowed

    val targetId = targetLabel?.resources?.getResourceEntryName(targetLabel.id)

    if (targetId.equals("tv_previous_result")) {
        baseFontSize = Calculator.BASE_FONT_SIZE
    }

    if (reset || targetLabel?.text.isNullOrEmpty()) {
        targetLabel?.setTextSize(TypedValue.COMPLEX_UNIT_SP, baseFontSize)
    } else {
        val currentLength: Int = targetLabel.text.toString().length
        var newFontSize = baseFontSize

        if (currentLength > MAX_LENGTH) {
            newFontSize = baseFontSize - (currentLength - MAX_LENGTH) * 2
        }

        if (newFontSize < minFontSize) {
            newFontSize = minFontSize
        } else if (newFontSize > baseFontSize) {
            newFontSize = baseFontSize
        }

        targetLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, newFontSize)
    }
}

fun performHapticFeedback(btn: Button?, motionEvent: MotionEvent?): Boolean {
    when (motionEvent?.action) {
        MotionEvent.ACTION_DOWN -> {
            btn?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            btn!!.alpha = 0.75f
            return true
        }

        MotionEvent.ACTION_UP -> {
            btn!!.alpha = 1.0f
            return true
        }
    }
    return false
}

@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
@RequiresApi(Build.VERSION_CODES.N)
fun getCurrentLocale(c: Context): Locale? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        c.resources.configuration.locales.get(0)
    } else {
        c.resources.configuration.locale
    }
}