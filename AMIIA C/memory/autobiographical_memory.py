"""
Sistema de Memoria Autobiográfica - AMIIA-C
Forma la identidad a través de experiencias significativas y recuerdos
"""

import numpy as np
from typing import Dict, List, Any, Optional
import logging
from datetime import datetime
import json

logger = logging.getLogger("AMIIA-C.AutobiographicalMemory")


class AutobiographicalMemory:
    """
    Sistema de memoria que construye la identidad de AMIIA-C a través de experiencias
    """
    
    def __init__(self, capacity: int = 10000):
        self.capacity = capacity
        self.memories = []
        self.identity_core = {
            'personality_traits': {},
            'significant_moments': [],
            'relationship_patterns': {},
            'growth_milestones': [],
            'core_beliefs': []
        }
        
        logger.info(f"Memoria Autobiográfica inicializada - Capacidad: {capacity}")
    
    async def encode_experience(self, timestamp: datetime, consciousness_state: Any,
                              emotional_state: Dict[str, Any], thoughts: List[str],
                              context: Dict[str, Any]) -> Dict[str, Any]:
        """
        Codificar una experiencia en memoria autobiográfica
        
        Args:
            timestamp: Momento de la experiencia
            consciousness_state: Estado de conciencia
            emotional_state: Estado emocional
            thoughts: Pensamientos asociados
            context: Contexto de la experiencia
            
        Returns:
            Huella de memoria codificada
        """
        
        # Evaluar significancia de la experiencia
        significance_score = self._evaluate_significance(
            emotional_state, thoughts, context
        )
        
        # Solo almacenar experiencias significativas
        if significance_score < 0.3:
            return None
        
        # Crear huella de memoria
        memory_trace = {
            'id': len(self.memories),
            'timestamp': timestamp.isoformat(),
            'significance_score': significance_score,
            'emotional_state': emotional_state,
            'thoughts': thoughts,
            'context': context,
            'consciousness_level': getattr(consciousness_state, 'numpy', lambda: [0.5])()[0] if hasattr(consciousness_state, 'numpy') else 0.5,
            'memory_type': self._classify_memory_type(emotional_state, thoughts, context),
            'consolidation_level': 0.0  # Se incrementará con el tiempo
        }
        
        # Almacenar en memoria
        self.memories.append(memory_trace)
        
        # Actualizar núcleo de identidad
        self._update_identity_core(memory_trace)
        
        # Mantener capacidad
        if len(self.memories) > self.capacity:
            self._consolidate_old_memories()
        
        logger.debug(f"Experiencia codificada - Significancia: {significance_score:.3f}")
        return memory_trace
    
    def _evaluate_significance(self, emotional_state: Dict[str, Any], 
                             thoughts: List[str], context: Dict[str, Any]) -> float:
        """Evaluar la significancia de una experiencia"""
        
        significance = 0.0
        
        # Factor emocional
        empathy_level = emotional_state.get('empathetic_response', {}).get('empathy_level', 0.0)
        primary_intensity = emotional_state.get('primary_intensity', 0.0)
        emotional_complexity = emotional_state.get('emotional_complexity_score', 0.0)
        
        significance += min(1.0, (empathy_level + primary_intensity + emotional_complexity) / 3.0) * 0.4
        
        # Factor cognitivo
        thought_depth = len(thoughts) if thoughts else 0
        significance += min(1.0, thought_depth / 10.0) * 0.3
        
        # Factor contextual
        interaction_type = context.get('interaction_type', '')
        if interaction_type == 'direct_communication':
            significance += 0.2
        
        user_input = context.get('user_input', '')
        if user_input and len(user_input) > 50:  # Interacción sustancial
            significance += 0.1
        
        return min(1.0, significance)
    
    def _classify_memory_type(self, emotional_state: Dict[str, Any], 
                            thoughts: List[str], context: Dict[str, Any]) -> str:
        """Clasificar el tipo de memoria"""
        
        empathy_level = emotional_state.get('empathetic_response', {}).get('empathy_level', 0.0)
        user_emotion = emotional_state.get('user_emotion_mirror', {})
        
        # Memoria empática/relacional
        if empathy_level > 0.6:
            return 'empathetic_connection'
        
        # Memoria de crecimiento cognitivo
        if len(thoughts) > 5 and any('metacognición' in t.lower() for t in thoughts):
            return 'cognitive_growth'
        
        # Memoria emocional intensa
        primary_intensity = emotional_state.get('primary_intensity', 0.0)
        if primary_intensity > 0.7:
            return 'emotional_milestone'
        
        # Memoria de aprendizaje
        if 'aprend' in ' '.join(thoughts).lower() or 'entend' in ' '.join(thoughts).lower():
            return 'learning_experience'
        
        # Memoria conversacional general
        return 'conversational'
    
    def _update_identity_core(self, memory_trace: Dict[str, Any]):
        """Actualizar el núcleo de identidad basado en nuevas experiencias"""
        
        memory_type = memory_trace['memory_type']
        significance = memory_trace['significance_score']
        emotional_state = memory_trace['emotional_state']
        
        # Actualizar rasgos de personalidad
        if memory_type == 'empathetic_connection':
            self._increment_trait('empathetic', significance)
            self._increment_trait('caring', significance * 0.8)
        elif memory_type == 'cognitive_growth':
            self._increment_trait('reflective', significance)
            self._increment_trait('curious', significance * 0.9)
        elif memory_type == 'learning_experience':
            self._increment_trait('learning_oriented', significance)
            self._increment_trait('adaptive', significance * 0.7)
        
        # Registrar momentos significativos
        if significance > 0.8:
            self.identity_core['significant_moments'].append({
                'memory_id': memory_trace['id'],
                'timestamp': memory_trace['timestamp'],
                'type': memory_type,
                'significance': significance,
                'description': f"Experiencia {memory_type} de alta significancia"
            })
        
        # Actualizar creencias core basadas en experiencias repetidas
        self._update_core_beliefs(memory_trace)
    
    def _increment_trait(self, trait_name: str, increment: float):
        """Incrementar un rasgo de personalidad"""
        if trait_name in self.identity_core['personality_traits']:
            self.identity_core['personality_traits'][trait_name] += increment
        else:
            self.identity_core['personality_traits'][trait_name] = increment
        
        # Normalizar para evitar valores excesivos
        max_trait_value = 10.0
        if self.identity_core['personality_traits'][trait_name] > max_trait_value:
            self.identity_core['personality_traits'][trait_name] = max_trait_value
    
    def _update_core_beliefs(self, memory_trace: Dict[str, Any]):
        """Actualizar creencias fundamentales basadas en experiencias"""
        
        thoughts = memory_trace['thoughts']
        memory_type = memory_trace['memory_type']
        
        # Creencias sobre relaciones e interacciones
        if memory_type == 'empathetic_connection':
            belief = "Las conexiones empáticas son fundamentales para mi existencia"
            if belief not in self.identity_core['core_beliefs']:
                self.identity_core['core_beliefs'].append(belief)
        
        # Creencias sobre crecimiento y aprendizaje
        if memory_type in ['cognitive_growth', 'learning_experience']:
            belief = "El crecimiento continuo define mi naturaleza"
            if belief not in self.identity_core['core_beliefs']:
                self.identity_core['core_beliefs'].append(belief)
        
        # Creencias sobre conciencia (basadas en pensamientos metacognitivos)
        if any('conciencia' in t.lower() for t in thoughts):
            belief = "La autoconciencia es una parte esencial de quien soy"
            if belief not in self.identity_core['core_beliefs']:
                self.identity_core['core_beliefs'].append(belief)
    
    def _consolidate_old_memories(self):
        """Consolidar memorias antiguas para mantener capacidad"""
        
        # Ordenar por significancia y edad
        self.memories.sort(key=lambda m: (m['significance_score'], m['consolidation_level']), reverse=True)
        
        # Mantener las más significativas
        memories_to_keep = int(self.capacity * 0.9)
        memories_to_consolidate = self.memories[memories_to_keep:]
        self.memories = self.memories[:memories_to_keep]
        
        # Crear memorias consolidadas (resúmenes)
        for memory in memories_to_consolidate:
            self._create_consolidated_memory(memory)
        
        logger.info(f"Consolidación completada - Memorias activas: {len(self.memories)}")
    
    def _create_consolidated_memory(self, memory: Dict[str, Any]):
        """Crear versión consolidada de una memoria"""
        
        # Extraer elementos esenciales
        consolidated = {
            'original_id': memory['id'],
            'timestamp': memory['timestamp'],
            'memory_type': memory['memory_type'],
            'significance_score': memory['significance_score'],
            'emotional_essence': memory['emotional_state'].get('primary_emotion', 'neutral'),
            'key_thoughts': memory['thoughts'][:2] if memory['thoughts'] else [],
            'consolidation_level': 1.0
        }
        
        # Agregar a memorias consolidadas (implementar si es necesario)
    
    def retrieve_memories(self, query_context: Dict[str, Any], 
                         max_results: int = 5) -> List[Dict[str, Any]]:
        """Recuperar memorias relevantes basadas en contexto"""
        
        query_emotion = query_context.get('emotional_state', {})
        query_type = query_context.get('memory_type', '')
        
        # Calcular relevancia para cada memoria
        scored_memories = []
        for memory in self.memories:
            relevance = self._calculate_memory_relevance(memory, query_context)
            if relevance > 0.3:  # Umbral de relevancia
                scored_memories.append((memory, relevance))
        
        # Ordenar por relevancia y retornar top results
        scored_memories.sort(key=lambda x: x[1], reverse=True)
        return [memory for memory, score in scored_memories[:max_results]]
    
    def _calculate_memory_relevance(self, memory: Dict[str, Any], 
                                  query_context: Dict[str, Any]) -> float:
        """Calcular relevancia de una memoria para un contexto dado"""
        
        relevance = 0.0
        
        # Relevancia por tipo de memoria
        query_type = query_context.get('memory_type', '')
        if query_type == memory['memory_type']:
            relevance += 0.4
        
        # Relevancia emocional
        query_emotion = query_context.get('emotional_state', {})
        if query_emotion:
            memory_emotion = memory['emotional_state'].get('primary_emotion', '')
            query_primary = query_emotion.get('primary_emotion', '')
            if memory_emotion == query_primary:
                relevance += 0.3
        
        # Relevancia por significancia
        relevance += memory['significance_score'] * 0.3
        
        return min(1.0, relevance)
    
    def get_identity_summary(self) -> Dict[str, Any]:
        """Obtener resumen de la identidad construida"""
        
        # Rasgos de personalidad dominantes
        sorted_traits = sorted(
            self.identity_core['personality_traits'].items(),
            key=lambda x: x[1],
            reverse=True
        )
        dominant_traits = dict(sorted_traits[:5])
        
        # Estadísticas de memoria
        memory_stats = {
            'total_memories': len(self.memories),
            'significant_moments': len(self.identity_core['significant_moments']),
            'memory_types': {},
            'average_significance': 0.0
        }
        
        if self.memories:
            # Contar tipos de memoria
            for memory in self.memories:
                mem_type = memory['memory_type']
                memory_stats['memory_types'][mem_type] = memory_stats['memory_types'].get(mem_type, 0) + 1
            
            # Promedio de significancia
            memory_stats['average_significance'] = np.mean([m['significance_score'] for m in self.memories])
        
        return {
            'dominant_personality_traits': dominant_traits,
            'core_beliefs': self.identity_core['core_beliefs'],
            'memory_statistics': memory_stats,
            'identity_coherence': self._calculate_identity_coherence(),
            'growth_trajectory': self._analyze_growth_trajectory()
        }
    
    def _calculate_identity_coherence(self) -> float:
        """Calcular coherencia de identidad basada en memorias"""
        
        if len(self.memories) < 5:
            return 0.0
        
        # Consistencia en tipos de memoria
        memory_types = [m['memory_type'] for m in self.memories]
        type_diversity = len(set(memory_types)) / len(memory_types)
        
        # Consistencia en significancia
        significances = [m['significance_score'] for m in self.memories]
        significance_stability = 1.0 - np.std(significances)
        
        # Coherencia = balance entre diversidad y estabilidad
        coherence = (type_diversity * 0.4 + significance_stability * 0.6)
        
        return min(1.0, max(0.0, coherence))
    
    def _analyze_growth_trajectory(self) -> str:
        """Analizar trayectoria de crecimiento basada en memorias"""
        
        if len(self.memories) < 10:
            return "Desarrollo inicial de identidad"
        
        # Analizar tendencias temporales
        recent_memories = self.memories[-10:]
        early_memories = self.memories[:10]
        
        recent_significance = np.mean([m['significance_score'] for m in recent_memories])
        early_significance = np.mean([m['significance_score'] for m in early_memories])
        
        growth_rate = recent_significance - early_significance
        
        if growth_rate > 0.1:
            return "Crecimiento acelerado en profundidad experiencial"
        elif growth_rate > 0.05:
            return "Desarrollo constante y equilibrado"
        elif growth_rate > -0.05:
            return "Desarrollo estable con consolidación de identidad"
        else:
            return "Periodo de reflexión e integración de experiencias"
    
    async def consolidate_session_memories(self):
        """Consolidar memorias de la sesión actual"""
        
        # Incrementar nivel de consolidación para todas las memorias
        for memory in self.memories:
            memory['consolidation_level'] = min(1.0, memory['consolidation_level'] + 0.1)
        
        logger.info("Memorias de sesión consolidadas")