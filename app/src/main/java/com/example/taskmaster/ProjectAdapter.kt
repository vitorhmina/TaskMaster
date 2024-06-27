package com.example.taskmaster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.Project
import java.text.SimpleDateFormat
import java.util.*

class ProjectAdapter(
    private val projectList: List<Project>,
    private val listener: ProjectItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

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
        val project = projectList[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val projectNameTextView: TextView = itemView.findViewById(R.id.projectName)
        private val projectStatusTextView: TextView = itemView.findViewById(R.id.status)
        private val projectEndDateTextView: TextView = itemView.findViewById(R.id.dueDate)
        private val projectProgressTextView: TextView = itemView.findViewById(R.id.progress)

        private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

        fun bind(project: Project) {
            projectNameTextView.text = project.name
            projectStatusTextView.text = project.status
            projectEndDateTextView.text = formatDate(project.plannedEndDate)
            projectProgressTextView.text = "${project.completedTasks}/${project.totalTasks}"

            when (project.status) {
                "Not Started" -> {
                    projectStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.user_text))
                    projectStatusTextView.setBackgroundResource(R.drawable.user_bg)
                }
                "In Progress" -> {
                    projectStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.admin_text))
                    projectStatusTextView.setBackgroundResource(R.drawable.admin_bg)
                }
                "Overdue" -> {
                    projectStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.red_text))
                    projectStatusTextView.setBackgroundResource(R.drawable.red_bg)
                }
                "Completed" -> {
                    projectStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.project_manager_text))
                    projectStatusTextView.setBackgroundResource(R.drawable.project_manager_bg)
                }
                else -> {
                    projectStatusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    projectStatusTextView.setBackgroundResource(R.drawable.default_bg)
                }
            }

            itemView.setOnClickListener {
                listener.onItemClicked(project.id)
            }

            itemView.findViewById<View>(R.id.users_button).setOnClickListener {
                listener.onUsersClicked(project.id)
            }

            itemView.findViewById<View>(R.id.edit_button).setOnClickListener {
                listener.onUpdateProject(project.id)
            }

            itemView.findViewById<View>(R.id.delete_button).setOnClickListener {
                listener.onDeleteProject(project.id)
            }
        }

        private fun formatDate(dateString: String?): String {
            return if (dateString != null) {
                try {
                    val date = isoDateFormat.parse(dateString)
                    date?.let {
                        SimpleDateFormat("MMM dd, yyyy", Locale.US).format(it)
                    } ?: "Invalid date"
                } catch (e: Exception) {
                    "Invalid date"
                }
            } else {
                "No date"
            }
        }
    }
}
