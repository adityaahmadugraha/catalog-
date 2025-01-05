package com.adit.catalog.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adit.catalog.data.local.ObjekData
import com.adit.catalog.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun isFavorite(id: Int): LiveData<Boolean> {
        return repository.isFavorite(id)
    }

    fun addFavorite(data: ObjekData) {
        viewModelScope.launch {
            repository.addToFavorites(data.toEntity())
        }
    }

    fun removeFavorite(data: ObjekData) {
        viewModelScope.launch {
            repository.removeFromFavorites(data.toEntity())
        }
    }
}

