package com.android.ai.androidbasics

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.android.ai.androidbasics.ui.theme.AndroidBasicsTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private val airplaneModeBroadcast = BroadCastReceiver()
    private val context = this

    private val viewModel by viewModels<ImageViewModel>()
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        workManager = WorkManager.getInstance(this)
        //getting a broadcast of airplane mode
        registerReceiver(airplaneModeBroadcast, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        //cretes notification channel
        notificationChannel1()
        notificationChannel2()
        //requested post permission
        requestPostPermission()
        //to show request
        //airplaneModeBroadcast.showNotification(this)
        // implemented intent Filter for text and image
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
                                Intent(applicationContext, ForegroundService::class.java).also { start ->
                                    start.action = "start"
                                    startService(start)
                                }
                            }
                        ) {
                            Text("Start Service")
                        }
                        Button(
                            onClick = {
                                Intent(applicationContext, ForegroundService::class.java).also { start ->
                                    start.action = "stop"
                                    startService(start)
                                }
                            }
                        ) {
                            Text("Stop Service")
                        }
                        Button(
                            onClick = {
                                airplaneModeBroadcast.showNotification(context)
                            }
                        ) {
                            Text("Show Notification")
                        }
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeBroadcast)
    }

    private fun notificationChannel1(){
        val notificationManager : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel("101", "broadcast check", NotificationManager.IMPORTANCE_HIGH))
    }
    private fun notificationChannel2(){
        val notificationManager : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel("102", "work", NotificationManager.IMPORTANCE_LOW))
    }

    fun requestPostPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.POST_NOTIFICATIONS), 101)
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
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()

                val request = OneTimeWorkRequestBuilder<SyncingData>()
                    .setInputData(
                        workDataOf(
                            "sync_data" to receivedText
                        )
                    )
                    .setConstraints(constraints)
                    .build()
                workManager.enqueue(request)
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

