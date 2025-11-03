package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.titoshvily.rickandmorty.data.model.Character


@Composable
fun CharacterList(
    characters: List<Character>,
    isLoading: Boolean,
    hasNextPage: Boolean,
    onLoadMore: () -> Unit = {}
) {

    val gridState = rememberLazyGridState()


    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

            if (lastVisibleItem != null && hasNextPage) {

                lastVisibleItem.index >= totalItems - 4
            } else {
                false
            }
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isLoading && hasNextPage) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), state = gridState, modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(characters) { character ->
            CharacterItem(character = character)
        }

        if (isLoading) {
            item {
                Text(
                    "Загрузка...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun CharacterItem(character: Character) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(character.image),
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Информация о персонаже
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = character.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${character.species} • ${character.status}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Цветной статус
                Text(
                    text = character.status,
                    fontSize = 11.sp,
                    color = when (character.status.lowercase()) {
                        "alive" -> Color.Green
                        "dead" -> Color.Red
                        else -> Color.Gray
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}