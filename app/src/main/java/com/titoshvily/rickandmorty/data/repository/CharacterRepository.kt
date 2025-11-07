package com.titoshvily.rickandmorty.data.repository

import com.titoshvily.rickandmorty.data.model.CharacterFilter
import com.titoshvily.rickandmorty.data.model.CharactersResponse

interface CharacterRepository {
    suspend fun getCharacters(page: Int): CharactersResponse
    suspend fun getSearch(name: String, page: Int): CharactersResponse
    suspend fun getCharactersWithFilter(filter: CharacterFilter, page: Int): CharactersResponse
}