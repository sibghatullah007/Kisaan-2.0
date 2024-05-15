package com.final_year_project.kisaan10.localDB

import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blogsTable")
data class Blogs(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val picture: String,
    val symptom: String,
    val treatment: String,
    val prevention: String
)
