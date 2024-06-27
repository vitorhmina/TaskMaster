package com.example.taskmaster

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View

object BottomNavigationHandler {

    fun handleNavigationClicks(context: Context, view: View) {
        when (view.id) {
            R.id.projects -> {
                val intent = Intent(context, Projects::class.java)
                context.startActivity(intent)
            }
            R.id.home -> {
                val intent = Intent(context, Dashboard::class.java)
                context.startActivity(intent)
            }
            R.id.profile -> {
                val intent = Intent(context, EditProfile::class.java)
                context.startActivity(intent)
            }
        }
    }
}
