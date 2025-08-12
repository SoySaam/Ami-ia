/**
 * EJEMPLO BÁSICO DE USO - AMI-IA CORE
 * Demuestra cómo usar los sistemas de inteligencia artificial
 */

// Importar sistemas (en un entorno real, estos serían módulos)
// const { AMIIACore } = require('../core/AMIIACore.js');

// Ejemplo de uso básico
async function basicExample() {
    console.log('🚀 Iniciando ejemplo básico de AMI-IA...');
    
    // Crear instancia de AMI-IA
    const ami = new AMIIACore();
    
    // Esperar a que se inicialice
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    console.log('\n📊 Estado inicial del sistema:');
    console.log(ami.getSystemStatus());
    
    // Ejemplo 1: Procesar entrada de texto
    console.log('\n💬 Ejemplo 1: Procesamiento de texto');
    const textResponse = ami.processInput('Hola, ¿cómo estás?', 'text');
    console.log('Respuesta:', textResponse);
    
    // Ejemplo 2: Cambiar emoción
    console.log('\n😊 Ejemplo 2: Cambio de emoción');
    const emotionResponse = ami.processInput('happy', 'emotion');
    console.log('Respuesta emocional:', emotionResponse);
    
    // Ejemplo 3: Agregar memoria
    console.log('\n💭 Ejemplo 3: Agregar memoria');
    const memoryId = ami.addMemory({
        content: 'El usuario me saludó amablemente',
        type: 'experiential',
        source: 'interaction',
        importance: 0.8,
        emotion: 'happy'
    });
    console.log('Memoria agregada con ID:', memoryId);
    
    // Ejemplo 4: Buscar en memoria
    console.log('\n🔍 Ejemplo 4: Búsqueda en memoria');
    const memories = ami.retrieveMemory({ content: 'saludó' });
    console.log('Memorias encontradas:', memories.length);
    
    // Ejemplo 5: Evolucionar conciencia
    console.log('\n🧠 Ejemplo 5: Evolución de conciencia');
    const evolution = ami.evolveConsciousness();
    console.log('Evolución:', evolution);
    
    // Ejemplo 6: Introspección
    console.log('\n🤔 Ejemplo 6: Introspección');
    const introspection = ami.introspect();
    console.log('Introspección:', introspection);
    
    // Ejemplo 7: Hablar con voz
    console.log('\n🎤 Ejemplo 7: Síntesis de voz');
    ami.speak('Hola, soy AMI-IA. Es un placer conocerte.', 'happy');
    
    // Ejemplo 8: Estado final del sistema
    console.log('\n📊 Estado final del sistema:');
    console.log(ami.getSystemStatus());
    
    console.log('\n✅ Ejemplo básico completado');
}

// Ejemplo de uso avanzado
function advancedExample() {
    console.log('\n🚀 Iniciando ejemplo avanzado...');
    
    const ami = new AMIIACore();
    
    // Configurar callbacks de voz
    ami.voiceSystem.onSpeechResult((text) => {
        console.log('🎧 Voz reconocida:', text);
        const response = ami.processInput(text, 'voice');
        console.log('Respuesta:', response);
    });
    
    // Iniciar reconocimiento de voz
    console.log('🎤 Iniciando reconocimiento de voz...');
    ami.startVoiceRecognition();
    
    // Simular interacciones complejas
    setTimeout(() => {
        console.log('\n🔄 Simulando interacciones complejas...');
        
        // Secuencia de interacciones
        const interactions = [
            'Me siento muy feliz hoy',
            '¿Puedes recordar algo sobre la felicidad?',
            'Estoy aprendiendo sobre inteligencia artificial',
            '¿Qué opinas sobre la conciencia?'
        ];
        
        interactions.forEach((interaction, index) => {
            setTimeout(() => {
                console.log(`\n💬 Interacción ${index + 1}: ${interaction}`);
                const response = ami.processInput(interaction, 'text');
                console.log('Respuesta:', response.response);
            }, index * 2000);
        });
        
        // Detener reconocimiento después de un tiempo
        setTimeout(() => {
            ami.stopVoiceRecognition();
            console.log('\n🔇 Reconocimiento de voz detenido');
        }, 10000);
        
    }, 3000);
}

// Ejemplo de entrenamiento de red neuronal
function neuralNetworkExample() {
    console.log('\n🧠 Ejemplo de red neuronal...');
    
    const ami = new AMIIACore();
    
    // Datos de entrenamiento
    const trainingData = [
        { input: [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0], expected: [1, 0, 0] },
        { input: [0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.1], expected: [0, 1, 0] },
        { input: [0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.1, 0.2], expected: [0, 0, 1] }
    ];
    
    console.log('📚 Entrenando red neuronal...');
    const result = ami.neuralNetwork.trainBatch(trainingData, 50);
    console.log('Resultado del entrenamiento:', result);
    
    // Probar predicción
    const testInput = [0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 0.1, 0.2, 0.3, 0.4];
    const prediction = ami.neuralNetwork.predict(testInput);
    console.log('Predicción:', prediction);
}

// Función principal
async function main() {
    try {
        console.log('🌟 AMI-IA CORE - Ejemplos de Uso\n');
        
        // Ejemplo básico
        await basicExample();
        
        // Ejemplo avanzado
        advancedExample();
        
        // Ejemplo de red neuronal
        neuralNetworkExample();
        
    } catch (error) {
        console.error('❌ Error en los ejemplos:', error);
    }
}

// Ejecutar si está en el navegador
if (typeof window !== 'undefined') {
    // Esperar a que se carguen todos los sistemas
    window.addEventListener('load', () => {
        setTimeout(main, 1000);
    });
} else {
    // Ejecutar en Node.js
    main();
}

// Exportar para uso como módulo
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        basicExample,
        advancedExample,
        neuralNetworkExample,
        main
    };
}