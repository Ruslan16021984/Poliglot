package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordCardWithGroups(
    @Embedded val wordCard: WordCardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WordCardGroupCrossRef::class,
            parentColumn = "wordCardId",
            entityColumn = "wordGroupId",
        ),
    )
    val groups: List<WordGroupEntity>,
)
