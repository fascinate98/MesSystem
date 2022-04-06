package com.returnz3ro.messystem.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.returnz3ro.messystem.R
import com.returnz3ro.messystem.databinding.ActivityLoginBinding
import java.io.Console

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginBtn.setOnClickListener {
            val id = binding.inputId.text
            val pw = binding.inputPw.text

        }
    }
}