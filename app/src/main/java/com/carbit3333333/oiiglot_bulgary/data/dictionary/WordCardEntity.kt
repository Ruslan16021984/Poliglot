package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_cards")
data class WordCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bgWord: String,
    val ruTranslation: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
