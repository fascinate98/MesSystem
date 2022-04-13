package com.returnz3ro.messystem.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityLoginBinding
import com.returnz3ro.messystem.view.callback.LoginActivityCallback
import com.returnz3ro.messystem.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity(), LoginActivityCallback {

    private var activityLoginBinding: ActivityLoginBinding?=null
    private var loginViewModel: LoginViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding= DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding?.loginActivityCallback=this
        loginViewModel = ViewModelProvider(this, LoginViewModel.Factory(this)).get(LoginViewModel::class.java)
    }
    override fun onLoginClick(view: View) {
        observeLogin(activityLoginBinding?.inputId?.text.toString(), activityLoginBinding?.inputPw?.text.toString())
    }

    private fun observeLogin(email: String, password: String){
        loginViewModel?.loginRepository(email, password)?.observe(this, Observer { loginUser->
            if(loginUser!=null){
                val mainIntent=Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }else{
                Toast.makeText(applicationContext, "Login Failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}