package com.example.passman

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class PasswordDetail : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var copyUsernameButton: ImageButton
    private lateinit var copyPasswordButton: ImageButton
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: Button

    private var passId: String? = null
    private var password: String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pass)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        passId = intent.getStringExtra("passId")
        if (passId == null) {
            Toast.makeText(this, "Error: Password ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleTextView = findViewById(R.id.title_text)
        usernameTextView = findViewById(R.id.username_text)
        passwordTextView = findViewById(R.id.password_text)
        copyUsernameButton = findViewById(R.id.copy_username)
        copyPasswordButton = findViewById(R.id.copy_password)
        editButton = findViewById(R.id.edit_pass)

        fetchPasswordDetails(passId!!)

        setupButtonListeners()
    }

    private fun fetchPasswordDetails(passId: String) {
        val db = Firebase.firestore

        db.collection("passwords")
            .whereEqualTo("id", passId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Password not found", Toast.LENGTH_SHORT).show()
                    finish()
                    return@addOnSuccessListener
                }

                val document = documents.documents[0]
                val title = document.getString("title") ?: "Unknown Title"
                username = document.getString("username") ?: "Unknown Username"
                password = document.getString("password")

                // Update UI
                titleTextView.text = title
                usernameTextView.text = username
                passwordTextView.text = password
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting password details", e)
                Toast.makeText(this, "Error fetching password details", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun setupButtonListeners() {
        copyUsernameButton.setOnClickListener {
            copyToClipboard("Username", username ?: "")
        }

        copyPasswordButton.setOnClickListener {
            copyToClipboard("Password", password ?: "")
        }

        editButton.setOnClickListener {
            // TODO: Implement edit functionality
            Toast.makeText(this, "Edit functionality not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToClipboard(label: String, text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
    }
}