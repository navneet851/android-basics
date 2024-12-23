package com.android.ai.androidbasics

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import com.android.ai.androidbasics.ui.theme.AndroidBasicsTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SecondActivity : ComponentActivity() {
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val songViewModel by viewModels<ImageViewModel>()
        setContent {
            AndroidBasicsTheme {
                LaunchedEffect(true) {
                    songViewModel.updateSongs(ContentProvider().getAudios(this@SecondActivity))
                }
                val songs = songViewModel.songs
//                LaunchedEffect(true) {
//                    withContext(Dispatchers.IO) {
//                        songs = ContentProvider().getAudios(this@SecondActivity)
//                    }
//                }

                if (songs == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading...")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        items(songs.size) { song ->
                            //val bitmap = contentResolver.loadThumbnail(songs[song].coverUri!!, Size(10, 10), null)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                GlideImage(
                                    modifier = Modifier.size(30.dp),
                                    model = songs[song].coverUri ?: R.drawable.ic_launcher_foreground,
                                    loading = placeholder(R.drawable.ic_launcher_background),
                                    contentDescription = "image"
                                )
                                Log.d("coverUri", songs[song].coverUri.toString())
                                Column {
                                    Text(songs!![song].title)
                                    Text(songs!![song].singer)
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}