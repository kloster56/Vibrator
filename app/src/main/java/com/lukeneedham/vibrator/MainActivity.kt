package com.lukeneedham.vibrator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lukeneedham.vibrator.ui.main.MainPage
import com.lukeneedham.vibrator.util.Vibrator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage(
                setVibrating = ::setVibrating
            )
        }
    }

    private fun setVibrating(isVibrating: Boolean) {
        if (isVibrating) {
            // Start the VibrationService
            val startServiceIntent = Intent(this, VibrationService::class.java)
            startService(startServiceIntent)
        } else {
            // Stop the VibrationService
            val stopServiceIntent = Intent(this, VibrationService::class.java)
            stopService(stopServiceIntent)
        }
    }
}
