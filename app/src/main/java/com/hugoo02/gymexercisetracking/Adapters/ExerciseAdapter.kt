package com.hugoo02.gymexercisetracking.Adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoo02.gymexercisetracking.Models.ExerciseModel
import com.hugoo02.gymexercisetracking.R

class ExerciseAdapter(private var userId: String, private var exerciseList: List<ExerciseModel>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_exercise, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exerciseList[position]

        holder.editTextExercise.setText(exercise.userWeight.toString())
        holder.textViewExercise.text = exercise.name

        // set the exercise imageUrl to the imageView with Glide
        Glide.with(holder.imageViewExercise.context)
            .load(exercise.imageURL)
            .into(holder.imageViewExercise)

        holder.editTextExercise.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                exercise.weightList?.set(userId, s.toString())
            }
        })


    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    fun updateList(newList: List<ExerciseModel>) {
        exerciseList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewExercise: ImageView = itemView.findViewById(R.id.imageViewExercise)
        val editTextExercise: EditText = itemView.findViewById(R.id.editTextExercise)
        val textViewExercise: TextView = itemView.findViewById(R.id.textViewExercise)

    }
}