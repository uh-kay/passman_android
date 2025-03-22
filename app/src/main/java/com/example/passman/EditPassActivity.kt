package com.example.passman

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Base64

class EditPassActivity: AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var editButton: Button

    private var passId: String? = null
    private var title: String? = null
    private var password: String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_panel)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        passId = intent.getStringExtra("passId")

        fetchPasswordDetails(passId.toString())

        titleEditText = findViewById<EditText>(R.id.edit_title)
        usernameEditText = findViewById<EditText>(R.id.edit_username)
        passwordEditText = findViewById<EditText>(R.id.edit_password)
        editButton = findViewById<Button>(R.id.edit_button)

        title = titleEditText.text.toString()
        username = usernameEditText.text.toString()
        password = passwordEditText.text.toString()

        editButton.setOnClickListener {
            updatePassword(passId.toString())
        }
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
                title = document.getString("title")
                username = document.getString("username")
                password = document.getString("password")

                // Update UI
                titleEditText.setText(title)
                usernameEditText.setText(username)
                passwordEditText.setText(password.toString())
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting password details", e)
                Toast.makeText(this, "Error fetching password details", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun updatePassword(passId: String) {
        val db = Firebase.firestore

        val updatedTitle = titleEditText.text.toString()
        val updatedUsername = usernameEditText.text.toString()
        val updatedPassword = passwordEditText.text.toString()

        db.collection("passwords")
            .whereEqualTo("id", passId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Password not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val documentId = documents.documents[0].id

                db.collection("passwords").document(documentId)
                    .update(
                        mapOf(
                        "title" to updatedTitle,
                        "username" to updatedUsername,
                        "password" to updatedPassword,
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error updating password", e)
                        Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error finding password document", e)
                Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
            }

    }
}