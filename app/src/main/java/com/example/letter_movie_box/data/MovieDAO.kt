package com.example.letter_movie_box.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Upsert //upsert instead of insert to prevent duplicates,(replace the row if already exists)
    suspend fun upsertMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE actors LIKE '%'||:actorName||'%'")
    fun getMoviesByActor(actorName: String):List<Movie>

    fun deleteDb(context: Context) {
        context.deleteDatabase("moviedb.db")
    }
}