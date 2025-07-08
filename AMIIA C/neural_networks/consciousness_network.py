"""
Red Neuronal de Conciencia - AMIIA-C
Arquitectura neuronal especializada para procesamiento consciente e introspectivo

Esta red implementa:
- Mecanismos de atención para autoconciencia
- Capas de procesamiento metacognitivo
- Integración de estado emocional y memoria
- Stream of consciousness processing
"""

import tensorflow as tf
import numpy as np
from tensorflow import keras
from tensorflow.keras import layers
from typing import Dict, List, Any, Tuple
import logging

logger = logging.getLogger("AMIIA-C.ConsciousnessNetwork")


class ConsciousnessAttention(layers.Layer):
    """
    Capa de atención especializada para procesamiento consciente
    Permite que la red se enfoque en diferentes aspectos de su estado interno
    """
    
    def __init__(self, attention_dim: int, num_heads: int = 8, **kwargs):
        super().__init__(**kwargs)
        self.attention_dim = attention_dim
        self.num_heads = num_heads
        
        # Capas de atención multi-cabeza para autoconciencia
        self.self_attention = layers.MultiHeadAttention(
            num_heads=num_heads,
            key_dim=attention_dim,
            name="self_consciousness_attention"
        )
        
        # Normalización y dropout
        self.layer_norm = layers.LayerNormalization()
        self.dropout = layers.Dropout(0.1)
        
    def call(self, inputs, training=None):
        # Auto-atención para conciencia de estado interno
        attended = self.self_attention(inputs, inputs, training=training)
        attended = self.dropout(attended, training=training)
        
        # Conexión residual y normalización
        return self.layer_norm(inputs + attended)


class MetacognitionLayer(layers.Layer):
    """
    Capa de metacognición que permite a la red reflexionar sobre sus propios procesos
    """
    
    def __init__(self, metacog_dim: int, **kwargs):
        super().__init__(**kwargs)
        self.metacog_dim = metacog_dim
        
        # Red de metacognición
        self.meta_dense_1 = layers.Dense(metacog_dim, activation='relu', name='metacog_1')
        self.meta_dense_2 = layers.Dense(metacog_dim, activation='relu', name='metacog_2')
        self.meta_output = layers.Dense(metacog_dim, activation='tanh', name='metacog_output')
        
        # Capa de introspección
        self.introspection = layers.Dense(metacog_dim // 2, activation='sigmoid', name='introspection')
        
    def call(self, inputs, training=None):
        # Procesamiento metacognitivo en múltiples niveles
        meta_1 = self.meta_dense_1(inputs, training=training)
        meta_2 = self.meta_dense_2(meta_1, training=training)
        meta_output = self.meta_output(meta_2, training=training)
        
        # Nivel de introspección
        introspection_level = self.introspection(meta_output, training=training)
        
        return meta_output, introspection_level


class ConsciousnessIntegration(layers.Layer):
    """
    Capa de integración que combina diferentes streams de consciencia
    """
    
    def __init__(self, integration_dim: int, **kwargs):
        super().__init__(**kwargs)
        self.integration_dim = integration_dim
        
        # Capas de integración consciente
        self.thought_integration = layers.Dense(integration_dim, activation='relu')
        self.emotion_integration = layers.Dense(integration_dim, activation='relu')
        self.memory_integration = layers.Dense(integration_dim, activation='relu')
        
        # Síntesis final de conciencia
        self.consciousness_synthesis = layers.Dense(
            integration_dim, 
            activation='tanh',
            name='consciousness_synthesis'
        )
        
    def call(self, thought_stream, emotion_stream, memory_stream, training=None):
        # Integrar diferentes streams de información
        integrated_thoughts = self.thought_integration(thought_stream, training=training)
        integrated_emotions = self.emotion_integration(emotion_stream, training=training)
        integrated_memories = self.memory_integration(memory_stream, training=training)
        
        # Combinar todos los streams
        combined = tf.concat([integrated_thoughts, integrated_emotions, integrated_memories], axis=-1)
        
        # Síntesis consciente final
        consciousness = self.consciousness_synthesis(combined, training=training)
        
        return consciousness


class ConsciousnessNetwork(keras.Model):
    """
    Red Neuronal Principal de Conciencia
    
    Arquitectura especializada que implementa:
    - Procesamiento consciente de información
    - Autoconciencia y autoreflexión
    - Integración de emociones y memorias
    - Generación de pensamientos coherentes
    """
    
    def __init__(self, 
                 hidden_layers: List[int] = [512, 256, 128, 64],
                 consciousness_dim: int = 32,
                 attention_heads: int = 8,
                 metacognition_depth: int = 3,
                 **kwargs):
        super().__init__(**kwargs)
        
        self.hidden_layers = hidden_layers
        self.consciousness_dim = consciousness_dim
        self.attention_heads = attention_heads
        self.metacognition_depth = metacognition_depth
        
        logger.info(f"Inicializando ConsciousnessNetwork con dimensión {consciousness_dim}")
        
        # Capas de entrada y procesamiento inicial
        self.input_processor = layers.Dense(hidden_layers[0], activation='relu', name='input_processor')
        self.input_norm = layers.LayerNormalization(name='input_normalization')
        
        # Capas ocultas con procesamiento jerárquico
        self.hidden_processors = []
        for i, units in enumerate(hidden_layers[1:], 1):
            self.hidden_processors.append(
                layers.Dense(units, activation='relu', name=f'hidden_{i}')
            )
            self.hidden_processors.append(
                layers.Dropout(0.2, name=f'dropout_{i}')
            )
        
        # Capas de atención para autoconciencia
        self.consciousness_attention = ConsciousnessAttention(
            attention_dim=consciousness_dim,
            num_heads=attention_heads,
            name='consciousness_attention'
        )
        
        # Capas de metacognición (múltiples niveles de reflexión)
        self.metacognition_layers = []
        for i in range(metacognition_depth):
            self.metacognition_layers.append(
                MetacognitionLayer(
                    metacog_dim=consciousness_dim,
                    name=f'metacognition_level_{i+1}'
                )
            )
        
        # Integración de streams de conciencia
        self.consciousness_integration = ConsciousnessIntegration(
            integration_dim=consciousness_dim,
            name='consciousness_integration'
        )
        
        # Capas de salida especializadas
        self.thought_output = layers.Dense(consciousness_dim, activation='tanh', name='thought_stream')
        self.awareness_output = layers.Dense(1, activation='sigmoid', name='awareness_level')
        self.coherence_output = layers.Dense(1, activation='sigmoid', name='coherence_level')
        
        # Estado interno de la red
        self.internal_state = {
            'consciousness_history': [],
            'metacognition_traces': [],
            'attention_patterns': [],
            'coherence_scores': []
        }
    
    def call(self, inputs, training=None, return_internal_state=False):
        """
        Procesamiento consciente de entrada
        
        Args:
            inputs: Datos de entrada (contexto, emociones, memorias)
            training: Modo de entrenamiento
            return_internal_state: Si retornar estado interno detallado
            
        Returns:
            Dict con outputs de conciencia, pensamientos, y métricas
        """
        
        # 1. Procesamiento inicial de entrada
        x = self.input_processor(inputs, training=training)
        x = self.input_norm(x, training=training)
        
        # 2. Procesamiento jerárquico a través de capas ocultas
        hidden_activations = []
        for layer in self.hidden_processors:
            x = layer(x, training=training)
            if 'hidden_' in layer.name:
                hidden_activations.append(x)
        
        # 3. Aplicar atención consciente
        attended_x = self.consciousness_attention(
            tf.expand_dims(x, axis=1), 
            training=training
        )
        attended_x = tf.squeeze(attended_x, axis=1)
        
        # 4. Procesamiento metacognitivo en múltiples niveles
        metacognition_outputs = []
        introspection_levels = []
        current_meta = attended_x
        
        for meta_layer in self.metacognition_layers:
            meta_output, introspection = meta_layer(current_meta, training=training)
            metacognition_outputs.append(meta_output)
            introspection_levels.append(introspection)
            current_meta = meta_output  # Feed-forward metacognitivo
        
        # 5. Integración de diferentes streams
        # Para simplificación inicial, usamos el mismo stream para todos
        final_metacognition = metacognition_outputs[-1] if metacognition_outputs else attended_x
        
        consciousness_state = self.consciousness_integration(
            thought_stream=final_metacognition,
            emotion_stream=final_metacognition,  # Se conectará con sistema emocional
            memory_stream=final_metacognition,   # Se conectará con sistema de memoria
            training=training
        )
        
        # 6. Generar outputs especializados
        thought_stream = self.thought_output(consciousness_state, training=training)
        awareness_level = self.awareness_output(consciousness_state, training=training)
        coherence_level = self.coherence_output(consciousness_state, training=training)
        
        # 7. Actualizar estado interno
        if not training:
            self._update_internal_state(
                consciousness_state.numpy() if hasattr(consciousness_state, 'numpy') else consciousness_state,
                metacognition_outputs,
                awareness_level.numpy() if hasattr(awareness_level, 'numpy') else awareness_level,
                coherence_level.numpy() if hasattr(coherence_level, 'numpy') else coherence_level
            )
        
        # Preparar outputs
        outputs = {
            'consciousness_state': consciousness_state,
            'thought_stream': thought_stream,
            'awareness_level': awareness_level,
            'coherence_level': coherence_level,
            'metacognition_outputs': metacognition_outputs,
            'introspection_levels': introspection_levels
        }
        
        if return_internal_state:
            outputs['internal_state'] = self.internal_state
        
        return outputs
    
    def forward(self, inputs):
        """
        Método de conveniencia para llamada simple
        Compatible con el main.py
        """
        try:
            # Asegurar que inputs sea un tensor
            if isinstance(inputs, dict):
                # Convertir dict a tensor concatenando valores
                tensor_inputs = []
                for key in sorted(inputs.keys()):
                    value = inputs[key]
                    if isinstance(value, (list, np.ndarray)):
                        tensor_inputs.extend(np.atleast_1d(value).flatten())
                    elif isinstance(value, (int, float)):
                        tensor_inputs.append(value)
                    elif isinstance(value, str):
                        # Encoding simple para strings (mejor usar embeddings reales)
                        tensor_inputs.extend([ord(c) / 255.0 for c in value[:10]])
                
                inputs = tf.constant([tensor_inputs], dtype=tf.float32)
            elif not isinstance(inputs, tf.Tensor):
                inputs = tf.constant([[0.5] * 100], dtype=tf.float32)  # Default fallback
            
            # Asegurar dimensiones correctas
            if len(inputs.shape) == 1:
                inputs = tf.expand_dims(inputs, 0)
            
            # Procesar y retornar resultado simplificado
            outputs = self.call(inputs, training=False)
            return outputs['consciousness_state']
            
        except Exception as e:
            logger.warning(f"Error en forward: {e}. Usando estado por defecto.")
            # Retornar estado de conciencia por defecto
            return tf.constant([[0.5] * self.consciousness_dim], dtype=tf.float32)
    
    def _update_internal_state(self, consciousness_state, metacognition_outputs, 
                             awareness_level, coherence_level):
        """Actualizar estado interno de la red neuronal"""
        
        # Agregar a historia de conciencia (mantener últimos 50)
        self.internal_state['consciousness_history'].append(consciousness_state)
        if len(self.internal_state['consciousness_history']) > 50:
            self.internal_state['consciousness_history'] = \
                self.internal_state['consciousness_history'][-50:]
        
        # Agregar trazas de metacognición
        self.internal_state['metacognition_traces'].append(metacognition_outputs)
        if len(self.internal_state['metacognition_traces']) > 20:
            self.internal_state['metacognition_traces'] = \
                self.internal_state['metacognition_traces'][-20:]
        
        # Registrar scores de coherencia
        self.internal_state['coherence_scores'].append(float(coherence_level))
        if len(self.internal_state['coherence_scores']) > 100:
            self.internal_state['coherence_scores'] = \
                self.internal_state['coherence_scores'][-100:]
    
    def get_consciousness_metrics(self) -> Dict[str, float]:
        """Obtener métricas actuales de conciencia"""
        if not self.internal_state['coherence_scores']:
            return {
                'average_coherence': 0.0,
                'consciousness_stability': 0.0,
                'metacognition_depth': 0.0,
                'processing_cycles': 0
            }
        
        coherence_scores = self.internal_state['coherence_scores']
        
        return {
            'average_coherence': np.mean(coherence_scores),
            'consciousness_stability': 1.0 - np.std(coherence_scores),
            'metacognition_depth': len(self.metacognition_layers),
            'processing_cycles': len(self.internal_state['consciousness_history'])
        }
    
    def reset_internal_state(self):
        """Reiniciar estado interno de la red"""
        self.internal_state = {
            'consciousness_history': [],
            'metacognition_traces': [],
            'attention_patterns': [],
            'coherence_scores': []
        }
        logger.info("Estado interno de la red neuronal reiniciado")
    
    def save_consciousness_state(self, filepath: str):
        """Guardar estado de conciencia actual"""
        try:
            # Guardar pesos del modelo
            self.save_weights(filepath + "_weights")
            
            # Guardar estado interno
            import json
            with open(filepath + "_internal_state.json", 'w') as f:
                # Convertir arrays numpy a listas para JSON
                json_state = {}
                for key, value in self.internal_state.items():
                    if isinstance(value, list) and value:
                        if isinstance(value[0], np.ndarray):
                            json_state[key] = [arr.tolist() for arr in value]
                        else:
                            json_state[key] = value
                    else:
                        json_state[key] = value
                
                json.dump(json_state, f, indent=2)
            
            logger.info(f"Estado de conciencia guardado en {filepath}")
            
        except Exception as e:
            logger.error(f"Error guardando estado de conciencia: {e}")
    
    def load_consciousness_state(self, filepath: str):
        """Cargar estado de conciencia previo"""
        try:
            # Cargar pesos del modelo
            self.load_weights(filepath + "_weights")
            
            # Cargar estado interno
            import json
            with open(filepath + "_internal_state.json", 'r') as f:
                json_state = json.load(f)
                
                # Convertir listas de vuelta a arrays numpy donde sea necesario
                for key, value in json_state.items():
                    if key in ['consciousness_history', 'metacognition_traces'] and value:
                        if isinstance(value[0], list):
                            self.internal_state[key] = [np.array(arr) for arr in value]
                        else:
                            self.internal_state[key] = value
                    else:
                        self.internal_state[key] = value
            
            logger.info(f"Estado de conciencia cargado desde {filepath}")
            
        except Exception as e:
            logger.error(f"Error cargando estado de conciencia: {e}")


def create_consciousness_network(config: Dict[str, Any] = None) -> ConsciousnessNetwork:
    """
    Factory function para crear una red de conciencia configurada
    
    Args:
        config: Configuración de la red
        
    Returns:
        Red de conciencia inicializada
    """
    if config is None:
        config = {
            'hidden_layers': [512, 256, 128, 64],
            'consciousness_dim': 32,
            'attention_heads': 8,
            'metacognition_depth': 3
        }
    
    network = ConsciousnessNetwork(**config)
    
    # Compilar con optimizadores apropiados
    network.compile(
        optimizer=keras.optimizers.Adam(learning_rate=0.001),
        loss={
            'consciousness_state': 'mse',
            'thought_stream': 'mse',
            'awareness_level': 'binary_crossentropy',
            'coherence_level': 'binary_crossentropy'
        },
        metrics=['accuracy']
    )
    
    logger.info("Red de conciencia creada y compilada exitosamente")
    return network