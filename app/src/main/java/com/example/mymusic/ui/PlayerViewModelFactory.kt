package com.example.mymusic.ui


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
    PLAYERVIEWMODELFACTORY

    WE HAVE TO CREAT AN FACTORY BECAUSE  A VIEW IS EXPECTED TO RETURN AN CONSTRUCTOR
    WITHOUT ANY TYPE OF ARGUMENTS

    SO WHEN AN VIEW MODEL HAS ARGUMENTS IT ASK THE FACTORY HOW TO INITIALISE IT AND
    CREAT THE CONSTRUCTOR AND ATLAST CALL THE FACTORY IN MAIN ACTIVITY

*/
class PlayerViewModelFactory(
    private val context: Context,
    private val songRepository: SongRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(context, songRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
