package com.carbit3333333.oiiglot_bulgary.data.dictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Task 1 keeps schema export off to avoid generating migration artifacts before the
// dictionary schema and migration story exist.
@Database(
    entities = [
        WordCardEntity::class,
        WordGroupEntity::class,
        WordCardGroupCrossRef::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class PersonalDictionaryDatabase : RoomDatabase() {
    abstract fun wordCardDao(): WordCardDao

    abstract fun wordGroupDao(): WordGroupDao

    companion object {
        @Volatile
        private var INSTANCE: PersonalDictionaryDatabase? = null

        fun getInstance(context: Context): PersonalDictionaryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PersonalDictionaryDatabase::class.java,
                    "personal_dictionary.db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
