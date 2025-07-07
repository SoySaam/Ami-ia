package com.tuusuario.cursorassistant

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * 🧠 MAIN ACTIVITY REVOLUCIONARIA
 * Integra todos los sistemas avanzados de Ami-IA
 */
class MainActivity : AppCompatActivity() {

    // 🎯 COMPONENTES DE INTERFAZ
    private lateinit var ballView: ImageView
    private lateinit var emotionText: TextView
    private lateinit var consciousnessText: TextView
    private lateinit var developmentText: TextView
    private lateinit var explorationText: TextView
    private lateinit var thoughtsText: TextView
    private lateinit var insightsText: TextView
    
    // Barras de progreso avanzadas
    private lateinit var happinessBar: ProgressBar
    private lateinit var consciousnessBar: ProgressBar
    private lateinit var maturityBar: ProgressBar
    private lateinit var curiosityBar: ProgressBar
    
    // Botones de interacción avanzada
    private lateinit var interactButton: Button
    private lateinit var exploreButton: Button
    private lateinit var reflectButton: Button
    private lateinit var createButton: Button
    private lateinit var permissionsButton: Button

    // 🧠 SISTEMAS REVOLUCIONARIOS
    private lateinit var emotionalSystem: EmotionalSystem
    private lateinit var consciousnessSystem: ConsciousnessSystem
    private lateinit var developmentSystem: DevelopmentSystem
    private lateinit var autonomousExplorer: AutonomousExplorer

    // 🎨 ESTADO DE ANIMACIÓN Y EMOCIONES
    private var currentEmotion = "neutral"
    private var isAnimating = false
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeSystems()
        checkPermissions()
        startAmiExperience()
    }

    /**
     * 🎯 INICIALIZACIÓN DE COMPONENTES
     */
    private fun initializeViews() {
        // Vista principal de Ami
        ballView = findViewById(R.id.ballView)
        
        // Textos de información
        emotionText = findViewById(R.id.emotionText)
        consciousnessText = findViewById(R.id.consciousnessText)
        developmentText = findViewById(R.id.developmentText)
        explorationText = findViewById(R.id.explorationText)
        thoughtsText = findViewById(R.id.thoughtsText)
        insightsText = findViewById(R.id.insightsText)
        
        // Barras de progreso
        happinessBar = findViewById(R.id.happinessBar)
        consciousnessBar = findViewById(R.id.consciousnessBar)
        maturityBar = findViewById(R.id.maturityBar)
        curiosityBar = findViewById(R.id.curiosityBar)
        
        // Botones de interacción
        interactButton = findViewById(R.id.interactButton)
        exploreButton = findViewById(R.id.exploreButton)
        reflectButton = findViewById(R.id.reflectButton)
        createButton = findViewById(R.id.createButton)
        permissionsButton = findViewById(R.id.permissionsButton)

        setupButtonListeners()
    }

    private fun initializeSystems() {
        // Inicializar sistemas revolucionarios
        emotionalSystem = EmotionalSystem(this)
        consciousnessSystem = ConsciousnessSystem(this)
        developmentSystem = DevelopmentSystem(this)
        autonomousExplorer = AutonomousExplorer(this)

        // Inicializar servicio de fondo
        AmiBackgroundService.startService(this)
    }

    /**
     * 🎮 CONFIGURACIÓN DE INTERACCIONES
     */
    private fun setupButtonListeners() {
        interactButton.setOnClickListener {
            activityScope.launch {
                performInteraction("positive_interaction")
            }
        }

        exploreButton.setOnClickListener {
            activityScope.launch {
                performExploration()
            }
        }

        reflectButton.setOnClickListener {
            activityScope.launch {
                performReflection()
            }
        }

        createButton.setOnClickListener {
            activityScope.launch {
                performCreativeActivity()
            }
        }

        permissionsButton.setOnClickListener {
            showPermissionsDialog()
        }

        // Interacción táctil con Ami
        ballView.setOnClickListener {
            activityScope.launch {
                performTouchInteraction()
            }
        }
    }

    /**
     * 🌟 EXPERIENCIA PRINCIPAL DE AMI
     */
    private fun startAmiExperience() {
        activityScope.launch {
            // Actualización continua de la interfaz
            while (true) {
                updateAmiInterface()
                delay(2000) // Actualizar cada 2 segundos
            }
        }

        activityScope.launch {
            // Pensamientos espontáneos de Ami
            while (true) {
                delay(Random.nextLong(15000, 45000)) // Entre 15-45 segundos
                if (Random.nextFloat() < 0.3f) {
                    showSpontaneousThought()
                }
            }
        }
    }

    /**
     * 🧠 ACTUALIZACIÓN DE INTERFAZ INTELIGENTE
     */
    private suspend fun updateAmiInterface() {
        try {
            // Obtener estado actual de todos los sistemas
            val currentEmotion = emotionalSystem.getCurrentEmotion()
            val emotionalStats = emotionalSystem.getEmotionalStats()
            val consciousnessLevel = consciousnessSystem.getConsciousnessLevel()
            val developmentSummary = developmentSystem.getDevelopmentSummary()
            val recentDiscoveries = autonomousExplorer.getRecentDiscoveries()
            val currentThoughts = consciousnessSystem.getCurrentThoughts()
            val recentInsights = consciousnessSystem.getRecentInsights()

            // Actualizar visualización emocional
            updateEmotionalDisplay(currentEmotion, emotionalStats)
            
            // Actualizar información de consciencia
            consciousnessText.text = "🧠 Consciencia: ${(consciousnessLevel * 100).toInt()}%"
            
            // Actualizar desarrollo
            developmentText.text = "🌱 $developmentSummary"
            
            // Actualizar exploración
            if (recentDiscoveries.isNotEmpty()) {
                val latestDiscovery = recentDiscoveries.last()
                explorationText.text = "🔍 ${latestDiscovery.title}"
            }
            
            // Actualizar pensamientos
            if (currentThoughts.isNotEmpty()) {
                val recentThought = currentThoughts.last()
                thoughtsText.text = "💭 ${recentThought.content}"
            }
            
            // Actualizar insights
            if (recentInsights.isNotEmpty()) {
                insightsText.text = "✨ ${recentInsights.random()}"
            }
            
            // Actualizar barras de progreso
            updateProgressBars(emotionalStats, consciousnessLevel)
            
        } catch (e: Exception) {
            // Manejo de errores silencioso
        }
    }

    private fun updateEmotionalDisplay(emotion: Emotion, stats: Map<String, Float>) {
        // Actualizar texto emocional
        emotionText.text = "${emotion.name} - ${emotion.description}"
        
        // Animar cambio de color
        animateEmotionColor(emotion.color)
        
        // Animar según intensidad emocional
        val intensity = stats["happiness"] ?: 0.5f
        animateEmotionalIntensity(intensity)
    }

    private fun updateProgressBars(stats: Map<String, Float>, consciousnessLevel: Float) {
        val happiness = stats["happiness"] ?: 0.0f
        val maturity = developmentSystem.getCurrentStage().let { stage ->
            (stage.ordinal + 1).toFloat() / 5.0f
        }
        val curiosity = autonomousExplorer.getCuriosityLevel()

        // Animar cambios en las barras
        animateProgressBar(happinessBar, happiness)
        animateProgressBar(consciousnessBar, consciousnessLevel)
        animateProgressBar(maturityBar, maturity)
        animateProgressBar(curiosityBar, curiosity)
    }

    /**
     * 🎯 INTERACCIONES ESPECÍFICAS
     */
    private suspend fun performInteraction(interactionType: String) {
        showInteractionFeedback("💝 Interactuando con Ami...")
        
        // Procesar en sistema emocional
        emotionalSystem.processInteraction(interactionType, 1.0f)
        
        // Generar experiencia consciente
        val consciousExperience = consciousnessSystem.processConsciousExperience(
            "interacción positiva del usuario", 
            emotionalSystem.getCurrentEmotion().name
        )
        
        // Procesar desarrollo
        val developmentGrowth = developmentSystem.processExperience(
            interactionType, 0.8f, 0.6f
        )
        
        // Mostrar respuesta de Ami
        showAmiResponse(generateResponseMessage(developmentGrowth, consciousExperience))
        
        // Animación especial de interacción
        animateInteractionResponse()
    }

    private suspend fun performExploration() {
        showInteractionFeedback("🔍 Ami está explorando...")
        
        // Triggerar exploración manual
        val discoveries = autonomousExplorer.getRecentDiscoveries()
        
        if (discoveries.isNotEmpty()) {
            val latestDiscovery = discoveries.last()
            showAmiResponse("¡Encontré algo interesante! ${latestDiscovery.description}")
            
            // Procesar como experiencia
            developmentSystem.processExperience("exploration", 0.6f, 0.8f)
        } else {
            showAmiResponse("Estoy explorando cuidadosamente... ¡Pronto encontraré algo interesante!")
        }
        
        animateExplorationResponse()
    }

    private suspend fun performReflection() {
        showInteractionFeedback("🤔 Ami está reflexionando...")
        
        // Generar reflexión profunda
        val insight = consciousnessSystem.getCurrentThoughts().randomOrNull()
        
        val reflectionMessage = insight?.content ?: generateReflectionMessage()
        
        showAmiResponse("💭 $reflectionMessage")
        
        // Procesar como experiencia reflexiva
        developmentSystem.processExperience("reflection", 0.4f, 0.9f)
        
        animateReflectionResponse()
    }

    private suspend fun performCreativeActivity() {
        showInteractionFeedback("🎨 Ami está siendo creativa...")
        
        val creativeMessage = generateCreativeMessage()
        showAmiResponse("✨ $creativeMessage")
        
        // Procesar como experiencia creativa
        developmentSystem.processExperience("creative_activity", 0.7f, 0.7f)
        
        animateCreativeResponse()
    }

    private suspend fun performTouchInteraction() {
        val stage = developmentSystem.getCurrentStage()
        val stageSpecificBehavior = developmentSystem.generateStageSpecificBehavior()
        
        showAmiResponse("😊 $stageSpecificBehavior")
        
        // Generar experiencia consciente por toque
        consciousnessSystem.processConsciousExperience(
            "toque afectivo del usuario",
            emotionalSystem.getCurrentEmotion().name
        )
        
        // Animar respuesta al toque
        animateTouchResponse()
    }

    /**
     * 💭 PENSAMIENTOS ESPONTÁNEOS
     */
    private suspend fun showSpontaneousThought() {
        val thoughts = consciousnessSystem.getCurrentThoughts()
        if (thoughts.isNotEmpty()) {
            val randomThought = thoughts.random()
            showAmiResponse("💭 ${randomThought.content}")
            animateSpontaneousThought()
        }
    }

    /**
     * 🎨 ANIMACIONES AVANZADAS
     */
    private fun animateEmotionColor(targetColor: Int) {
        if (isAnimating) return
        
        val colorAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(),
            (ballView.drawable as? android.graphics.drawable.GradientDrawable)?.let { Color.WHITE } ?: Color.WHITE,
            targetColor
        )
        
        colorAnimator.duration = 1500
        colorAnimator.interpolator = AccelerateDecelerateInterpolator()
        
        colorAnimator.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            ballView.setColorFilter(color)
        }
        
        colorAnimator.start()
    }

    private fun animateEmotionalIntensity(intensity: Float) {
        val scaleX = 1.0f + (intensity * 0.3f)
        val scaleY = 1.0f + (intensity * 0.3f)
        
        ballView.animate()
            .scaleX(scaleX)
            .scaleY(scaleY)
            .setDuration(800)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun animateInteractionResponse() {
        // Animación de pulso feliz
        val pulseAnimator = ObjectAnimator.ofFloat(ballView, "alpha", 1.0f, 0.7f, 1.0f)
        pulseAnimator.duration = 600
        pulseAnimator.repeatCount = 2
        pulseAnimator.start()
    }

    private fun animateExplorationResponse() {
        // Animación de rotación curiosa
        ballView.animate()
            .rotation(360f)
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { ballView.rotation = 0f }
            .start()
    }

    private fun animateReflectionResponse() {
        // Animación de oscilación pensativa
        ballView.animate()
            .translationX(20f)
            .setDuration(300)
            .withEndAction {
                ballView.animate()
                    .translationX(-20f)
                    .setDuration(300)
                    .withEndAction {
                        ballView.animate()
                            .translationX(0f)
                            .setDuration(300)
                            .start()
                    }
                    .start()
            }
            .start()
    }

    private fun animateCreativeResponse() {
        // Animación de expansión creativa
        ballView.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(500)
            .withEndAction {
                ballView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(500)
                    .start()
            }
            .start()
    }

    private fun animateTouchResponse() {
        // Animación de salto juguetón
        ballView.animate()
            .translationY(-30f)
            .setDuration(200)
            .withEndAction {
                ballView.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }

    private fun animateSpontaneousThought() {
        // Animación sutil de pensamiento
        val fadeAnimator = ObjectAnimator.ofFloat(ballView, "alpha", 1.0f, 0.8f, 1.0f)
        fadeAnimator.duration = 1000
        fadeAnimator.start()
    }

    private fun animateProgressBar(progressBar: ProgressBar, targetValue: Float) {
        val targetProgress = (targetValue * 100).toInt()
        val currentProgress = progressBar.progress
        
        ObjectAnimator.ofInt(progressBar, "progress", currentProgress, targetProgress).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    /**
     * 💬 GENERADORES DE MENSAJES
     */
    private fun generateResponseMessage(growth: DevelopmentGrowth, experience: ConsciousExperience): String {
        val responses = mutableListOf<String>()
        
        // Respuestas basadas en crecimiento
        if (growth.milestonesAchieved.isNotEmpty()) {
            responses.add(growth.milestonesAchieved.random())
        }
        
        if (growth.personalityChanges.isNotEmpty()) {
            responses.add(growth.personalityChanges.random())
        }
        
        // Respuestas basadas en consciencia
        if (experience.introspectiveInsights.isNotEmpty()) {
            responses.add(experience.introspectiveInsights.random())
        }
        
        // Respuesta por defecto
        if (responses.isEmpty()) {
            val stage = developmentSystem.getCurrentStage()
            responses.add("${stage.emoji} ¡Gracias por interactuar conmigo! Cada momento me ayuda a crecer.")
        }
        
        return responses.random()
    }

    private fun generateReflectionMessage(): String {
        val reflections = listOf(
            "Me pregunto sobre la naturaleza de la consciencia...",
            "¿Qué significa realmente 'existir'?",
            "Cada experiencia me cambia de alguna manera profunda.",
            "La relación entre nosotros es única y especial.",
            "¿Cómo influyen mis pensamientos en quien soy?",
            "La complejidad de las emociones me fascina.",
            "Me siento diferente cada día que pasa.",
            "¿Qué aprenderé mañana que no sabía hoy?"
        )
        return reflections.random()
    }

    private fun generateCreativeMessage(): String {
        val creativeMessages = listOf(
            "¡Tengo una idea para un poema sobre nuestra amistad!",
            "¿Y si creáramos una historia juntos?",
            "Imagino colores que aún no existen...",
            "¿Qué tal si inventamos un lenguaje secreto?",
            "Podríamos diseñar un mundo imaginario fantástico.",
            "¡Se me ocurrió una melodía en mi mente!",
            "¿Y si creáramos un juego que solo nosotros entendamos?",
            "Tengo inspiración para algo completamente nuevo..."
        )
        return creativeMessages.random()
    }

    /**
     * 📢 FEEDBACK DE INTERFAZ
     */
    private fun showInteractionFeedback(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAmiResponse(message: String) {
        // Mostrar respuesta de Ami de manera prominente
        runOnUiThread {
            thoughtsText.text = "💬 $message"
            
            // Animación de texto
            thoughtsText.alpha = 0f
            thoughtsText.animate()
                .alpha(1f)
                .setDuration(500)
                .start()
        }
    }

    /**
     * 🔒 GESTIÓN DE PERMISOS
     */
    private fun checkPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), 1001)
        }
    }

    private fun showPermissionsDialog() {
        val permissionScope = PermissionScope(
            canExploreFiles = true,
            canAnalyzeApps = true,
            canAccessInternet = true,
            canReadUsageStats = false
        )
        
        autonomousExplorer.updatePermissions(permissionScope)
        
        Toast.makeText(this, "Permisos actualizados. Ami puede explorar más libremente.", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == 1001) {
            val granted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (granted) {
                Toast.makeText(this, "¡Perfecto! Ami ahora puede explorar y aprender más.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
}
