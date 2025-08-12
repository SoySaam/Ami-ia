/**
 * SISTEMA DE VOZ CORE - AMI-IA
 * Maneja síntesis de voz, reconocimiento y procesamiento de audio
 */

class VoiceSystem {
    constructor() {
        this.synthesis = null;
        this.recognition = null;
        this.voiceSettings = {
            pitch: 1.0,
            rate: 1.0,
            volume: 1.0,
            voice: null
        };
        
        this.audioContext = null;
        this.audioBuffer = null;
        this.isListening = false;
        this.callbacks = {
            onSpeechStart: null,
            onSpeechEnd: null,
            onSpeechResult: null,
            onSpeechError: null
        };
        
        this.initVoiceSystem();
    }

    initVoiceSystem() {
        console.log('🎤 Inicializando sistema de voz...');
        
        // Inicializar síntesis de voz
        this.initSpeechSynthesis();
        
        // Inicializar reconocimiento de voz
        this.initSpeechRecognition();
        
        // Inicializar contexto de audio
        this.initAudioContext();
        
        console.log('✅ Sistema de voz inicializado');
    }

    initSpeechSynthesis() {
        if ('speechSynthesis' in window) {
            this.synthesis = window.speechSynthesis;
            
            // Obtener voces disponibles
            this.loadVoices();
            
            // Configurar voz por defecto
            this.setDefaultVoice();
        } else {
            console.warn('⚠️ Síntesis de voz no disponible en este navegador');
        }
    }

    loadVoices() {
        if (this.synthesis) {
            // Cargar voces disponibles
            let voices = this.synthesis.getVoices();
            
            if (voices.length === 0) {
                // Esperar a que se carguen las voces
                this.synthesis.onvoiceschanged = () => {
                    voices = this.synthesis.getVoices();
                    this.setDefaultVoice();
                };
            } else {
                this.setDefaultVoice();
            }
        }
    }

    setDefaultVoice() {
        if (this.synthesis) {
            const voices = this.synthesis.getVoices();
            
            // Buscar voz en español o inglés
            const preferredVoice = voices.find(voice => 
                voice.lang.startsWith('es') || voice.lang.startsWith('en')
            ) || voices[0];
            
            if (preferredVoice) {
                this.voiceSettings.voice = preferredVoice;
                console.log(`🎭 Voz seleccionada: ${preferredVoice.name} (${preferredVoice.lang})`);
            }
        }
    }

    initSpeechRecognition() {
        if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
            const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
            this.recognition = new SpeechRecognition();
            
            // Configurar reconocimiento
            this.recognition.continuous = false;
            this.recognition.interimResults = false;
            this.recognition.lang = 'es-ES'; // Español por defecto
            
            // Configurar callbacks
            this.setupRecognitionCallbacks();
            
        } else {
            console.warn('⚠️ Reconocimiento de voz no disponible en este navegador');
        }
    }

    setupRecognitionCallbacks() {
        if (this.recognition) {
            this.recognition.onstart = () => {
                this.isListening = true;
                console.log('🎧 Escuchando...');
                if (this.callbacks.onSpeechStart) {
                    this.callbacks.onSpeechStart();
                }
            };
            
            this.recognition.onend = () => {
                this.isListening = false;
                console.log('🔇 Dejó de escuchar');
                if (this.callbacks.onSpeechEnd) {
                    this.callbacks.onSpeechEnd();
                }
            };
            
            this.recognition.onresult = (event) => {
                const result = event.results[0][0].transcript;
                console.log('💬 Reconocido:', result);
                
                if (this.callbacks.onSpeechResult) {
                    this.callbacks.onSpeechResult(result);
                }
            };
            
            this.recognition.onerror = (event) => {
                console.error('❌ Error en reconocimiento:', event.error);
                if (this.callbacks.onSpeechError) {
                    this.callbacks.onSpeechError(event.error);
                }
            };
        }
    }

    initAudioContext() {
        try {
            this.audioContext = new (window.AudioContext || window.webkitAudioContext)();
            console.log('🎵 Contexto de audio inicializado');
        } catch (error) {
            console.warn('⚠️ Contexto de audio no disponible:', error);
        }
    }

    // SÍNTESIS DE VOZ
    speak(text, options = {}) {
        if (!this.synthesis) {
            console.error('❌ Síntesis de voz no disponible');
            return false;
        }
        
        // Detener cualquier síntesis en curso
        this.synthesis.cancel();
        
        // Crear utterance
        const utterance = new SpeechSynthesisUtterance(text);
        
        // Aplicar configuración
        utterance.voice = options.voice || this.voiceSettings.voice;
        utterance.pitch = options.pitch || this.voiceSettings.pitch;
        utterance.rate = options.rate || this.voiceSettings.rate;
        utterance.volume = options.volume || this.voiceSettings.volume;
        
        // Configurar callbacks
        utterance.onstart = () => {
            console.log('🗣️ Comenzando a hablar:', text);
        };
        
        utterance.onend = () => {
            console.log('🔇 Terminó de hablar');
        };
        
        utterance.onerror = (event) => {
            console.error('❌ Error en síntesis:', event.error);
        };
        
        // Hablar
        this.synthesis.speak(utterance);
        
        return true;
    }

    speakWithEmotion(text, emotion = 'neutral') {
        const emotionSettings = {
            happy: { pitch: 1.2, rate: 1.1, volume: 1.0 },
            sad: { pitch: 0.8, rate: 0.9, volume: 0.8 },
            angry: { pitch: 1.3, rate: 1.2, volume: 1.2 },
            excited: { pitch: 1.4, rate: 1.3, volume: 1.1 },
            calm: { pitch: 0.9, rate: 0.8, volume: 0.9 },
            nervous: { pitch: 1.1, rate: 1.4, volume: 0.7 }
        };
        
        const settings = emotionSettings[emotion] || emotionSettings.neutral;
        return this.speak(text, settings);
    }

    stopSpeaking() {
        if (this.synthesis) {
            this.synthesis.cancel();
            console.log('🔇 Habla detenida');
        }
    }

    // RECONOCIMIENTO DE VOZ
    startListening(options = {}) {
        if (!this.recognition) {
            console.error('❌ Reconocimiento de voz no disponible');
            return false;
        }
        
        if (this.isListening) {
            console.warn('⚠️ Ya está escuchando');
            return false;
        }
        
        // Configurar opciones
        if (options.language) {
            this.recognition.lang = options.language;
        }
        
        if (options.continuous !== undefined) {
            this.recognition.continuous = options.continuous;
        }
        
        if (options.interimResults !== undefined) {
            this.recognition.interimResults = options.interimResults;
        }
        
        // Iniciar reconocimiento
        try {
            this.recognition.start();
            return true;
        } catch (error) {
            console.error('❌ Error al iniciar reconocimiento:', error);
            return false;
        }
    }

    stopListening() {
        if (this.recognition && this.isListening) {
            this.recognition.stop();
            this.isListening = false;
            console.log('🔇 Reconocimiento detenido');
        }
    }

    // PROCESAMIENTO DE AUDIO
    createAudioBuffer(audioData, sampleRate = 44100) {
        if (!this.audioContext) {
            console.error('❌ Contexto de audio no disponible');
            return null;
        }
        
        try {
            const buffer = this.audioContext.createBuffer(1, audioData.length, sampleRate);
            const channelData = buffer.getChannelData(0);
            
            for (let i = 0; i < audioData.length; i++) {
                channelData[i] = audioData[i];
            }
            
            this.audioBuffer = buffer;
            return buffer;
        } catch (error) {
            console.error('❌ Error al crear buffer de audio:', error);
            return null;
        }
    }

    playAudioBuffer(buffer = null) {
        if (!this.audioContext) {
            console.error('❌ Contexto de audio no disponible');
            return false;
        }
        
        const audioBuffer = buffer || this.audioBuffer;
        if (!audioBuffer) {
            console.error('❌ No hay buffer de audio para reproducir');
            return false;
        }
        
        try {
            const source = this.audioContext.createBufferSource();
            source.buffer = audioBuffer;
            source.connect(this.audioContext.destination);
            source.start(0);
            
            console.log('🎵 Reproduciendo audio');
            return true;
        } catch (error) {
            console.error('❌ Error al reproducir audio:', error);
            return false;
        }
    }

    // ANÁLISIS DE AUDIO
    analyzeAudio(audioData) {
        if (!this.audioContext) {
            return null;
        }
        
        const analysis = {
            duration: audioData.length / 44100, // Asumiendo 44.1kHz
            amplitude: this.calculateAmplitude(audioData),
            frequency: this.calculateFrequency(audioData),
            energy: this.calculateEnergy(audioData)
        };
        
        return analysis;
    }

    calculateAmplitude(audioData) {
        let max = 0;
        for (let i = 0; i < audioData.length; i++) {
            max = Math.max(max, Math.abs(audioData[i]));
        }
        return max;
    }

    calculateFrequency(audioData) {
        // Implementación básica de análisis de frecuencia
        // En una implementación real, usarías FFT
        let sum = 0;
        for (let i = 0; i < audioData.length; i++) {
            sum += Math.abs(audioData[i]);
        }
        return sum / audioData.length;
    }

    calculateEnergy(audioData) {
        let energy = 0;
        for (let i = 0; i < audioData.length; i++) {
            energy += audioData[i] * audioData[i];
        }
        return energy / audioData.length;
    }

    // CONFIGURACIÓN
    setVoiceSettings(settings) {
        Object.assign(this.voiceSettings, settings);
        console.log('⚙️ Configuración de voz actualizada:', this.voiceSettings);
    }

    getAvailableVoices() {
        if (this.synthesis) {
            return this.synthesis.getVoices();
        }
        return [];
    }

    setVoice(voiceName) {
        if (this.synthesis) {
            const voices = this.synthesis.getVoices();
            const voice = voices.find(v => v.name === voiceName);
            
            if (voice) {
                this.voiceSettings.voice = voice;
                console.log(`🎭 Voz cambiada a: ${voice.name}`);
                return true;
            } else {
                console.warn(`⚠️ Voz no encontrada: ${voiceName}`);
                return false;
            }
        }
        return false;
    }

    // CALLBACKS
    onSpeechStart(callback) {
        this.callbacks.onSpeechStart = callback;
    }

    onSpeechEnd(callback) {
        this.callbacks.onSpeechEnd = callback;
    }

    onSpeechResult(callback) {
        this.callbacks.onSpeechResult = callback;
    }

    onSpeechError(callback) {
        this.callbacks.onSpeechError = callback;
    }

    // UTILIDADES
    isSupported() {
        return {
            synthesis: 'speechSynthesis' in window,
            recognition: 'webkitSpeechRecognition' in window || 'SpeechRecognition' in window,
            audio: 'AudioContext' in window || 'webkitAudioContext' in window
        };
    }

    getStatus() {
        return {
            synthesis: !!this.synthesis,
            recognition: !!this.recognition,
            audioContext: !!this.audioContext,
            isListening: this.isListening,
            voiceSettings: this.voiceSettings,
            supported: this.isSupported()
        };
    }

    // MÉTODOS AVANZADOS
    createVoiceProfile(emotion, personality) {
        const profiles = {
            friendly: {
                pitch: 1.1,
                rate: 1.0,
                volume: 1.0
            },
            professional: {
                pitch: 1.0,
                rate: 0.9,
                volume: 0.9
            },
            energetic: {
                pitch: 1.3,
                rate: 1.2,
                volume: 1.1
            },
            calm: {
                pitch: 0.9,
                rate: 0.8,
                volume: 0.8
            }
        };
        
        const baseProfile = profiles[personality] || profiles.friendly;
        const emotionProfile = this.getEmotionProfile(emotion);
        
        return {
            ...baseProfile,
            ...emotionProfile
        };
    }

    getEmotionProfile(emotion) {
        const emotionProfiles = {
            happy: { pitch: 1.2, rate: 1.1 },
            sad: { pitch: 0.8, rate: 0.9 },
            angry: { pitch: 1.3, rate: 1.2 },
            excited: { pitch: 1.4, rate: 1.3 },
            calm: { pitch: 0.9, rate: 0.8 },
            nervous: { pitch: 1.1, rate: 1.4 }
        };
        
        return emotionProfiles[emotion] || {};
    }

    // Método para limpiar recursos
    cleanup() {
        if (this.recognition) {
            this.recognition.stop();
        }
        
        if (this.synthesis) {
            this.synthesis.cancel();
        }
        
        if (this.audioContext) {
            this.audioContext.close();
        }
        
        console.log('🧹 Sistema de voz limpiado');
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = VoiceSystem;
}