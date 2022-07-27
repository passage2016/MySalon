package com.example.mysalon.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mysalon.R
import com.example.mysalon.databinding.ActivityRegisterBinding
import com.example.mysalon.viewModel.RegisterViewModel
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity(){
    lateinit var binding: ActivityRegisterBinding
    lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if(it.isSuccessful) {
                    val phone = binding.etRegisterMobileNumber.text.toString()
                    val password = binding.etRegisterPassword.text.toString()
                    registerViewModel.signUp(phone, password, it.result)
                }
            }

        }

        binding.tvRegisterLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerViewModel.userLiveData.observe(this){
            val builder = AlertDialog.Builder(this)
                .setTitle("Sign Up Success")
                .setMessage("Go to login")
                .setPositiveButton("Login"){
                        _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel"){
                        _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

}