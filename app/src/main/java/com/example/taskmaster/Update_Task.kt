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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Update_Task : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var projectId = 0
    private var taskId = 0

    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_task)
        apiService = getApiServiceWithAuth(this)
        taskId = intent.getIntExtra("taskId", -1)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this@Update_Task, Tasks::class.java)
            intent.putExtra("projectId", projectId)
            startActivity(intent)
        }

        val spinnerStatus = findViewById<Spinner>(R.id.spinnerOptions)
        val status = arrayOf("Not Started", "In Progress", "Completed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, status)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter
        spinnerStatus.setSelection(0)

        setupDatePicker(findViewById(R.id.startDateEditText))
        setupDatePicker(findViewById(R.id.endDateEditText))
        setupDatePicker(findViewById(R.id.actualEndDateEditText))

        apiService.getTaskById(taskId).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val task = response.body()
                    if (task != null) {
                        findViewById<EditText>(R.id.editTextName).setText(task.name)
                        findViewById<EditText>(R.id.editTextDescription).setText(task.description)
                        findViewById<EditText>(R.id.startDateEditText).setText(formatDateForDisplay(task.startDate))
                        findViewById<EditText>(R.id.endDateEditText).setText(formatDateForDisplay(task.plannedEndDate))
                        findViewById<EditText>(R.id.actualEndDateEditText).setText(formatDateForDisplay(task.actualEndDate))

                        val statusIndex = status.indexOf(task.status)
                        spinnerStatus.setSelection(statusIndex)

                        projectId = task.projectId
                    } else {
                        Toast.makeText(this@Update_Task, "Task not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Update_Task, "Failed to fetch task details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Toast.makeText(this@Update_Task, "Network error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to fetch task details", t)
            }
        })

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdateTask)
        buttonUpdate.setOnClickListener {
            updateTask()
        }
    }

    private fun navigateToTasksActivity() {
        val intent = Intent(this@Update_Task, Tasks::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    private fun updateTask() {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
        val startDateString = findViewById<EditText>(R.id.startDateEditText).text.toString()
        val endDateString = findViewById<EditText>(R.id.endDateEditText).text.toString()
        val actualEndDateString = findViewById<EditText>(R.id.actualEndDateEditText).text.toString()
        val statusSelected = findViewById<Spinner>(R.id.spinnerOptions).selectedItem.toString()

        val startDate = parseDate(startDateString)
        val endDate = parseDate(endDateString)
        val actualEndDate = parseDate(actualEndDateString)

        if (startDate == null || endDate == null) {
            Toast.makeText(this@Update_Task, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        val isoStartDate = startDate?.let { formatDateToISO(it) }
        val isoEndDate = endDate?.let { formatDateToISO(it) }
        val isoActualEndDate = actualEndDate?.let { formatDateToISO(it) }

        val task = Task(taskId, name, description, isoStartDate!!, isoEndDate!!, isoActualEndDate, statusSelected, projectId)
        Log.d(TAG, "Task update request: $task")

        val call = apiService.updateTask(taskId, task)
        call.enqueue(object : Callback<Task?> {
            override fun onResponse(call: Call<Task?>, response: Response<Task?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Update_Task, "Task updated successfully", Toast.LENGTH_SHORT).show()
                    navigateToTasksActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update task: ${response.code()} - $errorBody")
                    Toast.makeText(this@Update_Task, "Failed to update task: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Task?>, t: Throwable) {
                Toast.makeText(this@Update_Task, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    private fun parseDate(dateString: String): Date? {
        return try {
            displayDateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e(TAG, "Error parsing date: $dateString", e)
            null
        }
    }

    private fun formatDateToISO(date: Date): String {
        return isoDateFormat.format(date)
    }

    private fun formatDateForDisplay(dateString: String?): String {
        return try {
            if (dateString != null) {
                val isoDate = isoDateFormat.parse(dateString)
                displayDateFormat.format(isoDate)
            } else {
                ""
            }
        } catch (e: ParseException) {
            Log.e(TAG, "Error formatting date for display: $dateString", e)
            ""
        }
    }

    private fun setupDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                editText.setText(displayDateFormat.format(calendar.time))
            }
        editText.setOnClickListener {
            DatePickerDialog(
                this@Update_Task, dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    companion object {
        private const val TAG = "Update_Task"
    }
}
