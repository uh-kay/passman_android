package com.example.passman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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

        getPass()

        val addButton = findViewById<FloatingActionButton>(R.id.add_pass)

        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getPass() {
        val data = ArrayList<DataViewModel>()

        val userId = getUserId()

        val db = Firebase.firestore

        db.collection("users").document(userId.toString())
            .collection("passwords")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val title = document.getString("title")
                    val username = document.getString("username")

                    data.add(DataViewModel(
                        R.drawable.baseline_key_24,
                        title.toString(),
                        username.toString(),
                    ))
                }

                setupRecyclerView(data)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents", e)
            }
    }

    private fun setupRecyclerView(data: ArrayList<DataViewModel>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CustomAdapter(data)
        recyclerView.adapter = adapter
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("app_preference", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
}