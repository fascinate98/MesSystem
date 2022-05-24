package com.returnz3ro.messystem.view.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityLoginBinding
import com.returnz3ro.messystem.retrofit.RetrofitInstance
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.view.callback.LoginActivityCallback
import com.returnz3ro.messystem.viewmodel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity(), LoginActivityCallback {

    private var activityLoginBinding: ActivityLoginBinding?=null
    private var loginViewModel: LoginViewModel?=null
    private var backPressCloseHandler: BackPressCloseHandler?= null
    private lateinit var userData: DataStoreModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding= DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding?.loginActivityCallback=this
        loginViewModel = ViewModelProvider(this, LoginViewModel.Factory(this)).get(LoginViewModel::class.java)
        userData = DataStoreModule(this)
        //뒤로 가기 버튼 2번 클릭시 종료
        backPressCloseHandler= BackPressCloseHandler(this)
    }
    override fun onLoginClick(view: View) {
        hideKeyboard()
        observeLogin(activityLoginBinding?.inputId?.text.toString(), activityLoginBinding?.inputPw?.text.toString())
    }

    private fun observeLogin(id: String, password: String){

        loginViewModel?.loginService(id, password)?.observe(this, Observer { loginUser->
            if(loginUser!=null){
                GlobalScope.launch {
                    userData.setUserData(loginUser)
                }
                val mainIntent=Intent(this, MainActivity::class.java)
                mainIntent.putExtra("loginUser", loginUser)
                startActivity(mainIntent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Login Failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //키보드 숨기기
    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activityLoginBinding?.inputId?.getWindowToken(), 0)
        imm.hideSoftInputFromWindow(activityLoginBinding?.inputPw?.getWindowToken(), 0)
    }

    //화면 터치 시 키보드 내려감
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //뒤로 가기 버튼 2번 클릭시 종료
    override fun onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler?.onBackPressed()
    }

}