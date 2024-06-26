package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.Project
import com.example.taskmaster.retrofit.RetrofitClient
import com.example.taskmaster.retrofit.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog


class Tasks : AppCompatActivity(), TaskAdapter.TaskItemClickListener {

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
        fetchProjectName()

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, Projects::class.java)
            startActivity(intent)
        }

        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Create_Task::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
        }

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
                        titleTextView.text = project.name
                    }
                } else {
                    Log.e("Tasks", "Failed to fetch project name: ${response.errorBody()?.string()}")
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
                    taskList?.let {
                        val adapter = TaskAdapter(it, this@Tasks)
                        recyclerView.adapter = adapter
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

    override fun onItemClicked(taskId: Int) {
    }

    override fun onUsersClicked(taskId: Int) {
        val intent = Intent(this, UserProjects::class.java)
        intent.putExtra("taskId", taskId)
        startActivity(intent)
    }

    override fun onUpdateTask(taskId: Int) {
        val intent = Intent(this, Update_Task::class.java)
        intent.putExtra("taskId", taskId)
        startActivity(intent)
    }

    override fun onDeleteTask(taskId: Int) {
        val builder = AlertDialog.Builder(this@Tasks)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete this task?")

        builder.setPositiveButton("Yes") { dialog, which ->
            apiService.deleteTask(taskId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Tasks, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                        fetchTasks()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Tasks", "Failed to delete task: ${response.code()} ${response.message()} $errorBody")
                        Toast.makeText(this@Tasks, "Failed to delete task", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Log the throwable message
                    Log.e("Tasks", "Network error: ${t.message}", t)
                    Toast.makeText(this@Tasks, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
