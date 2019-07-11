package com.example.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Controller.App
import com.example.smack.Model.Channel
import com.example.smack.Utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()

    fun getCahnnels(complete: (Boolean) -> Unit) {

        val channelsRequest = object: JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null ,
            Response.Listener { response ->
                try {
                        for (i in 0 until response.length()) {
                          val channel = response.getJSONObject(i)
                            val name = channel.getString("name")
                            val chanDesc  = channel.getString("description")
                            val chanId = channel.getString("_id")

                            val newChannel = Channel(name, chanDesc, chanId)
                            this.channels.add(newChannel)
                            complete(true)
                        }

                } catch(e: JSONException) {
                    Log.d("JSON", "Exc: " + e.localizedMessage)
                    complete(false)
                }

        }, Response.ErrorListener { error ->
                Log.d("ERROR", "Couldn't retrieve channels")
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
        App.prefs.requestQueue.add(channelsRequest)
    }
}