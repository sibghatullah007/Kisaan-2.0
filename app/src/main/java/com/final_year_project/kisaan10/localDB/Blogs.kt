package com.final_year_project.kisaan10.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blogsTable")
data class Blogs(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val pictureResId: Int,
    val symptom: String,
    val treatment: String,
    val prevention: String
)
