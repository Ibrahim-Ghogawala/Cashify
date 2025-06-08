package com.ByteMe.cashify_budget_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Forgot_Password : AppCompatActivity() {

    private lateinit var CashifyDb: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CashifyDb = FirebaseAuth.getInstance()

        val Submit = findViewById<AppCompatButton>(R.id.SubmitButton)
        val ForgotEmail = findViewById<EditText>(R.id.ForgotPasswordEmail)
        val BackButton = findViewById<AppCompatButton>(R.id.BackToLogin)

        BackButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        Submit.setOnClickListener {
            val email = ForgotEmail.text.toString().trim()

            if(email.isEmpty()) {
                ForgotEmail.error = "Please enter your email address."
                return@setOnClickListener
            }

            CashifyDb.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ResetConfirmation::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}