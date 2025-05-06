package com.example.letter_movie_box

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letter_movie_box.data.Movie
import com.example.letter_movie_box.data.MovieDAO
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainViewModel(application)
        var dao : MovieDAO
        setContent {
            MainWindow(viewModel)
        }
    }
}

@Composable
fun MainWindow(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var browseKeyword by rememberSaveable { mutableStateOf("") } //collect textfield input
    var browseResults by rememberSaveable { mutableStateOf("") } //collect result from viemodel
    var isBrowseOn by rememberSaveable { mutableStateOf(false) } //boolean to decide textfield visibility

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF35637A))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.size(120.dp))

        Button(modifier = Modifier  //add to db btn
            .size(220.dp, 40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFAB56),
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            ),
            onClick = {
            coroutineScope.launch {
                addMoviesTODb(viewModel)
                Toast.makeText(context, "Movies Added", Toast.LENGTH_SHORT).show()
            }
        }){
            Text(
                text = "Add to DB",
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.size(10.dp))

        Button(modifier = Modifier  //search film btn
            .size(220.dp, 40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFAB56),
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            ),
            onClick = {
            val intent = Intent(context, SearchMovie::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "Search for a movie",
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.size(10.dp))

        Button(modifier = Modifier //search actor btn
            .size(220.dp, 40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFAB56),
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            ),
            onClick = {
            val intent = Intent(context, SearchActor::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "Search for actors",
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.size(10.dp))

        Button(modifier = Modifier //browse film btn
            .size(220.dp, 40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFAB56),
                contentColor = Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            ),
            onClick = {
                isBrowseOn = !isBrowseOn
                browseResults = ""
            }){
            Text(
                text = "Browse films",
                fontSize = 17.sp
            )
        }


        Spacer(Modifier.size(10.dp))

        if (isBrowseOn){
            TextField( //browse movies textfield, toggled by browse btn
                modifier = Modifier.size(220.dp, 50.dp),
                value = browseKeyword,
                onValueChange = {newKeyword ->
                    browseKeyword = newKeyword //constantly getting results from api when input changes and assign them to a variable
                    if (newKeyword.isNotEmpty()){
                        coroutineScope.launch {
                            val result = viewModel.fetchMatchingMovies(context, newKeyword)
                            if (result.isNotEmpty()) {
                                browseResults = result
                            }
                        }
                    }else{browseResults = ""}
                },
                textStyle = TextStyle(fontSize = 17.sp),
                shape = RoundedCornerShape(10.dp),
            )

            if (browseResults != ""){ //show the results if not empty
                Box(modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color(0xFF35637A),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(350.dp)
                    .wrapContentSize(Alignment.Center) ,
                ){
                    Text(
                        text = browseResults,
                        color = Color.White
                    )
                }
            }
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