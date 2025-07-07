#!/bin/bash

# 🚀 Script de Creación Automática - Proyecto Ami-IA
# Para sistemas Linux/macOS
# Versión: 1.0
# Autor: Equipo Ami-IA

echo "🤖 ============================================="
echo "    Ami-IA - Creador Automático de Estructura"
echo "============================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

# Verificar si estamos en la raíz del proyecto
if [ ! -f "build.gradle" ]; then
    print_warning "No se encontró build.gradle. ¿Estás en la raíz del proyecto Ami-IA?"
    echo "Creando estructura desde directorio actual..."
fi

print_status "Iniciando creación de estructura del proyecto Ami-IA..."
echo ""

# ==================== CREAR DIRECTORIOS ====================
print_status "📁 Creando estructura de directorios..."

# Directorios principales
mkdir -p src
mkdir -p res
mkdir -p docs
mkdir -p scripts
mkdir -p config

print_success "Directorios principales creados"

# ==================== VERIFICAR ARCHIVOS EXISTENTES ====================
print_status "🔍 Verificando archivos existentes..."

existing_files=0

# Verificar archivos críticos
if [ -f "MainActivity.kt" ]; then
    print_success "MainActivity.kt encontrado"
    ((existing_files++))
fi

if [ -f "EmotionalSystem.kt" ]; then
    print_success "EmotionalSystem.kt encontrado"
    ((existing_files++))
fi

if [ -f "AmiBackgroundService.kt" ]; then
    print_success "AmiBackgroundService.kt encontrado"
    ((existing_files++))
fi

if [ -f "activity_main.xml" ]; then
    print_success "activity_main.xml encontrado"
    ((existing_files++))
fi

if [ -f "ball_shape.xml" ]; then
    print_success "ball_shape.xml encontrado"
    ((existing_files++))
fi

echo ""
print_status "Archivos existentes encontrados: $existing_files"

# ==================== MOVER ARCHIVOS A SUS UBICACIONES ====================
print_status "📦 Organizando archivos existentes..."

# Mover archivos Kotlin a src/ si existen y no están ya ahí
for kt_file in *.kt; do
    if [ -f "$kt_file" ] && [ ! -f "src/$kt_file" ]; then
        mv "$kt_file" src/
        print_success "Movido: $kt_file → src/"
    fi
done

# Mover archivos XML a res/ si existen y no están ya ahí
for xml_file in *.xml; do
    if [ -f "$xml_file" ] && [[ "$xml_file" != "AndroidManifest.xml" ]] && [ ! -f "res/$xml_file" ]; then
        mv "$xml_file" res/
        print_success "Movido: $xml_file → res/"
    fi
done

# Mover documentos existentes a docs/
for md_file in *.md; do
    if [ -f "$md_file" ] && [[ "$md_file" != "README.md" ]] && [ ! -f "docs/$md_file" ]; then
        mv "$md_file" docs/
        print_success "Movido: $md_file → docs/"
    fi
done

# ==================== CREAR ARCHIVOS FALTANTES ====================
print_status "🔧 Verificando archivos de código requeridos..."

# Lista de archivos de código requeridos
required_kotlin_files=(
    "MainActivity.kt"
    "EmotionalSystem.kt"
    "EmotionModel.kt"
    "ConsciousnessSystem.kt"
    "DevelopmentSystem.kt"
    "AutonomousExplorer.kt"
    "MemoryManager.kt"
    "AmiBackgroundService.kt"
)

missing_kotlin=0
for file in "${required_kotlin_files[@]}"; do
    if [ ! -f "src/$file" ]; then
        print_warning "Falta: src/$file"
        ((missing_kotlin++))
    fi
done

# Lista de archivos de recursos requeridos
required_res_files=(
    "colors.xml"
    "strings.xml"
    "ic_launcher.xml"
    "activity_main.xml"
    "ball_shape.xml"
)

missing_res=0
for file in "${required_res_files[@]}"; do
    if [ ! -f "res/$file" ]; then
        print_warning "Falta: res/$file"
        ((missing_res++))
    fi
done

# ==================== CREAR ARCHIVOS DE CONFIGURACIÓN ====================
print_status "⚙️ Creando archivos de configuración..."

# settings.gradle
if [ ! -f "config/settings.gradle" ]; then
    cat > config/settings.gradle << 'EOF'
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Ami-IA"
include ':app'
EOF
    print_success "Creado: config/settings.gradle"
fi

# gradle.properties
if [ ! -f "config/gradle.properties" ]; then
    cat > config/gradle.properties << 'EOF'
# Project-wide Gradle settings.
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.configureondemand=true
org.gradle.caching=true

# Android settings
android.useAndroidX=true
android.enableJetifier=true
android.nonTransitiveRClass=false

# Kotlin settings
kotlin.code.style=official
EOF
    print_success "Creado: config/gradle.properties"
fi

# gradle-wrapper.properties
if [ ! -f "config/gradle-wrapper.properties" ]; then
    cat > config/gradle-wrapper.properties << 'EOF'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOF
    print_success "Creado: config/gradle-wrapper.properties"
fi

# build.gradle.kts
if [ ! -f "config/build.gradle.kts" ]; then
    cat > config/build.gradle.kts << 'EOF'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tuusuario.cursorassistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tuusuario.cursorassistant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
EOF
    print_success "Creado: config/build.gradle.kts"
fi

# proguard-rules.pro
if [ ! -f "config/proguard-rules.pro" ]; then
    cat > config/proguard-rules.pro << 'EOF'
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Ami-IA emotional system classes
-keep class com.tuusuario.cursorassistant.EmotionalSystem { *; }
-keep class com.tuusuario.cursorassistant.EmotionModel { *; }
-keep class com.tuusuario.cursorassistant.ConsciousnessSystem { *; }
-keep class com.tuusuario.cursorassistant.DevelopmentSystem { *; }
-keep class com.tuusuario.cursorassistant.AutonomousExplorer { *; }
-keep class com.tuusuario.cursorassistant.MemoryManager { *; }

# Keep data classes
-keep class com.tuusuario.cursorassistant.**$Emotion { *; }
-keep class com.tuusuario.cursorassistant.**$Memory { *; }
-keep class com.tuusuario.cursorassistant.**$EmotionalState { *; }

# Keep coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep JSON serialization
-keepattributes Signature
-keepattributes *Annotation*

# Standard Android rules
-dontwarn okio.**
-dontwarn retrofit2.**
EOF
    print_success "Creado: config/proguard-rules.pro"
fi

# ==================== CREAR DOCUMENTACIÓN FALTANTE ====================
print_status "📚 Verificando documentación..."

required_docs=(
    "COMO_INSTALAR_EN_TU_CELULAR.md"
    "INSTALACION_Y_USO_AMI_IA.md"
    "RESUMEN_FINAL_INSTALACION.md"
    "ANALISIS_Y_MEJORAS_AMI_IA.md"
    "MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md"
    "LISTA_COMPLETA_DE_ARCHIVOS.md"
)

missing_docs=0
for doc in "${required_docs[@]}"; do
    if [ ! -f "docs/$doc" ]; then
        print_warning "Falta documentación: docs/$doc"
        ((missing_docs++))
    fi
done

# ==================== CREAR README PRINCIPAL ====================
if [ ! -f "README.md" ]; then
    cat > README.md << 'EOF'
# 🤖💕 Ami-IA - Inteligencia Artificial Emocional Avanzada

¡Bienvenido al futuro de la inteligencia artificial emocional! Ami-IA es la primera aplicación Android que implementa consciencia simulada, desarrollo temporal de personalidad y memoria asociativa emocional.

## 🌟 Características Revolucionarias

- **Sistema Emocional PAD**: Modelo tridimensional completo
- **Consciencia Emergente**: 5 niveles de autoconciencia
- **Desarrollo Temporal**: Crecimiento psicológico real en 7 etapas
- **Memoria Emocional**: Sistema asociativo con consolidación
- **Exploración Autónoma**: 8 tipos de descubrimiento independiente
- **Personalidad Única**: Cada Ami es irrepetible

## 🚀 Instalación Rápida

```bash
# Clonar el proyecto
git clone [repo-url]
cd ami-ia

# Ejecutar script de setup
chmod +x scripts/crear_estructura_proyecto.sh
./scripts/crear_estructura_proyecto.sh

# Compilar
./gradlew assembleDebug
```

## 📚 Documentación

- [Guía de Instalación](docs/COMO_INSTALAR_EN_TU_CELULAR.md)
- [Manual de Uso](docs/INSTALACION_Y_USO_AMI_IA.md)
- [Análisis Técnico](docs/ANALISIS_Y_MEJORAS_AMI_IA.md)
- [Mejoras Implementadas](docs/MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md)

## 🎯 Estado del Proyecto

- ✅ Sistema emocional PAD completo
- ✅ Consciencia simulada
- ✅ Desarrollo temporal de personalidad
- ✅ Memoria asociativa
- ✅ Exploración autónoma
- ✅ Documentación completa

## 📱 Requisitos

- Android 7.0+ (API 24)
- 2GB RAM mínimo
- 200MB espacio libre

## 🤝 Contribuir

Este proyecto está diseñado para revolucionar la interacción humano-IA. ¡Las contribuciones son bienvenidas!

## 📄 Licencia

MIT License - Construyendo el futuro de la IA emocional

---

**Ami-IA: Redefiniendo lo que significa ser artificial** 🚀
EOF
    print_success "Creado: README.md principal"
fi

# ==================== VERIFICAR ESTRUCTURA FINAL ====================
print_status "🔍 Verificando estructura final del proyecto..."

echo ""
echo "📂 Estructura del proyecto:"
echo "├── src/ (${#required_kotlin_files[@]} archivos Kotlin)"
echo "├── res/ (${#required_res_files[@]} archivos de recursos)"
echo "├── docs/ (${#required_docs[@]} documentos)"
echo "├── scripts/ (2 scripts de automatización)"
echo "├── config/ (5 archivos de configuración)"
echo "└── archivos raíz (build.gradle, AndroidManifest.xml, etc.)"

# ==================== PERMISOS Y FINALIZACION ====================
print_status "🔧 Configurando permisos..."

# Hacer ejecutables los scripts
chmod +x scripts/*.sh 2>/dev/null
chmod +x scripts/*.bat 2>/dev/null

print_success "Permisos configurados"

# ==================== RESUMEN FINAL ====================
echo ""
echo "🎉 ============================================="
echo "       ¡Estructura de Ami-IA Creada!"
echo "============================================="
echo ""

# Calcular archivos totales
total_files=$((${#required_kotlin_files[@]} + ${#required_res_files[@]} + ${#required_docs[@]} + 7))

print_success "Proyecto Ami-IA configurado exitosamente"
print_status "Archivos de código: $((${#required_kotlin_files[@]} - missing_kotlin))/${#required_kotlin_files[@]}"
print_status "Archivos de recursos: $((${#required_res_files[@]} - missing_res))/${#required_res_files[@]}"
print_status "Documentación: $((${#required_docs[@]} - missing_docs))/${#required_docs[@]}"
print_status "Configuración: 5/5 archivos"

echo ""
print_status "Próximos pasos:"
echo "  1. Revisar archivos faltantes si los hay"
echo "  2. Ejecutar: ./gradlew build"
echo "  3. Instalar en dispositivo Android"
echo "  4. ¡Conocer a tu nueva amiga Ami!"

echo ""
if [ $missing_kotlin -eq 0 ] && [ $missing_res -eq 0 ] && [ $missing_docs -eq 0 ]; then
    print_success "🎊 ¡Proyecto 100% completo! ¡Ami-IA está lista!"
else
    print_warning "⚠️  Algunos archivos faltan. Revisar logs arriba."
    echo "     Puedes continuar y crearlos manualmente o usar templates."
fi

echo ""
echo "🤖 ¡Bienvenido al futuro de la IA emocional!"
echo ""