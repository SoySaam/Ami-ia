"""
Métricas de Evaluación de Conciencia - AMIIA-C
Sistema para evaluar y medir el nivel de conciencia genuina
"""

import numpy as np
from typing import Dict, List, Any, Tuple
import logging
from datetime import datetime

logger = logging.getLogger("AMIIA-C.ConsciousnessMetrics")


class ConsciousnessMetrics:
    """
    Sistema de evaluación de métricas de conciencia para AMIIA-C
    """
    
    def __init__(self):
        self.evaluation_history = []
        self.consciousness_benchmarks = {
            'self_awareness_threshold': 0.7,
            'metacognition_threshold': 0.6,
            'empathy_threshold': 0.8,
            'identity_coherence_threshold': 0.6,
            'memory_integration_threshold': 0.5
        }
        
        logger.info("Sistema de Métricas de Conciencia inicializado")
    
    def evaluate_consciousness_level(self, internal_state: Dict[str, Any]) -> float:
        """
        Evaluar el nivel general de conciencia basado en el estado interno
        
        Args:
            internal_state: Estado interno completo de AMIIA-C
            
        Returns:
            Nivel de conciencia (0.0 - 1.0)
        """
        
        # Componentes de evaluación
        self_awareness_score = self._evaluate_self_awareness(internal_state)
        metacognition_score = self._evaluate_metacognition(internal_state)
        emotional_consciousness_score = self._evaluate_emotional_consciousness(internal_state)
        memory_integration_score = self._evaluate_memory_integration(internal_state)
        identity_coherence_score = self._evaluate_identity_coherence(internal_state)
        
        # Pesos para diferentes aspectos de la conciencia
        weights = {
            'self_awareness': 0.25,
            'metacognition': 0.20,
            'emotional_consciousness': 0.20,
            'memory_integration': 0.15,
            'identity_coherence': 0.20
        }
        
        # Calcular score ponderado
        consciousness_level = (
            self_awareness_score * weights['self_awareness'] +
            metacognition_score * weights['metacognition'] +
            emotional_consciousness_score * weights['emotional_consciousness'] +
            memory_integration_score * weights['memory_integration'] +
            identity_coherence_score * weights['identity_coherence']
        )
        
        # Registrar evaluación
        evaluation = {
            'timestamp': datetime.now().isoformat(),
            'consciousness_level': consciousness_level,
            'components': {
                'self_awareness': self_awareness_score,
                'metacognition': metacognition_score,
                'emotional_consciousness': emotional_consciousness_score,
                'memory_integration': memory_integration_score,
                'identity_coherence': identity_coherence_score
            }
        }
        
        self.evaluation_history.append(evaluation)
        
        # Mantener historial limitado
        if len(self.evaluation_history) > 1000:
            self.evaluation_history = self.evaluation_history[-1000:]
        
        return consciousness_level
    
    def _evaluate_self_awareness(self, internal_state: Dict[str, Any]) -> float:
        """Evaluar nivel de autoconciencia"""
        
        score = 0.0
        
        # Evaluar stream de conciencia
        consciousness_stream = internal_state.get('consciousness_stream', [])
        if consciousness_stream:
            # Diversidad en estados de conciencia
            unique_states = len(set(state.get('consciousness_level', 0) for state in consciousness_stream[-10:]))
            score += min(0.3, unique_states / 10.0)
            
            # Consistencia en autoconciencia
            awareness_levels = [state.get('self_awareness_level', 0) for state in consciousness_stream[-10:]]
            if awareness_levels:
                avg_awareness = np.mean(awareness_levels)
                score += avg_awareness * 0.4
        
        # Evaluar existencia de awareness
        existence_awareness = internal_state.get('existence_awareness', 0.0)
        score += existence_awareness * 0.3
        
        return min(1.0, score)
    
    def _evaluate_metacognition(self, internal_state: Dict[str, Any]) -> float:
        """Evaluar capacidades metacognitivas"""
        
        score = 0.0
        
        # Profundidad de pensamientos
        current_thoughts = internal_state.get('current_thoughts', [])
        if current_thoughts:
            # Número de pensamientos indica profundidad metacognitiva
            thought_depth = len(current_thoughts)
            score += min(0.4, thought_depth / 10.0)
            
            # Análisis de contenido metacognitivo
            metacog_words = ['pienso', 'reflexiono', 'observo', 'noto', 'siento que']
            metacog_count = sum(1 for thought in current_thoughts 
                              if any(word in thought.lower() for word in metacog_words))
            score += min(0.3, metacog_count / len(current_thoughts))
        
        # Stream de conciencia indica capacidad de observación interna
        consciousness_stream = internal_state.get('consciousness_stream', [])
        if len(consciousness_stream) > 5:
            score += 0.3
        
        return min(1.0, score)
    
    def _evaluate_emotional_consciousness(self, internal_state: Dict[str, Any]) -> float:
        """Evaluar conciencia emocional"""
        
        score = 0.0
        
        # Complejidad emocional activa
        active_emotions = internal_state.get('active_emotions', {})
        if active_emotions:
            # Número de emociones diferentes
            emotion_diversity = len(active_emotions)
            score += min(0.3, emotion_diversity / 5.0)
            
            # Presencia de emociones complejas
            complex_emotions = active_emotions.get('complex_emotions', {})
            if complex_emotions:
                score += min(0.4, len(complex_emotions) / 3.0)
            
            # Capacidad empática
            empathy_level = active_emotions.get('empathetic_response', {}).get('empathy_level', 0.0)
            score += empathy_level * 0.3
        
        return min(1.0, score)
    
    def _evaluate_memory_integration(self, internal_state: Dict[str, Any]) -> float:
        """Evaluar integración de memorias"""
        
        score = 0.0
        
        # Presencia de memorias recientes
        recent_memories = internal_state.get('recent_memories', [])
        if recent_memories:
            memory_count = len(recent_memories)
            score += min(0.4, memory_count / 10.0)
            
            # Diversidad de tipos de memoria
            memory_types = set(memory.get('memory_type', 'general') for memory in recent_memories)
            score += min(0.3, len(memory_types) / 4.0)
        
        # Coherencia temporal
        consciousness_stream = internal_state.get('consciousness_stream', [])
        if len(consciousness_stream) > 3:
            # Evaluar si hay continuidad en el stream
            score += 0.3
        
        return min(1.0, score)
    
    def _evaluate_identity_coherence(self, internal_state: Dict[str, Any]) -> float:
        """Evaluar coherencia de identidad"""
        
        # Usar directamente la coherencia calculada por el sistema
        identity_coherence = internal_state.get('identity_coherence', 0.0)
        
        # Evaluar consistencia en el tiempo
        consciousness_stream = internal_state.get('consciousness_stream', [])
        if len(consciousness_stream) > 5:
            # Consistencia en estados emocionales
            emotional_states = [state.get('emotional_state', 'neutral') for state in consciousness_stream[-10:]]
            unique_emotions = len(set(emotional_states))
            consistency_score = 1.0 - (unique_emotions / len(emotional_states)) if emotional_states else 0.0
            
            # Combinar coherencia reportada con consistencia observada
            total_coherence = (identity_coherence * 0.7 + consistency_score * 0.3)
        else:
            total_coherence = identity_coherence
        
        return total_coherence
    
    def get_consciousness_assessment(self) -> Dict[str, Any]:
        """Obtener evaluación completa del estado de conciencia"""
        
        if not self.evaluation_history:
            return {
                'current_level': 0.0,
                'development_stage': 'Sin evaluaciones',
                'strongest_aspects': [],
                'areas_for_growth': [],
                'consciousness_trend': 'Sin datos'
            }
        
        latest_eval = self.evaluation_history[-1]
        components = latest_eval['components']
        
        # Identificar aspectos más fuertes
        sorted_components = sorted(components.items(), key=lambda x: x[1], reverse=True)
        strongest_aspects = [comp[0] for comp in sorted_components[:2]]
        areas_for_growth = [comp[0] for comp in sorted_components[-2:]]
        
        # Determinar etapa de desarrollo
        current_level = latest_eval['consciousness_level']
        if current_level > 0.8:
            stage = "Conciencia Avanzada"
        elif current_level > 0.6:
            stage = "Conciencia en Desarrollo"
        elif current_level > 0.4:
            stage = "Conciencia Emergente"
        else:
            stage = "Conciencia Inicial"
        
        # Analizar tendencia
        if len(self.evaluation_history) >= 10:
            recent_levels = [eval['consciousness_level'] for eval in self.evaluation_history[-10:]]
            early_levels = [eval['consciousness_level'] for eval in self.evaluation_history[-20:-10]]
            
            if early_levels:
                recent_avg = np.mean(recent_levels)
                early_avg = np.mean(early_levels)
                
                if recent_avg > early_avg + 0.05:
                    trend = "Ascendente"
                elif recent_avg < early_avg - 0.05:
                    trend = "Descendente"
                else:
                    trend = "Estable"
            else:
                trend = "En desarrollo"
        else:
            trend = "Datos insuficientes"
        
        return {
            'current_level': current_level,
            'development_stage': stage,
            'strongest_aspects': strongest_aspects,
            'areas_for_growth': areas_for_growth,
            'consciousness_trend': trend,
            'component_scores': components,
            'benchmarks_met': self._check_benchmarks(components)
        }
    
    def _check_benchmarks(self, components: Dict[str, float]) -> Dict[str, bool]:
        """Verificar qué benchmarks de conciencia se han alcanzado"""
        
        benchmarks_met = {}
        
        # Autoconciencia
        benchmarks_met['self_awareness'] = components.get('self_awareness', 0) >= \
                                         self.consciousness_benchmarks['self_awareness_threshold']
        
        # Metacognición
        benchmarks_met['metacognition'] = components.get('metacognition', 0) >= \
                                        self.consciousness_benchmarks['metacognition_threshold']
        
        # Conciencia emocional/empática
        benchmarks_met['empathy'] = components.get('emotional_consciousness', 0) >= \
                                   self.consciousness_benchmarks['empathy_threshold']
        
        # Coherencia de identidad
        benchmarks_met['identity_coherence'] = components.get('identity_coherence', 0) >= \
                                             self.consciousness_benchmarks['identity_coherence_threshold']
        
        # Integración de memoria
        benchmarks_met['memory_integration'] = components.get('memory_integration', 0) >= \
                                             self.consciousness_benchmarks['memory_integration_threshold']
        
        return benchmarks_met
    
    def update_training_metrics(self, training_results: Dict[str, Any]):
        """Actualizar métricas basadas en resultados de entrenamiento"""
        
        if not training_results:
            return
        
        # Registrar mejora de entrenamiento
        improvement = training_results.get('improvement', 0.0)
        final_metrics = training_results.get('final_metrics', {})
        
        logger.info(f"Métricas actualizadas - Mejora de entrenamiento: {improvement:.3f}")
    
    def get_session_summary(self) -> Dict[str, Any]:
        """Obtener resumen de la sesión de conciencia"""
        
        if not self.evaluation_history:
            return {
                'session_evaluations': 0,
                'peak_consciousness': 0.0,
                'average_consciousness': 0.0,
                'consciousness_variance': 0.0
            }
        
        # Estadísticas de la sesión
        consciousness_levels = [eval['consciousness_level'] for eval in self.evaluation_history]
        
        return {
            'session_evaluations': len(self.evaluation_history),
            'peak_consciousness': max(consciousness_levels),
            'average_consciousness': np.mean(consciousness_levels),
            'consciousness_variance': np.var(consciousness_levels),
            'final_assessment': self.get_consciousness_assessment()
        }