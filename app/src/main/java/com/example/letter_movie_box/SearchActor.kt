package com.example.letter_movie_box

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
import androidx.compose.ui.tooling.preview.Preview
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(value = keyword, onValueChange = { keyword = it })

        var ll by rememberSaveable { mutableStateOf("") }
        if(movies.isNotEmpty()){
            ll = movies[0].title
        }


        Button(onClick = {
            coroutineScope.launch {
                Toast.makeText(context, "Searching for actors", Toast.LENGTH_SHORT).show()
                withContext(Dispatchers.IO){
                    movies = viewModel.searchActor(keywordUTF8)
                }
                Toast.makeText(context, "Search found$ll", Toast.LENGTH_SHORT).show()
            }
        })
        {
            Text("Search Actor")
        }

        Text(text = ll)

    }
}