package com.example.passman

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.passman.databinding.ActivityMainBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private var currentPanel: View? = null
    private lateinit var mainPanel: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainPanel = findViewById<View>(R.id.main_panel)

        val loginPanel = findViewById<View>(R.id.login_panel)
        val registerPanel = findViewById<View>(R.id.register_panel)

        mainPanel.visibility = View.VISIBLE
        loginPanel.visibility = View.GONE
        registerPanel.visibility = View.GONE

        val registerButton = findViewById<Button>(R.id.register)
        registerButton.setOnClickListener {
            showPanel(registerPanel)
        }

        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            showPanel(loginPanel)
        }

        setupBackButton(loginPanel)
        setupBackButton(registerPanel)
    }

    private fun showPanel(panel: View) {
        mainPanel.visibility = View.GONE

        currentPanel?.visibility = View.GONE

        panel.visibility = View.VISIBLE
        currentPanel = panel
    }

    private fun setupBackButton(panel: View) {
        val backButton = panel.findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            panel.visibility = View.GONE
            mainPanel.visibility = View.VISIBLE
            currentPanel = null
        }
    }
}

