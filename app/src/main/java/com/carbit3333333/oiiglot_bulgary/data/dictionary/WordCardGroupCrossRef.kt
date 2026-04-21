package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "word_card_group_cross_ref",
    primaryKeys = ["wordCardId", "wordGroupId"],
    foreignKeys = [
        ForeignKey(
            entity = WordCardEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordCardId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = WordGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["wordGroupId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["wordCardId"]),
        Index(value = ["wordGroupId"]),
    ],
)
data class WordCardGroupCrossRef(
    val wordCardId: Long,
    val wordGroupId: Long,
)
