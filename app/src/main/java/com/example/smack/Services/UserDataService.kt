package com.example.smack.Services

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Controller.App
import com.example.smack.Utilities.URL_ADD_USER
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

object UserDataService {
    var userId = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun addUser(context: Context, name: String, email: String, avatarName: String,
                avatarColor: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val addUserRequest = object: JsonObjectRequest(Method.POST, URL_ADD_USER, null,
            Response.Listener { response ->
                try{
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarName =  response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    UserDataService.userId = response.getString("_id")
                    complete(true)
                } catch(e: JSONException) {
                    Log.d("JSON", "EXC "+ e.localizedMessage)
                }

        }, Response.ErrorListener {error ->
                Log.d("ERROR", "Couldn't add user: $error")
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
        App.prefs.requestQueue.add(addUserRequest)

    }

    fun returnAvatarColor(components: String) : Int {
        val strippedColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        var r = 0
        var g = 0
        var b = 0
        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()) {
            r = (scanner.nextDouble()* 255).toInt()
            g = (scanner.nextDouble()* 255).toInt()
            b = (scanner.nextDouble()* 255).toInt()
        }
        return Color.rgb(r, g, b)
    }

    fun logout() {
        userId = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
    }


}