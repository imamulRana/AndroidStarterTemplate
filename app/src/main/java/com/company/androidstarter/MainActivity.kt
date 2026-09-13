package com.company.androidstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.company.androidstarter.core.ui.JSONTest
import com.company.androidstarter.core.ui.theme.AndroidStarterTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var client: HttpClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidStarterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JSONTest(modifier = Modifier.padding(innerPadding), client = client)
                }
            }
        }
    }
}