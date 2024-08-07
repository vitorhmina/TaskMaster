package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Intro1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.intro_1)

        val imageViewArrow = findViewById<ImageView>(R.id.imageView2)

        imageViewArrow.setOnClickListener {
            val intent = Intent(this, Intro2::class.java)
            startActivity(intent)
            finish()
        }
    }
}