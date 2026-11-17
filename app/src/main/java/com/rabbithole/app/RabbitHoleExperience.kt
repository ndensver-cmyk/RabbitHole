package com.rabbithole.app
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
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
private val posters=mapOf("Nirvana" to "https://images.unsplash.com/photo-1524367400751-7a3b7c7b4b3a?w=1000","Nevermind" to "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=1000","Daft Punk" to "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=1000","Apollo 11" to "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1000","Voyager" to "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=1000","Pink Floyd" to "https://images.unsplash.com/photo-1519608487953-e999c86e7455?w=1000","Bauhaus" to "https://images.unsplash.com/photo-1494438639946-1ebd1d20bf85?w=1000","Seattle" to "https://images.unsplash.com/photo-1502175353174-a7a70e73b362?w=1000")
private fun posterFor(item:Discovery)=item.imageUrl.ifBlank{posters[item.title].orEmpty()}
@Composable private fun Tag(text:String,color:Color=XMuted){Text(text.uppercase(),color=color,fontSize=9.sp,letterSpacing=1.6.sp,fontWeight=FontWeight.Bold)}
@Composable private fun OrbitMark(modifier:Modifier=Modifier,color:Color=XWhite){
 val t=rememberInfiniteTransition(label="orbit")
 val angle by t.animateFloat(0f,360f,infiniteRepeatable(tween(10000)),label="angle")
 val pulse by t.animateFloat(.88f,1.13f,infiniteRepeatable(tween(1500),RepeatMode.Reverse),label="pulse")
 Canvas(modifier){val d=size.minDimension;val c=Offset(size.width/2,size.height/2);drawCircle(color.copy(alpha=.25f),d*.47f,c,style=Stroke(1.dp.toPx()));drawCircle(color.copy(alpha=.48f),d*.31f,c,style=Stroke(1.dp.toPx()));drawCircle(color.copy(alpha=.8f),d*.11f*pulse,c);val a=Math.toRadians(angle.toDouble());val p=Offset(c.x+kotlin.math.cos(a).toFloat()*d*.42f,c.y+kotlin.math.sin(a).toFloat()*d*.42f);drawLine(color.copy(alpha=.55f),c,p,1.dp.toPx());drawCircle(color,d*.035f,p)}
}
@Composable private fun Photo(item:Discovery,modifier:Modifier=Modifier){
 val image=posterFor(item)
 Box(modifier.clip(RoundedCornerShape(17.dp)).background(Brush.linearGradient(listOf(Color(0xFF3A3935),XBlack)))){
  if(image.isNotBlank())AsyncImage(model=image,contentDescription=item.title,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Crop,colorFilter=ColorFilter.colorMatrix(ColorMatrix().apply{setToSaturation(.12f)}))else OrbitMark(Modifier.align(Alignment.Center).fillMaxSize(.65f))
  Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color(0xDD000000)))))
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
fun ExperienceHome(query:String,onQuery:(String)->Unit,category:String,onCategory:(String)->Unit,results:List<Discovery>,searchStatus:String,saved:Set<String>,onSave:(String)->Unit,onOpen:(Discovery)->Unit,onMap:()->Unit){
 val uri=LocalUriHandler.current
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(bottom=28.dp)){
  item{Column(Modifier.padding(horizontal=18.dp)){
   Row(Modifier.fillMaxWidth().padding(top=18.dp,bottom=19.dp),verticalAlignment=Alignment.CenterVertically){OrbitMark(Modifier.size(35.dp),XBlack);Spacer(Modifier.width(9.dp));Column{Text("RABBIT HOLE",color=XBlack,fontSize=13.sp,fontWeight=FontWeight.Black,letterSpacing=2.sp);Tag("A world of curiosity")};Spacer(Modifier.weight(1f));Tag("EXPLORE / 001")}
   Box(Modifier.fillMaxWidth().height(254.dp).clip(RoundedCornerShape(23.dp)).background(XBlack)){OrbitMark(Modifier.align(Alignment.CenterEnd).offset(x=24.dp).size(225.dp));Column(Modifier.align(Alignment.CenterStart).padding(21.dp).fillMaxWidth(.78f)){Tag("TU PRÓXIMA OBSESIÓN EMPIEZA AQUÍ",Color.LightGray);Text("Hay mucho\nmás allá.",color=XWhite,fontSize=43.sp,lineHeight=43.sp,fontWeight=FontWeight.Black,letterSpacing=(-2.2).sp,modifier=Modifier.padding(top=10.dp));Text("Una idea te lleva a otra. Sigue el hilo y descubre adónde termina.",color=Color(0xFFD1D0CC),fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=10.dp));Row(Modifier.padding(top=16.dp).clip(CircleShape).background(XWhite).clickable{onMap()}.padding(horizontal=14.dp,vertical=10.dp)){Text("EXPLORAR UNIVERSO  ↗",color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Black)}}}
   Row(Modifier.fillMaxWidth().padding(top=17.dp).clip(RoundedCornerShape(16.dp)).background(XWhite).border(1.dp,XLine,RoundedCornerShape(16.dp)).padding(horizontal=14.dp,vertical=14.dp),verticalAlignment=Alignment.CenterVertically){Text("⌕",fontSize=24.sp,color=XBlack);Spacer(Modifier.width(9.dp));BasicTextField(query,onQuery,Modifier.weight(1f),singleLine=true,textStyle=TextStyle(color=XBlack,fontSize=13.sp),keyboardOptions=KeyboardOptions(imeAction=ImeAction.Search),decorationBox={inner->if(query.isEmpty())Text("Busca una idea, persona, lugar o historia…",fontSize=11.sp,color=XMuted);inner()});if(query.isNotEmpty())Text("×",Modifier.clickable{onQuery("")},fontSize=21.sp,color=XMuted)}
   Row(Modifier.horizontalScroll(rememberScrollState()).padding(top=11.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){listOf("Todo","Música","Cine","Historia","Ciencia").forEach{c->val chosen=category==c;Text(c,Modifier.clip(CircleShape).background(if(chosen)XBlack else XWhite).border(1.dp,if(chosen)XBlack else XLine,CircleShape).clickable{onCategory(c)}.padding(horizontal=14.dp,vertical=9.dp),color=if(chosen)XWhite else XBlack,fontSize=10.sp,fontWeight=FontWeight.SemiBold)}}
   Row(Modifier.fillMaxWidth().padding(top=25.dp,bottom=10.dp),verticalAlignment=Alignment.Bottom){Column(Modifier.weight(1f)){Tag(if(query.isBlank())"Hecho para tu curiosidad" else "Resultados de exploración");Text(if(query.isBlank())"Por dónde empezar" else "Sigue investigando",color=XBlack,fontSize=24.sp,fontWeight=FontWeight.Black,letterSpacing=(-.8).sp)};Text(results.size.toString()+" HALLAZGOS",color=XMuted,fontSize=8.sp,letterSpacing=1.sp)}
   Text(searchStatus,color=XMuted,fontSize=10.sp,modifier=Modifier.padding(bottom=9.dp))
   if(query.trim().length>=2)Row(Modifier.horizontalScroll(rememberScrollState()).padding(bottom=12.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){listOf("DuckDuckGo" to ("https://duckduckgo.com/?q="+android.net.Uri.encode(query)),"Google" to ("https://www.google.com/search?q="+android.net.Uri.encode(query)),"Bing" to ("https://www.bing.com/search?q="+android.net.Uri.encode(query)),"Brave" to ("https://search.brave.com/search?q="+android.net.Uri.encode(query))).forEach{(name,url)->Text(name,Modifier.clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(url)}}.padding(horizontal=11.dp,vertical=8.dp),color=XBlack,fontSize=9.sp)}}
  }}
  if(results.isEmpty())item{Box(Modifier.fillMaxWidth().padding(18.dp).height(210.dp).clip(RoundedCornerShape(20.dp)).background(XBlack)){OrbitMark(Modifier.align(Alignment.CenterEnd).size(160.dp));Column(Modifier.align(Alignment.CenterStart).padding(20.dp).fillMaxWidth(.7f)){Tag("AÚN NO HAY RESULTADOS",Color.LightGray);Text("Toda gran ruta\ncomienza con\nuna pregunta.",color=XWhite,fontSize=25.sp,lineHeight=27.sp,fontWeight=FontWeight.Black)}}}
  else itemsIndexed(results.chunked(2)){index,pair->Row(Modifier.fillMaxWidth().padding(horizontal=15.dp,vertical=5.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){pair.forEachIndexed{j,item->PosterCard(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.weight(1f),(index+j)%3==0)};if(pair.size==1)Spacer(Modifier.weight(1f))}}
 }
}
@Composable private fun PosterCard(item:Discovery,saved:Boolean,onOpen:()->Unit,onSave:()->Unit,modifier:Modifier,tall:Boolean){
 Column(modifier.clip(RoundedCornerShape(17.dp)).background(XWhite).clickable{onOpen()}){Box(Modifier.fillMaxWidth().height(if(tall)215.dp else 164.dp)){Photo(item,Modifier.fillMaxSize());Text(item.type,Modifier.align(Alignment.TopStart).padding(9.dp).clip(CircleShape).background(Color(0xAA080808)).padding(horizontal=9.dp,vertical=6.dp),color=XWhite,fontSize=7.sp);Text(if(saved)"♥" else "＋",Modifier.align(Alignment.TopEnd).padding(8.dp).clip(CircleShape).background(Color(0xAA080808)).clickable{onSave()}.padding(horizontal=9.dp,vertical=5.dp),color=XWhite,fontSize=14.sp)};Column(Modifier.padding(11.dp)){Tag(item.source);Text(item.title,color=XBlack,fontSize=15.sp,lineHeight=18.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=5.dp),maxLines=2);Text(item.description,color=Color(0xFF625F59),fontSize=10.sp,lineHeight=15.sp,maxLines=4,modifier=Modifier.padding(top=6.dp));Row(Modifier.padding(top=10.dp),verticalAlignment=Alignment.CenterVertically){Text("SEGUIR DESCUBRIENDO",color=XBlack,fontSize=7.sp,letterSpacing=.5.sp,fontWeight=FontWeight.Black);Spacer(Modifier.weight(1f));Text("↗",color=XBlack,fontSize=14.sp)}}}
}
@Composable
fun ExperienceMapScreen(onOpen:(Discovery)->Unit){
 val nodes=listOf(Triple("Nirvana",.50f,.42f),Triple("Nevermind",.22f,.20f),Triple("In Utero",.78f,.20f),Triple("Seattle",.16f,.68f),Triple("Grunge",.83f,.67f),Triple("Pink Floyd",.51f,.85f))
 val links=listOf(0 to 1,0 to 2,0 to 3,0 to 4,0 to 5,1 to 3,2 to 4)
 val t=rememberInfiniteTransition(label="map");val pulse by t.animateFloat(.75f,1.25f,infiniteRepeatable(tween(1800),RepeatMode.Reverse),label="pulse")
 Column(Modifier.fillMaxSize().background(XBlack).padding(horizontal=16.dp)){
  Row(Modifier.fillMaxWidth().padding(top=21.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Tag("RABBITHOLE / CARTOGRAFÍA",Color.LightGray);Text("Todo está\nconectado.",color=XWhite,fontSize=35.sp,lineHeight=36.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.5).sp)};OrbitMark(Modifier.size(80.dp))}
  Text("Una idea nunca está aislada. Toca un nodo, sigue una conexión y deja que el mapa se expanda.",color=Color(0xFFB7B4AD),fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=8.dp,bottom=14.dp))
  BoxWithConstraints(Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(22.dp)).background(Brush.radialGradient(listOf(Color(0xFF292925),XBlack)))){
   val w=constraints.maxWidth.toFloat();val h=constraints.maxHeight.toFloat()
   Canvas(Modifier.fillMaxSize()){val pts=nodes.map{Offset(w*it.second,h*it.third)};for(i in 0..38){val x=(i*73%997)/997f*size.width;val y=(i*137%991)/991f*size.height;drawCircle(Color.White.copy(alpha=if(i%3==0).30f else .12f),if(i%4==0)1.6.dp.toPx() else 1.dp.toPx(),Offset(x,y))};links.forEach{(a,b)->drawLine(Color.White.copy(alpha=.25f),pts[a],pts[b],1.dp.toPx());drawCircle(Color.White.copy(alpha=.45f),2.dp.toPx(),Offset((pts[a].x+pts[b].x)/2,(pts[a].y+pts[b].y)/2))};pts.forEachIndexed{i,p->if(i==0){drawCircle(Color.White.copy(alpha=.08f),43.dp.toPx()*pulse,p,style=Stroke(1.dp.toPx()));drawCircle(Color.White.copy(alpha=.15f),30.dp.toPx(),p);drawCircle(XWhite,5.dp.toPx(),p)}else{drawCircle(Color.White.copy(alpha=.13f),22.dp.toPx(),p);drawCircle(XWhite,3.dp.toPx(),p)}}}
   nodes.forEachIndexed{i,n->val item=catalog.find{it.title==n.first}?:catalog.first();val bob by t.animateFloat(if(i%2==0)-3f else 3f,if(i%2==0)3f else -3f,infiniteRepeatable(tween(2100+i*120),RepeatMode.Reverse),label="node");Column(Modifier.offset(x=maxWidth*n.second-46.dp,y=maxHeight*n.third-42.dp+bob.dp).width(92.dp).clickable{onOpen(item)},horizontalAlignment=Alignment.CenterHorizontally){Box(Modifier.size(if(i==0)54.dp else 43.dp).clip(CircleShape).background(Color(0xFF292925)).border(1.dp,Color.White.copy(alpha=.55f),CircleShape),contentAlignment=Alignment.Center){if(i==0)OrbitMark(Modifier.fillMaxSize(.8f))else Text("✦",color=XWhite,fontSize=15.sp)};Text(n.first,color=XWhite,fontSize=10.sp,fontWeight=if(i==0)FontWeight.Bold else FontWeight.Medium,modifier=Modifier.padding(top=5.dp))}}
   Tag("MAPA DE MUESTRA · MÚSICA",Color.LightGray);Text("001 / LIVE",Modifier.align(Alignment.BottomEnd).padding(13.dp),color=Color.LightGray,fontSize=8.sp,letterSpacing=1.3.sp)
  }
  Row(Modifier.fillMaxWidth().padding(vertical=13.dp),verticalAlignment=Alignment.CenterVertically){Tag("6 NODOS",XWhite);Spacer(Modifier.weight(1f));Tag("TOCA UN PUNTO PARA EXPLORAR")}
 }
}
@Composable
fun ExperienceLibraryScreen(title:String,subtitle:String,items:List<Discovery>,saved:Set<String>,onOpen:(Discovery)->Unit,onSave:(String)->Unit){
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(start=16.dp,end=16.dp,top=23.dp,bottom=28.dp)){item{Tag("RABBITHOLE / COLECCIÓN");Text(title,color=XBlack,fontSize=35.sp,lineHeight=38.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.5).sp,modifier=Modifier.padding(top=7.dp));Text(subtitle,color=XMuted,fontSize=12.sp,lineHeight=18.sp,modifier=Modifier.padding(top=7.dp,bottom=19.dp))};if(items.isEmpty())item{Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(20.dp)).background(XBlack),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){OrbitMark(Modifier.size(65.dp));Text("Tu próxima historia empieza aquí.",color=XWhite,fontSize=15.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=13.dp))}}}else items(items.chunked(2)){pair->Row(Modifier.fillMaxWidth().padding(bottom=10.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){pair.forEach{item->PosterCard(item,item.title in saved,{onOpen(item)},{onSave(item.title)},Modifier.weight(1f),true)};if(pair.size==1)Spacer(Modifier.weight(1f))}}}
}
@Composable
fun ExperienceDetail(item:Discovery,saved:Boolean,onBack:()->Unit,onSave:()->Unit,onConnection:(String)->Unit){
 val uri=LocalUriHandler.current;var expanded by remember(item.title){mutableStateOf(false)};val hero=posterFor(item)
 LazyColumn(Modifier.fillMaxSize().background(XPaper),contentPadding=PaddingValues(bottom=30.dp)){
  item{Box(Modifier.fillMaxWidth().height(330.dp).background(XBlack)){if(hero.isNotBlank())AsyncImage(model=hero,contentDescription=item.title,modifier=Modifier.fillMaxSize(),contentScale=ContentScale.Crop,colorFilter=ColorFilter.colorMatrix(ColorMatrix().apply{setToSaturation(.18f)}))else OrbitMark(Modifier.align(Alignment.Center).size(220.dp));Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0x22000000),Color(0xEE000000)))));Row(Modifier.align(Alignment.TopStart).fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically){Text("←  VOLVER",Modifier.clip(CircleShape).background(Color(0x55000000)).clickable{onBack()}.padding(horizontal=13.dp,vertical=10.dp),color=XWhite,fontSize=9.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.weight(1f));Tag("RABBIT HOLE / STORY",XWhite)};Column(Modifier.align(Alignment.BottomStart).padding(21.dp)){Tag(item.source+"  ·  "+item.type,Color.LightGray);Text(item.title,color=XWhite,fontSize=39.sp,lineHeight=41.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.7).sp,modifier=Modifier.padding(top=7.dp));Text(item.category.uppercase(),color=Color.LightGray,fontSize=9.sp,letterSpacing=1.5.sp,modifier=Modifier.padding(top=7.dp))}}
   Column(Modifier.padding(horizontal=19.dp)){Row(Modifier.fillMaxWidth().padding(top=17.dp),verticalAlignment=Alignment.CenterVertically){Text(if(saved)"EN TU COLECCIÓN ✓"else"GUARDAR EN TU COLECCIÓN  +",Modifier.clip(CircleShape).background(XBlack).clickable{onSave()}.padding(horizontal=14.dp,vertical=11.dp),color=XWhite,fontSize=8.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.weight(1f));Tag("LECTURA · 3 MIN")};Tag("LA HISTORIA DETRÁS DEL HALLAZGO");Text(item.title,color=XBlack,fontSize=27.sp,lineHeight=30.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=7.dp));Text(item.description.ifBlank{"Una puerta de entrada para explorar este tema y sus conexiones."},color=Color(0xFF393833),fontSize=15.sp,lineHeight=24.sp,modifier=Modifier.padding(top=10.dp));if(item.description.length>300){AnimatedVisibility(expanded,enter=fadeIn()+scaleIn(),exit=fadeOut()){Text("Este resumen procede de "+item.source+". Abre la referencia original para consultar el contexto completo y profundizar en sus detalles.",color=Color(0xFF55524C),fontSize=13.sp,lineHeight=21.sp,modifier=Modifier.padding(top=12.dp))};Text(if(expanded)"MOSTRAR MENOS"else"SEGUIR LEYENDO",Modifier.clickable{expanded=!expanded}.padding(top=12.dp),color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Black)};if(item.url.isNotBlank())Text("CONSULTAR FUENTE ORIGINAL  ↗",Modifier.padding(top=16.dp).clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(item.url)}}.padding(horizontal=14.dp,vertical=11.dp),color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Bold);if(item.audioUrl.isNotBlank())Text("ESCUCHAR FRAGMENTO  ↗",Modifier.padding(top=8.dp).clip(CircleShape).border(1.dp,XLine,CircleShape).clickable{runCatching{uri.openUri(item.audioUrl)}}.padding(horizontal=14.dp,vertical=11.dp),color=XBlack,fontSize=9.sp,fontWeight=FontWeight.Bold);EditorialSections(item);Text("SIGUE EL HILO",color=XMuted,fontSize=9.sp,letterSpacing=1.5.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=28.dp,bottom=10.dp))}
  }
  items(item.connections){connection->Row(Modifier.fillMaxWidth().padding(horizontal=19.dp,vertical=4.dp).clip(RoundedCornerShape(15.dp)).background(XWhite).clickable{onConnection(connection)}.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(37.dp).clip(CircleShape).background(XBlack),contentAlignment=Alignment.Center){Text("↗",color=XWhite,fontSize=17.sp)};Column(Modifier.weight(1f).padding(start=12.dp)){Text(connection,color=XBlack,fontSize=13.sp,fontWeight=FontWeight.Bold);Text("Una nueva ruta para continuar explorando",color=XMuted,fontSize=10.sp,modifier=Modifier.padding(top=3.dp))};Text("→",color=XBlack,fontSize=18.sp)}}
 }
}
@Composable
fun ExperienceNavigation(active:String,onSelect:(String)->Unit){
 Row(Modifier.fillMaxWidth().background(XBlack).padding(horizontal=7.dp,vertical=9.dp),horizontalArrangement=Arrangement.SpaceEvenly){listOf("Inicio" to "⌂","Universo" to "◎","Guardados" to "◇","Recorrido" to "↺").forEach{(name,glyph)->val selected=active==name;val scale by animateFloatAsState(if(selected)1.08f else 1f,tween(220),label="nav");Column(Modifier.weight(1f).scale(scale).clip(RoundedCornerShape(13.dp)).clickable{onSelect(name)}.padding(vertical=5.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(glyph,color=if(selected)XWhite else Color(0xFF77746F),fontSize=19.sp);Text(name.uppercase(),color=if(selected)XWhite else Color(0xFF77746F),fontSize=7.sp,letterSpacing=.7.sp,fontWeight=if(selected)FontWeight.Black else FontWeight.Medium);if(selected)Box(Modifier.padding(top=4.dp).width(13.dp).height(2.dp).clip(CircleShape).background(XWhite))}}}
}
