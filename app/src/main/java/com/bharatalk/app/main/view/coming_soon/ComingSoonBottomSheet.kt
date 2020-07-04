package com.bharatalk.app.main.view.coming_soon

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
        dismissButton.setOnClickListener {
            dismiss()
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