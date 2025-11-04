package com.titoshvily.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.titoshvily.rickandmorty.presentation.composable.CharacterList
import com.titoshvily.rickandmorty.presentation.composable.LoadScreen
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
                    val isLoading by characterViewModel.isLoading.collectAsState()



                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (isLoading && characters.isEmpty()) {
                            LoadScreen()
                        } else {

                            CharacterList(
                                characters = characters,
                                isLoading = isLoading,
                                onLoadMore = { characterViewModel.loadCharactersPage() },
                                hasNextPage = hasNextPage
                            )

                        }
                    }
                }
            }
        }
    }
}










