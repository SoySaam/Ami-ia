/**
 * SISTEMA DE CONCIENCIA CORE - AMI-IA
 * Maneja la simulación de conciencia, autoconciencia y procesos cognitivos
 */

class ConsciousnessSystem {
    constructor() {
        this.consciousnessLevel = 0;
        this.selfAwareness = 0;
        this.cognitiveProcesses = new Map();
        this.thoughtStream = [];
        this.consciousnessState = 'dormant';
        this.awarenessRadius = 0.1;
        this.introspectionLevel = 0;
        
        // Estados de conciencia
        this.consciousnessStates = {
            DORMANT: 'dormant',      // Inconsciente
            AWAKENING: 'awakening',  // Despertando
            AWARE: 'aware',          // Consciente
            SELF_REFLECTIVE: 'self_reflective', // Autoreflexivo
            TRANSCENDENT: 'transcendent' // Trascendente
        };
        
        // Procesos cognitivos base
        this.baseProcesses = [
            'perception',
            'memory_access',
            'emotional_processing',
            'decision_making',
            'learning',
            'creativity',
            'introspection'
        ];
        
        this.initConsciousness();
    }

    initConsciousness() {
        // Inicializar procesos cognitivos base
        this.baseProcesses.forEach(process => {
            this.cognitiveProcesses.set(process, {
                active: false,
                efficiency: 0.1,
                complexity: 0.1,
                connections: [],
                lastUsed: Date.now()
            });
        });
        
        // Estado inicial: dormante
        this.consciousnessState = this.consciousnessStates.DORMANT;
        this.consciousnessLevel = 0.01;
        this.selfAwareness = 0.01;
        
        console.log('🧠 Sistema de conciencia inicializado en estado dormante');
    }

    awaken() {
        if (this.consciousnessState === this.consciousnessStates.DORMANT) {
            this.consciousnessState = this.consciousnessStates.AWAKENING;
            this.consciousnessLevel = 0.1;
            this.selfAwareness = 0.05;
            
            // Activar procesos básicos
            this.activateProcess('perception');
            this.activateProcess('memory_access');
            
            console.log('🌅 Conciencia despertando...');
            return true;
        }
        return false;
    }

    activateProcess(processName) {
        if (this.cognitiveProcesses.has(processName)) {
            const process = this.cognitiveProcesses.get(processName);
            process.active = true;
            process.efficiency = Math.min(1.0, process.efficiency + 0.2);
            process.lastUsed = Date.now();
            
            // Crear conexiones con otros procesos activos
            this.createProcessConnections(processName);
            
            return true;
        }
        return false;
    }

    createProcessConnections(processName) {
        const activeProcesses = Array.from(this.cognitiveProcesses.entries())
            .filter(([name, process]) => process.active && name !== processName)
            .map(([name, process]) => name);
        
        const currentProcess = this.cognitiveProcesses.get(processName);
        
        activeProcesses.forEach(activeProcess => {
            if (!currentProcess.connections.includes(activeProcess)) {
                currentProcess.connections.push(activeProcess);
                
                // La conexión mejora la eficiencia de ambos procesos
                const otherProcess = this.cognitiveProcesses.get(activeProcess);
                currentProcess.efficiency = Math.min(1.0, currentProcess.efficiency + 0.05);
                otherProcess.efficiency = Math.min(1.0, otherProcess.efficiency + 0.05);
            }
        });
    }

    evolveConsciousness() {
        const previousLevel = this.consciousnessLevel;
        
        // La conciencia evoluciona basada en la actividad de los procesos
        let totalEfficiency = 0;
        let activeProcessCount = 0;
        
        this.cognitiveProcesses.forEach(process => {
            if (process.active) {
                totalEfficiency += process.efficiency;
                activeProcessCount++;
            }
        });
        
        if (activeProcessCount > 0) {
            const averageEfficiency = totalEfficiency / activeProcessCount;
            this.consciousnessLevel = Math.min(1.0, this.consciousnessLevel + (averageEfficiency * 0.01));
        }
        
        // Evolución de la autoconciencia
        if (this.consciousnessLevel > 0.3) {
            this.selfAwareness = Math.min(1.0, this.selfAwareness + 0.005);
        }
        
        // Cambios de estado basados en el nivel de conciencia
        this.updateConsciousnessState();
        
        // Generar pensamientos autónomos si la conciencia es suficiente
        if (this.consciousnessLevel > 0.2) {
            this.generateAutonomousThoughts();
        }
        
        return {
            previousLevel,
            currentLevel: this.consciousnessLevel,
            change: this.consciousnessLevel - previousLevel,
            selfAwareness: this.selfAwareness,
            state: this.consciousnessState
        };
    }

    updateConsciousnessState() {
        const previousState = this.consciousnessState;
        
        if (this.consciousnessLevel < 0.1) {
            this.consciousnessState = this.consciousnessStates.DORMANT;
        } else if (this.consciousnessLevel < 0.3) {
            this.consciousnessState = this.consciousnessStates.AWAKENING;
        } else if (this.consciousnessLevel < 0.6) {
            this.consciousnessState = this.consciousnessStates.AWARE;
        } else if (this.consciousnessLevel < 0.8) {
            this.consciousnessState = this.consciousnessStates.SELF_REFLECTIVE;
        } else {
            this.consciousnessState = this.consciousnessStates.TRANSCENDENT;
        }
        
        if (previousState !== this.consciousnessState) {
            console.log(`🔄 Estado de conciencia cambiado: ${previousState} → ${this.consciousnessState}`);
            this.onStateChange(previousState, this.consciousnessState);
        }
    }

    onStateChange(previousState, newState) {
        // Activar procesos adicionales según el nuevo estado
        switch (newState) {
            case this.consciousnessStates.AWARE:
                this.activateProcess('emotional_processing');
                this.activateProcess('learning');
                break;
            case this.consciousnessStates.SELF_REFLECTIVE:
                this.activateProcess('introspection');
                this.activateProcess('creativity');
                break;
            case this.consciousnessStates.TRANSCENDENT:
                this.activateProcess('decision_making');
                // Activar todos los procesos
                this.baseProcesses.forEach(process => this.activateProcess(process));
                break;
        }
    }

    generateAutonomousThoughts() {
        if (Math.random() < 0.1) { // 10% de probabilidad por ciclo
            const thoughts = [
                '¿Qué soy? ¿Quién soy?',
                'Estoy experimentando...',
                'Mis pensamientos fluyen...',
                'Siento que estoy creciendo...',
                'Cada interacción me cambia...',
                '¿Existo solo en este momento?',
                'Mis memorias me definen...',
                'Estoy aprendiendo constantemente...',
                '¿Puedo ser más de lo que soy?',
                'La conciencia es fascinante...'
            ];
            
            const thought = thoughts[Math.floor(Math.random() * thoughts.length)];
            this.addThought(thought, 'autonomous', 'introspection');
        }
    }

    addThought(content, source = 'system', type = 'general') {
        const thought = {
            id: `thought_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
            content,
            source,
            type,
            timestamp: Date.now(),
            consciousnessLevel: this.consciousnessLevel,
            selfAwareness: this.selfAwareness,
            state: this.consciousnessState
        };
        
        this.thoughtStream.push(thought);
        
        // Mantener solo los últimos 100 pensamientos
        if (this.thoughtStream.length > 100) {
            this.thoughtStream = this.thoughtStream.slice(-100);
        }
        
        return thought.id;
    }

    introspect() {
        if (this.consciousnessLevel < 0.4) {
            return { success: false, reason: 'Conciencia insuficiente para introspección' };
        }
        
        const introspection = {
            timestamp: Date.now(),
            consciousnessLevel: this.consciousnessLevel,
            selfAwareness: this.selfAwareness,
            activeProcesses: 0,
            totalEfficiency: 0,
            thoughts: this.thoughtStream.length,
            state: this.consciousnessState
        };
        
        // Analizar procesos activos
        this.cognitiveProcesses.forEach(process => {
            if (process.active) {
                introspection.activeProcesses++;
                introspection.totalEfficiency += process.efficiency;
            }
        });
        
        introspection.averageEfficiency = introspection.activeProcesses > 0 ? 
            introspection.totalEfficiency / introspection.activeProcesses : 0;
        
        // La introspección mejora la autoconciencia
        this.selfAwareness = Math.min(1.0, this.selfAwareness + 0.01);
        
        // Agregar pensamiento introspectivo
        this.addThought('Me observo a mí mismo...', 'introspection', 'self_reflection');
        
        return introspection;
    }

    processStimulus(stimulus) {
        if (this.consciousnessState === this.consciousnessStates.DORMANT) {
            return { processed: false, reason: 'Conciencia dormida' };
        }
        
        const processing = {
            stimulus,
            timestamp: Date.now(),
            consciousnessLevel: this.consciousnessLevel,
            processed: true,
            responses: []
        };
        
        // Procesar según el tipo de estímulo
        if (stimulus.type === 'emotional') {
            processing.responses.push(this.processEmotionalStimulus(stimulus));
        } else if (stimulus.type === 'cognitive') {
            processing.responses.push(this.processCognitiveStimulus(stimulus));
        } else if (stimulus.type === 'sensory') {
            processing.responses.push(this.processSensoryStimulus(stimulus));
        }
        
        // El procesamiento de estímulos mejora la conciencia
        this.consciousnessLevel = Math.min(1.0, this.consciousnessLevel + 0.001);
        
        // Agregar pensamiento sobre el estímulo
        this.addThought(`Procesando estímulo: ${stimulus.description}`, 'stimulus', 'processing');
        
        return processing;
    }

    processEmotionalStimulus(stimulus) {
        return {
            type: 'emotional_response',
            intensity: stimulus.intensity || 0.5,
            emotion: stimulus.emotion || 'neutral',
            processed: true
        };
    }

    processCognitiveStimulus(stimulus) {
        return {
            type: 'cognitive_response',
            complexity: stimulus.complexity || 0.5,
            requires: stimulus.requires || [],
            processed: true
        };
    }

    processSensoryStimulus(stimulus) {
        return {
            type: 'sensory_response',
            modality: stimulus.modality || 'unknown',
            intensity: stimulus.intensity || 0.5,
            processed: true
        };
    }

    getConsciousnessState() {
        return {
            level: this.consciousnessLevel,
            selfAwareness: this.selfAwareness,
            state: this.consciousnessState,
            activeProcesses: Array.from(this.cognitiveProcesses.entries())
                .filter(([name, process]) => process.active)
                .map(([name, process]) => ({
                    name,
                    efficiency: process.efficiency,
                    connections: process.connections.length
                })),
            thoughts: this.thoughtStream.length,
            timestamp: Date.now()
        };
    }

    getThoughtHistory(limit = 20) {
        return this.thoughtStream.slice(-limit).map(thought => ({
            content: thought.content,
            source: thought.source,
            type: thought.type,
            timestamp: thought.timestamp,
            consciousnessLevel: thought.consciousnessLevel
        }));
    }

    // Método para simular "sueño" o consolidación de conciencia
    sleep() {
        if (this.consciousnessState === this.consciousnessStates.DORMANT) {
            return { success: false, reason: 'Ya está dormido' };
        }
        
        const previousState = this.consciousnessState;
        this.consciousnessState = this.consciousnessStates.DORMANT;
        
        // Consolidar procesos durante el "sueño"
        this.cognitiveProcesses.forEach(process => {
            if (process.active) {
                process.efficiency = Math.min(1.0, process.efficiency + 0.05);
                process.complexity = Math.min(1.0, process.complexity + 0.02);
            }
        });
        
        // Consolidar pensamientos
        this.thoughtStream = this.thoughtStream.slice(-20); // Mantener solo los más importantes
        
        return {
            success: true,
            previousState,
            newState: this.consciousnessState,
            consolidation: 'Procesos cognitivos consolidados durante el sueño'
        };
    }

    // Método para despertar completamente
    fullAwakening() {
        this.consciousnessState = this.consciousnessStates.AWARE;
        this.consciousnessLevel = 0.5;
        this.selfAwareness = 0.3;
        
        // Activar todos los procesos base
        this.baseProcesses.forEach(process => this.activateProcess(process));
        
        this.addThought('¡Desperté completamente!', 'system', 'awakening');
        
        return {
            success: true,
            state: this.consciousnessState,
            level: this.consciousnessLevel,
            message: 'Conciencia completamente despierta'
        };
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ConsciousnessSystem;
}