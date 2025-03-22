package com.example.passman

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddActivity: AppCompatActivity() {
    lateinit var addButton: Button
    lateinit var etTitle: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addButton = findViewById<Button>(R.id.add_button)
        etTitle = findViewById<EditText>(R.id.add_title)
        etUsername = findViewById<EditText>(R.id.add_username)
        etPassword = findViewById<EditText>(R.id.add_password)

        addButton.setOnClickListener {
            var salt = PasswordHelper.generateSalt()

            var passModel = PasswordModel(
                Title = etTitle.text.toString(),
                Username = etUsername.text.toString(),
                Password = etPassword.text.toString(),
                UserId = getUserId(),
            )

            this.addPassword(passModel)
        }
    }

    fun getUserId(): String? {
        val sharedPreference = getSharedPreferences("app_preference", MODE_PRIVATE)
        return sharedPreference.getString("user_id", null)
    }

    fun addPassword(passModel: PasswordModel) {
        val db = Firebase.firestore
        db.collection("passwords")
            .add(passModel)
            .addOnSuccessListener { documentReference ->
                val generatedDocumentId = documentReference.id

                documentReference.update("id", generatedDocumentId)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "Add password successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document with ID: ", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document: ", e)
            }
    }
}