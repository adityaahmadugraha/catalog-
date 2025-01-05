package com.adit.catalog.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adit.catalog.data.local.ObjekData
import com.adit.catalog.data.repository.Repository
import com.adit.catalog.data.room.ObjekDataEntity
import com.adit.catalog.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _bookList = MutableLiveData<List<ObjekData>>()
    private val _filteredList = MutableLiveData<List<ObjekData>>()
    val filteredList: LiveData<List<ObjekData>> = _filteredList

    private var originalList: List<ObjekData> = emptyList()

    val favoriteBooks: LiveData<List<ObjekDataEntity>> = repository.getFavoriteBooks()

    fun fetchMenuList() {
        val bookList = repository.getListBook()
        _bookList.value = bookList
        originalList = bookList
        _filteredList.value = bookList
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
                Log.d("MainViewModel", "Adding ${book.title} to favorites")
                repository.addToFavorites(objekDataEntity)
                Log.d("MainViewModel", "${book.title} successfully added to favorites")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to add ${book.title} to favorites", e)
            }
        }
    }

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

    fun filterMenu(query: String) {
        val filtered = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
            }
        }

        _filteredList.value = applySorting(filtered)
    }

    fun sortList(sortType: Constant.SortType) {
        currentSortType = sortType
        _filteredList.value = applySorting(_filteredList.value ?: originalList)
    }

    private fun applySorting(list: List<ObjekData>): List<ObjekData> {
        Log.d("MainViewModel", "Sorting list with type: $currentSortType")
        val sortedList = when (currentSortType) {
            Constant.SortType.AZ -> list.sortedBy { it.title }
            Constant.SortType.ZA -> list.sortedByDescending { it.title }
            Constant.SortType.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
            Constant.SortType.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
        }
        Log.d("MainViewModel", "Sorted list size: ${sortedList.size}")
        return sortedList
    }




    var currentSortType: Constant.SortType = Constant.SortType.AZ
}
