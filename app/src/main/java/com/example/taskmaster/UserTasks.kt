package com.example.taskmaster

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.User
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient
import com.example.taskmaster.retrofit.UserProject
import com.example.taskmaster.retrofit.UserTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserTasks : AppCompatActivity(), UserTaskAdapter.UserTaskItemClickListener {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.user_tasks)

        apiService = RetrofitClient.getApiServiceWithAuth(this)

        taskId = intent.getIntExtra("taskId", -1)
        Log.d(TAG, "Received taskId: $taskId")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, Tasks::class.java)
            startActivity(intent)
        }

        val buttonAdd = findViewById<ImageButton>(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this, Assign_User_to_Task::class.java)
            intent.putExtra("taskId", taskId)
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

        fetchTaskTeam()
    }

    private fun fetchTaskTeam() {
        apiService.getTaskUsers(taskId).enqueue(object : Callback<List<UserTask>> {
            override fun onResponse(call: Call<List<UserTask>>, response: Response<List<UserTask>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        val adapter = UserTaskAdapter(it, this@UserTasks)
                        recyclerView.adapter = adapter
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<UserTask>>, t: Throwable) {
            }
        })
    }

    override fun onUpdateUserTask(userTaskId: Int) {
        Log.d(TAG, "Sending userTaskId: $userTaskId")
        val intent = Intent(this, Update_User_Task::class.java)
        intent.putExtra("userTaskId", userTaskId)
        startActivity(intent)
    }

    override fun onDeleteUserTask(userTaskId: Int) {
        val builder = AlertDialog.Builder(this@UserTasks)
        builder.setTitle("Confirm Remove User")
        builder.setMessage("Are you sure you want to remove this user from the task?")

        builder.setPositiveButton("Yes") { dialog, which ->
            apiService.deleteUserTask(userTaskId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UserTasks, "User removed from task successfully", Toast.LENGTH_SHORT).show()
                        fetchTaskTeam()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("Users", "Failed to remove user from task: ${response.code()} ${response.message()} $errorBody")
                        Toast.makeText(this@UserTasks, "Failed to remove user from task", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("UserTasks", "Network error: ${t.message}", t)
                    Toast.makeText(this@UserTasks, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val TAG = "User_Tasks"
    }

}
