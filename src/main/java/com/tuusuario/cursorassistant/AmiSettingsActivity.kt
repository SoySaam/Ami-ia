package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad de Configuración para Ami-IA
 * Permite personalizar la experiencia de la amiga virtual
 */
class AmiSettingsActivity : AppCompatActivity() {
    
    private lateinit var preferences: SharedPreferences
    private lateinit var emotionalSystem: EmotionalSystem
    private lateinit var soundManager: EmotionalSoundManager
    
    // UI Elements
    private lateinit var soundEnabledSwitch: Switch
    private lateinit var vibrationEnabledSwitch: Switch
    private lateinit var notificationsEnabledSwitch: Switch
    private lateinit var autonomyEnabledSwitch: Switch
    private lateinit var interactionSensitivitySeekBar: SeekBar
    private lateinit var ballSizeSeekBar: SeekBar
    private lateinit var personalityText: TextView
    private lateinit var resetButton: Button
    private lateinit var statsButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ami_settings)
        
        initializeComponents()
        setupUI()
        loadCurrentSettings()
    }
    
    private fun initializeComponents() {
        preferences = getSharedPreferences("ami_settings", Context.MODE_PRIVATE)
        emotionalSystem = EmotionalSystem(this)
        soundManager = EmotionalSoundManager(this)
        
        // Initialize UI elements
        soundEnabledSwitch = findViewById(R.id.soundEnabledSwitch)
        vibrationEnabledSwitch = findViewById(R.id.vibrationEnabledSwitch)
        notificationsEnabledSwitch = findViewById(R.id.notificationsEnabledSwitch)
        autonomyEnabledSwitch = findViewById(R.id.autonomyEnabledSwitch)
        interactionSensitivitySeekBar = findViewById(R.id.interactionSensitivitySeekBar)
        ballSizeSeekBar = findViewById(R.id.ballSizeSeekBar)
        personalityText = findViewById(R.id.personalityText)
        resetButton = findViewById(R.id.resetButton)
        statsButton = findViewById(R.id.statsButton)
    }
    
    private fun setupUI() {
        // Configure seekbars
        interactionSensitivitySeekBar.max = 100
        ballSizeSeekBar.max = 200
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ballSizeSeekBar.min = 50
        }
        
        // Setup listeners
        soundEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("sound_enabled", isChecked).apply()
            if (isChecked) {
                soundManager.playEmotionalSound("feliz")
            }
        }
        
        vibrationEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("vibration_enabled", isChecked).apply()
            if (isChecked) {
                soundManager.playInteractionFeedback("touch")
            }
        }
        
        notificationsEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("notifications_enabled", isChecked).apply()
        }
        
        autonomyEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("autonomy_enabled", isChecked).apply()
            if (isChecked) {
                AmiBackgroundService.startService(this)
            } else {
                AmiBackgroundService.stopService(this)
            }
        }
        
        interactionSensitivitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                preferences.edit().putInt("interaction_sensitivity", progress).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        ballSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                preferences.edit().putInt("ball_size", progress).apply()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        resetButton.setOnClickListener {
            showResetConfirmationDialog()
        }
        
        statsButton.setOnClickListener {
            showEmotionalStatistics()
        }
        
        // Update personality description
        updatePersonalityDescription()
    }
    
    private fun loadCurrentSettings() {
        soundEnabledSwitch.isChecked = preferences.getBoolean("sound_enabled", true)
        vibrationEnabledSwitch.isChecked = preferences.getBoolean("vibration_enabled", true)
        notificationsEnabledSwitch.isChecked = preferences.getBoolean("notifications_enabled", true)
        autonomyEnabledSwitch.isChecked = preferences.getBoolean("autonomy_enabled", true)
        interactionSensitivitySeekBar.progress = preferences.getInt("interaction_sensitivity", 70)
        ballSizeSeekBar.progress = preferences.getInt("ball_size", 150)
    }
    
    private fun updatePersonalityDescription() {
        val happiness = emotionalSystem.happiness.toInt()
        val trust = emotionalSystem.trust.toInt()
        val loneliness = emotionalSystem.loneliness.toInt()
        val energy = emotionalSystem.energy.toInt()
        
        val personalityDescription = buildString {
            append("Estado actual de Ami:\n\n")
            append("💛 Felicidad: $happiness%\n")
            append("💙 Confianza: $trust%\n")
            append("😔 Soledad: $loneliness%\n")
            append("⚡ Energía: $energy%\n\n")
            
            when {
                happiness > 80 && trust > 70 -> append("🌟 Ami está muy feliz y confía mucho en ti")
                happiness < 30 -> append("😢 Ami se siente triste y necesita más atención")
                trust < 30 -> append("😟 Ami se siente insegura contigo")
                loneliness > 80 -> append("😔 Ami se siente muy sola")
                energy > 80 -> append("⚡ Ami está llena de energía y emoción")
                energy < 30 -> append("💤 Ami se siente cansada y necesita descansar")
                else -> append("😊 Ami está en un estado emocional equilibrado")
            }
        }
        
        personalityText.text = personalityDescription
    }
    
    private fun showResetConfirmationDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Reiniciar Ami")
            .setMessage("¿Estás seguro de que quieres reiniciar completamente a Ami? Esto borrará toda su memoria emocional y volverá a su estado inicial.")
            .setPositiveButton("Sí, reiniciar") { _, _ ->
                emotionalSystem.resetEmotionalState()
                soundManager.playInteractionFeedback("long_press")
                updatePersonalityDescription()
                Toast.makeText(this, "Ami ha sido completamente reiniciada 🔄", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showEmotionalStatistics() {
        val stats = buildString {
            append("📊 Estadísticas Emocionales de Ami\n\n")
            
            val currentEmotion = emotionalSystem.getCurrentEmotion()
            append("Emoción actual: ${currentEmotion.name}\n")
            append("Color emocional: #${Integer.toHexString(currentEmotion.color).uppercase()}\n\n")
            
            append("Interacciones totales: ${preferences.getInt("total_interactions", 0)}\n")
            append("Días desde creación: ${getDaysSinceCreation()}\n")
            append("Última interacción: ${getLastInteractionTime()}\n\n")
            
            val capabilities = soundManager.getAudioCapabilities()
            append("Capacidades del dispositivo:\n")
            append("• Vibración: ${if (capabilities["VibrationSupported"] == true) "✅" else "❌"}\n")
            append("• Control de amplitud: ${if (capabilities["AmplitudeControl"] == true) "✅" else "❌"}\n")
            append("• Sistema de sonido: ${if (capabilities["SoundPoolActive"] == true) "✅" else "❌"}\n")
        }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("📊 Estadísticas de Ami")
            .setMessage(stats)
            .setPositiveButton("Cerrar", null)
            .show()
    }
    
    private fun getDaysSinceCreation(): Int {
        val creationTime = preferences.getLong("creation_time", System.currentTimeMillis())
        val currentTime = System.currentTimeMillis()
        return ((currentTime - creationTime) / (1000 * 60 * 60 * 24)).toInt()
    }
    
    private fun getLastInteractionTime(): String {
        val lastInteraction = preferences.getLong("last_interaction", 0)
        if (lastInteraction == 0L) return "Nunca"
        
        val timeDiff = System.currentTimeMillis() - lastInteraction
        val hours = timeDiff / (1000 * 60 * 60)
        val minutes = (timeDiff % (1000 * 60 * 60)) / (1000 * 60)
        
        return when {
            hours == 0L -> "Hace $minutes minutos"
            hours < 24 -> "Hace $hours horas"
            else -> "Hace ${hours / 24} días"
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}