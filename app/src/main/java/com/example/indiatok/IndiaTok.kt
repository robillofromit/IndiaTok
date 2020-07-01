package com.example.indiatok

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class IndiaTok: Application() {

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