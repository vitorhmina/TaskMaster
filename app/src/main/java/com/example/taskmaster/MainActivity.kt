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
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.init_app)

        findViewById<ProgressBar>(R.id.progressBar).progress = 50

        object : CountDownTimer(3500, 500) {
            override fun onTick(millisUntilFinished: Long) {
                // No operation
            }

            override fun onFinish() {
                val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
                var isInternetAvailable = NetworkUtils.isInternetAvailable(this@MainActivity)
                val intro1_interface = Intent(this@MainActivity, Intro1::class.java)
                val signup_in_interface = Intent(this@MainActivity, SignUP_IN::class.java)
                val nointernet_interface = Intent(this@MainActivity, NoInternet::class.java)

                if(!isInternetAvailable){
                    startActivity(nointernet_interface)
                    finish()
                    return
                }
                if(isFirstRun){
                    startActivity(intro1_interface)
                    finish()
                    return
                }
                startActivity(signup_in_interface)
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


