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

class Update_Project : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var projectId = 0

    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_project)
        apiService = getApiServiceWithAuth(this)

        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this@Update_Project, Projects::class.java)
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

        apiService.getProjectById(projectId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val project = response.body()
                    if (project != null) {
                        // Populate EditText fields with task data
                        findViewById<EditText>(R.id.editTextName).setText(project.name)
                        findViewById<EditText>(R.id.editTextDescription).setText(project.description)
                        findViewById<EditText>(R.id.startDateEditText).setText(formatDateForDisplay(project.startDate))
                        findViewById<EditText>(R.id.endDateEditText).setText(formatDateForDisplay(project.plannedEndDate))
                        findViewById<EditText>(R.id.actualEndDateEditText).setText(formatDateForDisplay(project.actualEndDate))

                        val statusIndex = status.indexOf(project.status)
                        spinnerStatus.setSelection(statusIndex)
                    } else {
                        Toast.makeText(this@Update_Project, "Project not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Update_Project, "Failed to fetch project details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Toast.makeText(this@Update_Project, "Network error", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to fetch project details", t)
            }
        })

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdateProject)
        buttonUpdate.setOnClickListener {
            updateProject()
        }
    }

    private fun navigateToProjectsActivity() {
        val intent = Intent(this@Update_Project, Projects::class.java)
        startActivity(intent)
    }

    private fun updateProject() {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
        val startDateString = findViewById<EditText>(R.id.startDateEditText).text.toString()
        val endDateString = findViewById<EditText>(R.id.endDateEditText).text.toString()
        val actualEndDateString = findViewById<EditText>(R.id.actualEndDateEditText).text.toString()
        val statusSelected = findViewById<Spinner>(R.id.spinnerOptions).selectedItem.toString()

        val startDate = parseDate(startDateString)
        val endDate = parseDate(endDateString)
        val actualEndDate = parseDate(actualEndDateString)

        // Check if any of the parsed dates are null
        if (startDate == null || endDate == null) {
            Toast.makeText(this@Update_Project, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        // Format non-null dates to ISO format
        val isoStartDate = startDate?.let { formatDateToISO(it) }
        val isoEndDate = endDate?.let { formatDateToISO(it) }
        val isoActualEndDate = actualEndDate?.let { formatDateToISO(it) }

        val project = Project(projectId, name, description, isoStartDate!!, isoEndDate!!, isoActualEndDate, statusSelected, null, null)
        Log.d(TAG, "Project update request: $project")

        // Make API call to update task
        val call = apiService.updateProject(projectId, project)
        call.enqueue(object : Callback<Project?> {
            override fun onResponse(call: Call<Project?>, response: Response<Project?>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Update_Project, "Project updated successfully", Toast.LENGTH_SHORT).show()
                    navigateToProjectsActivity()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update project: ${response.code()} - $errorBody")
                    Toast.makeText(this@Update_Project, "Failed to update project: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Project?>, t: Throwable) {
                Toast.makeText(this@Update_Project, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                this@Update_Project, dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    companion object {
        private const val TAG = "Update_Project"
    }
}
