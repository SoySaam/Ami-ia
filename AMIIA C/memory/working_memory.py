"""
Sistema de Memoria de Trabajo - AMIIA-C
Maneja información activa en el procesamiento consciente
"""

from typing import Dict, List, Any, Optional
import logging
from datetime import datetime
from collections import deque

logger = logging.getLogger("AMIIA-C.WorkingMemory")


class WorkingMemory:
    """
    Sistema de memoria de trabajo que mantiene información activa para procesamiento consciente
    """
    
    def __init__(self, slots: int = 7):
        self.slots = slots  # Límite de memoria de trabajo (basado en investigación cognitiva)
        self.active_items = deque(maxlen=slots)
        self.attention_weights = {}
        self.context_buffer = {}
        
        logger.info(f"Memoria de Trabajo inicializada - Slots: {slots}")
    
    def process_input(self, input_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Procesar entrada y mantener en memoria de trabajo
        
        Args:
            input_data: Datos de entrada para procesar
            
        Returns:
            Contexto procesado para la red de conciencia
        """
        
        # Agregar nueva información
        self.add_to_working_memory(input_data)
        
        # Actualizar pesos de atención
        self._update_attention_weights()
        
        # Crear contexto integrado
        working_context = self._create_integrated_context()
        
        return working_context
    
    def add_to_working_memory(self, item: Dict[str, Any]):
        """Agregar item a memoria de trabajo"""
        
        # Crear entrada de memoria con timestamp
        memory_item = {
            'content': item,
            'timestamp': datetime.now().isoformat(),
            'access_count': 1,
            'relevance_score': self._calculate_relevance(item)
        }
        
        # Si está llena, el deque automáticamente remueve el más antiguo
        self.active_items.append(memory_item)
        
        logger.debug(f"Item agregado a memoria de trabajo - Slots usados: {len(self.active_items)}")
    
    def add_context(self, context: Dict[str, Any]):
        """Agregar contexto adicional al buffer"""
        context_id = f"context_{len(self.context_buffer)}"
        self.context_buffer[context_id] = {
            'data': context,
            'timestamp': datetime.now().isoformat()
        }
        
        # Mantener solo contextos recientes
        if len(self.context_buffer) > 10:
            oldest_key = min(self.context_buffer.keys(), 
                           key=lambda k: self.context_buffer[k]['timestamp'])
            del self.context_buffer[oldest_key]
    
    def _calculate_relevance(self, item: Dict[str, Any]) -> float:
        """Calcular relevancia de un item para la conciencia"""
        
        relevance = 0.5  # Base
        
        # Información del usuario tiene alta relevancia
        if 'user_input' in item:
            relevance += 0.3
        
        # Información emocional es relevante
        if 'emotional_context' in item:
            relevance += 0.2
        
        # Información temporal/reciente es relevante
        if 'timestamp' in item:
            relevance += 0.1
        
        return min(1.0, relevance)
    
    def _update_attention_weights(self):
        """Actualizar pesos de atención para items en memoria de trabajo"""
        
        self.attention_weights.clear()
        
        total_relevance = sum(item['relevance_score'] for item in self.active_items)
        
        if total_relevance > 0:
            for i, item in enumerate(self.active_items):
                weight = item['relevance_score'] / total_relevance
                # Items más recientes tienen ligero boost
                recency_boost = (i + 1) / len(self.active_items) * 0.1
                self.attention_weights[i] = weight + recency_boost
    
    def _create_integrated_context(self) -> Dict[str, Any]:
        """Crear contexto integrado para la red de conciencia"""
        
        # Consolidar información de memoria de trabajo
        integrated_thoughts = []
        integrated_emotions = {}
        integrated_memories = []
        
        for i, item in enumerate(self.active_items):
            content = item['content']
            weight = self.attention_weights.get(i, 0.1)
            
            # Extraer pensamientos
            if 'thoughts' in content or 'previous_thoughts' in content:
                thoughts = content.get('thoughts', content.get('previous_thoughts', []))
                integrated_thoughts.extend(thoughts[-3:])  # Últimos 3 pensamientos
            
            # Extraer contexto emocional
            if 'emotional_context' in content:
                emotion_data = content['emotional_context']
                for emotion, value in emotion_data.items():
                    if emotion in integrated_emotions:
                        integrated_emotions[emotion] = max(integrated_emotions[emotion], value * weight)
                    else:
                        integrated_emotions[emotion] = value * weight
            
            # Extraer memorias
            if 'memory_context' in content:
                memories = content['memory_context']
                integrated_memories.extend(memories[-2:])  # Últimas 2 memorias
        
        # Agregar contexto del buffer
        buffer_data = []
        for context_entry in self.context_buffer.values():
            buffer_data.append(context_entry['data'])
        
        return {
            'integrated_thoughts': integrated_thoughts[-5:],  # Máximo 5 pensamientos
            'integrated_emotions': integrated_emotions,
            'integrated_memories': integrated_memories[-3:],  # Máximo 3 memorias
            'context_buffer': buffer_data[-3:],  # Últimos 3 contextos
            'working_memory_load': len(self.active_items) / self.slots,
            'attention_distribution': self.attention_weights.copy()
        }
    
    def get_working_memory_status(self) -> Dict[str, Any]:
        """Obtener estado actual de la memoria de trabajo"""
        
        return {
            'slots_used': len(self.active_items),
            'slots_available': self.slots - len(self.active_items),
            'memory_load': len(self.active_items) / self.slots,
            'oldest_item_age': self._get_oldest_item_age(),
            'average_relevance': self._get_average_relevance(),
            'context_buffer_size': len(self.context_buffer)
        }
    
    def _get_oldest_item_age(self) -> float:
        """Obtener edad del item más antiguo en minutos"""
        if not self.active_items:
            return 0.0
        
        oldest_timestamp = self.active_items[0]['timestamp']
        oldest_time = datetime.fromisoformat(oldest_timestamp)
        age_minutes = (datetime.now() - oldest_time).total_seconds() / 60
        
        return age_minutes
    
    def _get_average_relevance(self) -> float:
        """Obtener relevancia promedio de items en memoria"""
        if not self.active_items:
            return 0.0
        
        total_relevance = sum(item['relevance_score'] for item in self.active_items)
        return total_relevance / len(self.active_items)
    
    def clear_working_memory(self):
        """Limpiar memoria de trabajo"""
        self.active_items.clear()
        self.attention_weights.clear()
        self.context_buffer.clear()
        logger.info("Memoria de trabajo limpiada")