package com.example.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.smack.Controller.App
import com.example.smack.Model.Channel
import com.example.smack.Model.Message
import com.example.smack.Utilities.URL_GET_CHANNELS
import com.example.smack.Utilities.URL_GET_MESSAGES
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {

        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null,
            Response.Listener { response ->
                try {
                    for (i in 0 until response.length()) {
                        val channel = response.getJSONObject(i)
                        val name = channel.getString("name")
                        val chanDesc = channel.getString("description")
                        val chanId = channel.getString("_id")

                        val newChannel = Channel(name, chanDesc, chanId)
                        this.channels.add(newChannel)
                        complete(true)
                    }

                } catch (e: JSONException) {
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

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGES$channelId"

        val messagesRequest = object : JsonArrayRequest(Method.GET, url, null,
            Response.Listener { response ->
                clearMessages()
                try {
                    for (i in 0 until response.length()) {
                        val message = response.getJSONObject(i)
                        val messageBody = message.getString("messageBody")
                        val channelID = message.getString("channelId")
                        val id = message.getString("_id")
                        val userName = message.getString("userName")
                        val userAvatar = message.getString("userAvatar")
                        val userAvatarColor = message.getString("userAvatarColor")
                        val timeStamp = message.getString("timeStamp")

                        val newMessage = Message(
                            messageBody, userName, channelID, userAvatar,
                            userAvatarColor, id, timeStamp
                        )
                        this.messages.add(newMessage)
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "Exc: " + e.localizedMessage)
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Couldn't retrieve messages")
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
        App.prefs.requestQueue.add(messagesRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels() {
        channels.clear()
    }
}