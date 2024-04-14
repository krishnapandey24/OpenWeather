package com.krishna.openweather.utils

import android.app.Dialog
import android.content.Context
import android.view.View.OnClickListener
import android.widget.Button
import com.krishna.openweather.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.loading_dialog)
        setCancelable(false)
    }
}

class NetworkErrorDialog(context: Context, onClickListener: OnClickListener): Dialog(context){
    init {
        setContentView(R.layout.network_error_dialog)
        setCancelable(false)
        val button: Button= findViewById(R.id.button)
        button.setOnClickListener {
            onClickListener.onClick(button)
            dismiss()
        }
    }
}


class LocationPermissionNotGrantedDialog(context: Context, onClickListener: OnClickListener): Dialog(context){
    init {
        setContentView(R.layout.location_permisson_denied)
        setCancelable(false)
        val button: Button= findViewById(R.id.button)
        button.setOnClickListener {
            onClickListener.onClick(button)
            dismiss()
        }
    }
}