package com.carbit3333333.oiiglot_bulgary.data.dictionary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordGroupDao {
    @Query(
        """
        SELECT
            g.id AS id,
            g.name AS name,
            COUNT(c.wordCardId) AS wordCount
        FROM word_groups g
        LEFT JOIN word_card_group_cross_ref c ON g.id = c.wordGroupId
        GROUP BY g.id, g.name
        ORDER BY g.name COLLATE NOCASE ASC
        """
    )
    fun observeAllGroupsWithCounts(): Flow<List<WordGroupWithCount>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createGroup(group: WordGroupEntity): Long
}
