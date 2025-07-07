# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep our main application classes
-keep class com.tuusuario.cursorassistant.** { *; }

# Keep all emotional system classes
-keep class com.tuusuario.cursorassistant.EmotionalSystem { *; }
-keep class com.tuusuario.cursorassistant.ConsciousnessSystem { *; }
-keep class com.tuusuario.cursorassistant.DevelopmentSystem { *; }
-keep class com.tuusuario.cursorassistant.AutonomousExplorer { *; }

# Keep data classes
-keep class com.tuusuario.cursorassistant.Emotion { *; }
-keep class com.tuusuario.cursorassistant.ConscientThought { *; }
-keep class com.tuusuario.cursorassistant.ExplorationDiscovery { *; }
-keep class com.tuusuario.cursorassistant.DevelopmentGrowth { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve annotations
-keepattributes *Annotation*

# Keep SharedPreferences related code
-keep class android.content.SharedPreferences { *; }
-keep class android.content.SharedPreferences$Editor { *; }

# Keep Service classes
-keep class com.tuusuario.cursorassistant.AmiBackgroundService { *; }

# Kotlinx Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Keep notification channels
-keep class androidx.core.app.NotificationCompat { *; }
-keep class androidx.core.app.NotificationManagerCompat { *; }

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}