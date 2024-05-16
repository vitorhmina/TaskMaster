package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Adicionando manipulação da barra de progresso
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = 50 // Definindo o progresso inicial

        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Aqui você pode atualizar a barra de progresso a cada segundo se necessário
            }

            override fun onFinish() {
                // Cria um Intent para a nova atividade que você deseja abrir
                val intent = Intent(this@MainActivity, Intro1::class.java)
                // Inicia a nova atividade
                startActivity(intent)
                // Fecha a atividade atual
                finish()
            }
        }.start()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


