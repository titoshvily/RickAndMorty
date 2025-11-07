
package com.titoshvily.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.Location

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,
    val image: String,
    val episode: String,
    val url: String,
    val created: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = Location(originName, originUrl),
            location = Location(locationName, locationUrl),
            image = image,
            episode = episode.split(","),
            url = url,
            created = created
        )
    }
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        originUrl = origin.url,
        locationName = location.name,
        locationUrl = location.url,
        image = image,
        episode = episode.joinToString(","),
        url = url,
        created = created
    )
}