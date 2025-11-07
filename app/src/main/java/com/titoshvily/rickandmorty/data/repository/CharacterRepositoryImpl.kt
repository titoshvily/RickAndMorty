package com.titoshvily.rickandmorty.data.repository

import android.content.Context
import com.titoshvily.rickandmorty.data.api.RickAndMortyApi
import com.titoshvily.rickandmorty.data.local.CharacterDao
import com.titoshvily.rickandmorty.data.local.toEntity
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.CharacterFilter
import com.titoshvily.rickandmorty.data.model.CharactersResponse
import com.titoshvily.rickandmorty.utils.NetworkUtils

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val characterDao: CharacterDao,
    private val context: Context
) : CharacterRepository {

    override suspend fun getSearch(name: String, page: Int): CharactersResponse {
        val isOnline = NetworkUtils.isNetworkAvailable(context)

        return if (isOnline) {
            try {
                val response = api.getSearch(name, page)
                if (page == 1 && response.results.isNotEmpty()) {
                    characterDao.insertCharacters(response.results.map { it.toEntity() })
                }
                response
            } catch (e: Exception) {
                searchFromCache(name)
            }
        } else {
            searchFromCache(name)
        }
    }

    override suspend fun getCharactersWithFilter(filter: CharacterFilter, page: Int): CharactersResponse {
        val isOnline = NetworkUtils.isNetworkAvailable(context)

        return if (isOnline) {
            try {
                val response = api.getCharactersWithFilters(
                    name = filter.name.takeIf { it.isNotEmpty() },
                    status = filter.status?.value,
                    species = filter.species.takeIf { it.isNotEmpty() },
                    type = filter.type.takeIf { it.isNotEmpty() },
                    gender = filter.gender?.value,
                    page = page
                )
                if (page == 1 && response.results.isNotEmpty()) {
                    characterDao.insertCharacters(response.results.map { it.toEntity() })
                }
                response
            } catch (e: Exception) {
                filterFromCache(filter)
            }
        } else {
            filterFromCache(filter)
        }
    }

    override suspend fun getCharacters(page: Int): CharactersResponse {
        val isOnline = NetworkUtils.isNetworkAvailable(context)

        return if (isOnline) {
            try {
                val response = api.getCharacters(page)
                if (page == 1) {
                    characterDao.insertCharacters(response.results.map { it.toEntity() })
                }
                response
            } catch (e: Exception) {
                getCharactersFromCache()
            }
        } else {
            getCharactersFromCache()
        }
    }

    private suspend fun searchFromCache(name: String): CharactersResponse {
        val characters = characterDao.searchCharacters(name).map { it.toCharacter() }
        return createResponse(characters)
    }

    private suspend fun filterFromCache(filter: CharacterFilter): CharactersResponse {
        val characters = if (filter.name.isNotEmpty()) {
            characterDao.searchCharactersWithFilters(
                name = filter.name,
                status = filter.status?.value,
                species = filter.species,
                type = filter.type,
                gender = filter.gender?.value
            ).map { it.toCharacter() }
        } else {
            characterDao.getCharactersWithFilters(
                status = filter.status?.value,
                species = filter.species,
                type = filter.type,
                gender = filter.gender?.value
            ).map { it.toCharacter() }
        }

        return createResponse(characters)
    }

    private suspend fun getCharactersFromCache(): CharactersResponse {
        val characters = characterDao.getAllCharacters().map { it.toCharacter() }
        return createResponse(characters)
    }

    private fun createResponse(characters: List<Character>): CharactersResponse {
        return CharactersResponse(
            info = com.titoshvily.rickandmorty.data.model.Info(
                count = characters.size,
                pages = 1,
                next = null,
                prev = null
            ),
            results = characters
        )
    }
}