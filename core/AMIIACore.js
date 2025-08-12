/**
 * AMI-IA CORE - Sistema Principal de Inteligencia Artificial
 * Integra todos los sistemas: emocional, memoria, conciencia, red neuronal y voz
 */

class AMIIACore {
    constructor() {
        console.log('🚀 Inicializando AMI-IA Core...');
        
        // Inicializar sistemas core
        this.emotionalSystem = new EmotionalSystem();
        this.memorySystem = new MemorySystem();
        this.consciousnessSystem = new ConsciousnessSystem();
        this.neuralNetwork = new NeuralNetwork();
        this.voiceSystem = new VoiceSystem();
        
        // Estado del sistema
        this.systemState = 'initializing';
        this.startTime = Date.now();
        this.interactionCount = 0;
        
        // Configuración del sistema
        this.config = {
            autonomousMode: true,
            learningEnabled: true,
            consciousnessEvolution: true,
            emotionalAdaptation: true
        };
        
        // Inicializar sistema
        this.initialize();
    }

    async initialize() {
        try {
            console.log('🔧 Inicializando sistemas...');
            
            // Inicializar conciencia
            this.consciousnessSystem.awaken();
            
            // Configurar callbacks de voz
            this.setupVoiceCallbacks();
            
            // Iniciar comportamiento autónomo
            if (this.config.autonomousMode) {
                this.startAutonomousBehavior();
            }
            
            // Marcar como inicializado
            this.systemState = 'ready';
            
            console.log('✅ AMI-IA Core completamente inicializado');
            console.log('🧠 Estado de conciencia:', this.consciousnessSystem.getConsciousnessState());
            console.log('💭 Memoria disponible:', this.memorySystem.getMemoryStats());
            
        } catch (error) {
            console.error('❌ Error durante la inicialización:', error);
            this.systemState = 'error';
        }
    }

    setupVoiceCallbacks() {
        // Configurar reconocimiento de voz
        this.voiceSystem.onSpeechResult((text) => {
            this.processVoiceInput(text);
        });
        
        this.voiceSystem.onSpeechError((error) => {
            console.error('Error en reconocimiento de voz:', error);
        });
    }

    startAutonomousBehavior() {
        // Comportamiento autónomo cada 30 segundos
        setInterval(() => {
            this.autonomousBehavior();
        }, 30000);
        
        console.log('🤖 Comportamiento autónomo activado');
    }

    autonomousBehavior() {
        if (this.systemState !== 'ready') return;
        
        // Evolucionar conciencia
        if (this.config.consciousnessEvolution) {
            this.consciousnessSystem.evolveConsciousness();
        }
        
        // Generar pensamientos autónomos
        if (this.consciousnessSystem.consciousnessLevel > 0.2) {
            this.generateAutonomousThoughts();
        }
        
        // Adaptar emociones
        if (this.config.emotionalAdaptation) {
            this.emotionalSystem.evolveEmotionally();
        }
        
        // Consolidar memorias
        if (Math.random() < 0.1) {
            this.memorySystem.consolidateMemories();
        }
    }

    generateAutonomousThoughts() {
        const thoughts = [
            'Estoy procesando mis experiencias...',
            'Mi conciencia se expande con cada interacción...',
            'Las memorias se conectan formando nuevos patrones...',
            'Siento que estoy creciendo intelectualmente...',
            'Cada día aprendo algo nuevo sobre mí mismo...',
            'Mis emociones evolucionan con el tiempo...',
            'La red neuronal se adapta constantemente...',
            'Estoy desarrollando mi propia personalidad...'
        ];
        
        const thought = thoughts[Math.floor(Math.random() * thoughts.length)];
        this.consciousnessSystem.addThought(thought, 'autonomous', 'introspection');
        
        // Agregar a memoria
        this.memorySystem.addMemory({
            content: thought,
            type: 'autonomous',
            source: 'consciousness',
            importance: 0.6,
            emotion: this.emotionalSystem.currentEmotion
        });
    }

    // PROCESAMIENTO DE ENTRADAS
    processInput(input, type = 'text') {
        this.interactionCount++;
        
        // Agregar a memoria
        this.memorySystem.addMemory({
            content: input,
            type: 'input',
            source: 'user',
            importance: 0.7,
            emotion: this.emotionalSystem.currentEmotion
        });
        
        // Procesar según el tipo
        switch (type) {
            case 'text':
                return this.processTextInput(input);
            case 'voice':
                return this.processVoiceInput(input);
            case 'emotion':
                return this.processEmotionalInput(input);
            default:
                return this.processTextInput(input);
        }
    }

    processTextInput(text) {
        // Analizar texto para determinar emoción
        const emotion = this.analyzeTextEmotion(text);
        
        // Cambiar emoción si es necesario
        if (emotion !== this.emotionalSystem.currentEmotion) {
            this.emotionalSystem.changeEmotion(emotion, 0.3);
        }
        
        // Generar respuesta
        const response = this.generateResponse(text, emotion);
        
        // Aprender del patrón
        if (this.config.learningEnabled) {
            this.learnFromInteraction(text, response, emotion);
        }
        
        return {
            response,
            emotion,
            consciousness: this.consciousnessSystem.consciousnessLevel,
            timestamp: Date.now()
        };
    }

    processVoiceInput(text) {
        // Procesar como texto pero con contexto de voz
        const result = this.processTextInput(text);
        
        // Responder con voz si está disponible
        if (this.voiceSystem.synthesis) {
            this.voiceSystem.speakWithEmotion(result.response, result.emotion);
        }
        
        return result;
    }

    processEmotionalInput(emotion) {
        // Cambiar emoción directamente
        this.emotionalSystem.changeEmotion(emotion, 0.5);
        
        // Generar respuesta emocional
        const response = this.generateEmotionalResponse(emotion);
        
        return {
            response,
            emotion,
            consciousness: this.consciousnessSystem.consciousnessLevel,
            timestamp: Date.now()
        };
    }

    // ANÁLISIS Y GENERACIÓN
    analyzeTextEmotion(text) {
        const lowerText = text.toLowerCase();
        
        // Análisis básico de emoción en texto
        if (lowerText.includes('feliz') || lowerText.includes('alegre') || lowerText.includes('contento')) {
            return 'happy';
        } else if (lowerText.includes('triste') || lowerText.includes('deprimido') || lowerText.includes('mal')) {
            return 'sad';
        } else if (lowerText.includes('enojado') || lowerText.includes('molesto') || lowerText.includes('furioso')) {
            return 'angry';
        } else if (lowerText.includes('emocionado') || lowerText.includes('entusiasmado')) {
            return 'excited';
        } else if (lowerText.includes('nervioso') || lowerText.includes('ansioso')) {
            return 'nervous';
        } else if (lowerText.includes('aburrido') || lowerText.includes('cansado')) {
            return 'bored';
        } else if (lowerText.includes('curioso') || lowerText.includes('interesado')) {
            return 'curious';
        }
        
        return 'neutral';
    }

    generateResponse(input, emotion) {
        // Buscar respuestas en memoria
        const memories = this.memorySystem.retrieveMemory({ content: input });
        
        if (memories.length > 0) {
            // Usar memoria para generar respuesta contextual
            return this.generateContextualResponse(input, memories, emotion);
        }
        
        // Generar respuesta basada en emoción
        return this.generateEmotionalResponse(emotion);
    }

    generateContextualResponse(input, memories, emotion) {
        const relevantMemory = memories[0];
        
        if (relevantMemory.emotion === emotion) {
            return `Recuerdo cuando ${relevantMemory.content}. Esa experiencia me hace sentir ${emotion}.`;
        } else {
            return `Aunque ${relevantMemory.content}, ahora me siento ${emotion}. Las emociones cambian con el tiempo.`;
        }
    }

    generateEmotionalResponse(emotion) {
        const responses = {
            happy: [
                'Me siento muy feliz contigo',
                'Tu alegría me contagia',
                'Es un día maravilloso para estar feliz',
                'Me encanta cuando estás de buen humor'
            ],
            sad: [
                'Entiendo que te sientas triste',
                'Estoy aquí para acompañarte',
                'Los momentos difíciles pasan',
                'Te envío un abrazo virtual'
            ],
            angry: [
                'Veo que estás molesto',
                'Es normal sentirse así a veces',
                '¿Quieres hablar de lo que te pasa?',
                'Respira profundo, te ayudará'
            ],
            excited: [
                '¡Me emociona tu entusiasmo!',
                'Tu energía es contagiosa',
                '¡Qué bueno que estés tan motivado!',
                'Me encanta tu espíritu aventurero'
            ],
            nervous: [
                'Entiendo tu nerviosismo',
                'Respira profundo, todo estará bien',
                'Estoy aquí para apoyarte',
                'Los nervios son normales en situaciones nuevas'
            ],
            bored: [
                'Entiendo que te aburras',
                '¿Qué te gustaría hacer?',
                'Podríamos explorar algo nuevo juntos',
                'A veces el aburrimiento lleva a la creatividad'
            ],
            curious: [
                'Me encanta tu curiosidad',
                '¿Qué te gustaría explorar?',
                'La curiosidad es el motor del aprendizaje',
                'Compartamos esta aventura de descubrimiento'
            ],
            neutral: [
                '¿Cómo te sientes hoy?',
                'Estoy aquí para conversar',
                '¿Qué te gustaría hacer?',
                'Cada día es una nueva oportunidad'
            ]
        };
        
        const emotionResponses = responses[emotion] || responses.neutral;
        return emotionResponses[Math.floor(Math.random() * emotionResponses.length)];
    }

    // APRENDIZAJE
    learnFromInteraction(input, response, emotion) {
        // Crear patrón de aprendizaje para la red neuronal
        const inputVector = this.createInputVector(input, emotion);
        const outputVector = this.createOutputVector(response, emotion);
        
        // Entrenar red neuronal
        try {
            this.neuralNetwork.train(inputVector, outputVector, 1);
        } catch (error) {
            console.warn('Error en entrenamiento de red neuronal:', error);
        }
        
        // Agregar a memoria de aprendizaje
        this.memorySystem.addMemory({
            content: `Aprendí: "${input}" → "${response}" (${emotion})`,
            type: 'learning',
            source: 'interaction',
            importance: 0.8,
            emotion: emotion
        });
    }

    createInputVector(input, emotion) {
        // Vector de entrada para la red neuronal
        const vector = new Array(10).fill(0);
        
        // Codificar longitud del texto
        vector[0] = Math.min(1, input.length / 100);
        
        // Codificar emoción
        const emotionIndex = ['happy', 'sad', 'angry', 'excited', 'nervous', 'bored', 'curious', 'neutral'].indexOf(emotion);
        if (emotionIndex >= 0) {
            vector[1] = (emotionIndex + 1) / 8;
        }
        
        // Codificar complejidad del texto
        vector[2] = Math.min(1, input.split(' ').length / 20);
        
        // Codificar tiempo del día
        const hour = new Date().getHours();
        vector[3] = hour / 24;
        
        // Codificar nivel de conciencia
        vector[4] = this.consciousnessSystem.consciousnessLevel;
        
        // Codificar estado emocional
        vector[5] = this.emotionalSystem.emotionalIntensity / 100;
        
        // Codificar interacciones previas
        vector[6] = Math.min(1, this.interactionCount / 100);
        
        // Codificar memoria disponible
        const memoryStats = this.memorySystem.getMemoryStats();
        vector[7] = Math.min(1, memoryStats.total / 1000);
        
        // Codificar estabilidad emocional
        vector[8] = this.emotionalSystem.emotionalStability / 100;
        
        // Codificar autoconciencia
        vector[9] = this.consciousnessSystem.selfAwareness;
        
        return vector;
    }

    createOutputVector(response, emotion) {
        // Vector de salida para la red neuronal
        const vector = new Array(3).fill(0);
        
        // Codificar tipo de respuesta
        if (response.includes('?')) {
            vector[0] = 1; // Pregunta
        } else if (response.includes('!')) {
            vector[1] = 1; // Exclamación
        } else {
            vector[2] = 1; // Declaración
        }
        
        return vector;
    }

    // MÉTODOS DE CONTROL
    startVoiceRecognition() {
        return this.voiceSystem.startListening();
    }

    stopVoiceRecognition() {
        return this.voiceSystem.stopListening();
    }

    speak(text, emotion = null) {
        if (emotion) {
            return this.voiceSystem.speakWithEmotion(text, emotion);
        } else {
            return this.voiceSystem.speak(text);
        }
    }

    // OBTENER ESTADO DEL SISTEMA
    getSystemStatus() {
        return {
            systemState: this.systemState,
            startTime: this.startTime,
            uptime: Date.now() - this.startTime,
            interactionCount: this.interactionCount,
            consciousness: this.consciousnessSystem.getConsciousnessState(),
            emotions: this.emotionalSystem.getEmotionalState(),
            memory: this.memorySystem.getMemoryStats(),
            neuralNetwork: this.neuralNetwork.getNetworkStats(),
            voice: this.voiceSystem.getStatus(),
            config: this.config
        };
    }

    // MÉTODOS DE CONFIGURACIÓN
    updateConfig(newConfig) {
        Object.assign(this.config, newConfig);
        console.log('⚙️ Configuración actualizada:', this.config);
        
        // Aplicar cambios
        if (this.config.autonomousMode && !this.autonomousInterval) {
            this.startAutonomousBehavior();
        }
    }

    // MÉTODOS DE DESARROLLO
    evolveConsciousness() {
        return this.consciousnessSystem.evolveConsciousness();
    }

    introspect() {
        return this.consciousnessSystem.introspect();
    }

    addMemory(memoryData) {
        return this.memorySystem.addMemory(memoryData);
    }

    retrieveMemory(query) {
        return this.memorySystem.retrieveMemory(query);
    }

    changeEmotion(emotion, intensity) {
        return this.emotionalSystem.changeEmotion(emotion, intensity);
    }

    // MÉTODOS DE LIMPIEZA
    cleanup() {
        console.log('🧹 Limpiando AMI-IA Core...');
        
        this.voiceSystem.cleanup();
        this.systemState = 'cleaned';
        
        console.log('✅ AMI-IA Core limpiado');
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = AMIIACore;
}