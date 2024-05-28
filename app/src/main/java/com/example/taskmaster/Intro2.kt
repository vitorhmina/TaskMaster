package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Intro2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.intro_2)

        val imageViewArrow = findViewById<ImageView>(R.id.imageView2)

        // Configure o clique na ImageView para iniciar a atividade Intro2
        imageViewArrow.setOnClickListener {
            val intent = Intent(this, Intro3::class.java)
            startActivity(intent)
            finish()
        }
    }
}