package com.krishna.openweather.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.krishna.openweather.R


class DetailsAdapter(private val context:Context, private val dataHolder: Array<Pair<String, String>>, private val size: Int) : RecyclerView.Adapter<DetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.details_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pair= dataHolder[position]
        try{
            holder.key.text= pair.first
            holder.value.text= pair.second
        }catch (e: Exception){
            e.printStackTrace()
        }



    }

    override fun getItemCount(): Int {
        return size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val key: TextView = itemView.findViewById(R.id.key)
        val value: TextView = itemView.findViewById(R.id.value)

    }








}