package com.returnz3ro.messystem.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.zxing.integration.android.IntentIntegrator
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityRecogqrBinding
import org.json.JSONException
import org.json.JSONObject

class RecogQRActivity : AppCompatActivity(){

    private lateinit var qrScan: IntentIntegrator
    private lateinit var binding: ActivityRecogqrBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recogqr)

        qrScan = IntentIntegrator(this)
        qrScan.setOrientationLocked(false)
        qrScan.initiateScan()

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
        IntentIntegrator(this).initiateScan()
    }

    fun onClick(view: View?) {
        var address: String = binding.et.getText().toString()
        if (!address.startsWith("http://")) {
            address = "http://$address"
        }
        binding.wv.loadUrl(address)
    }

    override fun onBackPressed() {
        if (binding.wv.isActivated()) {
            if (binding.wv.canGoBack()) {
                binding.wv.goBack()
            } else {
                qrScan.initiateScan()
            }
        } else {
            super.onBackPressed()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // QR 코드를 찍은 결과를 변수에 담는다.
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.d("TTT", "QR 코드 체크")

        //결과가 있으면
        if (result != null) {
            // 컨텐츠가 없으면
            if (result.contents == null) {
                //토스트를 띄운다.
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
            // 컨텐츠가 있으면
            else {
                //토스트를 띄운다.
                Toast.makeText(this, "scanned" + result.contents, Toast.LENGTH_LONG).show()
                Log.d("TTT", "QR 코드 URL:${result.contents}")

                //웹뷰 설정
                binding.wv.settings.javaScriptEnabled = true
                binding.wv.webViewClient = WebViewClient()

                //웹뷰를 띄운다.
                binding.wv.loadUrl(result.contents)
            }
            // 결과가 없으면
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}