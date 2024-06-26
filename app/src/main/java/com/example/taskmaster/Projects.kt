package com.example.taskmaster

import android.content.Intent
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
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Projects : AppCompatActivity(), ProjectAdapter.ProjectItemClickListener {
    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.projects)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchProjects()

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Create_Project::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.projects).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
        findViewById<View>(R.id.home).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
        findViewById<View>(R.id.profile).setOnClickListener {
            BottomNavigationHandler.handleNavigationClicks(this, it)
        }
    }

    private fun fetchProjects() {
        apiService.getUserProjects().enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    val projectList = response.body()
                    projectList?.let {
                        val adapter = ProjectAdapter(it, this@Projects, this@Projects)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("Projects", "Failed to fetch projects: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                Log.e("Projects", "API call failed", t)
            }
        })
    }

    override fun onItemClicked(projectId: Int) {
        val intent = Intent(this, Tasks::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    override fun onUsersClicked(projectId: Int) {
        val intent = Intent(this, UserProjects::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    override fun onUpdateProject(projectId: Int) {
        val intent = Intent(this, Update_Project::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    override fun onDeleteProject(projectId: Int) {
        val builder = AlertDialog.Builder(this@Projects)
        builder.setTitle(getString(R.string.confirm_delete))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_project))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            apiService.deleteProject(projectId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Projects,
                            getString(R.string.project_deleted_successfully), Toast.LENGTH_SHORT).show()
                        fetchProjects()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Projects", "Failed to delete project: ${response.code()} ${response.message()} $errorBody")
                        Toast.makeText(this@Projects,
                            getString(R.string.failed_to_delete_project), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("Projects", "Network error: ${t.message}", t)
                    Toast.makeText(this@Projects, getString(R.string.no_internet_2), Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
