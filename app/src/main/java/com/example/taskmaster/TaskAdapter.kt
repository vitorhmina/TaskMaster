package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val taskList: List<Task>,
    private val taskItemClickListener: TaskItemClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface TaskItemClickListener {
        fun onItemClicked(taskId: Int)
        fun onUsersClicked(taskId: Int)
        fun onUpdateTask(taskId: Int)
        fun onDeleteTask(taskId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.taskName)
        private val taskStatusTextView: TextView = itemView.findViewById(R.id.status)
        private val taskEndDateTextView: TextView = itemView.findViewById(R.id.dueDate)

        private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

        fun bind(task: Task) {
            taskNameTextView.text = task.name
            taskStatusTextView.text = task.status
            taskEndDateTextView.text = formatDate(task.plannedEndDate)

            itemView.findViewById<View>(R.id.users_button).setOnClickListener {
                taskItemClickListener.onUsersClicked(task.id)
            }

            itemView.findViewById<View>(R.id.edit_button).setOnClickListener {
                taskItemClickListener.onUpdateTask(task.id)
            }

            itemView.findViewById<View>(R.id.delete_button).setOnClickListener {
                taskItemClickListener.onDeleteTask(task.id)
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
