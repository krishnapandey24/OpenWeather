package com.krishna.openweather.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Appt {

    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    fun log(message: String){
        Log.d("tagg",message)
    }

    fun epochToTime(epoch: Long): String {
        val date = Date(epoch * 1000)
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    fun epochToDayDate(epoch: Long): String {
        val date = Date(epoch * 1000)
        val sdf = SimpleDateFormat("EEE MMM d", Locale.getDefault())
        return sdf.format(date)
    }


}
