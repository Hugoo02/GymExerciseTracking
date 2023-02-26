package com.hugoo02.gymexercisetracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoo02.gymexercisetracking.Adapters.ItemListSelectAdapter
import com.hugoo02.gymexercisetracking.Adapters.WorkoutAdapter
import com.hugoo02.gymexercisetracking.Models.SelectItemModel

class AddUserActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var rvWorkoutList: RecyclerView
    private lateinit var buttonCreateUser: Button
    private lateinit var workoutAdapter: ItemListSelectAdapter
    private val itemList : MutableList<SelectItemModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        editTextUsername = findViewById(R.id.editTextUsername)
        rvWorkoutList = findViewById(R.id.rvWorkoutList)
        buttonCreateUser = findViewById(R.id.buttonCreateUser)

        getWorkoutList()

        buttonCreateUser.setOnClickListener {
            val username = editTextUsername.text.toString()
            if (username.isNotEmpty()) {
                val selectedWorkouts = itemList.filter { it.isSelected }

                addUserToDatabase(username, selectedWorkouts)
                val selectedWorkoutNames = selectedWorkouts.map { it.name }
                val message = "Username: $username\nSelected workouts: ${selectedWorkoutNames.joinToString(", ")}"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUserToDatabase(username: String, selectedWorkouts: List<SelectItemModel>) {

        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "name" to username,
            "workouts" to selectedWorkouts.map { it.id }
        )

        // Add the user data to the database
        db.collection("Users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                // Get the generated ID from Firestore and update the user document with it
                val userId = documentReference.id
                val updatedUser = hashMapOf(
                    "id" to userId,
                    "name" to username,
                    "workouts" to selectedWorkouts.map { it.id }
                )

                db.collection("Users")
                    .document(userId)
                    .set(updatedUser)
                    .addOnSuccessListener {
                        Log.d("Users", "DocumentSnapshot added with ID: $userId")

                        // Update the weight of all exercises in the selected workouts
                        for (workout in selectedWorkouts) {
                            db.collection("Workouts")
                                .document(workout.id)
                                .get()
                                .addOnSuccessListener { workoutSnapshot ->
                                    val exercises = workoutSnapshot.get("exercises") as List<String>
                                    for (exerciseId in exercises) {
                                        val exerciseRef = db.collection("Exercises").document(exerciseId)
                                        exerciseRef.get()
                                            .addOnSuccessListener { exerciseSnapshot ->
                                                val weightMap = exerciseSnapshot.get("weight") as HashMap<String, String>
                                                weightMap[userId] = "0"
                                                exerciseRef.update("weight", weightMap)
                                                    .addOnSuccessListener {
                                                        Log.d("Exercises", "Exercise weight updated for user $userId")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.w("Exercises", "Error updating exercise weight for user $userId", e)
                                                    }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("Exercises", "Error getting exercise with ID $exerciseId", e)
                                            }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Workouts", "Error getting workout with ID ${workout.id}", e)
                                }
                        }

                        // Finish the activity once all requests have completed
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Users", "Error updating document with ID: $userId", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("Users", "Error adding document", e)
            }
    }

    private fun getWorkoutList() {

        val db = FirebaseFirestore.getInstance()

        db.collection("Workouts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // get the workout name
                    val workoutName = document.data["name"] as String
                    itemList.add(SelectItemModel(document.id, workoutName, false))
                }

                workoutAdapter = ItemListSelectAdapter(itemList)
                rvWorkoutList.adapter = workoutAdapter

                rvWorkoutList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = workoutAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w("Workouts", "Error getting documents.", exception)
            }
    }
}