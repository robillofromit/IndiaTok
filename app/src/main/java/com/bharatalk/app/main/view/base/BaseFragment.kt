package com.bharatalk.app.main.view.base

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.bharatalk.app.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

}
