package com.android.ai.androidbasics

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class ContentProvider {
    suspend fun getAudios(context: Context) : List<Song>{
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ARTIST,
        )
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.AudioColumns.TITLE} ASC"
        )?.use{cursor ->
            val songId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val songTitle = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
            val songSinger = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)

            var songIndex = 0
            while (cursor.moveToNext()){
                val id = cursor.getLong(songId)
                val title = cursor.getString(songTitle)
                val singer = cursor.getString(songSinger)
                val coverUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id)

                songs.add(Song(songIndex, title, singer, coverUri))

                songIndex++
            }


        }
        return songs
    }

}
data class Song(
    val id: Int,
    val title: String,
    val singer: String,
    val coverUri: Uri?

)