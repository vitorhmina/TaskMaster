package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SingUP_IN : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup_in)

        val buttonSignup = findViewById<Button>(R.id.sign_up_button)

        buttonSignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        val buttonSignIn = findViewById<Button>(R.id.sign_in_button)

        buttonSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}