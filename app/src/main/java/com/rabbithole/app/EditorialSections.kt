package com.rabbithole.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditorialSections(item: Discovery) {
    val paragraphs = item.description.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
    val context = when (item.title) {
        "Nirvana" -> "La historia de Nirvana conecta tres escalas: una comunidad musical local, una identidad artística reconocible y una industria que cambió cuando el público empezó a escuchar algo distinto."
        "Nevermind" -> "Escúchalo como un álbum completo: sus cambios de intensidad, el orden de las canciones y la relación entre melodía y distorsión cuentan tanto como sus sencillos."
        "In Utero" -> "Su sonido no es un simple gesto de rechazo. Es una decisión de producción y una búsqueda expresiva que deja al descubierto los contrastes de la banda."
        "Seattle" -> "La escena se entiende mejor como una red: salas, sellos, tiendas, amistades y colaboraciones que permitieron que distintas bandas crecieran juntas."
        "Grunge" -> "Conviene separar el sonido de la etiqueta comercial. Las bandas compartían espacios e influencias, pero no una única fórmula ni una identidad idéntica."
        "Pink Floyd" -> "La música y la imagen avanzan juntas: portadas, iluminación y diseño de conciertos forman parte de cómo el grupo construyó sus álbumes como experiencias."
        "The Dark Side of the Moon" -> "La continuidad entre canciones, los efectos de estudio y el diseño de portada son piezas de una misma idea: convertir un álbum en un mundo."
        "Daft Punk" -> "El proyecto demuestra cómo una identidad visual puede ampliar una propuesta musical sin quitarle protagonismo al sonido."
        "Discovery" -> "El álbum abre una segunda lectura cuando se conecta con Interstella 5555: la música deja de ser solo una secuencia de canciones y se convierte en relato."
        "Interstella 5555" -> "La película funciona como una colaboración entre música y animación: cada canción aporta una parte del relato y cada imagen modifica la forma de escucharla."
        "Historia del cine" -> "Una película también es un documento de su época: sus técnicas, temas y formas de distribución revelan qué estaba cambiando alrededor."
        "Apollo 11" -> "El alunizaje fue una hazaña colectiva. Detrás de las imágenes más famosas hubo miles de decisiones de ingeniería, pruebas y procedimientos."
        "Voyager" -> "Las sondas son máquinas científicas y cápsulas culturales: estudian otros mundos y, al mismo tiempo, llevan un mensaje simbólico de la Tierra."
        else -> "El diseño conecta intención y uso. Observar materiales, contexto y decisiones formales permite entender por qué un objeto o una imagen funcionan como funcionan."
    }
    Column(Modifier.fillMaxWidth()) {
        if (paragraphs.isNotEmpty()) {
            Text("01  /  EL CONTEXTO", color = Color(0xFF77736D), fontSize = 9.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 10.dp))
            Text(paragraphs[0], color = Color(0xFF171715), fontSize = 17.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium)
        }
        if (paragraphs.size > 1) {
            Row(Modifier.fillMaxWidth().padding(top = 22.dp, bottom = 17.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Box(Modifier.width(34.dp).height(2.dp).background(Color(0xFF171715)))
                Text("  02  /  MIRAR MÁS DE CERCA", color = Color(0xFF77736D), fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Black)
            }
            Text(paragraphs.drop(1).joinToString("\n\n"), color = Color(0xFF45423D), fontSize = 14.sp, lineHeight = 24.sp)
        }
        Column(Modifier.fillMaxWidth().padding(top = 25.dp).background(Color(0xFF111110), RoundedCornerShape(2.dp)).padding(19.dp)) {
            Text("UNA CLAVE PARA ENTENDERLO", color = Color(0xFFB9B6AF), fontSize = 8.sp, letterSpacing = 1.7.sp, fontWeight = FontWeight.Black)
            Text(context, color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 10.dp))
            Text("RUTA DE EXPLORACIÓN  /  "+item.connections.take(3).joinToString("  ·  ").uppercase(), color = Color(0xFFB9B6AF), fontSize = 8.sp, lineHeight = 13.sp, letterSpacing = .7.sp, modifier = Modifier.padding(top = 15.dp))
        }
    }
}
