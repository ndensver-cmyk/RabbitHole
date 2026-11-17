package com.rabbithole.app

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF090909)
private val Paper = Color(0xFFF5F5F3)
private val Muted = Color(0xFF777777)
private val Rule = Color(0xFFE3E3E1)

data class Discovery(val title:String,val type:String,val category:String,val description:String,val connections:List<String>,val source:String="RabbitHole",val url:String="",val imageUrl:String="",val audioUrl:String="")
internal val catalog = listOf(
 Discovery("Nirvana","ARTISTA","Música","Nirvana nació en Aberdeen, Washington, en 1987, lejos de los grandes centros de la industria musical. Su historia suele contarse como el ascenso repentino de una banda, pero detrás de ese salto hubo años de conciertos pequeños, sellos independientes y una escena que estaba construyendo su propio lenguaje. La música del grupo unió melodías memorables con guitarras ásperas, estructuras punk y una sensibilidad que podía pasar de la calma a la explosión en segundos.\n\nLa formación más conocida —Kurt Cobain, Krist Novoselic y Dave Grohl— grabó discos que siguen abriendo conversaciones sobre composición, producción, fama y cultura juvenil. Escuchar a Nirvana hoy también permite mirar el mapa musical del noroeste estadounidense y entender cómo una escena local llegó a cambiar el sonido popular de los años noventa.",listOf("Nevermind","In Utero","Seattle","Grunge","Punk rock")),
 Discovery("Nevermind","ÁLBUM","Música","Publicado el 24 de septiembre de 1991, Nevermind fue el disco que llevó a Nirvana desde el circuito alternativo hasta una audiencia internacional. Su impacto no se explica solamente por el éxito de “Smells Like Teen Spirit”: el álbum construye un recorrido de canciones que alternan tensión, melodía, ruido y momentos de aparente fragilidad. La producción de Butch Vig ayudó a conservar la energía de la banda y, al mismo tiempo, hizo que sus contrastes llegaran con claridad a la radio y la televisión.\n\nEl disco se convirtió en un punto de inflexión para la industria musical y para la visibilidad del rock alternativo. Escucharlo completo permite apreciar cómo conviven los sencillos más conocidos con canciones menos citadas, y cómo su secuencia crea una identidad mucho más amplia que la de un solo himno generacional.",listOf("Nirvana","Butch Vig","Grunge","MTV Unplugged")),
 Discovery("In Utero","ÁLBUM","Música","Cuando Nirvana publicó In Utero en septiembre de 1993, el grupo ya conocía la dimensión inesperada de su éxito. En lugar de repetir la fórmula de Nevermind, presentó un álbum de sonido más áspero, directo y deliberadamente menos pulido. La grabación, asociada al trabajo de Steve Albini, deja escuchar con mayor crudeza los instrumentos y conserva una sensación de espacio y fricción que forma parte de su carácter.\n\nEl disco no es simplemente una reacción contra la fama: también es una obra llena de contrastes, con melodías delicadas que conviven con explosiones de ruido y letras abiertas a distintas interpretaciones. Recorrerlo permite observar a una banda que buscaba mantener el control de su lenguaje artístico mientras lidiaba con expectativas enormes. Es una puerta para explorar la producción musical, la presión de la industria y la evolución de Nirvana.",listOf("Nirvana","Steve Albini","Alternative rock")),
 Discovery("Seattle","ESCENA MUSICAL","Historia","Seattle se convirtió en el punto de referencia más visible del grunge, pero su historia musical es mucho más amplia que una etiqueta o una década. A finales de los años ochenta, salas pequeñas, tiendas de discos, sellos independientes y redes de músicos formaron un ecosistema creativo que permitió experimentar lejos de los centros tradicionales de la industria. Sub Pop fue uno de los sellos que ayudó a conectar esa escena con oyentes de otros lugares.\n\nEl clima, la geografía y la vida cultural de la ciudad suelen aparecer en los relatos sobre su sonido, aunque ninguna explicación única alcanza para describirlo. Para entender Seattle hay que escuchar bandas distintas, seguir sus colaboraciones y mirar cómo una comunidad local construyó oportunidades para circular su música. Esta ruta conecta el grunge con el punk, el rock independiente y la historia urbana de la ciudad.",listOf("Grunge","Sub Pop","Pearl Jam","Soundgarden")),
 Discovery("Grunge","MOVIMIENTO","Música","El grunge fue una escena y una etiqueta musical asociada principalmente con el noroeste de Estados Unidos durante finales de los años ochenta y comienzos de los noventa. Su sonido reunió influencias del punk, el heavy metal y el rock alternativo: guitarras densas, cambios bruscos de intensidad, melodías vulnerables y una producción que podía conservar asperezas en lugar de esconderlas. No existió una fórmula única; cada banda desarrolló una identidad propia.\n\nLa palabra también terminó describiendo una estética y una narrativa cultural que la prensa y la industria amplificaron cuando la música de Seattle alcanzó una audiencia masiva. Mirar el movimiento con más detalle significa distinguir entre las bandas, los sellos, los espacios y las personas que hicieron posible la escena. Nirvana fue una de sus puertas más conocidas, pero Soundgarden, Pearl Jam, Alice in Chains y muchos proyectos menos comerciales muestran un paisaje mucho más diverso.",listOf("Seattle","Nirvana","Soundgarden","Alice in Chains")),
 Discovery("Pink Floyd","ARTISTA","Música","Pink Floyd desarrolló una de las trayectorias más reconocibles del rock británico al combinar experimentación sonora, composiciones extensas y una atención poco habitual al concepto de álbum. Desde sus primeros trabajos psicodélicos hasta las grandes obras de los años setenta, el grupo convirtió el estudio de grabación en una herramienta creativa: efectos, capas instrumentales, voces, silencios y transiciones forman parte de una experiencia que a menudo se escucha como un recorrido completo.\n\nSu universo no se limita a la música. El diseño gráfico, las portadas, la iluminación y los espectáculos en vivo ayudaron a construir una identidad visual coherente y memorable. Explorar Pink Floyd permite seguir conexiones entre arte, tecnología, arquitectura sonora y cultura popular, además de descubrir cómo distintas etapas de una banda pueden dialogar sin sonar iguales.",listOf("The Dark Side of the Moon","The Wall","Rock progresivo")),
 Discovery("The Dark Side of the Moon","ÁLBUM","Música","Lanzado en 1973, The Dark Side of the Moon está construido como una experiencia continua. Sus canciones enlazan ideas sobre el tiempo, el dinero, el trabajo, el conflicto y la fragilidad humana mediante transiciones sonoras, voces, efectos de estudio y motivos musicales que reaparecen a lo largo del disco. Más que una colección de piezas aisladas, propone un viaje que invita a escucharse de principio a fin.\n\nLa producción de Pink Floyd convirtió el álbum en una referencia de la grabación multipista y del diseño sonoro, mientras que su portada —el prisma que descompone la luz— se volvió una imagen inseparable de su identidad. Esta historia conecta música, diseño editorial, ingeniería de audio y cultura visual. También permite observar cómo un concepto artístico puede mantenerse reconocible durante décadas sin perder la riqueza de sus detalles.",listOf("Pink Floyd","Wish You Were Here","Rock progresivo")),
 Discovery("Daft Punk","DÚO MUSICAL","Música","Daft Punk fue el proyecto de los músicos franceses Thomas Bangalter y Guy-Manuel de Homem-Christo. Desde la escena house de los noventa, el dúo construyó un lenguaje que mezcló disco, funk, electrónica y pop, con una obsesión especial por el ritmo, los samples y la textura del sonido. Cada etapa exploró una idea distinta: la energía de la pista de baile, la nostalgia por la música popular y la relación entre tecnología e identidad.\n\nSus cascos y su presencia pública cuidadosamente diseñada hicieron que el proyecto funcionara también como un universo visual. La música, los videoclips, el vestuario y los espectáculos formaron piezas de una misma narrativa. Seguir esta ruta permite descubrir cómo un dúo puede transformar referencias del pasado en algo nuevo, y cómo la imagen puede ampliar el significado de una obra sin sustituir a la música.",listOf("Discovery","Random Access Memories","French house")),
 Discovery("Discovery","ÁLBUM","Música","Publicado en 2001, Discovery llevó el sonido de Daft Punk hacia una mezcla luminosa de house, disco, funk y fragmentos de la memoria musical de sus integrantes. El álbum utiliza samples, voces procesadas y melodías directas para crear canciones que parecen familiares y futuristas al mismo tiempo. Su energía es bailable, pero también está llena de detalles de producción que recompensan la escucha con audífonos y la atención a cada capa.\n\nEl proyecto creció más allá del formato musical con Interstella 5555, una película animada que desarrolla una historia visual a partir de las canciones del disco. Esa conexión convirtió el álbum en un universo narrativo: cada tema puede abrir una escena, un personaje o una nueva lectura. Es una ruta ideal para explorar el sampling, la cultura pop, la animación y la forma en que un álbum puede convertirse en una experiencia audiovisual completa.",listOf("Daft Punk","Interstella 5555","French house")),
 Discovery("Interstella 5555","PELÍCULA MUSICAL","Cine","Interstella 5555: The 5tory of the 5ecret 5tar 5ystem es una película animada de ciencia ficción creada como acompañamiento visual del álbum Discovery de Daft Punk. Su relato sigue a una banda extraterrestre cuya música despierta el interés de un productor humano, y convierte las canciones del disco en capítulos de una aventura continua. La película utiliza la animación y la música como motores principales, sin depender de diálogos convencionales.\n\nSu vínculo con el álbum hace que la experiencia pueda recorrerse de dos maneras: escuchar las canciones y después reconocer sus imágenes, o mirar la película y descubrir cómo cada secuencia transforma la música en narrativa. La obra conecta la electrónica francesa con la animación japonesa y demuestra cómo una colaboración entre disciplinas puede crear un mundo coherente, reconocible y emocional.",listOf("Daft Punk","Discovery","Anime")),
 Discovery("Historia del cine","TEMA","Cine","La historia del cine es una sucesión de inventos, movimientos artísticos y formas de mirar el mundo. Desde las primeras imágenes en movimiento y el cine mudo hasta la llegada del sonido, el color, los efectos digitales y las plataformas actuales, cada transformación técnica abrió posibilidades narrativas y también cambió la manera en que las películas se producen, distribuyen y ven.\n\nPero el cine no avanza en línea recta. El expresionismo, el neorrealismo, la nouvelle vague, el Nuevo Hollywood y muchas otras corrientes respondieron a contextos sociales y culturales distintos. Explorar esta historia significa conectar directores, películas, tecnologías, estudios y públicos. Cada película puede funcionar como una puerta hacia su época: revela qué preocupaba a las personas, qué imágenes circulaban y qué nuevas formas de contar historias estaban apareciendo.",listOf("Cine mudo","Nuevo Hollywood","Cine documental")),
 Discovery("Apollo 11","MISIÓN ESPACIAL","Ciencia","Apollo 11 fue la misión de la NASA que llevó a los primeros seres humanos a la superficie lunar en julio de 1969. Neil Armstrong y Buzz Aldrin descendieron en el módulo lunar Eagle, mientras Michael Collins permaneció en órbita a bordo del módulo de mando. El alunizaje fue el resultado visible de un programa enorme que reunió investigación científica, ingeniería, fabricación, pruebas, entrenamiento y coordinación entre miles de personas.\n\nLa misión suele recordarse por sus imágenes históricas, pero también es una historia de decisiones técnicas y trabajo colectivo. El cohete Saturno V, los sistemas de navegación, las comunicaciones y los procedimientos de seguridad muestran la complejidad de llevar personas a otro mundo y traerlas de vuelta. Seguir Apollo 11 permite explorar la carrera espacial, la historia de la NASA y los desafíos que todavía acompañan a la exploración humana.",listOf("NASA","Saturno V","Carrera espacial","Neil Armstrong")),
 Discovery("Voyager","PROGRAMA ESPACIAL","Ciencia","Las sondas Voyager 1 y Voyager 2 fueron lanzadas en 1977 para aprovechar una oportunidad poco común de estudiar varios planetas exteriores durante una misma etapa de exploración. Sus encuentros con Júpiter y Saturno, y en el caso de Voyager 2 también con Urano y Neptuno, transformaron la comprensión de mundos que hasta entonces se conocían principalmente mediante observaciones lejanas. Las imágenes y mediciones enviadas a la Tierra revelaron atmósferas dinámicas, lunas complejas y sistemas de anillos sorprendentes.\n\nDespués de completar sus recorridos planetarios, ambas naves continuaron hacia el espacio interestelar. También llevan el Disco de Oro, una selección de sonidos, imágenes y saludos que busca representar aspectos de la vida en la Tierra. Voyager conecta ciencia planetaria, ingeniería de larga duración y una pregunta profundamente humana: cómo contar quiénes somos a una posible inteligencia que nunca hemos conocido.",listOf("NASA","Disco de oro","Júpiter","Saturno")),
 Discovery("Historia del diseño","TEMA","Historia","La historia del diseño muestra cómo las personas han dado forma a los objetos, los espacios y los mensajes que utilizan todos los días. No se limita a la decoración: reúne decisiones sobre función, materiales, producción, comunicación y experiencia. La industrialización, los nuevos sistemas de impresión, la arquitectura moderna y los medios digitales cambiaron las herramientas disponibles y también las expectativas sobre cómo deben funcionar las cosas.\n\nMovimientos como Arts and Crafts, Bauhaus, De Stijl y el diseño suizo propusieron distintas relaciones entre arte, industria y claridad visual. Sus ideas siguen presentes en productos, edificios, carteles e interfaces contemporáneas. Seguir esta ruta permite descubrir por qué una silla, una portada o una aplicación se ven y se sienten de cierta manera, y cómo las decisiones de diseño reflejan los valores y las posibilidades técnicas de cada época.",listOf("Bauhaus","Diseño suizo","Diseño industrial"))
)
private val starter = listOf("Nirvana","Universo","Cine de terror","Historia del diseño")

private fun interestMatches(item:Discovery,topic:String):Boolean{
 val text=(item.title+" "+item.category+" "+item.description+" "+item.connections.joinToString(" ")).lowercase()
 val terms=when(topic){
  "Rock alternativo","Grunge"->listOf("nirvana","grunge","rock","seattle","punk","soundgarden")
  "Música electrónica"->listOf("daft punk","electrónica","house","discovery")
  "Astronomía"->listOf("astronom","apollo","voyager","espacio","luna")
  "Ciencia ficción"->listOf("ciencia ficción","espacio","futuro","voyager")
  "Diseño gráfico","Diseño industrial"->listOf("diseño","bauhaus","tipografía","industrial")
  "Terror"->listOf("terror","horror","cine")
  "Documentales"->listOf("documental","historia","apollo")
  else->listOf(topic.lowercase().substringBefore(" "))
 }
 return terms.any{text.contains(it)}
}

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
  window.statusBarColor=android.graphics.Color.rgb(245,245,243)
  window.navigationBarColor=android.graphics.Color.rgb(245,245,243)
  window.decorView.systemUiVisibility=android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
  setContent{ RabbitHoleApp() }
 }
}

@Composable private fun RabbitHoleApp(){
 val context=LocalContext.current
 val prefs=context.getSharedPreferences("rabbithole_profile",0)
 var onboarded by remember{mutableStateOf(prefs.getBoolean("onboarded",false))}
 var interests by remember{mutableStateOf(prefs.getStringSet("interests",emptySet())?.toSet()?:emptySet())}
 var query by remember{mutableStateOf("")}
 var tab by remember{mutableStateOf("Inicio")}
 var category by remember{mutableStateOf("Todo")}
 var selected by remember{mutableStateOf<Discovery?>(null)}
 var saved by remember{mutableStateOf(setOf<String>())}
 var history by remember{mutableStateOf(listOf<String>())}
 var onlineResults by remember{mutableStateOf(emptyList<Discovery>())}
 var searching by remember{mutableStateOf(false)}
 if(!onboarded){ ExperienceOnboardingScreen(interests,{topic->interests=if(topic in interests)interests-topic else interests+topic}){prefs.edit().putBoolean("onboarded",true).putStringSet("interests",interests).apply();onboarded=true}; return }
 LaunchedEffect(query) {
  if(query.trim().length<2){onlineResults=emptyList();searching=false}
  else {
   delay(650)
   searching=true
   onlineResults=withContext(Dispatchers.IO){searchOpenSources(query)}
   searching=false
  }
 }
 val localMatches=catalog.filter{(category=="Todo"||it.category==category)&&(query.isBlank()||it.title.contains(query,true)||it.description.contains(query,true)||it.connections.any{c->c.contains(query,true)})}.sortedByDescending{item->if(interests.any{interestMatches(item,it)})1 else 0}
 val matches=(localMatches+onlineResults).distinctBy{"${it.source}:${it.title.lowercase()}"}
 val searchStatus=when{query.isBlank()->"Biblioteca inicial + fuentes abiertas";searching->"Consultando 10 fuentes gratuitas…";else->"${onlineResults.size} hallazgos externos · ${localMatches.size} locales"}
 MaterialTheme(colorScheme=lightColorScheme(primary=Ink,onPrimary=Color.White,background=Paper,surface=Color.White,onSurface=Ink)){
  Column(Modifier.fillMaxSize().background(Paper)){
   AnimatedContent(if(selected!=null)"Detalle" else tab,transitionSpec={(fadeIn(tween(260))+slideInVertically(tween(320)){it/9}) togetherWith (fadeOut(tween(180))+slideOutVertically(tween(220)){ -it/12 })},label="screen"){page->
    when(page){
     "Inicio"->ExperienceHome(query,{query=it},category,{category=it},matches,searchStatus,saved,{t->saved=if(t in saved)saved-t else saved+t},{item->selected=item;history=(listOf(item.title)+history).distinct().take(40)},{tab="Intereses"},{tab="Universo"})
     "Universo"->ExperienceMapScreen{item->selected=item;history=(listOf(item.title)+history).distinct().take(40)}
     "Intereses"->ExperienceOnboardingScreen(interests,{topic->interests=if(topic in interests)interests-topic else interests+topic}){prefs.edit().putBoolean("onboarded",true).putStringSet("interests",interests).apply();tab="Inicio"}
     "Guardados"->ExperienceLibraryScreen("Tu colección","Los descubrimientos que quieres conservar.",catalog.filter{it.title in saved},saved,{selected=it},{t->saved=saved-t})
     "Recorrido"->ExperienceLibraryScreen("Tu recorrido","Vuelve a los temas que ya exploraste.",history.mapNotNull{t->catalog.find{it.title==t}},saved,{selected=it},{t->saved=if(t in saved)saved-t else saved+t})
     else->ExperienceHome(query,{query=it},category,{category=it},matches,searchStatus,saved,{t->saved=if(t in saved)saved-t else saved+t},{selected=it},{tab="Intereses"},{tab="Universo"})
    }
   }
   if(selected==null) ExperienceNavigation(tab){tab=it}
  }
  selected?.let{item->ExperienceDetail(item,item.title in saved,{selected=null},{saved=if(item.title in saved)saved-item.title else saved+item.title}){connection->
   selected=catalog.find{it.title.equals(connection,true)}?:onlineResults.find{it.title.equals(connection,true)}?:Discovery(connection,"NUEVA RUTA","Todo","Una conexión para continuar tu exploración.",listOf("Contexto","Referencias","Ideas relacionadas"))
   history=(listOf(connection)+history).distinct().take(40)
  }}
 }
}

@Composable private fun Home(query:String,onQuery:(String)->Unit,category:String,onCategory:(String)->Unit,results:List<Discovery>,searchStatus:String,saved:Set<String>,onSave:(String)->Unit,onOpen:(Discovery)->Unit,onMap:()->Unit){
 val uriHandler=LocalUriHandler.current
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(bottom=20.dp)){
  item{
   Column(Modifier.padding(start=22.dp,end=22.dp,top=23.dp)){
    Text("THERE IS ALWAYS",fontSize=9.sp,color=Muted,letterSpacing=2.3.sp,fontWeight=FontWeight.Bold)
    Text("MORE TO\nDISCOVER.",fontSize=43.sp,lineHeight=43.sp,letterSpacing=(-2).sp,fontWeight=FontWeight.Black)
    Text("Empieza con una curiosidad. Termina en un universo.",Modifier.padding(top=10.dp),fontSize=12.sp,color=Muted)
    Spacer(Modifier.height(22.dp))
    Row(Modifier.fillMaxWidth().background(Color.White).border(1.dp,Rule,RoundedCornerShape(3.dp)).padding(15.dp),verticalAlignment=Alignment.CenterVertically){
     Text("⌕",fontSize=23.sp,color=Muted);Spacer(Modifier.width(10.dp))
     BasicTextField(query,onQuery,Modifier.weight(1f),singleLine=true,textStyle=TextStyle(color=Ink,fontSize=14.sp),keyboardOptions=KeyboardOptions(imeAction=ImeAction.Search),decorationBox={inner->if(query.isEmpty())Text("¿Qué te gustaría descubrir?",fontSize=13.sp,color=Muted);inner()})
     if(query.isNotEmpty())Text("×",Modifier.clickable{onQuery("")},fontSize=20.sp,color=Muted)
    }
    Text("PRUEBA CON",Modifier.padding(top=16.dp,bottom=9.dp),fontSize=9.sp,color=Muted,letterSpacing=1.7.sp,fontWeight=FontWeight.Bold)
    Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){starter.forEach{term->Text(term,Modifier.border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{onQuery(term)}.padding(horizontal=11.dp,vertical=8.dp),fontSize=10.sp)}}
    Spacer(Modifier.height(25.dp))
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(3.dp)).background(Ink).clickable{onMap()}.padding(17.dp),verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f)){
      Text("TU PRIMER PASO",fontSize=9.sp,color=Color.LightGray,letterSpacing=1.6.sp)
      Text("Conecta ideas.\nSigue el camino.",Modifier.padding(top=6.dp),fontSize=21.sp,lineHeight=23.sp,color=Color.White,fontWeight=FontWeight.Bold)
      Text("ABRIR EL UNIVERSO  ↗",Modifier.padding(top=13.dp),fontSize=9.sp,color=Color.White,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold)
     };UniverseGlyph(Modifier.size(94.dp))
    }
    Row(Modifier.padding(top=25.dp,bottom=10.dp),verticalAlignment=Alignment.CenterVertically){Text(if(query.isBlank())"PUNTOS DE PARTIDA" else "RESULTADOS",fontSize=9.sp,color=Muted,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.weight(1f));Text("${results.size} RUTAS",fontSize=8.sp,color=Muted)}
    Text(searchStatus,Modifier.padding(bottom=10.dp),fontSize=9.sp,color=Muted)
    if(query.trim().length>=2){
     Text("AMPLIAR BÚSQUEDA EN LA WEB",Modifier.padding(bottom=7.dp),fontSize=8.sp,color=Muted,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold)
     Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
      listOf("DuckDuckGo" to "https://duckduckgo.com/?q=${Uri.encode(query)}","Google" to "https://www.google.com/search?q=${Uri.encode(query)}","Bing" to "https://www.bing.com/search?q=${Uri.encode(query)}","Brave" to "https://search.brave.com/search?q=${Uri.encode(query)}","Qwant" to "https://www.qwant.com/?q=${Uri.encode(query)}","Mojeek" to "https://www.mojeek.com/search?q=${Uri.encode(query)}").forEach{(name,url)->Text(name,Modifier.border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{runCatching{uriHandler.openUri(url)}}.padding(horizontal=10.dp,vertical=8.dp),fontSize=9.sp,color=Ink)}
     }
     Spacer(Modifier.height(12.dp))
    }
    Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
     listOf("Todo","Música","Cine","Historia","Ciencia").forEach{c->val active=c==category;Text(c.uppercase(),Modifier.background(if(active)Ink else Color.White,RoundedCornerShape(3.dp)).border(1.dp,if(active)Ink else Rule,RoundedCornerShape(3.dp)).clickable{onCategory(c)}.padding(horizontal=11.dp,vertical=8.dp),fontSize=9.sp,color=if(active)Color.White else Ink,letterSpacing=.5.sp)}
    }
   }
  }
  items(results){item->DiscoveryTile(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.padding(horizontal=22.dp,vertical=4.dp))}
  if(results.isEmpty())item{Text("No encontramos esa ruta en esta primera biblioteca. Prueba con una sugerencia o cambia el tema.",Modifier.padding(22.dp),fontSize=12.sp,color=Muted)}
 }
}

@Composable private fun MapScreen(onOpen:(Discovery)->Unit){
 val nodes=listOf(Triple("Nirvana",.50f,.43f),Triple("Nevermind",.22f,.22f),Triple("In Utero",.78f,.22f),Triple("Seattle",.17f,.68f),Triple("Grunge",.82f,.67f),Triple("Pink Floyd",.50f,.86f))
 Column(Modifier.fillMaxSize().padding(horizontal=20.dp)){
  Text("YOUR UNIVERSE",Modifier.padding(top=20.dp),fontSize=9.sp,color=Muted,letterSpacing=2.sp)
  Text("Todo está\nconectado.",fontSize=34.sp,lineHeight=35.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.5).sp)
  Text("Toca cualquier nodo para seguir una ruta.",Modifier.padding(top=7.dp,bottom=15.dp),fontSize=12.sp,color=Muted)
  BoxWithConstraints(Modifier.fillMaxWidth().weight(1f).background(Color.White,RoundedCornerShape(4.dp)).border(1.dp,Rule,RoundedCornerShape(4.dp))){
   val w=constraints.maxWidth.toFloat();val h=constraints.maxHeight.toFloat()
   Canvas(Modifier.fillMaxSize()){
    val p=nodes.map{Offset(w*it.second,h*it.third)}
    listOf(0 to 1,0 to 2,0 to 3,0 to 4,0 to 5,1 to 3,2 to 4).forEach{(a,b)->drawLine(Color(0xFFBDBDBD),p[a],p[b],strokeWidth=1.dp.toPx())}
    p.forEachIndexed{i,point->drawCircle(if(i==0)Ink else Color(0xFFE6E6E6),if(i==0)28.dp.toPx() else 17.dp.toPx(),point);drawCircle(if(i==0)Color.White else Ink,if(i==0)4.dp.toPx() else 2.dp.toPx(),point)}
   }
   nodes.forEachIndexed{i,n->
    val item=catalog.find{it.title==n.first}?:catalog.first()
    Box(Modifier.offset(x=maxWidth*n.second-42.dp,y=maxHeight*n.third+23.dp).width(84.dp).clickable{onOpen(item)},contentAlignment=Alignment.Center){Text(n.first,fontSize=10.sp,fontWeight=if(i==0)FontWeight.Bold else FontWeight.Medium)}
    Box(Modifier.offset(x=maxWidth*n.second-22.dp,y=maxHeight*n.third-22.dp).size(44.dp).clickable{onOpen(item)})
   }
   Text("MUSIC / 001",Modifier.align(Alignment.BottomStart).padding(13.dp),fontSize=8.sp,color=Muted,letterSpacing=1.sp)
   Text("LIVE MAP",Modifier.align(Alignment.BottomEnd).padding(13.dp),fontSize=8.sp,color=Muted,letterSpacing=1.sp)
  }
  Text("Esta primera constelación es una muestra: abre un nodo para descubrir sus conexiones.",Modifier.padding(vertical=12.dp),fontSize=10.sp,color=Muted)
 }
}

@Composable private fun LegacyLibraryScreen(title:String,subtitle:String,items:List<Discovery>,saved:Set<String>,onOpen:(Discovery)->Unit,onSave:(String)->Unit){
 Column(Modifier.fillMaxSize()){
  Column(Modifier.padding(start=22.dp,end=22.dp,top=26.dp,bottom=18.dp)){Text("RABBIT HOLE / LIBRARY",fontSize=9.sp,color=Muted,letterSpacing=1.7.sp);Text(title,fontSize=34.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.4).sp);Text(subtitle,fontSize=12.sp,color=Muted)}
  if(items.isEmpty())Column(Modifier.fillMaxWidth().padding(35.dp),horizontalAlignment=Alignment.CenterHorizontally){Portal(Modifier.size(54.dp));Text("Todavía no hay nada aquí.",Modifier.padding(top=14.dp),fontSize=15.sp,fontWeight=FontWeight.Bold);Text("Explora un tema y guarda los descubrimientos que te interesen.",Modifier.padding(top=5.dp),fontSize=11.sp,color=Muted)}
  else LazyColumn(contentPadding=PaddingValues(bottom=20.dp)){items(items){item->DiscoveryTile(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.padding(horizontal=22.dp,vertical=4.dp))}}
 }
}

@Composable private fun DiscoveryTile(item:Discovery,isSaved:Boolean,onOpen:()->Unit,onSave:()->Unit,modifier:Modifier=Modifier){
 Row(modifier.fillMaxWidth().clip(RoundedCornerShape(3.dp)).background(Color.White).border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{onOpen()}.padding(13.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(38.dp).background(Paper,RoundedCornerShape(3.dp)),contentAlignment=Alignment.Center){Portal(Modifier.size(23.dp))}
  Spacer(Modifier.width(11.dp))
  Column(Modifier.weight(1f)){Text("${item.source.uppercase()}  ·  ${item.type}",fontSize=8.sp,color=Muted,letterSpacing=.7.sp,fontWeight=FontWeight.Bold);Text(item.title,fontSize=14.sp,fontWeight=FontWeight.SemiBold);Text(item.description,fontSize=10.sp,color=Muted,maxLines=2,lineHeight=14.sp)}
  Text(if(isSaved)"●" else "＋",Modifier.clickable{onSave()}.padding(start=10.dp),fontSize=17.sp,color=if(isSaved)Ink else Muted)
 }
}

@Composable private fun Detail(item:Discovery,saved:Boolean,onBack:()->Unit,onSave:()->Unit,onConnection:(String)->Unit){
 val uriHandler=LocalUriHandler.current
 var heroImage by remember(item.title){mutableStateOf(item.imageUrl)}
 LaunchedEffect(item.title){if(heroImage.isBlank())heroImage=loadCommonsImage(item.title)}
 Column(Modifier.fillMaxSize().background(Paper)){
  Row(Modifier.fillMaxWidth().padding(20.dp),verticalAlignment=Alignment.CenterVertically){Text("←  VOLVER",Modifier.clickable{onBack()},fontSize=10.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp);Spacer(Modifier.weight(1f));Text("RABBIT HOLE / DISCOVERY",fontSize=8.sp,color=Muted)}
  Column(Modifier.padding(horizontal=22.dp)){
   Box(Modifier.fillMaxWidth().height(185.dp).clip(RoundedCornerShape(3.dp)).background(Ink)){if(heroImage.isNotBlank())AsyncImage(model=heroImage,contentDescription="Imagen relacionada con "+item.title,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Crop);Box(Modifier.fillMaxSize().background(Color(0x55000000)));Text(item.type,Modifier.align(Alignment.BottomStart).padding(14.dp),fontSize=9.sp,color=Color.White,letterSpacing=1.6.sp)}
   Text("YOU FOUND A RABBIT HOLE",Modifier.padding(top=20.dp),fontSize=9.sp,color=Muted,letterSpacing=1.6.sp)
   Text(item.title,Modifier.padding(top=5.dp),fontSize=34.sp,lineHeight=37.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.4).sp)
   Text(item.description,Modifier.padding(top=9.dp),fontSize=13.sp,lineHeight=20.sp,color=Muted)
   Text("FUENTE: ${item.source.uppercase()}",Modifier.padding(top=12.dp),fontSize=9.sp,color=Muted,letterSpacing=1.sp)
   EditorialSections(item)
   if(item.audioUrl.isNotBlank())Text("ESCUCHAR FRAGMENTO  ↗",Modifier.padding(top=12.dp).clip(RoundedCornerShape(3.dp)).border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{runCatching{uriHandler.openUri(item.audioUrl)}}.padding(horizontal=12.dp,vertical=10.dp),fontSize=9.sp,color=Ink,fontWeight=FontWeight.Bold)
   if(item.url.isNotBlank()) Text("ABRIR FUENTE EXTERNA  ↗",Modifier.padding(top=8.dp).clip(RoundedCornerShape(3.dp)).border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{runCatching{uriHandler.openUri(item.url)}}.padding(horizontal=12.dp,vertical=9.dp),fontSize=9.sp,color=Ink,fontWeight=FontWeight.Bold,letterSpacing=.7.sp)
   Text(if(saved)"GUARDADO  ✓" else "GUARDAR DESCUBRIMIENTO  +",Modifier.padding(top=16.dp).clip(RoundedCornerShape(3.dp)).background(Ink).clickable{onSave()}.padding(horizontal=14.dp,vertical=10.dp),fontSize=9.sp,color=Color.White,letterSpacing=1.sp,fontWeight=FontWeight.Bold)
   Text(if(item.source=="RabbitHole")"SIGUE EL HILO" else "MÁS RESULTADOS DE ESTA BÚSQUEDA",Modifier.padding(top=24.dp,bottom=10.dp),fontSize=9.sp,color=Muted,letterSpacing=1.7.sp,fontWeight=FontWeight.Bold)
  }
  LazyColumn(contentPadding=PaddingValues(horizontal=22.dp,vertical=2.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){items(item.connections){connection->Row(Modifier.fillMaxWidth().background(Color.White,RoundedCornerShape(3.dp)).border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{onConnection(connection)}.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(7.dp).background(Ink,CircleShape));Spacer(Modifier.width(12.dp));Text(connection,Modifier.weight(1f),fontSize=12.sp,fontWeight=FontWeight.Medium);Text("↗",fontSize=16.sp,color=Muted)}}}
 }
}

@Composable private fun Portal(modifier:Modifier=Modifier){Canvas(modifier){val c=center;val d=size.minDimension;drawCircle(Ink,d*.46f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()));drawCircle(Ink,d*.29f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()));drawArc(Ink,210f,235f,false,Offset(d*.16f,d*.16f),androidx.compose.ui.geometry.Size(d*.68f,d*.68f),style=androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx(),cap=StrokeCap.Round));drawCircle(Ink,d*.05f,c)}}
@Composable private fun UniverseGlyph(modifier:Modifier=Modifier){Canvas(modifier){val d=size.minDimension;drawCircle(Color(0xFF777777),d*.44f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()));drawCircle(Color(0xFFBDBDBD),d*.30f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()));drawArc(Color.White,35f,270f,false,Offset(d*.19f,d*.19f),androidx.compose.ui.geometry.Size(d*.62f,d*.62f),style=androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx(),cap=StrokeCap.Round));drawCircle(Color.White,d*.12f);listOf(Offset(d*.5f,d*.02f),Offset(d*.91f,d*.38f),Offset(d*.8f,d*.88f),Offset(d*.12f,d*.75f)).forEach{drawCircle(Color.White,d*.025f,it)}}}
@Composable private fun Navigation(active:String,onSelect:(String)->Unit){Row(Modifier.fillMaxWidth().background(Color.White).border(1.dp,Rule).padding(vertical=9.dp),horizontalArrangement=Arrangement.SpaceEvenly){listOf("Inicio","Universo","Guardados","Recorrido").forEachIndexed{i,name->Column(Modifier.weight(1f).clickable{onSelect(name)},horizontalAlignment=Alignment.CenterHorizontally){Text(listOf("⌂","◎","◇","↺")[i],fontSize=17.sp,color=if(active==name)Ink else Muted);Text(name.uppercase(),fontSize=8.sp,color=if(active==name)Ink else Muted,letterSpacing=.5.sp,fontWeight=if(active==name)FontWeight.Bold else FontWeight.Normal)}}}}
