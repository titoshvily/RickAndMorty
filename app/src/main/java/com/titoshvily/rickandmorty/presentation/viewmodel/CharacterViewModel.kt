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
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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

}