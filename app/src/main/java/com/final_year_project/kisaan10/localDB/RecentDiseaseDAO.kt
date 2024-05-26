package com.final_year_project.kisaan10.localDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface RecentDiseaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDisease(recentDisease: RecentDisease)
    @Update
    suspend fun updateDisease(recentDisease: RecentDisease)
    @Delete
    suspend fun deleteDisease(recentDisease: RecentDisease)
    @Query("SELECT * FROM recentDisease ORDER BY id DESC")
    fun getDiseases(): LiveData<List<RecentDisease>>
}