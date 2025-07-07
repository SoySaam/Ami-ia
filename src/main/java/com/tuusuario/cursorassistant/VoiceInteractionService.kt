package com.tuusuario.cursorassistant

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import kotlin.coroutines.resume

class VoiceInteractionService : TextToSpeech.OnInitListener {
    
    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var isTtsReady = false
    
    private var recognitionCallback: ((String) -> Unit)? = null
    
    fun initialize(context: Context) {
        textToSpeech = TextToSpeech(context, this)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.let { tts ->
                val result = tts.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to English if Spanish not available
                    tts.setLanguage(Locale.ENGLISH)
                }
                
                // Set speech rate and pitch for a more natural voice
                tts.setSpeechRate(0.9f)
                tts.setPitch(1.1f)
                
                // Set up utterance listener for emotional feedback
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // Could trigger animation or visual feedback
                    }
                    
                    override fun onDone(utteranceId: String?) {
                        // Speech finished
                    }
                    
                    override fun onError(utteranceId: String?) {
                        // Handle TTS error
                    }
                })
                
                isTtsReady = true
            }
        }
    }
    
    suspend fun startListening(context: Context): String = withTimeoutOrNull(10000) {
        suspendCancellableCoroutine { continuation ->
            if (isListening) {
                continuation.resume("")
                return@suspendCancellableCoroutine
            }
            
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000)
            }
            
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                }
                
                override fun onBeginningOfSpeech() {
                    // User started speaking
                }
                
                override fun onRmsChanged(rmsdB: Float) {
                    // Audio level changed - could use for visual feedback
                }
                
                override fun onBufferReceived(buffer: ByteArray?) {
                    // Audio buffer received
                }
                
                override fun onEndOfSpeech() {
                    isListening = false
                }
                
                override fun onError(error: Int) {
                    isListening = false
                    val errorMessage = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                        SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permisos insuficientes"
                        SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera agotado"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No se encontró coincidencia"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                        SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Sin entrada de voz"
                        else -> "Error desconocido"
                    }
                    continuation.resume("")
                }
                
                override fun onResults(results: Bundle?) {
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val recognizedText = matches?.firstOrNull() ?: ""
                    continuation.resume(recognizedText)
                }
                
                override fun onPartialResults(partialResults: Bundle?) {
                    // Could show partial recognition results
                }
                
                override fun onEvent(eventType: Int, params: Bundle?) {
                    // Handle recognition events
                }
            })
            
            speechRecognizer?.startListening(intent)
            
            continuation.invokeOnCancellation {
                speechRecognizer?.cancel()
                isListening = false
            }
        }
    } ?: ""
    
    fun speak(text: String) {
        if (!isTtsReady) return
        
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ami_speech_${System.currentTimeMillis()}")
        }
        
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "ami_speech")
    }
    
    fun speakWithEmotion(text: String, emotion: String) {
        if (!isTtsReady) return
        
        // Adjust speech parameters based on emotion
        textToSpeech?.let { tts ->
            when (emotion.lowercase()) {
                "feliz", "alegría", "happy" -> {
                    tts.setSpeechRate(1.1f)
                    tts.setPitch(1.3f)
                }
                "triste", "sadness", "sad" -> {
                    tts.setSpeechRate(0.7f)
                    tts.setPitch(0.8f)
                }
                "enojado", "anger", "angry" -> {
                    tts.setSpeechRate(1.2f)
                    tts.setPitch(0.9f)
                }
                "nervioso", "nervous" -> {
                    tts.setSpeechRate(1.3f)
                    tts.setPitch(1.1f)
                }
                "relajado", "calm" -> {
                    tts.setSpeechRate(0.8f)
                    tts.setPitch(1.0f)
                }
                else -> {
                    tts.setSpeechRate(0.9f)
                    tts.setPitch(1.1f)
                }
            }
        }
        
        speak(text)
    }
    
    fun stopListening() {
        speechRecognizer?.cancel()
        isListening = false
    }
    
    fun stopSpeaking() {
        textToSpeech?.stop()
    }
    
    fun release() {
        speechRecognizer?.destroy()
        textToSpeech?.shutdown()
        isListening = false
        isTtsReady = false
    }
    
    fun isCurrentlyListening(): Boolean = isListening
    
    fun isTtsInitialized(): Boolean = isTtsReady
}