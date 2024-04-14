package com.krishna.openweather.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.krishna.openweather.R
import com.krishna.openweather.models.Forecast
import com.krishna.openweather.utils.Appt
import com.krishna.openweather.utils.Constants
import com.krishna.openweather.utils.Constants.celsius
import com.squareup.picasso.Picasso


class TimeAndWeatherAdapter(private val context:Context, private val dataHolder: List<Forecast>, private val size: Int) : RecyclerView.Adapter<TimeAndWeatherAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.time_and_temperature_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val forecast= dataHolder[position]
        holder.time.text= Appt.epochToTime(forecast.dt)
        Picasso.get().load(Constants.iconUrl.format(forecast.weather.first().icon)).into(holder.icon)
        holder.temperature.text= "${forecast.main.temp}$celsius"
    }

    override fun getItemCount(): Int {
        return size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val icon: ImageView = itemView.findViewById(R.id.iconView)
        val time: TextView= itemView.findViewById(R.id.timeView)
        val temperature: TextView= itemView.findViewById(R.id.temperatureView)
    }








}