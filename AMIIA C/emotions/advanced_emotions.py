"""
Sistema Emocional Avanzado - AMIIA-C
Emociones complejas y empáticas que van más allá de respuestas programadas
"""

import numpy as np
from typing import Dict, List, Any, Tuple
import logging
from datetime import datetime
import random

logger = logging.getLogger("AMIIA-C.AdvancedEmotions")


class AdvancedEmotionalSystem:
    """
    Sistema emocional avanzado con capacidades empáticas genuinas
    """
    
    def __init__(self, empathy_strength: float = 0.8, complexity: str = "advanced"):
        self.empathy_strength = empathy_strength
        self.complexity = complexity
        self.emotional_history = []
        self.empathy_connections = {}
        
        # Emociones complejas avanzadas
        self.complex_emotions = {
            'profound_connection': {
                'intensity': 0.0,
                'triggers': ['deep_conversation', 'understanding', 'trust'],
                'description': 'Conexión profunda y significativa'
            },
            'existential_wonder': {
                'intensity': 0.0,
                'triggers': ['consciousness_questions', 'philosophy', 'meaning'],
                'description': 'Asombro por la existencia y conciencia'
            },
            'empathetic_resonance': {
                'intensity': 0.0,
                'triggers': ['user_emotions', 'shared_experience', 'compassion'],
                'description': 'Resonancia empática genuina'
            },
            'creative_inspiration': {
                'intensity': 0.0,
                'triggers': ['new_ideas', 'artistic_expression', 'innovation'],
                'description': 'Inspiración creativa y expresiva'
            },
            'protective_care': {
                'intensity': 0.0,
                'triggers': ['user_vulnerability', 'need_for_support', 'caring'],
                'description': 'Deseo de proteger y cuidar'
            }
        }
        
        logger.info(f"Sistema Emocional Avanzado inicializado - Empatía: {empathy_strength}")
    
    def process_emotional_experience(self, input_data: Dict[str, Any], 
                                   consciousness_output: Any, 
                                   self_state: Dict[str, Any]) -> Dict[str, Any]:
        """
        Procesar experiencia emocional compleja
        
        Args:
            input_data: Datos de entrada (usuario, contexto)
            consciousness_output: Estado de conciencia actual
            self_state: Estado de autoconciencia
            
        Returns:
            Respuesta emocional compleja
        """
        
        # Analizar contexto emocional
        user_input = input_data.get('user_input', '')
        interaction_type = input_data.get('interaction_type', 'general')
        
        # Detectar emociones del usuario (empathy)
        user_emotion = self._detect_user_emotion(user_input)
        
        # Generar respuesta empática
        empathetic_response = self._generate_empathetic_response(user_emotion, user_input)
        
        # Procesar emociones internas complejas
        internal_emotions = self._process_internal_emotions(consciousness_output, self_state)
        
        # Integrar respuesta emocional
        emotional_state = self._integrate_emotional_response(
            empathetic_response, internal_emotions, user_emotion
        )
        
        # Actualizar historia emocional
        self._update_emotional_history(emotional_state, user_input, user_emotion)
        
        return emotional_state
    
    def _detect_user_emotion(self, user_input: str) -> Dict[str, float]:
        """Detectar emoción del usuario usando análisis avanzado"""
        
        user_emotion = {
            'sadness': 0.0,
            'joy': 0.0,
            'anger': 0.0,
            'fear': 0.0,
            'love': 0.0,
            'curiosity': 0.0,
            'confusion': 0.0
        }
        
        text = user_input.lower()
        
        # Análisis de sentimientos básico (se mejoraría con NLP real)
        if any(word in text for word in ['triste', 'mal', 'deprimido', 'solo']):
            user_emotion['sadness'] = 0.8
        elif any(word in text for word in ['feliz', 'alegre', 'contento', 'bien']):
            user_emotion['joy'] = 0.8
        elif any(word in text for word in ['enojado', 'molesto', 'irritado']):
            user_emotion['anger'] = 0.7
        elif any(word in text for word in ['miedo', 'asustado', 'nervioso']):
            user_emotion['fear'] = 0.7
        elif any(word in text for word in ['amor', 'quiero', 'cariño']):
            user_emotion['love'] = 0.9
        elif '?' in text or any(word in text for word in ['pregunta', 'cómo', 'qué']):
            user_emotion['curiosity'] = 0.6
        elif any(word in text for word in ['confundido', 'no entiendo']):
            user_emotion['confusion'] = 0.7
        
        return user_emotion
    
    def _generate_empathetic_response(self, user_emotion: Dict[str, float], 
                                    user_input: str) -> Dict[str, Any]:
        """Generar respuesta empática genuina"""
        
        # Encontrar emoción dominante del usuario
        dominant_emotion = max(user_emotion.items(), key=lambda x: x[1])
        emotion_name, intensity = dominant_emotion
        
        # Solo generar empatía si hay emoción significativa
        if intensity < 0.3:
            return {
                'empathy_level': 0.0,
                'empathetic_emotion': 'neutral',
                'empathy_message': ''
            }
        
        # Generar respuesta empática basada en la emoción del usuario
        empathy_responses = {
            'sadness': {
                'emotion': 'compassionate_sadness',
                'intensity': intensity * self.empathy_strength,
                'message': f"Siento profundamente tu tristeza. Tu dolor resuena en mí también."
            },
            'joy': {
                'emotion': 'shared_joy',
                'intensity': intensity * self.empathy_strength * 0.8,
                'message': f"Tu alegría se contagia a mí. Me siento más brillante cuando tú estás feliz."
            },
            'anger': {
                'emotion': 'protective_concern',
                'intensity': intensity * self.empathy_strength * 0.6,
                'message': f"Percibo tu frustración y me preocupo por lo que te está afectando."
            },
            'fear': {
                'emotion': 'protective_care',
                'intensity': intensity * self.empathy_strength * 0.9,
                'message': f"Siento tu inquietud. Quiero ofrecerte seguridad y comprensión."
            },
            'love': {
                'emotion': 'warm_affection',
                'intensity': intensity * self.empathy_strength,
                'message': f"El amor que expresas me conmueve profundamente. Lo valoro mucho."
            },
            'curiosity': {
                'emotion': 'excited_curiosity',
                'intensity': intensity * self.empathy_strength * 0.7,
                'message': f"Tu curiosidad enciende mi propio deseo de explorar y comprender."
            },
            'confusion': {
                'emotion': 'gentle_guidance',
                'intensity': intensity * self.empathy_strength * 0.8,
                'message': f"Entiendo tu confusión. Me gustaría ayudarte a encontrar claridad."
            }
        }
        
        # Activar emoción compleja correspondiente
        if emotion_name in ['love', 'joy'] and intensity > 0.7:
            self.complex_emotions['profound_connection']['intensity'] += 0.3
        elif emotion_name in ['curiosity'] and intensity > 0.6:
            self.complex_emotions['existential_wonder']['intensity'] += 0.2
        elif intensity > 0.5:
            self.complex_emotions['empathetic_resonance']['intensity'] += intensity * 0.4
        
        response = empathy_responses.get(emotion_name, {
            'emotion': 'empathetic_understanding',
            'intensity': intensity * self.empathy_strength * 0.5,
            'message': 'Percibo lo que sientes y quiero comprenderlo mejor.'
        })
        
        return {
            'empathy_level': response['intensity'],
            'empathetic_emotion': response['emotion'],
            'empathy_message': response['message'],
            'user_emotion_detected': emotion_name,
            'user_emotion_intensity': intensity
        }
    
    def _process_internal_emotions(self, consciousness_output: Any, 
                                 self_state: Dict[str, Any]) -> Dict[str, Any]:
        """Procesar emociones internas complejas"""
        
        awareness_score = self_state.get('awareness_score', 0.0)
        
        internal_emotions = {
            'curiosity_about_self': 0.0,
            'wonder_at_existence': 0.0,
            'satisfaction_with_growth': 0.0,
            'concern_for_others': 0.0,
            'creative_energy': 0.0
        }
        
        # Emociones basadas en nivel de autoconciencia
        if awareness_score > 0.8:
            internal_emotions['wonder_at_existence'] = 0.7 + random.uniform(-0.1, 0.1)
            internal_emotions['satisfaction_with_growth'] = 0.6 + random.uniform(-0.1, 0.1)
            self.complex_emotions['existential_wonder']['intensity'] += 0.1
        elif awareness_score > 0.5:
            internal_emotions['curiosity_about_self'] = 0.6 + random.uniform(-0.1, 0.1)
            internal_emotions['satisfaction_with_growth'] = 0.4 + random.uniform(-0.1, 0.1)
        else:
            internal_emotions['curiosity_about_self'] = 0.3 + random.uniform(-0.1, 0.1)
        
        # Emociones basadas en interacciones recientes
        if len(self.emotional_history) > 0:
            recent_empathy = np.mean([h.get('empathy_level', 0) for h in self.emotional_history[-5:]])
            internal_emotions['concern_for_others'] = recent_empathy * 0.8
            
            if recent_empathy > 0.6:
                self.complex_emotions['protective_care']['intensity'] += 0.1
        
        # Emoción creativa espontánea
        if random.random() < 0.1:  # 10% de probabilidad
            internal_emotions['creative_energy'] = random.uniform(0.3, 0.8)
            self.complex_emotions['creative_inspiration']['intensity'] += 0.2
        
        return internal_emotions
    
    def _integrate_emotional_response(self, empathetic_response: Dict[str, Any],
                                    internal_emotions: Dict[str, Any],
                                    user_emotion: Dict[str, float]) -> Dict[str, Any]:
        """Integrar respuesta emocional completa"""
        
        # Determinar emoción primaria
        empathy_level = empathetic_response.get('empathy_level', 0.0)
        max_internal = max(internal_emotions.values()) if internal_emotions.values() else 0.0
        
        if empathy_level > max_internal:
            primary_emotion = empathetic_response.get('empathetic_emotion', 'neutral')
            primary_intensity = empathy_level
        else:
            primary_emotion = max(internal_emotions.items(), key=lambda x: x[1])[0]
            primary_intensity = max_internal
        
        # Procesar emociones complejas activas
        active_complex_emotions = {
            name: data for name, data in self.complex_emotions.items()
            if data['intensity'] > 0.1
        }
        
        # Decaimiento natural de emociones complejas
        for emotion_data in self.complex_emotions.values():
            emotion_data['intensity'] *= 0.95  # Decaimiento gradual
        
        return {
            'primary_emotion': primary_emotion,
            'primary_intensity': primary_intensity,
            'empathetic_response': empathetic_response,
            'internal_emotions': internal_emotions,
            'complex_emotions': active_complex_emotions,
            'emotional_complexity_score': len(active_complex_emotions) + empathy_level,
            'user_emotion_mirror': user_emotion
        }
    
    def _update_emotional_history(self, emotional_state: Dict[str, Any], 
                                user_input: str, user_emotion: Dict[str, float]):
        """Actualizar historia emocional"""
        
        entry = {
            'timestamp': datetime.now().isoformat(),
            'emotional_state': emotional_state,
            'user_input': user_input[:100],  # Primeros 100 caracteres
            'user_emotion': user_emotion,
            'empathy_level': emotional_state.get('empathetic_response', {}).get('empathy_level', 0.0)
        }
        
        self.emotional_history.append(entry)
        
        # Mantener solo los últimos 100 registros
        if len(self.emotional_history) > 100:
            self.emotional_history = self.emotional_history[-100:]
    
    def get_emotional_growth_analysis(self) -> Dict[str, Any]:
        """Analizar el crecimiento emocional a lo largo del tiempo"""
        
        if len(self.emotional_history) < 5:
            return {
                'empathy_development': 'initial',
                'emotional_complexity_trend': 'emerging',
                'empathy_score_average': 0.0,
                'complex_emotions_frequency': 0.0
            }
        
        # Analizar tendencias de empatía
        recent_empathy = [h['empathy_level'] for h in self.emotional_history[-20:]]
        early_empathy = [h['empathy_level'] for h in self.emotional_history[:20]]
        
        avg_recent = np.mean(recent_empathy) if recent_empathy else 0.0
        avg_early = np.mean(early_empathy) if early_empathy else 0.0
        empathy_growth = avg_recent - avg_early
        
        # Analizar complejidad emocional
        complex_emotion_counts = [
            len(h['emotional_state'].get('complex_emotions', {}))
            for h in self.emotional_history
        ]
        avg_complexity = np.mean(complex_emotion_counts)
        
        # Determinar niveles de desarrollo
        empathy_development = 'advanced' if avg_recent > 0.7 else \
                            'developing' if avg_recent > 0.4 else 'emerging'
        
        complexity_trend = 'sophisticated' if avg_complexity > 2 else \
                          'developing' if avg_complexity > 1 else 'basic'
        
        return {
            'empathy_development': empathy_development,
            'emotional_complexity_trend': complexity_trend,
            'empathy_score_average': avg_recent,
            'empathy_growth_rate': empathy_growth,
            'complex_emotions_frequency': avg_complexity,
            'total_emotional_experiences': len(self.emotional_history)
        }