package com.example.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Controller.App
import com.example.smack.Utilities.BROADCAST_USER_DATA_CHANGES
import com.example.smack.Utilities.URL_FIND_USER
import com.example.smack.Utilities.URL_LOGIN
import com.example.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

object AuthService {

    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER,
            Response.Listener { response ->
                println(response)
                complete(true)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Couldn't register user: $error")
                complete(false)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(registerRequest)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null,
            Response.Listener { response ->
                try {
                    App.prefs.userEmail = response.getString("user")
                    App.prefs.authToken = response.getString("token")
                    App.prefs.isLoggedIn = true
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }

            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Couldn't login user: $error")
                complete(false)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(loginRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_FIND_USER${App.prefs.userEmail}", null,
            Response.Listener { response ->
                try {
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarName = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    UserDataService.userId = response.getString("_id")

                    val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGES)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChanged)
                    complete(true)


                } catch (e: JSONException) {
                    Log.d("JSON", "EXP: " + e.localizedMessage)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Couldn't find user: $error")
                complete(false)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(findUserRequest)
    }

}