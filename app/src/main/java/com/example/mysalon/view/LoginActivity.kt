package com.example.mysalon.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.example.mysalon.R
import com.example.mysalon.databinding.ActivityLoginBinding
import com.example.mysalon.view.RegisterActivity.Companion.PASSWORD
import com.example.mysalon.view.RegisterActivity.Companion.PHONE
import com.example.mysalon.viewModel.LoginViewModel
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        if(intent.extras?.get(PHONE) != null && intent.extras?.get(PASSWORD) != null){
            val phone = intent.extras?.get(PHONE) as String
            val password = intent.extras?.get(PASSWORD) as String
            loginViewModel.login(phone, password)
        }

        binding.btnLogin.setOnClickListener {
            val phone = binding.etMobilePhone.text.toString()
            val password = binding.etLoginPassword.text.toString()
            var check = true
            var message = ""
            if (phone.length < 8) {
                check = false
                message = "Mobile Number should at least 8 digits"
            }
            if (phone.length > 13) {
                check = false
                message = "Mobile Number should not more than 13 digits"
            }
            if (!phone.isDigitsOnly()) {
                check = false
                message = "Mobile Number should be digits"
            }
            if (password.length < 8) {
                check = false
                message = "Password should at least 8 digits"
            }
            if (check) {
                loginViewModel.login(phone, password)
            } else {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Login Error")
                    .setMessage(message)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            }

        }

        binding.tvLoginRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgetPassword.setOnClickListener {
            val intent = Intent(this, ResetActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.userLiveData.observe(this){
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    loginViewModel.updateFcmToken(it.result)
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            Log.e(LOGIN_INFO, it.toString())
            intent.putExtra(LOGIN_INFO, it)
            startActivity(intent)
        }

        loginViewModel.errorMessage.observe(this){
            val builder = AlertDialog.Builder(this)
                .setTitle("Login Error")
                .setMessage(it)
                .setPositiveButton("Ok") { _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }

    }

    companion object {
        const val LOGIN_INFO = "login_info"
    }

}