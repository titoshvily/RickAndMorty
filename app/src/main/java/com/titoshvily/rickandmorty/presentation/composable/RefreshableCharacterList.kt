package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.titoshvily.rickandmorty.data.model.Character

@Composable
fun RefreshableCharacterList(
    characters: List<Character>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    isEmptyState: Boolean,
    searchQuery: String,
    isFilterActive: Boolean,
    hasNextPage: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCharacterClick: (Character) -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isEmptyState && !isLoading -> {
                    when {
                        searchQuery.isNotEmpty() -> SearchEmptyState(searchQuery = searchQuery)
                        isFilterActive -> FilterEmptyState()
                        else -> GeneralEmptyState()
                    }
                }

                characters.isNotEmpty() -> {
                    CharacterList(
                        characters = characters,
                        isLoading = isLoading,
                        hasNextPage = hasNextPage,
                        onLoadMore = onLoadMore,
                        onCharacterClick = onCharacterClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                isLoading && characters.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadScreen()
                    }
                }
            }
        }
    }
}