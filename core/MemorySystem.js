/**
 * SISTEMA DE MEMORIA CORE - AMI-IA
 * Maneja almacenamiento, recuperación y asociación de memorias
 */

class MemorySystem {
    constructor() {
        this.shortTermMemory = [];
        this.longTermMemory = [];
        this.associativeMemory = new Map();
        this.memoryCapacity = 1000;
        this.retentionRate = 0.85;
        this.associationStrength = 0.7;
        
        // Tipos de memoria
        this.memoryTypes = {
            EMOTIONAL: 'emotional',
            FACTUAL: 'factual',
            EXPERIENTIAL: 'experiential',
            ASSOCIATIVE: 'associative',
            PROCEDURAL: 'procedural'
        };
        
        this.initMemorySystem();
    }

    initMemorySystem() {
        // Inicializar con memorias base del sistema
        this.addMemory({
            content: 'Sistema de memoria inicializado',
            type: this.memoryTypes.FACTUAL,
            source: 'system',
            timestamp: Date.now(),
            importance: 0.9,
            emotion: 'neutral'
        });
    }

    addMemory(memoryData) {
        const memory = {
            id: this.generateMemoryId(),
            content: memoryData.content,
            type: memoryData.type || this.memoryTypes.EXPERIENTIAL,
            source: memoryData.source || 'unknown',
            timestamp: memoryData.timestamp || Date.now(),
            importance: Math.min(1.0, Math.max(0.1, memoryData.importance || 0.5)),
            emotion: memoryData.emotion || 'neutral',
            associations: memoryData.associations || [],
            accessCount: 0,
            lastAccessed: Date.now(),
            decayRate: this.calculateDecayRate(memoryData.importance, memoryData.type)
        };

        // Agregar a memoria a corto plazo
        this.shortTermMemory.push(memory);
        
        // Procesar asociaciones
        this.processAssociations(memory);
        
        // Limpiar memoria a corto plazo si excede el límite
        this.manageMemoryCapacity();
        
        return memory.id;
    }

    generateMemoryId() {
        return `mem_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    }

    calculateDecayRate(importance, type) {
        let baseRate = 0.1;
        
        // Las memorias importantes decaen más lentamente
        if (importance > 0.8) baseRate *= 0.5;
        if (importance < 0.3) baseRate *= 2.0;
        
        // Diferentes tipos de memoria tienen diferentes tasas de decaimiento
        switch (type) {
            case this.memoryTypes.EMOTIONAL:
                baseRate *= 0.7; // Las emociones se recuerdan mejor
                break;
            case this.memoryTypes.PROCEDURAL:
                baseRate *= 0.6; // Los procedimientos se retienen mejor
                break;
            case this.memoryTypes.FACTUAL:
                baseRate *= 1.2; // Los hechos pueden olvidarse más fácilmente
                break;
        }
        
        return baseRate;
    }

    processAssociations(memory) {
        // Crear asociaciones basadas en contenido, emoción y contexto
        const associations = [];
        
        // Asociaciones por emoción
        if (memory.emotion !== 'neutral') {
            associations.push({
                type: 'emotion',
                value: memory.emotion,
                strength: 0.8
            });
        }
        
        // Asociaciones por contenido (palabras clave)
        const keywords = this.extractKeywords(memory.content);
        keywords.forEach(keyword => {
            associations.push({
                type: 'content',
                value: keyword,
                strength: 0.6
            });
        });
        
        // Asociaciones por tiempo (memorias cercanas en tiempo)
        const timeAssociations = this.findTimeAssociations(memory.timestamp);
        timeAssociations.forEach(assoc => {
            associations.push({
                type: 'temporal',
                value: assoc.id,
                strength: assoc.strength
            });
        });
        
        memory.associations = associations;
        
        // Actualizar mapa de memoria asociativa
        this.updateAssociativeMap(memory);
    }

    extractKeywords(content) {
        const words = content.toLowerCase().split(/\s+/);
        const stopWords = ['el', 'la', 'de', 'que', 'y', 'a', 'en', 'un', 'es', 'se', 'no', 'te', 'lo', 'le', 'da', 'su', 'por', 'son', 'con', 'para', 'al', 'del', 'los', 'una', 'como', 'pero', 'sus', 'me', 'hasta', 'hay', 'donde', 'han', 'quien', 'están', 'estado', 'desde', 'todo', 'nos', 'durante', 'todos', 'uno', 'les', 'ni', 'contra', 'otros', 'ese', 'eso', 'ante', 'ellos', 'e', 'esto', 'mí', 'antes', 'algunos', 'qué', 'unos', 'yo', 'otro', 'otras', 'otra', 'él', 'tanto', 'esa', 'estos', 'mucho', 'quienes', 'nada', 'muchos', 'cual', 'poco', 'ella', 'estar', 'estas', 'algunas', 'algo', 'nosotros'];
        
        return words.filter(word => 
            word.length > 3 && 
            !stopWords.includes(word) && 
            /^[a-záéíóúñ]+$/i.test(word)
        ).slice(0, 5); // Máximo 5 palabras clave
    }

    findTimeAssociations(timestamp) {
        const timeWindow = 5 * 60 * 1000; // 5 minutos
        const associations = [];
        
        [...this.shortTermMemory, ...this.longTermMemory].forEach(memory => {
            if (Math.abs(memory.timestamp - timestamp) < timeWindow) {
                const strength = 1 - (Math.abs(memory.timestamp - timestamp) / timeWindow);
                associations.push({
                    id: memory.id,
                    strength: strength * 0.5 // Las asociaciones temporales son más débiles
                });
            }
        });
        
        return associations;
    }

    updateAssociativeMap(memory) {
        memory.associations.forEach(association => {
            const key = `${association.type}:${association.value}`;
            
            if (!this.associativeMemory.has(key)) {
                this.associativeMemory.set(key, []);
            }
            
            this.associativeMemory.get(key).push({
                memoryId: memory.id,
                strength: association.strength,
                timestamp: memory.timestamp
            });
        });
    }

    retrieveMemory(query, type = 'all') {
        const results = [];
        
        // Búsqueda por contenido
        if (query.content) {
            const contentMatches = this.searchByContent(query.content);
            results.push(...contentMatches);
        }
        
        // Búsqueda por emoción
        if (query.emotion) {
            const emotionMatches = this.searchByEmotion(query.emotion);
            results.push(...emotionMatches);
        }
        
        // Búsqueda por asociaciones
        if (query.associations) {
            const associationMatches = this.searchByAssociations(query.associations);
            results.push(...associationMatches);
        }
        
        // Búsqueda por tiempo
        if (query.timeRange) {
            const timeMatches = this.searchByTimeRange(query.timeRange);
            results.push(...timeMatches);
        }
        
        // Filtrar por tipo si se especifica
        if (type !== 'all') {
            results = results.filter(memory => memory.type === type);
        }
        
        // Ordenar por relevancia
        results.sort((a, b) => this.calculateRelevance(b, query) - this.calculateRelevance(a, query));
        
        // Actualizar contadores de acceso
        results.slice(0, 10).forEach(memory => this.updateAccessStats(memory.id));
        
        return results.slice(0, 20); // Máximo 20 resultados
    }

    searchByContent(content) {
        const query = content.toLowerCase();
        return [...this.shortTermMemory, ...this.longTermMemory].filter(memory => 
            memory.content.toLowerCase().includes(query)
        );
    }

    searchByEmotion(emotion) {
        return [...this.shortTermMemory, ...this.longTermMemory].filter(memory => 
            memory.emotion === emotion
        );
    }

    searchByAssociations(associations) {
        const results = [];
        associations.forEach(assoc => {
            const key = `${assoc.type}:${assoc.value}`;
            if (this.associativeMemory.has(key)) {
                const relatedMemories = this.associativeMemory.get(key);
                relatedMemories.forEach(related => {
                    const memory = this.findMemoryById(related.memoryId);
                    if (memory) {
                        results.push({
                            ...memory,
                            associationStrength: related.strength
                        });
                    }
                });
            }
        });
        return results;
    }

    searchByTimeRange(timeRange) {
        const { start, end } = timeRange;
        return [...this.shortTermMemory, ...this.longTermMemory].filter(memory => 
            memory.timestamp >= start && memory.timestamp <= end
        );
    }

    calculateRelevance(memory, query) {
        let relevance = 0;
        
        // Relevancia por importancia
        relevance += memory.importance * 0.3;
        
        // Relevancia por acceso reciente
        const timeSinceAccess = Date.now() - memory.lastAccessed;
        const recencyFactor = Math.max(0, 1 - (timeSinceAccess / (24 * 60 * 60 * 1000)));
        relevance += recencyFactor * 0.2;
        
        // Relevancia por frecuencia de acceso
        const frequencyFactor = Math.min(1, memory.accessCount / 10);
        relevance += frequencyFactor * 0.2;
        
        // Relevancia por coincidencia exacta
        if (query.content && memory.content.toLowerCase().includes(query.content.toLowerCase())) {
            relevance += 0.3;
        }
        
        return relevance;
    }

    findMemoryById(id) {
        return [...this.shortTermMemory, ...this.longTermMemory].find(memory => memory.id === id);
    }

    updateAccessStats(memoryId) {
        const memory = this.findMemoryById(memoryId);
        if (memory) {
            memory.accessCount++;
            memory.lastAccessed = Date.now();
        }
    }

    manageMemoryCapacity() {
        // Mover memorias importantes a largo plazo
        if (this.shortTermMemory.length > this.memoryCapacity * 0.8) {
            const importantMemories = this.shortTermMemory.filter(memory => 
                memory.importance > 0.7 || memory.accessCount > 5
            );
            
            importantMemories.forEach(memory => {
                this.longTermMemory.push(memory);
                this.shortTermMemory = this.shortTermMemory.filter(m => m.id !== memory.id);
            });
        }
        
        // Limpiar memorias menos importantes si aún excede el límite
        if (this.shortTermMemory.length > this.memoryCapacity) {
            this.shortTermMemory.sort((a, b) => 
                (a.importance + a.accessCount * 0.1) - (b.importance + b.accessCount * 0.1)
            );
            
            const toRemove = this.shortTermMemory.length - this.memoryCapacity;
            this.shortTermMemory.splice(0, toRemove);
        }
    }

    // Simular decaimiento natural de la memoria
    decayMemories() {
        const now = Date.now();
        
        [...this.shortTermMemory, ...this.longTermMemory].forEach(memory => {
            const timeSinceCreation = now - memory.timestamp;
            const decayFactor = memory.decayRate * (timeSinceCreation / (24 * 60 * 60 * 1000));
            
            if (decayFactor > 1.0 && Math.random() < 0.1) {
                // Eliminar memoria completamente decaída
                this.removeMemory(memory.id);
            } else {
                // Reducir importancia gradualmente
                memory.importance = Math.max(0.1, memory.importance - decayFactor * 0.01);
            }
        });
    }

    removeMemory(memoryId) {
        this.shortTermMemory = this.shortTermMemory.filter(m => m.id !== memoryId);
        this.longTermMemory = this.longTermMemory.filter(m => m.id !== memoryId);
        
        // Limpiar asociaciones
        this.associativeMemory.forEach((memories, key) => {
            this.associativeMemory.set(key, memories.filter(m => m.memoryId !== memoryId));
        });
    }

    getMemoryStats() {
        return {
            shortTerm: this.shortTermMemory.length,
            longTerm: this.longTermMemory.length,
            total: this.shortTermMemory.length + this.longTermMemory.length,
            capacity: this.memoryCapacity,
            associations: this.associativeMemory.size,
            system: 'MemorySystem v1.0'
        };
    }

    // Método para consolidar memorias (simular sueño/consolidación)
    consolidateMemories() {
        // Mover memorias importantes a largo plazo
        const importantShortTerm = this.shortTermMemory.filter(memory => 
            memory.importance > 0.6 || memory.accessCount > 3
        );
        
        importantShortTerm.forEach(memory => {
            this.longTermMemory.push(memory);
            this.shortTermMemory = this.shortTermMemory.filter(m => m.id !== memory.id);
        });
        
        // Fortalecer asociaciones existentes
        this.associativeMemory.forEach((memories, key) => {
            if (memories.length > 1) {
                memories.forEach(memory => {
                    memory.strength = Math.min(1.0, memory.strength * 1.1);
                });
            }
        });
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MemorySystem;
}