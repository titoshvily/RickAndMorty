package com.titoshvily.rickandmorty.data.api

import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {



   @GET("character/")
   suspend fun getCharacters(
       @Query("page") page: Int? = null
   ) : CharactersResponse


    @GET("character/")
    suspend fun getSearch(
        @Query("name") name: String? = null,
        @Query("page") page: Int? = null
    ) : CharactersResponse

    @GET("character/")
    suspend fun getCharactersWithFilters(
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,
        @Query("page") page: Int? = null
    ): CharactersResponse

}