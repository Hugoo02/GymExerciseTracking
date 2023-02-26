package com.hugoo02.gymexercisetracking.Models

import com.hugoo02.gymexercisetracking.Adapters.WorkoutAdapter

class WorkoutModel(
    val id: String? = null,
    val name: String? = null,
    val exercises: List<String>? = null,) {

    constructor() : this(null, null, null)

    fun toHashMap() : HashMap<String, Any?>{
        val hashMap = HashMap<String, Any?>()
        hashMap["id"]                       = id
        hashMap["name"]                     = name
        hashMap["exercises"]                = exercises

        return hashMap
    }

    companion object{
        fun fromHash(hashMap:  HashMap<String, Any>) : WorkoutModel {
            val item = WorkoutModel(
                hashMap["id"].toString(),
                hashMap["name"].toString(),
                hashMap["exercises"] as List<String>,
            )
            return item
        }
    }

}