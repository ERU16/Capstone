package com.dicoding.capstonebre.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClassificationResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ClassificationResult)

    @Query("SELECT * FROM classification_results ORDER BY id DESC")
    suspend fun getAllResults(): List<ClassificationResult>

    @Query("DELETE FROM classification_results")
    suspend fun clearAll()
}