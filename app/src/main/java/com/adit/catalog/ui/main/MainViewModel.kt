package com.adit.catalog.ui.main

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

    private val _filteredList = MutableLiveData<List<ObjekData>>()
    val filteredList: LiveData<List<ObjekData>> = _filteredList


    val favoriteBooks: LiveData<List<ObjekDataEntity>> = repository.getFavoriteBooks()

    private var originalList: List<ObjekData> = emptyList()
    private var lastQuery: String = ""

    private var currentSortType: Constant.SortType = Constant.SortType.AZ

    fun fetchMenuList() {
        viewModelScope.launch {
                originalList = repository.getListBook()
                _filteredList.value = applySorting(originalList)

        }
    }

    fun addToFavorites(book: ObjekData) {
        viewModelScope.launch {
                repository.addToFavorites(book.toEntity())
        }
    }

    fun removeFromFavorites(book: ObjekData) {
        viewModelScope.launch {
                repository.removeFromFavorites(book.toEntity())
        }
    }

    fun filterBook(query: String) {
        if (query == lastQuery) return
        lastQuery = query

        val filtered = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
        _filteredList.value = applySorting(filtered)

    }
    fun sortList(sortType: Constant.SortType) {
        _filteredList.value = when (sortType) {
            Constant.SortType.AZ -> _filteredList.value?.sortedBy { it.title }
            Constant.SortType.ZA -> _filteredList.value?.sortedByDescending { it.title }
            Constant.SortType.PRICE_LOW_TO_HIGH -> _filteredList.value?.sortedBy { it.price }
            Constant.SortType.PRICE_HIGH_TO_LOW -> _filteredList.value?.sortedByDescending { it.price }
        }
    }


    private fun applySorting(list: List<ObjekData>): List<ObjekData> {
        return when (currentSortType) {
            Constant.SortType.AZ -> list.sortedBy { it.title }
            Constant.SortType.ZA -> list.sortedByDescending { it.title }
            Constant.SortType.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
            Constant.SortType.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
        }
    }


}
