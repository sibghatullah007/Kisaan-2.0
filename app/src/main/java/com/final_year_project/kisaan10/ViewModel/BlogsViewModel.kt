package com.final_year_project.kisaan10.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.localDB.BlogsDAO
import com.final_year_project.kisaan10.localDB.KissanDatabase
import kotlinx.coroutines.launch

class BlogsViewModel(application: Application) : AndroidViewModel(application) {
    private val blogsDao: BlogsDAO = KissanDatabase.getDatabase(application).blogsDAO()
    val allBlogs: LiveData<List<Blogs>> = blogsDao.getBlogs()

    fun insert(blog: Blogs) = viewModelScope.launch {
        blogsDao.insertBlog(blog)
    }

    fun delete(blog: Blogs) = viewModelScope.launch {
        blogsDao.deleteBlog(blog)
    }
}
