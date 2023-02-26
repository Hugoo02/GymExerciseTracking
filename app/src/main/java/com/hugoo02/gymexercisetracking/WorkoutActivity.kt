package com.hugoo02.gymexercisetracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hugoo02.gymexercisetracking.Adapters.ExerciseAdapter
import com.hugoo02.gymexercisetracking.Adapters.UserAdapter
import com.hugoo02.gymexercisetracking.Adapters.WorkoutAdapter
import com.hugoo02.gymexercisetracking.Models.UserModel
import com.hugoo02.gymexercisetracking.Models.WorkoutModel

class WorkoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")

        val textViewUsername = findViewById<TextView>(R.id.textViewUsername)

        textViewUsername.text = userName

        getWorkoutsList(userId!!) { workouts ->
            getWorkoutListInfo(workouts) { workoutListInfo ->

                val recyclerViewWorkouts = findViewById<RecyclerView>(R.id.recyclerViewWorkouts)

                val adapter = WorkoutAdapter(workoutListInfo)

                val dividerItemDecoration = DividerItemDecoration(
                    recyclerViewWorkouts.context,
                    DividerItemDecoration.VERTICAL
                )
                recyclerViewWorkouts.addItemDecoration(dividerItemDecoration)


                recyclerViewWorkouts.layoutManager = LinearLayoutManager(this)
                recyclerViewWorkouts.adapter = adapter

                adapter.setOnItemClickListener(object : WorkoutAdapter.OnItemClickListener {
                    override fun onItemClick(id: String) {
                        val intent = Intent(this@WorkoutActivity, ExerciseActivity::class.java)
                        intent.putExtra("userId", userId)
                        intent.putExtra("workoutId", id)
                        intent.putExtra("userName", userName)
                        startActivity(intent)
                    }
                })
            }
        }

    }

    private fun getWorkoutListInfo(workoutList: List<String>, callback: (List<WorkoutModel>) -> Unit) {
        val db = Firebase.firestore

        val workoutListInfo = mutableListOf<WorkoutModel>()

        val requests = workoutList.map { workoutId ->
            db.collection("Workouts")
                .document(workoutId)
                .get()
                .addOnSuccessListener { result ->
                    val workout = WorkoutModel.fromHash(result.data as HashMap<String, Any>)
                    workoutListInfo.add(workout)
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        }

        Tasks.whenAllComplete(requests).addOnSuccessListener {
            callback(workoutListInfo)
        }
    }

    private fun getWorkoutsList(userId: String, callback: (List<String>) -> Unit) {
        val db = Firebase.firestore

        db.collection("Users")
            .document(userId)
            .get()
            .addOnSuccessListener { result ->
                val user = UserModel.fromHash(result.data as HashMap<String, Any>)
                callback(user.workouts!!)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}