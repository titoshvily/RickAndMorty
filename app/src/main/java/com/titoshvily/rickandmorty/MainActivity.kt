package com.titoshvily.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.titoshvily.rickandmorty.presentation.composable.CharacterDetailScreen
import com.titoshvily.rickandmorty.presentation.composable.FilterDialog
import com.titoshvily.rickandmorty.presentation.composable.RefreshableCharacterList
import com.titoshvily.rickandmorty.presentation.composable.SearchBar
import com.titoshvily.rickandmorty.presentation.viewmodel.CharacterViewModel
import com.titoshvily.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    class CharacterViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CharacterViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                var selectedCharacter by remember {
                    mutableStateOf<com.titoshvily.rickandmorty.data.model.Character?>(null)
                }
                var showFilterDialog by remember { mutableStateOf(false) }

                val characters by characterViewModel.characters.collectAsState()
                val isLoading by characterViewModel.isLoading.collectAsState()
                val isRefreshing by characterViewModel.isRefreshing.collectAsState()
                val isEmptyState by characterViewModel.isEmptyState.collectAsState()
                val searchQuery by characterViewModel.searchQuery.collectAsState()
                val currentFilter by characterViewModel.currentFilter.collectAsState()
                val hasNextPage = characterViewModel.hasNextPage
                val isFilterActive = characterViewModel.isFilterActive()

                if (selectedCharacter != null) {
                    CharacterDetailScreen(
                        character = selectedCharacter!!,
                        onBackClick = { selectedCharacter = null }
                    )
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SearchBar(
                                searchQuery = searchQuery,
                                onSearchQueryChanged = { query ->
                                    characterViewModel.onSearch(query)
                                },
                                onSearch = { query ->
                                    characterViewModel.onSearch(query)
                                },
                                onClear = {
                                    characterViewModel.clearSearch()
                                },
                                onFilterClick = {
                                    showFilterDialog = true
                                },
                                isFilterActive = isFilterActive
                            )

                            Spacer(modifier = Modifier.padding(2.dp))

                            RefreshableCharacterList(
                                characters = characters,
                                isLoading = isLoading,
                                isRefreshing = isRefreshing,
                                isEmptyState = isEmptyState,
                                searchQuery = searchQuery,
                                isFilterActive = isFilterActive,
                                hasNextPage = hasNextPage,
                                onRefresh = {
                                    characterViewModel.refreshData()
                                },
                                onLoadMore = {
                                    characterViewModel.loadMore()
                                },
                                onCharacterClick = { character ->
                                    selectedCharacter = character
                                }
                            )

                            if (showFilterDialog) {
                                FilterDialog(
                                    currentFilter = currentFilter,
                                    onFilterChanged = { newFilter ->
                                        characterViewModel.applyFilter(newFilter)
                                    },
                                    onClear = {
                                        characterViewModel.clearFilter()
                                    },
                                    onDismiss = {
                                        showFilterDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}