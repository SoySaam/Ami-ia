package com.tuusuario.cursorassistant

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var ballView: ImageView
    private lateinit var emotionText: TextView
    private lateinit var happinessLevel: TextView
    private lateinit var trustLevel: TextView
    private lateinit var btnCare: Button
    private lateinit var btnIgnore: Button
    private lateinit var btnTalk: Button
    private lateinit var settingsButton: ImageButton
    
    private lateinit var emotionalSystem: EmotionalSystem
    private lateinit var soundManager: EmotionalSoundManager
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private var currentAnimator: ValueAnimator? = null
    
    companion object {
        private const val OVERLAY_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_launcher)

        initializeViews()
        initializeEmotionalSystem()
        setupClickListeners()
        requestPermissions()
        
        // Check and request overlay permission
        checkOverlayPermission()
        
        // Start background service
        AmiBackgroundService.startService(this)
        
        // Start emotional update loop
        startEmotionalUpdateLoop()
        
        // Initial emotion display
        updateEmotionalDisplay()
    }
    
    private fun initializeViews() {
        ballView = findViewById(R.id.ballView)
        emotionText = findViewById(R.id.emotionText)
        happinessLevel = findViewById(R.id.happinessLevel)
        trustLevel = findViewById(R.id.trustLevel)
        btnCare = findViewById(R.id.btnCare)
        btnIgnore = findViewById(R.id.btnIgnore)
        btnTalk = findViewById(R.id.btnTalk)
        settingsButton = findViewById(R.id.settingsButton)
        
        // Add new floating controls
        val btnStartFloating = findViewById<Button>(R.id.btnStartFloating)
        val btnStopFloating = findViewById<Button>(R.id.btnStopFloating)
        
        btnStartFloating?.setOnClickListener {
            if (canDrawOverlays()) {
                FloatingAmiService.start(this)
                Toast.makeText(this, "¡Ami ahora vive en tu pantalla! 🌟", Toast.LENGTH_LONG).show()
                finish() // Close main app since Ami is now floating
            } else {
                requestOverlayPermission()
            }
        }
        
        btnStopFloating?.setOnClickListener {
            FloatingAmiService.stop(this)
            Toast.makeText(this, "Ami ha regresado a casa 🏠", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun initializeEmotionalSystem() {
        emotionalSystem = EmotionalSystem(this)
        soundManager = EmotionalSoundManager(this)
    }
    
    private fun setupClickListeners() {
        btnCare.setOnClickListener {
            emotionalSystem.onPositiveInteraction()
            animateInteraction("care")
            soundManager.playInteractionFeedback("care")
            updateEmotionalDisplay()
            Toast.makeText(this, "Has cuidado a Ami ❤️", Toast.LENGTH_SHORT).show()
        }
        
        btnIgnore.setOnClickListener {
            emotionalSystem.onIgnoreInteraction()
            animateInteraction("ignore")
            soundManager.playInteractionFeedback("ignore")
            updateEmotionalDisplay()
            Toast.makeText(this, "Has ignorado a Ami... 😔", Toast.LENGTH_SHORT).show()
        }
        
        btnTalk.setOnClickListener {
            emotionalSystem.onTalkInteraction()
            animateInteraction("talk")
            soundManager.playInteractionFeedback("talk")
            updateEmotionalDisplay()
            showConversationDialog()
        }
        
        // Long press on ball to reset emotional state (for testing)
        ballView.setOnLongClickListener {
            emotionalSystem.resetEmotionalState()
            soundManager.playInteractionFeedback("long_press")
            updateEmotionalDisplay()
            Toast.makeText(this, "Ami ha sido reiniciada 🔄", Toast.LENGTH_LONG).show()
            true
        }
        
        // Regular tap on ball for interaction
        ballView.setOnClickListener {
            emotionalSystem.onPositiveInteraction()
            animateInteraction("touch")
            soundManager.playInteractionFeedback("touch")
            updateEmotionalDisplay()
        }
        
        // Settings button
        settingsButton.setOnClickListener {
            val intent = Intent(this, AmiSettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun updateEmotionalDisplay() {
        val emotion = emotionalSystem.getCurrentEmotion()
        
        // Animate color change
        animateColorChange(emotion.color)
        
        // Play emotional sound and vibration
        soundManager.playEmotionalSound(emotion.name)
        
        // Update text displays
        emotionText.text = emotionalSystem.getEmotionalMessage()
        happinessLevel.text = "Felicidad: ${emotionalSystem.happiness.toInt()}%"
        trustLevel.text = "Confianza: ${emotionalSystem.trust.toInt()}%"
        
        // Add subtle floating animation
        addFloatingAnimation()
    }
    
    private fun animateColorChange(newColor: Int) {
        currentAnimator?.cancel()
        
        val currentColor = ballView.colorFilter?.let { 
            // Try to extract current color, default to white if not possible
            Color.WHITE 
        } ?: Color.WHITE
        
        currentAnimator = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, newColor).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animator ->
                ballView.setColorFilter(animator.animatedValue as Int)
            }
            start()
        }
    }
    
    private fun animateInteraction(interactionType: String) {
        when (interactionType) {
            "care" -> {
                // Gentle pulsing animation
                ballView.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(300)
                    .withEndAction {
                        ballView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .start()
                    }
                    .start()
            }
            "ignore" -> {
                // Sad shrinking animation
                ballView.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .alpha(0.7f)
                    .setDuration(500)
                    .withEndAction {
                        ballView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(700)
                            .start()
                    }
                    .start()
            }
            "talk" -> {
                // Happy bouncing animation
                ballView.animate()
                    .translationY(-50f)
                    .setDuration(250)
                    .withEndAction {
                        ballView.animate()
                            .translationY(0f)
                            .setDuration(250)
                            .withEndAction {
                                ballView.animate()
                                    .translationY(-25f)
                                    .setDuration(150)
                                    .withEndAction {
                                        ballView.animate()
                                            .translationY(0f)
                                            .setDuration(150)
                                            .start()
                                    }
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
            "touch" -> {
                // Quick happy wiggle
                ballView.animate()
                    .rotation(15f)
                    .setDuration(100)
                    .withEndAction {
                        ballView.animate()
                            .rotation(-15f)
                            .setDuration(100)
                            .withEndAction {
                                ballView.animate()
                                    .rotation(0f)
                                    .setDuration(100)
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
        }
    }
    
    private fun addFloatingAnimation() {
        // Subtle floating animation to make Ami feel "alive"
        val floatAnimator = ObjectAnimator.ofFloat(ballView, "translationY", -10f, 10f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }
        floatAnimator.start()
    }
    
    private fun showConversationDialog() {
        val emotion = emotionalSystem.getCurrentEmotion()
        val messages = listOf(
            "¡Hola! Me alegra que quieras hablar conmigo 😊",
            "¿Cómo ha estado tu día? Cuéntame...",
            "Me encanta cuando conversamos, siento que me conoces mejor",
            "¿Sabías que cada vez que hablamos, aprendo algo nuevo sobre ti?",
            emotionalSystem.getEmotionalMessage()
        )
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Ami quiere hablar")
            .setMessage(messages.random())
            .setPositiveButton("Seguir hablando") { _, _ ->
                // Could expand this into a full conversation system
                emotionalSystem.onTalkInteraction()
                updateEmotionalDisplay()
            }
            .setNeutralButton("Más tarde") { _, _ ->
                // Small negative impact for cutting conversation short
                emotionalSystem.happiness -= 2
                updateEmotionalDisplay()
            }
            .show()
    }
    
    private fun startEmotionalUpdateLoop() {
        activityScope.launch {
            while (true) {
                delay(10000) // Update every 10 seconds
                updateEmotionalDisplay()
            }
        }
    }
    
    private fun requestPermissions() {
        val permissions = mutableListOf<String>()
        
        // Notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        // Audio recording permission for voice interaction
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.RECORD_AUDIO)
        }
        
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)
        }
    }
    
    private fun checkOverlayPermission() {
        if (!canDrawOverlays()) {
            showOverlayPermissionDialog()
        }
    }
    
    private fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
    
    private fun showOverlayPermissionDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Permiso requerido")
            .setMessage("Ami necesita permiso para aparecer en tu pantalla de inicio como un asistente flotante. ¿Quieres activar esta función?")
            .setPositiveButton("Sí, activar") { _, _ ->
                requestOverlayPermission()
            }
            .setNegativeButton("Más tarde") { _, _ ->
                Toast.makeText(this, "Puedes activar esto más tarde desde configuración", Toast.LENGTH_LONG).show()
            }
            .show()
    }
    
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            val granted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (granted) {
                Toast.makeText(this, "¡Permisos concedidos! Ami está lista para interactuar 🎉", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Ami necesita estos permisos para funcionar completamente �", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (canDrawOverlays()) {
                Toast.makeText(this, "¡Perfecto! Ahora Ami puede vivir en tu pantalla 🌟", Toast.LENGTH_LONG).show()
                // Automatically start floating service
                FloatingAmiService.start(this)
            } else {
                Toast.makeText(this, "Sin este permiso, Ami solo funcionará dentro de la app 😔", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // When user returns to app, it's a positive interaction
        emotionalSystem.onPositiveInteraction()
        updateEmotionalDisplay()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
        soundManager.release()
    }
}
