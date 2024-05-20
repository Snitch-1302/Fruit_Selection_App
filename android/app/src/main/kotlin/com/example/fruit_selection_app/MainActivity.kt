package com.example.fruit_selection_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.*

class MainActivity : FlutterActivity() {
    private lateinit var tts: TextToSpeech
    private val CHANNEL = "com.example.app/tts"
    private val REQUEST_CODE = 1001

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "speak") {
                val data = call.arguments as Map<String, String>
                val text = "Name: ${data["name"]}, Address: ${data["addressLine1"]}, City: ${data["city"]}, State: ${data["state"]}, Country: ${data["country"]}"
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                result.success(null)
            } else if (call.method == "startFruitSelection") {
                startFruitSelectionActivity()
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    private fun startFruitSelectionActivity() {
        Log.d("MainActivity", "Starting FruitSelectionActivity")
        val intent = Intent(this, FruitSelectionActivity::class.java)
        try {
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to start FruitSelectionActivity", e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedFruits = data?.getStringArrayListExtra("selectedFruits")
            Log.d("MainActivity", "Selected fruits: $selectedFruits")

            selectedFruits?.let {
                flutterEngine?.dartExecutor?.binaryMessenger?.let { messenger ->
                    MethodChannel(messenger, CHANNEL).invokeMethod("fruitsSelected", selectedFruits)
                }
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
