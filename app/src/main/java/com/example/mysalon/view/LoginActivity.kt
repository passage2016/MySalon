package com.example.mysalon.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
            val intent: Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}