package com.android.ai.androidbasics

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.ai.androidbasics.ui.theme.AndroidBasicsTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ImageViewModel>()
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            AndroidBasicsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = viewModel.text ?: "No text received",
                            modifier = Modifier.padding(16.dp)
                        )
                        GlideImage(
                            model = viewModel.uri?: R.drawable.ic_launcher_foreground,
//                            model = "https://www.crucial.in/content/dam/crucial/articles/pc-users/how-to-make-your-laptop-run-faster/uninstall.jpg.transform/medium-jpg/img.jpg",
                            loading = placeholder(R.drawable.ic_launcher_background),
                            contentDescription = "image",
                            modifier = Modifier.size(200.dp)
                        )
                        Button(
                            onClick = {
                                Intent(
                                    applicationContext,
                                    SecondActivity::class.java
                                ).also { intent ->
                                    startActivity(intent)
                                }

                            }
                        ) {
                            Text("Explicit Activity")
                        }

                        Button(
                            onClick = {
                                Intent(Intent.ACTION_MAIN).also { intent ->
                                    intent.`package` = "com.google.android.youtube"
                                    startActivity(intent)
                                }
                            }
                        ) {
                            Text("Expilcitly App to youtube")
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
//                                    type = "message/rfc822" // Restrict to email apps
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("nav5051neet@gmail.com"))
                                    putExtra(Intent.EXTRA_SUBJECT, "Sending Intent")
                                    putExtra(Intent.EXTRA_TEXT, "This is the email body content.")
                                }
                                val chooser = Intent.createChooser(intent, "webpage")
                                if (intent.resolveActivity(packageManager) != null) {
                                    startActivity(chooser)
                                }
                            }
                        ) {
                            Text("Second")
                        }

                    }
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        when {
            intent.action == Intent.ACTION_SEND && intent.type == "text/plain" -> {
                val receivedText: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
                viewModel.updateText(receivedText)
            }
            intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true -> {
                val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM)
                }
                viewModel.updateUri(imageUri)
            }
        }
    }

}

