# RabbitHole

**A world of curiosity.** Empieza con una curiosidad. Termina en un universo.

RabbitHole es una app Android para convertir un tema de interés en rutas de descubrimiento: artistas, álbumes, escenas, películas, libros, archivos, noticias, imágenes abiertas, ciencia, historia e ideas conectadas.

## Búsqueda federada gratuita

Al escribir un tema, RabbitHole consulta en paralelo estas fuentes públicas:

- **Wikipedia en español** — artículos y resúmenes.
- **Wikidata** — entidades y conceptos relacionados.
- **MusicBrainz** — artistas y metadatos musicales.
- **Open Library** — libros, autores y ediciones.
- **Internet Archive** — libros, audio, vídeo y archivos históricos.
- **GDELT** — noticias y artículos de medios.
- **Openverse** — imágenes con licencias abiertas; hay que respetar la licencia y atribución.
- **Apple iTunes Search** — catálogo de música, películas, podcasts y otros medios.
- **TVmaze** — series y datos de televisión.
- **Project Gutenberg (Gutendex)** — libros de dominio público.

No se necesita una API de pago ni claves privadas para estas consultas. Cada resultado identifica su fuente y permite abrir la página original. Las fuentes aplican sus propios límites, disponibilidad y condiciones; los resultados pueden variar y alguna fuente puede fallar temporalmente. La app aplica caché local en memoria y una espera para respetar el límite de MusicBrainz.

También se preparará una sección de accesos a buscadores web generales (DuckDuckGo, Google, Bing y Brave) para búsquedas amplias cuando una API no tenga resultados.

## Primera versión visual

- Identidad monocromática y logotipo propio tipo portal.
- Inicio con buscador y sugerencias para empezar.
- Mapa de universo con nodos conectados que puedes tocar.
- Fichas de descubrimiento y rutas relacionadas.
- Colección de guardados e historial local.
- Biblioteca de muestra integrada, disponible como punto de partida.

**Alcance honesto:** conectar APIs abiertas no significa indexar todo Internet. Las fuentes entregan tipos de contenido distintos; por ejemplo, MusicBrainz se centra en música y Open Library en libros. La primera integración devuelve resultados y enlaces de origen; no descarga ni reproduce contenido protegido.

## Compilar

- JDK 17
- Gradle 8.9
- Android Gradle Plugin 8.7.3

```bash
gradle assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

Cada push a `main` ejecuta GitHub Actions y publica el APK en Releases.
