package com.returnz3ro.messystem.view.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityMainBinding
import com.returnz3ro.messystem.utils.setScale
import com.returnz3ro.messystem.view.adapter.JoborderAdapter
import com.returnz3ro.messystem.viewmodel.MainViewModel
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var animationPlaybackSpeed: Double = 0.8

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JoborderAdapter
    lateinit var mainViewModel: MainViewModel
    private var backPressCloseHandler: BackPressCloseHandler?= null

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting viewmodel
        mainViewModel = ViewModelProvider(this, MainViewModel.Factory(this)).get(MainViewModel::class.java)

        // Appbar behavior init
        (binding.appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior = ToolbarBehavior()

        // RecyclerView Init / get Slitter, Joborder list
        getDataList()

        mainViewModel.getAllJoborders()
        mainViewModel.getSlitterList()

        //qr icon click
        binding.qrIcon.setOnClickListener {
            startActivity(Intent(this, RecogQRActivity::class.java))
            finish()
        }

        //drawer icon click
        binding.drawerIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        //logout click
//        binding.logoutSettingBg.setOnClickListener {
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }

        binding.swipeContainer.setOnRefreshListener(object : SSPullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                // This is demo code to perform
                GlobalScope.launch {
                    delay(1000)
                    binding.swipeContainer.setRefreshing(false) // This line stops layout refreshing

                    MainScope().launch {
                        Toast.makeText(this@MainActivity,"Refresh Complete",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        binding.swipeContainer.setLottieAnimation("refresh.json")
        binding.swipeContainer.setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.NORMAL)
        binding.swipeContainer.setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
        binding.swipeContainer.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.swipeContainer.setRefreshViewParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300))

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler= BackPressCloseHandler(this)

    }

    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.recyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    private fun getDataList() {
        adapter = JoborderAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        updateRecyclerViewAnimDuration()
        //binding.recyclerView.adapter = JoborderAdapter(this)
        mainViewModel.getAllJoborders()?.observe(this, Observer { joborderList->
            if(joborderList!=null){
                adapter.setJoborderlist(joborderList)
                binding.swipeContainer.setRefreshing(false)
            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }

        })

        mainViewModel.getSlitterList()?.observe(this, Observer { slitterList->
            if(slitterList!=null){
                adapter.setSlitterList(slitterList)
                binding.swipeContainer.setRefreshing(false)
            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }

        })

        binding.swipeContainer.setRefreshing(false)
    }

    //Update RecyclerView Item Animation Durations
    private fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        adapter.getScaleDownAnimator(isScaledDown)



    //뒤로 가기 버튼 2번 클릭시 종료
    override fun onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler?.onBackPressed()
    }



}