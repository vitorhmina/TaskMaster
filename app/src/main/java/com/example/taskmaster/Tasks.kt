package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.Task
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.example.taskmaster.retrofit.Project

class Tasks : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var titleTextView: TextView
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.projects)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        projectId = intent.getIntExtra("projectId", -1)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        titleTextView = findViewById(R.id.title)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        fetchProjectName()
        fetchTasks()
    }

    private fun fetchProjectName() {
        if (projectId == -1) {
            Log.e("Tasks", "Invalid project ID")
            return
        }

        apiService.getProjectById(projectId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val project = response.body()
                    if (project != null) {
                        // Set the title TextView with the project name
                        titleTextView.text = project.name
                    }
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Log.e("Tasks", "API call failed", t)
            }
        })

    }

    private fun fetchTasks() {
        if (projectId == -1) {
            Log.e("Tasks", "Invalid project ID")
            return
        }

        apiService.getProjectTasks(projectId).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    val taskList = response.body()
                    if (taskList != null) {
                        val adapter = TaskAdapter(taskList)
                        recyclerView.adapter = adapter
                    } else {
                        Log.e("Tasks", "Task list is null or empty")
                    }
                } else {
                    Log.e("Tasks", "Failed to fetch tasks: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("Tasks", "API call failed", t)
            }
        })
    }
}
