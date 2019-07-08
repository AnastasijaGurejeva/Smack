package com.example.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.smack.R
import com.example.smack.Services.AuthService
import com.example.smack.Services.UserDataService
import com.example.smack.Utilities.BROADCAST_USER_DATA_CHANGES
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5 0.5 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        addUserSpinner.visibility = View.INVISIBLE
    }

    fun onGenerateUserAvatar(view: View) {
        val random = java.util.Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", this.packageName)
        userAvatarSignUp.setImageResource(resourceId)
    }

    fun onCreateBackgroundColor(view: View) {
        val random = java.util.Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        userAvatarSignUp.setBackgroundColor(Color.rgb(r, g, b))

        //to translate color for both IOS and Android
        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB]"
    }

    fun onCreateUserClicked(view: View) {
        enableSpinner(true)
        val email = userEmailSignUp.text.toString()
        val password = userPasswordSignUp.text.toString()
        val userName = userNameSignUp.text.toString()

        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            UserDataService.addUser(this, userName, email, userAvatar, avatarColor) { addUserSuccess ->
                                if (addUserSuccess) {
                                    println(addUserSuccess)
                                    val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGES)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChanged)
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else
            Toast.makeText(this, "Make sure all fields are filled in", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            addUserSpinner.visibility = View.VISIBLE

        } else {
            addUserSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        userAvatarSignUp.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}
