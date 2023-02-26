package com.hugoo02.gymexercisetracking.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hugoo02.gymexercisetracking.Models.WorkoutModel
import com.hugoo02.gymexercisetracking.R

class WorkoutAdapter(private var workoutList: List<WorkoutModel>) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_simple, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = workoutList[position]

        holder.textViewName.text = user.name

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(workoutList[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    fun updateList(newList: List<WorkoutModel>) {
        workoutList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)

    }
}