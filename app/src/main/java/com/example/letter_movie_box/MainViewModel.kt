package com.example.letter_movie_box

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.letter_movie_box.data.Movie
import com.example.letter_movie_box.data.MovieDatabase
import com.example.letter_movie_box.data.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val repository:MovieRepository
    init {
        val movieDao = MovieDatabase.getDatabase(application).dao()
        repository = MovieRepository(movieDao)
    }

    fun addtoDb(movie: Movie) = viewModelScope.launch{
        repository.addMovie(movie)
    }

    fun searchActor(actorName:String): List<Movie>{
        return repository.getMoviesByActor(actorName)
    }


    suspend fun fetchMovie(context: Context, keyword:String): Movie? { //function to search a single movie
        lateinit var fetchedMovie:Movie
        val encodedKeyword = withContext(Dispatchers.IO) {
            URLEncoder.encode(keyword, "UTF-8")
        }
        val urlString = "https://www.omdbapi.com/?t=$encodedKeyword&apikey=38b008c3"
        val url = URL(urlString)
        val con: HttpURLConnection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection   //establish the api connection

        val stb = StringBuilder()

        withContext(Dispatchers.IO){
            val bf = BufferedReader(InputStreamReader(con.inputStream))
            var line: String? = bf.readLine()
            while (line != null){
                stb.append(line + "\n")
                line = bf.readLine() //get the response to a stringbuilder
            }
        }
        val stbToJson = JSONObject(stb.toString())
        if (stbToJson.getString("Response").equals("True")){ //convert to json, create a movie object if match found,
            fetchedMovie = parseToMovieSingle(stbToJson)

            return fetchedMovie
        }else{
            Toast.makeText(context, "Zero Matches found", Toast.LENGTH_SHORT).show() //otherwise, send error toast
        }
        //Toast.makeText(context, "Got ${fetchedMovie.title}", Toast.LENGTH_SHORT).show()
        return null
    }

    private fun parseToMovieSingle(jsonObject: JSONObject) : Movie{

        return Movie(
            imdbId = jsonObject.getString("imdbID"),
            title = jsonObject.getString("Title"),
            year = jsonObject.getString("Year"),
            rated = jsonObject.getString("Rated"),
            released = jsonObject.getString("Released"),
            runtime = jsonObject.getString("Runtime"),
            genre = jsonObject.getString("Genre"),
            director = jsonObject.getString("Director"),
            writer = jsonObject.getString("Writer"),
            actors = jsonObject.getString("Actors"),
            plot = jsonObject.getString("Plot")
        )
    }

    suspend fun fetchMatchingMovies(context: Context, keyword:String): String { //function to get multiple matches
        val moviesList = ArrayList<Movie>()
        val encodedKeyword = withContext(Dispatchers.IO) {
            URLEncoder.encode(keyword, "UTF-8")
        }
        val urlString = "https://www.omdbapi.com/?s=$encodedKeyword&apikey=38b008c3"
        val url = URL(urlString)
        val con: HttpURLConnection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection

        val stb = StringBuilder()

        withContext(Dispatchers.IO){
            val bf = BufferedReader(InputStreamReader(con.inputStream))
            var line: String? = bf.readLine()
            while (line != null){
                stb.append(line + "\n")
                line = bf.readLine()
            }
        }

        val stbToJson = JSONObject(stb.toString())
        if (stbToJson.getString("Response").equals("True")){
            val searchArray = stbToJson.getJSONArray("Search") //search results are stored in a json array with the key "search"
            for (i in 0 until searchArray.length()) {
                val movieJson = searchArray.getJSONObject(i)
                val movie = Movie( //iterate through the list, get the basic details of each json object to a movie object and add to a movie array
                    imdbId = movieJson.getString("imdbID"),
                    title = movieJson.getString("Title"),
                    year = movieJson.getString("Year"),
                    rated = "",
                    released = "",
                    runtime = "",
                    genre = "",
                    director = "",
                    writer = "",
                    actors = "",
                    plot = ""
                )
                moviesList.add(movie)
            }
            var returnString = "" //iterate through the movie list, get title and year, add to a string and send it
            moviesList.forEach { movie ->
                returnString += movie.title+" ("+movie.year+") "+"\n"
            }
            return returnString

        }else{
            return ""
            //Toast.makeText(context, "Zero Matches found", Toast.LENGTH_SHORT).show()
        }
    }


    @Composable //box used in searchmovie and searchactor to display movies
    fun MovieCard(movie: Movie){
        Box(modifier = Modifier
            .padding(16.dp)
            .background(
                color = Color(0xFFFFF5EB),
                shape = RoundedCornerShape(10.dp)
            )
            .size(width = 350.dp, height = 470.dp)
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                fontSize = 20.sp,
                color = Color.Black,
                text = "IMDB : ${movie.imdbId} \n" +
                        "Title : ${movie.title} \n" +
                        "Year : ${movie.year} \n" +
                        "Rated : ${movie.rated} \n" +
                        "Released : ${movie.released} \n" +
                        "Runtime : ${movie.runtime} \n" +
                        "Genre : ${movie.genre} \n" +
                        "Director : ${movie.director} \n" +
                        "Writer : ${movie.writer} \n" +
                        "Actors : ${movie.actors} \n" +
                        "Plot : ${movie.plot} \n"
            )
        }
    }

}