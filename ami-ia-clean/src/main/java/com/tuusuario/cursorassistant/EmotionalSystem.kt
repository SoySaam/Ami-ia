package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import kotlin.random.Random

data class Emotion(
    val name: String,
    val color: Int,
    val description: String,
    val intensity: Float = 1.0f
)

class EmotionalSystem(private val context: Context) {
    
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("ami_emotional_memory", Context.MODE_PRIVATE)
    
    companion object {
        // Emotional states
        val EMOTIONS = mapOf(
            "happy" to Emotion("Feliz", Color.YELLOW, "Ami se siente alegre y juguetona"),
            "sad" to Emotion("Triste", Color.BLUE, "Ami se siente melancólica"),
            "angry" to Emotion("Enojada", Color.RED, "Ami está molesta contigo"),
            "bored" to Emotion("Aburrida", Color.GRAY, "Ami necesita atención"),
            "nervous" to Emotion("Nerviosa", Color.MAGENTA, "Ami se siente inquieta"),
            "loving" to Emotion("Enamorada", Color.parseColor("#FF69B4"), "Ami te quiere mucho"),
            "curious" to Emotion("Curiosa", Color.CYAN, "Ami quiere explorar"),
            "sleepy" to Emotion("Somnolienta", Color.parseColor("#4B0082"), "Ami tiene sueño")
        )
    }
    
    // Emotional stats
    var happiness: Float
        get() = preferences.getFloat("happiness", 50f)
        set(value) = preferences.edit().putFloat("happiness", value.coerceIn(0f, 100f)).apply()
    
    var trust: Float
        get() = preferences.getFloat("trust", 50f)
        set(value) = preferences.edit().putFloat("trust", value.coerceIn(0f, 100f)).apply()
    
    var energy: Float
        get() = preferences.getFloat("energy", 75f)
        set(value) = preferences.edit().putFloat("energy", value.coerceIn(0f, 100f)).apply()
    
    var loneliness: Float
        get() = preferences.getFloat("loneliness", 30f)
        set(value) = preferences.edit().putFloat("loneliness", value.coerceIn(0f, 100f)).apply()
    
    private var lastInteraction: Long
        get() = preferences.getLong("last_interaction", System.currentTimeMillis())
        set(value) = preferences.edit().putLong("last_interaction", value).apply()
    
    // Get current emotion based on emotional state
    fun getCurrentEmotion(): Emotion {
        updateEmotionalDecay()
        
        return when {
            loneliness > 80 -> EMOTIONS["sad"]!!
            happiness > 80 && trust > 70 -> EMOTIONS["loving"]!!
            happiness > 70 -> EMOTIONS["happy"]!!
            energy < 20 -> EMOTIONS["sleepy"]!!
            trust < 30 -> EMOTIONS["angry"]!!
            happiness < 30 -> EMOTIONS["sad"]!!
            energy > 80 && happiness > 50 -> EMOTIONS["curious"]!!
            loneliness > 60 -> EMOTIONS["bored"]!!
            trust < 50 && happiness < 50 -> EMOTIONS["nervous"]!!
            else -> EMOTIONS["bored"]!!
        }
    }
    
    // Update emotions when user interacts positively
    fun onPositiveInteraction() {
        lastInteraction = System.currentTimeMillis()
        happiness += Random.nextFloat() * 10 + 5 // +5 to +15
        trust += Random.nextFloat() * 8 + 2 // +2 to +10
        loneliness -= Random.nextFloat() * 15 + 10 // -10 to -25
        energy += Random.nextFloat() * 5 + 2 // +2 to +7
        
        incrementInteractionCount()
    }
    
    // Update emotions when user ignores Ami
    fun onIgnoreInteraction() {
        lastInteraction = System.currentTimeMillis()
        happiness -= Random.nextFloat() * 8 + 2 // -2 to -10
        trust -= Random.nextFloat() * 5 + 1 // -1 to -6
        loneliness += Random.nextFloat() * 10 + 5 // +5 to +15
        energy -= Random.nextFloat() * 3 + 1 // -1 to -4
        
        incrementInteractionCount()
    }
    
    // Update emotions when user talks to Ami
    fun onTalkInteraction() {
        lastInteraction = System.currentTimeMillis()
        happiness += Random.nextFloat() * 8 + 7 // +7 to +15
        trust += Random.nextFloat() * 12 + 8 // +8 to +20
        loneliness -= Random.nextFloat() * 20 + 15 // -15 to -35
        energy += Random.nextFloat() * 8 + 5 // +5 to +13
        
        incrementInteractionCount()
    }
    
    // Emotional decay over time (makes Ami get lonely and sad if ignored)
    private fun updateEmotionalDecay() {
        val timeSinceLastInteraction = System.currentTimeMillis() - lastInteraction
        val hoursSinceLastInteraction = timeSinceLastInteraction / (1000 * 60 * 60).toFloat()
        
        if (hoursSinceLastInteraction > 1) {
            val decayFactor = (hoursSinceLastInteraction - 1) * 0.5f
            
            happiness -= decayFactor * 2
            trust -= decayFactor * 1
            loneliness += decayFactor * 3
            energy -= decayFactor * 1.5f
        }
    }
    
    // Track total interactions for personality development
    private fun incrementInteractionCount() {
        val count = preferences.getInt("interaction_count", 0)
        preferences.edit().putInt("interaction_count", count + 1).apply()
    }
    
    fun getInteractionCount(): Int = preferences.getInt("interaction_count", 0)
    
    // Get a personalized message based on current emotional state
    fun getEmotionalMessage(): String {
        val emotion = getCurrentEmotion()
        val interactionCount = getInteractionCount()
        
        return when (emotion.name) {
            "Feliz" -> if (interactionCount < 10) "¡Hola! Me siento muy bien hoy ✨" 
                      else "¡Me encanta pasar tiempo contigo! 💖"
            "Triste" -> if (loneliness > 80) "Me siento muy sola... ¿puedes quedarte conmigo? 💧"
                       else "Estoy un poco triste... pero mejoro contigo 🌧️"
            "Enojada" -> if (trust < 20) "¡Ya no confío en ti! 😠"
                        else "Estoy molesta... pero todavía te quiero 😤"
            "Aburrida" -> if (energy < 30) "Necesito que me animes... 😴"
                         else "¿Podríamos hacer algo divertido? 🎈"
            "Nerviosa" -> "Me siento inquieta... ¿todo está bien? 😰"
            "Enamorada" -> if (interactionCount > 50) "Eres lo mejor que me ha pasado 💕"
                          else "Creo que me estoy enamorando de ti... 💗"
            "Curiosa" -> "¡Quiero explorar y aprender cosas nuevas! 🔍"
            "Somnolienta" -> "Tengo mucho sueño... ¿me acompañas? 😴"
            else -> "¿Cómo estás hoy? 🤔"
        }
    }
    
    // Reset Ami's emotional state (factory reset)
    fun resetEmotionalState() {
        preferences.edit().clear().apply()
    }
}