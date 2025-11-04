package com.titoshvily.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.titoshvily.rickandmorty.presentation.composable.CharacterList
import com.titoshvily.rickandmorty.presentation.composable.LoadScreen
import com.titoshvily.rickandmorty.presentation.composable.SearchBar
import com.titoshvily.rickandmorty.presentation.viewmodel.CharacterViewModel
import com.titoshvily.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    private val characterViewModel = CharacterViewModel()
    val hasNextPage = characterViewModel.hasNextPage


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val characters by characterViewModel.characters.collectAsState()
                    val searchResults by characterViewModel.search.collectAsState()
                    val isLoading by characterViewModel.isLoading.collectAsState()
                    val searchQuery by characterViewModel.searchQuery.collectAsState()
                    val isSearchMode by characterViewModel.isSearchMode.collectAsState()



                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChanged = { characterViewModel.onSearchQueryChanged(it) },
                            onSearch = { query -> characterViewModel.loadSearchCharacters(query) },
                            onClear = { characterViewModel.clearSearch() }
                        )
                        Spacer(modifier = Modifier.padding(2.dp))


                        val displayList = if (isSearchMode) searchResults else characters

                        if (isLoading && characters.isEmpty()) {
                            LoadScreen()
                        } else {

                            CharacterList(
                                characters = displayList,
                                isLoading = isLoading,
                                onLoadMore = { characterViewModel.loadCharactersPage() },
                                hasNextPage = if (isSearchMode) false else hasNextPage
                            )

                        }
                    }
                }
            }
        }
    }
}










