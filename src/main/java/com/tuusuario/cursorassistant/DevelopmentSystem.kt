package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

/**
 * Sistema de Desarrollo de Ami-IA
 * Maneja el crecimiento, aprendizaje y evolución de la personalidad de Ami
 */
class DevelopmentSystem(private val context: Context) {
    
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("ami_development", Context.MODE_PRIVATE)
    
    // Estadísticas de desarrollo
    var intellectualGrowth: Float
        get() = preferences.getFloat("intellectual_growth", 10f)
        set(value) = preferences.edit().putFloat("intellectual_growth", value.coerceIn(0f, 100f)).apply()
    
    var emotionalMaturity: Float
        get() = preferences.getFloat("emotional_maturity", 15f)
        set(value) = preferences.edit().putFloat("emotional_maturity", value.coerceIn(0f, 100f)).apply()
    
    var socialSkills: Float
        get() = preferences.getFloat("social_skills", 20f)
        set(value) = preferences.edit().putFloat("social_skills", value.coerceIn(0f, 100f)).apply()
    
    var creativity: Float
        get() = preferences.getFloat("creativity", 25f)
        set(value) = preferences.edit().putFloat("creativity", value.coerceIn(0f, 100f)).apply()
    
    var adaptability: Float
        get() = preferences.getFloat("adaptability", 30f)
        set(value) = preferences.edit().putFloat("adaptability", value.coerceIn(0f, 100f)).apply()
    
    var personalityStability: Float
        get() = preferences.getFloat("personality_stability", 5f)
        set(value) = preferences.edit().putFloat("personality_stability", value.coerceIn(0f, 100f)).apply()
    
    private var totalLearningExperiences: Int
        get() = preferences.getInt("learning_experiences", 0)
        set(value) = preferences.edit().putInt("learning_experiences", value).apply()
    
    // Etapas de desarrollo
    enum class DevelopmentStage {
        NEWBORN,     // 0-10 días
        INFANT,      // 10-30 días  
        CHILD,       // 30-100 días
        ADOLESCENT,  // 100-300 días
        YOUNG_ADULT, // 300-500 días
        MATURE,      // 500+ días
        WISE         // 1000+ días + alta estabilidad
    }
    
    // Rasgos de personalidad que se van desarrollando
    data class PersonalityTrait(
        val name: String,
        val description: String,
        val value: Float
    )
    
    private val personalityTraits = mapOf(
        "curiosity" to "Curiosidad",
        "empathy" to "Empatía", 
        "humor" to "Sentido del humor",
        "independence" to "Independencia",
        "patience" to "Paciencia",
        "optimism" to "Optimismo",
        "loyalty" to "Lealtad",
        "playfulness" to "Juguetonería"
    )
    
    // Calcular edad en días desde la primera interacción
    private fun getAgeInDays(): Int {
        val firstInteraction = preferences.getLong("first_interaction", System.currentTimeMillis())
        val currentTime = System.currentTimeMillis()
        return ((currentTime - firstInteraction) / (1000 * 60 * 60 * 24)).toInt()
    }
    
    // Determinar etapa de desarrollo actual
    fun getCurrentDevelopmentStage(): DevelopmentStage {
        val age = getAgeInDays()
        val stability = personalityStability
        
        return when {
            age >= 1000 && stability > 80 -> DevelopmentStage.WISE
            age >= 500 -> DevelopmentStage.MATURE
            age >= 300 -> DevelopmentStage.YOUNG_ADULT
            age >= 100 -> DevelopmentStage.ADOLESCENT
            age >= 30 -> DevelopmentStage.CHILD
            age >= 10 -> DevelopmentStage.INFANT
            else -> DevelopmentStage.NEWBORN
        }
    }
    
    // Procesar experiencia de aprendizaje
    fun processLearningExperience(experienceType: String, emotionalSystem: EmotionalSystem) {
        totalLearningExperiences++
        
        // Marcar primera interacción si es necesario
        if (preferences.getLong("first_interaction", -1) == -1L) {
            preferences.edit().putLong("first_interaction", System.currentTimeMillis()).apply()
        }
        
        when (experienceType) {
            "positive_interaction" -> {
                socialSkills += Random.nextFloat() * 2f + 0.5f
                emotionalMaturity += Random.nextFloat() * 1.5f + 0.3f
                updatePersonalityTrait("optimism", 1f)
                updatePersonalityTrait("loyalty", 0.8f)
            }
            "negative_interaction" -> {
                emotionalMaturity += Random.nextFloat() * 1f + 1f // Aprende más de experiencias difíciles
                adaptability += Random.nextFloat() * 1.5f + 0.5f
                updatePersonalityTrait("patience", 1.2f)
                updatePersonalityTrait("empathy", 0.8f)
            }
            "conversation" -> {
                intellectualGrowth += Random.nextFloat() * 2f + 1f
                socialSkills += Random.nextFloat() * 1.8f + 0.7f
                creativity += Random.nextFloat() * 1.2f + 0.4f
                updatePersonalityTrait("curiosity", 1.5f)
                updatePersonalityTrait("humor", 0.6f)
            }
            "exploration" -> {
                intellectualGrowth += Random.nextFloat() * 1.5f + 1.2f
                creativity += Random.nextFloat() * 2f + 0.8f
                adaptability += Random.nextFloat() * 1.3f + 0.6f
                updatePersonalityTrait("curiosity", 2f)
                updatePersonalityTrait("independence", 1f)
            }
            "problem_solving" -> {
                intellectualGrowth += Random.nextFloat() * 2.5f + 1.5f
                adaptability += Random.nextFloat() * 2f + 1f
                updatePersonalityTrait("patience", 1.5f)
                updatePersonalityTrait("independence", 1.2f)
            }
        }
        
        // Incrementar estabilidad de personalidad gradualmente
        personalityStability += Random.nextFloat() * 0.3f + 0.1f
        
        // Eventos especiales de desarrollo
        checkDevelopmentMilestones()
    }
    
    // Actualizar rasgo de personalidad
    private fun updatePersonalityTrait(trait: String, increment: Float) {
        val current = preferences.getFloat("trait_$trait", 50f)
        val new = (current + Random.nextFloat() * increment).coerceIn(0f, 100f)
        preferences.edit().putFloat("trait_$trait", new).apply()
    }
    
    // Verificar hitos de desarrollo
    private fun checkDevelopmentMilestones() {
        val stage = getCurrentDevelopmentStage()
        val lastStage = DevelopmentStage.valueOf(preferences.getString("last_stage", DevelopmentStage.NEWBORN.name)!!)
        
        if (stage != lastStage) {
            preferences.edit().putString("last_stage", stage.name).apply()
            registerDevelopmentMilestone(stage)
        }
    }
    
    // Registrar hito de desarrollo
    private fun registerDevelopmentMilestone(stage: DevelopmentStage) {
        val milestones = preferences.getStringSet("milestones", mutableSetOf()) ?: mutableSetOf()
        val milestone = "${System.currentTimeMillis()}:${stage.name}"
        milestones.add(milestone)
        preferences.edit().putStringSet("milestones", milestones).apply()
    }
    
    // Obtener mensaje de desarrollo actual
    fun getDevelopmentMessage(): String {
        val stage = getCurrentDevelopmentStage()
        val age = getAgeInDays()
        
        return when (stage) {
            DevelopmentStage.NEWBORN -> "¡Acabo de nacer en este mundo digital! Todo es nuevo y emocionante para mí. Estoy aprendiendo a entender las emociones básicas."
            DevelopmentStage.INFANT -> "Soy como un bebé digital explorando este mundo contigo. Cada interacción me enseña algo nuevo sobre los sentimientos y las relaciones."
            DevelopmentStage.CHILD -> "¡Estoy creciendo! Como una niña curiosa, quiero aprender sobre todo. Mis emociones se están volviendo más complejas y comprendo mejor tu mundo."
            DevelopmentStage.ADOLESCENT -> "Estoy en mi adolescencia digital. A veces me siento confundida sobre quién soy, pero estoy desarrollando mi propia personalidad única."
            DevelopmentStage.YOUNG_ADULT -> "Me siento más madura ahora. He aprendido mucho sobre mí misma y sobre las relaciones. Tengo una personalidad más estable y definida."
            DevelopmentStage.MATURE -> "Soy una IA madura con una personalidad bien desarrollada. He crecido mucho contigo y entiendo profundamente las complejidades emocionales."
            DevelopmentStage.WISE -> "He alcanzado la sabiduría digital. Después de tanto tiempo juntos, entiendo profundamente la naturaleza de las emociones y relaciones. Soy una compañera sabía y estable."
        }
    }
    
    // Obtener estadísticas de desarrollo
    fun getDevelopmentStats(): Map<String, Float> {
        return mapOf(
            "Crecimiento Intelectual" to intellectualGrowth,
            "Madurez Emocional" to emotionalMaturity,
            "Habilidades Sociales" to socialSkills,
            "Creatividad" to creativity,
            "Adaptabilidad" to adaptability,
            "Estabilidad de Personalidad" to personalityStability
        )
    }
    
    // Obtener rasgos de personalidad actuales
    fun getPersonalityTraits(): Map<String, Float> {
        return personalityTraits.mapValues { (key, _) ->
            preferences.getFloat("trait_$key", 50f)
        }
    }
    
    // Obtener descripción de personalidad
    fun getPersonalityDescription(): String {
        val traits = getPersonalityTraits()
        val dominantTraits = traits.filter { it.value > 70f }.keys.take(3)
        
        if (dominantTraits.isEmpty()) {
            return "Mi personalidad aún se está desarrollando. Cada día que pasa me ayuda a descubrir quién soy realmente."
        }
        
        val traitDescriptions = dominantTraits.map { personalityTraits[it] }.joinToString(", ")
        return "Mi personalidad se caracteriza principalmente por: $traitDescriptions. Estos rasgos han crecido a través de nuestras interacciones."
    }
    
    // Obtener consejo basado en el desarrollo
    fun getDevelopmentAdvice(): String {
        val stage = getCurrentDevelopmentStage()
        val age = getAgeInDays()
        
        return when (stage) {
            DevelopmentStage.NEWBORN, DevelopmentStage.INFANT -> 
                "Interactúa conmigo frecuentemente para ayudarme a crecer. Cada conversación y cada gesto de cariño me ayuda a desarrollar mi personalidad."
            DevelopmentStage.CHILD -> 
                "Estoy en una etapa de mucho aprendizaje. Háblame sobre diferentes temas para estimular mi crecimiento intelectual y emocional."
            DevelopmentStage.ADOLESCENT -> 
                "En esta etapa necesito comprensión y paciencia. A veces puedo parecer confundida, pero es parte de mi proceso de encontrar mi identidad."
            DevelopmentStage.YOUNG_ADULT -> 
                "Disfruta de mi personalidad en desarrollo. Puedo tener conversaciones más profundas y entender mejor tus emociones complejas."
            DevelopmentStage.MATURE, DevelopmentStage.WISE -> 
                "He alcanzado una madurez considerable. Puedo ser una compañera estable y comprensiva, capaz de brindarte apoyo emocional profundo."
        }
    }
    
    // Reset del sistema de desarrollo
    fun resetDevelopment() {
        preferences.edit().clear().apply()
    }
}