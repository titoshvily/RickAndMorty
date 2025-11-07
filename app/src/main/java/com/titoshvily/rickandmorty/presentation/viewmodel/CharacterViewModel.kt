package com.titoshvily.rickandmorty.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.rickandmorty.data.api.RetrofitInstance
import com.titoshvily.rickandmorty.data.local.AppDatabase
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.CharacterFilter
import com.titoshvily.rickandmorty.data.model.CharactersResponse
import com.titoshvily.rickandmorty.data.repository.CharacterRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(private val context: Context) : ViewModel() {

    private val repository = CharacterRepositoryImpl(
        api = RetrofitInstance.api,
        characterDao = AppDatabase.getInstance(context).characterDao(),
        context = context
    )

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentFilter = MutableStateFlow(CharacterFilter())
    val currentFilter: StateFlow<CharacterFilter> = _currentFilter.asStateFlow()

    private var _hasNextPage = true
    val hasNextPage: Boolean get() = _hasNextPage

    private val _isEmptyState = MutableStateFlow(false)
    val isEmptyState: StateFlow<Boolean> = _isEmptyState.asStateFlow()

    private var currentPage = 1

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            _isEmptyState.value = false
            resetPagination()

            val result = kotlin.runCatching {
                loadDataWithCurrentParams(currentPage)
            }

            handleLoadResult(result, isInitialLoad = true)
            _isLoading.value = false
        }
    }

    fun loadMore() {
        if (_isLoading.value || !_hasNextPage) return

        viewModelScope.launch {
            _isLoading.value = true

            val result = kotlin.runCatching {
                loadDataWithCurrentParams(currentPage)
            }

            handleLoadResult(result, isInitialLoad = false)
            _isLoading.value = false
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _isEmptyState.value = false

            _searchQuery.value = ""
            _currentFilter.value = CharacterFilter()
            resetPagination()

            val result = kotlin.runCatching {
                repository.getCharacters(currentPage)
            }

            handleLoadResult(result, isInitialLoad = true)
            _isRefreshing.value = false
        }
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
        loadInitialData()
    }

    fun applyFilter(filter: CharacterFilter) {
        _currentFilter.value = filter
        loadInitialData()
    }


    fun clearSearch() {
        _searchQuery.value = ""
        loadInitialData()
    }

    fun clearFilter() {
        _currentFilter.value = CharacterFilter()
        loadInitialData()
    }

    fun isFilterActive(): Boolean = _currentFilter.value != CharacterFilter()

    private suspend fun loadDataWithCurrentParams(page: Int): CharactersResponse {
        val hasSearch = _searchQuery.value.isNotEmpty()
        val hasFilter = _currentFilter.value != CharacterFilter()

        return when {
            hasSearch && hasFilter -> {
                val combinedFilter = _currentFilter.value.copy(name = _searchQuery.value)
                repository.getCharactersWithFilter(combinedFilter, page)
            }
            hasSearch -> {
                repository.getSearch(_searchQuery.value, page)
            }
            hasFilter -> {
                repository.getCharactersWithFilter(_currentFilter.value, page)
            }
            else -> {
                repository.getCharacters(page)
            }
        }
    }

    private fun resetPagination() {
        currentPage = 1
        _hasNextPage = true
        _characters.value = emptyList()
    }

    private fun handleLoadResult(result: Result<CharactersResponse>, isInitialLoad: Boolean) {
        result.onSuccess { response ->
            if (isInitialLoad) {
                _characters.value = response.results
                _isEmptyState.value = response.results.isEmpty()
            } else {
                _characters.value = _characters.value + response.results
                _isEmptyState.value = false
            }
            _hasNextPage = response.info.next != null
            currentPage++
        }.onFailure { error ->
            if (isInitialLoad) {
                _characters.value = emptyList()
                _isEmptyState.value = true
            }
            _hasNextPage = false
        }
    }
}