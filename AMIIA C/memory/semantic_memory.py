import hashlib
import math
from typing import Dict, List, Any, Optional
from collections import deque
import logging

logger = logging.getLogger("AMIIA-C.SemanticMemory")


class SemanticMemory:
    """
    Memoria semántica ligera basada en hashing de n-gramas y similitud coseno.
    - No requiere dependencias pesadas
    - Soporta inserción y consulta top-k
    """

    def __init__(self, embedding_dim: int = 512, max_items: int = 5000):
        self.embedding_dim = embedding_dim
        self.max_items = max_items
        self._items: deque = deque(maxlen=max_items)
        self._vectors: deque = deque(maxlen=max_items)

        logger.info(
            f"Memoria Semántica inicializada (dim={embedding_dim}, max_items={max_items})"
        )

    def add_memory(self, memory_id: str, text: str, metadata: Optional[Dict[str, Any]] = None):
        """Agregar una memoria con su embedding semántico."""
        if not text:
            return
        vector = self._embed_text(text)
        self._items.append({"id": memory_id, "text": text, "metadata": metadata or {}})
        self._vectors.append(vector)

    def query(self, text: str, top_k: int = 3) -> List[Dict[str, Any]]:
        """Recuperar memorias más similares al texto dado."""
        if not text or not self._items:
            return []
        q_vec = self._embed_text(text)
        scored: List[tuple[float, int]] = []
        for idx, vec in enumerate(self._vectors):
            score = self._cosine_similarity(q_vec, vec)
            scored.append((score, idx))
        scored.sort(key=lambda x: x[0], reverse=True)
        results = []
        for score, idx in scored[: max(1, top_k)]:
            item = self._items[idx]
            results.append({"id": item["id"], "text": item["text"], "metadata": item["metadata"], "score": float(score)})
        return results

    def build_context_snippets(self, text: str, top_k: int = 3) -> List[str]:
        """Devuelve solo fragmentos de texto para inyectar como contexto."""
        hits = self.query(text, top_k=top_k)
        return [h["text"] for h in hits]

    # Internos

    def _embed_text(self, text: str) -> List[float]:
        """
        Embedding hash simple:
        - tokenización por palabras + trigramas de caracteres
        - hashing a un espacio de dimensión fija
        - normalización L2
        """
        dim = self.embedding_dim
        vec = [0.0] * dim

        cleaned = (text or "").lower().strip()

        # Palabras
        for token in cleaned.split():
            h = int(hashlib.md5(token.encode("utf-8")).hexdigest(), 16)
            idx = h % dim
            vec[idx] += 1.0

        # Trigramas de caracteres
        chars = f"^{cleaned}$"
        for i in range(max(0, len(chars) - 2)):
            tri = chars[i : i + 3]
            h = int(hashlib.sha1(tri.encode("utf-8")).hexdigest(), 16)
            idx = h % dim
            vec[idx] += 0.5

        # Normalización L2
        norm = math.sqrt(sum(v * v for v in vec)) or 1.0
        return [v / norm for v in vec]

    def _cosine_similarity(self, a: List[float], b: List[float]) -> float:
        return float(sum(x * y for x, y in zip(a, b)))