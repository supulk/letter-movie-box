package com.example.letter_movie_box

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letter_movie_box.data.Movie
import com.example.letter_movie_box.ui.theme.LettermovieboxTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class SearchActor : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = MainViewModel(application)
        super.onCreate(savedInstanceState)
        setContent {
            ActorSearchWindow(viewModel)
        }
    }
}

@Composable
fun ActorSearchWindow(viewModel: MainViewModel){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var keyword by rememberSaveable { mutableStateOf("") }
    var movies by rememberSaveable { mutableStateOf<List<Movie>>(emptyList()) }
    var keywordUTF8 = URLEncoder.encode(keyword, "UTF-8")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF35637A))
            .verticalScroll(rememberScrollState()) ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.size(120.dp))

        TextField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = { Text("eg: Tim Robbins ") },
            textStyle = TextStyle(fontSize = 18.sp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFFAB56),
                unfocusedContainerColor = Color(0xFFFFF5EB)
            ),
        )

        Spacer(Modifier.size(20.dp))


        Button(modifier = Modifier
            .size(220.dp, 40.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFAB56),
                contentColor = Color.Black
            ),
            onClick = {
            coroutineScope.launch {
                //Toast.makeText(context, "Searching for actors", Toast.LENGTH_SHORT).show()
                if (keyword == ""){
                    Toast.makeText(context, "Value is required", Toast.LENGTH_SHORT).show()
                }else{
                    withContext(Dispatchers.IO) {
                        movies = viewModel.searchActor(keywordUTF8)
                    }
                }
                //Toast.makeText(context, "Search found ${movies[0].title}", Toast.LENGTH_SHORT).show()
            }
        })
        {
            Text("Search Actor")
        }

        if(movies.isNotEmpty()){
            movies.forEach { movie: Movie ->
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
                        text = "IMDB : ${movie?.imdbId} \n" +
                                "Title : ${movie?.title} \n" +
                                "Year : ${movie?.year} \n" +
                                "Rated : ${movie?.rated} \n" +
                                "Released : ${movie?.released} \n" +
                                "Runtime : ${movie?.runtime} \n" +
                                "Genre : ${movie?.genre} \n" +
                                "Director : ${movie?.director} \n" +
                                "Writer : ${movie?.writer} \n" +
                                "Actors : ${movie?.actors} \n" +
                                "Plot : ${movie?.plot} \n"
                    )
                }
            }
        }

    }
}