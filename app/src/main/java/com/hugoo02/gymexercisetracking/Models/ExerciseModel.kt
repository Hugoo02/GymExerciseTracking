package com.hugoo02.gymexercisetracking.Models

import com.hugoo02.gymexercisetracking.Adapters.WorkoutAdapter
import com.hugoo02.gymexercisetracking.ExerciseActivity

class ExerciseModel(
    var id: String? = null,
    var name: String? = null,
    var imageURL: String? = null,
    var userWeight: String? = null,
    var weightList: HashMap<String, String>? = null,) {

    constructor() : this(null, null, null)

    fun toHashMap() : HashMap<String, Any?>{
        val hashMap = HashMap<String, Any?>()
        hashMap["name"]                     = name
        hashMap["imageURL"]                 = imageURL
        hashMap["userWeight"]               = userWeight

        return hashMap
    }

    companion object{
        fun fromHash(hashMap:  HashMap<String, Any>) : ExerciseModel {
            val item = ExerciseModel(
                hashMap["id"].toString(),
                hashMap["name"].toString(),
                hashMap["imageURL"].toString(),
                hashMap["userWeight"].toString(),
            )
            return item
        }
    }

}