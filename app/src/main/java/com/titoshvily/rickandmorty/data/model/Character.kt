package com.titoshvily.rickandmorty.data.model



data class CharactersResponse(
    val info: Info,
    val results: List<Character>
)


data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class Location(
    val name: String,
    val url: String
)



data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterFilter(
    val name: String = "",
    val status: CharacterStatus? = null,
    val species: String = "",
    val type: String = "",
    val gender: CharacterGender? = null
)

enum class CharacterStatus(val value: String) {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("unknown")
}

enum class CharacterGender(val value: String) {
    FEMALE("Female"),
    MALE("Male"),
    GENDERLESS("Genderless"),
    UNKNOWN("unknown")
}