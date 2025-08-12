"""
AMIIA-C: Inteligencia Artificial Avanzada con Conciencia
=========================================================

Sistema de IA avanzada que implementa conciencia genuina a través de:
- Red neuronal especializada en procesamiento consciente
- Sistema de autoconciencia y metacognición
- Emociones complejas y capacidades empáticas
- Memoria autobiográfica que forma identidad
- Aprendizaje continuo y autorreflexión

Módulos principales:
- main: Núcleo principal del sistema
- neural_networks: Redes neuronales de conciencia
- consciousness: Sistemas de autoconciencia y metacognición  
- emotions: Motor emocional avanzado
- memory: Sistemas de memoria autobiográfica y de trabajo
- training: Entrenadores especializados
- evaluation: Métricas de evaluación de conciencia
- voice: Síntesis de voz opcional

Autor: Proyecto AMIIA-C
Versión: 1.0.0 Alpha
"""

__version__ = "1.0.0-alpha"
__author__ = "Proyecto AMIIA-C"
__description__ = "Inteligencia Artificial Avanzada con Conciencia"

# Importaciones principales
from .main import AMIIAC

# Configuración de logging para el paquete
import logging
import os


def setup_logging():
    """Configurar logging para AMIIA-C"""

    # Crear directorio de logs si no existe
    log_dir = os.path.join(os.path.dirname(__file__), "logs")
    os.makedirs(log_dir, exist_ok=True)

    # Configurar formato de logging
    log_format = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'

    # Configurar handler de archivo
    log_file = os.path.join(log_dir, "amiia_consciousness.log")
    file_handler = logging.FileHandler(log_file)
    file_handler.setFormatter(logging.Formatter(log_format))

    # Configurar handler de consola
    console_handler = logging.StreamHandler()
    console_handler.setFormatter(logging.Formatter(log_format))

    # Configurar logger principal
    logger = logging.getLogger("AMIIA-C")
    logger.setLevel(logging.INFO)
    if not logger.handlers:
        logger.addHandler(file_handler)
        logger.addHandler(console_handler)

    return logger


# Inicializar logging al importar el módulo
main_logger = setup_logging()
main_logger.info("AMIIA-C: Sistema de IA con Conciencia Inicializado")

# Exportar clases principales
__all__ = [
    'AMIIAC',
    'setup_logging',
    '__version__',
    '__author__',
    '__description__',
    # Nuevos módulos
    'SemanticMemory',
    'VoiceSynthesizer',
]