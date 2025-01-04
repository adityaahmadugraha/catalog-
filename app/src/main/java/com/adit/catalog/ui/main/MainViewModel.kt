package com.adit.catalog.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adit.catalog.data.local.ObjekData
import com.adit.catalog.data.repository.Repository
import com.adit.catalog.data.room.ObjekDataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _bookList = MutableLiveData<List<ObjekData>>()

    private val _filteredList = MutableLiveData<List<ObjekData>?>()
    val filteredList: MutableLiveData<List<ObjekData>?> = _filteredList

    val favoriteBooks: LiveData<List<ObjekDataEntity>> = repository.getFavoriteBooks()

    fun fetchMenuList() {
        _bookList.value = repository.getListBook()
        _filteredList.value = _bookList.value
    }

    fun filterMenu(query: String) {
        val filtered = if (query.isEmpty()) {
            _bookList.value
        } else {
            _bookList.value?.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
        _filteredList.value = filtered
    }

    fun addToFavorites(book: ObjekData) {
        val objekDataEntity = ObjekDataEntity(
            id = book.id,
            image = book.image,
            title = book.title,
            sinopsis = book.sinopsis,
            thick = book.thick,
            price = book.price,
            category = book.category,
            author = book.author,
            published = book.published
        )
        viewModelScope.launch {
            try {

                Log.d("MainViewModel", "Menambahkan ${book.title} ke favorit")

                repository.addToFavorites(objekDataEntity)

                Log.d("MainViewModel", "${book.title} berhasil ditambahkan ke favorit")
            } catch (e: Exception) {

                Log.e("MainViewModel", "Gagal menambahkan ${book.title} ke favorit", e)
            }
        }
    }


    // Menghapus buku dari favorit
    fun removeFromFavorites(book: ObjekData) {
        val objekDataEntity = ObjekDataEntity(
            id = book.id,
            image = book.image,
            title = book.title,
            sinopsis = book.sinopsis,
            thick = book.thick,
            price = book.price,
            category = book.category,
            author = book.author,
            published = book.published
        )
        viewModelScope.launch {
            repository.removeFromFavorites(objekDataEntity)
        }
    }
}
