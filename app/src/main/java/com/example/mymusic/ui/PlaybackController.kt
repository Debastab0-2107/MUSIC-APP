package com.example.mymusic.ui

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.mymusic.Song
import kotlinx.coroutines.flow.MutableStateFlow

class PlaybackController(
    private val context : Context,
    private val songRepo : SongRepository) {

    private val mp = MediaPlayer()
    private val _isPlaying = MutableStateFlow(false)
    private val _currSong = MutableStateFlow<Song?>(null)


    public final val isPlaying: MutableStateFlow<Boolean> = _isPlaying
    public final val currSong: MutableStateFlow<Song?> = _currSong

    //! The song speed randomly going up and sometimes also sound glitchy, its a emulator problem (maybe)

    fun play(songId: Long){
        _currSong.value= songRepo.getSongById(songId)
        val uri =_currSong.value?.getUri() ?:return

        mp.reset()
        mp.setDataSource(context, uri)
        Log.d("MediaPlayer", "Speed=${mp.playbackParams.speed}AAAAAAAAAAAAAAA")
        mp.prepare()

        mp.playbackParams=mp.playbackParams.setSpeed(1.0f)
        mp.start()
        _isPlaying.value=true
    }

    //PAUSE
    fun pause(){
        if(_isPlaying.value){
            mp.pause()
            _isPlaying.value =false
        }
    }

    //RESUME
    fun resume(){
        if(!_isPlaying.value){
            mp.start()
            _isPlaying.value =true
        }
    }
}