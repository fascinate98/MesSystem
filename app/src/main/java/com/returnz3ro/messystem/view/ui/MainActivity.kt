package com.returnz3ro.messystem.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityMainBinding
import com.returnz3ro.messystem.viewmodel.LoginViewModel
import com.returnz3ro.messystem.viewmodel.MainViewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        getAllData()
    }
    private fun getAllData(){
        mainViewModel?.getAllMainData()?.observe(this, Observer { getAllMainData->
            if(getAllMainData!=null){
                Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
                val mainIntent= Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Login Failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}