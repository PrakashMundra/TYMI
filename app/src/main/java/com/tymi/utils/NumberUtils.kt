package com.tymi.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object NumberUtils {
    fun getFormattedNumber(value: String): String {
        if (!value.trim().isEmpty()) {
            val parsed = BigDecimal(value)
            val numberInstance = NumberFormat.getNumberInstance(Locale.ENGLISH)
            val decimalFormat = numberInstance as DecimalFormat
            decimalFormat.isDecimalSeparatorAlwaysShown = false
            val decimalFormatSymbols = decimalFormat.decimalFormatSymbols
            decimalFormat.decimalFormatSymbols = decimalFormatSymbols
            numberInstance.setMinimumFractionDigits(0)
            numberInstance.setMaximumFractionDigits(2)
            return numberInstance.format(parsed)
        }
        return ""
    }
}