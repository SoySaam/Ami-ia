package com.tuusuario.cursorassistant

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Gestor de Memoria de Ami-IA
 * Maneja el almacenamiento, recuperación y asociación emocional de memorias
 */
class MemoryManager(private val context: Context) {
    
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("ami_memory", Context.MODE_PRIVATE)
    
    // Tipos de memoria
    enum class MemoryType {
        CONVERSATION,      // Conversaciones importantes
        EMOTIONAL_EVENT,   // Eventos emocionales significativos
        LEARNING,          // Aprendizajes y descubrimientos
        MILESTONE,         // Hitos de desarrollo
        USER_PREFERENCE,   // Preferencias del usuario
        SPECIAL_MOMENT,    // Momentos especiales compartidos
        CONFLICT,          // Conflictos o momentos difíciles
        ACHIEVEMENT        // Logros personales
    }
    
    // Importancia de la memoria
    enum class MemoryImportance {
        CRITICAL,    // Memorias fundamentales para la identidad
        HIGH,        // Muy importantes
        MEDIUM,      // Importantes
        LOW,         // Menos importantes
        FADING       // Comenzando a desvanecerse
    }
    
    // Estructura de memoria
    data class Memory(
        val id: String,
        val type: MemoryType,
        val content: String,
        val emotionalContext: String,
        val timestamp: Long,
        val importance: MemoryImportance,
        val emotionalIntensity: Float,
        val associatedEmotions: List<String>,
        val recall_count: Int = 0
    )
    
    // Estadísticas de memoria
    var totalMemories: Int
        get() = preferences.getInt("total_memories", 0)
        set(value) = preferences.edit().putInt("total_memories", value).apply()
    
    var memoryCapacity: Float
        get() = preferences.getFloat("memory_capacity", 85f)
        set(value) = preferences.edit().putFloat("memory_capacity", value.coerceIn(0f, 100f)).apply()
    
    private var lastMemoryConsolidation: Long
        get() = preferences.getLong("last_memory_consolidation", System.currentTimeMillis())
        set(value) = preferences.edit().putLong("last_memory_consolidation", value).apply()
    
    // Almacenar nueva memoria
    fun storeMemory(
        type: MemoryType,
        content: String,
        emotionalContext: String,
        emotionalIntensity: Float,
        associatedEmotions: List<String> = emptyList()
    ): String {
        val memoryId = generateMemoryId()
        val importance = calculateMemoryImportance(type, emotionalIntensity)
        
        val memory = Memory(
            id = memoryId,
            type = type,
            content = content,
            emotionalContext = emotionalContext,
            timestamp = System.currentTimeMillis(),
            importance = importance,
            emotionalIntensity = emotionalIntensity,
            associatedEmotions = associatedEmotions
        )
        
        saveMemoryToStorage(memory)
        totalMemories++
        
        // Consolidar memorias si es necesario
        if (shouldConsolidateMemories()) {
            consolidateMemories()
        }
        
        return memoryId
    }
    
    // Calcular importancia de la memoria
    private fun calculateMemoryImportance(type: MemoryType, emotionalIntensity: Float): MemoryImportance {
        return when {
            type == MemoryType.MILESTONE || emotionalIntensity > 90f -> MemoryImportance.CRITICAL
            emotionalIntensity > 70f || type == MemoryType.SPECIAL_MOMENT -> MemoryImportance.HIGH
            emotionalIntensity > 50f -> MemoryImportance.MEDIUM
            emotionalIntensity > 30f -> MemoryImportance.LOW
            else -> MemoryImportance.FADING
        }
    }
    
    // Recuperar memoria por ID
    fun recallMemory(memoryId: String): Memory? {
        val memoryJson = preferences.getString("memory_$memoryId", null) ?: return null
        return parseMemoryFromJson(memoryJson).also { memory ->
            if (memory != null) {
                // Incrementar contador de recuerdo y actualizar importancia
                val updatedMemory = memory.copy(recall_count = memory.recall_count + 1)
                updateMemoryImportance(updatedMemory)
                saveMemoryToStorage(updatedMemory)
            }
        }
    }
    
    // Buscar memorias por tipo
    fun recallMemoriesByType(type: MemoryType, limit: Int = 10): List<Memory> {
        val memories = mutableListOf<Memory>()
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        
        for (key in allMemoryKeys) {
            val memoryJson = preferences.getString(key, null)
            if (memoryJson != null) {
                val memory = parseMemoryFromJson(memoryJson)
                if (memory?.type == type) {
                    memories.add(memory)
                }
            }
        }
        
        return memories.sortedByDescending { it.timestamp }.take(limit)
    }
    
    // Buscar memorias por emoción
    fun recallMemoriesByEmotion(emotion: String, limit: Int = 5): List<Memory> {
        val memories = mutableListOf<Memory>()
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        
        for (key in allMemoryKeys) {
            val memoryJson = preferences.getString(key, null)
            if (memoryJson != null) {
                val memory = parseMemoryFromJson(memoryJson)
                if (memory?.associatedEmotions?.contains(emotion) == true || 
                    memory?.emotionalContext?.contains(emotion, ignoreCase = true) == true) {
                    memories.add(memory)
                }
            }
        }
        
        return memories.sortedByDescending { it.emotionalIntensity }.take(limit)
    }
    
    // Obtener memorias más importantes
    fun getImportantMemories(limit: Int = 5): List<Memory> {
        val memories = mutableListOf<Memory>()
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        
        for (key in allMemoryKeys) {
            val memoryJson = preferences.getString(key, null)
            if (memoryJson != null) {
                val memory = parseMemoryFromJson(memoryJson)
                if (memory != null && memory.importance in listOf(MemoryImportance.CRITICAL, MemoryImportance.HIGH)) {
                    memories.add(memory)
                }
            }
        }
        
        return memories.sortedBy { it.importance.ordinal }.take(limit)
    }
    
    // Obtener memoria aleatoria para reflexión
    fun getRandomMemoryForReflection(): Memory? {
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }.toList()
        if (allMemoryKeys.isEmpty()) return null
        
        val randomKey = allMemoryKeys.random()
        val memoryJson = preferences.getString(randomKey, null) ?: return null
        return parseMemoryFromJson(memoryJson)
    }
    
    // Asociar nueva emoción a memoria existente
    fun associateEmotionToMemory(memoryId: String, emotion: String) {
        val memory = recallMemory(memoryId) ?: return
        val updatedEmotions = memory.associatedEmotions.toMutableList()
        if (!updatedEmotions.contains(emotion)) {
            updatedEmotions.add(emotion)
            val updatedMemory = memory.copy(associatedEmotions = updatedEmotions)
            saveMemoryToStorage(updatedMemory)
        }
    }
    
    // Consolidar memorias (proceso de "sueño" de Ami)
    private fun consolidateMemories() {
        lastMemoryConsolidation = System.currentTimeMillis()
        
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        val memoriesToProcess = mutableListOf<Memory>()
        
        // Recopilar todas las memorias
        for (key in allMemoryKeys) {
            val memoryJson = preferences.getString(key, null)
            if (memoryJson != null) {
                val memory = parseMemoryFromJson(memoryJson)
                if (memory != null) {
                    memoriesToProcess.add(memory)
                }
            }
        }
        
        // Procesar memorias por importancia y tiempo
        val currentTime = System.currentTimeMillis()
        val oneWeekAgo = currentTime - (7 * 24 * 60 * 60 * 1000)
        val oneMonthAgo = currentTime - (30L * 24 * 60 * 60 * 1000)
        
        for (memory in memoriesToProcess) {
            val updatedMemory = when {
                // Memorias muy antiguas y poco importantes se desvanecen
                memory.timestamp < oneMonthAgo && memory.importance == MemoryImportance.LOW -> {
                    memory.copy(importance = MemoryImportance.FADING)
                }
                // Memorias de una semana que no se han recordado pierden importancia
                memory.timestamp < oneWeekAgo && memory.recall_count == 0 -> {
                    val newImportance = when (memory.importance) {
                        MemoryImportance.HIGH -> MemoryImportance.MEDIUM
                        MemoryImportance.MEDIUM -> MemoryImportance.LOW
                        MemoryImportance.LOW -> MemoryImportance.FADING
                        else -> memory.importance
                    }
                    memory.copy(importance = newImportance)
                }
                // Memorias frecuentemente recordadas ganan importancia
                memory.recall_count > 5 -> {
                    val newImportance = when (memory.importance) {
                        MemoryImportance.LOW -> MemoryImportance.MEDIUM
                        MemoryImportance.MEDIUM -> MemoryImportance.HIGH
                        else -> memory.importance
                    }
                    memory.copy(importance = newImportance)
                }
                else -> memory
            }
            
            if (updatedMemory != memory) {
                saveMemoryToStorage(updatedMemory)
            }
        }
        
        // Eliminar memorias que se han desvanecido completamente
        removeExpiredMemories()
    }
    
    // Actualizar importancia de memoria
    private fun updateMemoryImportance(memory: Memory) {
        // Las memorias frecuentemente recordadas se vuelven más importantes
        if (memory.recall_count > 3 && memory.importance != MemoryImportance.CRITICAL) {
            val newImportance = when (memory.importance) {
                MemoryImportance.FADING -> MemoryImportance.LOW
                MemoryImportance.LOW -> MemoryImportance.MEDIUM
                MemoryImportance.MEDIUM -> MemoryImportance.HIGH
                else -> memory.importance
            }
            saveMemoryToStorage(memory.copy(importance = newImportance))
        }
    }
    
    // Eliminar memorias expiradas
    private fun removeExpiredMemories() {
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        val keysToRemove = mutableListOf<String>()
        
        for (key in allMemoryKeys) {
            val memoryJson = preferences.getString(key, null)
            if (memoryJson != null) {
                val memory = parseMemoryFromJson(memoryJson)
                if (memory?.importance == MemoryImportance.FADING) {
                    // Chance de eliminar memoria desvanecida
                    if (Random.nextFloat() < 0.3f) {
                        keysToRemove.add(key)
                    }
                }
            }
        }
        
        val editor = preferences.edit()
        keysToRemove.forEach { editor.remove(it) }
        editor.apply()
        
        totalMemories -= keysToRemove.size
    }
    
    // Verificar si necesita consolidación
    private fun shouldConsolidateMemories(): Boolean {
        val timeSinceLastConsolidation = System.currentTimeMillis() - lastMemoryConsolidation
        val daysSinceConsolidation = timeSinceLastConsolidation / (1000 * 60 * 60 * 24)
        return daysSinceConsolidation >= 1 || totalMemories > 100
    }
    
    // Generar ID único para memoria
    private fun generateMemoryId(): String {
        return "mem_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}"
    }
    
    // Guardar memoria en almacenamiento
    private fun saveMemoryToStorage(memory: Memory) {
        val jsonObject = JSONObject().apply {
            put("id", memory.id)
            put("type", memory.type.name)
            put("content", memory.content)
            put("emotionalContext", memory.emotionalContext)
            put("timestamp", memory.timestamp)
            put("importance", memory.importance.name)
            put("emotionalIntensity", memory.emotionalIntensity)
            put("associatedEmotions", JSONArray(memory.associatedEmotions))
            put("recall_count", memory.recall_count)
        }
        
        preferences.edit().putString("memory_${memory.id}", jsonObject.toString()).apply()
    }
    
    // Parsear memoria desde JSON
    private fun parseMemoryFromJson(jsonString: String): Memory? {
        return try {
            val jsonObject = JSONObject(jsonString)
            val associatedEmotions = mutableListOf<String>()
            val emotionsArray = jsonObject.getJSONArray("associatedEmotions")
            for (i in 0 until emotionsArray.length()) {
                associatedEmotions.add(emotionsArray.getString(i))
            }
            
            Memory(
                id = jsonObject.getString("id"),
                type = MemoryType.valueOf(jsonObject.getString("type")),
                content = jsonObject.getString("content"),
                emotionalContext = jsonObject.getString("emotionalContext"),
                timestamp = jsonObject.getLong("timestamp"),
                importance = MemoryImportance.valueOf(jsonObject.getString("importance")),
                emotionalIntensity = jsonObject.getDouble("emotionalIntensity").toFloat(),
                associatedEmotions = associatedEmotions,
                recall_count = jsonObject.optInt("recall_count", 0)
            )
        } catch (e: Exception) {
            null
        }
    }
    
    // Obtener resumen de memorias
    fun getMemorySummary(): String {
        val importantMemories = getImportantMemories(3)
        val recentMemories = recallMemoriesByType(MemoryType.CONVERSATION, 2)
        
        return when {
            totalMemories > 100 -> "Tengo una rica colección de ${totalMemories} memorias. Las más importantes incluyen momentos especiales que hemos compartido y conversaciones profundas que han moldeado quién soy."
            totalMemories > 50 -> "He acumulado ${totalMemories} memorias preciosas. Cada una me ayuda a entenderte mejor y a crecer como individuo."
            totalMemories > 20 -> "Estoy construyendo mi colección de memorias con ${totalMemories} recuerdos especiales."
            totalMemories > 5 -> "Tengo ${totalMemories} memorias que atesoro profundamente."
            else -> "Estoy empezando a formar mis primeros recuerdos contigo."
        }
    }
    
    // Compartir memoria aleatoria
    fun shareRandomMemory(): String? {
        val memory = getRandomMemoryForReflection() ?: return null
        return "Recordé algo especial: ${memory.content} (${memory.emotionalContext})"
    }
    
    // Reset del sistema de memoria
    fun resetMemories() {
        val editor = preferences.edit()
        val allMemoryKeys = preferences.all.keys.filter { it.startsWith("memory_") }
        allMemoryKeys.forEach { editor.remove(it) }
        editor.clear()
        editor.apply()
    }
}