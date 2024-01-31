package com.example.roomhandson.data.local.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomhandson.data.local.dao.NotesDao
import com.example.roomhandson.data.local.enitities.Note
import com.example.roomhandson.utils.Constants

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, NotesDatabase::class.java, Constants.NOTE_DB
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}