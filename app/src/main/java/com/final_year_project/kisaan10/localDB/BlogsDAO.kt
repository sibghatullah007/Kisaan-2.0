package com.final_year_project.kisaan10.localDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BlogsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlog(blogs: Blogs)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(blogs: List<Blogs>)
    @Update
    suspend fun updateBlog(blogs: Blogs)
    @Delete
    suspend fun deleteBlog(blogs: Blogs)
    @Query("SELECT * FROM blogsTable")
    fun getBlogs(): LiveData<List<Blogs>>
}