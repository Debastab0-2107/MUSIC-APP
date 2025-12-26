package com.example.mymusic.ui.screens



import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymusic.R
import com.example.mymusic.ui.PlayerViewModel


@Composable
fun NowPlayScreen(
    playerViewModel: PlayerViewModel
){
    val isPlaying by playerViewModel.isPlaying.collectAsState() //CHANGED IN PLACE OF MUTABLE
    var sliderPosition by remember { mutableStateOf(0.5f) }


    Surface( modifier=Modifier.fillMaxSize()) {
        Column(modifier=Modifier.background(Color.White).padding(10.dp).fillMaxSize()){
            Image(
                painter = painterResource(R.drawable.cover),
                null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text="Song Title",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                color = Color.Black
            )
            Text(
                text="Artist Name ",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 12.dp),
                color = Color.Black
            )
            SongProgress(value = sliderPosition, onValueChange = { sliderPosition = it } )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                PlaybackControl({}, icon = Icons.Default.Face, "shuffle")
                PlaybackControl({}, icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft, "prev")
                CrossfadeIcon(
                    isPlaying = isPlaying,
                    onToggle = {
                        if (isPlaying){
                            playerViewModel.pause()
                        }else{
                            playerViewModel.resume()
                        }
                    } ,
                )
                PlaybackControl({}, icon = Icons.AutoMirrored.Filled.KeyboardArrowRight, "next")
                PlaybackControl({}, icon = Icons.Default.FavoriteBorder, "fav")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongProgress(value: Float, onValueChange: (Float) -> Unit) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        //valueRange= 0f..10f,
        //modifier = Modifier,
        thumb = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .shadow(4.dp, shape = CircleShape)
                    .background(Color.White, CircleShape)
            )
        },
        track = {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            ) {
                val progress: Float = it.value / it.valueRange.endInclusive

                drawRoundRect(
                    color = Color.LightGray,
                    cornerRadius = CornerRadius(50f, 50f)
                )
                drawRoundRect(
                    color = Color.White,
                    size = Size(size.width * progress, size.height),
                    cornerRadius = CornerRadius(50f, 50f)
                )
            }
        }
    )
}
@Composable
fun PlaybackControl(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
        modifier = modifier
    ) {
        //box {icon}
        Box(
            modifier = Modifier.fillMaxSize()
            //.clip(CircleShape)
            //.shadow(6.dp, CircleShape)
            //.background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,   //MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun CrossfadeIcon(
    isPlaying: Boolean,
    onToggle:() -> Unit
){

     IconButton(onClick={ onToggle()}
     ) {
         Crossfade(
             targetState = isPlaying,
             animationSpec = tween(durationMillis = 100),
             label = "PlayPauseCrossfade"
         ){ playingState ->
             if (playingState) {
                 Icon(
                     imageVector = Icons.Filled.Settings,
                     contentDescription = "Pause",
                     modifier = Modifier.size(48.dp),
                     tint = Color.White

                 )

             } else {
                 Icon(
                     imageVector = Icons.Filled.PlayArrow,
                     contentDescription = "Play",
                     modifier = Modifier.size(48.dp),
                     tint = Color.White
                 )
             }
         }
     }
}

/*
      JUST FOR CHECKING THE UI
      */
@Composable
fun NowPlayScreenn(
    isPlaying: Boolean,
    onPlayPause: () -> Unit
){
    val isPlaying by remember { mutableStateOf(false)} //CHANGED IN PLACE OF MUTABLE
    var sliderPosition by remember { mutableStateOf(0.5f) }


    Surface( modifier=Modifier.fillMaxSize()) {
        Column(modifier=Modifier.background(Color.Black).padding(10.dp).fillMaxSize()){
            Spacer(modifier = Modifier.size(40.dp))
            Image(
                painter = painterResource(R.drawable.cover),
                null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(2.5.dp, Color.White,RoundedCornerShape(30.dp) )
            )
            Text(
                text="Song Title",
                fontSize = 27.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                color = Color.White
            )
            Text(
                text="Artist Name ",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(start = 12.dp),
                color = Color.White
            )
            SongProgress(value = sliderPosition, onValueChange = { sliderPosition = it } )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                PlaybackControl({}, icon = Icons.Default.Face, "shuffle")
                PlaybackControl({}, icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft, "prev")
                CrossfadeIcon(
                    isPlaying = isPlaying,
                    onToggle = {
                        if (isPlaying){
                           // playerViewModel.pause()
                        }else{
                           // playerViewModel.resume()
                        }
                    } ,
                )
                PlaybackControl({}, icon = Icons.AutoMirrored.Filled.KeyboardArrowRight, "next")
                PlaybackControl({}, icon = Icons.Default.FavoriteBorder, "fav")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetinggPreview() {
    NowPlayScreenn( isPlaying = true,
        onPlayPause = {})
}