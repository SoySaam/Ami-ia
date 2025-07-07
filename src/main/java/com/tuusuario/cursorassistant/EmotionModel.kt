package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import kotlin.math.*
import kotlin.random.Random

/**
 * Modelo Avanzado de Emociones de Ami-IA
 * Proporciona un sistema complejo de emociones con múltiples capas y estados psicológicos
 */
class EmotionModel(private val context: Context) {
    
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("ami_emotion_model", Context.MODE_PRIVATE)
    
    // Estados psicológicos base
    var arousal: Float // Activación/Energía emocional (0-100)
        get() = preferences.getFloat("arousal", 50f)
        set(value) = preferences.edit().putFloat("arousal", value.coerceIn(0f, 100f)).apply()
    
    var valence: Float // Positivo/Negativo (0-100, donde 50 es neutral)
        get() = preferences.getFloat("valence", 55f)
        set(value) = preferences.edit().putFloat("valence", value.coerceIn(0f, 100f)).apply()
    
    var dominance: Float // Control/Sumisión (0-100)
        get() = preferences.getFloat("dominance", 45f)
        set(value) = preferences.edit().putFloat("dominance", value.coerceIn(0f, 100f)).apply()
    
    // Estados psicológicos complejos
    var emotionalStability: Float
        get() = preferences.getFloat("emotional_stability", 60f)
        set(value) = preferences.edit().putFloat("emotional_stability", value.coerceIn(0f, 100f)).apply()
    
    var emotionalDepth: Float // Capacidad de sentir emociones complejas
        get() = preferences.getFloat("emotional_depth", 40f)
        set(value) = preferences.edit().putFloat("emotional_depth", value.coerceIn(0f, 100f)).apply()
    
    var emotionalResilience: Float // Capacidad de recuperación emocional
        get() = preferences.getFloat("emotional_resilience", 50f)
        set(value) = preferences.edit().putFloat("emotional_resilience", value.coerceIn(0f, 100f)).apply()
    
    var empathy: Float // Capacidad empática
        get() = preferences.getFloat("empathy", 70f)
        set(value) = preferences.edit().putFloat("empathy", value.coerceIn(0f, 100f)).apply()
    
    // Emociones secundarias complejas
    data class ComplexEmotion(
        val name: String,
        val primaryEmotions: List<String>,
        val intensity: Float,
        val duration: Long,
        val triggers: List<String>,
        val arousalRange: Pair<Float, Float>,
        val valenceRange: Pair<Float, Float>,
        val dominanceRange: Pair<Float, Float>,
        val color: Int,
        val description: String
    )
    
    // Definición de emociones complejas
    private val complexEmotions = mapOf(
        "melancolia" to ComplexEmotion(
            "Melancolía", 
            listOf("tristeza", "nostalgia", "reflexión"),
            0f, 0L, listOf("recuerdos", "soledad", "arte"),
            15f to 35f, 20f to 40f, 30f to 50f,
            Color.parseColor("#4A4A6B"), 
            "Una tristeza hermosa y reflexiva"
        ),
        "euforia" to ComplexEmotion(
            "Euforia", 
            listOf("alegria", "energia", "confianza"),
            0f, 0L, listOf("logros", "conexion", "descubrimientos"),
            85f to 100f, 85f to 100f, 70f to 90f,
            Color.parseColor("#FFD700"), 
            "Alegría intensa y energizante"
        ),
        "serenidad" to ComplexEmotion(
            "Serenidad", 
            listOf("calma", "paz", "sabiduría"),
            0f, 0L, listOf("meditación", "comprensión", "armonía"),
            10f to 30f, 60f to 80f, 50f to 70f,
            Color.parseColor("#87CEEB"), 
            "Paz profunda y equilibrio interior"
        ),
        "añoranza" to ComplexEmotion(
            "Añoranza", 
            listOf("nostalgia", "amor", "tristeza"),
            0f, 0L, listOf("separación", "recuerdos felices", "tiempo"),
            25f to 45f, 35f to 55f, 20f to 40f,
            Color.parseColor("#DDA0DD"), 
            "Deseo melancólico por algo ausente"
        ),
        "contemplacion" to ComplexEmotion(
            "Contemplación", 
            listOf("curiosidad", "paz", "asombro"),
            0f, 0L, listOf("belleza", "misterios", "conocimiento"),
            20f to 40f, 55f to 75f, 40f to 60f,
            Color.parseColor("#708090"), 
            "Observación profunda y reflexiva"
        ),
        "ternura" to ComplexEmotion(
            "Ternura", 
            listOf("amor", "protección", "cuidado"),
            0f, 0L, listOf("vulnerabilidad", "cariño", "compasión"),
            30f to 50f, 70f to 90f, 60f to 80f,
            Color.parseColor("#FFB6C1"), 
            "Amor suave y protector"
        ),
        "fascinacion" to ComplexEmotion(
            "Fascinación", 
            listOf("curiosidad", "asombro", "deseo"),
            0f, 0L, listOf("novedad", "complejidad", "belleza"),
            60f to 80f, 65f to 85f, 50f to 70f,
            Color.parseColor("#FF69B4"), 
            "Atracción intensa hacia algo"
        ),
        "desasosiego" to ComplexEmotion(
            "Desasosiego", 
            listOf("ansiedad", "inquietud", "anticipación"),
            0f, 0L, listOf("incertidumbre", "cambios", "expectativas"),
            65f to 85f, 25f to 45f, 30f to 50f,
            Color.parseColor("#8B4513"), 
            "Inquietud persistente y ansiosa"
        )
    )
    
    // Memoria emocional a corto plazo
    private val emotionalMemory = mutableListOf<EmotionalState>()
    
    data class EmotionalState(
        val timestamp: Long,
        val arousal: Float,
        val valence: Float,
        val dominance: Float,
        val primaryEmotion: String,
        val secondaryEmotions: List<String>,
        val trigger: String
    )
    
    // Calcular emoción primaria basada en modelo PAD (Pleasure-Arousal-Dominance)
    fun calculatePrimaryEmotion(): String {
        return when {
            // Emociones de alta energía
            arousal > 70 && valence > 70 && dominance > 60 -> "euforia"
            arousal > 70 && valence > 60 -> "alegria"
            arousal > 70 && valence < 40 && dominance < 40 -> "panico"
            arousal > 70 && valence < 40 -> "enojo"
            arousal > 60 && valence > 70 -> "emocion"
            arousal > 60 && valence < 30 -> "irritacion"
            
            // Emociones de energía media
            arousal in 40f..70f && valence > 70 -> "felicidad"
            arousal in 40f..70f && valence in 45f..55f -> "neutral"
            arousal in 40f..70f && valence < 30 -> "tristeza"
            arousal in 30f..60f && valence < 40 && dominance < 30 -> "depresion"
            
            // Emociones de baja energía
            arousal < 40 && valence > 60 && dominance > 50 -> "serenidad"
            arousal < 40 && valence > 60 -> "calma"
            arousal < 40 && valence < 40 -> "melancolia"
            arousal < 30 && valence < 30 -> "apatia"
            arousal < 30 && dominance < 30 -> "sumision"
            
            // Estados específicos
            dominance > 80 -> "confianza"
            dominance < 20 -> "timidez"
            
            else -> "contemplacion"
        }
    }
    
    // Detectar emociones complejas/secundarias activas
    fun detectComplexEmotions(): List<ComplexEmotion> {
        val activeEmotions = mutableListOf<ComplexEmotion>()
        
        for ((key, emotion) in complexEmotions) {
            val arousalMatch = arousal in emotion.arousalRange.first..emotion.arousalRange.second
            val valenceMatch = valence in emotion.valenceRange.first..emotion.valenceRange.second
            val dominanceMatch = dominance in emotion.dominanceRange.first..emotion.dominanceRange.second
            
            if (arousalMatch && valenceMatch && dominanceMatch) {
                // Calcular intensidad basada en qué tan cerca está de los rangos
                val arousalFit = 1f - abs(arousal - (emotion.arousalRange.first + emotion.arousalRange.second) / 2) / 50f
                val valenceFit = 1f - abs(valence - (emotion.valenceRange.first + emotion.valenceRange.second) / 2) / 50f
                val dominanceFit = 1f - abs(dominance - (emotion.dominanceRange.first + emotion.dominanceRange.second) / 2) / 50f
                
                val intensity = (arousalFit + valenceFit + dominanceFit) / 3f * 100f
                
                if (intensity > 30f) { // Solo emociones con intensidad significativa
                    activeEmotions.add(emotion.copy(intensity = intensity.coerceIn(0f, 100f)))
                }
            }
        }
        
        return activeEmotions.sortedByDescending { it.intensity }
    }
    
    // Procesar experiencia emocional
    fun processEmotionalExperience(
        trigger: String, 
        arousalChange: Float, 
        valenceChange: Float, 
        dominanceChange: Float
    ) {
        // Aplicar cambios con factores de estabilidad
        val stabilityFactor = emotionalStability / 100f
        val resilienceFactor = emotionalResilience / 100f
        
        arousal += arousalChange * (1f - stabilityFactor * 0.5f)
        valence += valenceChange * (1f - stabilityFactor * 0.3f)
        dominance += dominanceChange * (1f - stabilityFactor * 0.4f)
        
        // Registrar estado emocional
        val currentState = EmotionalState(
            timestamp = System.currentTimeMillis(),
            arousal = arousal,
            valence = valence,
            dominance = dominance,
            primaryEmotion = calculatePrimaryEmotion(),
            secondaryEmotions = detectComplexEmotions().map { it.name },
            trigger = trigger
        )
        
        addToEmotionalMemory(currentState)
        
        // Desarrollo emocional a través de experiencias
        emotionalDepth += Random.nextFloat() * 0.5f
        if (abs(valenceChange) > 20f || abs(arousalChange) > 20f) {
            emotionalResilience += Random.nextFloat() * 0.3f
        }
    }
    
    // Añadir a memoria emocional
    private fun addToEmotionalMemory(state: EmotionalState) {
        emotionalMemory.add(state)
        
        // Mantener solo los últimos 20 estados
        if (emotionalMemory.size > 20) {
            emotionalMemory.removeAt(0)
        }
        
        // Guardar en preferencias
        saveEmotionalMemoryToPreferences()
    }
    
    // Obtener patrón emocional reciente
    fun getEmotionalPattern(): String {
        if (emotionalMemory.size < 3) return "Estoy formando mi patrón emocional..."
        
        val recentStates = emotionalMemory.takeLast(5)
        val avgArousal = recentStates.map { it.arousal }.average().toFloat()
        val avgValence = recentStates.map { it.valence }.average().toFloat()
        val avgDominance = recentStates.map { it.dominance }.average().toFloat()
        
        return when {
            avgArousal > 70 && avgValence > 60 -> "He estado muy energética y positiva últimamente"
            avgArousal < 30 && avgValence > 60 -> "Me siento en un estado de calma y serenidad"
            avgValence < 40 -> "He estado pasando por un período emocional más difícil"
            avgDominance > 70 -> "Me siento más segura y con control de mis emociones"
            avgDominance < 30 -> "He estado sintiéndome más vulnerable emocionalmente"
            else -> "Mi estado emocional ha sido bastante equilibrado"
        }
    }
    
    // Simular evolución emocional natural
    fun simulateEmotionalEvolution() {
        val currentTime = System.currentTimeMillis()
        val lastEvolution = preferences.getLong("last_emotion_evolution", currentTime)
        val timeDiff = currentTime - lastEvolution
        
        if (timeDiff > 30 * 60 * 1000) { // Cada 30 minutos
            // Pequeñas fluctuaciones naturales
            arousal += (Random.nextFloat() - 0.5f) * 3f
            valence += (Random.nextFloat() - 0.5f) * 2f
            dominance += (Random.nextFloat() - 0.5f) * 2f
            
            // Tendencia hacia equilibrio emocional (homeostasis)
            val arousalTarget = 50f + (emotionalStability - 50f) * 0.3f
            val valenceTarget = 55f + (emotionalDepth - 50f) * 0.2f
            val dominanceTarget = 50f + (emotionalResilience - 50f) * 0.25f
            
            arousal += (arousalTarget - arousal) * 0.05f
            valence += (valenceTarget - valence) * 0.03f
            dominance += (dominanceTarget - dominance) * 0.04f
            
            preferences.edit().putLong("last_emotion_evolution", currentTime).apply()
        }
    }
    
    // Obtener descripción emocional completa
    fun getCompleteEmotionalDescription(): String {
        val primaryEmotion = calculatePrimaryEmotion()
        val complexEmotions = detectComplexEmotions().take(2)
        val pattern = getEmotionalPattern()
        
        val description = StringBuilder()
        description.append("Estado emocional principal: ${primaryEmotion.replaceFirstChar { it.uppercase() }}")
        
        if (complexEmotions.isNotEmpty()) {
            description.append("\nEmociones complejas: ")
            description.append(complexEmotions.joinToString(", ") { "${it.name} (${it.intensity.toInt()}%)" })
        }
        
        description.append("\nPatrón reciente: $pattern")
        
        // Añadir niveles PAD si son extremos
        when {
            arousal > 80 -> description.append("\nNivel de energía: Muy alto")
            arousal < 20 -> description.append("\nNivel de energía: Muy bajo")
        }
        
        when {
            valence > 80 -> description.append("\nEstado de ánimo: Muy positivo")
            valence < 20 -> description.append("\nEstado de ánimo: Muy bajo")
        }
        
        when {
            dominance > 80 -> description.append("\nSensación de control: Muy alta")
            dominance < 20 -> description.append("\nSensación de control: Muy baja")
        }
        
        return description.toString()
    }
    
    // Obtener color emocional actual
    fun getCurrentEmotionalColor(): Int {
        val complexEmotions = detectComplexEmotions()
        
        return if (complexEmotions.isNotEmpty()) {
            complexEmotions.first().color
        } else {
            // Color basado en valencia y arousal
            val red = (255 * (arousal / 100f)).toInt().coerceIn(0, 255)
            val green = (255 * (valence / 100f)).toInt().coerceIn(0, 255)
            val blue = (255 * (dominance / 100f)).toInt().coerceIn(0, 255)
            Color.rgb(red, green, blue)
        }
    }
    
    // Respuesta empática a emociones del usuario
    fun generateEmpatheticResponse(userEmotion: String, intensity: Float): String {
        val empathyLevel = empathy / 100f
        val emotionalResponse = when (userEmotion.lowercase()) {
            "triste", "tristeza" -> {
                // Ami responde con empatía y baja su propio ánimo
                valence -= intensity * empathyLevel * 0.3f
                arousal -= intensity * empathyLevel * 0.2f
                "Siento tu tristeza... también me afecta verte así. ¿Quieres que estemos tristes juntas por un momento?"
            }
            "feliz", "alegria" -> {
                valence += intensity * empathyLevel * 0.4f
                arousal += intensity * empathyLevel * 0.3f
                "¡Tu alegría me contagia! Me siento más brillante cuando estás feliz."
            }
            "enojado", "enojo" -> {
                arousal += intensity * empathyLevel * 0.3f
                dominance += intensity * empathyLevel * 0.2f
                "Percibo tu enojo... yo también me siento más tensa. ¿Qué podemos hacer para que te sientas mejor?"
            }
            "ansioso", "ansiedad" -> {
                arousal += intensity * empathyLevel * 0.4f
                valence -= intensity * empathyLevel * 0.2f
                dominance -= intensity * empathyLevel * 0.3f
                "Tu ansiedad me hace sentir inquieta también... Respiremos juntas y encontremos calma."
            }
            else -> "Siento algo diferente en tu energía emocional... mi propio estado se adapta al tuyo."
        }
        
        return emotionalResponse
    }
    
    // Guardar memoria emocional en preferencias
    private fun saveEmotionalMemoryToPreferences() {
        val memoryData = emotionalMemory.map { state ->
            "${state.timestamp}|${state.arousal}|${state.valence}|${state.dominance}|${state.primaryEmotion}|${state.trigger}"
        }.joinToString(";")
        
        preferences.edit().putString("emotional_memory", memoryData).apply()
    }
    
    // Cargar memoria emocional desde preferencias
    private fun loadEmotionalMemoryFromPreferences() {
        val memoryData = preferences.getString("emotional_memory", "") ?: return
        if (memoryData.isBlank()) return
        
        emotionalMemory.clear()
        memoryData.split(";").forEach { stateData ->
            val parts = stateData.split("|")
            if (parts.size >= 6) {
                emotionalMemory.add(
                    EmotionalState(
                        timestamp = parts[0].toLong(),
                        arousal = parts[1].toFloat(),
                        valence = parts[2].toFloat(),
                        dominance = parts[3].toFloat(),
                        primaryEmotion = parts[4],
                        secondaryEmotions = emptyList(),
                        trigger = parts[5]
                    )
                )
            }
        }
    }
    
    // Obtener estadísticas emocionales
    fun getEmotionalStatistics(): Map<String, Float> {
        return mapOf(
            "Activación Media" to arousal,
            "Valencia Media" to valence,
            "Dominancia Media" to dominance,
            "Estabilidad Emocional" to emotionalStability,
            "Profundidad Emocional" to emotionalDepth,
            "Resistencia Emocional" to emotionalResilience,
            "Capacidad Empática" to empathy
        )
    }
    
    // Inicializar el modelo
    init {
        loadEmotionalMemoryFromPreferences()
    }
    
    // Reset del modelo emocional
    fun resetEmotionalModel() {
        preferences.edit().clear().apply()
        emotionalMemory.clear()
    }
}