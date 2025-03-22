package com.example.passman

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val lockButton = findViewById<FloatingActionButton>(R.id.lock)

        addButton.setOnClickListener {
            val intent = Intent(this, AddPassActivity::class.java)
            startActivity(intent)
        }

        lockButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getPass()
    }

    private fun getPass() {
        val data = ArrayList<DataViewModel>()

        val userId = getUserId()

        val db = Firebase.firestore

        db.collection("passwords")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val title = document.getString("title")
                    val username = document.getString("username")
                    val id = document.getString("id")

                    data.add(DataViewModel(
                        R.drawable.baseline_key_24,
                        title.toString(),
                        username.toString(),
                        id.toString(),
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

        val adapter = CustomAdapter(data) { clickedItem ->
            val intent = Intent(this, PassDetailActivity::class.java)
            intent.putExtra("passId", clickedItem.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("app_preference", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
}