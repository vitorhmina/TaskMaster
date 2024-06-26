package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.Project
import android.util.Log
import android.widget.ImageView
import java.util.*
import java.text.SimpleDateFormat


class ProjectAdapter(private val projectList: List<Project>, private val listener: ProjectItemClickListener) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    private val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    interface ProjectItemClickListener {
        fun onItemClicked(projectId: Int)
        fun onUsersClicked(projectId: Int)
        fun onUpdateProject(projectId: Int)
        fun onDeleteProject(projectId: Int)
    }

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

        // Set a click listener for the entire card view
        holder.itemView.setOnClickListener {
            listener.onItemClicked(currentProject.id)
        }

        // Set a click listener for the entire card view
        holder.usersIcon.setOnClickListener {
            listener.onUsersClicked(currentProject.id)
        }

        // Handle update project click
        holder.updateIcon.setOnClickListener {
            listener.onUpdateProject(currentProject.id)
        }

        // Handle delete project click
        holder.deleteIcon.setOnClickListener {
            listener.onDeleteProject(currentProject.id)
        }
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.projectName)
        val dueDate: TextView = itemView.findViewById(R.id.dueDate)
        val status: TextView = itemView.findViewById(R.id.status)
        val progress: TextView = itemView.findViewById(R.id.progress)
        val usersIcon: ImageView = itemView.findViewById(R.id.users_button)
        val updateIcon: ImageView = itemView.findViewById(R.id.edit_button)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_button)

    }
}
