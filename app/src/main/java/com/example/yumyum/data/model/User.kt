package com.example.yumyum.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = false) val username:String,
    val password:String,
    val name:String
)
