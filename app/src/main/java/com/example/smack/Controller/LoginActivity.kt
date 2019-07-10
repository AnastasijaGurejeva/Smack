package com.example.smack.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smack.R
import com.example.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginClickedLoginPage(view: View) {
        val email = emailLoginField.text.toString()
        val password = passwordLoginField.text.toString()
        AuthService.loginUser(this, email, password) { logginSuccess ->
            if(logginSuccess) {
                AuthService.findUserByEmail(this) { findSucces ->
                    if(findSucces) {
                        finish()
                    }
                }
            }

        }
        
    }

    fun onSignUpClicked(view: View) {
        val signupIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signupIntent)
        finish()
    }
}
