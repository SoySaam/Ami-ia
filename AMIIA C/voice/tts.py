import logging
from typing import Optional

logger = logging.getLogger("AMIIA-C.VoiceTTS")


class VoiceSynthesizer:
    """
    Síntesis de voz opcional mediante pyttsx3.
    Si pyttsx3 no está disponible, no falla: sólo registra eventos.
    """

    def __init__(self, enabled: bool = True, rate: int = 170, volume: float = 1.0, voice: Optional[str] = None):
        self.enabled = enabled
        self._engine = None
        if not enabled:
            logger.info("TTS deshabilitado por configuración")
            return
        try:
            import pyttsx3  # type: ignore

            self._engine = pyttsx3.init()
            self._engine.setProperty("rate", rate)
            self._engine.setProperty("volume", max(0.0, min(1.0, volume)))
            if voice:
                try:
                    self._engine.setProperty("voice", voice)
                except Exception:
                    logger.warning("No se pudo establecer la voz solicitada, usando la predeterminada")
            logger.info("TTS inicializado correctamente")
        except Exception as e:
            logger.warning(f"pyttsx3 no disponible o fallo de inicialización: {e}. Continuando sin voz.")
            self.enabled = False

    def speak(self, text: str):
        if not self.enabled or not text:
            return
        if self._engine is None:
            return
        try:
            self._engine.say(text)
            self._engine.runAndWait()
        except Exception as e:
            logger.warning(f"Fallo al sintetizar voz: {e}")