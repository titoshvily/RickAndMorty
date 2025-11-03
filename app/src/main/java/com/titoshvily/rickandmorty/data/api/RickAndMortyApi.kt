package com.titoshvily.rickandmorty.data.api

import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character/{id}")
   suspend fun getModelById(@Path("id") id : Int): Character


   @GET("character/")
   suspend fun getCharacters(
       @Query("page") page: Int? = null
   ) : CharactersResponse


}