package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Intro3 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.intro_3)

        val buttonNavigate = findViewById<Button>(R.id.getStarted_button)

        buttonNavigate.setOnClickListener {
            val intent = Intent(this, SingUP_IN::class.java)
            startActivity(intent)
            finish()
        }
    }
}