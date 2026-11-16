# RabbitHole

**Sigue tu curiosidad. Descubre conexiones que no sabías que existían.**

RabbitHole convierte un tema de interés en un recorrido de descubrimientos: artistas, álbumes, películas, historias, ideas y conexiones.

## Primera versión

- Interfaz minimalista en blanco y negro.
- Buscador de temas y puntos de partida.
- Fichas de descubrimiento con rutas relacionadas.
- Colección de elementos guardados.
- Historial de exploración.
- Funciona sin cuenta y sin API de pago.
- Preparada para incorporar fuentes abiertas de información en futuras versiones.

> Esta versión inicial usa una pequeña biblioteca local de ejemplos y rutas sugeridas. Las búsquedas no consultan todavía todo Internet ni verifican disponibilidad de contenidos externos.

## Compilar

El proyecto usa Kotlin, Jetpack Compose y Android Gradle Plugin. Requiere JDK 17 y Gradle 8.9.

```bash
gradle assembleDebug
```

APK generado en `app/build/outputs/apk/debug/app-debug.apk`.

Cada compilación de la rama `main` ejecuta GitHub Actions y publica el APK en GitHub Releases.
