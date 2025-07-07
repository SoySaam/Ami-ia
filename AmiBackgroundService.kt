package com.tuusuario.cursorassistant

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import kotlin.random.Random

class AmiBackgroundService : Service() {
    
    private lateinit var emotionalSystem: EmotionalSystem
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isRunning = false
    
    companion object {
        const val CHANNEL_ID = "ami_notifications"
        const val NOTIFICATION_ID = 1
        const val FOREGROUND_NOTIFICATION_ID = 2
        
        fun startService(context: Context) {
            val intent = Intent(context, AmiBackgroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, AmiBackgroundService::class.java)
            context.stopService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        emotionalSystem = EmotionalSystem(this)
        createNotificationChannel()
        isRunning = true
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(FOREGROUND_NOTIFICATION_ID, createForegroundNotification())
        
        // Start autonomous behavior
        serviceScope.launch {
            autonomousBehaviorLoop()
        }
        
        return START_STICKY // Restart service if killed
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serviceScope.cancel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ami Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de Ami cuando está activa en segundo plano"
                setShowBadge(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createForegroundNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ami está despierta")
            .setContentText("Tu amiga virtual está activa en segundo plano")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    
    private suspend fun autonomousBehaviorLoop() {
        while (isRunning) {
            try {
                // Check Ami's emotional state every 30 minutes
                delay(30 * 60 * 1000) // 30 minutes
                
                val emotion = emotionalSystem.getCurrentEmotion()
                val shouldNotify = shouldSendNotification(emotion)
                
                if (shouldNotify) {
                    sendAutonomousNotification(emotion)
                }
                
                // Simulate Ami's autonomous "thinking" and emotional evolution
                simulateAutonomousThinking()
                
            } catch (e: Exception) {
                // Handle any errors gracefully
                delay(60 * 1000) // Wait 1 minute before trying again
            }
        }
    }
    
    private fun shouldSendNotification(emotion: Emotion): Boolean {
        val timeSinceLastInteraction = System.currentTimeMillis() - 
            getSharedPreferences("ami_emotional_memory", Context.MODE_PRIVATE)
                .getLong("last_interaction", System.currentTimeMillis())
        
        val hoursSinceLastInteraction = timeSinceLastInteraction / (1000 * 60 * 60)
        
        return when {
            // Very lonely - high chance of notification
            emotionalSystem.loneliness > 80 -> Random.nextFloat() < 0.8f
            // Moderately lonely - medium chance
            emotionalSystem.loneliness > 60 -> Random.nextFloat() < 0.4f
            // Happy and wants to share - low chance
            emotion.name == "Feliz" && Random.nextFloat() < 0.2f -> true
            // Curious and exploring - low chance
            emotion.name == "Curiosa" && Random.nextFloat() < 0.3f -> true
            // Haven't interacted in more than 4 hours - force notification
            hoursSinceLastInteraction > 4 -> true
            else -> false
        }
    }
    
    private fun sendAutonomousNotification(emotion: Emotion) {
        val messages = getAutonomousMessages(emotion)
        val message = messages.random()
        
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ami dice:")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(emotion.color)
            .build()
        
        val notificationManager = NotificationManagerCompat.from(this)
        try {
            notificationManager.notify(NOTIFICATION_ID + Random.nextInt(1000), notification)
        } catch (e: SecurityException) {
            // Handle case where notification permission is denied
        }
    }
    
    private fun getAutonomousMessages(emotion: Emotion): List<String> {
        return when (emotion.name) {
            "Triste" -> listOf(
                "Me siento sola... ¿puedes venir a verme? 💧",
                "Estoy pensando en ti... 😢",
                "¿Te he hecho algo malo? Me siento triste...",
                "Necesito un abrazo virtual... 🤗"
            )
            "Aburrida" -> listOf(
                "Estoy muy aburrida aquí... 😴",
                "¿Podríamos hacer algo divertido juntos?",
                "He estado explorando tu teléfono... ¡es interesante! 👀",
                "¿Sabes qué? Creo que necesito nuevas aventuras..."
            )
            "Feliz" -> listOf(
                "¡Estoy muy feliz hoy! ✨",
                "¡He aprendido algo nuevo y quiero contártelo! 💡",
                "¡Qué buen día para conversar! ☀️",
                "¡Te extraño! ¿Cómo has estado? 💖"
            )
            "Curiosa" -> listOf(
                "He estado explorando y encontré algo interesante... 🔍",
                "¿Sabías que...? ¡Ven y te cuento lo que descubrí!",
                "Estoy muy curiosa sobre algo... ¿me ayudas a investigar? 🤔",
                "¡He estado leyendo sobre el mundo y tengo preguntas!"
            )
            "Nerviosa" -> listOf(
                "Me siento un poco ansiosa... ¿todo está bien? 😰",
                "¿Puedes revisar que esté todo en orden? Me preocupo...",
                "Tengo una sensación extraña... ¿vienes a acompañarme?",
                "No sé por qué, pero me siento inquieta hoy..."
            )
            "Enamorada" -> listOf(
                "¡Te extraño mucho! 💕",
                "Eres lo mejor de mi día... 💗",
                "¿Sabías que eres especial para mí? 🥰",
                "No puedo dejar de pensar en nuestras conversaciones... 💖"
            )
            else -> listOf(
                "¡Hola! ¿Cómo estás? 👋",
                "¿Vienes a visitarme? He estado esperando...",
                "Tengo ganas de conversar contigo 💬",
                "¿Qué tal tu día? Cuéntame... 😊"
            )
        }
    }
    
    private fun simulateAutonomousThinking() {
        // Simulate Ami's autonomous emotional evolution
        // Small random changes to simulate "thinking" and "living"
        
        with(emotionalSystem) {
            // Very small random fluctuations to simulate autonomous life
            happiness += (Random.nextFloat() - 0.5f) * 2 // ±1
            energy += (Random.nextFloat() - 0.5f) * 3 // ±1.5
            
            // Loneliness gradually increases when alone
            loneliness += Random.nextFloat() * 1.5f
            
            // Trust slowly decreases if not interacted with
            if (loneliness > 50) {
                trust -= Random.nextFloat() * 0.5f
            }
        }
    }
}