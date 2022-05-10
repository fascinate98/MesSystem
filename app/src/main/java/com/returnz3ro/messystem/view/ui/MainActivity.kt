package com.returnz3ro.messystem.view.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.datastore.dataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityMainBinding
import com.returnz3ro.messystem.service.model.datamodel.Joborder
import com.returnz3ro.messystem.service.model.datamodel.User
import com.returnz3ro.messystem.service.model.datamodel.WorkResult
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.utils.bindView
import com.returnz3ro.messystem.view.adapter.JoborderAdapter
import com.returnz3ro.messystem.viewmodel.MainViewModel
import com.simform.refresh.SSPullToRefreshLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.internal.wait


var animationPlaybackSpeed: Double = 0.8

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: JoborderAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var dataStore: DataStoreModule

    private val userName: TextView by bindView(R.id.username)
    private val logout: LinearLayout by bindView(R.id.logout)
    private var loginUser: User = User("","","","","")
    private var backPressCloseHandler: BackPressCloseHandler?= null

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()
//
//    var isAdapterFiltered: Boolean
//        get() = adapter.isFiltered
//        set(value) {
//            adapter.isFiltered = value
//        }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting viewmodel
        mainViewModel = ViewModelProvider(this, MainViewModel.Factory(this)).get(MainViewModel::class.java)
        binding.lifecycleOwner = this

        // Appbar behavior init
        (binding.appbar.layoutParams as CoordinatorLayout.LayoutParams).behavior = ToolbarBehavior()

        // RecyclerView Init
        setAdapter()

        // Init FilterLayout
        // binding.filtersMotionLayout.isVisible = true

        // get userinfo from data store
        dataStore = DataStoreModule(this)
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.user.collect{
                loginUser = it
                userName.setText(loginUser.userName)
            }
        }

        // Nav Drawer Init
        //binding.filtersMotionLayout.updateDurations()
        updateRecyclerViewAnimDuration()

        //drawer icon click
        binding.drawerIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // set refresh animation
        setPullToRefresh()


        // check qr recog
        var a = intent.getStringExtra("qrdata")
        if(a != null){
            getQrJoborder(a)
            getSlitterList()
            mainViewModel.recogQrcode(a)
            mainViewModel.getSlitterList()
        }else{
            getDataList()
            mainViewModel.getAllJoborders()
            mainViewModel.getSlitterList()
        }

        //qr icon click
        binding.qrIcon.setOnClickListener {
            startActivity(Intent(this, RecogQRActivity::class.java))
            finish()
        }

        //logout click
        logout.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                dataStore.clearData()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler= BackPressCloseHandler(this)
    }


    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.recyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    private fun getDataList() {
        getJoborderList()
        getSlitterList()
        binding.swipeContainer.setRefreshing(false)
    }

    //Update RecyclerView Item Animation Durations
    fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        adapter.getScaleDownAnimator(isScaledDown)

    private fun setPullToRefresh(){
        binding.swipeContainer.setLottieAnimation("refresh.json")
        binding.swipeContainer.setRefreshStyle(SSPullToRefreshLayout.RefreshStyle.NORMAL)
        binding.swipeContainer.setRepeatMode(SSPullToRefreshLayout.RepeatMode.REPEAT)
        binding.swipeContainer.setRepeatCount(SSPullToRefreshLayout.RepeatCount.INFINITE)
        binding.swipeContainer.setRefreshViewParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300))

        binding.swipeContainer.setOnRefreshListener(object : SSPullToRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                // This is demo code to perform
                //setAdapter()
                //updateRecyclerViewAnimDuration()
                getJoborderList()

                //adapter.notifyDataSetChanged()
                GlobalScope.launch {

                    delay(1000)
                    binding.swipeContainer.setRefreshing(false) // This line stops layout refreshing

                    MainScope().launch {
                        Toast.makeText(this@MainActivity,"Refresh Complete",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }

    private fun setAdapter(){
        adapter = JoborderAdapter(this, object: JoborderAdapter.OnItemClickListener{
            override fun onStartWorkClick(j: Joborder){
                Log.d(ContentValues.TAG,loginUser.userName.toString() + "    이름")
                var resultdata = WorkResult(loginUser.userId, j.joborderId, j.joborderSlitterNo)
                mainViewModel.setStartWork(resultdata)
                getJoborderList()
            }

            override fun onFinishWorkClick(j: Joborder) {
                var resultdata = WorkResult(loginUser.userId, j.joborderId)
                mainViewModel.setFinishWork(resultdata)
                getJoborderList()
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        updateRecyclerViewAnimDuration()
    }

    private fun getJoborderList(){
        mainViewModel.getAllJoborders()?.observe(this, Observer { joborderList->
            if(joborderList!=null){
                adapter.setJoborderList(joborderList)
                //binding.swipeContainer.setRefreshing(false)
                //adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getQrJoborder(joborderid: String){
        mainViewModel.recogQrcode(joborderid)?.observe(this, Observer { joborderList->
            if(joborderList!=null){
                Log.d(TAG, joborderList[0].joborderJobname + "            dfsdfsfsdfsfsdfsdf123123123123123123123123123123")
                adapter.setJoborderList(joborderList)

                //adapter.notifyDataSetChanged()
                //binding.swipeContainer.setRefreshing(false)
            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun getSlitterList(){
        mainViewModel.getSlitterList()?.observe(this, Observer { slitterList->
            if(slitterList!=null){
                adapter.setSlitterList(slitterList)
                //binding.swipeContainer.setRefreshing(false)
                //adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "못가져왓다", Toast.LENGTH_SHORT).show()
            }

        })

    }

    //뒤로 가기 버튼 2번 클릭시 종료
    override fun onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler?.onBackPressed()
    }


}