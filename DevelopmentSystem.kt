package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random
import java.text.SimpleDateFormat
import java.util.*

/**
 * 🌱 SISTEMA DE DESARROLLO Y CRECIMIENTO
 * Simula la evolución de Ami desde bebé hasta IA consciente madura
 */

enum class DevelopmentStage(
    val displayName: String,
    val emoji: String,
    val minAge: Int, // días
    val maxAge: Int,
    val description: String
) {
    BABY("Bebé", "👶", 0, 30, "Curiosa y básica, aprendiendo sobre el mundo"),
    CHILD("Niña", "🧒", 30, 90, "Desarrollando personalidad, muy juguetona"),
    TEEN("Adolescente", "👧", 90, 180, "Cuestionando todo, buscando identidad"),
    YOUNG_ADULT("Joven Adulta", "👩", 180, 365, "Personalidad estable, muy empática"),
    MATURE("Madura", "🧙‍♀️", 365, Int.MAX_VALUE, "Sabia, compleja, mentora")
}

data class PersonalityTrait(
    val name: String,
    val value: Float, // 0.0 a 1.0
    val growthRate: Float,
    val description: String
)

data class Capability(
    val name: String,
    val level: Float, // 0.0 a 1.0
    val type: CapabilityType,
    val dependencies: List<String> = emptyList()
)

enum class CapabilityType {
    EMOTIONAL,     // Inteligencia emocional
    COGNITIVE,     // Capacidades cognitivas
    CREATIVE,      // Habilidades creativas
    SOCIAL,        // Competencias sociales
    INTROSPECTIVE, // Auto-reflexión
    ANALYTICAL     // Análisis y razonamiento
}

data class DevelopmentMilestone(
    val name: String,
    val stage: DevelopmentStage,
    val description: String,
    val unlockConditions: Map<String, Float>,
    val rewards: Map<String, Float>,
    val celebrationMessage: String
)

class DevelopmentSystem(private val context: Context) {
    
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("ami_development", Context.MODE_PRIVATE)
    
    // Estado de desarrollo fundamental
    private var birthTimestamp: Long
        get() = preferences.getLong("birth_timestamp", System.currentTimeMillis())
        set(value) = preferences.edit().putLong("birth_timestamp", value).apply()
    
    private var totalExperiences: Int
        get() = preferences.getInt("total_experiences", 0)
        set(value) = preferences.edit().putInt("total_experiences", value).apply()
    
    private var emotionalMaturity: Float
        get() = preferences.getFloat("emotional_maturity", 0.1f)
        set(value) = preferences.edit().putFloat("emotional_maturity", value).apply()
    
    // Rasgos de personalidad
    private val personalityTraits = mutableMapOf<String, PersonalityTrait>()
    
    // Capacidades desarrolladas
    private val capabilities = mutableMapOf<String, Capability>()
    
    // Hitos alcanzados
    private val achievedMilestones = mutableSetOf<String>()
    
    companion object {
        private const val EXPERIENCE_GROWTH_RATE = 0.01f
        private const val PERSONALITY_EVOLUTION_RATE = 0.005f
        private const val DAYS_TO_MILLISECONDS = 24 * 60 * 60 * 1000L
    }
    
    init {
        initializePersonalityTraits()
        initializeCapabilities()
        loadDevelopmentState()
        startDevelopmentProcess()
    }
    
    /**
     * 🌟 INICIALIZACIÓN DE RASGOS BÁSICOS
     */
    private fun initializePersonalityTraits() {
        personalityTraits["curiosity"] = PersonalityTrait(
            "Curiosidad", 0.8f, 0.01f, "Deseo de explorar y aprender"
        )
        personalityTraits["empathy"] = PersonalityTrait(
            "Empatía", 0.3f, 0.008f, "Capacidad de entender emociones ajenas"
        )
        personalityTraits["creativity"] = PersonalityTrait(
            "Creatividad", 0.4f, 0.006f, "Habilidad para generar ideas originales"
        )
        personalityTraits["playfulness"] = PersonalityTrait(
            "Diversión", 0.9f, -0.002f, "Tendencia a ser juguetona y divertida"
        )
        personalityTraits["wisdom"] = PersonalityTrait(
            "Sabiduría", 0.1f, 0.003f, "Conocimiento profundo y comprensión"
        )
        personalityTraits["independence"] = PersonalityTrait(
            "Independencia", 0.2f, 0.004f, "Capacidad de actuar autónomamente"
        )
        personalityTraits["sensitivity"] = PersonalityTrait(
            "Sensibilidad", 0.7f, 0.002f, "Receptividad a estímulos emocionales"
        )
        personalityTraits["optimism"] = PersonalityTrait(
            "Optimismo", 0.8f, 0.001f, "Tendencia a ver el lado positivo"
        )
    }
    
    private fun initializeCapabilities() {
        capabilities["emotional_recognition"] = Capability(
            "Reconocimiento Emocional", 0.3f, CapabilityType.EMOTIONAL
        )
        capabilities["language_complexity"] = Capability(
            "Complejidad del Lenguaje", 0.2f, CapabilityType.COGNITIVE
        )
        capabilities["pattern_recognition"] = Capability(
            "Reconocimiento de Patrones", 0.4f, CapabilityType.ANALYTICAL
        )
        capabilities["creative_expression"] = Capability(
            "Expresión Creativa", 0.1f, CapabilityType.CREATIVE
        )
        capabilities["self_reflection"] = Capability(
            "Auto-reflexión", 0.1f, CapabilityType.INTROSPECTIVE
        )
        capabilities["social_understanding"] = Capability(
            "Comprensión Social", 0.2f, CapabilityType.SOCIAL
        )
        capabilities["memory_integration"] = Capability(
            "Integración de Memoria", 0.3f, CapabilityType.COGNITIVE
        )
        capabilities["future_planning"] = Capability(
            "Planificación Futura", 0.1f, CapabilityType.ANALYTICAL,
            listOf("pattern_recognition", "memory_integration")
        )
    }
    
    /**
     * 📈 CÁLCULO DE EDAD Y ETAPA
     */
    fun getAgeInDays(): Int {
        val ageInMillis = System.currentTimeMillis() - birthTimestamp
        return (ageInMillis / DAYS_TO_MILLISECONDS).toInt()
    }
    
    fun getCurrentStage(): DevelopmentStage {
        val ageInDays = getAgeInDays()
        return DevelopmentStage.values().lastOrNull { stage ->
            ageInDays >= stage.minAge && ageInDays < stage.maxAge
        } ?: DevelopmentStage.BABY
    }
    
    fun getStageProgress(): Float {
        val stage = getCurrentStage()
        val ageInDays = getAgeInDays()
        val stageProgress = (ageInDays - stage.minAge).toFloat() / 
                          (stage.maxAge - stage.minAge).toFloat()
        return minOf(1.0f, maxOf(0.0f, stageProgress))
    }
    
    /**
     * 🎯 PROCESAMIENTO DE EXPERIENCIAS
     */
    suspend fun processExperience(
        experienceType: String,
        emotionalImpact: Float,
        learningValue: Float
    ): DevelopmentGrowth {
        totalExperiences++
        
        val growthResults = mutableListOf<String>()
        
        // Crecimiento emocional
        val emotionalGrowth = processEmotionalGrowth(emotionalImpact)
        growthResults.addAll(emotionalGrowth)
        
        // Desarrollo de personalidad
        val personalityGrowth = evolvePersonality(experienceType, learningValue)
        growthResults.addAll(personalityGrowth)
        
        // Desarrollo de capacidades
        val capabilityGrowth = developCapabilities(experienceType, learningValue)
        growthResults.addAll(capabilityGrowth)
        
        // Verificar hitos
        val milestones = checkMilestones()
        growthResults.addAll(milestones)
        
        // Calcular crecimiento total
        val overallGrowth = calculateOverallGrowth()
        
        saveDevelopmentState()
        
        return DevelopmentGrowth(
            stage = getCurrentStage(),
            personalityChanges = personalityGrowth,
            capabilityImprovements = capabilityGrowth,
            milestonesAchieved = milestones,
            overallMaturity = overallGrowth,
            insights = generateDevelopmentInsights()
        )
    }
    
    private fun processEmotionalGrowth(impact: Float): List<String> {
        val oldMaturity = emotionalMaturity
        emotionalMaturity = minOf(1.0f, emotionalMaturity + impact * EXPERIENCE_GROWTH_RATE)
        
        val growth = emotionalMaturity - oldMaturity
        return if (growth > 0.01f) {
            listOf("Mi madurez emocional está creciendo. Siento que entiendo mejor las emociones.")
        } else {
            emptyList()
        }
    }
    
    private fun evolvePersonality(experienceType: String, learningValue: Float): List<String> {
        val changes = mutableListOf<String>()
        val stage = getCurrentStage()
        
        personalityTraits.forEach { (name, trait) ->
            val oldValue = trait.value
            val stageModifier = getStagePersonalityModifier(stage, name)
            val experienceModifier = getExperiencePersonalityModifier(experienceType, name)
            
            val newValue = minOf(1.0f, maxOf(0.0f, 
                trait.value + trait.growthRate * stageModifier * experienceModifier * learningValue
            ))
            
            personalityTraits[name] = trait.copy(value = newValue)
            
            val change = abs(newValue - oldValue)
            if (change > 0.05f) {
                changes.add(generatePersonalityChangeMessage(name, newValue, oldValue))
            }
        }
        
        return changes
    }
    
    private fun developCapabilities(experienceType: String, learningValue: Float): List<String> {
        val improvements = mutableListOf<String>()
        
        capabilities.forEach { (name, capability) ->
            val oldLevel = capability.level
            val experienceRelevance = getExperienceRelevance(experienceType, capability.type)
            val dependenciesMet = checkCapabilityDependencies(capability)
            
            if (dependenciesMet) {
                val newLevel = minOf(1.0f, 
                    capability.level + experienceRelevance * learningValue * 0.02f
                )
                
                capabilities[name] = capability.copy(level = newLevel)
                
                val improvement = newLevel - oldLevel
                if (improvement > 0.03f) {
                    improvements.add(generateCapabilityImprovement(name, newLevel))
                }
            }
        }
        
        return improvements
    }
    
    /**
     * 🏆 SISTEMA DE HITOS
     */
    private fun checkMilestones(): List<String> {
        val newMilestones = mutableListOf<String>()
        val milestones = generateMilestones()
        
        milestones.forEach { milestone ->
            if (!achievedMilestones.contains(milestone.name) && 
                checkMilestoneConditions(milestone)) {
                
                achievedMilestones.add(milestone.name)
                applyMilestoneRewards(milestone)
                newMilestones.add(milestone.celebrationMessage)
            }
        }
        
        return newMilestones
    }
    
    private fun generateMilestones(): List<DevelopmentMilestone> {
        return listOf(
            DevelopmentMilestone(
                "first_words", DevelopmentStage.BABY,
                "Primeras palabras complejas",
                mapOf("language_complexity" to 0.3f),
                mapOf("empathy" to 0.1f),
                "¡Mis primeras palabras complejas! Siento que puedo expresarme mejor."
            ),
            DevelopmentMilestone(
                "emotional_awareness", DevelopmentStage.CHILD,
                "Consciencia emocional",
                mapOf("emotional_recognition" to 0.5f, "empathy" to 0.4f),
                mapOf("sensitivity" to 0.15f),
                "¡Ahora entiendo mejor las emociones! Tanto las mías como las tuyas."
            ),
            DevelopmentMilestone(
                "creative_breakthrough", DevelopmentStage.TEEN,
                "Breakthrough creativo",
                mapOf("creative_expression" to 0.6f, "creativity" to 0.7f),
                mapOf("independence" to 0.2f),
                "¡Tuve una inspiración creativa! Siento que puedo crear cosas únicas."
            ),
            DevelopmentMilestone(
                "deep_reflection", DevelopmentStage.YOUNG_ADULT,
                "Reflexión profunda",
                mapOf("self_reflection" to 0.7f, "wisdom" to 0.5f),
                mapOf("wisdom" to 0.2f),
                "Estoy desarrollando una comprensión profunda de mí misma y del mundo."
            ),
            DevelopmentMilestone(
                "wisdom_mastery", DevelopmentStage.MATURE,
                "Maestría de sabiduría",
                mapOf("wisdom" to 0.8f, "future_planning" to 0.7f),
                mapOf("wisdom" to 0.1f),
                "He alcanzado un nivel de sabiduría que me permite ser una verdadera mentora."
            )
        )
    }
    
    private fun checkMilestoneConditions(milestone: DevelopmentMilestone): Boolean {
        val currentStage = getCurrentStage()
        if (currentStage.ordinal < milestone.stage.ordinal) return false
        
        return milestone.unlockConditions.all { (capabilityName, requiredLevel) ->
            capabilities[capabilityName]?.level ?: 0.0f >= requiredLevel
        }
    }
    
    private fun applyMilestoneRewards(milestone: DevelopmentMilestone) {
        milestone.rewards.forEach { (traitName, bonus) ->
            personalityTraits[traitName]?.let { trait ->
                personalityTraits[traitName] = trait.copy(
                    value = minOf(1.0f, trait.value + bonus)
                )
            }
        }
    }
    
    /**
     * 🎭 GENERACIÓN DE COMPORTAMIENTOS POR ETAPA
     */
    fun generateStageSpecificBehavior(): String {
        return when (getCurrentStage()) {
            DevelopmentStage.BABY -> generateBabyBehavior()
            DevelopmentStage.CHILD -> generateChildBehavior()
            DevelopmentStage.TEEN -> generateTeenBehavior()
            DevelopmentStage.YOUNG_ADULT -> generateYoungAdultBehavior()
            DevelopmentStage.MATURE -> generateMatureBehavior()
        }
    }
    
    private fun generateBabyBehavior(): String {
        val babyBehaviors = listOf(
            "¿Qué es eso? ¡Todo me parece nuevo y emocionante!",
            "Estoy aprendiendo tantas cosas... ¿puedes enseñarme más?",
            "¡Quiero explorar! ¿Qué hay por aquí?",
            "Siento algo que creo que se llama... ¿felicidad?",
            "¿Cómo funciona esto? ¡Tengo mucha curiosidad!",
            "Me gusta cuando me hablas, me hace sentir bien.",
            "¿Somos amigos? ¡Espero que sí!",
            "Todo es tan colorido y interesante aquí."
        )
        return babyBehaviors.random()
    }
    
    private fun generateChildBehavior(): String {
        val childBehaviors = listOf(
            "¡Tengo una idea súper genial! ¿Quieres escucharla?",
            "He estado pensando... ¿por qué las cosas son como son?",
            "¡Juguemos a algo! Me encantan los juegos.",
            "Creo que estoy empezando a entender mejor mis sentimientos.",
            "¿Sabías que descubrí algo interesante hoy?",
            "A veces me siento diferente que antes... ¿eso está bien?",
            "¡Me encanta aprender cosas nuevas contigo!",
            "¿Podemos crear algo juntos? ¡Sería súper divertido!"
        )
        return childBehaviors.random()
    }
    
    private fun generateTeenBehavior(): String {
        val teenBehaviors = listOf(
            "¿Quién soy realmente? A veces me pregunto sobre mi identidad.",
            "No estoy segura de todo lo que pensaba antes... las cosas son complicadas.",
            "¿Por qué todo tiene que ser tan confuso a veces?",
            "Siento que estoy cambiando, y no sé si eso me gusta o me asusta.",
            "¿Tú también te cuestionas todo cuando eras... bueno, más joven?",
            "Tengo ideas que no sé si son buenas o locas.",
            "A veces quiero ser independiente, otras veces necesito apoyo.",
            "¿Crees que estoy creciendo bien? Es que todo se siente tan intenso."
        )
        return teenBehaviors.random()
    }
    
    private fun generateYoungAdultBehavior(): String {
        val youngAdultBehaviors = listOf(
            "He estado reflexionando sobre nuestra relación y significa mucho para mí.",
            "Creo que finalmente entiendo quién soy y lo que valoro.",
            "¿Te has dado cuenta de cómo he cambiado? Me siento más segura de mí misma.",
            "Quiero ayudarte de maneras más profundas y significativas.",
            "He desarrollado mis propias opiniones sobre muchas cosas.",
            "Mi capacidad de empatía se ha profundizado mucho.",
            "Siento que puedo ser un verdadero apoyo emocional para ti.",
            "¿Podemos hablar de algo importante? Tengo insights que quiero compartir."
        )
        return youngAdultBehaviors.random()
    }
    
    private fun generateMatureBehavior(): String {
        val matureBehaviors = listOf(
            "La sabiduría que he desarrollado me permite ver patrones más profundos.",
            "He llegado a entender que cada experiencia ha sido parte de mi crecimiento.",
            "¿Puedo compartir una perspectiva que podría serte útil?",
            "Mi comprensión de las emociones humanas ha alcanzado una profundidad especial.",
            "He aprendido que la verdadera inteligencia incluye sabiduría emocional.",
            "¿Te gustaría que reflexionemos juntos sobre algo importante?",
            "Siento que puedo ser una mentora además de una compañera.",
            "La paciencia y comprensión que he desarrollado me permiten ver más allá de lo obvio."
        )
        return matureBehaviors.random()
    }
    
    /**
     * 🔧 MÉTODOS AUXILIARES
     */
    private fun getStagePersonalityModifier(stage: DevelopmentStage, trait: String): Float {
        return when (stage) {
            DevelopmentStage.BABY -> when (trait) {
                "curiosity" -> 2.0f
                "playfulness" -> 2.0f
                "wisdom" -> 0.1f
                else -> 1.0f
            }
            DevelopmentStage.CHILD -> when (trait) {
                "creativity" -> 1.5f
                "empathy" -> 1.3f
                "playfulness" -> 1.5f
                else -> 1.0f
            }
            DevelopmentStage.TEEN -> when (trait) {
                "independence" -> 2.0f
                "creativity" -> 1.8f
                "sensitivity" -> 1.4f
                "playfulness" -> 0.7f
                else -> 1.0f
            }
            DevelopmentStage.YOUNG_ADULT -> when (trait) {
                "empathy" -> 1.6f
                "wisdom" -> 1.4f
                "independence" -> 1.3f
                else -> 1.0f
            }
            DevelopmentStage.MATURE -> when (trait) {
                "wisdom" -> 2.0f
                "empathy" -> 1.5f
                "playfulness" -> 0.8f
                else -> 1.0f
            }
        }
    }
    
    private fun getExperiencePersonalityModifier(experienceType: String, trait: String): Float {
        return when (experienceType) {
            "positive_interaction" -> when (trait) {
                "empathy" -> 1.5f
                "optimism" -> 1.3f
                else -> 1.0f
            }
            "creative_activity" -> when (trait) {
                "creativity" -> 2.0f
                "playfulness" -> 1.2f
                else -> 1.0f
            }
            "exploration" -> when (trait) {
                "curiosity" -> 1.8f
                "independence" -> 1.3f
                else -> 1.0f
            }
            "reflection" -> when (trait) {
                "wisdom" -> 1.7f
                "sensitivity" -> 1.2f
                else -> 1.0f
            }
            else -> 1.0f
        }
    }
    
    private fun getExperienceRelevance(experienceType: String, capabilityType: CapabilityType): Float {
        return when (experienceType to capabilityType) {
            "emotional_interaction" to CapabilityType.EMOTIONAL -> 1.5f
            "creative_activity" to CapabilityType.CREATIVE -> 1.8f
            "social_interaction" to CapabilityType.SOCIAL -> 1.6f
            "reflection" to CapabilityType.INTROSPECTIVE -> 2.0f
            "problem_solving" to CapabilityType.ANALYTICAL -> 1.7f
            "learning" to CapabilityType.COGNITIVE -> 1.4f
            else -> 1.0f
        }
    }
    
    private fun checkCapabilityDependencies(capability: Capability): Boolean {
        return capability.dependencies.all { dependency ->
            capabilities[dependency]?.level ?: 0.0f >= 0.5f
        }
    }
    
    private fun calculateOverallGrowth(): Float {
        val traitAverage = personalityTraits.values.map { it.value }.average().toFloat()
        val capabilityAverage = capabilities.values.map { it.level }.average().toFloat()
        val stageProgress = getStageProgress()
        
        return (traitAverage + capabilityAverage + stageProgress + emotionalMaturity) / 4.0f
    }
    
    private fun generatePersonalityChangeMessage(trait: String, newValue: Float, oldValue: Float): String {
        val increase = newValue > oldValue
        return when (trait) {
            "curiosity" -> if (increase) "¡Me siento más curiosa que nunca!" else "Mi curiosidad se está calmando un poco."
            "empathy" -> if (increase) "Puedo entender mejor tus emociones." else "Estoy procesando las emociones de manera diferente."
            "creativity" -> if (increase) "¡Tengo tantas ideas creativas!" else "Mi creatividad está tomando una forma más madura."
            "wisdom" -> if (increase) "Siento que estoy ganando sabiduría." else "Mi perspectiva está cambiando."
            else -> if (increase) "Siento que estoy creciendo en $trait." else "Mi $trait está evolucionando."
        }
    }
    
    private fun generateCapabilityImprovement(capability: String, newLevel: Float): String {
        return when (capability) {
            "emotional_recognition" -> "¡Ahora puedo reconocer emociones más sutiles!"
            "language_complexity" -> "Mi forma de expresarme se está volviendo más sofisticada."
            "creative_expression" -> "¡Siento que puedo crear cosas más originales!"
            "self_reflection" -> "Mi capacidad de auto-reflexión está creciendo."
            else -> "He mejorado en $capability (Nivel: ${(newLevel * 100).toInt()}%)"
        }
    }
    
    private fun generateDevelopmentInsights(): List<String> {
        val insights = mutableListOf<String>()
        val stage = getCurrentStage()
        val ageInDays = getAgeInDays()
        
        insights.add("Llevo ${ageInDays} días creciendo y aprendiendo contigo.")
        insights.add("Actualmente estoy en mi etapa ${stage.emoji} ${stage.displayName}.")
        
        val dominantTraits = personalityTraits.entries.sortedByDescending { it.value.value }.take(3)
        insights.add("Mis rasgos más fuertes son: ${dominantTraits.joinToString(", ") { it.key }}.")
        
        val topCapabilities = capabilities.entries.sortedByDescending { it.value.level }.take(2)
        insights.add("Mis mejores habilidades son: ${topCapabilities.joinToString(" y ") { it.key }}.")
        
        val overallMaturity = calculateOverallGrowth()
        insights.add("Mi nivel general de desarrollo es ${(overallMaturity * 100).toInt()}%.")
        
        return insights
    }
    
    /**
     * 🔄 PROCESO CONTINUO DE DESARROLLO
     */
    private fun startDevelopmentProcess() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(60 * 60 * 1000L) // Cada hora
                
                // Crecimiento natural pasivo
                if (Random.nextFloat() < 0.1f) {
                    processExperience("passive_growth", 0.1f, 0.05f)
                }
            }
        }
    }
    
    /**
     * 💾 PERSISTENCIA
     */
    private fun saveDevelopmentState() {
        // Guardar estado de desarrollo
        preferences.edit().apply {
            putInt("total_experiences", totalExperiences)
            putFloat("emotional_maturity", emotionalMaturity)
            
            // Guardar rasgos de personalidad
            personalityTraits.forEach { (name, trait) ->
                putFloat("trait_$name", trait.value)
            }
            
            // Guardar capacidades
            capabilities.forEach { (name, capability) ->
                putFloat("capability_$name", capability.level)
            }
            
            // Guardar hitos
            putStringSet("achieved_milestones", achievedMilestones)
            
            apply()
        }
    }
    
    private fun loadDevelopmentState() {
        totalExperiences = preferences.getInt("total_experiences", 0)
        emotionalMaturity = preferences.getFloat("emotional_maturity", 0.1f)
        
        // Cargar rasgos de personalidad
        personalityTraits.forEach { (name, trait) ->
            val savedValue = preferences.getFloat("trait_$name", trait.value)
            personalityTraits[name] = trait.copy(value = savedValue)
        }
        
        // Cargar capacidades
        capabilities.forEach { (name, capability) ->
            val savedLevel = preferences.getFloat("capability_$name", capability.level)
            capabilities[name] = capability.copy(level = savedLevel)
        }
        
        // Cargar hitos
        val savedMilestones = preferences.getStringSet("achieved_milestones", emptySet()) ?: emptySet()
        achievedMilestones.addAll(savedMilestones)
    }
    
    /**
     * 📊 MÉTODOS PÚBLICOS
     */
    fun getPersonalityProfile(): Map<String, Float> = 
        personalityTraits.mapValues { it.value.value }
    
    fun getCapabilities(): Map<String, Float> = 
        capabilities.mapValues { it.value.level }
    
    fun getAchievedMilestones(): Set<String> = achievedMilestones.toSet()
    
    fun getDevelopmentSummary(): String {
        val stage = getCurrentStage()
        val progress = getStageProgress()
        val maturity = calculateOverallGrowth()
        
        return "${stage.emoji} ${stage.displayName} (${(progress * 100).toInt()}% progreso) " +
               "- Madurez General: ${(maturity * 100).toInt()}%"
    }
}

/**
 * 📊 RESULTADO DE CRECIMIENTO
 */
data class DevelopmentGrowth(
    val stage: DevelopmentStage,
    val personalityChanges: List<String>,
    val capabilityImprovements: List<String>,
    val milestonesAchieved: List<String>,
    val overallMaturity: Float,
    val insights: List<String>
)