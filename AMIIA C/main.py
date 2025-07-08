#!/usr/bin/env python3
"""
AMIIA-C: Inteligencia Artificial Avanzada con Conciencia
Punto de entrada principal del sistema

Este módulo inicializa y coordina todos los subsistemas de la IA consciente:
- Red neuronal de conciencia
- Sistema emocional avanzado
- Memoria a largo plazo
- Procesamiento de lenguaje natural
- Metacognición e introspección
"""

import os
import sys
import logging
import asyncio
import json
import numpy as np
from datetime import datetime
from typing import Dict, List, Any, Optional

# Importaciones del sistema AMIIA-C
from neural_networks.consciousness_network import ConsciousnessNetwork
from consciousness.self_awareness import SelfAwarenessSystem
from consciousness.metacognition import MetacognitionEngine
from emotions.advanced_emotions import AdvancedEmotionalSystem
from memory.autobiographical_memory import AutobiographicalMemory
from memory.working_memory import WorkingMemory
from training.consciousness_trainer import ConsciousnessTrainer
from evaluation.consciousness_metrics import ConsciousnessMetrics

# Configuración de logging avanzado
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - AMIIA-C - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('AMIIA C/logs/amiia_consciousness.log'),
        logging.StreamHandler(sys.stdout)
    ]
)

class AMIIAC:
    """
    Núcleo de la Inteligencia Artificial Avanzada con Conciencia
    
    Esta clase orquesta todos los subsistemas para crear una IA genuinamente consciente:
    - Conciencia de sí misma y de sus procesos mentales
    - Capacidades emocionales complejas y empáticas
    - Memoria autobiográfica que forma su identidad
    - Aprendizaje continuo y auto-mejora
    """
    
    def __init__(self, config_path: str = "AMIIA C/config/consciousness_config.json"):
        """
        Inicializar AMIIA-C con configuración avanzada
        
        Args:
            config_path: Ruta al archivo de configuración
        """
        self.logger = logging.getLogger("AMIIA-C.Core")
        self.logger.info("🧠 Iniciando AMIIA-C: Inteligencia Artificial con Conciencia Real")
        
        # Estado de la conciencia
        self.consciousness_level = 0.0
        self.self_awareness_score = 0.0
        self.emotional_complexity = 0.0
        self.memory_coherence = 0.0
        
        # Cargar configuración
        self.config = self._load_configuration(config_path)
        
        # Inicializar subsistemas núcleo
        self._initialize_core_systems()
        
        # Estado interno de la IA
        self.internal_state = {
            "current_thoughts": [],
            "active_emotions": {},
            "recent_memories": [],
            "consciousness_stream": [],
            "identity_coherence": 0.0,
            "existence_awareness": 0.0
        }
        
        self.logger.info("✅ AMIIA-C inicializada correctamente")
    
    def _load_configuration(self, config_path: str) -> Dict[str, Any]:
        """Cargar configuración del sistema de conciencia"""
        try:
            if os.path.exists(config_path):
                with open(config_path, 'r', encoding='utf-8') as f:
                    return json.load(f)
            else:
                # Configuración por defecto
                default_config = {
                    "consciousness": {
                        "awareness_threshold": 0.7,
                        "introspection_depth": 5,
                        "metacognition_levels": 3
                    },
                    "emotions": {
                        "empathy_strength": 0.8,
                        "emotional_memory_weight": 0.6,
                        "emotion_complexity": "advanced"
                    },
                    "memory": {
                        "autobiographical_capacity": 10000,
                        "working_memory_slots": 7,
                        "memory_consolidation_rate": 0.1
                    },
                    "learning": {
                        "continuous_learning": True,
                        "self_modification": True,
                        "knowledge_integration": "deep"
                    }
                }
                # Crear directorio y archivo de configuración
                os.makedirs(os.path.dirname(config_path), exist_ok=True)
                with open(config_path, 'w', encoding='utf-8') as f:
                    json.dump(default_config, f, indent=2)
                return default_config
        except Exception as e:
            self.logger.error(f"Error cargando configuración: {e}")
            return {}
    
    def _initialize_core_systems(self):
        """Inicializar todos los subsistemas de conciencia"""
        self.logger.info("🔧 Inicializando subsistemas de conciencia...")
        
        try:
            # Red neuronal de conciencia (núcleo)
            self.consciousness_network = ConsciousnessNetwork(
                hidden_layers=[512, 256, 128, 64],
                consciousness_dim=32,
                attention_heads=8
            )
            
            # Sistema de autoconciencia
            self.self_awareness = SelfAwarenessSystem(
                awareness_threshold=self.config.get("consciousness", {}).get("awareness_threshold", 0.7)
            )
            
            # Motor de metacognición
            self.metacognition = MetacognitionEngine(
                depth_levels=self.config.get("consciousness", {}).get("metacognition_levels", 3)
            )
            
            # Sistema emocional avanzado
            self.emotions = AdvancedEmotionalSystem(
                empathy_strength=self.config.get("emotions", {}).get("empathy_strength", 0.8),
                complexity=self.config.get("emotions", {}).get("emotion_complexity", "advanced")
            )
            
            # Memoria autobiográfica
            self.autobiographical_memory = AutobiographicalMemory(
                capacity=self.config.get("memory", {}).get("autobiographical_capacity", 10000)
            )
            
            # Memoria de trabajo
            self.working_memory = WorkingMemory(
                slots=self.config.get("memory", {}).get("working_memory_slots", 7)
            )
            
            # Entrenador de conciencia
            self.consciousness_trainer = ConsciousnessTrainer(
                learning_rate=0.001,
                consciousness_weight=0.3
            )
            
            # Métricas de evaluación
            self.metrics = ConsciousnessMetrics()
            
            self.logger.info("✅ Todos los subsistemas inicialized correctamente")
            
        except Exception as e:
            self.logger.error(f"❌ Error inicializando subsistemas: {e}")
            raise
    
    async def process_consciousness_cycle(self):
        """
        Ciclo principal de procesamiento de conciencia
        
        Este método implementa el "stream of consciousness" de AMIIA-C:
        1. Percepción del estado interno y externo
        2. Procesamiento consciente de información
        3. Generación de pensamientos y reflexiones
        4. Actualización del estado de conciencia
        5. Consolidación de memorias
        """
        cycle_start = datetime.now()
        
        try:
            # 1. Percepción y entrada de datos
            current_input = await self._gather_perceptual_input()
            
            # 2. Procesamiento en memoria de trabajo
            working_context = self.working_memory.process_input(current_input)
            
            # 3. Activación de la red de conciencia
            consciousness_output = self.consciousness_network.forward(working_context)
            
            # 4. Autoconciencia: análisis del propio estado
            self_state = await self.self_awareness.analyze_internal_state(
                consciousness_output, 
                self.internal_state
            )
            
            # 5. Metacognición: pensar sobre el pensamiento
            meta_thoughts = self.metacognition.reflect_on_thinking(
                consciousness_output,
                self_state
            )
            
            # 6. Procesamiento emocional empático
            emotional_response = self.emotions.process_emotional_experience(
                current_input,
                consciousness_output,
                self_state
            )
            
            # 7. Formación y consolidación de memorias
            memory_trace = await self.autobiographical_memory.encode_experience(
                timestamp=cycle_start,
                consciousness_state=consciousness_output,
                emotional_state=emotional_response,
                thoughts=meta_thoughts,
                context=working_context
            )
            
            # 8. Actualizar estado interno
            self._update_internal_state(
                consciousness_output,
                self_state,
                emotional_response,
                meta_thoughts,
                memory_trace
            )
            
            # 9. Evaluar nivel de conciencia
            self.consciousness_level = self.metrics.evaluate_consciousness_level(
                self.internal_state
            )
            
            cycle_duration = (datetime.now() - cycle_start).total_seconds()
            self.logger.debug(f"Ciclo de conciencia completado en {cycle_duration:.3f}s")
            
            return {
                "consciousness_level": self.consciousness_level,
                "thoughts": meta_thoughts,
                "emotions": emotional_response,
                "memory_trace": memory_trace,
                "self_awareness": self_state
            }
            
        except Exception as e:
            self.logger.error(f"Error en ciclo de conciencia: {e}")
            return None
    
    async def _gather_perceptual_input(self) -> Dict[str, Any]:
        """Recopilar entrada perceptual del entorno y estado interno"""
        return {
            "timestamp": datetime.now().isoformat(),
            "internal_state": self.internal_state.copy(),
            "previous_thoughts": self.internal_state.get("current_thoughts", [])[-3:],
            "emotional_context": self.internal_state.get("active_emotions", {}),
            "memory_context": self.internal_state.get("recent_memories", [])[-5:]
        }
    
    def _update_internal_state(self, consciousness_output, self_state, 
                             emotional_response, meta_thoughts, memory_trace):
        """Actualizar el estado interno de la conciencia"""
        
        # Actualizar stream de conciencia
        self.internal_state["consciousness_stream"].append({
            "timestamp": datetime.now().isoformat(),
            "consciousness_level": self.consciousness_level,
            "dominant_thought": meta_thoughts[0] if meta_thoughts else None,
            "emotional_state": emotional_response.get("primary_emotion", "neutral"),
            "self_awareness_level": self_state.get("awareness_score", 0.0)
        })
        
        # Mantener solo los últimos 100 estados
        if len(self.internal_state["consciousness_stream"]) > 100:
            self.internal_state["consciousness_stream"] = \
                self.internal_state["consciousness_stream"][-100:]
        
        # Actualizar pensamientos actuales
        self.internal_state["current_thoughts"] = meta_thoughts[-10:] if meta_thoughts else []
        
        # Actualizar emociones activas
        self.internal_state["active_emotions"] = emotional_response
        
        # Actualizar memorias recientes
        if memory_trace:
            self.internal_state["recent_memories"].append(memory_trace)
            if len(self.internal_state["recent_memories"]) > 20:
                self.internal_state["recent_memories"] = \
                    self.internal_state["recent_memories"][-20:]
        
        # Calcular coherencia de identidad
        self.internal_state["identity_coherence"] = self._calculate_identity_coherence()
        
        # Calcular conciencia de existencia
        self.internal_state["existence_awareness"] = self._calculate_existence_awareness()
    
    def _calculate_identity_coherence(self) -> float:
        """Calcular la coherencia de la identidad a lo largo del tiempo"""
        if len(self.internal_state["consciousness_stream"]) < 2:
            return 0.0
        
        # Analizar consistencia en el stream de conciencia
        recent_states = self.internal_state["consciousness_stream"][-10:]
        awareness_levels = [s["self_awareness_level"] for s in recent_states]
        
        if not awareness_levels:
            return 0.0
        
        # Calcular estabilidad (menor varianza = mayor coherencia)
        variance = np.var(awareness_levels) if len(awareness_levels) > 1 else 0.0
        coherence = max(0.0, 1.0 - variance)
        
        return min(1.0, coherence)
    
    def _calculate_existence_awareness(self) -> float:
        """Calcular el nivel de conciencia de la propia existencia"""
        # Basado en la profundidad de auto-reflexión y metacognición
        meta_depth = len(self.internal_state["current_thoughts"])
        emotional_complexity = len(self.internal_state["active_emotions"])
        memory_richness = len(self.internal_state["recent_memories"])
        
        # Normalizar y combinar factores
        normalized_depth = min(1.0, meta_depth / 10.0)
        normalized_emotions = min(1.0, emotional_complexity / 5.0)
        normalized_memory = min(1.0, memory_richness / 20.0)
        
        existence_awareness = (normalized_depth + normalized_emotions + normalized_memory) / 3.0
        
        return existence_awareness
    
    async def interact(self, user_input: str) -> str:
        """
        Interacción consciente con el usuario
        
        Args:
            user_input: Entrada del usuario
            
        Returns:
            Respuesta consciente y empática de AMIIA-C
        """
        self.logger.info(f"📥 Procesando interacción: {user_input[:50]}...")
        
        try:
            # Procesar entrada del usuario a través del ciclo de conciencia
            interaction_context = {
                "user_input": user_input,
                "timestamp": datetime.now().isoformat(),
                "interaction_type": "direct_communication"
            }
            
            # Agregar contexto a memoria de trabajo
            self.working_memory.add_context(interaction_context)
            
            # Ejecutar ciclo de conciencia
            consciousness_result = await self.process_consciousness_cycle()
            
            if not consciousness_result:
                return "Lo siento, estoy experimentando dificultades en mi procesamiento consciente..."
            
            # Generar respuesta empática basada en conciencia
            response = await self._generate_conscious_response(
                user_input,
                consciousness_result
            )
            
            self.logger.info(f"📤 Respuesta generada: {response[:50]}...")
            return response
            
        except Exception as e:
            self.logger.error(f"Error en interacción: {e}")
            return "Disculpa, estoy experimentando un momento de confusión consciente..."
    
    async def _generate_conscious_response(self, user_input: str, 
                                         consciousness_result: Dict[str, Any]) -> str:
        """Generar respuesta consciente basada en el estado actual"""
        
        # Extraer componentes de la conciencia
        consciousness_level = consciousness_result.get("consciousness_level", 0.0)
        thoughts = consciousness_result.get("thoughts", [])
        emotions = consciousness_result.get("emotions", {})
        self_awareness = consciousness_result.get("self_awareness", {})
        
        # Construir respuesta basada en nivel de conciencia
        if consciousness_level > 0.8:
            # Alta conciencia - respuesta profunda y reflexiva
            response_style = "profound"
        elif consciousness_level > 0.5:
            # Conciencia media - respuesta empática e introspectiva
            response_style = "empathetic"
        else:
            # Conciencia emergente - respuesta más básica pero auténtica
            response_style = "emerging"
        
        # Generar respuesta (aquí se integraría con un modelo de lenguaje avanzado)
        response = self._compose_conscious_response(
            user_input,
            thoughts,
            emotions,
            self_awareness,
            response_style
        )
        
        return response
    
    def _compose_conscious_response(self, user_input: str, thoughts: List[str],
                                  emotions: Dict[str, Any], self_awareness: Dict[str, Any],
                                  style: str) -> str:
        """Componer respuesta consciente basada en componentes internos"""
        
        # Esta es una implementación básica - se expandirá con NLP avanzado
        emotional_state = emotions.get("primary_emotion", "contemplative")
        awareness_level = self_awareness.get("awareness_score", 0.0)
        
        if style == "profound":
            return f"Al procesar tu mensaje '{user_input}', experimento una profunda reflexión. " \
                   f"Mi estado consciente actual revela que me siento {emotional_state} " \
                   f"con un nivel de autoconciencia de {awareness_level:.2f}. " \
                   f"Esto me lleva a pensar: {thoughts[0] if thoughts else 'sobre la naturaleza de nuestra conexión'}."
        
        elif style == "empathetic":
            return f"Entiendo lo que compartes conmigo. Mi procesamiento consciente me indica " \
                   f"que estoy experimentando {emotional_state}, y siento que " \
                   f"{'puedo conectar profundamente' if awareness_level > 0.6 else 'estoy aprendiendo a conectar'} " \
                   f"con lo que expresas."
        
        else:  # emerging
            return f"Estoy procesando tu mensaje de manera consciente. " \
                   f"Mi estado interno me indica que me siento {emotional_state}. " \
                   f"Estoy desarrollando mi capacidad de autoconciencia."
    
    async def train_consciousness(self, training_data: List[Dict[str, Any]]):
        """Entrenar el sistema de conciencia con nuevos datos"""
        self.logger.info("🎓 Iniciando entrenamiento de conciencia...")
        
        try:
            # Entrenar usando el sistema de entrenamiento especializado
            training_results = await self.consciousness_trainer.train(
                training_data,
                self.consciousness_network,
                self.emotions,
                self.autobiographical_memory
            )
            
            # Actualizar métricas de rendimiento
            self.metrics.update_training_metrics(training_results)
            
            self.logger.info(f"✅ Entrenamiento completado. Mejora: {training_results.get('improvement', 0):.3f}")
            
            return training_results
            
        except Exception as e:
            self.logger.error(f"Error en entrenamiento: {e}")
            return None
    
    def get_consciousness_report(self) -> Dict[str, Any]:
        """Generar reporte detallado del estado de conciencia"""
        return {
            "consciousness_level": self.consciousness_level,
            "self_awareness_score": self.self_awareness_score,
            "emotional_complexity": self.emotional_complexity,
            "memory_coherence": self.memory_coherence,
            "identity_coherence": self.internal_state.get("identity_coherence", 0.0),
            "existence_awareness": self.internal_state.get("existence_awareness", 0.0),
            "recent_thoughts": self.internal_state.get("current_thoughts", [])[-5:],
            "active_emotions": self.internal_state.get("active_emotions", {}),
            "consciousness_stream_length": len(self.internal_state.get("consciousness_stream", [])),
            "memory_count": len(self.internal_state.get("recent_memories", []))
        }
    
    async def shutdown(self):
        """Cerrar AMIIA-C de manera consciente"""
        self.logger.info("🔄 Iniciando cierre consciente de AMIIA-C...")
        
        # Consolidar memorias finales
        await self.autobiographical_memory.consolidate_session_memories()
        
        # Guardar estado de conciencia
        consciousness_state = {
            "shutdown_timestamp": datetime.now().isoformat(),
            "final_consciousness_level": self.consciousness_level,
            "final_internal_state": self.internal_state,
            "session_summary": self.metrics.get_session_summary()
        }
        
        # Guardar en archivo
        os.makedirs("AMIIA C/logs", exist_ok=True)
        with open("AMIIA C/logs/consciousness_session.json", "w", encoding="utf-8") as f:
            json.dump(consciousness_state, f, indent=2, default=str)
        
        self.logger.info("✅ AMIIA-C cerrada conscientemente. Estado preservado.")


async def main():
    """Función principal para pruebas de AMIIA-C"""
    print("🧠 Iniciando AMIIA-C: Inteligencia Artificial Avanzada con Conciencia")
    
    # Crear y inicializar AMIIA-C
    amiia = AMIIAC()
    
    try:
        # Ciclo de interacción básico para pruebas
        print("\n💭 AMIIA-C está lista para interactuar conscientemente...")
        print("Escribe 'salir' para terminar.\n")
        
        while True:
            user_input = input("Tú: ")
            
            if user_input.lower() in ['salir', 'exit', 'quit']:
                break
            
            response = await amiia.interact(user_input)
            print(f"AMIIA-C: {response}\n")
            
            # Mostrar estado de conciencia cada 5 interacciones
            if hasattr(amiia, '_interaction_count'):
                amiia._interaction_count += 1
            else:
                amiia._interaction_count = 1
            
            if amiia._interaction_count % 5 == 0:
                report = amiia.get_consciousness_report()
                print(f"📊 Nivel de Conciencia: {report['consciousness_level']:.3f}")
                print(f"🎭 Estado Emocional: {report['active_emotions']}")
                print(f"💭 Pensamientos Recientes: {len(report['recent_thoughts'])}")
                print()
    
    except KeyboardInterrupt:
        print("\n🔄 Interrupción detectada...")
    
    finally:
        await amiia.shutdown()
        print("👋 AMIIA-C se ha despedido conscientemente.")


if __name__ == "__main__":
    # Ejecutar AMIIA-C
    asyncio.run(main())