package com.bharatalk.app.main.view.coming_soon

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bharatalk.app.BuildConfig
import com.bharatalk.app.R
import com.bharatalk.app.main.view.base.RoundedBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.coming_soon_sheet.*


class ComingSoonBottomSheet: RoundedBottomSheetDialogFragment() {

    companion object {
        fun newInstance() = ComingSoonBottomSheet()
    }

    override fun getLayoutResId(): Int {
        return R.layout.coming_soon_sheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        clipParentOutline()
        setClickListeners()
    }

    private fun clipParentOutline() {
        parentView.clipToOutline = true
    }

    private fun setClickListeners() {
        rateUsButton.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=" + context!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)
                    )
                )
            }
        }
        shareAppButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            dialog.window?.let {
                it.setGravity(Gravity.CENTER)
                it.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                it.setBackgroundDrawableResource(android.R.color.transparent)
            }

            view?.let { view ->
                view.post {
                    val parent = view.parent as View
                    val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                    val behavior = params.behavior
                    val bottomSheetBehavior = behavior as BottomSheetBehavior<*>
                    bottomSheetBehavior.peekHeight = view.measuredHeight + 100
                }
            }
        }
    }
}