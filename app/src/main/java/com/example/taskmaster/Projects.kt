package com.example.taskmaster
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.retrofit.Project
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Projects: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.projects)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create a list of Project objects
        val projectList = mutableListOf<Project>()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        projectList.add(Project(
            id = 1,
            name = "Project A",
            description = "A description",
            startDate = dateFormat.parse("01-01-2024"),
            plannedEndDate = dateFormat.parse("30-06-2024"),
            endDate = null,
            status = "Active"
        ))
        projectList.add(Project(
            id = 2,
            name = "Project B",
            description = "B description",
            startDate = dateFormat.parse("15-01-2024"),
            plannedEndDate = dateFormat.parse("15-07-2024"),
            endDate = null,
            status = "Active"
        ))
        projectList.add(Project(
            id = 3,
            name = "Project C",
            description = "C description",
            startDate = dateFormat.parse("05-02-2024"),
            plannedEndDate = dateFormat.parse("05-08-2024"),
            endDate = null,
            status = "Completed"
        ))

        // Create an adapter and set it to the RecyclerView
        val adapter = ProjectAdapter(projectList)
        recyclerView.adapter = adapter

    }
}
