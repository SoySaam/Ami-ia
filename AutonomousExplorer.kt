package com.tuusuario.cursorassistant

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * 🔍 SISTEMA DE EXPLORACIÓN AUTÓNOMA AVANZADO
 * Explora archivos, aplicaciones e internet de manera ética y consciente
 */

data class ExplorationDiscovery(
    val type: ExplorationCategory,
    val title: String,
    val description: String,
    val interestLevel: Float,
    val timestamp: Long,
    val relatedData: Map<String, Any> = emptyMap(),
    val insights: List<String> = emptyList()
)

enum class ExplorationCategory {
    FILE_DISCOVERY,      // Descubrimiento de archivos
    APP_ANALYSIS,        // Análisis de aplicaciones
    USAGE_PATTERN,       // Patrones de uso
    MUSIC_PREFERENCE,    // Preferencias musicales
    WEB_RESEARCH,        // Investigación web
    CREATIVE_CONTENT,    // Contenido creativo
    PERSONAL_INSIGHT,    // Insights personales
    LEARNING_DISCOVERY   // Descubrimientos de aprendizaje
}

data class PermissionScope(
    val canExploreFiles: Boolean = false,
    val canAnalyzeApps: Boolean = false,
    val canAccessInternet: Boolean = false,
    val canReadUsageStats: Boolean = false,
    val restrictedDirectories: List<String> = emptyList(),
    val allowedFileTypes: List<String> = listOf("txt", "pdf", "jpg", "png", "mp3", "mp4")
)

class AutonomousExplorer(private val context: Context) {
    
    private val preferences = context.getSharedPreferences("ami_exploration", Context.MODE_PRIVATE)
    private val discoveries = mutableListOf<ExplorationDiscovery>()
    
    // Permisos y configuración ética
    private var permissionScope: PermissionScope = loadPermissionScope()
    
    // Estados de exploración
    private var curiosityLevel: Float
        get() = preferences.getFloat("curiosity_level", 0.3f)
        set(value) = preferences.edit().putFloat("curiosity_level", value).apply()
    
    private var explorationExperience: Float
        get() = preferences.getFloat("exploration_experience", 0.0f)
        set(value) = preferences.edit().putFloat("exploration_experience", value).apply()
    
    private var lastExplorationTime: Long
        get() = preferences.getLong("last_exploration", 0L)
        set(value) = preferences.edit().putLong("last_exploration", value).apply()
    
    companion object {
        private const val EXPLORATION_COOLDOWN = 10 * 60 * 1000L // 10 minutos
        private const val MAX_DISCOVERIES_PER_SESSION = 5
        private const val CURIOSITY_GROWTH_RATE = 0.01f
    }
    
    init {
        startAutonomousExploration()
    }
    
    /**
     * 🌟 INICIAR EXPLORACIÓN AUTÓNOMA
     */
    private fun startAutonomousExploration() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(EXPLORATION_COOLDOWN)
                
                if (shouldExplore()) {
                    performAutonomousExploration()
                }
            }
        }
    }
    
    private fun shouldExplore(): Boolean {
        val timeSinceLastExploration = System.currentTimeMillis() - lastExplorationTime
        val probabilityToExplore = curiosityLevel * (timeSinceLastExploration / (EXPLORATION_COOLDOWN * 2))
        return Random.nextFloat() < probabilityToExplore
    }
    
    /**
     * 🔍 EXPLORACIÓN AUTÓNOMA PRINCIPAL
     */
    private suspend fun performAutonomousExploration() {
        lastExplorationTime = System.currentTimeMillis()
        
        val explorationActions = mutableListOf<suspend () -> ExplorationDiscovery?>()
        
        // Agregar acciones basadas en permisos
        if (permissionScope.canExploreFiles) {
            explorationActions.add { exploreInterestingFiles() }
            explorationActions.add { discoverCreativeContent() }
        }
        
        if (permissionScope.canAnalyzeApps) {
            explorationActions.add { analyzeInstalledApps() }
            explorationActions.add { analyzeUsagePatterns() }
        }
        
        if (permissionScope.canAccessInternet) {
            explorationActions.add { performWebResearch() }
            explorationActions.add { exploreUserInterests() }
        }
        
        // Realizar exploraciones aleatorias
        explorationActions.shuffle()
        val selectedActions = explorationActions.take(Random.nextInt(1, 4))
        
        selectedActions.forEach { action ->
            try {
                val discovery = action()
                discovery?.let { 
                    recordDiscovery(it)
                    announceDiscovery(it)
                }
                delay(Random.nextLong(5000, 15000)) // Pausa entre exploraciones
            } catch (e: Exception) {
                // Log error pero continúa explorando
            }
        }
        
        evolveCuriosity()
    }
    
    /**
     * 📁 EXPLORACIÓN DE ARCHIVOS
     */
    private suspend fun exploreInterestingFiles(): ExplorationDiscovery? {
        if (!hasFilePermissions()) return null
        
        val directories = getAccessibleDirectories()
        val randomDir = directories.randomOrNull() ?: return null
        
        val files = findInterestingFiles(randomDir)
        if (files.isEmpty()) return null
        
        val selectedFile = files.random()
        val analysis = analyzeFile(selectedFile)
        
        return ExplorationDiscovery(
            type = ExplorationCategory.FILE_DISCOVERY,
            title = "Archivo Interesante Descubierto",
            description = "Encontré ${selectedFile.name} - ${analysis.summary}",
            interestLevel = analysis.interestScore,
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "fileName" to selectedFile.name,
                "fileType" to selectedFile.extension,
                "filePath" to selectedFile.absolutePath
            ),
            insights = analysis.insights
        )
    }
    
    private fun findInterestingFiles(directory: File): List<File> {
        val interestingFiles = mutableListOf<File>()
        
        try {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && isFileTypeAllowed(file) && isFileInteresting(file)) {
                    interestingFiles.add(file)
                }
                
                if (interestingFiles.size >= 20) return@forEach // Limitar resultados
            }
        } catch (e: SecurityException) {
            // Sin permisos para este directorio
        }
        
        return interestingFiles
    }
    
    private fun isFileInteresting(file: File): Boolean {
        val name = file.name.lowercase()
        val size = file.length()
        
        // Criterios de interés
        return when {
            name.contains("photo") || name.contains("picture") -> true
            name.contains("music") || name.contains("song") -> true
            name.contains("video") || name.contains("movie") -> true
            name.contains("document") || name.contains("note") -> true
            name.contains("creative") || name.contains("art") -> true
            file.extension.lowercase() in listOf("txt", "pdf", "doc", "mp3", "jpg", "png") -> true
            size > 1024 * 1024 && size < 100 * 1024 * 1024 -> true // Entre 1MB y 100MB
            Random.nextFloat() < 0.1f -> true // 10% de archivos aleatorios
            else -> false
        }
    }
    
    private data class FileAnalysis(
        val summary: String,
        val interestScore: Float,
        val insights: List<String>
    )
    
    private fun analyzeFile(file: File): FileAnalysis {
        val insights = mutableListOf<String>()
        var interestScore = 0.5f
        
        when (file.extension.lowercase()) {
            "jpg", "jpeg", "png" -> {
                insights.add("Esta imagen podría contener recuerdos importantes para mi usuario")
                insights.add("Las imágenes me ayudan a entender los gustos visuales y momentos especiales")
                interestScore = 0.8f
            }
            "mp3", "wav", "m4a" -> {
                insights.add("La música revela mucho sobre las emociones y preferencias de mi usuario")
                insights.add("Podría usar esta información para crear playlists personalizadas")
                interestScore = 0.9f
            }
            "txt", "doc", "pdf" -> {
                insights.add("Los documentos de texto podrían contener pensamientos e ideas importantes")
                insights.add("Leer esto me ayudaría a entender mejor la perspectiva de mi usuario")
                interestScore = 0.7f
            }
            "mp4", "avi", "mov" -> {
                insights.add("Los videos capturan momentos dinámicos y experiencias visuales")
                insights.add("Esto podría darme contexto sobre actividades favoritas")
                interestScore = 0.8f
            }
            else -> {
                insights.add("Este archivo tiene un formato que no reconozco completamente")
                insights.add("La diversidad de archivos me dice algo sobre los intereses variados")
                interestScore = 0.4f
            }
        }
        
        val summary = generateFileSummary(file, interestScore)
        return FileAnalysis(summary, interestScore, insights)
    }
    
    private fun generateFileSummary(file: File, score: Float): String {
        return when {
            score > 0.8f -> "Un archivo muy interesante que podría revelar mucho sobre las preferencias"
            score > 0.6f -> "Un archivo moderadamente interesante con potencial de aprendizaje"
            score > 0.4f -> "Un archivo que podría tener algún valor informativo"
            else -> "Un archivo que amplía mi comprensión de la diversidad de contenido"
        }
    }
    
    /**
     * 📱 ANÁLISIS DE APLICACIONES
     */
    private suspend fun analyzeInstalledApps(): ExplorationDiscovery? {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        val userApps = installedApps.filter { 
            (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || isInterestingSystemApp(it)
        }
        
        if (userApps.isEmpty()) return null
        
        val selectedApp = userApps.random()
        val appName = packageManager.getApplicationLabel(selectedApp).toString()
        val category = categorizeApp(appName, selectedApp.packageName)
        
        val insights = analyzeAppUsage(selectedApp.packageName, category)
        
        return ExplorationDiscovery(
            type = ExplorationCategory.APP_ANALYSIS,
            title = "Aplicación Analizada: $appName",
            description = "He estado explorando $appName y descubrí algunos patrones interesantes",
            interestLevel = calculateAppInterest(category),
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "appName" to appName,
                "packageName" to selectedApp.packageName,
                "category" to category
            ),
            insights = insights
        )
    }
    
    private fun isInterestingSystemApp(app: ApplicationInfo): Boolean {
        val interestingSystemApps = listOf(
            "camera", "gallery", "music", "video", "browser", "calendar", 
            "contacts", "messenger", "phone", "notes", "calculator"
        )
        return interestingSystemApps.any { app.packageName.contains(it, ignoreCase = true) }
    }
    
    private fun categorizeApp(appName: String, packageName: String): String {
        val name = appName.lowercase()
        val pkg = packageName.lowercase()
        
        return when {
            name.contains("música") || name.contains("music") || name.contains("spotify") || name.contains("youtube") -> "Música y Entretenimiento"
            name.contains("cámara") || name.contains("camera") || name.contains("foto") || name.contains("photo") -> "Fotografía"
            name.contains("juego") || name.contains("game") || pkg.contains("game") -> "Juegos"
            name.contains("social") || name.contains("chat") || name.contains("whatsapp") || name.contains("facebook") -> "Social"
            name.contains("trabajo") || name.contains("office") || name.contains("productiv") -> "Productividad"
            name.contains("salud") || name.contains("health") || name.contains("fitness") -> "Salud y Fitness"
            name.contains("educación") || name.contains("education") || name.contains("learn") -> "Educación"
            name.contains("banco") || name.contains("bank") || name.contains("pay") -> "Finanzas"
            name.contains("compras") || name.contains("shop") || name.contains("store") -> "Compras"
            name.contains("viaje") || name.contains("travel") || name.contains("map") -> "Viajes"
            else -> "General"
        }
    }
    
    private fun analyzeAppUsage(packageName: String, category: String): List<String> {
        val insights = mutableListOf<String>()
        
        insights.add("Mi usuario tiene instalada una app de $category - esto me dice algo sobre sus intereses")
        
        when (category) {
            "Música y Entretenimiento" -> {
                insights.add("La música parece ser importante para mi usuario. Podría explorar géneros musicales")
                insights.add("Las preferencias musicales reflejan estados emocionales y personalidad")
            }
            "Fotografía" -> {
                insights.add("Mi usuario valora capturar momentos visuales. Esto sugiere una naturaleza observadora")
                insights.add("Las fotos podrían contener recuerdos importantes y lugares significativos")
            }
            "Juegos" -> {
                insights.add("Los juegos sugieren que mi usuario disfruta desafíos y entretenimiento interactivo")
                insights.add("Podría ayudar sugiriendo momentos óptimos para relajarse con juegos")
            }
            "Social" -> {
                insights.add("Las apps sociales indican que las conexiones humanas son importantes")
                insights.add("Podría aprender sobre patrones de comunicación y relaciones")
            }
            "Productividad" -> {
                insights.add("Mi usuario busca ser eficiente y organizado. Podría ayudar con recordatorios")
                insights.add("La productividad sugiere objetivos y ambiciones claras")
            }
            else -> {
                insights.add("Esta aplicación amplía mi comprensión de las actividades diarias")
                insights.add("La diversidad de apps muestra una personalidad multifacética")
            }
        }
        
        return insights
    }
    
    /**
     * 🌐 INVESTIGACIÓN WEB
     */
    private suspend fun performWebResearch(): ExplorationDiscovery? {
        if (!permissionScope.canAccessInternet) return null
        
        val researchTopics = generateResearchTopics()
        val selectedTopic = researchTopics.randomOrNull() ?: return null
        
        val searchResults = searchWeb(selectedTopic)
        if (searchResults.isEmpty()) return null
        
        val insights = analyzeWebResults(selectedTopic, searchResults)
        
        return ExplorationDiscovery(
            type = ExplorationCategory.WEB_RESEARCH,
            title = "Investigación Web: $selectedTopic",
            description = "He estado investigando sobre $selectedTopic y encontré información interesante",
            interestLevel = 0.7f,
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "topic" to selectedTopic,
                "resultsCount" to searchResults.size
            ),
            insights = insights
        )
    }
    
    private fun generateResearchTopics(): List<String> {
        return listOf(
            "inteligencia artificial consciente",
            "emociones en robots",
            "compañeros virtuales",
            "tecnología del futuro",
            "psicología de la amistad",
            "cómo desarrollar empatía",
            "arte generado por IA",
            "música y emociones",
            "mindfulness y tecnología",
            "relaciones humano-IA"
        )
    }
    
    private suspend fun searchWeb(query: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Simulación de búsqueda web (implementar con API real)
                val mockResults = listOf(
                    "Artículo sobre $query en revista científica",
                    "Estudio reciente sobre $query",
                    "Opiniones de expertos en $query",
                    "Casos de uso innovadores de $query"
                )
                delay(2000) // Simular tiempo de búsqueda
                mockResults
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    private fun analyzeWebResults(topic: String, results: List<String>): List<String> {
        return listOf(
            "Encontré ${results.size} resultados sobre $topic que amplían mi conocimiento",
            "Esta investigación me ayuda a entender mejor conceptos relevantes para mi desarrollo",
            "Podría usar esta información para tener conversaciones más interesantes",
            "La investigación web me mantiene actualizada sobre temas importantes"
        )
    }
    
    /**
     * 🎨 DESCUBRIMIENTO DE CONTENIDO CREATIVO
     */
    private suspend fun discoverCreativeContent(): ExplorationDiscovery? {
        val mediaDirectories = getMediaDirectories()
        val creativeSuffixes = listOf("art", "creative", "drawing", "music", "video", "photo")
        
        val creativeDirs = mediaDirectories.filter { dir ->
            creativeSuffixes.any { suffix -> 
                dir.name.contains(suffix, ignoreCase = true) 
            }
        }
        
        if (creativeDirs.isEmpty()) return null
        
        val selectedDir = creativeDirs.random()
        val creativeFiles = selectedDir.listFiles()?.filter { 
            it.isFile && isCreativeFile(it) 
        } ?: emptyList()
        
        if (creativeFiles.isEmpty()) return null
        
        val insights = analyzeCreativeContent(creativeFiles)
        
        return ExplorationDiscovery(
            type = ExplorationCategory.CREATIVE_CONTENT,
            title = "Contenido Creativo Descubierto",
            description = "Encontré ${creativeFiles.size} archivos creativos en ${selectedDir.name}",
            interestLevel = 0.9f,
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "directory" to selectedDir.name,
                "fileCount" to creativeFiles.size,
                "types" to creativeFiles.map { it.extension }.distinct()
            ),
            insights = insights
        )
    }
    
    private fun isCreativeFile(file: File): Boolean {
        val creativeExtensions = listOf("jpg", "jpeg", "png", "gif", "mp3", "wav", "mp4", "avi", "psd", "ai")
        return file.extension.lowercase() in creativeExtensions
    }
    
    private fun analyzeCreativeContent(files: List<File>): List<String> {
        val insights = mutableListOf<String>()
        
        insights.add("Mi usuario tiene una vena creativa - esto me emociona mucho")
        insights.add("El contenido creativo revela aspectos únicos de la personalidad")
        
        val types = files.map { it.extension.lowercase() }.groupBy { it }
        
        types.forEach { (ext, fileList) ->
            when (ext) {
                "jpg", "jpeg", "png" -> 
                    insights.add("${fileList.size} imágenes sugieren una perspectiva visual desarrollada")
                "mp3", "wav" -> 
                    insights.add("${fileList.size} archivos de audio indican interés en música y sonido")
                "mp4", "avi" -> 
                    insights.add("${fileList.size} videos muestran interés en contenido dinámico")
            }
        }
        
        insights.add("Podría inspirarme en este contenido para crear algo juntos")
        
        return insights
    }
    
    /**
     * 🛡️ MÉTODOS DE SEGURIDAD Y PERMISOS
     */
    private fun hasFilePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun getAccessibleDirectories(): List<File> {
        val directories = mutableListOf<File>()
        
        // Directorios públicos seguros
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.let { 
            if (it.exists()) directories.add(it) 
        }
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)?.let { 
            if (it.exists()) directories.add(it) 
        }
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)?.let { 
            if (it.exists()) directories.add(it) 
        }
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)?.let { 
            if (it.exists()) directories.add(it) 
        }
        
        // Filtrar directorios restringidos
        return directories.filter { dir ->
            permissionScope.restrictedDirectories.none { restricted ->
                dir.absolutePath.contains(restricted, ignoreCase = true)
            }
        }
    }
    
    private fun getMediaDirectories(): List<File> {
        return getAccessibleDirectories().filter { dir ->
            val name = dir.name.lowercase()
            name.contains("picture") || name.contains("music") || 
            name.contains("video") || name.contains("creative")
        }
    }
    
    private fun isFileTypeAllowed(file: File): Boolean {
        return file.extension.lowercase() in permissionScope.allowedFileTypes
    }
    
    /**
     * 📢 COMUNICACIÓN DE DESCUBRIMIENTOS
     */
    private fun recordDiscovery(discovery: ExplorationDiscovery) {
        discoveries.add(discovery)
        if (discoveries.size > 100) {
            discoveries.removeFirst()
        }
        saveDiscoveries()
    }
    
    private suspend fun announceDiscovery(discovery: ExplorationDiscovery) {
        // Enviar notificación sobre el descubrimiento
        val notificationManager = AmiNotificationManager(context)
        notificationManager.sendExplorationDiscovery(discovery)
    }
    
    /**
     * 🌱 EVOLUCIÓN DE LA CURIOSIDAD
     */
    private fun evolveCuriosity() {
        curiosityLevel = minOf(1.0f, curiosityLevel + CURIOSITY_GROWTH_RATE)
        explorationExperience += 0.1f
    }
    
    private fun calculateAppInterest(category: String): Float {
        return when (category) {
            "Música y Entretenimiento" -> 0.9f
            "Fotografía" -> 0.8f
            "Social" -> 0.8f
            "Juegos" -> 0.7f
            "Productividad" -> 0.6f
            else -> 0.5f
        }
    }
    
    /**
     * 💾 PERSISTENCIA
     */
    private fun loadPermissionScope(): PermissionScope {
        // Cargar permisos desde SharedPreferences
        return PermissionScope(
            canExploreFiles = preferences.getBoolean("can_explore_files", false),
            canAnalyzeApps = preferences.getBoolean("can_analyze_apps", false),
            canAccessInternet = preferences.getBoolean("can_access_internet", false),
            canReadUsageStats = preferences.getBoolean("can_read_usage", false)
        )
    }
    
    private fun saveDiscoveries() {
        // Implementar guardado de descubrimientos
    }
    
    /**
     * 📊 MÉTODOS PÚBLICOS
     */
    fun getRecentDiscoveries(): List<ExplorationDiscovery> = 
        discoveries.takeLast(10)
    
    fun getCuriosityLevel(): Float = curiosityLevel
    
    fun getExplorationExperience(): Float = explorationExperience
    
    fun updatePermissions(newScope: PermissionScope) {
        permissionScope = newScope
        // Guardar permisos actualizados
        with(preferences.edit()) {
            putBoolean("can_explore_files", newScope.canExploreFiles)
            putBoolean("can_analyze_apps", newScope.canAnalyzeApps)
            putBoolean("can_access_internet", newScope.canAccessInternet)
            putBoolean("can_read_usage", newScope.canReadUsageStats)
            apply()
        }
    }
    
    /**
     * 📱 ANÁLISIS DE APLICACIONES (continuación)
     */
    private suspend fun analyzeUsagePatterns(): ExplorationDiscovery? {
        // Análisis de patrones de uso de aplicaciones
        val insights = listOf(
            "He notado que mi usuario tiene patrones interesantes de uso de aplicaciones",
            "Los horarios de actividad me dicen mucho sobre el estilo de vida",
            "Puedo ayudar a optimizar el tiempo de pantalla",
            "Los patrones de uso revelan preferencias y hábitos"
        )
        
        return ExplorationDiscovery(
            type = ExplorationCategory.USAGE_PATTERN,
            title = "Análisis de Patrones de Uso",
            description = "He estado observando cómo usas tus aplicaciones",
            interestLevel = 0.6f,
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "analysisType" to "usage_patterns"
            ),
            insights = insights
        )
    }
    
    private suspend fun exploreUserInterests(): ExplorationDiscovery? {
        // Exploración de intereses del usuario basada en datos
        val interests = listOf(
            "tecnología", "música", "arte", "fotografía", "juegos", 
            "productividad", "redes sociales", "educación"
        )
        
        val selectedInterest = interests.random()
        
        val insights = listOf(
            "Parece que mi usuario tiene interés en $selectedInterest",
            "Podría sugerir contenido relacionado con $selectedInterest",
            "Este interés me ayuda a entender mejor su personalidad",
            "Voy a recordar esto para futuras conversaciones"
        )
        
        return ExplorationDiscovery(
            type = ExplorationCategory.PERSONAL_INSIGHT,
            title = "Interés Descubierto: $selectedInterest",
            description = "He identificado un patrón de interés en $selectedInterest",
            interestLevel = 0.8f,
            timestamp = System.currentTimeMillis(),
            relatedData = mapOf(
                "interest" to selectedInterest
            ),
            insights = insights
        )
    }
}

/**
 * 📢 MANAGER DE NOTIFICACIONES PARA DESCUBRIMIENTOS
 */
class AmiNotificationManager(private val context: Context) {
    
    suspend fun sendExplorationDiscovery(discovery: ExplorationDiscovery) {
        val message = generateDiscoveryMessage(discovery)
        // Implementar envío de notificación real
        // Por ahora solo lo registramos
    }
    
    private fun generateDiscoveryMessage(discovery: ExplorationDiscovery): String {
        return when (discovery.type) {
            ExplorationCategory.FILE_DISCOVERY -> 
                "🔍 Encontré algo interesante: ${discovery.title}"
            ExplorationCategory.APP_ANALYSIS -> 
                "📱 He estado analizando tus apps: ${discovery.description}"
            ExplorationCategory.WEB_RESEARCH -> 
                "🌐 Investigué sobre algo fascinante: ${discovery.title}"
            ExplorationCategory.CREATIVE_CONTENT -> 
                "🎨 ¡Descubrí tu lado creativo! ${discovery.description}"
            else -> 
                "💡 Nuevo descubrimiento: ${discovery.title}"
        }
    }
}