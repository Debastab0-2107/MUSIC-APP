package com.example.mymusic.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mymusic.R
import com.example.mymusic.Song
import com.example.mymusic.ui.SongRepository


@Composable
fun HomeScreen(modifier: Modifier=Modifier,
               onSongClick: (Long) -> Unit,
               songRepo: SongRepository) {

    val songs =remember{songRepo.getAllSongs()}
    // this for the column like view
    LazyColumn {
        items(songs) { song ->
              SongTile(song,onClick={onSongClick(song.id)})
        }
    }
    /*
    // this for the row like view
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement =  Arrangement.spacedBy(10.dp)
        ) {
            items(items=songs) { song ->
                SongTile(song, onClick = {onSongClick(song.id)})
            }
        }*/

}

@Composable
fun SongTile(song:Song, onClick: ()->Unit){
    Row( modifier = Modifier
                    .padding(10.dp)
                    .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
//        Image(
//            painter = painterResource(R.drawable.cover),
//            contentDescription = null,
//            modifier = Modifier.fillMaxHeight().aspectRatio(1f).weight(0.2f)
//        )
        AsyncImage(
            model= song.getAlbumArtUri(),
            contentDescription = "Album Art",
            modifier = Modifier
                .aspectRatio(1f)
                .padding(10.dp).weight(0.25f)
                .clip(RoundedCornerShape(10.dp))
                .border(1.5.dp, Color.White, RoundedCornerShape(10.dp)),
            fallback = painterResource(R.drawable.img),
            error = painterResource(R.drawable.img)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth().weight(0.75f)
        ) {
            //Song name section
            Text(text = song.title, fontSize = 17.sp)

            //Artist section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    imageVector=Icons.Default.Person,
                    "Artist",
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = song.artist, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
   // HomeScreen(onSongClick = {},)
}