package com.hugoo02.gymexercisetracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoo02.gymexercisetracking.Adapters.ExerciseAdapter
import com.hugoo02.gymexercisetracking.Models.ExerciseModel
import com.hugoo02.gymexercisetracking.Models.WorkoutModel

class ExerciseActivity : AppCompatActivity() {

    var exerciseListInfo : MutableList<ExerciseModel> = mutableListOf()
    var adapter : ExerciseAdapter? = null
    var workoutId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val recyclerViewExercises = findViewById<RecyclerView>(R.id.recyclerViewExercises)
        val buttonSave = findViewById<FloatingActionButton>(R.id.buttonSave)
        val textViewUsername = findViewById<TextView>(R.id.textViewUsername)

        val userId = intent.getStringExtra("userId")
        workoutId = intent.getStringExtra("workoutId")
        val userName = intent.getStringExtra("userName")

        textViewUsername.text = userName

        buttonSave.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            for (i in 0 until adapter!!.itemCount) {
                    val exercise = exerciseListInfo[i]

                db.collection("Exercises").document(exercise.id!!)
                    .update("weight", exercise.weightList)
                    .addOnSuccessListener {
                        Log.d("Exercises", "${exercise.name} updated with new weight: ")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Exercises", "Error updating document", e)
                    }
            }

            Toast.makeText(this, "Peso foi salvo morcäo!", Toast.LENGTH_SHORT).show()
        }

        getExercisesList(workoutId!!) { exercises ->
            getExercisesListInfo(userId!!, exercises) { exerciseListInfo ->
                adapter = ExerciseAdapter(userId, exerciseListInfo)

                val dividerItemDecoration = DividerItemDecoration(
                    recyclerViewExercises.context,
                    DividerItemDecoration.VERTICAL
                )
                recyclerViewExercises.addItemDecoration(dividerItemDecoration)

                recyclerViewExercises.layoutManager = LinearLayoutManager(this)
                recyclerViewExercises.adapter = adapter

            }
        }

    }

    private fun getExercisesList(workoutId: String, callback: (List<String>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Workouts")
            .document(workoutId)
            .get()
            .addOnSuccessListener { result ->
                val workout = WorkoutModel.fromHash(result.data as HashMap<String, Any>)
                callback(workout.exercises!!)
            }
            .addOnFailureListener { exception ->
                Log.w("ExerciseActivity", "Error getting documents: ", exception)
            }
    }

    private fun getExercisesListInfo(userId: String, exerciseList: List<String>, callback: (List<ExerciseModel>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        val requests = exerciseList.map { exerciseId ->
            db.collection("Exercises")
                .document(exerciseId)
                .get()
                .addOnSuccessListener { result ->
                    val weight = result.get("weight") as HashMap<String, String>
                    val userWeight = weight[userId]
                    val exercise = result.toObject(ExerciseModel::class.java)
                    if (exercise != null) {
                        exercise.userWeight = userWeight
                        exercise.weightList = weight
                        exerciseListInfo.add(exercise)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("ExerciseActivity", "Error getting documents: ", exception)
                }
        }

        Tasks.whenAllComplete(requests).addOnCompleteListener {
            exerciseListInfo = sortExerciseList(exerciseListInfo)
            callback(exerciseListInfo)
        }
    }

    private fun sortExerciseList(exerciseListInfo: MutableList<ExerciseModel>): MutableList<ExerciseModel> {
        val workout = exerciseIdToName[workoutId]

        when (workout){
            "Legs" -> {
                val exerciseOrder = mapOf(
                    "Jz6MKAK19kvUbmRLYm5t" to 1,
                    "7UpKsJVyIkxuNW25SIei" to 2,
                    "C9OYDsedXPsd1SRJ54Ke" to 3,
                    "y1XaIC3T9A3gkiwgF9V8" to 4,
                    "XUMhNj3dwWk9AkBqDoCV" to 5,
                    "oWtbGRIQSQTPFIeq8k42" to 6
                )
                exerciseListInfo.sortBy { exerciseOrder[it.id] }
            }
            "Pull" -> {
                val exerciseOrder = mapOf(
                    "VLFw77OVaYFuyey8uPQN" to 1,
                    "ZCJRh4YxcByXzCJWmIaW" to 2,
                    "XePQdv6wi5ZZLvcJqnwe" to 3,
                    "H9dOPccTWhZNKztKXST2" to 4,
                    "c34WrY6LAfiVa1NVS9Y0" to 5,
                    "NDcr0cmoxirfaomqoLkZ" to 6,
                    "q5Afo45GKfMyCLfbu3wY" to 7,
                    "oWtbGRIQSQTPFIeq8k42" to 8
                )
                exerciseListInfo.sortBy { exerciseOrder[it.id] }
            }
            "Push" -> {

                val exerciseOrder = mapOf(
                    "4z3yJsivWXuqIQxjdhKv" to 1,
                    "hiJUgqERsczuEbTlSiwF" to 2,
                    "LmdiE8wnz0ylGXSVwYgv" to 3,
                    "Ckn58GkfpN7nGsRm0tzs" to 4,
                    "o8OxScLEmQLv8PlCC4oL" to 5,
                    "xSUuxHaXSz20sneap5CU" to 6,
                    "oWtbGRIQSQTPFIeq8k42" to 7
                )
                exerciseListInfo.sortBy { exerciseOrder[it.id] }
            }
        }

        return exerciseListInfo
    }

    companion object{
        val exerciseIdToName = mapOf(
            "0Ifd7rT6KJCf41ge1nrS" to "Legs",
            "CwE0eUPINQdDSOygtY8p" to "Pull",
            "RT0mH8nwYDes6t5r0rrl" to "Push",
        )
    }

}

