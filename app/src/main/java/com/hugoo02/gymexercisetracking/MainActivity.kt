package com.hugoo02.gymexercisetracking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hugoo02.gymexercisetracking.Adapters.UserAdapter
import com.hugoo02.gymexercisetracking.Models.UserModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val recyclerViewUsers = findViewById<RecyclerView>(R.id.recyclerViewUsers)

        getUserList { userList ->
            val adapter = UserAdapter(userList)

            val dividerItemDecoration = DividerItemDecoration(
                recyclerViewUsers.context,
                DividerItemDecoration.VERTICAL
            )
            recyclerViewUsers.addItemDecoration(dividerItemDecoration)

            recyclerViewUsers.layoutManager = LinearLayoutManager(this)
            recyclerViewUsers.adapter = adapter

            adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    val intent = Intent(this@MainActivity, WorkoutActivity::class.java)
                    intent.putExtra("userId", id)
                    intent.putExtra("userName", userList.find { it.id == id }?.name)
                    startActivity(intent)
                }
            })
        }
    }

    private fun getUserList(callback: (MutableList<UserModel>) -> Unit) {
        val db = Firebase.firestore

        val userList = mutableListOf<UserModel>()

        db.collection("Users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val userItem = UserModel.fromHash(document.data as HashMap<String, Any>)
                    userList.add(userItem)
                }
                callback(userList)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}