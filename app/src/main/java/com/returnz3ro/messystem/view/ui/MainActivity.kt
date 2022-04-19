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
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityMainBinding
import com.returnz3ro.messystem.view.adapter.JoborderAdapter
import com.returnz3ro.messystem.viewmodel.MainViewModel


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

        binding.swipeContainer.setOnRefreshListener {
            getJoborderList()
            binding.swipeContainer.isRefreshing = false
        }

        // RecyclerView Init


        //get data retrofit
        getJoborderList()

        mainViewModel.getAllJoborders()

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
        binding.logoutSettingBg.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 스크롤 업 대신에 리프레쉬 이벤트가 트리거 되는걸 방지
        binding.swipeContainer.setOnRefreshListener {
            getJoborderList()
        }


        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler= BackPressCloseHandler(this)

    }

    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.recyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    private fun getJoborderList() {
        adapter = JoborderAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        updateRecyclerViewAnimDuration()
        //binding.recyclerView.adapter = JoborderAdapter(this)
        mainViewModel.getAllJoborders()?.observe(this, Observer { joborderList->
            if(joborderList!=null){
                adapter.setJoborderlist(joborderList)
                Log.d(ContentValues.TAG, joborderList.get(1).joborderJobname + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")

            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }

        })

        binding.swipeContainer.isRefreshing = false
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