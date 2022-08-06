package com.example.nulli.util

import android.content.Context
import android.util.TypedValue


class nulliUtil {
    fun dp2Px(context: Context, dp: Int): Float {
        val dpInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
        return dpInPixel
    }

    fun px2dp(context: Context, px: Float): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }
}