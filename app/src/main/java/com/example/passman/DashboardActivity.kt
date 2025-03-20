package com.example.passman

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<DataViewModel>()

        data.add(DataViewModel(com.google.android.material.R.drawable.ic_m3_chip_close, "Lorem ipsum", "Lorem ipsum sir dolor amet..."))
        data.add(DataViewModel(com.google.android.material.R.drawable.ic_keyboard_black_24dp, "Apa maksud", "Lorem ipsum sir dolor amet..."))
        data.add(DataViewModel(com.google.android.material.R.drawable.ic_keyboard_black_24dp, "Coba aja", "Lorem ipsum sir dolor amet..."))

        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter
    }
}