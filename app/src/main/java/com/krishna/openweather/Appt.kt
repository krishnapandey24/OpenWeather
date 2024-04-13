package com.krishna.openweather

import android.content.Context
import android.util.Log
import android.widget.Toast

object Appt {

    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    fun log(message: String){
        Log.d("tagg",message)
    }

}
