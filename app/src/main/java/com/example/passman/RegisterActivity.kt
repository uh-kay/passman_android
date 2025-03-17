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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var registerButton: Button
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordConfirmation: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register_button)
        etUsername = findViewById(R.id.register_username)
        etPassword = findViewById(R.id.register_password)
        etPasswordConfirmation = findViewById(R.id.register_confirm_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_panel)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerButton.setOnClickListener {
            var salt = PasswordHelper.generateSalt()

            var userModel = UserModel(
                Username = etUsername.text.toString(),
                Password = PasswordHelper.hashPassword(etPassword.text.toString(), salt).toString(),
                SaltString = salt.toString(),
            )

            checkUser(etUsername.text.toString()) { isSuccess, isRegistered ->
                if (!isSuccess) {
                    Toast.makeText(
                        applicationContext,
                        "error",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@checkUser
                } else if (isRegistered) {
                    Toast.makeText(
                        applicationContext,
                        "Duplicate username",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@checkUser
                }

                this.registerUser(userModel)
            }
        }
    }

    private fun checkUser(username: String, checkResult: (isSuccess: Boolean, isRegistered: Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                var isSuccess = true
                var isRegistered = false

                if (!documents.isEmpty) {
                    isRegistered = true
                }
                checkResult.invoke(isSuccess, isRegistered)
            }
            .addOnFailureListener { e ->
                checkResult.invoke(false, false)
                Log.w(TAG, "Error getting documents: ", e)
            }
    }

    fun registerUser(userModel: UserModel) {
        val db = Firebase.firestore
        db.collection("users")
            .add(userModel)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    applicationContext,
                    "Registration successful",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document: ", e)
            }
    }
}
