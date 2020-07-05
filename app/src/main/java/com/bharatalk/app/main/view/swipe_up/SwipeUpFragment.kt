package com.bharatalk.app.main.view.swipe_up

import android.os.Bundle
import android.view.View

import com.bharatalk.app.R
import com.bharatalk.app.main.view.base.BaseFragment

class SwipeUpFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            SwipeUpFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_swipe_up
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {

    }
}
