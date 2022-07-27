package com.example.mysalon.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mysalon.R
import com.example.mysalon.databinding.ActivityLoginBinding
import com.example.mysalon.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.btnLogin.setOnClickListener {
            val phone = binding.etMobilePhone.text.toString()
            val password = binding.etLoginPassword.text.toString()
            loginViewModel.login(phone, password)
        }

        binding.tvLoginRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.userLiveData.observe(this){
            val intent = Intent(this, MainActivity::class.java)
            Log.e(LOGIN_INFO, it.toString())
            intent.putExtra(LOGIN_INFO, it)
            startActivity(intent)
        }

    }

    companion object {
        const val LOGIN_INFO = "login_info"
    }

}