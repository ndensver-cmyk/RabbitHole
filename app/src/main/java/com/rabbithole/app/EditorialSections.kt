package com.rabbithole.app
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable fun EditorialSections(item:Discovery){
 val parts=when(item.title){
 "Nirvana"->listOf("CONTEXTO" to "Nirvana surgió en Aberdeen, Washington, en 1987. Su sonido conectó melodías pop con energía punk y guitarras distorsionadas.","POR QUÉ IMPORTA" to "Su éxito ayudó a llevar el rock alternativo desde circuitos independientes hacia una audiencia internacional y dio visibilidad a la escena del noroeste estadounidense.","PUNTOS CLAVE" to "La formación más conocida reunió a Kurt Cobain, Krist Novoselic y Dave Grohl. Sus álbumes centrales incluyen Bleach, Nevermind e In Utero.")
 "Nevermind"->listOf("CONTEXTO" to "Publicado el 24 de septiembre de 1991, fue el segundo álbum de estudio de Nirvana y fue producido por Butch Vig.","POR QUÉ IMPORTA" to "El álbum amplió el alcance del rock alternativo y convirtió a Nirvana en un fenómeno internacional.","PUNTOS CLAVE" to "Entre sus sencillos están Smells Like Teen Spirit, Come as You Are e In Bloom.")
 "Apollo 11"->listOf("CONTEXTO" to "Apollo 11 fue la misión de la NASA que llevó a los primeros seres humanos a la superficie lunar en julio de 1969.","POR QUÉ IMPORTA" to "Fue un logro tecnológico y científico construido a partir de años de investigación y trabajo de miles de personas.","PUNTOS CLAVE" to "Neil Armstrong y Buzz Aldrin descendieron a la superficie; Michael Collins permaneció en órbita. La tripulación regresó el 24 de julio.")
 "Voyager"->listOf("CONTEXTO" to "Voyager es un programa de exploración robótica de la NASA. Voyager 1 y Voyager 2 fueron lanzadas en 1977.","POR QUÉ IMPORTA" to "Sus sobrevuelos transformaron el conocimiento sobre Júpiter, Saturno, Urano y Neptuno.","PUNTOS CLAVE" to "Voyager 2 es la única nave que ha visitado Urano y Neptuno. Ambas sondas llevan un disco de oro con sonidos e imágenes de la Tierra.")
 "Bauhaus"->listOf("CONTEXTO" to "La escuela Bauhaus fue fundada por Walter Gropius en Weimar, Alemania, en 1919.","POR QUÉ IMPORTA" to "Su enseñanza integró arte, artesanía, arquitectura e industria y dejó una influencia profunda en el diseño moderno.","PUNTOS CLAVE" to "La escuela pasó por Weimar, Dessau y Berlín antes de cerrar en 1933. Entre sus docentes estuvieron Paul Klee y Wassily Kandinsky.")
 else->listOf("CONTEXTO" to item.description,"CÓMO EXPLORARLO" to "Observa el momento histórico, las personas y las ideas que rodean este tema. Contrasta las fuentes y sigue las conexiones para ampliar tu comprensión.","UNA RUTA POSIBLE" to "Empieza por la introducción, revisa la fuente original y continúa por los conceptos relacionados.")
 }
 Column(Modifier.fillMaxWidth()){parts.forEach{(heading,body)->Text(heading,Modifier.padding(top=20.dp,bottom=7.dp),fontSize=9.sp,color=Color(0xFF777570),letterSpacing=1.3.sp,fontWeight=FontWeight.Bold);Text(body,fontSize=12.sp,lineHeight=19.sp,color=Color(0xFF222222))}}
}