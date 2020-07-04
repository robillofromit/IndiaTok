package com.bharatalk.app.main.util

import java.math.RoundingMode
import java.text.DecimalFormat

object NumberUtil {

    private const val BILLION = 1000000000f
    private const val MILLION = 1000000f
    private const val THOUSAND = 1000f

    fun format(input: Int): String {
        return format(input.toLong())
    }

    fun format(input: Long): String {
        val df = DecimalFormat("#.#").apply {
            roundingMode = RoundingMode.CEILING
        }
        val billion = input.toFloat() / BILLION
        if (billion > 1) {
            return "${df.format(billion)}B"
        }
        val million = input.toFloat() / MILLION
        if (million > 1) {
            return "${df.format(million)}M"
        }
        val thousand = input.toFloat() / THOUSAND
        if (thousand > 1) {
            return "${df.format(thousand)}K"
        }
        return "$input"
    }

}