package com.example.letter_movie_box.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun dao(): MovieDAO

    companion object{ //similar to a java static property
        @Volatile
        var INSTANCE: MovieDatabase? = null
        fun getDatabase(context: Context): MovieDatabase { //returns a the database if not empty
            return INSTANCE ?: synchronized(this){  //if empty, create a new one
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