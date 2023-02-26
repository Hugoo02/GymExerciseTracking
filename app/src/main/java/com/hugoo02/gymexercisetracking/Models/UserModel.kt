package com.hugoo02.gymexercisetracking.Models

class UserModel(
    val id: String? = null,
    val name: String? = null,
    val workouts: List<String>? = null,) {

    constructor() : this(null, null, null)

    fun toHashMap() : HashMap<String, Any?>{
        val hashMap = HashMap<String, Any?>()
        hashMap["id"]                   = id
        hashMap["name"]                 = name
        hashMap["workouts"]             = workouts

        return hashMap
    }

    companion object{
        fun fromHash(hashMap:  HashMap<String, Any>) : UserModel {
            val item = UserModel(
                hashMap["id"].toString(),
                hashMap["name"].toString(),
                hashMap["workouts"] as List<String>,
            )
            return item
        }
    }

}