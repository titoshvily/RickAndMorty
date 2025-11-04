package com.titoshvily.rickandmorty.data.repository

import com.titoshvily.rickandmorty.data.api.RetrofitInstance
import com.titoshvily.rickandmorty.data.model.Character
import com.titoshvily.rickandmorty.data.model.CharactersResponse

class CharacterRepository {

    private val api = RetrofitInstance.api

    suspend fun getCharacters(page:Int): CharactersResponse{
        return api.getCharacters(page)
    }

    suspend fun getCharactersById(id:Int): Character {
        return api.getModelById(id)
    }





}