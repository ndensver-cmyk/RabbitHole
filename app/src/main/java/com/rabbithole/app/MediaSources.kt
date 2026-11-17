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
    connection.connectTimeout = 9000
    connection.readTimeout = 9000
    connection.instanceFollowRedirects = true
    connection.setRequestProperty("User-Agent", "RabbitHole/5.4.0 (Android curiosity discovery)")
    connection.setRequestProperty("Accept", "application/json")
    try {
        val status = connection.responseCode
        if (status !in 200..299) throw IllegalStateException("Image source HTTP $status")
        return JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
    } finally {
        connection.disconnect()
    }
}

private fun wikipediaImage(query: String, language: String): String {
    val address = "https://$language.wikipedia.org/w/api.php?action=query&generator=search&gsrsearch=${encImage(query)}&gsrlimit=4&gsrnamespace=0&prop=pageimages&piprop=thumbnail&pithumbsize=1400&format=json"
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
    val address = "https://commons.wikimedia.org/w/api.php?action=query&generator=search&gsrsearch=${encImage(query + " filetype:bitmap")}&gsrnamespace=6&gsrlimit=5&prop=imageinfo&iiprop=url&iiurlwidth=1400&format=json"
    val pages = imageJson(address).optJSONObject("query")?.optJSONObject("pages") ?: return ""
    val keys = pages.keys()
    while (keys.hasNext()) {
        val page = pages.optJSONObject(keys.next()) ?: continue
        val info = page.optJSONArray("imageinfo")?.optJSONObject(0) ?: continue
        val image = info.optString("thumburl").ifBlank { info.optString("url") }
        if (image.startsWith("https://")) return image
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
 * Tries independent image catalogues instead of silently giving up after one
 * Wikimedia Commons request. Results are cached for the current app process.
 */
suspend fun loadCommonsImage(query: String): String = withContext(Dispatchers.IO) {
    val normalized = query.trim()
    if (normalized.isBlank()) return@withContext ""
    imageCache[normalized.lowercase()]?.let { return@withContext it }
    val attempts = listOf<() -> String>(
        { wikipediaImage(normalized, "en") },
        { wikipediaImage(normalized, "es") },
        { commonsImage(normalized) },
        { openverseImage(normalized) }
    )
    for (attempt in attempts) {
        val image = try { attempt() } catch (_: Exception) { "" }
        if (image.startsWith("https://")) {
            imageCache[normalized.lowercase()] = image
            return@withContext image
        }
    }
    ""
}
