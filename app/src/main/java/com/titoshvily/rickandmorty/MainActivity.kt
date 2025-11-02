package com.titoshvily.rickandmorty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.titoshvily.rickandmorty.data.RickAndMortyApi
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private var characters by mutableStateOf<List<Character>>(emptyList())
    private var isLoading by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { loadList() } ) {
                        Text("GET")
                    }
                        if (isLoading){
                            Text("Loading...")
                        }
                        CharacterList(characters)

                    }
                }
            }
        }
    }

    private fun loadList(){
        CoroutineScope(Dispatchers.IO).launch {
            isLoading = true
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = runCatching {
                val service = retrofit.create(RickAndMortyApi::class.java)
                service.getCharacters()
            }

            result.onSuccess{ response ->

                 characters = response.results
                isLoading = false
            }.onFailure { error->
                Log.e("MyLog", "Error: $error")
            }



        }
    }
}

@Composable
fun CharacterList(characters: List<Character>){
    LazyColumn() {
        items(characters){ character->
            CharacterItem(character)
        }
    }
}


@Composable
fun CharacterItem(character: Character) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(character.name, fontWeight = FontWeight.Bold)
            Text("${character.species} • ${character.status}")
            Text(character.gender)
        }
    }
}





