package com.bharatalk.app.loading

import android.os.Bundle
import android.os.Handler
import com.bharatalk.app.R
import com.bharatalk.app.base.BaseActivity
import com.bharatalk.app.toks.ToksActivity
import java.lang.Exception

class LoadingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        setup()
    }

    override fun setup() {
        setStatusBarColor(R.color.white)
        launchToksAfterDelay()
    }

    private fun launchToksAfterDelay() {
        Handler().postDelayed({
            try {
                startActivity(ToksActivity.newIntent(this))
            }
            catch (ignored: Exception) {}
        }, 2000)
    }
}
