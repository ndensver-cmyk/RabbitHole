package com.rabbithole.app
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val XBlack=Color(0xFF090909)
private val XPaper=Color(0xFFF3F1EC)
private val XWhite=Color.White
private val XMuted=Color(0xFF8D8A84)
private val XLine=Color(0xFFDEDAD2)
private val XPanel=Color(0xFF191917)
private fun posterFor(item:Discovery)=item.imageUrl
@Composable private fun Tag(text:String,color:Color=XMuted){Text(text.uppercase(),color=color,fontSize=9.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold)}
@Composable private fun OrbitMark(modifier:Modifier=Modifier,color:Color=XWhite){
 val t=rememberInfiniteTransition(label="orbit")
 val angle by t.animateFloat(0f,360f,infiniteRepeatable(tween(10000)),label="angle")
 val pulse by t.animateFloat(.88f,1.13f,infiniteRepeatable(tween(1500),RepeatMode.Reverse),label="pulse")
 Canvas(modifier){val d=size.minDimension;val c=Offset(size.width/2,size.height/2);drawCircle(color.copy(alpha=.25f),d*.47f,c,style=Stroke(1.dp.toPx()));drawCircle(color.copy(alpha=.48f),d*.31f,c,style=Stroke(1.dp.toPx()));drawCircle(color.copy(alpha=.8f),d*.11f*pulse,c);val a=Math.toRadians(angle.toDouble());val p=Offset(c.x+kotlin.math.cos(a).toFloat()*d*.42f,c.y+kotlin.math.sin(a).toFloat()*d*.42f);drawLine(color.copy(alpha=.55f),c,p,1.dp.toPx());drawCircle(color,d*.035f,p)}
}
private fun posterAssetName(title:String):String? = when(title.trim().lowercase()){
 "nirvana"->"poster_nirvana.jpg"
 "nevermind"->"poster_nevermind.jpg"
 "in utero"->"poster_inutero.jpg"
 "seattle"->"poster_seattle.jpg"
 "grunge"->"poster_grunge.jpg"
 "pink floyd"->"poster_pink_floyd.jpg"
 "the dark side of the moon"->"poster_dark_side.jpg"
 "daft punk"->"poster_daftpunk.jpg"
 "discovery"->"poster_discovery.jpg"
 "interstella 5555"->"poster_interstella.jpg"
 "historia del cine"->"poster_cinema.jpg"
 "apollo 11"->"poster_apollo11.jpg"
 "voyager"->"poster_voyager.jpg"
 "historia del diseño"->"poster_design_history.jpg"
 "foo fighters"->"poster_foo_fighters.jpg"
 "pearl jam"->"poster_pearl_jam.jpg"
 "soundgarden"->"poster_soundgarden.jpg"
 "alice in chains"->"poster_alice_chains.jpg"
 "radiohead"->"poster_radiohead.jpg"
 "ok computer"->"poster_ok_computer.jpg"
 "the beatles"->"poster_beatles.jpg"
 "abbey road"->"poster_abbey_road.jpg"
 "david bowie"->"poster_bowie.jpg"
 "kraftwerk"->"poster_kraftwerk.jpg"
 "blade runner"->"poster_blade_runner.jpg"
 "akira"->"poster_akira.jpg"
 "ghost in the shell"->"poster_ghost_shell.jpg"
 "the matrix"->"poster_matrix.jpg"
 "studio ghibli"->"poster_studio_ghibli.jpg"
 "the shining"->"poster_shining.jpg"
 "bauhaus"->"poster_bauhaus.jpg"
 "brutalismo"->"poster_brutalism.jpg"
 "titanic"->"poster_titanic.jpg"
 "pompeya"->"poster_pompeii.jpg"
 "antiguo egipto"->"poster_egypt.jpg"
 "chernóbil"->"poster_chernobyl.jpg"
 "agujeros negros"->"poster_black_hole.jpg"
 "telescopio james webb"->"poster_james_webb.jpg"
 "marte"->"poster_mars.jpg"
 "mars rovers"->"poster_mars_rover.jpg"
 "linux"->"poster_linux.jpg"
 "world wide web"->"poster_world_wide_web.jpg"
 "twin peaks"->"poster_twin_peaks.jpg"
 "historia de la fotografía"->"poster_photo_history.jpg"
 else->null
}
@Composable private fun Photo(item:Discovery,modifier:Modifier=Modifier){
 val context=LocalContext.current
 var localPoster by remember(item.title){mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null)}
 var image by remember(item.title){mutableStateOf(posterFor(item))}
 var failed by remember(item.title){mutableStateOf(false)}
 var retried by remember(item.title){mutableStateOf(false)}
 LaunchedEffect(item.title){
  val asset=posterAssetName(item.title)
  if(asset!=null){
   localPoster=withContext(Dispatchers.IO){
    runCatching{context.assets.open("posters/"+asset).use{BitmapFactory.decodeStream(it)?.asImageBitmap()}}.getOrNull()
   }
  }
  if(localPoster==null&&image.isBlank())image=loadCommonsImage(item.title)
 }
 LaunchedEffect(failed){
  if(failed&&!retried&&localPoster==null){
   retried=true
   val alternative=loadCommonsImage(item.title)
   if(alternative.isNotBlank()&&alternative!=image){image=alternative;failed=false}
  }
 }
 Box(modifier.clip(RoundedCornerShape(3.dp)).background(Brush.linearGradient(listOf(Color(0xFF494640),XBlack)))){
  when{
   localPoster!=null->Image(bitmap=localPoster!!,contentDescription="Imagen de "+item.title,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Crop,colorFilter=ColorFilter.colorMatrix(ColorMatrix().apply{setToSaturation(.28f)}))
   image.isNotBlank()&&!failed->AsyncImage(model=ImageRequest.Builder(context).data(image).crossfade(true).allowHardware(false).build(),contentDescription="Imagen de "+item.title,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Crop,onError={failed=true})
   else->Box(Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color(0xFF494640),Color(0xFF201F1C),XBlack)))){
    OrbitMark(Modifier.align(Alignment.Center).fillMaxSize(.62f),Color.White.copy(alpha=.82f))
    Text(item.title.uppercase(),Modifier.align(Alignment.BottomStart).padding(12.dp),color=Color.White.copy(alpha=.82f),fontSize=12.sp,lineHeight=14.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp,maxLines=3)
    Text(if(failed)"IMAGEN NO DISPONIBLE" else "BUSCANDO IMAGEN",Modifier.align(Alignment.TopStart).padding(11.dp),color=Color.White.copy(alpha=.65f),fontSize=7.sp,letterSpacing=1.sp,fontWeight=FontWeight.Bold)
   }
  }
  Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x16000000),Color.Transparent,Color(0xBB000000)))))
 }
}
@Composable
fun ExperienceOnboardingScreen(selected:Set<String>,onToggle:(String)->Unit,onContinue:()->Unit){
 val groups=linkedMapOf("Música" to listOf("Rock alternativo","Grunge","Música electrónica","Hip-hop","Jazz","Música clásica"),"Cine y series" to listOf("Cine de autor","Ciencia ficción","Terror","Documentales","Animación"),"Arte y diseño" to listOf("Diseño gráfico","Arquitectura","Fotografía","Arte moderno","Diseño industrial"),"Ciencia" to listOf("Astronomía","Tecnología","Naturaleza","Psicología","Física"),"Historia" to listOf("Historia antigua","Historia contemporánea","Culturas","Personajes","Exploración"),"Libros e ideas" to listOf("Literatura","Filosofía","Cómics","Ensayo","Mitología"),"Videojuegos" to listOf("Historia del videojuego","Juegos independientes","Diseño de juegos","Mundos y lore"))
 var active by remember{mutableStateOf("Música")}
 val drift by rememberInfiniteTransition(label="intro").animateFloat(-7f,7f,infiniteRepeatable(tween(2300),RepeatMode.Reverse),label="drift")
 Column(Modifier.fillMaxSize().background(XBlack)){
  Row(Modifier.fillMaxWidth().padding(22.dp),verticalAlignment=Alignment.CenterVertically){OrbitMark(Modifier.size(34.dp));Spacer(Modifier.width(10.dp));Column{Text("RABBIT HOLE",color=XWhite,fontWeight=FontWeight.Black,letterSpacing=2.sp);Tag("Tu mapa de curiosidades",Color.LightGray)};Spacer(Modifier.weight(1f));Tag("01 / 02",Color.LightGray)}
  LazyColumn(Modifier.weight(1f),contentPadding=PaddingValues(horizontal=20.dp,vertical=5.dp)){
   item{
    Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(22.dp)).background(XPanel)){OrbitMark(Modifier.align(Alignment.CenterEnd).offset(x=drift.dp).size(210.dp));Column(Modifier.align(Alignment.CenterStart).padding(21.dp).fillMaxWidth(.75f)){Tag("Una experiencia que evoluciona contigo",Color.LightGray);Text("Sigue lo que\nte mueve.",color=XWhite,fontSize=37.sp,lineHeight=38.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.8).sp);Text("Tus intereses son el comienzo, no los límites.",color=Color.LightGray,fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=9.dp))}}
    Text("ARMA TU UNIVERSO",color=XWhite,fontSize=20.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=25.dp))
    Text("Elige varias áreas. RabbitHole dará prioridad a rutas que puedan interesarte, sin cerrar la puerta a nuevos temas.",color=XMuted,fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=6.dp,bottom=16.dp))
    Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){groups.keys.forEach{g->val chosen=g==active;Text(g,Modifier.clip(CircleShape).background(if(chosen)XWhite else XPanel).clickable{active=g}.padding(horizontal=14.dp,vertical=10.dp),color=if(chosen)XBlack else XWhite,fontSize=10.sp,fontWeight=FontWeight.SemiBold)}}
    Tag("TEMAS DE "+active,Color.LightGray)
   }
   items(groups[active].orEmpty().chunked(2)){pair->Row(Modifier.fillMaxWidth().padding(top=9.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){pair.forEach{topic->val chosen=topic in selected;val s by animateFloatAsState(if(chosen)1.02f else 1f,tween(180),label="select");Column(Modifier.weight(1f).scale(s).height(88.dp).clip(RoundedCornerShape(15.dp)).background(if(chosen)XWhite else XPanel).border(1.dp,if(chosen)XWhite else Color(0xFF383834),RoundedCornerShape(15.dp)).clickable{onToggle(topic)}.padding(13.dp),verticalArrangement=Arrangement.SpaceBetween){Tag(if(chosen)"EN TU PERFIL" else "EXPLORAR",if(chosen)XMuted else Color.LightGray);Row(verticalAlignment=Alignment.CenterVertically){Text(topic,Modifier.weight(1f),color=if(chosen)XBlack else XWhite,fontSize=12.sp,fontWeight=FontWeight.SemiBold);Text(if(chosen)"✓" else "+",color=if(chosen)XBlack else XWhite,fontSize=20.sp)}}};if(pair.size==1)Spacer(Modifier.weight(1f))}}
   item{Text(selected.size.toString()+" intereses seleccionados",color=XMuted,fontSize=10.sp,modifier=Modifier.padding(top=13.dp))}
  }
  Column(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=13.dp)){Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(XWhite).clickable{onContinue()}.padding(17.dp),verticalAlignment=Alignment.CenterVertically){Text("ENTRAR A RABBITHOLE",Modifier.weight(1f),color=XBlack,fontSize=10.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp);Text("↗",color=XBlack,fontSize=19.sp)};Text("No es una lista. Es el principio de un recorrido.",Modifier.align(Alignment.CenterHorizontally).padding(top=8.dp),color=XMuted,fontSize=9.sp)}
 }
}
@Composable
fun ExperienceHome(query:String,onQuery:(String)->Unit,category:String,onCategory:(String)->Unit,results:List<Discovery>,searchStatus:String,saved:Set<String>,onSave:(String)->Unit,onOpen:(Discovery)->Unit,onProfile:()->Unit,onMap:()->Unit){
 val uri=LocalUriHandler.current
 val lead=results.firstOrNull() ?: catalog.first()
 val dossierItems=(results.filter{it.description.length>180}.ifEmpty{results}).take(14)
 val allRows=results.drop(1).chunked(2).take(24)
 val heroMotion=rememberInfiniteTransition(label="coverMotion")
 val heroZoom by heroMotion.animateFloat(1f,1.055f,infiniteRepeatable(tween(14000,easing=LinearEasing),RepeatMode.Reverse),label="heroZoom")
 val categories=listOf("Todo","Música","Cine","Historia","Ciencia","Diseño","Tecnología","Arte")
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(bottom=38.dp)){
  item{
   Column(Modifier.fillMaxWidth().background(XBlack)){
    Row(Modifier.fillMaxWidth().padding(horizontal=21.dp,vertical=17.dp),verticalAlignment=Alignment.CenterVertically){
     OrbitMark(Modifier.size(36.dp))
     Column(Modifier.padding(start=11.dp)){Text("RABBIT HOLE",color=XWhite,fontSize=13.sp,fontWeight=FontWeight.Black,letterSpacing=2.5.sp);Tag("THE INFINITE CURIOSITY JOURNAL",Color.LightGray)}
     Spacer(Modifier.weight(1f))
     Text("PERFIL  ↗",Modifier.clip(CircleShape).border(1.dp,Color.White.copy(alpha=.35f),CircleShape).clickable{onProfile()}.padding(horizontal=13.dp,vertical=10.dp),color=XWhite,fontSize=8.sp,fontWeight=FontWeight.Black)
    }
    Box(Modifier.fillMaxWidth().height(480.dp).background(XBlack)){
     Photo(lead,Modifier.fillMaxSize().scale(heroZoom))
     Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x33000000),Color(0x12000000),Color(0xF5000000)))))
     Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(start=23.dp,end=23.dp,bottom=25.dp)){
      Row(verticalAlignment=Alignment.CenterVertically){Tag("DOSSIER DESTACADO",Color.White);Spacer(Modifier.width(10.dp));Box(Modifier.width(35.dp).height(1.dp).background(Color.White.copy(alpha=.7f)));Tag("RH / 001",Color.White)}
      Text(lead.title.uppercase(),color=XWhite,fontSize=if(lead.title.length>17)37.sp else 45.sp,lineHeight=if(lead.title.length>17)39.sp else 46.sp,fontWeight=FontWeight.Black,letterSpacing=(-2.1).sp,modifier=Modifier.padding(top=10.dp))
      Text(lead.description,color=Color(0xFFE4E1DB),fontSize=12.sp,lineHeight=18.sp,maxLines=3,modifier=Modifier.fillMaxWidth(.94f).padding(top=8.dp))
      Row(Modifier.padding(top=17.dp),verticalAlignment=Alignment.CenterVertically){
       Text("ABRIR DOSSIER  ↗",Modifier.clip(CircleShape).background(XWhite).clickable{onOpen(lead)}.padding(horizontal=17.dp,vertical=12.dp),color=XBlack,fontSize=8.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp)
       Spacer(Modifier.width(12.dp))
       Text(lead.type.uppercase()+"  /  "+lead.category.uppercase(),color=Color.White,fontSize=8.sp,letterSpacing=1.sp,fontWeight=FontWeight.Bold)
      }
     }
     Text("01 — PORTADA",Modifier.align(Alignment.TopStart).padding(19.dp),color=Color.White,fontSize=8.sp,letterSpacing=1.7.sp,fontWeight=FontWeight.Black)
     Text("DESLIZA PARA ENTRAR",Modifier.align(Alignment.TopEnd).padding(19.dp),color=Color.White,fontSize=7.sp,letterSpacing=1.2.sp)
    }
   }
  }
  item{
   Column(Modifier.padding(horizontal=20.dp).padding(top=24.dp)){
    Row(verticalAlignment=Alignment.Bottom){
     Column(Modifier.weight(1f)){Tag("EL PRIMER HILO");Text("¿Qué te da curiosidad?",color=XBlack,fontSize=25.sp,fontWeight=FontWeight.Black,letterSpacing=(-.8).sp)}
     Tag(results.size.toString()+" EN LA BIBLIOTECA")
    }
    Row(Modifier.fillMaxWidth().padding(top=13.dp).clip(RoundedCornerShape(4.dp)).background(XWhite).border(1.dp,XLine,RoundedCornerShape(4.dp)).padding(horizontal=14.dp,vertical=15.dp),verticalAlignment=Alignment.CenterVertically){
     Text("⌕",fontSize=25.sp,color=XBlack);Spacer(Modifier.width(10.dp))
     BasicTextField(query,onQuery,Modifier.weight(1f),singleLine=true,textStyle=TextStyle(color=XBlack,fontSize=13.sp),keyboardOptions=KeyboardOptions(imeAction=ImeAction.Search),decorationBox={inner->if(query.isEmpty())Text("Busca una persona, obra, lugar o idea…",fontSize=11.sp,color=XMuted);inner()})
     if(query.isNotEmpty())Text("×",Modifier.clickable{onQuery("")},fontSize=21.sp,color=XMuted)
    }
    Row(Modifier.horizontalScroll(rememberScrollState()).padding(top=12.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){
     categories.forEach{v->val active=category==v;Text(v.uppercase(),Modifier.clip(CircleShape).background(if(active)XBlack else XWhite).border(1.dp,if(active)XBlack else XLine,CircleShape).clickable{onCategory(v)}.padding(horizontal=13.dp,vertical=9.dp),color=if(active)XWhite else XBlack,fontSize=8.sp,fontWeight=FontWeight.Black,letterSpacing=.5.sp)}
    }
    Text(searchStatus.uppercase(),color=XMuted,fontSize=8.sp,letterSpacing=1.sp,modifier=Modifier.padding(top=12.dp))
    if(query.trim().length>=2){
     Text("BUSCA TAMBIÉN EN",color=XMuted,fontSize=8.sp,letterSpacing=1.4.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=14.dp,bottom=8.dp))
     Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(7.dp)){
      listOf("DuckDuckGo" to "https://duckduckgo.com/?q=","Google" to "https://www.google.com/search?q=","Bing" to "https://www.bing.com/search?q=","Brave" to "https://search.brave.com/search?q=").forEach{(name,base)->Text(name,Modifier.clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(base+android.net.Uri.encode(query))}}.padding(horizontal=12.dp,vertical=9.dp),color=XBlack,fontSize=9.sp)}
     }
    }
   }
  }
  item{
   Column(Modifier.padding(top=30.dp)){
    Row(Modifier.padding(horizontal=20.dp),verticalAlignment=Alignment.Bottom){Column(Modifier.weight(1f)){Tag("ARCHIVO EDITORIAL / 01");Text("Dossiers abiertos.",color=XBlack,fontSize=28.sp,fontWeight=FontWeight.Black,letterSpacing=(-1).sp)};Tag(dossierItems.size.toString()+" HISTORIAS")}
    Text("No son fichas rápidas. Son puertas de entrada a historias, contextos, obras y conexiones.",Modifier.padding(horizontal=20.dp).padding(top=6.dp),color=XMuted,fontSize=11.sp,lineHeight=17.sp)
    androidx.compose.foundation.lazy.LazyRow(contentPadding=PaddingValues(horizontal=20.dp),horizontalArrangement=Arrangement.spacedBy(13.dp),modifier=Modifier.padding(top=14.dp)){
     items(dossierItems){item->Box(Modifier.width(258.dp).height(380.dp).clip(RoundedCornerShape(2.dp)).background(XBlack).clickable{onOpen(item)}){
      Photo(item,Modifier.fillMaxSize())
      Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x16000000),Color(0x12000000),Color(0xF0000000)))))
      Column(Modifier.align(Alignment.TopStart).padding(15.dp)){Tag("RABBITHOLE FILE",Color.White);Tag(item.type,Color.White)}
      Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(18.dp)){
       Text(item.category.uppercase()+"  /  "+item.source.uppercase(),color=Color(0xFFD6D3CD),fontSize=7.sp,letterSpacing=1.1.sp,fontWeight=FontWeight.Bold)
       Text(item.title.uppercase(),color=XWhite,fontSize=29.sp,lineHeight=31.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.2).sp,modifier=Modifier.padding(top=7.dp))
       Text(item.description,color=Color(0xFFE4E1DB),fontSize=10.sp,lineHeight=15.sp,maxLines=4,modifier=Modifier.padding(top=8.dp))
       Row(Modifier.padding(top=13.dp),verticalAlignment=Alignment.CenterVertically){Text("LEER DOSSIER  ↗",color=XWhite,fontSize=8.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp);Spacer(Modifier.weight(1f));Text("＋",color=XWhite,fontSize=18.sp)}
      }
     }}
    }
   }
  }
  item{
   Column(Modifier.padding(horizontal=20.dp).padding(top=34.dp)){
    Tag("CARTOGRAFÍA DE CURIOSIDADES")
    Text("Un tema nunca viene solo.",color=XBlack,fontSize=27.sp,lineHeight=30.sp,fontWeight=FontWeight.Black,letterSpacing=(-1).sp,modifier=Modifier.padding(top=5.dp))
    Text("Cada nombre es una entrada. Cada conexión abre otras diez posibilidades.",color=XMuted,fontSize=11.sp,lineHeight=17.sp,modifier=Modifier.padding(top=6.dp))
    Row(Modifier.fillMaxWidth().padding(top=15.dp).clip(RoundedCornerShape(3.dp)).background(XBlack).clickable{onMap()}.padding(18.dp),verticalAlignment=Alignment.CenterVertically){
     Column(Modifier.weight(1f)){Tag("MAPA INTERACTIVO",Color.LightGray);Text("Entra al universo.",color=XWhite,fontSize=21.sp,fontWeight=FontWeight.Black);Text("Explora las conexiones entre temas.",color=Color.LightGray,fontSize=10.sp,modifier=Modifier.padding(top=4.dp))}
     OrbitMark(Modifier.size(66.dp))
    }
   }
  }
  if(allRows.isNotEmpty()){
   item{Column(Modifier.padding(horizontal=20.dp).padding(top=32.dp)){Tag("EL ARCHIVO SIGUE CRECIENDO");Text("Sigue cayendo.",color=XBlack,fontSize=28.sp,fontWeight=FontWeight.Black);Text("Más historias para perderse un buen rato.",color=XMuted,fontSize=11.sp,modifier=Modifier.padding(top=4.dp))}}
   items(allRows){pair->Row(Modifier.fillMaxWidth().padding(horizontal=16.dp).padding(top=12.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)){pair.forEach{item->PosterCard(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.weight(1f),true)};if(pair.size==1)Spacer(Modifier.weight(1f))}}
  }
  item{
   Column(Modifier.fillMaxWidth().background(XBlack).padding(horizontal=22.dp,vertical=28.dp)){
    Tag("NO HAY UN ÚLTIMO DESCUBRIMIENTO",Color.LightGray)
    Text("Sigue la madriguera.",color=XWhite,fontSize=31.sp,lineHeight=34.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=7.dp))
    Text("Cambia de tema, sigue una conexión, abre un dossier y deja que una pregunta te lleve a otra.",color=Color(0xFFC5C2BC),fontSize=12.sp,lineHeight=19.sp,modifier=Modifier.padding(top=8.dp))
    Text("VOLVER AL UNIVERSO  ↗",Modifier.padding(top=17.dp).clip(CircleShape).background(XWhite).clickable{onMap()}.padding(horizontal=16.dp,vertical=12.dp),color=XBlack,fontSize=8.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp)
   }
  }
 }
}
@Composable private fun PosterCard(item:Discovery,saved:Boolean,onOpen:()->Unit,onSave:()->Unit,modifier:Modifier,tall:Boolean){
 Box(modifier.fillMaxWidth().height(if(tall)340.dp else 285.dp).clip(RoundedCornerShape(2.dp)).background(XBlack).clickable{onOpen()}){
  Photo(item,Modifier.fillMaxSize())
  Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x22000000),Color(0x08000000),Color(0xE8000000)))))
  Text(item.type.uppercase(),Modifier.align(Alignment.TopStart).padding(14.dp),color=XWhite,fontSize=8.sp,letterSpacing=1.7.sp,fontWeight=FontWeight.Black)
  Text(if(saved)"♥" else "＋",Modifier.align(Alignment.TopEnd).padding(12.dp).clickable{onSave()},color=XWhite,fontSize=19.sp)
  Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(horizontal=17.dp,vertical=19.dp)){
   Box(Modifier.width(35.dp).height(2.dp).background(XWhite))
   Text(item.title.uppercase(),color=XWhite,fontSize=if(item.title.length>17)25.sp else 31.sp,lineHeight=if(item.title.length>17)27.sp else 32.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.2).sp,modifier=Modifier.padding(top=11.dp))
   Text("RABBITHOLE  /  DESCUBRIR  ↗",color=Color.White.copy(alpha=.82f),fontSize=7.sp,letterSpacing=1.1.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=12.dp))
  }
 }
}
@Composable
fun ExperienceMapScreen(onOpen:(Discovery)->Unit){
 var focus by remember{mutableStateOf(catalog.first())}
 val related=focus.connections.map{title->catalog.find{it.title==title}?:Discovery(title,"CONEXIÓN","Exploración","Esta conexión aparece relacionada con "+focus.title+". Ábrela para iniciar una nueva ruta y descubrir más contexto.",listOf(focus.title),"RabbitHole")}
 val nodes=related.take(8)
 val infinite=rememberInfiniteTransition(label="living-universe")
 val pulse by infinite.animateFloat(.92f,1.12f,infiniteRepeatable(tween(1700),RepeatMode.Reverse),label="pulse")
 val spin by infinite.animateFloat(0f,360f,infiniteRepeatable(tween(26000,easing=LinearEasing)),label="spin")
 Column(Modifier.fillMaxSize().background(XBlack).padding(horizontal=18.dp)){
  Row(Modifier.fillMaxWidth().padding(top=20.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Tag("RABBITHOLE / MAPA VIVO",Color.LightGray);Text("Sigue las\nconexiones.",color=XWhite,fontSize=36.sp,lineHeight=37.sp,fontWeight=FontWeight.Black)};OrbitMark(Modifier.size(72.dp))}
  Text("Elige un punto para cambiar el centro del mapa. Cada tema abre una nueva red de conexiones.",color=Color(0xFFB7B4AD),fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=8.dp,bottom=13.dp))
  BoxWithConstraints(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(8.dp)).background(Brush.radialGradient(listOf(Color(0xFF30302C),Color(0xFF111110),XBlack)))){
   val w=constraints.maxWidth.toFloat();val h=constraints.maxHeight.toFloat();val cx=w*.5f;val cy=h*.49f
   val coords=nodes.mapIndexed{i,_->val angle=Math.toRadians((i*360.0/maxOf(1,nodes.size)-90.0)+spin*.08);val radius=minOf(w*.34f,h*.34f);Offset(cx+kotlin.math.cos(angle).toFloat()*radius,cy+kotlin.math.sin(angle).toFloat()*radius)}
   Canvas(Modifier.fillMaxSize()){for(i in 0..70){val x=(i*73%997)/997f*size.width;val y=(i*137%991)/991f*size.height;drawCircle(Color.White.copy(alpha=if(i%5==0).30f else .10f),if(i%7==0)1.7.dp.toPx() else .8.dp.toPx(),Offset(x,y))};val center=Offset(cx,cy);coords.forEach{p->drawLine(Color.White.copy(alpha=.33f),center,p,1.dp.toPx());drawCircle(Color.White.copy(alpha=.18f),3.dp.toPx(),Offset((center.x+p.x)/2,(center.y+p.y)/2))};drawCircle(Color.White.copy(alpha=.09f),58.dp.toPx()*pulse,center,style=Stroke(1.dp.toPx()));drawCircle(Color.White.copy(alpha=.22f),43.dp.toPx(),center);drawCircle(Color.White.copy(alpha=.75f),3.dp.toPx(),center)}
   nodes.forEachIndexed{i,node->val p=coords[i];val bob by rememberInfiniteTransition(label="node-"+node.title).animateFloat(-3f,3f,infiniteRepeatable(tween(1800+i*130),RepeatMode.Reverse),label="bob");Column(Modifier.offset(x=(p.x/w*maxWidth.value).dp-43.dp,y=(p.y/h*maxHeight.value).dp-36.dp+bob.dp).width(86.dp).clickable{focus=node},horizontalAlignment=Alignment.CenterHorizontally){Box(Modifier.size(47.dp).clip(CircleShape).background(if(focus.title==node.title)XWhite else Color(0xFF292925)).border(1.dp,Color.White.copy(alpha=.7f),CircleShape),contentAlignment=Alignment.Center){Text(if(focus.title==node.title)"●" else "✦",color=if(focus.title==node.title)XBlack else XWhite,fontSize=15.sp)};Text(node.title,color=XWhite,fontSize=9.sp,lineHeight=11.sp,fontWeight=if(focus.title==node.title)FontWeight.Black else FontWeight.Medium,maxLines=2)}}
   Column(Modifier.align(Alignment.Center).width(126.dp).clickable{onOpen(focus)},horizontalAlignment=Alignment.CenterHorizontally){Box(Modifier.size(78.dp).clip(CircleShape).background(XWhite).border(5.dp,Color(0xFF777770),CircleShape),contentAlignment=Alignment.Center){OrbitMark(Modifier.fillMaxSize(.78f),XBlack)};Text(focus.title,color=XWhite,fontSize=12.sp,fontWeight=FontWeight.Black,maxLines=2,modifier=Modifier.padding(top=8.dp));Tag("CENTRO ACTUAL",Color.LightGray)}
   Text("NODOS RELACIONADOS: "+nodes.size,Modifier.align(Alignment.TopStart).padding(13.dp),color=Color.LightGray,fontSize=8.sp)
   Text("TOCA UN NODO PARA REENFOCAR",Modifier.align(Alignment.BottomStart).padding(13.dp),color=Color.LightGray,fontSize=8.sp)
  }
  Row(Modifier.fillMaxWidth().padding(vertical=13.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Tag("EXPLORANDO AHORA",Color.LightGray);Text(focus.title,color=XWhite,fontSize=17.sp,fontWeight=FontWeight.Bold);Text(nodes.size.toString()+" conexiones disponibles",color=XMuted,fontSize=10.sp,modifier=Modifier.padding(top=3.dp))};Text("ABRIR HISTORIA  ↗",Modifier.clip(CircleShape).background(XWhite).clickable{onOpen(focus)}.padding(horizontal=15.dp,vertical=12.dp),color=XBlack,fontSize=8.sp,fontWeight=FontWeight.Black)}
 }
}
@Composable
fun ExperienceLibraryScreen(title:String,subtitle:String,items:List<Discovery>,saved:Set<String>,onOpen:(Discovery)->Unit,onSave:(String)->Unit){
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(start=16.dp,end=16.dp,top=23.dp,bottom=28.dp)){item{Tag("RABBITHOLE / COLECCIÓN");Text(title,color=XBlack,fontSize=35.sp,lineHeight=38.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.5).sp,modifier=Modifier.padding(top=7.dp));Text(subtitle,color=XMuted,fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=7.dp,bottom=19.dp))};if(items.isEmpty())item{Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(20.dp)).background(XBlack),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){OrbitMark(Modifier.size(65.dp));Text("Tu próxima historia empieza aquí.",color=XWhite,fontSize=15.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=13.dp))}}}else items(items.chunked(2)){pair->Row(Modifier.fillMaxWidth().padding(bottom=10.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){pair.forEach{item->PosterCard(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.weight(1f),true)};if(pair.size==1)Spacer(Modifier.weight(1f))}}}
}
@Composable
fun ExperienceDetail(item:Discovery,saved:Boolean,onBack:()->Unit,onSave:()->Unit,onConnection:(String)->Unit){
 val uri=LocalUriHandler.current
 val entrance by rememberInfiniteTransition(label="posterMotion").animateFloat(1f,1.075f,infiniteRepeatable(tween(11000,easing=LinearEasing),RepeatMode.Reverse),label="posterBreath")
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(bottom=50.dp)){
  item{
   Box(Modifier.fillMaxWidth().height(590.dp).background(XBlack)){
    Photo(item,Modifier.fillMaxSize().scale(entrance))
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x99000000),Color(0x12000000),Color(0x22000000),Color(0xF5000000)))))
    Box(Modifier.align(Alignment.CenterStart).padding(start=9.dp).width(2.dp).fillMaxHeight(.54f).background(Color.White.copy(alpha=.65f)))
    Text("R H  /  0 0 1",Modifier.align(Alignment.CenterEnd).padding(end=12.dp).then(Modifier),color=Color.White.copy(alpha=.8f),fontSize=8.sp,letterSpacing=2.sp)
    Row(Modifier.align(Alignment.TopStart).fillMaxWidth().padding(horizontal=18.dp,vertical=20.dp),verticalAlignment=Alignment.CenterVertically){
     Text("←  SALIR",Modifier.clip(CircleShape).background(Color(0x77000000)).clickable{onBack()}.padding(horizontal=14.dp,vertical=11.dp),color=XWhite,fontSize=9.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp)
     Spacer(Modifier.weight(1f))
     Text("RABBITHOLE  /  ARCHIVO",color=Color.White,fontSize=8.sp,letterSpacing=1.8.sp,fontWeight=FontWeight.Bold)
     Spacer(Modifier.weight(1f))
     Text(if(saved)"♥" else "＋",Modifier.clip(CircleShape).background(Color(0x77000000)).clickable{onSave()}.padding(horizontal=13.dp,vertical=8.dp),color=XWhite,fontSize=18.sp)
    }
    Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(start=25.dp,end=25.dp,bottom=30.dp)){
     Text("DOSSIER VISUAL     /     "+item.type.uppercase(),color=Color.White,fontSize=9.sp,letterSpacing=2.sp,fontWeight=FontWeight.Black)
     Text(item.title.uppercase(),color=XWhite,fontSize=if(item.title.length>18)43.sp else 55.sp,lineHeight=if(item.title.length>18)44.sp else 55.sp,fontWeight=FontWeight.Black,letterSpacing=(-2.8).sp,modifier=Modifier.padding(top=10.dp))
     Row(Modifier.padding(top=13.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.width(42.dp).height(3.dp).background(XWhite));Text("  UNA HISTORIA PARA EXPLORAR",color=Color.White,fontSize=8.sp,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold)}
     Text(item.description.substringBefore("\n\n").take(430),color=Color(0xFFF0EEE8),fontSize=14.sp,lineHeight=22.sp,modifier=Modifier.padding(top=13.dp).fillMaxWidth(.96f))
    }
   }
   Row(Modifier.fillMaxWidth().background(XBlack).padding(horizontal=23.dp,vertical=16.dp),verticalAlignment=Alignment.CenterVertically){
    Column(Modifier.weight(1f)){Text("COLECCIÓN",color=Color(0xFFAAA69E),fontSize=8.sp,letterSpacing=1.5.sp);Text(item.category.uppercase(),color=XWhite,fontSize=11.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=4.dp))}
    Box(Modifier.width(1.dp).height(30.dp).background(Color(0xFF55534E)))
    Column(Modifier.weight(1f).padding(start=15.dp)){Text("FORMATO",color=Color(0xFFAAA69E),fontSize=8.sp,letterSpacing=1.5.sp);Text("ENSAYO VISUAL",color=XWhite,fontSize=11.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=4.dp))}
    Box(Modifier.width(1.dp).height(30.dp).background(Color(0xFF55534E)))
    Column(Modifier.weight(1f).padding(start=15.dp)){Text("ORIGEN",color=Color(0xFFAAA69E),fontSize=8.sp,letterSpacing=1.5.sp);Text(item.source.uppercase().take(15),color=XWhite,fontSize=10.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=4.dp))}
   }
   Column(Modifier.padding(horizontal=23.dp)){
    Row(Modifier.fillMaxWidth().padding(top=30.dp,bottom=12.dp),verticalAlignment=Alignment.CenterVertically){Text("01",color=XBlack,fontSize=46.sp,lineHeight=46.sp,fontWeight=FontWeight.Black,letterSpacing=(-2).sp);Column(Modifier.padding(start=12.dp)){Text("EL DOSSIER",color=XBlack,fontSize=10.sp,letterSpacing=2.sp,fontWeight=FontWeight.Black);Text("MÁS ALLÁ DE LA PORTADA",color=XMuted,fontSize=8.sp,letterSpacing=1.2.sp,modifier=Modifier.padding(top=4.dp))}}
    Box(Modifier.fillMaxWidth().height(1.dp).background(XLine))
    Text("Detente un momento. Mira las conexiones.",color=XBlack,fontSize=29.sp,lineHeight=33.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.2).sp,modifier=Modifier.padding(top=20.dp,bottom=19.dp))
    EditorialSections(item)
   }
   if(item.connections.isNotEmpty()){
    val contextTitle=item.connections.first()
    val contextItem=Discovery(contextTitle,"CONEXIÓN",item.category,"Una ruta visual conectada con "+item.title+".",listOf(item.title),"RabbitHole")
    Column(Modifier.padding(top=30.dp)){
     Box(Modifier.fillMaxWidth().height(310.dp).background(XBlack)){
      Photo(contextItem,Modifier.fillMaxSize())
      Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color(0xCC000000)))))
      Text("OTRA PERSPECTIVA  /  "+contextTitle.uppercase(),Modifier.align(Alignment.BottomStart).padding(20.dp),color=Color.White,fontSize=9.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Black)
     }
    }
   }
   Column(Modifier.padding(horizontal=23.dp)){
    Text("FUENTES Y CONTEXTO",color=XMuted,fontSize=9.sp,letterSpacing=1.7.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=28.dp))
    Text("Una historia nunca termina en una sola página.",color=XBlack,fontSize=26.sp,lineHeight=29.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=8.dp))
    Text("Usa este dossier como punto de partida: contrasta los datos, visita las fuentes originales y sigue las conexiones para construir tu propio recorrido.",color=Color(0xFF55524C),fontSize=14.sp,lineHeight=23.sp,modifier=Modifier.padding(top=10.dp))
    if(item.url.isNotBlank())Text("ABRIR FUENTE ORIGINAL  ↗",Modifier.padding(top=16.dp).clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(item.url)}}.padding(horizontal=15.dp,vertical=12.dp),color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Black)
    if(item.audioUrl.isNotBlank())Text("ESCUCHAR FRAGMENTO  ↗",Modifier.padding(top=9.dp).clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(item.audioUrl)}}.padding(horizontal=15.dp,vertical=12.dp),color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Black)
    Text("SIGUE EL HILO",color=XMuted,fontSize=9.sp,letterSpacing=1.8.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=34.dp,bottom=7.dp))
    Text("La siguiente puerta está aquí.",color=XBlack,fontSize=29.sp,lineHeight=32.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.1).sp)
    Text("Cada conexión abre un nuevo capítulo.",color=XMuted,fontSize=12.sp,modifier=Modifier.padding(top=6.dp,bottom=16.dp))
   }
  }
  itemsIndexed(item.connections){index,connection->
   val linked=Discovery(connection,"CONEXIÓN",item.category,"Una ruta conectada con "+item.title+".",listOf(item.title),"RabbitHole")
   Row(Modifier.fillMaxWidth().padding(horizontal=22.dp,vertical=8.dp).clip(RoundedCornerShape(3.dp)).background(Color.White).clickable{onConnection(connection)}.padding(10.dp),verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.width(30.dp),contentAlignment=Alignment.Center){Text((index+1).toString().padStart(2,'0'),color=XMuted,fontSize=11.sp,fontWeight=FontWeight.Black)}
    Photo(linked,Modifier.size(88.dp,104.dp))
    Column(Modifier.weight(1f).padding(start=13.dp)){Text("CAPÍTULO  0${index+1}",color=XMuted,fontSize=8.sp,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold);Text(connection,color=XBlack,fontSize=19.sp,lineHeight=22.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=6.dp));Text("ABRIR DESCUBRIMIENTO  ↗",color=XBlack,fontSize=8.sp,letterSpacing=.8.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=10.dp))}
   }
  }
 }
}
@Composable
fun ExperienceNavigation(active:String,onSelect:(String)->Unit){
 Row(Modifier.fillMaxWidth().background(XBlack).padding(horizontal=7.dp,vertical=9.dp),horizontalArrangement=Arrangement.SpaceEvenly){listOf("Inicio" to "⌂","Universo" to "◎","Guardados" to "◇","Recorrido" to "↺").forEach{(name,glyph)->val selected=active==name;val scale by animateFloatAsState(if(selected)1.08f else 1f,tween(220),label="nav");Column(Modifier.weight(1f).scale(scale).clip(RoundedCornerShape(13.dp)).clickable{onSelect(name)}.padding(vertical=5.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(glyph,color=if(selected)XWhite else Color(0xFF77746F),fontSize=19.sp);Text(name.uppercase(),color=if(selected)XWhite else Color(0xFF77746F),fontSize=7.sp,letterSpacing=.7.sp,fontWeight=if(selected)FontWeight.Black else FontWeight.Medium);if(selected)Box(Modifier.padding(top=4.dp).width(13.dp).height(2.dp).clip(CircleShape).background(XWhite))}}}
}
