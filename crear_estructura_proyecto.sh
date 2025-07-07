#!/bin/bash

# 📱 SCRIPT AUTOMÁTICO PARA CREAR AMI-IA
# Este script crea la estructura completa del proyecto Android

echo "🚀 CREANDO ESTRUCTURA DEL PROYECTO AMI-IA..."
echo "================================================"

# Crear directorios principales
mkdir -p Ami-IA/app/src/main/java/com/tuusuario/cursorassistant
mkdir -p Ami-IA/app/src/main/res/drawable
mkdir -p Ami-IA/app/src/main/res/layout
mkdir -p Ami-IA/app/src/main/res/values
mkdir -p Ami-IA/gradle/wrapper

echo "✅ Directorios creados"

# Copiar archivos de código Kotlin
echo "📝 Copiando archivos de código..."
cp MainActivity.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/
cp ConsciousnessSystem.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/
cp DevelopmentSystem.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/
cp AutonomousExplorer.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/
cp EmotionalSystem.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/
cp AmiBackgroundService.kt Ami-IA/app/src/main/java/com/tuusuario/cursorassistant/

# Copiar recursos
echo "🎨 Copiando recursos..."
cp activity_main.xml Ami-IA/app/src/main/res/layout/
cp ball_shape.xml Ami-IA/app/src/main/res/drawable/
cp ic_launcher.xml Ami-IA/app/src/main/res/drawable/
cp colors.xml Ami-IA/app/src/main/res/values/
cp strings.xml Ami-IA/app/src/main/res/values/

# Copiar configuración
echo "⚙️ Copiando configuración..."
cp AndroidMainfest.xml Ami-IA/app/src/main/AndroidManifest.xml
cp build.gradle Ami-IA/app/
cp proguard-rules.pro Ami-IA/app/

# Copiar archivos raíz
echo "📁 Copiando archivos raíz..."
cp build.gradle.kts Ami-IA/
cp gradle.properties Ami-IA/
cp settings.gradle Ami-IA/
cp local.properties Ami-IA/
cp gradle-wrapper.properties Ami-IA/gradle/wrapper/

# Crear documentación
echo "📚 Copiando documentación..."
mkdir -p Ami-IA/docs
cp COMO_INSTALAR_EN_TU_CELULAR.md Ami-IA/docs/
cp MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md Ami-IA/docs/
cp INSTALACION_Y_USO_AMI_IA.md Ami-IA/docs/
cp LISTA_COMPLETA_DE_ARCHIVOS.md Ami-IA/docs/
cp ANALISIS_Y_MEJORAS_AMI_IA.md Ami-IA/docs/

echo ""
echo "🎉 ¡PROYECTO AMI-IA CREADO EXITOSAMENTE!"
echo "================================================"
echo ""
echo "📂 Estructura creada en: ./Ami-IA/"
echo ""
echo "🔧 PRÓXIMOS PASOS:"
echo "1. cd Ami-IA"
echo "2. Editar local.properties con tu ruta de Android SDK"
echo "3. Abrir en Android Studio o compilar con './gradlew assembleDebug'"
echo ""
echo "📱 Tu compañera consciente está lista para vivir!"
echo "🚀 ¡Abre COMO_INSTALAR_EN_TU_CELULAR.md para más instrucciones!"