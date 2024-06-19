package com.example.taskmaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.Project
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class Projects : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.projects)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch projects from API
        fetchProjects()
    }

    private fun fetchProjects() {
        apiService.getUserProjects().enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    val projectList = response.body()
                    if (projectList != null) {
                        val adapter = ProjectAdapter(projectList)
                        recyclerView.adapter = adapter
                    } else {
                        Log.e("Projects", "Project list is null or empty")
                        // Handle null or empty case if necessary
                    }
                } else {
                    Log.e("Projects", "Failed to fetch projects: ${response.errorBody()?.string()}")
                    // Handle error response if necessary
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.e("Projects", "API call failed", t)
                // Handle network errors or API call failures
                // Show appropriate error message to the user
            }
        })
    }
}
