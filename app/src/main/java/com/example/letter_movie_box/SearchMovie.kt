package com.example.letter_movie_box

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

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
    var movie: Movie? by rememberSaveable { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF35637A)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.size(120.dp))

            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                placeholder = { Text("eg: Batman") },
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
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                onClick = {
                    if (keyword != "") {
                        coroutineScope.launch {
                            //Toast.makeText(context, "Searching", Toast.LENGTH_SHORT).show()
                            movie = viewModel.fetchMovie(context, keyword)
                            //Toast.makeText(context, "Got ${movie?.title}", Toast.LENGTH_SHORT).show()
                            keyword = ""
                        }
                    }else{
                        Toast.makeText(context, "Value is Required", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            {
                Text("Search Movie")
            }


            if (movie!=null){
                viewModel.MovieCard(movie!!)
            }else{
                Box(modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFFFF5EB),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(width = 350.dp, height = 470.dp)
                ) {}
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 70.dp),
            contentAlignment = Alignment.Center
        ){

            Button(modifier = Modifier
                .size(220.dp, 40.dp),
                shape = RoundedCornerShape(10.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFAB56),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                onClick = {
                    movie?.let {
                        coroutineScope.launch {
                            viewModel.addtoDb(it)
                            Toast.makeText(context, "Movie Added", Toast.LENGTH_SHORT).show()
                            movie = null
                        }
                    }
                },
                enabled = (movie != null)
            ) {
                Text("Add this movie")
            }
        }
    }
}


