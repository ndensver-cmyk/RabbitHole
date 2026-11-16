package com.rabbithole.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

private val sourceCache = ConcurrentHashMap<String, Pair<Long, List<Discovery>>>()
@Volatile private var lastMusicBrainzRequest = 0L

private fun enc(value: String) = URLEncoder.encode(value, "UTF-8")

private fun requestJson(address: String): JSONObject {
    val connection = (URL(address).openConnection() as HttpURLConnection)
    connection.requestMethod = "GET"
    connection.connectTimeout = 7000
    connection.readTimeout = 7000
    connection.setRequestProperty("User-Agent", "RabbitHole/1.1 (Android discovery app)")
    connection.setRequestProperty("Accept", "application/json")
    try {
        val code = connection.responseCode
        if (code !in 200..299) throw IllegalStateException("HTTP $code")
        return JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
    } finally {
        connection.disconnect()
    }
}

private fun hit(title: String, type: String, description: String, source: String, url: String) =
    Discovery(title.trim(), type, "Todo", description.trim().take(420), emptyList(), source, url)

private suspend fun wikipedia(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://es.wikipedia.org/w/api.php?action=query&generator=search&gsrsearch=${enc(q)}&gsrlimit=6&gsrnamespace=0&prop=extracts%7Cinfo&exintro=1&explaintext=1&inprop=url&format=json")
    val pages = json.optJSONObject("query")?.optJSONObject("pages") ?: return@withContext emptyList()
    val out = mutableListOf<Discovery>()
    val keys = pages.keys()
    while (keys.hasNext()) {
        val page = pages.optJSONObject(keys.next()) ?: continue
        val title = page.optString("title")
        if (title.isNotBlank()) out += hit(title, "ENCICLOPEDIA", page.optString("extract", "Artículo de Wikipedia sobre $title."), "Wikipedia", page.optString("fullurl", "https://es.wikipedia.org/wiki/${enc(title).replace("+", "_")}"))
    }
    out
}

private suspend fun wikidata(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://www.wikidata.org/w/api.php?action=wbsearchentities&search=${enc(q)}&language=es&uselang=es&type=item&limit=6&format=json")
    val results = json.optJSONArray("search") ?: return@withContext emptyList()
    (0 until results.length()).mapNotNull { i ->
        val item = results.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("label")
        if (title.isBlank()) null else hit(title, "ENTIDAD / CONCEPTO", item.optString("description", "Entidad relacionada en la base de conocimiento Wikidata."), "Wikidata", item.optString("concepturi", "https://www.wikidata.org"))
    }
}

private suspend fun openLibrary(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://openlibrary.org/search.json?q=${enc(q)}&limit=5&fields=key,title,author_name,first_publish_year")
    val docs = json.optJSONArray("docs") ?: return@withContext emptyList()
    (0 until docs.length()).mapNotNull { i ->
        val item = docs.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("title")
        if (title.isBlank()) null else {
            val authors = item.optJSONArray("author_name")
            val author = if (authors != null && authors.length() > 0) authors.optString(0) else "Autor no indicado"
            val year = item.optString("first_publish_year")
            hit(title, "LIBRO", "$author · ${year.takeIf { it.isNotBlank() } ?: "Fecha no indicada"}", "Open Library", "https://openlibrary.org${item.optString("key")}")
        }
    }
}

private suspend fun archive(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://archive.org/advancedsearch.php?q=${enc(q)}&fl[]=identifier,title,description,mediatype,year&rows=6&page=1&output=json")
    val docs = json.optJSONObject("response")?.optJSONArray("docs") ?: return@withContext emptyList()
    (0 until docs.length()).mapNotNull { i ->
        val item = docs.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("title")
        val id = item.optString("identifier")
        if (title.isBlank() || id.isBlank()) null else hit(title, item.optString("mediatype", "ARCHIVO").uppercase(), item.optString("description", "Material preservado en Internet Archive."), "Internet Archive", "https://archive.org/details/$id")
    }
}

private suspend fun musicBrainz(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val wait = synchronized(this@withContext) {
        val now = System.currentTimeMillis()
        val delayMs = (1100L - (now - lastMusicBrainzRequest)).coerceAtLeast(0L)
        lastMusicBrainzRequest = now + delayMs
        delayMs
    }
    if (wait > 0) Thread.sleep(wait)
    val json = requestJson("https://musicbrainz.org/ws/2/artist/?query=${enc(q)}&fmt=json&limit=5")
    val artists = json.optJSONArray("artists") ?: return@withContext emptyList()
    (0 until artists.length()).mapNotNull { i ->
        val item = artists.optJSONObject(i) ?: return@mapNotNull null
        val name = item.optString("name")
        if (name.isBlank()) null else {
            val area = item.optJSONObject("area")?.optString("name").orEmpty()
            val type = item.optString("type")
            hit(name, "ARTISTA MUSICAL", listOf(type, area, item.optString("disambiguation")).filter { it.isNotBlank() }.joinToString(" · ").ifBlank { "Artista registrado en MusicBrainz." }, "MusicBrainz", "https://musicbrainz.org/artist/${item.optString("id")}")
        }
    }
}

private suspend fun gdelt(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://api.gdeltproject.org/api/v2/doc/doc?query=${enc(q)}&mode=ArtList&format=json&maxrecords=5&sort=HybridRel")
    val articles = json.optJSONArray("articles") ?: return@withContext emptyList()
    (0 until articles.length()).mapNotNull { i ->
        val item = articles.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("title")
        if (title.isBlank()) null else hit(title, "ARTÍCULO / NOTICIA", "${item.optString("domain")} · ${item.optString("seendate")}", "GDELT", item.optString("url"))
    }
}

private suspend fun openverse(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://api.openverse.org/v1/images/?q=${enc(q)}&page_size=5")
    val results = json.optJSONArray("results") ?: return@withContext emptyList()
    (0 until results.length()).mapNotNull { i ->
        val item = results.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("title").ifBlank { "Imagen relacionada con $q" }
        val creator = item.optString("creator")
        val license = item.optString("license")
        hit(title, "IMAGEN CON LICENCIA ABIERTA", listOf(creator, license).filter { it.isNotBlank() }.joinToString(" · ").ifBlank { "Revisa la licencia y atribución antes de reutilizar." }, "Openverse", item.optString("foreign_landing_url", item.optString("url")))
    }
}

private suspend fun itunes(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://itunes.apple.com/search?term=${enc(q)}&limit=5&media=all")
    val results = json.optJSONArray("results") ?: return@withContext emptyList()
    (0 until results.length()).mapNotNull { i ->
        val item = results.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("trackName").ifBlank { item.optString("collectionName") }
        val link = item.optString("trackViewUrl").ifBlank { item.optString("collectionViewUrl") }
        if (title.isBlank() || link.isBlank()) null else hit(title, item.optString("kind", "CONTENIDO").uppercase(), item.optString("artistName", item.optString("primaryGenreName", "Catálogo Apple")), "Apple iTunes", link)
    }
}

private suspend fun tvmaze(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val jsonArray = withContext(Dispatchers.IO) {
        val connection = URL("https://api.tvmaze.com/search/shows?q=${enc(q)}").openConnection() as HttpURLConnection
        connection.connectTimeout = 7000
        connection.readTimeout = 7000
        connection.setRequestProperty("User-Agent", "RabbitHole/1.1")
        try { org.json.JSONArray(connection.getInputStream().bufferedReader().use { it.readText() }) } finally { connection.disconnect() }
    }
    (0 until jsonArray.length()).mapNotNull { i ->
        val show = jsonArray.optJSONObject(i)?.optJSONObject("show") ?: return@mapNotNull null
        val name = show.optString("name")
        if (name.isBlank()) null else hit(name, "SERIE / TELEVISIÓN", show.optString("summary").replace(Regex("<[^>]*>"), "").ifBlank { "Serie registrada en TVmaze." }, "TVmaze", show.optString("url"))
    }
}

private suspend fun gutendex(q: String): List<Discovery> = withContext(Dispatchers.IO) {
    val json = requestJson("https://gutendex.com/books/?search=${enc(q)}")
    val results = json.optJSONArray("results") ?: return@withContext emptyList()
    (0 until minOf(results.length(), 5)).mapNotNull { i ->
        val item = results.optJSONObject(i) ?: return@mapNotNull null
        val title = item.optString("title")
        val authors = item.optJSONArray("authors")
        val author = if (authors != null && authors.length() > 0) authors.optJSONObject(0)?.optString("name").orEmpty() else ""
        val id = item.optInt("id")
        if (title.isBlank()) null else hit(title, "LIBRO DE DOMINIO PÚBLICO", author.ifBlank { "Proyecto Gutenberg" }, "Project Gutenberg", "https://www.gutenberg.org/ebooks/$id")
    }
}

suspend fun searchOpenSources(query: String): List<Discovery> {
    val q = query.trim()
    if (q.length < 2) return emptyList()
    val key = q.lowercase()
    sourceCache[key]?.let { cached -> if (System.currentTimeMillis() - cached.first < 10 * 60 * 1000) return cached.second }
    val sources = listOf<suspend () -> List<Discovery>>(
        { wikipedia(q) }, { wikidata(q) }, { openLibrary(q) }, { archive(q) },
        { musicBrainz(q) }, { gdelt(q) }, { openverse(q) }, { itunes(q) },
        { tvmaze(q) }, { gutendex(q) }
    )
    val found = coroutineScope {
        sources.map { source -> async(Dispatchers.IO) { try { source() } catch (_: Exception) { emptyList() } } }.awaitAll().flatten()
    }.distinctBy { "${it.source}:${it.title.lowercase()}" }
    sourceCache[key] = System.currentTimeMillis() to found
    found
}
