package com.example.letter_movie_box

import android.app.Application
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.example.letter_movie_box.data.Movie
import com.example.letter_movie_box.data.MovieDAO
import com.example.letter_movie_box.data.MovieDatabase
import com.example.letter_movie_box.ui.theme.LettermovieboxTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

lateinit var db: MovieDatabase
lateinit var movieDao: MovieDAO

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainViewModel(application)
        setContent {
            MainWindow(viewModel)
        }
    }
}

@Composable
fun MainWindow(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current;
        Button(onClick = {
            coroutineScope.launch {
                addMoviesTODb(viewModel)
                Toast.makeText(context, "Movies Added", Toast.LENGTH_SHORT).show()
            }
        }){
            Text("Add to DB")
        }
        Button(onClick = {
            val intent = Intent(context, SearchMovie::class.java)
            context.startActivity(intent)
        }) {
            Text("Search for a movie")
        }
        Button(onClick = {
            val intent = Intent(context, SearchActor::class.java)
            context.startActivity(intent)
        }) {
            Text("Search for actors")
        }
    }
}

suspend fun addMoviesTODb(viewModel: MainViewModel){
    val movies = listOf(
        Movie(
            imdbId = "tt0111161",
            title = "The Shawshank Redemption",
            year = "1994",
            rated = "R",
            released = "14 Oct 1994",
            runtime = "142 min",
            genre = "Drama",
            director = "Frank Darabont",
            writer = "Stephen King, Frank Darabont",
            actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
            plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
        ),
        Movie(
            imdbId = "tt2313197",
            title = "Batman: The Dark Knight Returns, Part 1",
            year = "2012",
            rated = "PG-13",
            released = "25 Sep 2012",
            runtime = "76 min",
            genre = "Animation, Action, Crime, Drama, Thriller",
            director = "Jay Oliva",
            writer = "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
            actors = "Peter Weller, Ariel Winter, David Selby, Wade Williams",
            plot = "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"
        ),
        Movie(
            imdbId = "tt0167260",
            title = "The Lord of the Rings: The Return of the King",
            year = "2003",
            rated = "PG-13",
            released = "17 Dec 2003",
            runtime = "201 min",
            genre = "Action, Adventure, Drama",
            director = "Peter Jackson",
            writer = "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
            actors = "Elijah Wood, Viggo Mortensen, Ian McKellen",
            plot = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
        ),
        Movie(
            imdbId = "tt1375666",
            title = "Inception",
            year = "2010",
            rated = "PG-13",
            released = "16 Jul 2010",
            runtime = "148 min",
            genre = "Action, Adventure, Sci-Fi",
            director = "Christopher Nolan",
            writer = "Christopher Nolan",
            actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
            plot = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
        ),
        Movie(
            imdbId = "tt0133093",
            title = "The Matrix",
            year = "1999",
            rated = "R",
            released = "31 Mar 1999",
            runtime = "136 min",
            genre = "Action, Sci-Fi",
            director = "Lana Wachowski, Lilly Wachowski",
            writer = "Lilly Wachowski, Lana Wachowski",
            actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
            plot = "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
        )
    )
    movies.forEach{ movie ->
        viewModel.addtoDb(movie)
    }
}