package com.example.letter_movie_box

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase(){
    abstract val dao:MovieDAO

    companion object{
        @Volatile
        var INSTANCE:MovieDatabase? = null
        fun getDatabase(context: Context):MovieDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    MovieDatabase::class.java,
                    "moviedb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}