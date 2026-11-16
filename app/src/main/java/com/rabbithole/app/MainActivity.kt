package com.rabbithole.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101010)
private val Paper = Color(0xFFF7F7F5)
private val Muted = Color(0xFF777777)
private val Line = Color(0xFFE2E2E0)

data class Discovery(val title: String, val type: String, val description: String, val connections: List<String>)

private val library = listOf(
    Discovery("Nevermind", "ÁLBUM", "El disco que llevó el sonido de Seattle al centro de la cultura popular.", listOf("Nirvana", "In Utero", " grunge", "Documentales musicales")),
    Discovery("Nirvana", "ARTISTA", "Una banda que transformó el rock alternativo de los años noventa.", listOf("Nevermind", "In Utero", "Seattle", "Punk rock")),
    Discovery("In Utero", "ÁLBUM", "Un álbum más áspero e introspectivo, publicado en 1993.", listOf("Nirvana", "Steve Albini", "Rock alternativo")),
    Discovery("Seattle", "LUGAR", "Una ciudad clave para entender la escena musical del noroeste estadounidense.", listOf("Grunge", "Sub Pop", "Pearl Jam")),
    Discovery("Grunge", "MOVIMIENTO", "Una escena musical que mezcló punk, metal y sensibilidad alternativa.", listOf("Seattle", "Nirvana", "Soundgarden")),
    Discovery("Pink Floyd", "ARTISTA", "Una banda británica conocida por sus álbumes conceptuales y exploraciones sonoras.", listOf("The Dark Side of the Moon", "The Wall", "Rock progresivo")),
    Discovery("The Dark Side of the Moon", "ÁLBUM", "Un viaje conceptual sobre el tiempo, la presión y la experiencia humana.", listOf("Pink Floyd", "Wish You Were Here", "Rock progresivo")),
    Discovery("Daft Punk", "ARTISTA", "Dúo francés que conectó la música electrónica con el pop y la cultura visual.", listOf("Discovery", "Random Access Memories", "French house")),
    Discovery("Discovery", "ÁLBUM", "Un álbum electrónico lleno de melodías, samples y referencias al pop.", listOf("Daft Punk", "Interstella 5555", "French house")),
    Discovery("Interstella 5555", "DOCUMENTAL", "Una película animada que cuenta una historia musical sin diálogos convencionales.", listOf("Daft Punk", "Discovery", "Anime musical")),
    Discovery("Historia del cine", "TEMA", "Un recorrido por movimientos, directores, técnicas y películas que cambiaron el medio.", listOf("Cine mudo", "Nuevo Hollywood", "Cine documental")),
    Discovery("Historia espacial", "TEMA", "Misiones, descubrimientos científicos y momentos clave de la exploración espacial.", listOf("Apollo 11", "Voyager", "Telescopio Hubble"))
)

private val categories = listOf("Todo", "Música", "Cine", "Historia", "Ciencia")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RabbitHoleApp() }
    }
}

@Composable
fun RabbitHoleApp() {
    var query by remember { mutableStateOf("") }
    var activeTab by remember { mutableStateOf("Explorar") }
    var selectedCategory by remember { mutableStateOf("Todo") }
    var selected by remember { mutableStateOf<Discovery?>(null) }
    var saved by remember { mutableStateOf(setOf<String>()) }
    var history by remember { mutableStateOf(listOf<String>()) }

    val results = remember(query, selectedCategory) {
        if (query.isBlank()) library.take(6) else {
            val exact = library.filter { it.title.contains(query, true) || it.description.contains(query, true) || it.connections.any { c -> c.contains(query, true) } }
            if (exact.isNotEmpty()) exact else listOf(
                Discovery(query.trim().replaceFirstChar { it.uppercase() }, "TEMA", "Tu punto de partida. Explora conexiones, contextos y caminos relacionados con este tema.", listOf("Historia de ${query.trim()}", "Personas clave", "Obras relacionadas", "Documentales", "Contexto cultural"))
            )
        }
    }

    MaterialTheme(colorScheme = lightColorScheme(primary = Ink, onPrimary = Color.White, background = Paper, surface = Color.White, onSurface = Ink)) {
        Column(Modifier.fillMaxSize().background(Paper)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(34.dp).clip(CircleShape).background(Ink), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.size(18.dp)) {
                        drawCircle(Color.White, radius = size.minDimension * .35f, center = Offset(size.width * .43f, size.height * .42f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
                        drawLine(Color.White, Offset(size.width * .62f, size.height * .62f), Offset(size.width * .92f, size.height * .92f), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
                    }
                }
                Spacer(Modifier.width(10.dp))
                Text("RabbitHole", fontSize = 21.sp, fontWeight = FontWeight.Bold, letterSpacing = (-.7).sp)
                Spacer(Modifier.weight(1f))
                Text("DESCUBRE MÁS", fontSize = 9.sp, color = Muted, letterSpacing = 1.5.sp, fontWeight = FontWeight.SemiBold)
            }
            when (activeTab) {
                "Explorar" -> {
                    Text("Sigue tu curiosidad.", Modifier.padding(start = 22.dp, top = 20.dp), fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1.2).sp)
                    Text("Una idea puede llevarte mucho más lejos.", Modifier.padding(start = 22.dp, top = 5.dp, bottom = 20.dp), color = Muted, fontSize = 13.sp)
                    Row(Modifier.padding(horizontal = 22.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(horizontal = 15.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("⌕", fontSize = 25.sp, color = Muted)
                        Spacer(Modifier.width(10.dp))
                        BasicTextField(value = query, onValueChange = { query = it }, modifier = Modifier.weight(1f), singleLine = true, textStyle = TextStyle(color = Ink, fontSize = 15.sp), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), decorationBox = { inner -> if (query.isEmpty()) Text("Busca una banda, una idea, una época...", color = Muted, fontSize = 13.sp); inner() })
                        if (query.isNotEmpty()) Text("×", Modifier.clickable { query = "" }.padding(4.dp), fontSize = 20.sp)
                    }
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { category ->
                            val active = selectedCategory == category
                            Box(Modifier.clip(CircleShape).background(if (active) Ink else Color.White).clickable { selectedCategory = category }.padding(horizontal = 16.dp, vertical = 9.dp)) {
                                Text(category, color = if (active) Color.White else Ink, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (query.isBlank()) "PUNTOS DE PARTIDA" else "RESULTADOS", fontSize = 10.sp, color = Muted, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Text("${results.size} rutas", fontSize = 11.sp, color = Muted)
                    }
                    LazyColumn(contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 10.dp, bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                        items(results) { item ->
                            DiscoveryCard(item = item, onClick = { selected = item; history = (listOf(item.title) + history).distinct().take(30) }, isSaved = saved.contains(item.title), onSave = { saved = if (saved.contains(item.title)) saved - item.title else saved + item.title })
                        }
                    }
                }
                "Guardados" -> {
                    PageHeading("Tu colección", "Los descubrimientos que decidiste conservar.")
                    val items = library.filter { saved.contains(it.title) }
                    if (items.isEmpty()) EmptyState("Todavía no has guardado nada.", "Explora un tema y toca el marcador para conservarlo.")
                    else LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(items) { DiscoveryCard(it, { selected = it }, true) { saved = saved - it.title } }
                    }
                }
                else -> {
                    PageHeading("Tu recorrido", "Vuelve a los temas que has explorado.")
                    if (history.isEmpty()) EmptyState("Tu recorrido empieza aquí.", "Cada tema que abras aparecerá en este espacio.")
                    else LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(history) { title ->
                            val item = library.find { it.title == title } ?: Discovery(title, "TEMA", "Un tema que exploraste recientemente.", listOf("Contexto", "Personas clave", "Obras relacionadas"))
                            DiscoveryCard(item, { selected = item }, saved.contains(title)) { saved = saved + title }
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().background(Color.White).padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Explorar", "Guardados", "Recorrido").forEach { tab ->
                    Column(Modifier.clickable { activeTab = tab; selected = null }.padding(horizontal = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.width(22.dp).height(3.dp).clip(CircleShape).background(if (activeTab == tab) Ink else Color.Transparent))
                        Spacer(Modifier.height(7.dp))
                        Text(tab, fontSize = 11.sp, color = if (activeTab == tab) Ink else Muted, fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
        AnimatedVisibility(visible = selected != null, enter = fadeIn(), exit = fadeOut()) {
            selected?.let { DiscoveryDetail(it, saved.contains(it.title), onSave = { saved = if (saved.contains(it.title)) saved - it.title else saved + it.title }, onClose = { selected = null }, onExplore = { title ->
                val next = library.find { it.title.equals(title, true) } ?: Discovery(title, "NUEVA RUTA", "Una conexión para seguir explorando. Busca este tema para descubrir más.", listOf("Contexto", "Referencias", "Temas relacionados"))
                selected = next
                history = (listOf(next.title) + history).distinct().take(30)
            }) }
        }
    }
}

@Composable
private fun PageHeading(title: String, subtitle: String) {
    Text(title, Modifier.padding(start = 22.dp, top = 30.dp), fontSize = 29.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
    Text(subtitle, Modifier.padding(start = 22.dp, top = 6.dp, bottom = 20.dp), color = Muted, fontSize = 13.sp)
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(36.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("○", fontSize = 42.sp, color = Muted)
        Spacer(Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(5.dp))
        Text(subtitle, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun DiscoveryCard(item: Discovery, onClick: () -> Unit, isSaved: Boolean, onSave: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Paper), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(23.dp)) {
                drawCircle(Ink, radius = size.minDimension * .34f, center = center, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.7.dp.toPx()))
                drawCircle(Ink, radius = 2.4.dp.toPx(), center = center)
            }
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(item.type, color = Muted, fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(3.dp))
            Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(3.dp))
            Text(item.description, color = Muted, fontSize = 11.sp, maxLines = 2, lineHeight = 15.sp)
        }
        Text(if (isSaved) "●" else "＋", Modifier.clickable { onSave() }.padding(start = 10.dp, end = 2.dp), color = if (isSaved) Ink else Muted, fontSize = 17.sp)
    }
}

@Composable
private fun DiscoveryDetail(item: Discovery, isSaved: Boolean, onSave: () -> Unit, onClose: () -> Unit, onExplore: (String) -> Unit) {
    Column(Modifier.fillMaxSize().background(Paper)) {
        Row(Modifier.fillMaxWidth().padding(22.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("←  VOLVER", Modifier.clickable { onClose() }, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.weight(1f))
            Text("RABBIT HOLE", fontSize = 9.sp, color = Muted, letterSpacing = 1.5.sp)
        }
        Text(item.type, Modifier.padding(start = 24.dp, top = 26.dp), color = Muted, fontSize = 10.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Bold)
        Text(item.title, Modifier.padding(horizontal = 24.dp, vertical = 8.dp), fontSize = 35.sp, lineHeight = 39.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1.2).sp)
        Text(item.description, Modifier.padding(horizontal = 24.dp, vertical = 8.dp), color = Muted, fontSize = 14.sp, lineHeight = 21.sp)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.padding(horizontal = 24.dp).clip(CircleShape).background(Ink).clickable { onSave() }.padding(horizontal = 18.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (isSaved) "Guardado" else "Guardar descubrimiento", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Text("SIGUE EL HILO", Modifier.padding(start = 24.dp, top = 34.dp, bottom = 14.dp), fontSize = 10.sp, color = Muted, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold)
        LazyColumn(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            items(item.connections) { connection ->
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp)).background(Color.White).clickable { onExplore(connection) }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Ink))
                    Spacer(Modifier.width(13.dp))
                    Text(connection, Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("↗", color = Muted, fontSize = 18.sp)
                }
            }
        }
    }
}
