package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_groups",
    indices = [
        Index(value = ["name"], unique = true),
    ],
)
data class WordGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)
