package com.returnz3ro.messystem.view.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityMainBinding
import com.returnz3ro.messystem.service.repository.UserRepository
import com.returnz3ro.messystem.view.adapter.MainListAdapter
import com.returnz3ro.messystem.viewmodel.LoginViewModel
import com.returnz3ro.messystem.viewmodel.MainViewModel

var animationPlaybackSpeed: Double = 0.8

class MainActivity: AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainListAdapter: MainListAdapter
    private var mainViewModel: MainViewModel?=null
    private var backPressCloseHandler: BackPressCloseHandler?= null

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this, MainViewModel.Factory(this)).get(MainViewModel::class.java)

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler= BackPressCloseHandler(this)

        // Appbar behavior init
        (activityMainBinding.appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior = ToolbarBehavior()

        // RecyclerView Init
        mainListAdapter = MainListAdapter(this)
        activityMainBinding.recyclerView.adapter = mainListAdapter
        activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        activityMainBinding.recyclerView.setHasFixedSize(true)
        updateRecyclerViewAnimDuration()

        //qr icon click
        activityMainBinding.qrIcon.setOnClickListener {
            startActivity(Intent(this, RecogQRActivity::class.java))
            finish()
        }

        //TODO:drawer icon click

        //TODO:Open Nav Drawer when opening app for the first time
    }

    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = activityMainBinding.recyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    //Update RecyclerView Item Animation Durations
    fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        mainListAdapter.getScaleDownAnimator(isScaledDown)

    //뒤로 가기 버튼 2번 클릭시 종료
    override fun onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler?.onBackPressed()
    }
}