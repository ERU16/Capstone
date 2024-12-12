package com.dicoding.capstonebre.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classification_results")
data class ClassificationResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val labels: String,
    val score: Float,
    val imageUri: String
)