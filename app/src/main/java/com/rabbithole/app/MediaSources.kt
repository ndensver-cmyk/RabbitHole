package com.rabbithole.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap

private val imageCache = ConcurrentHashMap<String, String>()
private fun encImage(value: String) = URLEncoder.encode(value.trim(), "UTF-8")

private fun imageJson(address: String): JSONObject {
    val connection = URL(address).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    connection.instanceFollowRedirects = true
    connection.setRequestProperty("User-Agent", "RabbitHole/5.5.0 (https://github.com/ndensver-cmyk/RabbitHole)")
    connection.setRequestProperty("Accept", "application/json")
    connection.setRequestProperty("Accept-Language", "en,es;q=0.8")
    try {
        val status = connection.responseCode
        if (status !in 200..299) throw IllegalStateException("Image catalogue HTTP $status")
        return JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
    } finally {
        connection.disconnect()
    }
}

private fun wikipediaSummaryImage(query: String, language: String): String {
    val title = URLEncoder.encode(query.trim().replace(' ', '_'), "UTF-8").replace("+", "%20")
    val json = imageJson("https://$language.wikipedia.org/api/rest_v1/page/summary/$title")
    return json.optJSONObject("originalimage")?.optString("source").orEmpty()
        .ifBlank { json.optJSONObject("thumbnail")?.optString("source").orEmpty() }
}

private fun wikipediaSearchImage(query: String, language: String): String {
    val address = "https://$language.wikipedia.org/w/api.php?action=query&generator=search&gsrsearch=${encImage(query)}&gsrlimit=6&gsrnamespace=0&prop=pageimages&piprop=thumbnail&pithumbsize=1400&format=json"
    val pages = imageJson(address).optJSONObject("query")?.optJSONObject("pages") ?: return ""
    val keys = pages.keys()
    while (keys.hasNext()) {
        val page = pages.optJSONObject(keys.next()) ?: continue
        val image = page.optJSONObject("thumbnail")?.optString("source").orEmpty()
        if (image.startsWith("https://")) return image
    }
    return ""
}

private fun commonsImage(query: String): String {
    val address = "https://commons.wikimedia.org/w/api.php?action=query&generator=search&gsrsearch=${encImage(query)}&gsrnamespace=6&gsrlimit=8&prop=imageinfo&iiprop=url&iiurlwidth=1400&format=json"
    val pages = imageJson(address).optJSONObject("query")?.optJSONObject("pages") ?: return ""
    val keys = pages.keys()
    while (keys.hasNext()) {
        val page = pages.optJSONObject(keys.next()) ?: continue
        val info = page.optJSONArray("imageinfo")?.optJSONObject(0) ?: continue
        val image = info.optString("thumburl").ifBlank { info.optString("url") }
        if (image.startsWith("https://") && !image.endsWith(".svg")) return image
    }
    return ""
}

private fun openverseImage(query: String): String {
    val address = "https://api.openverse.org/v1/images/?q=${encImage(query)}&page_size=8"
    val results = imageJson(address).optJSONArray("results") ?: return ""
    for (index in 0 until results.length()) {
        val result = results.optJSONObject(index) ?: continue
        val image = result.optString("thumbnail").ifBlank { result.optString("url") }
        if (image.startsWith("https://")) return image
    }
    return ""
}

/**
 * Tries page-specific Wikipedia images first, then Wikimedia search and Openverse.
 * The image URL is cached so each poster does not repeat the same network lookup.
 */
suspend fun loadCommonsImage(query: String): String = withContext(Dispatchers.IO) {
    val normalized = query.trim()
    if (normalized.isBlank()) return@withContext ""
    imageCache[normalized.lowercase()]?.let { return@withContext it }
    val attempts = listOf<() -> String>(
        { wikipediaSummaryImage(normalized, "en") },
        { wikipediaSummaryImage(normalized, "es") },
        { wikipediaSearchImage(normalized, "en") },
        { wikipediaSearchImage(normalized, "es") },
        { commonsImage(normalized) },
        { openverseImage(normalized) }
    )
    for (attempt in attempts) {
        val image = try { attempt() } catch (_: Exception) { "" }
        if (image.startsWith("https://") && !image.endsWith(".svg")) {
            imageCache[normalized.lowercase()] = image
            return@withContext image
        }
    }
    ""
}
