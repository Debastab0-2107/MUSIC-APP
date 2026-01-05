package com.example.mymusic.ui
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.mymusic.Song

//<uses-permission android:name="android.permission.READ_MEDIA_AUDIO"/> // dependency ,add in manifest
//MediaStore is a sorta inbuilt database that has access to all files in android.
//      if an file needed from device(mostly media)

class SongRepository(private val context: Context) {

    fun getAllSongs(): List<Song> {
        val songs = mutableListOf<Song>()

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
            ),
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            null // "${MediaStore.Audio.Media.ARTIST} ASC"// sorts asc /!\never tried
        )

        cursor?.use {
            val idCol     :Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol  :Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol :Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val AlbumCol  :Int = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while(it.moveToNext()){
                val id     :Long   = it.getLong(idCol)
                val title  :String = it.getString(titleCol)
                val artist :String = it.getString(artistCol)
                val albumid:Long   = it.getLong(AlbumCol)

                songs.add( Song( id, title, artist, albumid ))
            }
        }
        return songs
    }

    // query ( FROM __, SELECT __ '*'==null,WHERE __<=> ?, the array value of '?'s , ORDER BY(__) )
    fun getSongById(songId: Long): Song? {

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
            ),
            "${MediaStore.Audio.Media._ID} =?" ,
            arrayOf(songId.toString()),
            null
        )

        cursor?.use{
            if (it.moveToFirst()){
                return Song(
                    id     =  songId,
                    title  =  it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                    artist =  it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                   // albumArtUri = getAlbumArtUri(songId)
                    albumid = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                )
            }
        }

        return null
    }

}