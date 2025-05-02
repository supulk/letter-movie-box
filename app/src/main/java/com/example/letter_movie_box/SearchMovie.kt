package com.example.letter_movie_box

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.letter_movie_box.data.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovie : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = MainViewModel(application)
        super.onCreate(savedInstanceState)
        setContent {
            MovieSearchWindow(viewModel)
        }
    }
}

@Composable
fun MovieSearchWindow(viewModel: MainViewModel){
    val context = LocalContext.current
    var keyword by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var matchFound by rememberSaveable { mutableStateOf(false) }
    var movie: Movie? by rememberSaveable { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = keyword, onValueChange = { keyword = it })




        Button(
            onClick = {
                if (keyword != "") {
                    coroutineScope.launch {
                        Toast.makeText(context, "Searching", Toast.LENGTH_SHORT).show()
                        movie = fetchMovie(context, keyword)
                        Toast.makeText(context, "Got ${movie?.title}", Toast.LENGTH_SHORT).show()
                        keyword = ""
                    }
                }
            }
        )
        {
            Text("Search Movie")
        }


        Text(
            text = "${movie?.title} (${movie?.year})"
        )


        Button(onClick = {
            movie?.let {
                coroutineScope.launch {
                    viewModel.addtoDb(it)
                }
            }
        },enabled = movie != null
        ) {
            Text("Add this movie")
        }
    }
}

suspend fun fetchMovie(context: Context, keyword:String):Movie{
    val urlString = "omdbapi.com/?t="+ keyword +"&apikey=38b008c3"
    val url = URL(urlString)
    val con: HttpURLConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpURLConnection

    var stb = StringBuilder()

    withContext(Dispatchers.IO){
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null){
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    val fetchedMovie = parseToMovie(stb)
    Toast.makeText(context, "Got ${fetchedMovie.title}", Toast.LENGTH_SHORT).show()
    return fetchedMovie
}

fun parseToMovie(stb: StringBuilder) : Movie{
    val jsonObject = JSONObject(stb.toString())

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
