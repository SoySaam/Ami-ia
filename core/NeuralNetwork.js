/**
 * RED NEURONAL CORE - AMI-IA
 * Sistema de red neuronal artificial para aprendizaje y procesamiento
 */

class NeuralNetwork {
    constructor() {
        this.layers = [];
        this.weights = [];
        this.biases = [];
        this.learningRate = 0.01;
        this.momentum = 0.9;
        this.epochs = 0;
        this.trainingData = [];
        this.validationData = [];
        
        // Configuración de la red
        this.config = {
            inputSize: 10,
            hiddenLayers: [8, 6, 4],
            outputSize: 3,
            activationFunction: 'tanh',
            lossFunction: 'mse'
        };
        
        this.initialize();
    }

    initialize() {
        console.log('🧠 Inicializando red neuronal...');
        
        // Construir capas
        this.buildLayers();
        
        // Inicializar pesos y sesgos
        this.initializeWeights();
        
        console.log('✅ Red neuronal inicializada');
    }

    buildLayers() {
        const layerSizes = [this.config.inputSize, ...this.config.hiddenLayers, this.config.outputSize];
        
        for (let i = 0; i < layerSizes.length; i++) {
            this.layers.push(layerSizes[i]);
        }
    }

    initializeWeights() {
        // Inicializar pesos con valores aleatorios pequeños
        for (let i = 0; i < this.layers.length - 1; i++) {
            const weightMatrix = [];
            const biasVector = [];
            
            for (let j = 0; j < this.layers[i + 1]; j++) {
                const neuronWeights = [];
                for (let k = 0; k < this.layers[i]; k++) {
                    neuronWeights.push((Math.random() - 0.5) * 0.1);
                }
                weightMatrix.push(neuronWeights);
                biasVector.push((Math.random() - 0.5) * 0.1);
            }
            
            this.weights.push(weightMatrix);
            this.biases.push(biasVector);
        }
    }

    // Función de activación
    activate(x, functionName = 'tanh') {
        switch (functionName) {
            case 'sigmoid':
                return 1 / (1 + Math.exp(-x));
            case 'tanh':
                return Math.tanh(x);
            case 'relu':
                return Math.max(0, x);
            case 'leaky_relu':
                return x > 0 ? x : 0.01 * x;
            default:
                return Math.tanh(x);
        }
    }

    // Derivada de la función de activación
    activateDerivative(x, functionName = 'tanh') {
        switch (functionName) {
            case 'sigmoid':
                const sig = this.activate(x, 'sigmoid');
                return sig * (1 - sig);
            case 'tanh':
                return 1 - Math.tanh(x) ** 2;
            case 'relu':
                return x > 0 ? 1 : 0;
            case 'leaky_relu':
                return x > 0 ? 1 : 0.01;
            default:
                return 1 - Math.tanh(x) ** 2;
        }
    }

    // Propagación hacia adelante
    forward(input) {
        if (input.length !== this.config.inputSize) {
            throw new Error(`Entrada debe tener ${this.config.inputSize} elementos`);
        }

        let currentLayer = [...input];
        const activations = [currentLayer];
        const zValues = [];

        // Propagar a través de las capas
        for (let i = 0; i < this.weights.length; i++) {
            const layerZ = [];
            const layerActivations = [];

            for (let j = 0; j < this.weights[i].length; j++) {
                let z = this.biases[i][j];
                
                for (let k = 0; k < this.weights[i][j].length; k++) {
                    z += this.weights[i][j][k] * currentLayer[k];
                }
                
                layerZ.push(z);
                layerActivations.push(this.activate(z, this.config.activationFunction));
            }

            zValues.push(layerZ);
            activations.push(layerActivations);
            currentLayer = layerActivations;
        }

        return {
            output: currentLayer,
            activations,
            zValues
        };
    }

    // Calcular pérdida
    calculateLoss(predicted, expected) {
        if (this.config.lossFunction === 'mse') {
            let loss = 0;
            for (let i = 0; i < predicted.length; i++) {
                loss += Math.pow(predicted[i] - expected[i], 2);
            }
            return loss / predicted.length;
        }
        return 0;
    }

    // Retropropagación del error
    backward(input, expected, activations, zValues) {
        const gradients = [];
        const weightGradients = [];
        const biasGradients = [];

        // Calcular gradiente de la capa de salida
        let delta = [];
        for (let i = 0; i < activations[activations.length - 1].length; i++) {
            const error = activations[activations.length - 1][i] - expected[i];
            const derivative = this.activateDerivative(zValues[zValues.length - 1][i], this.config.activationFunction);
            delta.push(error * derivative);
        }

        gradients.unshift(delta);

        // Calcular gradientes para las capas ocultas
        for (let i = this.weights.length - 1; i > 0; i--) {
            const newDelta = [];
            
            for (let j = 0; j < this.weights[i].length; j++) {
                let error = 0;
                for (let k = 0; k < this.weights[i + 1] ? this.weights[i + 1].length : delta.length; k++) {
                    if (this.weights[i + 1]) {
                        error += this.weights[i + 1][k][j] * delta[k];
                    } else {
                        error += delta[k];
                    }
                }
                
                const derivative = this.activateDerivative(zValues[i - 1][j], this.config.activationFunction);
                newDelta.push(error * derivative);
            }
            
            delta = newDelta;
            gradients.unshift(delta);
        }

        // Calcular gradientes de pesos y sesgos
        for (let i = 0; i < this.weights.length; i++) {
            const layerWeightGradients = [];
            const layerBiasGradients = [];

            for (let j = 0; j < this.weights[i].length; j++) {
                const neuronWeightGradients = [];
                for (let k = 0; k < this.weights[i][j].length; k++) {
                    neuronWeightGradients.push(gradients[i + 1][j] * activations[i][k]);
                }
                layerWeightGradients.push(neuronWeightGradients);
                layerBiasGradients.push(gradients[i + 1][j]);
            }

            weightGradients.push(layerWeightGradients);
            biasGradients.push(layerBiasGradients);
        }

        return { weightGradients, biasGradients };
    }

    // Actualizar pesos y sesgos
    updateWeights(weightGradients, biasGradients) {
        for (let i = 0; i < this.weights.length; i++) {
            for (let j = 0; j < this.weights[i].length; j++) {
                for (let k = 0; k < this.weights[i][j].length; k++) {
                    this.weights[i][j][k] -= this.learningRate * weightGradients[i][j][k];
                }
                this.biases[i][j] -= this.learningRate * biasGradients[i][j];
            }
        }
    }

    // Entrenar la red
    train(input, expected, epochs = 1) {
        let totalLoss = 0;
        
        for (let epoch = 0; epoch < epochs; epoch++) {
            // Propagación hacia adelante
            const { output, activations, zValues } = this.forward(input);
            
            // Calcular pérdida
            const loss = this.calculateLoss(output, expected);
            totalLoss += loss;
            
            // Retropropagación
            const { weightGradients, biasGradients } = this.backward(input, expected, activations, zValues);
            
            // Actualizar pesos
            this.updateWeights(weightGradients, biasGradients);
            
            this.epochs++;
        }
        
        return {
            averageLoss: totalLoss / epochs,
            epochs: this.epochs
        };
    }

    // Entrenar con conjunto de datos
    trainBatch(trainingData, epochs = 100, batchSize = 32) {
        console.log(`🚀 Iniciando entrenamiento con ${trainingData.length} ejemplos...`);
        
        const losses = [];
        
        for (let epoch = 0; epoch < epochs; epoch++) {
            let epochLoss = 0;
            
            // Mezclar datos de entrenamiento
            const shuffledData = this.shuffleArray([...trainingData]);
            
            // Procesar en lotes
            for (let i = 0; i < shuffledData.length; i += batchSize) {
                const batch = shuffledData.slice(i, i + batchSize);
                let batchLoss = 0;
                
                batch.forEach(({ input, expected }) => {
                    const { output } = this.forward(input);
                    const loss = this.calculateLoss(output, expected);
                    batchLoss += loss;
                    
                    // Entrenar con este ejemplo
                    this.train(input, expected, 1);
                });
                
                epochLoss += batchLoss / batch.length;
            }
            
            losses.push(epochLoss);
            
            // Mostrar progreso cada 10 épocas
            if (epoch % 10 === 0) {
                console.log(`Época ${epoch}: Pérdida = ${epochLoss.toFixed(6)}`);
            }
            
            // Early stopping si la pérdida no mejora
            if (epoch > 20 && epochLoss > losses[epoch - 10] * 1.1) {
                console.log('🛑 Early stopping activado');
                break;
            }
        }
        
        console.log('✅ Entrenamiento completado');
        return {
            finalLoss: losses[losses.length - 1],
            lossHistory: losses,
            epochs: this.epochs
        };
    }

    // Predecir
    predict(input) {
        const { output } = this.forward(input);
        return output;
    }

    // Evaluar rendimiento
    evaluate(testData) {
        let totalLoss = 0;
        let correctPredictions = 0;
        
        testData.forEach(({ input, expected }) => {
            const prediction = this.predict(input);
            const loss = this.calculateLoss(prediction, expected);
            totalLoss += loss;
            
            // Contar predicciones correctas (para clasificación)
            if (this.isCorrectPrediction(prediction, expected)) {
                correctPredictions++;
            }
        });
        
        const accuracy = correctPredictions / testData.length;
        const averageLoss = totalLoss / testData.length;
        
        return {
            accuracy,
            averageLoss,
            correctPredictions,
            totalPredictions: testData.length
        };
    }

    isCorrectPrediction(prediction, expected) {
        // Para clasificación: la clase con mayor probabilidad
        const predictedClass = prediction.indexOf(Math.max(...prediction));
        const expectedClass = expected.indexOf(Math.max(...expected));
        return predictedClass === expectedClass;
    }

    // Utilidades
    shuffleArray(array) {
        const shuffled = [...array];
        for (let i = shuffled.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
        }
        return shuffled;
    }

    // Guardar y cargar modelo
    saveModel() {
        const model = {
            config: this.config,
            weights: this.weights,
            biases: this.biases,
            epochs: this.epochs,
            timestamp: Date.now()
        };
        
        return JSON.stringify(model);
    }

    loadModel(modelString) {
        try {
            const model = JSON.parse(modelString);
            this.config = model.config;
            this.weights = model.weights;
            this.biases = model.biases;
            this.epochs = model.epochs;
            
            console.log('📥 Modelo cargado exitosamente');
            return true;
        } catch (error) {
            console.error('❌ Error al cargar modelo:', error);
            return false;
        }
    }

    // Métodos para adaptación dinámica
    adaptLearningRate(performance) {
        if (performance.improving) {
            this.learningRate = Math.min(0.1, this.learningRate * 1.05);
        } else {
            this.learningRate = Math.max(0.001, this.learningRate * 0.95);
        }
    }

    addLayer(layerSize) {
        if (this.layers.length < 2) return false;
        
        // Insertar nueva capa antes de la última
        const insertIndex = this.layers.length - 1;
        this.layers.splice(insertIndex, 0, layerSize);
        
        // Recalcular pesos y sesgos
        this.recalculateArchitecture();
        
        return true;
    }

    recalculateArchitecture() {
        // Reconstruir pesos y sesgos para la nueva arquitectura
        this.weights = [];
        this.biases = [];
        this.initializeWeights();
    }

    // Obtener estadísticas de la red
    getNetworkStats() {
        let totalWeights = 0;
        let totalBiases = 0;
        
        this.weights.forEach(layer => {
            layer.forEach(neuron => {
                totalWeights += neuron.length;
            });
        });
        
        this.biases.forEach(layer => {
            totalBiases += layer.length;
        });
        
        return {
            layers: this.layers,
            totalWeights,
            totalBiases,
            parameters: totalWeights + totalBiases,
            epochs: this.epochs,
            learningRate: this.learningRate,
            config: this.config
        };
    }
}

// Exportar para uso en otros módulos
if (typeof module !== 'undefined' && module.exports) {
    module.exports = NeuralNetwork;
}