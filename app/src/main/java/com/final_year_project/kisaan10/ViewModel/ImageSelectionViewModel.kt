package com.final_year_project.kisaan10.ViewModel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.InputStream

class ImageSelectionViewModel : ViewModel() {
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val _selectedImageBitMap = MutableLiveData<Bitmap>()
    val selectedImageBitMap : LiveData<Bitmap?> = _selectedImageBitMap

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

    fun uriToBitmap(context: Context): Bitmap? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri.value!!)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}