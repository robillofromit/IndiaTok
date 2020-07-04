package com.bharatalk.app.main.view

import android.app.Application
import com.bharatalk.app.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class Bharatalk: Application() {

    override fun onCreate() {
        super.onCreate()

        initViewPump()
    }

    private fun initViewPump() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(calligraphyInterceptor())
                .build())
    }

    private fun calligraphyInterceptor(): CalligraphyInterceptor {
        return CalligraphyInterceptor(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }
}