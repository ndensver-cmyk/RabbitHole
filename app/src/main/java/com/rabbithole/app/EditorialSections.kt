package com.rabbithole.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val EditorialInk = Color(0xFF111110)
private val EditorialGrey = Color(0xFF77736D)

private fun extendedStory(item: Discovery): List<String> = when (item.title) {
    "Nirvana" -> listOf(
        "El contexto importa tanto como las canciones. Aberdeen no era un centro de la industria musical y precisamente por eso la trayectoria del grupo se entiende mejor desde los circuitos independientes: conciertos en espacios pequeños, grabaciones de bajo presupuesto y una comunidad de músicos que compartía escenarios e influencias. El sonido no apareció de la nada; fue tomando forma entre el punk, el rock alternativo y distintas maneras de usar el contraste entre melodía y distorsión.",
        "La dinámica del trío fue una parte decisiva de su identidad. La batería de Dave Grohl aportó una fuerza muy reconocible, el bajo de Krist Novoselic sostuvo arreglos sencillos pero efectivos y la escritura de Kurt Cobain combinó ganchos inmediatos con una expresividad ambigua. Esa mezcla permitió que canciones intensas fueran también memorables y accesibles sin perder su aspereza.",
        "La fama transformó la escala de todo lo que rodeaba a la banda. Por eso su historia también sirve para hablar de la relación entre escenas independientes y grandes discográficas, de la presión sobre los artistas y de cómo los medios pueden convertir un movimiento diverso en una etiqueta. Escuchar sus discos en orden permite notar que Nirvana nunca fue una sola fórmula."
    )
    "Nevermind" -> listOf(
        "El álbum está construido alrededor de los cambios de energía. Muchas canciones alternan pasajes contenidos con estribillos explosivos, una estructura que hace que la distorsión tenga impacto sin borrar la melodía. Esa arquitectura se percibe mejor al escuchar el disco completo y no únicamente sus sencillos más conocidos.",
        "La producción de Butch Vig y la mezcla posterior de Andy Wallace ayudaron a definir el carácter del álbum: guitarras densas, batería presente y voces que conservan una mezcla de vulnerabilidad y fuerza. El resultado acercó una estética alternativa a una audiencia enorme, pero también abrió discusiones sobre cuánto debía pulirse el sonido de una banda independiente.",
        "Su legado es doble. Por un lado, cambió la visibilidad comercial del rock alternativo en los noventa; por otro, dejó una referencia de composición basada en el contraste, la repetición y la claridad melódica. No hace falta conocer toda la historia del grunge para reconocer cómo el disco organiza tensión y descanso."
    )
    "In Utero" -> listOf(
        "La grabación con Steve Albini buscó una sensación distinta a la de Nevermind. El sonido es más crudo y deja que los instrumentos respiren; las voces y las guitarras no siempre ocupan el centro de una mezcla brillante, sino que forman parte de una textura más áspera. Esa decisión ayuda a entender por qué el álbum se siente tan físico.",
        "Aun con esa crudeza, el disco no renuncia a la melodía. Canciones de gran intensidad conviven con momentos más delicados y arreglos que revelan una escritura cuidadosa. La tensión entre belleza y ruido no es un accidente: es uno de los principios que ordenan el álbum.",
        "Escucharlo junto a Nevermind permite comparar dos maneras de registrar a la misma banda. La diferencia no es simplemente comercial o estética; también habla de control creativo, expectativas externas y de la posibilidad de cambiar de lenguaje cuando el público espera una repetición."
    )
    "Seattle" -> listOf(
        "La escena de Seattle fue una red antes que un género. Salas, tiendas de discos, sellos, estudios, amistades y colaboraciones permitieron que grupos con sonidos diferentes compartieran un entorno. Sub Pop ayudó a documentar y difundir parte de esa actividad, pero no fue el único elemento que la hizo posible.",
        "El término grunge terminó reuniendo propuestas que no eran idénticas. Algunas bandas se acercaban más al punk, otras al metal o al rock psicodélico. La etiqueta facilitó que la prensa y la industria contaran una historia sencilla, aunque la escena real era más variada y estaba conectada con décadas de música alternativa.",
        "Mirar la ciudad desde su infraestructura cultural cambia la pregunta. En lugar de buscar una fórmula sonora, conviene observar cómo se crean espacios para tocar, cómo circulan las grabaciones y qué relaciones permiten que una comunidad local llegue a oyentes de otros países."
    )
    "Grunge" -> listOf(
        "El grunge suele reconocerse por guitarras saturadas, dinámicas fuertes y una estética deliberadamente poco pulida, pero esos rasgos no forman una receta universal. Las bandas asociadas al movimiento tenían influencias y objetivos distintos; algunas venían del punk, otras del heavy metal y otras de escenas independientes anteriores.",
        "La explosión comercial de comienzos de los noventa convirtió una escena regional en una categoría internacional. La prensa, las radios y las discográficas ayudaron a difundirla, pero también simplificaron sus diferencias. Esa tensión entre comunidad musical y etiqueta de mercado es parte esencial de su historia.",
        "Para explorarlo con criterio, compara discos y no solo apariencias. Escucha cómo cada banda maneja el tempo, la distorsión, las melodías y los silencios. Las diferencias entre grupos dicen tanto como los elementos que comparten."
    )
    "Pink Floyd" -> listOf(
        "Pink Floyd desarrolló una forma de álbum en la que música, arte gráfico, iluminación y puesta en escena se refuerzan entre sí. Sus discos invitan a escuchar secuencias completas: motivos que regresan, transiciones que conectan canciones y efectos de estudio que crean espacios sonoros.",
        "La experimentación técnica fue importante, pero no explica todo. La banda también trabajó con estructuras, letras e imágenes capaces de convertir temas abstractos —el tiempo, la alienación, la memoria o el poder— en experiencias reconocibles para el oyente.",
        "Sus distintas etapas muestran cambios de integrantes y de dirección artística. Seguir esa evolución permite entender que una identidad musical no es fija: se construye, se discute y puede transformarse sin perder todas sus conexiones con el pasado."
    )
    "The Dark Side of the Moon" -> listOf(
        "El álbum funciona como una secuencia continua. Los enlaces entre canciones, los sonidos ambientales y los motivos que reaparecen hacen que la escucha se parezca más a recorrer una obra completa que a reproducir una colección de temas separados.",
        "El trabajo de estudio fue central para su identidad. Voces, relojes, fragmentos hablados, sintetizadores y guitarras se organizan como capas que amplían el significado de cada canción. La tecnología no aparece como adorno: ayuda a construir el espacio narrativo del disco.",
        "Su portada se volvió inseparable de la música. El prisma es una imagen simple y reconocible que traduce visualmente ideas de luz, espectro y percepción. Es un buen ejemplo de cómo el diseño gráfico puede condensar el concepto de un álbum sin explicarlo literalmente."
    )
    "Daft Punk" -> listOf(
        "Daft Punk construyó una identidad reconocible al combinar música electrónica, cultura de club, referencias al funk y una puesta en escena cuidadosamente diseñada. Los cascos y la imagen robótica no son solo disfraces: funcionan como una manera de desplazar la atención desde la vida privada de los músicos hacia el universo del proyecto.",
        "Su sonido se desarrolló mediante samples, secuenciadores, sintetizadores y una relación muy consciente con la historia de la música popular. La nostalgia aparece transformada: materiales del pasado se reorganizan con precisión y se convierten en algo nuevo.",
        "El proyecto también demuestra que una identidad visual puede ser narrativa. Portadas, videos, vestuario y conciertos forman un sistema coherente que permite reconocer una obra incluso antes de escucharla."
    )
    "Discovery" -> listOf(
        "Discovery toma referencias de la música disco, el funk, el pop y la electrónica de baile, y las convierte en canciones de estructura clara y energía luminosa. Los samples no son simples citas: se editan y reorganizan hasta formar una identidad propia.",
        "La producción combina instrumentos electrónicos con texturas que recuerdan grabaciones de otras épocas. Esa mezcla crea una sensación familiar y futurista al mismo tiempo, una de las razones por las que el álbum funciona como un viaje cohesionado.",
        "Su relación con Interstella 5555 amplía la experiencia. Las canciones se convierten en escenas de una historia animada sin diálogos, de modo que la música guía el relato y las imágenes ofrecen otra manera de interpretarla."
    )
    "Interstella 5555" -> listOf(
        "La película cuenta su historia principalmente a través de imágenes y música. Al no depender de diálogos convencionales, la animación debe comunicar emociones, cambios de escenario y conflictos mediante el ritmo visual y la relación directa con las canciones.",
        "La colaboración entre Daft Punk y el equipo de animación asociado a Leiji Matsumoto une dos lenguajes muy distintos: la electrónica francesa y una tradición visual de ciencia ficción japonesa. El resultado no se siente como una colección de videoclips independientes, sino como una narración continua.",
        "Verla después de escuchar Discovery cambia la experiencia del álbum. Algunas melodías adquieren una dimensión narrativa y ciertos momentos instrumentales funcionan como transiciones dramáticas. También puede recorrerse al revés: la película invita a volver a las canciones con nuevas asociaciones."
    )
    "Historia del cine" -> listOf(
        "La historia del cine no es una línea recta de inventos que sustituyen a otros. Es una sucesión de tecnologías, lenguajes y modelos de producción que conviven, compiten y se transforman. Cada etapa cambia lo que puede filmarse, cómo se distribuyen las películas y qué espera el público.",
        "El montaje, el sonido sincronizado, el color, los efectos visuales y los formatos digitales modificaron tanto la técnica como la narración. Una innovación se vuelve importante cuando los cineastas encuentran nuevas formas de contar historias con ella, no solo porque el equipo sea más avanzado.",
        "También hay que mirar más allá de Hollywood. Los movimientos de distintas regiones han ampliado las posibilidades del medio y han contado historias desde contextos sociales y culturales muy diferentes. Comparar películas de épocas y países distintos permite ver cómo el cine registra su tiempo."
    )
    "Apollo 11" -> listOf(
        "El programa Apollo fue el resultado de una enorme coordinación entre astronautas, ingenieros, técnicos, equipos científicos y centros de control. La imagen de los primeros pasos en la Luna resume una operación que dependió de miles de procedimientos y decisiones tomadas en tierra y durante el vuelo.",
        "La misión Apollo 11 tuvo que resolver problemas de navegación, comunicaciones, soporte vital y control del módulo lunar. Cada fase —el lanzamiento, el viaje, el alunizaje, la actividad en la superficie y el regreso— exigía márgenes de seguridad y planes alternativos.",
        "Su importancia va más allá del símbolo político de la carrera espacial. También dejó avances técnicos, datos científicos y una referencia cultural que sigue influyendo en cómo imaginamos la exploración. Revisar la misión con atención permite distinguir el logro humano de los mitos que a veces lo rodean."
    )
    "Voyager" -> listOf(
        "Las misiones Voyager fueron diseñadas para aprovechar una rara oportunidad de alineación planetaria y estudiar varios mundos exteriores. Sus instrumentos enviaron información que transformó el conocimiento sobre Júpiter, Saturno, Urano y Neptuno, y sus imágenes cambiaron la manera de imaginar esos planetas.",
        "Aunque las sondas ya no realizan sobrevuelos planetarios, continúan comunicando mediciones desde regiones muy alejadas del Sol. La distancia convierte cada intercambio de datos en un reto: la señal es débil, el tiempo de comunicación aumenta y la energía disponible disminuye lentamente.",
        "El Disco de Oro añade una dimensión cultural a la misión. Reúne sonidos, música e imágenes seleccionados como una presentación de la Tierra. Es un gesto simbólico, no un mensaje garantizado para encontrar destinatario, y por eso también invita a pensar qué elegimos contar sobre nosotros mismos."
    )
    else -> listOf(
        "Para comprender este tema conviene observar su origen, las personas que lo desarrollaron y las condiciones que hicieron posible su aparición. Ninguna obra o idea surge aislada: suele responder a preguntas anteriores, herramientas disponibles y conversaciones de su época.",
        "Una segunda lectura consiste en mirar sus decisiones concretas. Los materiales, métodos, influencias y límites ayudan a explicar por qué el resultado tiene esa forma y no otra. Compararlo con ejemplos cercanos permite distinguir sus aportes de los rasgos que comparte con otros trabajos.",
        "La mejor manera de continuar es seguir las conexiones, contrastar fuentes y formular nuevas preguntas. Este dossier es un punto de partida para explorar, no una conclusión definitiva."
    )
}

@Composable
fun EditorialSections(item: Discovery) {
    val original = item.description.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
    val sections = (original + extendedStory(item)).distinct().take(5)
    val wordCount = sections.joinToString(" ").split(Regex("\\s+")).count { it.isNotBlank() }
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(bottom = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("LECTURA", color = EditorialGrey, fontSize = 8.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(8.dp))
            Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFD8D4CC)))
            Text("  ${wordCount} PALABRAS", color = EditorialGrey, fontSize = 8.sp, letterSpacing = .8.sp, fontWeight = FontWeight.Bold)
        }
        Text("01  /  EL CONTEXTO", color = EditorialGrey, fontSize = 9.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 10.dp))
        if (sections.isNotEmpty()) {
            Text(sections[0], color = EditorialInk, fontSize = 18.sp, lineHeight = 29.sp, fontWeight = FontWeight.Medium)
        }
        if (sections.size > 1) {
            Text("02  /  LO QUE HAY DETRÁS", color = EditorialGrey, fontSize = 9.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 27.dp, bottom = 10.dp))
            Text(sections[1], color = Color(0xFF45423D), fontSize = 14.sp, lineHeight = 25.sp)
        }
        Column(Modifier.fillMaxWidth().padding(top = 25.dp, bottom = 25.dp).background(EditorialInk, RoundedCornerShape(2.dp)).padding(20.dp)) {
            Text("UNA CLAVE PARA ENTENDERLO", color = Color(0xFFB9B6AF), fontSize = 8.sp, letterSpacing = 1.7.sp, fontWeight = FontWeight.Black)
            Text(keyFor(item.title), color = Color.White, fontSize = 18.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 11.dp))
        }
        sections.drop(2).forEachIndexed { index, paragraph ->
            Text("0${index + 3}  /  ${listOf("UNA CAPA MÁS", "SU HUELLA", "PARA SEGUIR")[index.coerceAtMost(2)]}", color = EditorialGrey, fontSize = 9.sp, letterSpacing = 1.8.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 8.dp, bottom = 10.dp))
            Text(paragraph, color = Color(0xFF45423D), fontSize = 14.sp, lineHeight = 25.sp)
        }
        if (item.connections.isNotEmpty()) {
            Text("SIGUE EXPLORANDO  /  " + item.connections.take(3).joinToString("  ·  ").uppercase(), color = EditorialGrey, fontSize = 8.sp, lineHeight = 14.sp, letterSpacing = .7.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 26.dp))
        }
    }
}

private fun keyFor(title: String): String = when (title) {
    "Nirvana" -> "Nirvana se entiende mejor como el cruce entre una escena independiente, tres músicos con lenguajes complementarios y una industria que no supo anticipar el alcance de aquella música."
    "Nevermind" -> "El contraste es la arquitectura del disco: tensión y descanso, ruido y melodía, intimidad y explosión."
    "In Utero" -> "La crudeza no es falta de cuidado. Es una decisión de sonido que deja oír la fricción y los contrastes de la banda."
    "Seattle" -> "Más que un sonido único, Seattle fue una comunidad creativa sostenida por espacios, sellos, amistades y circulación musical."
    "Grunge" -> "La etiqueta reúne bandas distintas. Escuchar sus diferencias evita confundir un movimiento cultural con una fórmula."
    "Pink Floyd" -> "La música, el diseño gráfico y la puesta en escena forman parte de una misma experiencia artística."
    "The Dark Side of the Moon" -> "El álbum funciona como una obra continua: sonido, secuencia y portada comparten una misma idea."
    "Daft Punk" -> "La identidad visual no acompaña desde afuera: amplía el mundo musical y lo convierte en un proyecto reconocible."
    "Discovery" -> "Samples, melodías y ritmo convierten referencias del pasado en una experiencia pop electrónica con identidad propia."
    "Interstella 5555" -> "La música no ilustra la película: es el motor que organiza su relato visual."
    "Historia del cine" -> "Cada cambio técnico importa cuando transforma las historias que se pueden contar y la manera de verlas."
    "Apollo 11" -> "El alunizaje fue una hazaña colectiva, construida con ingeniería, coordinación y miles de decisiones precisas."
    "Voyager" -> "Las sondas son exploradoras científicas y, al mismo tiempo, una cápsula simbólica de la humanidad."
    else -> "Entender un tema exige mirar sus decisiones, su contexto y las conexiones que lo rodean."
}
