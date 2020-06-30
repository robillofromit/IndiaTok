package com.example.indiatok.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.indiatok.R
import com.example.indiatok.base.BaseActivity
import com.example.indiatok.toks.ToksActivity
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
