package com.titoshvily.rickandmorty.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel: ViewModel() {

    //MainList
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

        //SearchList

    private val _search = MutableStateFlow<List<Character>>(emptyList())
    val search: StateFlow<List<Character>> = _search.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


        //Status download
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Flag status search
    private val _isSearchMode = MutableStateFlow(false)
    val isSearchMode: StateFlow<Boolean> = _isSearchMode.asStateFlow()

    private var _hasNextPage = true
    val hasNextPage: Boolean get() = _hasNextPage


    private var currentPage = 1

    private val repository = CharacterRepository()


    init {
        loadCharactersPage()
    }


    fun loadCharactersPage(){
        if (_isLoading.value || !_hasNextPage) return

        viewModelScope.launch {

            _isLoading.value = true
            val result = runCatching {
                repository.getCharacters(currentPage)
            }
            result.onSuccess { response ->
                _characters.value = _characters.value + response.results
                currentPage++
                _hasNextPage = response.info.next != null
                _isLoading.value = false

            }.onFailure { error->
                Log.d("MyLog", "Error: $error")
                _isLoading.value = false
            }

        }

    }



    fun loadSearchCharacters(name:String){
        viewModelScope.launch {
            _isLoading.value = true
            _isSearchMode.value = true
            val result = runCatching {
                repository.getSearch(name)
            }
            result.onSuccess { response ->
                _search.value = response.results
                _isLoading.value = false
            }.onFailure { error->
                Log.d("MyLog", "Error: $error")
                _search.value = emptyList()
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _search.value = emptyList()
        _isSearchMode.value = false
        Log.d("MyLog", "❌ Поиск очищен")
    }


    fun getCurrentCharacters(): List<Character> {
        return if (_isSearchMode.value) {
            _search.value
        } else {
            _characters.value
        }
    }

}






