package com.example.mymusic

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    //val duration: Int?,
    val albumid : Long?
){
    fun getUri(): Uri {
        return ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            id
        )
    }
    fun getAlbumArtUri(): Uri? {
        return albumid?.let {
             ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                it
            )
        }

    }
}