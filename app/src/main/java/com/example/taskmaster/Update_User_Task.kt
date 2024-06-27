package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient.getApiServiceWithAuth
import com.example.taskmaster.retrofit.UserProject
import com.example.taskmaster.retrofit.UserTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Update_User_Task : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var taskId = 0
    private var email = ""
    private var userTaskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_assign_to_task)
        apiService = getApiServiceWithAuth(this)
        userTaskId = intent.getIntExtra("userTaskId", -1)
        Log.d(TAG, "Received userTaskId: $userTaskId")

        fetchUserTaskDetails()

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            Log.d(TAG, "Sending taskId: $taskId")
            val intent = Intent(this@Update_User_Task, UserTasks::class.java)
            intent.putExtra("taskId", taskId)
            startActivity(intent)
        }

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdateUserTask)
        buttonUpdate.setOnClickListener {
            updateUserTask()
        }
    }

    private fun fetchUserTaskDetails() {
        apiService.getUserTaskById(userTaskId).enqueue(object : Callback<UserTask> {
            override fun onResponse(call: Call<UserTask>, response: Response<UserTask>) {
                if (response.isSuccessful) {
                    val userTask = response.body()
                    if (userTask != null) {
                        findViewById<EditText>(R.id.editTextLocation).setText(userTask.location)
                        findViewById<EditText>(R.id.editTextCompletionRate).setText(userTask.completionRate)
                        findViewById<EditText>(R.id.editTextTimeSpent).setText(userTask.timeSpent)


                        email = userTask.email ?: ""
                        taskId = userTask.taskId
                    } else {
                        Toast.makeText(
                            this@Update_User_Task,
                            "User Task not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@Update_User_Task,
                        "Failed to fetch User Task details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserTask>, t: Throwable) {
                Toast.makeText(this@Update_User_Task, "Network error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to fetch User Task details", t)
            }
        })
    }

    private fun navigateToUserTasksActivity() {
        val intent = Intent(this@Update_User_Task, UserTasks::class.java)
        intent.putExtra("taskId", taskId)
        startActivity(intent)
    }

    private fun updateUserTask() {
        val location = findViewById<EditText>(R.id.editTextLocation).text.toString()
        val completionRate = findViewById<EditText>(R.id.editTextCompletionRate).text.toString()
        val timeSpent = findViewById<EditText>(R.id.editTextTimeSpent).text.toString()


        if (email.isBlank()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'", Locale.US)
        val formattedDate = dateFormat.format(currentDate)

        val userTask = UserTask(
            id = userTaskId,
            name = null,
            email = email,
            date = formattedDate,
            location = location,
            completionRate = completionRate,
            timeSpent = timeSpent,
            taskId = taskId
        )

        Log.d(TAG, "User Task update request: $userTask")

        // Make API call to update user project
        apiService.updateUserTask(userTaskId, userTask).enqueue(object : Callback<UserTask?> {
            override fun onResponse(call: Call<UserTask?>, response: Response<UserTask?>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@Update_User_Task,
                        "User Task updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToUserTasksActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update User Task: ${response.code()} - $errorBody")
                    Toast.makeText(
                        this@Update_User_Task,
                        "Failed to update User Task: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserTask?>, t: Throwable) {
                Toast.makeText(
                    this@Update_User_Task,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    companion object {
        private const val TAG = "Update_User_Task"
    }
}
