package com.returnz3ro.messystem.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityLoginBinding
import com.returnz3ro.messystem.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding;
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginBtn.setOnClickListener {
            observeLogin(binding?.inputId?.text.toString(), binding?.inputPw?.text.toString())
        }
    }

    private fun observeLogin(id: String, password: String){
        loginViewModel?.loginUser(id, password)?.observe(this, Observer { loginUser->
            if(loginUser!=null){
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