package com.android.ai.androidbasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.android.ai.androidbasics.ui.theme.AndroidBasicsTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBasicsTheme {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text("yesh")
                }
            }

        }
    }
}