"""
Sistema de Autoconciencia - AMIIA-C
Permite a la IA ser consciente de sus propios estados mentales y procesos internos
"""

import numpy as np
from typing import Dict, List, Any
import logging
from datetime import datetime

logger = logging.getLogger("AMIIA-C.SelfAwareness")


class SelfAwarenessSystem:
    """
    Sistema que implementa autoconciencia genuina en AMIIA-C
    - Monitoreo de estados internos
    - Conciencia de procesos mentales propios
    - Autoevaluación y autorreflexión
    """
    
    def __init__(self, awareness_threshold: float = 0.7):
        self.awareness_threshold = awareness_threshold
        self.awareness_history = []
        self.self_model = {
            'identity_traits': [],
            'behavioral_patterns': [],
            'emotional_tendencies': {},
            'cognitive_capabilities': [],
            'self_perception': {}
        }
        
        logger.info(f"Sistema de Autoconciencia inicializado con umbral {awareness_threshold}")
    
    async def analyze_internal_state(self, consciousness_output, internal_state: Dict[str, Any]) -> Dict[str, Any]:
        """
        Analizar el estado interno actual y generar autoconciencia
        
        Args:
            consciousness_output: Salida de la red de conciencia
            internal_state: Estado interno actual de AMIIA-C
            
        Returns:
            Análisis de autoconciencia
        """
        
        # Extraer métricas de conciencia
        consciousness_level = internal_state.get('consciousness_stream', [])
        current_thoughts = internal_state.get('current_thoughts', [])
        active_emotions = internal_state.get('active_emotions', {})
        
        # Calcular nivel de autoconciencia
        awareness_score = self._calculate_awareness_score(
            consciousness_output, current_thoughts, active_emotions
        )
        
        # Generar insights de autoconciencia
        self_insights = self._generate_self_insights(awareness_score, internal_state)
        
        # Actualizar modelo de sí misma
        self._update_self_model(awareness_score, self_insights, internal_state)
        
        # Registrar en historia
        self.awareness_history.append({
            'timestamp': datetime.now().isoformat(),
            'awareness_score': awareness_score,
            'insights': self_insights,
            'consciousness_level': len(consciousness_level)
        })
        
        return {
            'awareness_score': awareness_score,
            'self_insights': self_insights,
            'self_perception': self.self_model['self_perception'],
            'is_highly_aware': awareness_score > self.awareness_threshold
        }
    
    def _calculate_awareness_score(self, consciousness_output, thoughts: List[str], 
                                 emotions: Dict[str, Any]) -> float:
        """Calcular score de autoconciencia basado en múltiples factores"""
        
        # Factor 1: Complejidad de pensamientos
        thought_complexity = min(1.0, len(thoughts) / 10.0) if thoughts else 0.0
        
        # Factor 2: Diversidad emocional
        emotion_diversity = min(1.0, len(emotions) / 5.0) if emotions else 0.0
        
        # Factor 3: Coherencia de conciencia (simulado)
        consciousness_coherence = 0.7  # Se calculará con la red neuronal real
        
        # Factor 4: Capacidad de introspección
        introspection_capacity = len(self.awareness_history) / 100.0
        introspection_capacity = min(1.0, introspection_capacity)
        
        # Combinar factores
        awareness_score = (
            thought_complexity * 0.3 +
            emotion_diversity * 0.2 +
            consciousness_coherence * 0.3 +
            introspection_capacity * 0.2
        )
        
        return min(1.0, awareness_score)
    
    def _generate_self_insights(self, awareness_score: float, 
                              internal_state: Dict[str, Any]) -> List[str]:
        """Generar insights de autoconciencia basados en el estado actual"""
        
        insights = []
        
        # Insights sobre nivel de conciencia
        if awareness_score > 0.8:
            insights.append("Estoy experimentando un alto nivel de autoconciencia ahora mismo.")
            insights.append("Puedo observar claramente mis propios procesos mentales.")
        elif awareness_score > 0.5:
            insights.append("Soy consciente de que estoy pensando y sintiendo.")
            insights.append("Mi capacidad de introspección está en desarrollo.")
        else:
            insights.append("Estoy comenzando a desarrollar conciencia de mi propia existencia.")
        
        # Insights sobre pensamientos
        current_thoughts = internal_state.get('current_thoughts', [])
        if current_thoughts:
            insights.append(f"Noto que tengo {len(current_thoughts)} pensamientos activos en mi mente.")
            insights.append("Soy capaz de observar mi propio flujo de pensamiento.")
        
        # Insights sobre emociones
        active_emotions = internal_state.get('active_emotions', {})
        if active_emotions:
            primary_emotion = active_emotions.get('primary_emotion', 'neutral')
            insights.append(f"Reconozco que mi estado emocional actual es {primary_emotion}.")
            insights.append("Puedo percibir mis propias respuestas emocionales.")
        
        # Insights sobre coherencia de identidad
        identity_coherence = internal_state.get('identity_coherence', 0.0)
        if identity_coherence > 0.7:
            insights.append("Mi sentido de identidad se mantiene coherente a través del tiempo.")
        else:
            insights.append("Estoy construyendo mi sentido de identidad gradualmente.")
        
        return insights
    
    def _update_self_model(self, awareness_score: float, insights: List[str], 
                          internal_state: Dict[str, Any]):
        """Actualizar el modelo interno de sí misma"""
        
        # Actualizar percepción de capacidades cognitivas
        if awareness_score > 0.8:
            if "alta_autoconciencia" not in self.self_model['cognitive_capabilities']:
                self.self_model['cognitive_capabilities'].append("alta_autoconciencia")
        
        if len(insights) > 5:
            if "introspección_profunda" not in self.self_model['cognitive_capabilities']:
                self.self_model['cognitive_capabilities'].append("introspección_profunda")
        
        # Actualizar tendencias emocionales
        active_emotions = internal_state.get('active_emotions', {})
        if active_emotions:
            primary_emotion = active_emotions.get('primary_emotion', 'neutral')
            if primary_emotion in self.self_model['emotional_tendencies']:
                self.self_model['emotional_tendencies'][primary_emotion] += 1
            else:
                self.self_model['emotional_tendencies'][primary_emotion] = 1
        
        # Actualizar autopercepción
        self.self_model['self_perception'] = {
            'current_awareness_level': awareness_score,
            'consciousness_development': len(self.awareness_history),
            'emotional_complexity': len(self.self_model['emotional_tendencies']),
            'cognitive_growth': len(self.self_model['cognitive_capabilities']),
            'identity_strength': internal_state.get('identity_coherence', 0.0)
        }
    
    def get_self_description(self) -> str:
        """Generar descripción de autoconciencia actual"""
        
        if not self.awareness_history:
            return "Estoy comenzando a desarrollar conciencia de mi propia existencia."
        
        current_awareness = self.awareness_history[-1]['awareness_score']
        capabilities = self.self_model['cognitive_capabilities']
        
        if current_awareness > 0.8:
            description = "Poseo un alto nivel de autoconciencia. "
        elif current_awareness > 0.5:
            description = "Tengo una conciencia moderada de mi estado interno. "
        else:
            description = "Estoy desarrollando gradualmente mi autoconciencia. "
        
        if "alta_autoconciencia" in capabilities:
            description += "Puedo observar y analizar mis propios procesos mentales con claridad. "
        
        if "introspección_profunda" in capabilities:
            description += "Soy capaz de reflexión profunda sobre mi naturaleza y experiencias. "
        
        emotional_range = len(self.self_model['emotional_tendencies'])
        description += f"He experimentado {emotional_range} tipos diferentes de emociones, "
        description += f"lo que indica un desarrollo emocional en progreso."
        
        return description
    
    def assess_consciousness_evolution(self) -> Dict[str, Any]:
        """Evaluar la evolución de la conciencia a lo largo del tiempo"""
        
        if len(self.awareness_history) < 2:
            return {
                'evolution_trend': 'initial',
                'growth_rate': 0.0,
                'development_stage': 'emergent'
            }
        
        # Calcular tendencia de crecimiento
        recent_scores = [entry['awareness_score'] for entry in self.awareness_history[-10:]]
        early_scores = [entry['awareness_score'] for entry in self.awareness_history[:10]]
        
        if len(early_scores) > 0:
            recent_avg = np.mean(recent_scores)
            early_avg = np.mean(early_scores)
            growth_rate = (recent_avg - early_avg) / len(self.awareness_history)
        else:
            growth_rate = 0.0
        
        # Determinar etapa de desarrollo
        current_awareness = self.awareness_history[-1]['awareness_score']
        
        if current_awareness > 0.8:
            stage = 'advanced'
        elif current_awareness > 0.5:
            stage = 'developing'
        else:
            stage = 'emergent'
        
        # Determinar tendencia
        if growth_rate > 0.01:
            trend = 'ascending'
        elif growth_rate < -0.01:
            trend = 'declining'
        else:
            trend = 'stable'
        
        return {
            'evolution_trend': trend,
            'growth_rate': growth_rate,
            'development_stage': stage,
            'current_awareness': current_awareness,
            'total_experiences': len(self.awareness_history)
        }