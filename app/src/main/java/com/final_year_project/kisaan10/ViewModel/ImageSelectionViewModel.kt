package com.final_year_project.kisaan10.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageSelectionViewModel : ViewModel() {
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val _navigateToConfirmation = MutableLiveData<Boolean>()
    val navigateToConfirmation: LiveData<Boolean> = _navigateToConfirmation


    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
        Log.v("Set uri From ViewModel",selectedImageUri.value.toString())
    }

    fun clearSelectedImageUri() {
        _selectedImageUri.value = null
    }

    fun navigateToConfirmation() {
        _navigateToConfirmation.value = true
    }

    fun onNavigationComplete() {
        _navigateToConfirmation.value = false
    }
    fun getSelectedImageUri(): Uri? {
        Log.v("Get uri From ViewModel",this._selectedImageUri.value.toString())
        return selectedImageUri.value
    }
}