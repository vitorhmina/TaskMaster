package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dashboard)

        val userCard = findViewById<CardView>(R.id.UserCard)
        val xxxxCard = findViewById<CardView>(R.id.xxxxCard)
        val projectCard = findViewById<CardView>(R.id.projectCard)
        val taskCard = findViewById<CardView>(R.id.TaskCard)

        // Configurar o clique nos CardViews
        userCard.setOnClickListener {
            val intent = Intent(this, Users::class.java)
            startActivity(intent)
            //finish() // Opcional: encerra a MainActivity após iniciar a nova atividade
        }

        xxxxCard.setOnClickListener {
            val intent = Intent(this, Users::class.java)
            startActivity(intent)
        }

        projectCard.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        taskCard.setOnClickListener {
            val intent = Intent(this, Tasks::class.java)
            startActivity(intent)
        }
    }
}