package com.final_year_project.kisaan10.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.final_year_project.kisaan10.localDB.KissanDatabase
import com.final_year_project.kisaan10.localDB.RecentDisease
import com.final_year_project.kisaan10.localDB.RecentDiseaseDAO
import kotlinx.coroutines.launch

class RecentDiseaseViewModel(application: Application) : AndroidViewModel(application)  {
    private val recentDiseaseDAO: RecentDiseaseDAO = KissanDatabase.getDatabase(application).RecentDiseaseDAO()
    val allDiseases: LiveData<List<RecentDisease>> = recentDiseaseDAO.getDiseases()

    fun insert(recentDisease: RecentDisease) = viewModelScope.launch {
        recentDiseaseDAO.insertDisease(recentDisease)
    }

    fun delete(recentDisease: RecentDisease) = viewModelScope.launch {
        recentDiseaseDAO.deleteDisease(recentDisease)
    }
}