package com.example.mymusic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymusic.ui.PlaybackController
import com.example.mymusic.ui.PlayerViewModel
import com.example.mymusic.ui.PlayerViewModelFactory
import com.example.mymusic.ui.SongRepository
import com.example.mymusic.ui.screens.HomeScreen
import com.example.mymusic.ui.screens.NowPlayScreen

import com.example.mymusic.ui.theme.MyMusicTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
    }//recomposition will happen automatically

    private val playerViewModel: PlayerViewModel by viewModels{
        PlayerViewModelFactory(
            applicationContext,
            SongRepository(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_AUDIO
              ) != PackageManager.PERMISSION_GRANTED
            ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        }
        setContent {
            MyMusicTheme {
                MyMusicApp(playerViewModel)
            }
        }
    }
}

//@PreviewScreenSizes
@Composable
fun MyMusicApp(playerViewModel: PlayerViewModel) {

    val navCtrl =rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize().padding(top=20.dp)) { innerPadding ->
        NavHost(navCtrl, startDestination = "/Home"){
            composable("/Home"){
                HomeScreen(
                    modifier=Modifier.padding(innerPadding),
                    onSongClick ={  songId->
                        navCtrl.navigate("/player/${songId}")
                    },
                    SongRepository(context = LocalContext.current)
                )
            }
            /* composable("/Home"){
                 NowPlayScreen(123,  Modifier)
             }*/

            composable("/player/{songId}",
                listOf(navArgument("songId"){ type= NavType.LongType})
            ){  
                val id: Long =it.arguments?.getLong("songId") ?:0
                LaunchedEffect(id) { playerViewModel.play(id) }
                NowPlayScreen(playerViewModel)
                //playerScreen() //id=it.arguments?.getLong("songId") ?:0
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMusicTheme {
       MyMusicApp(viewModel() )
    }
}