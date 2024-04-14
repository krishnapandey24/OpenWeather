package com.krishna.openweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krishna.openweather.R
import com.krishna.openweather.adapters.DetailsAdapter
import com.krishna.openweather.adapters.WeeklyAdapter
import com.krishna.openweather.databinding.ActivityMain2Binding
import com.krishna.openweather.databinding.ActivityMainBinding
import com.krishna.openweather.models.Forecast

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val day= intent.getStringExtra("day") ?: ""
        binding.toolbar.title=day
        val details= intent.getSerializableExtra("details") as? Array<Pair<String, String>> ?: emptyArray()

        val recyclerView: RecyclerView = binding.detailsView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = DetailsAdapter(this, details, details.size)
    }


}