package com.titoshvily.rickandmorty.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.titoshvily.rickandmorty.data.model.CharacterFilter
import com.titoshvily.rickandmorty.data.model.CharacterGender
import com.titoshvily.rickandmorty.data.model.CharacterStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentFilter: CharacterFilter,
    onFilterChanged: (CharacterFilter) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    var localFilter by remember { mutableStateOf(currentFilter) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры персонажей") },
        text = {
            Column {
                var expandedStatus by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = localFilter.status?.value ?: "Любой статус",
                        onValueChange = { },
                        label = { Text("Статус") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = { expandedStatus = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Любой статус") },
                            onClick = {
                                localFilter = localFilter.copy(status = null)
                                expandedStatus = false
                            }
                        )
                        CharacterStatus.entries.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.value) },
                                onClick = {
                                    localFilter = localFilter.copy(status = status)
                                    expandedStatus = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = localFilter.species,
                    onValueChange = {
                        localFilter = localFilter.copy(species = it)
                    },
                    label = { Text("Вид") },
                    placeholder = { Text("Human, Alien, etc.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = localFilter.type,
                    onValueChange = {
                        localFilter = localFilter.copy(type = it)
                    },
                    label = { Text("Тип") },
                    placeholder = { Text("Genetic experiment, etc.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                var expandedGender by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = { expandedGender = !expandedGender }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = localFilter.gender?.value ?: "Любой пол",
                        onValueChange = { },
                        label = { Text("Пол") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(bottom = 8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Любой пол") },
                            onClick = {
                                localFilter = localFilter.copy(gender = null)
                                expandedGender = false
                            }
                        )
                        CharacterGender.entries.forEach { gender ->
                            DropdownMenuItem(
                                text = { Text(gender.value) },
                                onClick = {
                                    localFilter = localFilter.copy(gender = gender)
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row {
                Button(
                    onClick = {
                        onClear()
                        onDismiss()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Сбросить")
                }
                Button(
                    onClick = {
                        onFilterChanged(localFilter)
                        onDismiss()
                    }
                ) {
                    Text("Применить")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}