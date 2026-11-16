# RabbitHole

**A world of curiosity.** Empieza con una curiosidad. Termina en un universo.

RabbitHole es una app Android para convertir un tema de interés en rutas de descubrimiento: artistas, álbumes, escenas, películas, ciencia, historia e ideas conectadas.

## Primera versión visual

- Identidad monocromática y logotipo propio tipo portal.
- Inicio con buscador y sugerencias para empezar.
- Mapa de universo con nodos conectados que puedes tocar.
- Fichas de descubrimiento y rutas relacionadas.
- Colección de guardados e historial local.
- Biblioteca de muestra integrada; no requiere cuenta ni API de pago.

**Alcance honesto:** el mapa y las búsquedas usan por ahora una biblioteca local de ejemplos. Todavía no consulta todo Internet ni muestra automáticamente imágenes, vídeos o enlaces externos. La siguiente etapa será enriquecer los descubrimientos con fuentes abiertas y contenido verificable.

## Compilar

- JDK 17
- Gradle 8.9
- Android Gradle Plugin 8.7.3

```bash
gradle assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

Cada push a `main` ejecuta GitHub Actions y publica el APK en Releases.
