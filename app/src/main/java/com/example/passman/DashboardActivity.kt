package com.example.passman

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        data.add(DataViewModel(R.drawable.baseline_key_24, "Lorem ipsum", "Lorem ipsum sir dolor amet..."))
        data.add(DataViewModel(R.drawable.baseline_key_24, "Apa maksud", "Lorem ipsum sir dolor amet..."))
        data.add(DataViewModel(R.drawable.baseline_key_24, "Coba aja", "Lorem ipsum sir dolor amet..."))

        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter

        val addButton = findViewById<FloatingActionButton>(R.id.add_pass)

        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}