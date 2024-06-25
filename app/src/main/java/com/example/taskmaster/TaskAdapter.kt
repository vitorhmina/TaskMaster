package com.example.taskmaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.retrofit.Task
import android.util.Log
import java.util.*
import java.text.SimpleDateFormat


class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val displayDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.taskName.text = currentTask.name

        Log.d("TaskAdapter", "Task: ${currentTask.name}, Planned End Date: ${currentTask.plannedEndDate}")
        holder.dueDate.text = "Due: ${displayDateFormat.format(currentTask.plannedEndDate)}"
        holder.status.text = currentTask.status
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val dueDate: TextView = itemView.findViewById(R.id.dueDate)
        val status: TextView = itemView.findViewById(R.id.status)
    }
}
