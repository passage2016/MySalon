package com.example.mysalon.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.example.mysalon.databinding.ActivityForgetPasswordBinding
import com.example.mysalon.databinding.ActivityRegisterBinding
import com.example.mysalon.viewModel.RegisterViewModel
import com.example.mysalon.viewModel.ResetViewModel
import com.google.firebase.messaging.FirebaseMessaging

class ResetActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgetPasswordBinding
    lateinit var resetViewModel: ResetViewModel
    lateinit var phone: String
    lateinit var password: String
    lateinit var confirmPassword: String
    var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resetViewModel = ViewModelProvider(this)[ResetViewModel::class.java]

        binding.btnReset.setOnClickListener {

            phone = binding.etMobilePhone.text.toString()
            password = binding.etLoginPassword.text.toString()
            confirmPassword = binding.etLoginConfirmPassword.text.toString()

            var check = true

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
            if (password != confirmPassword) {
                check = false
                message = "Password not same."
            }
            if (check) {
                resetViewModel.resetPassword(phone, password, binding.etCode.text.toString())
            } else {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Register Error")
                    .setMessage(message)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            }


        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.btnSendCode.setOnClickListener {
            var check = true
            phone = binding.etMobilePhone.text.toString()
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
            if (check) {
                resetViewModel.getPhoneVerificationCode(phone)
            } else {
                val builder = AlertDialog.Builder(this)
                    .setTitle("Register Error")
                    .setMessage(message)
                    .setPositiveButton("Ok") { _, _ ->
                    }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(true)
                alertDialog.show()
            }
        }

        resetViewModel.errorMessage.observe(this){
            val builder = AlertDialog.Builder(this)
                .setTitle("Register Error")
                .setMessage(it)
                .setPositiveButton("Ok") { _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }

        resetViewModel.successMessage.observe(this){
            val builder = AlertDialog.Builder(this)
                .setTitle("Register Success")
                .setMessage(it)
                .setPositiveButton("Ok") { _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }
    }

    companion object {
        const val PHONE = "phone"
        const val PASSWORD = "password"
    }

}