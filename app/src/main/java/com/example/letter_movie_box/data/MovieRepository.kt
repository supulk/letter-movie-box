package com.example.letter_movie_box.data

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDAO) {

    suspend fun addMovie(movie: Movie){
        movieDao.upsertMovie(movie)
    }

    fun getMoviesByActor(name:String):Flow<List<Movie>>{
        return movieDao.getMoviesByActor(name)
    }
}