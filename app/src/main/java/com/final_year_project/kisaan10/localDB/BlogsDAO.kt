package com.final_year_project.kisaan10.localDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BlogsDAO {
    @Insert
    suspend fun insertBlog(blogs: Blogs)
    @Update
    suspend fun updateBlog(blogs: Blogs)
    @Delete
    suspend fun deleteBlog(blogs: Blogs)
    @Query("SELECT * FROM blogsTable")
    fun getBlogs(): LiveData<List<Blogs>>
}