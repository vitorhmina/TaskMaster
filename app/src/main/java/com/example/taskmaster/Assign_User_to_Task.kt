package com.example.taskmaster

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.RetrofitClient.getApiServiceWithAuth
import com.example.taskmaster.retrofit.Task
import com.example.taskmaster.retrofit.User
import com.example.taskmaster.retrofit.UserProject
import com.example.taskmaster.retrofit.UserTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Assign_User_to_Task : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var taskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assign_to_task)
        apiService = getApiServiceWithAuth(this)
        taskId = intent.getIntExtra("taskId", -1)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            navigateToUserTasksActivity()
        }

        val buttonAssignToProject = findViewById<Button>(R.id.buttonAssignToTask)
        buttonAssignToProject.setOnClickListener {
            AssignToTask()
        }
    }

    private fun navigateToUserTasksActivity() {
        val intent = Intent(this@Assign_User_to_Task, UserTasks::class.java)
        intent.putExtra("taskId", taskId)
        startActivity(intent)
    }

    private fun AssignToTask() {
        val email = findViewById<EditText>(R.id.editTextEmail).text.toString()

        if (taskId == -1) {
            Toast.makeText(this@Assign_User_to_Task, "Invalid task ID", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'", Locale.US)
        val formattedDate = dateFormat.format(currentDate)

        val userTask = UserTask(0, null, email, formattedDate, null, null, null, taskId)
        Log.d(TAG, "User Task creation request: $userTask")

        val call = apiService.createUserTask(userTask)
        call.enqueue(object : Callback<UserTask?> {
            override fun onResponse(call: Call<UserTask?>, response: Response<UserTask?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Assign_User_to_Task, "User assigned successfully", Toast.LENGTH_SHORT).show()
                    navigateToUserTasksActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to assign user: ${response.code()} - $errorBody")
                    Toast.makeText(this@Assign_User_to_Task, "Failed to assign user: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserTask?>, t: Throwable) {
                Toast.makeText(this@Assign_User_to_Task, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    companion object {
        private const val TAG = "Assign_User_To_Task"
    }
}
