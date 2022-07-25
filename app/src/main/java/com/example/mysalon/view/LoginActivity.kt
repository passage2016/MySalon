package com.example.mysalon.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysalon.R

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val etLoginEmail: EditText = findViewById(R.id.et_login_email)
        val etLoginPassword: EditText = findViewById(R.id.et_login_password)
        val tvLoginRegister: TextView = findViewById(R.id.tv_login_register)

    }

}