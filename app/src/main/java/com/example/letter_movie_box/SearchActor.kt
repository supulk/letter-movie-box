package com.example.letter_movie_box

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letter_movie_box.data.Movie
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
    var keyword by rememberSaveable { mutableStateOf("") } //store search keyword
    var movies by rememberSaveable { mutableStateOf<List<Movie>>(emptyList()) } //store results as a array
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

        TextField( //search textfield
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


        Button(modifier = Modifier //search btn
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
                    withContext(Dispatchers.IO) { //execute in a threadpool optimized for input output actions
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
                viewModel.MovieCard(movie)
            }
        }

    }
}