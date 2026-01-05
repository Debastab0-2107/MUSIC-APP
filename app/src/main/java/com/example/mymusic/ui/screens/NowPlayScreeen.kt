package com.example.mymusic.ui.screens



import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymusic.R
import com.example.mymusic.ui.PlayerViewModel


@Composable
fun NowPlayScreen(
    playerViewModel: PlayerViewModel
){

    val song by playerViewModel.currsong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState() //CHANGED IN PLACE OF MUTABLE
    var sliderPosition by remember { mutableStateOf(0.5f) }

    // ✅ Animatable for rotation control
    val rotation = remember { Animatable(0f) }
    // ✅ Start / Stop rotation based on isPlaying
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                rotation.animateTo(
                    targetValue = rotation.value + 360f,
                    animationSpec = tween(
                        durationMillis = 7000,
                        easing = LinearEasing
                    )
                )
            }
        } else {
            rotation.stop()
        }
    }



    Surface( modifier=Modifier.fillMaxSize()) {
        Box( modifier= Modifier.background(Color.Black).fillMaxSize() ){

            AsyncImage(
                model=song?.getAlbumArtUri(),
                contentDescription = "Album Art",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(14.dp)
                    .alpha(0.75f),
                   // .aspectRatio(1f)
                contentScale = ContentScale.Crop,
                fallback = painterResource(R.drawable.img),
                error = painterResource(R.drawable.img)
            )
            Column(modifier=Modifier.background(Color.Transparent).padding(10.dp).fillMaxSize()){
                    Spacer(modifier = Modifier.size(40.dp))
                    Box(
                        modifier = Modifier.padding(0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model=song?.getAlbumArtUri(),
                            contentDescription = "Album Art",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(45.dp),
                            contentScale = ContentScale.FillWidth,
                                    /*.aspectRatio(1f)
                                    .padding(10.dp)
                                    .graphicsLayer {
                                        rotationZ = rotation.value
                                    }
                                    .clip(CircleShape)
                                    .border(2.5.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop,*/
                            fallback = painterResource(R.drawable.img),
                            error = painterResource(R.drawable.img)
                        )

                        Image(
                            painter = painterResource(R.drawable.module),
                            null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(0.dp)
                        )
                        Row(
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.515f), shape = RoundedCornerShape(35.dp))
                                .padding(5.dp)
                        ){
                            Image(
                                painter=painterResource(R.drawable.roller3),
                                contentDescription = "roller",
                                modifier = Modifier
                                    .size(60.dp)
                                    .graphicsLayer {
                                        rotationZ = rotation.value
                                    }
                            )
                            Spacer( modifier = Modifier.size(50.dp))
                            Image(
                                painter=painterResource(R.drawable.roller3),
                                contentDescription = "roller",
                                modifier = Modifier
                                    .size(60.dp)
                                    .graphicsLayer {
                                        rotationZ = rotation.value
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(30.dp))

                    Text(
                        text=song?.title ?: "Unknown Title",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                        color = Color.White
                    )
                    Text(
                        text=song?.artist ?: "Unknown Title",
                        fontSize = 15.sp,
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
                    color = Color.DarkGray,
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
                     painter= painterResource(R.drawable.pause),
                     contentDescription = "Pause",
                     modifier = Modifier.size(58.dp),
                     tint = Color.White

                 )

             } else {
                 Icon(
                     imageVector = Icons.Filled.PlayArrow,
                     contentDescription = "Play",
                     modifier = Modifier.size(58.dp),
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

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 7000, // speed (higher = slower)
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAnim"
    )
    Surface( modifier=Modifier.fillMaxSize()) {
        Box(modifier= Modifier.background(Color.Black).fillMaxSize() ){

            Image(
                    painter = painterResource(R.drawable.artboard2),
                    null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(14.dp)
                        .alpha(0.75f),
                    contentScale = ContentScale.Crop
                )

            Column(modifier=Modifier.background(Color.Transparent).padding(10.dp).fillMaxSize()){
                Spacer(modifier = Modifier.size(50.dp))
                Box(
                    modifier = Modifier.padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.img),
                        null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(45.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    Image(
                        painter = painterResource(R.drawable.module),
                        null,
                        modifier = Modifier
                            .aspectRatio(1f)
                    )
                    Row(
                        modifier = Modifier.background(Color.White.copy(alpha = 0.615f), shape = RoundedCornerShape(35.dp))
                            .padding(5.dp)
                    ){
                        Image(
                            painter=painterResource(R.drawable.roller2),
                            contentDescription = "roller",
                            modifier = Modifier
                                .size(60.dp)
                                .graphicsLayer {
                                    rotationZ = rotation
                                }
                        )
                        Spacer( modifier = Modifier.size(50.dp))
                        Image(
                            painter=painterResource(R.drawable.roller2),
                            contentDescription = "roller",
                            modifier = Modifier
                                .size(60.dp)
                                .graphicsLayer {
                                    rotationZ = rotation
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.size(30.dp))
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

}

@Preview(showBackground = true)
@Composable
fun GreetinggPreview() {
    NowPlayScreenn( isPlaying = true,
        onPlayPause = {})
}