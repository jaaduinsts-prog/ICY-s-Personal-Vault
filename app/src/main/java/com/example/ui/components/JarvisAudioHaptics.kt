package com.example.ui.components

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class JarvisAudioHaptics(private val context: Context) {
    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (_: Exception) {
        null
    }

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 60)
    } catch (_: Exception) {
        null
    }

    fun playContractPulse() {
        // Quick sharp haptic + high tone
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(70)
            }
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
        } catch (_: Exception) {}
    }

    fun playRelaxPulse() {
        // Softer dual pulse + low tone
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 50, 40), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60)
            }
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
        } catch (_: Exception) {}
    }

    fun playQuickFlickHaptic() {
        // Sharp tactical pulse for quick flicks
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(50, 220))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(50)
            }
            toneGenerator?.startTone(ToneGenerator.TONE_DTMF_D, 60)
        } catch (_: Exception) {}
    }

    fun playSuccessFanfare() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 200)
        } catch (_: Exception) {}
    }
}
