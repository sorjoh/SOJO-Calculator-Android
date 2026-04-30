package se.sojo.apps.maths.calculator

class Calculator {
    companion object {
        const val DOT_SIGN: String = "."

        fun formatValue(input: String, forceFormat: Boolean = false): String {
            return if (input.length > 1 && input.take(1) == "0" && !input.contains(DOT_SIGN)) {
                input.substring(1)
            } else if (input.contains(DOT_SIGN)) {
                if (forceFormat) {
                    // TODO: Strings.Format(Conversions.ToDouble(input), "##,##0.#####################");
                    input
                } else {
                    input
                }
            } else {
                // TODO: Strings.Format(Conversions.ToDouble(input), "##,##0.#####################");
                input
            }
        }
    }

    enum class Operator(val symbol: String) {
        ADD("+"), //Resources.getSystem().getString(R.string.add_sign)),
        SUBTRACT("-"),
        MULTIPLY("x"),
        DIVIDE("÷"),
        MODULO("≡"),
        PERCENT("%"),
        SQUARE("x²"),
        CUBE("x³"),
        PI("π"),
        POWER("^"),
        SQUARE_ROOT("√"),
        CUBE_ROOT("∛"),
        RECIPROCAL("1/x"),
        RND("rnd"),
        EQUALS("="),

        //DELETE("DEL"),
        //PLUS_MINUS("+/-")
        NONE("")
    }


}
