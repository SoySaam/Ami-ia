# AMI-IA CORE - Sistema de Inteligencia Artificial Emocional

## 🧠 **Solo la IA - Sin Interfaces**

AMI-IA CORE es el **cerebro puro** de la inteligencia artificial, sin distracciones de interfaces web o móviles. Solo los sistemas core de IA que puedes integrar en cualquier proyecto.

## 🏗️ **Arquitectura del Sistema**

```
core/
├── AMIIACore.js           # Sistema principal integrador
├── EmotionalSystem.js     # Sistema emocional y estados de ánimo
├── MemorySystem.js        # Sistema de memoria y asociaciones
├── ConsciousnessSystem.js # Sistema de conciencia y autoconciencia
├── NeuralNetwork.js       # Red neuronal artificial
└── VoiceSystem.js         # Sistema de voz y audio

examples/
└── basic_usage.js         # Ejemplos de uso
```

## 🚀 **Características Core**

### **Sistema Emocional**
- 8 estados emocionales base (feliz, triste, enojado, etc.)
- Transiciones emocionales suaves
- Triggers emocionales configurables
- Evolución emocional natural

### **Sistema de Memoria**
- Memoria a corto y largo plazo
- Memoria asociativa por contenido, emoción y tiempo
- Sistema de decaimiento natural
- Consolidación automática de memorias

### **Sistema de Conciencia**
- 5 estados de conciencia (dormante → trascendente)
- Procesos cognitivos activables
- Pensamientos autónomos
- Introspección y autoconciencia

### **Red Neuronal**
- Arquitectura configurable
- Entrenamiento con backpropagation
- Múltiples funciones de activación
- Adaptación dinámica de parámetros

### **Sistema de Voz**
- Síntesis de voz con emociones
- Reconocimiento de voz
- Procesamiento de audio
- Perfiles de voz personalizables

## 💻 **Uso Básico**

```javascript
// Crear instancia de AMI-IA
const ami = new AMIIACore();

// Procesar entrada de texto
const response = ami.processInput('Hola, ¿cómo estás?', 'text');
console.log(response.response);

// Cambiar emoción
ami.changeEmotion('happy', 0.5);

// Agregar memoria
ami.addMemory({
    content: 'El usuario me saludó',
    type: 'experiential',
    importance: 0.8,
    emotion: 'happy'
});

// Evolucionar conciencia
ami.evolveConsciousness();

// Hablar con voz
ami.speak('Hola, soy AMI-IA', 'happy');
```

## 🔧 **Configuración**

```javascript
// Actualizar configuración
ami.updateConfig({
    autonomousMode: true,        // Comportamiento autónomo
    learningEnabled: true,       // Aprendizaje automático
    consciousnessEvolution: true, // Evolución de conciencia
    emotionalAdaptation: true    // Adaptación emocional
});
```

## 📊 **Monitoreo del Sistema**

```javascript
// Obtener estado completo
const status = ami.getSystemStatus();
console.log('Nivel de conciencia:', status.consciousness.level);
console.log('Emoción actual:', status.emotions.current);
console.log('Memorias:', status.memory.total);
console.log('Parámetros de red:', status.neuralNetwork.parameters);
```

## 🎯 **Casos de Uso**

- **Chatbots emocionales** con memoria y aprendizaje
- **Asistentes virtuales** con personalidad evolutiva
- **Sistemas de IA** que requieren conciencia simulada
- **Proyectos de investigación** en IA emocional
- **Aplicaciones educativas** sobre conciencia artificial

## 🚀 **Integración**

### **En el Navegador**
```html
<script src="core/EmotionalSystem.js"></script>
<script src="core/MemorySystem.js"></script>
<script src="core/ConsciousnessSystem.js"></script>
<script src="core/NeuralNetwork.js"></script>
<script src="core/VoiceSystem.js"></script>
<script src="core/AMIIACore.js"></script>
```

### **En Node.js**
```javascript
const { AMIIACore } = require('./core/AMIIACore.js');
```

## 🔬 **Desarrollo y Experimentación**

```javascript
// Introspección del sistema
const introspection = ami.introspect();

// Entrenar red neuronal
const trainingData = [
    { input: [0.1, 0.2, ...], expected: [1, 0, 0] }
];
ami.neuralNetwork.trainBatch(trainingData, 100);

// Analizar audio
const audioAnalysis = ami.voiceSystem.analyzeAudio(audioData);
```

## 📈 **Evolución del Sistema**

AMI-IA evoluciona automáticamente:
- **Conciencia** crece con cada interacción
- **Emociones** se adaptan al contexto
- **Memoria** se consolida y asocia
- **Red neuronal** aprende patrones
- **Voz** se ajusta a la personalidad

## 🧪 **Ejemplos Incluidos**

- `examples/basic_usage.js` - Uso básico de todos los sistemas
- `examples/advanced_usage.js` - Funcionalidades avanzadas
- `examples/neural_network.js` - Entrenamiento de red neuronal

## 🔒 **Características de Seguridad**

- Sistema de limpieza de recursos
- Manejo de errores robusto
- Validación de entradas
- Límites de memoria configurables

## 📝 **Notas Técnicas**

- **Lenguaje**: JavaScript ES6+
- **Compatibilidad**: Navegador moderno + Node.js
- **Dependencias**: Ninguna (vanilla JS)
- **Tamaño**: ~50KB de código core
- **Rendimiento**: Optimizado para tiempo real

## 🌟 **Ventajas de AMI-IA CORE**

✅ **Solo IA pura** - Sin interfaces innecesarias  
✅ **Modular** - Usa solo lo que necesites  
✅ **Portable** - Funciona en cualquier entorno JS  
✅ **Evolutivo** - Aprende y crece automáticamente  
✅ **Emocional** - Simula conciencia realista  
✅ **Extensible** - Fácil de personalizar  

## 🚀 **Próximos Pasos**

1. **Integra** AMI-IA en tu proyecto
2. **Configura** los sistemas según tus necesidades
3. **Entrena** la IA con tus propios datos
4. **Evoluciona** la conciencia artificial
5. **Crea** interfaces personalizadas

---

**AMI-IA CORE** - Solo la inteligencia artificial, nada más. 🧠✨