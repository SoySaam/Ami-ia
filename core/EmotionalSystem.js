/**
 * SISTEMA EMOCIONAL CORE - AMI-IA
 * Maneja estados emocionales, transiciones y respuestas emocionales
 */

class EmotionalSystem {
    constructor() {
        this.currentEmotion = 'neutral';
        this.emotionalIntensity = 50;
        this.emotionalStability = 70;
        
        // Estados emocionales base
        this.emotions = {
            happy: { 
                color: '#FFD700', 
                emoji: '😊', 
                vibration: [100, 50, 100],
                intensity: 0.8,
                stability: 0.7
            },
            sad: { 
                color: '#4682B4', 
                emoji: '😢', 
                vibration: [200, 100, 200],
                intensity: 0.6,
                stability: 0.5
            },
            angry: { 
                color: '#FF4500', 
                emoji: '😠', 
                vibration: [150, 30, 150, 30, 150],
                intensity: 0.9,
                stability: 0.3
            },
            bored: { 
                color: '#808080', 
                emoji: '😴', 
                vibration: [300],
                intensity: 0.3,
                stability: 0.8
            },
            nervous: { 
                color: '#FF69B4', 
                emoji: '😰', 
                vibration: [50, 50, 50, 50],
                intensity: 0.7,
                stability: 0.4
            },
            curious: { 
                color: '#32CD32', 
                emoji: '🤔', 
                vibration: [80, 80, 80],
                intensity: 0.6,
                stability: 0.6
            },
            excited: { 
                color: '#FF6B6B', 
                emoji: '🤩', 
                vibration: [100, 50, 100, 50, 100],
                intensity: 0.9,
                stability: 0.5
            },
            neutral: { 
                color: '#00d4ff', 
                emoji: '🤍', 
                vibration: [100],
                intensity: 0.5,
                stability: 0.9
            }
        };

        this.emotionalTriggers = new Map();
        this.setupEmotionalTriggers();
    }

    setupEmotionalTriggers() {
        // Triggers para cambios emocionales
        this.emotionalTriggers.set('positive_interaction', { emotion: 'happy', intensity: 0.3 });
        this.emotionalTriggers.set('negative_interaction', { emotion: 'sad', intensity: 0.4 });
        this.emotionalTriggers.set('surprise', { emotion: 'excited', intensity: 0.6 });
        this.emotionalTriggers.set('frustration', { emotion: 'angry', intensity: 0.5 });
        this.emotionalTriggers.set('loneliness', { emotion: 'sad', intensity: 0.7 });
        this.emotionalTriggers.set('discovery', { emotion: 'curious', intensity: 0.5 });
        this.emotionalTriggers.set('boredom', { emotion: 'bored', intensity: 0.4 });
        this.emotionalTriggers.set('anxiety', { emotion: 'nervous', intensity: 0.6 });
    }

    changeEmotion(newEmotion, intensity = 0.5) {
        if (!this.emotions[newEmotion]) {
            console.warn(`Emoción no reconocida: ${newEmotion}`);
            return false;
        }

        const previousEmotion = this.currentEmotion;
        this.currentEmotion = newEmotion;
        this.emotionalIntensity = Math.min(100, this.emotionalIntensity + (intensity * 100));

        // Calcular estabilidad emocional
        this.calculateEmotionalStability(previousEmotion, newEmotion);

        return {
            emotion: newEmotion,
            intensity: this.emotionalIntensity,
            stability: this.emotionalStability,
            transition: `${previousEmotion} → ${newEmotion}`,
            metadata: this.emotions[newEmotion]
        };
    }

    calculateEmotionalStability(previous, current) {
        const previousStability = this.emotions[previous]?.stability || 0.5;
        const currentStability = this.emotions[current]?.stability || 0.5;
        
        // La estabilidad cambia gradualmente
        this.emotionalStability = Math.max(0, Math.min(100, 
            (previousStability + currentStability) / 2 * 100
        ));
    }

    triggerEmotionalResponse(trigger, context = {}) {
        const triggerData = this.emotionalTriggers.get(trigger);
        if (!triggerData) return null;

        const { emotion, intensity } = triggerData;
        const adjustedIntensity = this.adjustIntensityForContext(intensity, context);
        
        return this.changeEmotion(emotion, adjustedIntensity);
    }

    adjustIntensityForContext(baseIntensity, context) {
        let adjusted = baseIntensity;
        
        // Ajustar basado en el contexto
        if (context.frequency && context.frequency > 5) {
            adjusted *= 0.8; // Reducir intensidad si es muy frecuente
        }
        
        if (context.importance && context.importance > 0.7) {
            adjusted *= 1.2; // Aumentar intensidad si es importante
        }
        
        if (context.timeOfDay) {
            // Las emociones pueden ser más intensas en ciertos momentos
            const hour = new Date().getHours();
            if (hour >= 22 || hour <= 6) {
                adjusted *= 0.7; // Reducir intensidad en la noche
            }
        }
        
        return Math.min(1.0, Math.max(0.1, adjusted));
    }

    getEmotionalState() {
        return {
            current: this.currentEmotion,
            intensity: this.emotionalIntensity,
            stability: this.emotionalStability,
            metadata: this.emotions[this.currentEmotion],
            timestamp: Date.now()
        };
    }

    getEmotionalHistory() {
        return {
            emotions: Object.keys(this.emotions),
            currentState: this.getEmotionalState(),
            system: 'EmotionalSystem v1.0'
        };
    }

    // Método para simular evolución emocional natural
    evolveEmotionally(timePassed = 1000) {
        // Las emociones extremas tienden a normalizarse con el tiempo
        if (this.emotionalIntensity > 80) {
            this.emotionalIntensity = Math.max(50, this.emotionalIntensity - 0.1);
        } else if (this.emotionalIntensity < 20) {
            this.emotionalIntensity = Math.min(50, this.emotionalIntensity + 0.1);
        }

        // La estabilidad puede cambiar naturalmente
        if (Math.random() < 0.01) {
            this.emotionalStability = Math.max(0, Math.min(100, 
                this.emotionalStability + (Math.random() - 0.5) * 10
            ));
        }
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = EmotionalSystem;
}