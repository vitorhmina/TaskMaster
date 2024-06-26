package com.example.taskmaster

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.retrofit.ApiService
import com.example.taskmaster.retrofit.Project
import com.example.taskmaster.retrofit.RetrofitClient.getApiServiceWithAuth
import com.example.taskmaster.retrofit.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Create_Project : AppCompatActivity() {
    private lateinit var apiService: ApiService

    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_project)
        apiService = getApiServiceWithAuth(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            navigateToProjectsActivity()
        }

        val spinnerStatus = findViewById<Spinner>(R.id.spinnerOptions)
        val status = arrayOf("Not Started", "In Progress", "Completed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, status)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter
        spinnerStatus.setSelection(0)

        val buttonCreateProject = findViewById<Button>(R.id.buttonCreateProject)
        buttonCreateProject.setOnClickListener {
            createProject()
        }

        setupDatePicker(findViewById(R.id.startDateEditText))
        setupDatePicker(findViewById(R.id.endDateEditText))
    }

    private fun navigateToProjectsActivity() {
        val intent = Intent(this@Create_Project, Projects::class.java)
        startActivity(intent)
    }

    private fun createProject() {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
        val startDateString = findViewById<EditText>(R.id.startDateEditText).text.toString()
        val endDateString = findViewById<EditText>(R.id.endDateEditText).text.toString()
        val statusSelected = findViewById<Spinner>(R.id.spinnerOptions).selectedItem.toString()

        val startDate = parseDate(startDateString)
        val endDate = parseDate(endDateString)

        if (startDate == null || endDate == null) {
            Toast.makeText(this@Create_Project, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        val isoStartDate = formatDateToISO(startDate)
        val isoEndDate = formatDateToISO(endDate)

        val project = Project(0, name, description, isoStartDate, isoEndDate, null, statusSelected, null, null)
        Log.d(TAG, "Project creation request: $project")

        val call = apiService.createProject(project)
        call.enqueue(object : Callback<Project?> {
            override fun onResponse(call: Call<Project?>, response: Response<Project?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Create_Project, "Project created successfully", Toast.LENGTH_SHORT).show()
                    navigateToProjectsActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to create project: ${response.code()} - $errorBody")
                    Toast.makeText(this@Create_Project, "Failed to create project: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Project?>, t: Throwable) {
                Toast.makeText(this@Create_Project, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                this@Create_Project, dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    companion object {
        private const val TAG = "Create_Project"
    }
}
