package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            CardItem(character = character)
        }



    }
}


