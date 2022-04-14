package com.returnz3ro.messystem.view.ui

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.returnz3ro.messystem.R

class ToolbarBehavior : CoordinatorLayout.Behavior<AppBarLayout>() {
    private lateinit var toolbar: View
    private lateinit var toolbarTitle: View
    private lateinit var drawerIcon: View
    private lateinit var qrIcon: View

    private var toolbarOriginalHeight: Float = -1f
    private var toolbarCollapsedHeight: Float = -1f
    private var viewsSet = false
    private var minScale = 0.6f

    private fun getViews(child: AppBarLayout) {
        if (viewsSet) return
        viewsSet = true

        toolbar = child.findViewById(R.id.appbar_container)
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title)
        drawerIcon = toolbar.findViewById(R.id.drawer_icon)
        qrIcon = toolbar.findViewById(R.id.qr_icon)


        toolbarOriginalHeight = toolbar.layoutParams.height.toFloat()
        toolbarCollapsedHeight = toolbarOriginalHeight * minScale
    }


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, directTargetChild: View,
                                     target: View, axes: Int, type: Int): Boolean {
        getViews(child)
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View,
                                dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                type: Int, consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        getViews(child)

        if (dyConsumed > 0) {

            // scroll up:
            if (toolbar.layoutParams.height > toolbarCollapsedHeight) {

                //--- shrink toolbar
                val height = toolbar.layoutParams.height - dyConsumed
                toolbar.layoutParams.height = if (height < toolbarCollapsedHeight) toolbarCollapsedHeight.toInt() else height
                toolbar.requestLayout()

                //--- translate up drawer icon
                var translate: Float = (toolbarOriginalHeight - toolbar.layoutParams.height) / (toolbarOriginalHeight - toolbarCollapsedHeight)
                translate *= toolbarOriginalHeight
                drawerIcon.translationY = -translate
                qrIcon.translationY = -translate

                //--- title
                val scale = toolbar.layoutParams.height / toolbarOriginalHeight
                toolbarTitle.scaleX = if (scale < minScale) minScale else scale
                toolbarTitle.scaleY = toolbarTitle.scaleX
            }
        } else if (dyUnconsumed < 0) {

            // scroll down
            if (toolbar.layoutParams.height < toolbarOriginalHeight) {

                //--- expand toolbar
                // subtract because dyUnconsumed is < 0
                val height = toolbar.layoutParams.height - dyUnconsumed
                toolbar.layoutParams.height = if (height > toolbarOriginalHeight) toolbarOriginalHeight.toInt() else height
                toolbar.requestLayout()

                //--- translate down  drawer icon
                var translate: Float = (toolbarOriginalHeight - toolbar.layoutParams.height) / (toolbarOriginalHeight - toolbarCollapsedHeight)
                translate *= toolbarOriginalHeight
                drawerIcon.translationY = -translate
                qrIcon.translationY = -translate

                //--- title
                val scale = toolbar.layoutParams.height / toolbarOriginalHeight
                toolbarTitle.scaleX = if (scale < minScale) minScale else scale
                toolbarTitle.scaleY = toolbarTitle.scaleX
            }
        }
    }
}