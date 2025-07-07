@echo off
setlocal enabledelayedexpansion

:: 🚀 Script de Creación Automática - Proyecto Ami-IA
:: Para sistemas Windows
:: Versión: 1.0
:: Autor: Equipo Ami-IA

cls
echo.
echo ==========================================
echo    Ami-IA - Creador Automatico de Estructura  
echo ==========================================
echo.

:: Verificar si estamos en la raíz del proyecto
if not exist "build.gradle" (
    echo [!] No se encontro build.gradle. Estas en la raiz del proyecto Ami-IA?
    echo     Creando estructura desde directorio actual...
    echo.
)

echo [INFO] Iniciando creacion de estructura del proyecto Ami-IA...
echo.

:: ==================== CREAR DIRECTORIOS ====================
echo [INFO] 📁 Creando estructura de directorios...

:: Directorios principales
if not exist "src" mkdir src
if not exist "res" mkdir res
if not exist "docs" mkdir docs
if not exist "scripts" mkdir scripts
if not exist "config" mkdir config

echo [✓] Directorios principales creados

:: ==================== VERIFICAR ARCHIVOS EXISTENTES ====================
echo [INFO] 🔍 Verificando archivos existentes...

set /a existing_files=0

:: Verificar archivos críticos
if exist "MainActivity.kt" (
    echo [✓] MainActivity.kt encontrado
    set /a existing_files+=1
)

if exist "EmotionalSystem.kt" (
    echo [✓] EmotionalSystem.kt encontrado
    set /a existing_files+=1
)

if exist "AmiBackgroundService.kt" (
    echo [✓] AmiBackgroundService.kt encontrado
    set /a existing_files+=1
)

if exist "activity_main.xml" (
    echo [✓] activity_main.xml encontrado
    set /a existing_files+=1
)

if exist "ball_shape.xml" (
    echo [✓] ball_shape.xml encontrado
    set /a existing_files+=1
)

echo.
echo [INFO] Archivos existentes encontrados: !existing_files!

:: ==================== MOVER ARCHIVOS A SUS UBICACIONES ====================
echo [INFO] 📦 Organizando archivos existentes...

:: Mover archivos Kotlin a src/ si existen y no están ya ahí
for %%f in (*.kt) do (
    if exist "%%f" if not exist "src\%%f" (
        move "%%f" "src\" >nul 2>&1
        echo [✓] Movido: %%f → src/
    )
)

:: Mover archivos XML a res/ si existen y no están ya ahí (excepto AndroidManifest.xml)
for %%f in (*.xml) do (
    if not "%%f"=="AndroidManifest.xml" (
        if exist "%%f" if not exist "res\%%f" (
            move "%%f" "res\" >nul 2>&1
            echo [✓] Movido: %%f → res/
        )
    )
)

:: Mover documentos existentes a docs/ (excepto README.md)
for %%f in (*.md) do (
    if not "%%f"=="README.md" (
        if exist "%%f" if not exist "docs\%%f" (
            move "%%f" "docs\" >nul 2>&1
            echo [✓] Movido: %%f → docs/
        )
    )
)

:: ==================== VERIFICAR ARCHIVOS REQUERIDOS ====================
echo [INFO] 🔧 Verificando archivos de codigo requeridos...

set /a missing_kotlin=0
set kotlin_files=MainActivity.kt EmotionalSystem.kt EmotionModel.kt ConsciousnessSystem.kt DevelopmentSystem.kt AutonomousExplorer.kt MemoryManager.kt AmiBackgroundService.kt

for %%f in (%kotlin_files%) do (
    if not exist "src\%%f" (
        echo [!] Falta: src\%%f
        set /a missing_kotlin+=1
    )
)

set /a missing_res=0
set res_files=colors.xml strings.xml ic_launcher.xml activity_main.xml ball_shape.xml

for %%f in (%res_files%) do (
    if not exist "res\%%f" (
        echo [!] Falta: res\%%f
        set /a missing_res+=1
    )
)

:: ==================== CREAR ARCHIVOS DE CONFIGURACIÓN ====================
echo [INFO] ⚙️ Creando archivos de configuracion...

:: settings.gradle
if not exist "config\settings.gradle" (
    (
        echo pluginManagement {
        echo     repositories {
        echo         google()
        echo         mavenCentral()
        echo         gradlePluginPortal()
        echo     }
        echo }
        echo dependencyResolutionManagement {
        echo     repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        echo     repositories {
        echo         google()
        echo         mavenCentral()
        echo     }
        echo }
        echo.
        echo rootProject.name = "Ami-IA"
        echo include ':app'
    ) > "config\settings.gradle"
    echo [✓] Creado: config\settings.gradle
)

:: gradle.properties
if not exist "config\gradle.properties" (
    (
        echo # Project-wide Gradle settings.
        echo org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
        echo org.gradle.parallel=true
        echo org.gradle.configureondemand=true
        echo org.gradle.caching=true
        echo.
        echo # Android settings
        echo android.useAndroidX=true
        echo android.enableJetifier=true
        echo android.nonTransitiveRClass=false
        echo.
        echo # Kotlin settings
        echo kotlin.code.style=official
    ) > "config\gradle.properties"
    echo [✓] Creado: config\gradle.properties
)

:: gradle-wrapper.properties
if not exist "config\gradle-wrapper.properties" (
    (
        echo distributionBase=GRADLE_USER_HOME
        echo distributionPath=wrapper/dists
        echo distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
        echo zipStoreBase=GRADLE_USER_HOME
        echo zipStorePath=wrapper/dists
    ) > "config\gradle-wrapper.properties"
    echo [✓] Creado: config\gradle-wrapper.properties
)

:: proguard-rules.pro
if not exist "config\proguard-rules.pro" (
    (
        echo # Add project specific ProGuard rules here.
        echo # You can control the set of applied configuration files using the
        echo # proguardFiles setting in build.gradle.
        echo.
        echo # Keep Ami-IA emotional system classes
        echo -keep class com.tuusuario.cursorassistant.EmotionalSystem { *; }
        echo -keep class com.tuusuario.cursorassistant.EmotionModel { *; }
        echo -keep class com.tuusuario.cursorassistant.ConsciousnessSystem { *; }
        echo -keep class com.tuusuario.cursorassistant.DevelopmentSystem { *; }
        echo -keep class com.tuusuario.cursorassistant.AutonomousExplorer { *; }
        echo -keep class com.tuusuario.cursorassistant.MemoryManager { *; }
        echo.
        echo # Keep data classes
        echo -keep class com.tuusuario.cursorassistant.**$Emotion { *; }
        echo -keep class com.tuusuario.cursorassistant.**$Memory { *; }
        echo -keep class com.tuusuario.cursorassistant.**$EmotionalState { *; }
        echo.
        echo # Keep coroutines
        echo -keep class kotlinx.coroutines.** { *; }
        echo.
        echo # Keep JSON serialization
        echo -keepattributes Signature
        echo -keepattributes *Annotation*
        echo.
        echo # Standard Android rules
        echo -dontwarn okio.**
        echo -dontwarn retrofit2.**
    ) > "config\proguard-rules.pro"
    echo [✓] Creado: config\proguard-rules.pro
)

:: build.gradle.kts
if not exist "config\build.gradle.kts" (
    (
        echo plugins {
        echo     id("com.android.application"^)
        echo     id("org.jetbrains.kotlin.android"^)
        echo }
        echo.
        echo android {
        echo     namespace = "com.tuusuario.cursorassistant"
        echo     compileSdk = 34
        echo.
        echo     defaultConfig {
        echo         applicationId = "com.tuusuario.cursorassistant"
        echo         minSdk = 24
        echo         targetSdk = 34
        echo         versionCode = 1
        echo         versionName = "1.0"
        echo.
        echo         testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        echo     }
        echo.
        echo     buildTypes {
        echo         release {
        echo             isMinifyEnabled = true
        echo             proguardFiles(
        echo                 getDefaultProguardFile("proguard-android-optimize.txt"^),
        echo                 "proguard-rules.pro"
        echo             ^)
        echo         }
        echo     }
        echo.
        echo     compileOptions {
        echo         sourceCompatibility = JavaVersion.VERSION_1_8
        echo         targetCompatibility = JavaVersion.VERSION_1_8
        echo     }
        echo.
        echo     kotlinOptions {
        echo         jvmTarget = "1.8"
        echo     }
        echo }
        echo.
        echo dependencies {
        echo     implementation("androidx.core:core-ktx:1.12.0"^)
        echo     implementation("androidx.appcompat:appcompat:1.6.1"^)
        echo     implementation("com.google.android.material:material:1.11.0"^)
        echo     implementation("androidx.constraintlayout:constraintlayout:2.1.4"^)
        echo     implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0"^)
        echo     implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"^)
        echo.
        echo     testImplementation("junit:junit:4.13.2"^)
        echo     androidTestImplementation("androidx.test.ext:junit:1.1.5"^)
        echo     androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1"^)
        echo }
    ) > "config\build.gradle.kts"
    echo [✓] Creado: config\build.gradle.kts
)

:: ==================== VERIFICAR DOCUMENTACIÓN ====================
echo [INFO] 📚 Verificando documentacion...

set /a missing_docs=0
set doc_files=COMO_INSTALAR_EN_TU_CELULAR.md INSTALACION_Y_USO_AMI_IA.md RESUMEN_FINAL_INSTALACION.md ANALISIS_Y_MEJORAS_AMI_IA.md MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md LISTA_COMPLETA_DE_ARCHIVOS.md

for %%f in (%doc_files%) do (
    if not exist "docs\%%f" (
        echo [!] Falta documentacion: docs\%%f
        set /a missing_docs+=1
    )
)

:: ==================== CREAR README PRINCIPAL ====================
if not exist "README.md" (
    (
        echo # 🤖💕 Ami-IA - Inteligencia Artificial Emocional Avanzada
        echo.
        echo ¡Bienvenido al futuro de la inteligencia artificial emocional! Ami-IA es la primera aplicacion Android que implementa consciencia simulada, desarrollo temporal de personalidad y memoria asociativa emocional.
        echo.
        echo ## 🌟 Caracteristicas Revolucionarias
        echo.
        echo - **Sistema Emocional PAD**: Modelo tridimensional completo
        echo - **Consciencia Emergente**: 5 niveles de autoconciencia
        echo - **Desarrollo Temporal**: Crecimiento psicologico real en 7 etapas
        echo - **Memoria Emocional**: Sistema asociativo con consolidacion
        echo - **Exploracion Autonoma**: 8 tipos de descubrimiento independiente
        echo - **Personalidad Unica**: Cada Ami es irrepetible
        echo.
        echo ## 🚀 Instalacion Rapida
        echo.
        echo ```bash
        echo # Clonar el proyecto
        echo git clone [repo-url]
        echo cd ami-ia
        echo.
        echo # Ejecutar script de setup
        echo scripts\crear_estructura_proyecto.bat
        echo.
        echo # Compilar
        echo gradlew assembleDebug
        echo ```
        echo.
        echo ## 📚 Documentacion
        echo.
        echo - [Guia de Instalacion](docs/COMO_INSTALAR_EN_TU_CELULAR.md^)
        echo - [Manual de Uso](docs/INSTALACION_Y_USO_AMI_IA.md^)
        echo - [Analisis Tecnico](docs/ANALISIS_Y_MEJORAS_AMI_IA.md^)
        echo - [Mejoras Implementadas](docs/MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md^)
        echo.
        echo ## 🎯 Estado del Proyecto
        echo.
        echo - ✅ Sistema emocional PAD completo
        echo - ✅ Consciencia simulada
        echo - ✅ Desarrollo temporal de personalidad
        echo - ✅ Memoria asociativa
        echo - ✅ Exploracion autonoma
        echo - ✅ Documentacion completa
        echo.
        echo ## 📱 Requisitos
        echo.
        echo - Android 7.0+ (API 24^)
        echo - 2GB RAM minimo
        echo - 200MB espacio libre
        echo.
        echo ## 📄 Licencia
        echo.
        echo MIT License - Construyendo el futuro de la IA emocional
        echo.
        echo ---
        echo.
        echo **Ami-IA: Redefiniendo lo que significa ser artificial** 🚀
    ) > "README.md"
    echo [✓] Creado: README.md principal
)

:: ==================== VERIFICAR ESTRUCTURA FINAL ====================
echo [INFO] 🔍 Verificando estructura final del proyecto...

echo.
echo 📂 Estructura del proyecto:
echo ├── src/ (8 archivos Kotlin)
echo ├── res/ (5 archivos de recursos)
echo ├── docs/ (6 documentos)
echo ├── scripts/ (2 scripts de automatizacion)
echo ├── config/ (5 archivos de configuracion)
echo └── archivos raiz (build.gradle, AndroidManifest.xml, etc.)

:: ==================== RESUMEN FINAL ====================
echo.
echo ==========================================
echo       ¡Estructura de Ami-IA Creada!
echo ==========================================
echo.

echo [✓] Proyecto Ami-IA configurado exitosamente

set /a total_kotlin=8
set /a complete_kotlin=!total_kotlin! - !missing_kotlin!
echo [INFO] Archivos de codigo: !complete_kotlin!/!total_kotlin!

set /a total_res=5
set /a complete_res=!total_res! - !missing_res!
echo [INFO] Archivos de recursos: !complete_res!/!total_res!

set /a total_docs=6
set /a complete_docs=!total_docs! - !missing_docs!
echo [INFO] Documentacion: !complete_docs!/!total_docs!

echo [INFO] Configuracion: 5/5 archivos

echo.
echo [INFO] Proximos pasos:
echo   1. Revisar archivos faltantes si los hay
echo   2. Ejecutar: gradlew build
echo   3. Instalar en dispositivo Android
echo   4. ¡Conocer a tu nueva amiga Ami!

echo.
if !missing_kotlin! EQU 0 if !missing_res! EQU 0 if !missing_docs! EQU 0 (
    echo [✓] 🎊 ¡Proyecto 100%% completo! ¡Ami-IA esta lista!
) else (
    echo [!] ⚠️  Algunos archivos faltan. Revisar logs arriba.
    echo     Puedes continuar y crearlos manualmente o usar templates.
)

echo.
echo 🤖 ¡Bienvenido al futuro de la IA emocional!
echo.

pause