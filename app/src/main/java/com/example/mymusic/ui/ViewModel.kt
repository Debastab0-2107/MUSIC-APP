package com.example.mymusic.ui



import android.content.Context
import androidx.lifecycle.ViewModel

class PlayerViewModel(
    context: Context,
    songRepo: SongRepository,
) : ViewModel() {

    //One instance created here
    private val playerbackCtrller = PlaybackController(context, songRepo)

    //Expose state
    val isPlaying = playerbackCtrller.isPlaying
    val currsong = playerbackCtrller.currSong

    // Expose actions (THIS IS IMPORTANT)
    fun play(songId : Long) {
        playerbackCtrller.play(songId)
    }
    fun pause() {
        playerbackCtrller.pause()
    }
    fun resume() {
        playerbackCtrller.resume()
    }
}