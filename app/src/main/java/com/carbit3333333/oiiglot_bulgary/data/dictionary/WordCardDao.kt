package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordCardDao {
    @Query(
        """
        SELECT *
        FROM word_cards
        ORDER BY updatedAt DESC, id DESC
        """
    )
    fun observeAllWords(): Flow<List<WordCardEntity>>

    // Literal substring search keeps Cyrillic matching predictable for Task 1.
    @Query(
        """
        SELECT *
        FROM word_cards
        WHERE INSTR(bgWord, TRIM(:query)) > 0
           OR INSTR(ruTranslation, TRIM(:query)) > 0
        ORDER BY updatedAt DESC, id DESC
        """
    )
    fun searchWords(query: String): Flow<List<WordCardEntity>>

    @Query(
        """
        SELECT *
        FROM word_cards
        WHERE (
            TRIM(:query) = ''
            OR INSTR(bgWord, TRIM(:query)) > 0
            OR INSTR(ruTranslation, TRIM(:query)) > 0
        )
        AND (
            :groupId IS NULL
            OR EXISTS (
                SELECT 1
                FROM word_card_group_cross_ref c
                WHERE c.wordCardId = word_cards.id
                  AND c.wordGroupId = :groupId
            )
        )
        ORDER BY updatedAt DESC, id DESC
        """
    )
    fun observeWords(query: String, groupId: Long?): Flow<List<WordCardEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM word_cards
        WHERE id = :wordId
        LIMIT 1
        """
    )
    suspend fun getWordWithGroupsById(wordId: Long): WordCardWithGroups?

    @Insert
    suspend fun insertWord(word: WordCardEntity): Long

    @Update
    suspend fun updateWord(word: WordCardEntity)

    @Delete
    suspend fun deleteWord(word: WordCardEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroupCrossRef(crossRef: WordCardGroupCrossRef)

    @Query(
        """
        DELETE FROM word_card_group_cross_ref
        WHERE wordCardId = :wordCardId
        """
    )
    suspend fun deleteGroupCrossRefsForWord(wordCardId: Long)
}
