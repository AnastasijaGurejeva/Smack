package com.example.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Services.AuthService.authToken
import com.example.smack.Utilities.URL_ADD_USER
import org.json.JSONException
import org.json.JSONObject

object UserDataService {
    var id = ""
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
                    UserDataService.id = response.getString("_id")
                    complete(true)
                } catch(e: JSONException) {
                    Log.d("JSON", "EXC "+e.localizedMessage)
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
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }

        }
        Volley.newRequestQueue(context).add(addUserRequest)

    }


}