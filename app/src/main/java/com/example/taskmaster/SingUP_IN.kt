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

        val buttonNavigate = findViewById<Button>(R.id.sign_up_button)

        buttonNavigate.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}