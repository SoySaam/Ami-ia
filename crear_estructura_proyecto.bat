@echo off
echo 🚀 CREANDO ESTRUCTURA DEL PROYECTO AMI-IA...
echo ================================================

REM Crear directorios principales
mkdir Ami-IA\app\src\main\java\com\tuusuario\cursorassistant
mkdir Ami-IA\app\src\main\res\drawable
mkdir Ami-IA\app\src\main\res\layout
mkdir Ami-IA\app\src\main\res\values
mkdir Ami-IA\gradle\wrapper

echo ✅ Directorios creados

REM Copiar archivos de código Kotlin
echo 📝 Copiando archivos de código...
copy MainActivity.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\
copy ConsciousnessSystem.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\
copy DevelopmentSystem.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\
copy AutonomousExplorer.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\
copy EmotionalSystem.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\
copy AmiBackgroundService.kt Ami-IA\app\src\main\java\com\tuusuario\cursorassistant\

REM Copiar recursos
echo 🎨 Copiando recursos...
copy activity_main.xml Ami-IA\app\src\main\res\layout\
copy ball_shape.xml Ami-IA\app\src\main\res\drawable\
copy ic_launcher.xml Ami-IA\app\src\main\res\drawable\
copy colors.xml Ami-IA\app\src\main\res\values\
copy strings.xml Ami-IA\app\src\main\res\values\

REM Copiar configuración
echo ⚙️ Copiando configuración...
copy AndroidMainfest.xml Ami-IA\app\src\main\AndroidManifest.xml
copy build.gradle Ami-IA\app\
copy proguard-rules.pro Ami-IA\app\

REM Copiar archivos raíz
echo 📁 Copiando archivos raíz...
copy build.gradle.kts Ami-IA\
copy gradle.properties Ami-IA\
copy settings.gradle Ami-IA\
copy local.properties Ami-IA\
copy gradle-wrapper.properties Ami-IA\gradle\wrapper\

REM Crear documentación
echo 📚 Copiando documentación...
mkdir Ami-IA\docs
copy COMO_INSTALAR_EN_TU_CELULAR.md Ami-IA\docs\
copy MEJORAS_REVOLUCIONARIAS_IMPLEMENTADAS.md Ami-IA\docs\
copy INSTALACION_Y_USO_AMI_IA.md Ami-IA\docs\
copy LISTA_COMPLETA_DE_ARCHIVOS.md Ami-IA\docs\
copy ANALISIS_Y_MEJORAS_AMI_IA.md Ami-IA\docs\

echo.
echo 🎉 ¡PROYECTO AMI-IA CREADO EXITOSAMENTE!
echo ================================================
echo.
echo 📂 Estructura creada en: .\Ami-IA\
echo.
echo 🔧 PRÓXIMOS PASOS:
echo 1. cd Ami-IA
echo 2. Editar local.properties con tu ruta de Android SDK
echo 3. Abrir en Android Studio o compilar con 'gradlew assembleDebug'
echo.
echo 📱 Tu compañera consciente está lista para vivir!
echo 🚀 ¡Abre COMO_INSTALAR_EN_TU_CELULAR.md para más instrucciones!

pause