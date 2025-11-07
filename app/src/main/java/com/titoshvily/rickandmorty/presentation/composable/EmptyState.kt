package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.titoshvily.rickandmorty.R

@Composable
fun EmptyState(
    iconRes: Int = R.drawable.ic_searchoff,
    title: String = "Ничего не найдено",
    subtitle: String = "Попробуйте изменить параметры поиска или фильтры",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun SearchEmptyState(
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    EmptyState(
        iconRes = R.drawable.ic_searchoff,
        title = if (searchQuery.isNotEmpty()) {
            "По запросу \"$searchQuery\" ничего не найдено"
        } else {
            "Ничего не найдено"
        },
        subtitle = "Попробуйте изменить поисковый запрос или сбросить фильтры",
        modifier = modifier
    )
}

@Composable
fun FilterEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        iconRes = R.drawable.ic_searchoff,
        title = "По выбранным фильтрам ничего не найдено",
        subtitle = "Попробуйте изменить параметры фильтров",
        modifier = modifier
    )
}

@Composable
fun GeneralEmptyState(
    modifier: Modifier = Modifier
) {
    EmptyState(
        iconRes = R.drawable.ic_searchoff,
        title = "Список пуст",
        subtitle = "Потяните вниз для обновления",
        modifier = modifier
    )
}