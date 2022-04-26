package com.returnz3ro.messystem.view.ui

import android.content.ContentValues
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityRecogqrBinding
import com.returnz3ro.messystem.viewmodel.MainViewModel


class RecogQRActivity : AppCompatActivity(){

    private lateinit var qrScan: IntentIntegrator
    private lateinit var binding: ActivityRecogqrBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recogqr)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        qrScan = IntentIntegrator(this)
        qrScan.setOrientationLocked(false)
        qrScan.initiateScan()
        qrScan.setBeepEnabled(false)

        // Setting viewmodel
        mainViewModel = ViewModelProvider(this, MainViewModel.Factory(this)).get(MainViewModel::class.java)

        var webSettings : WebSettings = binding.wv.settings
        webSettings.javaScriptEnabled = true

        binding.et.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //bt의 onClick을 실행
                binding.bt.callOnClick()
                //키보드 숨기기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
        qrScan.initiateScan()
    }

    fun onClick(view: View?) {
        var address: String = binding.et.getText().toString()
        if (!address.startsWith("http://")) {
            //address = "http://192.168.0.54:8080/api/performance/C1040_1_792315"
            address = "http://$address"
        }
        //address = "http://192.168.0.54:8080/api/performance/C1040_1_792315"
        binding.wv.loadUrl(address)
    }

    override fun onBackPressed() {
        if (binding.wv.isActivated()) {
            if (binding.wv.canGoBack()) {
                binding.wv.goBack()
            } else {
//                val mainIntent=Intent(this, MainActivity::class.java)
//                startActivity(mainIntent)
//                finish()
                qrScan.initiateScan()
            }
        } else {
            super.onBackPressed()
        }
    }


    // Get the results:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                var a = result.contents.split('/')
                Log.d(ContentValues.TAG,   a[a.lastIndex]+ "ddddddddddddddddddddddddddddddd")
                val mainIntent=Intent(this, MainActivity::class.java)
                mainIntent.putExtra("qrdata",a[a.lastIndex])
                startActivity(mainIntent)
                finish()




            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}