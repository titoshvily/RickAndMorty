package com.titoshvily.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    suspend fun searchCharacters(query: String): List<CharacterEntity>

    @Query("""
        SELECT * FROM characters 
        WHERE 
        (:status IS NULL OR status = :status) 
        AND (:species IS NULL OR species LIKE '%' || :species || '%') 
        AND (:type IS NULL OR type LIKE '%' || :type || '%') 
        AND (:gender IS NULL OR gender = :gender) 
        ORDER BY id ASC
    """)
    suspend fun getCharactersWithFilters(
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): List<CharacterEntity>

    @Query("""
        SELECT * FROM characters 
        WHERE 
        name LIKE '%' || :name || '%' 
        AND (:status IS NULL OR status = :status) 
        AND (:species IS NULL OR species LIKE '%' || :species || '%') 
        AND (:type IS NULL OR type LIKE '%' || :type || '%') 
        AND (:gender IS NULL OR gender = :gender) 
        ORDER BY id ASC
    """)
    suspend fun searchCharactersWithFilters(
        name: String,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): List<CharacterEntity>

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharactersCount(): Int

    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}