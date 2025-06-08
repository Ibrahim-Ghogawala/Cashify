package com.ByteMe.cashify_budget_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val regButton = findViewById<AppCompatButton>(R.id.registerButton)
        val logButton = findViewById<AppCompatButton>(R.id.loginButton)

        regButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        logButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}