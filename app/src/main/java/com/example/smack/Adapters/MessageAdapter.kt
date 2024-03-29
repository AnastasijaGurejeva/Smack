package com.example.smack.Adapters

import android.content.Context
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.smack.R
import com.example.smack.Services.UserDataService
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(val context: Context, val messages: ArrayList<com.example.smack.Model.Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val userName = itemView.findViewById<TextView>(R.id.messageUserNameLable)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeStampLable)
        val messageBody = itemView.findViewById<TextView>(R.id.messageBodyLable)

        fun bindMessage(context: Context, message: com.example.smack.Model.Message) {
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage.setImageResource(resourceId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            userName.text = message.userName
            messageBody.text = message.message
            timeStamp.text = returnDateString(message.timeStamp)
        }

        fun returnDateString(isoString: String): String {

            val isoFormater = SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormater.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormater.parse(isoString)
            } catch (e: ParseException) {
                Log.d("PARSE", "Can't parse date")
            }

            val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())

            return outDateString.format(convertedDate)
        }

    }
}