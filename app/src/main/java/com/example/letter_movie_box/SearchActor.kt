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
import com.example.letter_movie_box.ui.theme.LettermovieboxTheme
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    var actorName by rememberSaveable { mutableStateOf("default") }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        //TextField()
        Button(onClick = {
            coroutineScope.launch {
                Toast.makeText(context, "Searching for actors", Toast.LENGTH_SHORT).show()
                viewModel.searchActor(actorName)
            }
        })
        {
            Text("Search Actor")
        }
    }
}