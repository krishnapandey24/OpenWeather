package com.krishna.openweather.utils

import android.app.Dialog
import android.content.Context
import com.krishna.openweather.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.loading_dialog)
        setCancelable(false)
    }
}

class NetworkErrorDialog(context: Context): Dialog(context){
    init {
        setContentView(R.layout.network_error_dialog)
        setCancelable(true)
    }
}