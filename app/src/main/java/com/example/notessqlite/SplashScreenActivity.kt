package com.example.notessqlite


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the splash screen layout as the content view
        setContentView(R.layout.activity_splash_screen)

        // Optionally, you can add code here to perform any initialization or setup tasks
        // such as loading data or checking user authentication status.

        // Example: Transition to the main activity after a delay
        val splashScreenDuration = 1000L // 1 seconds
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        // Start the main Activity after the specified duration
        // using a coroutine for asynchronous behavior
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashScreenDuration)
            startActivity(mainActivityIntent)
            finish() // Close the splash screen Activity
        }
    }
}
