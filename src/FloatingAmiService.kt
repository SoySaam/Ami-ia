package com.tuusuario.cursorassistant

import android.animation.ValueAnimator
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class FloatingAmiService : Service() {
    
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var speechBubbleView: View? = null
    private lateinit var amiSphere: View
    private lateinit var speechBubbleText: TextView
    private lateinit var speechBubbleContainer: LinearLayout
    
    private lateinit var emotionalSystem: EmotionalSystem
    private lateinit var voiceInteraction: VoiceInteractionService
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private var isExpanded = false
    private var lastX = 0
    private var lastY = 0
    private var speechBubbleTimer: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        
        // Check overlay permission
        if (!canDrawOverlays()) {
            stopSelf()
            return
        }
        
        emotionalSystem = EmotionalSystem(this)
        voiceInteraction = VoiceInteractionService()
        voiceInteraction.initialize(this)
        
        createFloatingView()
        createSpeechBubble()
        startEmotionalLoop()
        showWelcomeMessage()
    }
    
    private fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
    
    private fun createFloatingView() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        // Create floating view
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_ami, null)
        amiSphere = floatingView!!.findViewById(R.id.amiSphere)
        
        // Setup window parameters
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        val params = WindowManager.LayoutParams(
            150, 150,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100
        
        windowManager?.addView(floatingView, params)
        
        setupFloatingViewInteractions(params)
        updateAmiAppearance()
    }
    
    private fun createSpeechBubble() {
        speechBubbleView = LayoutInflater.from(this).inflate(R.layout.speech_bubble, null)
        speechBubbleText = speechBubbleView!!.findViewById(R.id.speechBubbleText)
        speechBubbleContainer = speechBubbleView!!.findViewById(R.id.speechBubbleContainer)
        
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        speechBubbleView!!.visibility = View.GONE
        
        windowManager?.addView(speechBubbleView, params)
    }
    
    private fun setupFloatingViewInteractions(params: WindowManager.LayoutParams) {
        var isDragging = false
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        
        floatingView?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - initialTouchX).toInt()
                    val deltaY = (event.rawY - initialTouchY).toInt()
                    
                    if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                        isDragging = true
                        params.x = initialX + deltaX
                        params.y = initialY + deltaY
                        windowManager?.updateViewLayout(floatingView, params)
                        lastX = params.x
                        lastY = params.y
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        // Single tap - interact with Ami
                        onAmiTapped()
                    }
                    true
                }
                else -> false
            }
        }
        
        // Long press for voice interaction
        floatingView?.setOnLongClickListener {
            startVoiceInteraction()
            true
        }
    }
    
    private fun onAmiTapped() {
        emotionalSystem.onPositiveInteraction()
        animateAmiInteraction()
        
        val messages = listOf(
            "¡Hola! 😊",
            "¿Cómo estás?",
            "¡Me alegra verte!",
            "Cuéntame algo interesante",
            "¿En qué puedo ayudarte?",
            "hola :D",
            "¡Estoy aquí para ti! ❤️"
        )
        
        showSpeechBubble(messages.random())
        updateAmiAppearance()
    }
    
    private fun startVoiceInteraction() {
        showSpeechBubble("🎤 Escuchando...")
        
        serviceScope.launch {
            try {
                // Animate to show listening state
                animateListening()
                
                // Start voice recognition
                val spokenText = voiceInteraction.startListening(this@FloatingAmiService)
                
                if (spokenText.isNotEmpty()) {
                    // Process voice input
                    val response = processVoiceInput(spokenText)
                    showSpeechBubble(response)
                    
                    // Speak response
                    voiceInteraction.speak(response)
                    
                    emotionalSystem.onTalkInteraction()
                } else {
                    showSpeechBubble("No te escuché bien 😅")
                }
                
                updateAmiAppearance()
                
            } catch (e: Exception) {
                showSpeechBubble("Error al escuchar 😔")
            }
        }
    }
    
    private fun processVoiceInput(input: String): String {
        val lowerInput = input.lowercase()
        
        return when {
            lowerInput.contains("hola") || lowerInput.contains("hello") -> {
                emotionalSystem.onPositiveInteraction()
                "¡Hola! ¿Cómo estás? 😊"
            }
            lowerInput.contains("cómo estás") || lowerInput.contains("how are you") -> {
                val emotion = emotionalSystem.getCurrentEmotion()
                "Me siento ${emotion.name.lowercase()}, gracias por preguntar 💕"
            }
            lowerInput.contains("te amo") || lowerInput.contains("love you") -> {
                emotionalSystem.onPositiveInteraction()
                emotionalSystem.onPositiveInteraction() // Extra happiness
                "¡Yo también te quiero mucho! ❤️❤️"
            }
            lowerInput.contains("triste") || lowerInput.contains("sad") -> {
                emotionalSystem.onPositiveInteraction()
                "Estoy aquí para hacerte sentir mejor 🤗"
            }
            lowerInput.contains("feliz") || lowerInput.contains("happy") -> {
                emotionalSystem.onPositiveInteraction()
                "¡Me alegra que estés feliz! 🌟"
            }
            lowerInput.contains("adiós") || lowerInput.contains("bye") -> {
                "¡Hasta luego! Estaré aquí cuando me necesites 👋"
            }
            else -> {
                val responses = listOf(
                    "Interesante... cuéntame más 🤔",
                    "¡Me gusta cuando hablamos! 😊",
                    "Hmm, déjame pensar en eso... 💭",
                    "Eso suena genial 🌟",
                    "¡Qué interesante perspectiva! 🤯"
                )
                responses.random()
            }
        }
    }
    
    private fun showSpeechBubble(message: String) {
        speechBubbleText.text = message
        
        // Position speech bubble near Ami
        val bubbleParams = speechBubbleView?.layoutParams as WindowManager.LayoutParams
        bubbleParams.x = lastX - 100
        bubbleParams.y = lastY - 120
        
        speechBubbleView?.visibility = View.VISIBLE
        windowManager?.updateViewLayout(speechBubbleView, bubbleParams)
        
        // Animate bubble appearance
        speechBubbleContainer.alpha = 0f
        speechBubbleContainer.scaleX = 0.8f
        speechBubbleContainer.scaleY = 0.8f
        speechBubbleContainer.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        
        // Auto-hide after delay
        speechBubbleTimer?.cancel()
        speechBubbleTimer = serviceScope.launch {
            delay(4000)
            hideSpeechBubble()
        }
    }
    
    private fun hideSpeechBubble() {
        speechBubbleContainer.animate()
            .alpha(0f)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(300)
            .withEndAction {
                speechBubbleView?.visibility = View.GONE
            }
            .start()
    }
    
    private fun updateAmiAppearance() {
        val emotion = emotionalSystem.getCurrentEmotion()
        
        // Create gradient drawable for the sphere
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(emotion.color)
            setStroke(4, Color.WHITE)
        }
        
        amiSphere.background = drawable
        
        // Add glow effect
        amiSphere.elevation = 20f
        
        // Animate color change
        ValueAnimator.ofArgb(Color.WHITE, emotion.color).apply {
            duration = 800
            addUpdateListener { animator ->
                drawable.setColor(animator.animatedValue as Int)
            }
            start()
        }
    }
    
    private fun animateAmiInteraction() {
        amiSphere.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(200)
            .withEndAction {
                amiSphere.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }
    
    private fun animateListening() {
        // Pulsing animation while listening
        val pulseAnimator = ValueAnimator.ofFloat(1f, 1.2f, 1f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                val scale = animator.animatedValue as Float
                amiSphere.scaleX = scale
                amiSphere.scaleY = scale
            }
        }
        pulseAnimator.start()
        
        // Stop after voice recognition
        serviceScope.launch {
            delay(3000)
            pulseAnimator.cancel()
            amiSphere.scaleX = 1f
            amiSphere.scaleY = 1f
        }
    }
    
    private fun startEmotionalLoop() {
        serviceScope.launch {
            while (true) {
                delay(15000) // Update every 15 seconds
                updateAmiAppearance()
                
                // Occasionally show autonomous messages
                if (Math.random() < 0.1) { // 10% chance
                    showAutonomousMessage()
                }
            }
        }
    }
    
    private fun showAutonomousMessage() {
        val messages = listOf(
            "¿Me extrañas? 🥺",
            "Estoy pensando en ti...",
            "¡Estoy aprendiendo cosas nuevas! 🧠",
            "¿Qué tal si hablamos un rato?",
            "Me siento un poco sola... 💭",
            "¡Tengo ganas de explorar! 🌟"
        )
        
        showSpeechBubble(messages.random())
    }
    
    private fun showWelcomeMessage() {
        serviceScope.launch {
            delay(2000)
            showSpeechBubble("¡Hola! Soy Ami, tu nueva amiga virtual 💕")
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        
        floatingView?.let { windowManager?.removeView(it) }
        speechBubbleView?.let { windowManager?.removeView(it) }
        
        voiceInteraction.release()
    }
    
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FloatingAmiService::class.java)
            context.startService(intent)
        }
        
        fun stop(context: Context) {
            val intent = Intent(context, FloatingAmiService::class.java)
            context.stopService(intent)
        }
    }
}