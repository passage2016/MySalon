package com.example.mysalon.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.example.mysalon.databinding.ActivityRegisterBinding
import com.example.mysalon.viewModel.RegisterViewModel
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var registerViewModel: RegisterViewModel
    lateinit var phone: String
    lateinit var password: String
    lateinit var confirmPassword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {

            phone = binding.etRegisterMobileNumber.text.toString()
            password = binding.etRegisterPassword.text.toString()
            confirmPassword = binding.etRegisterConfirmPassword.text.toString()

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
            if (password != confirmPassword) {
                check = false
                message = "Password not same."
            }
            if (check) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                    if (it.isSuccessful) {
                        registerViewModel.signUp(phone, password, it.result)
                    }
                }
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

        binding.tvRegisterLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerViewModel.userLiveData.observe(this) {
            val builder = AlertDialog.Builder(this)
                .setTitle("Sign Up Success")
                .setMessage("Go to login")
                .setPositiveButton("Login") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra(PHONE, phone)
                    intent.putExtra(PASSWORD, password)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    companion object {
        const val PHONE = "phone"
        const val PASSWORD = "password"
    }

}