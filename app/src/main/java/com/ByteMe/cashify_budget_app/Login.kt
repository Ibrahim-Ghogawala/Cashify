package com.ByteMe.cashify_budget_app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var CashifyDbAuth: FirebaseAuth
    private lateinit var CashifyDbStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CashifyDbAuth = FirebaseAuth.getInstance()
        CashifyDbStore = FirebaseFirestore.getInstance()

        val usernameBox = findViewById<EditText>(R.id.LogUsername)
        val passwordBox = findViewById<EditText>(R.id.LogPassword)
        val loginButton = findViewById<AppCompatButton>(R.id.LogButton)
        val registerButton = findViewById<TextView>(R.id.RegLinkButton)
        val ForgotPassword = findViewById<TextView>(R.id.ForgotPasswordLink)

        registerButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        ForgotPassword.setOnClickListener {
            val intent = Intent(this, Forgot_Password::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameBox.text.toString().trim()
            val password = passwordBox.text.toString().trim()

            if (password.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CashifyDbStore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val email = document.getString("email")

                        CashifyDbAuth.signInWithEmailAndPassword(email!!, password)
                            .addOnSuccessListener { task ->
                                Toast.makeText(
                                    this,
                                    "Login Successful!, Welcome $username!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, Home::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else {
                        Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


