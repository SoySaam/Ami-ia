package com.tuusuario.cursorassistant

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import kotlin.random.Random

/**
 * Sistema de Audio y Vibración Emocional para Ami-IA
 * Proporciona feedback auditivo y háptico basado en emociones
 */
class EmotionalSoundManager(private val context: Context) {
    
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()
    private var vibrator: Vibrator? = null
    
    init {
        initializeSoundSystem()
        initializeVibrationSystem()
    }
    
    private fun initializeSoundSystem() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
            
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()
    }
    
    private fun initializeVibrationSystem() {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    
    /**
     * Reproducir sonido basado en emoción
     */
    fun playEmotionalSound(emotion: String) {
        try {
            // Generar tonos sintéticos ya que no tenemos archivos de audio
            generateSyntheticTone(emotion)
        } catch (e: Exception) {
            Log.e("EmotionalSound", "Error playing sound for emotion: $emotion", e)
        }
    }
    
    /**
     * Generar tonos sintéticos para cada emoción
     */
    private fun generateSyntheticTone(emotion: String) {
        val (frequency, duration) = when (emotion.lowercase()) {
            "feliz", "alegria", "euforia" -> Pair(800f, 300)
            "triste", "melancolia", "tristeza" -> Pair(200f, 800)
            "enojada", "enojo", "ira" -> Pair(150f, 200)
            "aburrida", "apatia" -> Pair(300f, 500)
            "nerviosa", "ansiosa", "desasosiego" -> Pair(600f, 150)
            "enamorada", "ternura" -> Pair(900f, 400)
            "serenidad", "calma" -> Pair(400f, 600)
            "contemplacion" -> Pair(500f, 700)
            "fascinacion" -> Pair(700f, 250)
            "panico" -> Pair(1000f, 100)
            else -> Pair(440f, 300) // Nota LA por defecto
        }
        
        // Usar MediaPlayer para generar tono (simulado con vibración por ahora)
        playVibrationPattern(emotion)
    }
    
    /**
     * Reproducir patrón de vibración específico para cada emoción
     */
    fun playVibrationPattern(emotion: String) {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val pattern = getVibrationPattern(emotion)
                val effect = VibrationEffect.createWaveform(pattern.first, pattern.second)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(getSimpleVibrationDuration(emotion))
            }
        }
    }
    
    /**
     * Obtener patrón de vibración para cada emoción
     */
    private fun getVibrationPattern(emotion: String): Pair<LongArray, IntArray> {
        return when (emotion.lowercase()) {
            "feliz", "alegria", "euforia" -> {
                // Patrón alegre: vibraciones cortas y rápidas
                Pair(
                    longArrayOf(0, 100, 50, 100, 50, 100),
                    intArrayOf(0, 255, 0, 255, 0, 255)
                )
            }
            "triste", "melancolia", "tristeza" -> {
                // Patrón triste: vibración larga y suave
                Pair(
                    longArrayOf(0, 800, 200, 400),
                    intArrayOf(0, 100, 0, 50)
                )
            }
            "enojada", "enojo", "ira" -> {
                // Patrón enojado: vibraciones intensas e irregulares
                Pair(
                    longArrayOf(0, 200, 100, 300, 50, 200),
                    intArrayOf(0, 255, 0, 255, 0, 255)
                )
            }
            "aburrida", "apatia" -> {
                // Patrón aburrido: vibración monótona
                Pair(
                    longArrayOf(0, 500, 500, 500),
                    intArrayOf(0, 80, 0, 80)
                )
            }
            "nerviosa", "ansiosa", "desasosiego" -> {
                // Patrón nervioso: vibraciones rápidas e irregulares
                Pair(
                    longArrayOf(0, 50, 30, 50, 30, 50, 30, 50),
                    intArrayOf(0, 200, 0, 200, 0, 200, 0, 200)
                )
            }
            "enamorada", "ternura" -> {
                // Patrón amoroso: vibraciones suaves en forma de corazón
                Pair(
                    longArrayOf(0, 150, 100, 150, 200, 300),
                    intArrayOf(0, 180, 0, 180, 0, 220)
                )
            }
            "serenidad", "calma" -> {
                // Patrón sereno: vibración suave y constante
                Pair(
                    longArrayOf(0, 600),
                    intArrayOf(0, 120)
                )
            }
            "contemplacion" -> {
                // Patrón contemplativo: vibraciones pausadas
                Pair(
                    longArrayOf(0, 200, 300, 200, 300, 200),
                    intArrayOf(0, 150, 0, 150, 0, 150)
                )
            }
            "fascinacion" -> {
                // Patrón fascinante: vibraciones crecientes
                Pair(
                    longArrayOf(0, 100, 50, 150, 50, 200),
                    intArrayOf(0, 100, 0, 150, 0, 200)
                )
            }
            "panico" -> {
                // Patrón pánico: vibraciones muy rápidas
                Pair(
                    longArrayOf(0, 50, 25, 50, 25, 50, 25, 50),
                    intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)
                )
            }
            else -> {
                // Patrón neutral
                Pair(
                    longArrayOf(0, 200),
                    intArrayOf(0, 150)
                )
            }
        }
    }
    
    /**
     * Duración simple de vibración para versiones Android antiguas
     */
    private fun getSimpleVibrationDuration(emotion: String): Long {
        return when (emotion.lowercase()) {
            "feliz", "alegria", "euforia" -> 200L
            "triste", "melancolia", "tristeza" -> 800L
            "enojada", "enojo", "ira" -> 300L
            "aburrida", "apatia" -> 500L
            "nerviosa", "ansiosa", "desasosiego" -> 150L
            "enamorada", "ternura" -> 400L
            "serenidad", "calma" -> 600L
            "contemplacion" -> 350L
            "fascinacion" -> 250L
            "panico" -> 100L
            else -> 300L
        }
    }
    
    /**
     * Feedback háptico para interacciones táctiles
     */
    fun playInteractionFeedback(interactionType: String) {
        when (interactionType) {
            "touch" -> playLightVibration()
            "care" -> playWarmVibration()
            "ignore" -> playSadVibration()
            "talk" -> playExcitedVibration()
            "long_press" -> playConfirmationVibration()
        }
    }
    
    private fun playLightVibration() {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(50)
            }
        }
    }
    
    private fun playWarmVibration() {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(
                    longArrayOf(0, 100, 50, 100),
                    intArrayOf(0, 180, 0, 180),
                    -1
                )
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(200)
            }
        }
    }
    
    private fun playSadVibration() {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(400, 100)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(400)
            }
        }
    }
    
    private fun playExcitedVibration() {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(
                    longArrayOf(0, 50, 30, 50, 30, 50),
                    intArrayOf(0, 200, 0, 200, 0, 200),
                    -1
                )
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(longArrayOf(0, 50, 30, 50, 30, 50), -1)
            }
        }
    }
    
    private fun playConfirmationVibration() {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(
                    longArrayOf(0, 100, 100, 100, 100, 200),
                    intArrayOf(0, 150, 0, 150, 0, 200),
                    -1
                )
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(500)
            }
        }
    }
    
    /**
     * Generar sonido procedural basado en estado emocional
     */
    fun generateProceduralSound(arousal: Float, valence: Float, dominance: Float) {
        // Mapear valores emocionales a características de sonido
        val baseFrequency = 200f + (valence * 6f) // 200-800 Hz
        val modulation = arousal / 100f // Intensidad
        val duration = (300 + (dominance * 3)).toInt() // 300-600ms
        
        // Simular con vibración por ahora
        playProceduralVibration(baseFrequency.toInt(), duration, modulation)
    }
    
    private fun playProceduralVibration(frequency: Int, duration: Int, intensity: Float) {
        vibrator?.let { vib ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val amplitudeValue = (intensity * 255).toInt().coerceIn(1, 255)
                val effect = VibrationEffect.createOneShot(duration.toLong(), amplitudeValue)
                vib.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(duration.toLong())
            }
        }
    }
    
    /**
     * Obtener información de capacidades de audio
     */
    fun getAudioCapabilities(): Map<String, Boolean> {
        return mapOf(
            "VibrationSupported" to (vibrator?.hasVibrator() ?: false),
            "SoundPoolActive" to (soundPool != null),
            "AmplitudeControl" to (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        )
    }
    
    /**
     * Limpiar recursos
     */
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}