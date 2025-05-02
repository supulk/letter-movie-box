package com.example.letter_movie_box

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letter_movie_box.data.Movie
import com.example.letter_movie_box.data.MovieDatabase
import com.example.letter_movie_box.data.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val repository:MovieRepository
    init {
        val movieDao = MovieDatabase.getDatabase(application).dao()
        repository = MovieRepository(movieDao)
    }

    fun addtoDb(movie: Movie) = viewModelScope.launch{
        repository.addMovie(movie)
    }

    suspend fun searchActor(actorName:String): Flow<List<Movie>>{
        return repository.getMoviesByActor(actorName)
    }
}