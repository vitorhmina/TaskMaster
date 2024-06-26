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

class Create_Task : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var projectId = 0

    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task)
        apiService = getApiServiceWithAuth(this)
        projectId = intent.getIntExtra("projectId", -1)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            navigateToTasksActivity()
        }

        val spinnerStatus = findViewById<Spinner>(R.id.spinnerOptions)
        val status = arrayOf("Not Started", "In Progress", "Completed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, status)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter
        spinnerStatus.setSelection(0)

        val buttonCreateTask = findViewById<Button>(R.id.buttonCreateTask)
        buttonCreateTask.setOnClickListener {
            createTask()
        }

        setupDatePicker(findViewById(R.id.startDateEditText))
        setupDatePicker(findViewById(R.id.endDateEditText))
    }

    private fun navigateToTasksActivity() {
        val intent = Intent(this@Create_Task, Tasks::class.java)
        intent.putExtra("projectId", projectId)
        startActivity(intent)
    }

    private fun createTask() {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
        val startDateString = findViewById<EditText>(R.id.startDateEditText).text.toString()
        val endDateString = findViewById<EditText>(R.id.endDateEditText).text.toString()
        val statusSelected = findViewById<Spinner>(R.id.spinnerOptions).selectedItem.toString()

        val startDate = parseDate(startDateString)
        val endDate = parseDate(endDateString)

        if (startDate == null || endDate == null) {
            Toast.makeText(this@Create_Task, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        val isoStartDate = formatDateToISO(startDate)
        val isoEndDate = formatDateToISO(endDate)

        if (projectId == -1) {
            Toast.makeText(this@Create_Task, "Invalid project ID", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(0, name, description, isoStartDate, isoEndDate, null, statusSelected, projectId)
        Log.d(TAG, "Task creation request: $task")

        val call = apiService.createTask(task)
        call.enqueue(object : Callback<Task?> {
            override fun onResponse(call: Call<Task?>, response: Response<Task?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Create_Task, "Task created successfully", Toast.LENGTH_SHORT).show()
                    navigateToTasksActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to create task: ${response.code()} - $errorBody")
                    Toast.makeText(this@Create_Task, "Failed to create task: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Task?>, t: Throwable) {
                Toast.makeText(this@Create_Task, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                this@Create_Task, dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    companion object {
        private const val TAG = "Create_Task"
    }
}
