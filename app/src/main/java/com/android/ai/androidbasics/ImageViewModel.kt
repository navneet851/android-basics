package com.android.ai.androidbasics

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {
    var text by mutableStateOf<String?>(null)
        private set
    var uri by mutableStateOf<Uri?>(null)
        private set

    var songs by mutableStateOf<List<Song>?>(null)
        private set

    fun updateText(newText: String?) {
        text = newText
    }

    fun updateUri(newUri: Uri?) {
        uri = newUri
    }

    fun updateSongs(newSongs: List<Song>?){
        songs = newSongs
    }


}