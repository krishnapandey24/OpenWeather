package com.krishna.openweather.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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


class WeeklyAdapter(private val context:Context, private val dataHolder: List<Forecast>, private val size: Int) : RecyclerView.Adapter<WeeklyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.weekly_view_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val forecast= dataHolder[position]
        holder.dayAndDate.text= Appt.epochToDayDate(forecast.dt)
        Picasso.get().load(Constants.iconUrl.format(forecast.weather.first().icon)).into(holder.icon)
        holder.maxMinTemperature.text= "${forecast.main.tempMax} / ${forecast.main.tempMin}$celsius"
        holder.goToButton.setOnClickListener{
//            val intent = Intent(context, ServiceActivity::class.java)
//            intent.putExtra("service",service)
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val icon: ImageView = itemView.findViewById(R.id.imageView2)
        val dayAndDate: TextView= itemView.findViewById(R.id.dayAndDate)
        val maxMinTemperature: TextView= itemView.findViewById(R.id.maxMinTempView)
        val goToButton: ImageButton= itemView.findViewById(R.id.goTo)
    }








}