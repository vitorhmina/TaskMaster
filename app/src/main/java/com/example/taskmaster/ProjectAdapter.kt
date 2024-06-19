package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.Project
import android.util.Log
import java.util.*
import java.text.SimpleDateFormat


class ProjectAdapter(private val projectList: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    private val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentProject = projectList[position]
        holder.projectName.text = currentProject.name

        Log.d("ProjectAdapter", "Project: ${currentProject.name}, Planned End Date: ${currentProject.plannedEndDate}")
        holder.dueDate.text = "Due: ${displayDateFormat.format(currentProject.plannedEndDate)}"
        holder.status.text = currentProject.status
        holder.progress.text = "${currentProject.completedTasks}/${currentProject.totalTasks}"
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val dueDate: TextView = itemView.findViewById(R.id.dueDate)
        val status: TextView = itemView.findViewById(R.id.status)
        val progress: TextView = itemView.findViewById(R.id.progress)
    }
}
