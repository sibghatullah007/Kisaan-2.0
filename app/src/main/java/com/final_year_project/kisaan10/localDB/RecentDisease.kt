package com.final_year_project.kisaan10.localDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentDisease")
data class RecentDisease(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val pictureResId: String,
    val symptom: String,
    val treatment: String,
    val prevention: String
)
