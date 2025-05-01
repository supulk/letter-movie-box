package com.example.letter_movie_box.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Upsert
    suspend fun upsertMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE actors LIKE '%'||:actorName||'%'")
    fun getMoviesByActor(actorName: String):Flow<List<Movie>>
}