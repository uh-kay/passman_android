package com.example.passman

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Base64

class LoginActivity : AppCompatActivity() {

    lateinit var loginButton : Button
    lateinit var etUsername : EditText
    lateinit var etPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreference = getSharedPreferences(
            "app_preference", MODE_PRIVATE
        )

        var id = sharedPreference.getString("id", "").toString()

        if (id.isNotBlank()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton = findViewById<Button>(R.id.login_button)
        etUsername = findViewById<EditText>(R.id.login_username)
        etPassword = findViewById<EditText>(R.id.login_password)

        loginButton.setOnClickListener {
            this.auth(etUsername.text.toString(), etPassword.text.toString()) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        applicationContext,
                        "Wrong username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@auth
                }

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun auth(username: String, password: String, checkResult: (isValid: Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                var isValid = false

                for (document in documents) {
                    var pass = document.data["password"].toString()
                    var salt = Base64.getDecoder().decode(document.data["saltString"].toString())

                    var checkPass = Base64.getEncoder().encodeToString(PasswordHelper.hashPassword(password, salt))

                    if (pass != checkPass) {
                        break
                    }

                    val sharedPreference = getSharedPreferences(
                        "app_preference", MODE_PRIVATE
                    )

                    sharedPreference.edit(commit = true) {
                        putString("user_id", document.data["id"].toString())
                        putString("master_pass", document.data["password"].toString())
                    }

                    isValid = true
                }

                checkResult.invoke(isValid)
            }
    }
}