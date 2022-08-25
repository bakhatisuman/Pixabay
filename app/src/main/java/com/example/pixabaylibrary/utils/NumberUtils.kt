package com.example.pixabaylibrary.utils

import kotlin.math.ln
import kotlin.math.pow

class NumberUtils {
    companion object{
         fun prettyNumberCount(number: Long): String {
            if (number < 1000) return "" + number
            val exp = (ln(number.toDouble()) / ln(1000.0)).toInt()

            return String.format(
                "%.1f%c", number / 1000.0.pow(exp.toDouble()), "KMBTPE"[exp - 1]
            )
        }
    }
}