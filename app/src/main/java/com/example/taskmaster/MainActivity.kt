package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.init_app)

        //roda
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = 50

        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Log.d("MainActivity", "Countdown finished, starting Intro1 activity...")
                val intent = Intent(this@MainActivity, Intro1::class.java)
                startActivity(intent)
                Log.d("MainActivity", "Intro1 activity started.")
                finish()
                Log.d("MainActivity", "MainActivity finished.")
            }
        }.start()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


