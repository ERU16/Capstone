package com.dicoding.capstonebre.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.capstonebre.database.AppDatabase
import com.dicoding.capstonebre.database.ClassificationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClassificationResultViewModel(application: Application) : AndroidViewModel(application) {
    private val classificationResultDao = AppDatabase.getDatabase(application).classificationResultDao()

    private val _allResults = MutableLiveData<List<ClassificationResult>>()
    val allResults: LiveData<List<ClassificationResult>> get() = _allResults

    init {
        fetchAllResults()
    }

    private fun fetchAllResults() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = classificationResultDao.getAllResults()
            _allResults.postValue(results)
        }
    }

    fun addNewResult(newResult: ClassificationResult) {
        viewModelScope.launch(Dispatchers.IO) {
            classificationResultDao.insert(newResult)
            fetchAllResults()
        }
    }
}
