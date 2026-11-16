package com.rabbithole.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF090909)
private val Paper = Color(0xFFF5F5F3)
private val Muted = Color(0xFF777777)
private val Rule = Color(0xFFE3E3E1)

data class Discovery(val title:String,val type:String,val category:String,val description:String,val connections:List<String>)
private val catalog = listOf(
 Discovery("Nirvana","ARTISTA","Música","La banda que llevó el sonido de Seattle a una generación entera.",listOf("Nevermind","In Utero","Seattle","Grunge","Punk rock")),
 Discovery("Nevermind","ÁLBUM","Música","El álbum de 1991 que cambió el lugar del rock alternativo en la cultura popular.",listOf("Nirvana","Butch Vig","Grunge","MTV Unplugged")),
 Discovery("In Utero","ÁLBUM","Música","El lado más áspero, íntimo y experimental de Nirvana.",listOf("Nirvana","Steve Albini","Alternative rock")),
 Discovery("Seattle","ESCENA MUSICAL","Historia","La ciudad donde creció una escena musical irrepetible.",listOf("Grunge","Sub Pop","Pearl Jam","Soundgarden")),
 Discovery("Grunge","MOVIMIENTO","Música","Una mezcla de punk, metal y sensibilidad alternativa que definió una época.",listOf("Seattle","Nirvana","Soundgarden","Alice in Chains")),
 Discovery("Pink Floyd","ARTISTA","Música","Una banda británica que convirtió los álbumes en experiencias sonoras y visuales.",listOf("The Dark Side of the Moon","The Wall","Rock progresivo")),
 Discovery("The Dark Side of the Moon","ÁLBUM","Música","Un viaje conceptual sobre el tiempo, la presión y la experiencia humana.",listOf("Pink Floyd","Wish You Were Here","Rock progresivo")),
 Discovery("Daft Punk","DÚO MUSICAL","Música","El dúo francés que conectó la electrónica con el pop y la cultura visual.",listOf("Discovery","Random Access Memories","French house")),
 Discovery("Discovery","ÁLBUM","Música","Un universo electrónico de melodías, samples y referencias al pop.",listOf("Daft Punk","Interstella 5555","French house")),
 Discovery("Interstella 5555","PELÍCULA MUSICAL","Cine","Una película animada que cuenta una historia a través de la música de Daft Punk.",listOf("Daft Punk","Discovery","Anime")),
 Discovery("Historia del cine","TEMA","Cine","Movimientos, directores y películas que transformaron el lenguaje cinematográfico.",listOf("Cine mudo","Nuevo Hollywood","Cine documental")),
 Discovery("Apollo 11","MISIÓN ESPACIAL","Ciencia","La misión que llevó a los primeros seres humanos a caminar sobre la Luna.",listOf("NASA","Saturno V","Carrera espacial","Neil Armstrong")),
 Discovery("Voyager","PROGRAMA ESPACIAL","Ciencia","Dos sondas que ampliaron nuestra mirada sobre los límites del sistema solar.",listOf("NASA","Disco de oro","Júpiter","Saturno")),
 Discovery("Historia del diseño","TEMA","Historia","Ideas, movimientos y objetos que cambiaron la manera en que vemos el mundo.",listOf("Bauhaus","Diseño suizo","Diseño industrial"))
)
private val starter = listOf("Nirvana","Universo","Cine de terror","Historia del diseño")

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
  window.statusBarColor=android.graphics.Color.rgb(245,245,243)
  window.navigationBarColor=android.graphics.Color.rgb(245,245,243)
  window.decorView.systemUiVisibility=android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
  setContent{ RabbitHoleApp() }
 }
}

@Composable private fun RabbitHoleApp(){
 var query by remember{mutableStateOf("")}
 var tab by remember{mutableStateOf("Inicio")}
 var category by remember{mutableStateOf("Todo")}
 var selected by remember{mutableStateOf<Discovery?>(null)}
 var saved by remember{mutableStateOf(setOf<String>())}
 var history by remember{mutableStateOf(listOf<String>())}
 val matches=catalog.filter{(category=="Todo"||it.category==category)&&(query.isBlank()||it.title.contains(query,true)||it.description.contains(query,true)||it.connections.any{c->c.contains(query,true)})}
 MaterialTheme(colorScheme=lightColorScheme(primary=Ink,onPrimary=Color.White,background=Paper,surface=Color.White,onSurface=Ink)){
  Column(Modifier.fillMaxSize().background(Paper)){
   Row(Modifier.fillMaxWidth().padding(horizontal=22.dp,vertical=15.dp),verticalAlignment=Alignment.CenterVertically){
    Portal(Modifier.size(34.dp)); Spacer(Modifier.width(10.dp))
    Column { Text("RABBIT HOLE",fontSize=14.sp,fontWeight=FontWeight.Black,letterSpacing=2.sp);Text("A WORLD OF CURIOSITY",fontSize=8.sp,color=Muted,letterSpacing=1.5.sp) }
    Spacer(Modifier.weight(1f));Text("01 / EXPLORE",fontSize=9.sp,color=Muted,letterSpacing=1.sp)
   }
   AnimatedContent(if(selected!=null)"Detalle" else tab,transitionSpec={fadeIn() togetherWith fadeOut()},label="screen"){page->
    when(page){
     "Inicio"->Home(query,{query=it},category,{category=it},matches,saved,{t->saved=if(t in saved)saved-t else saved+t},{item->selected=item;history=(listOf(item.title)+history).distinct().take(40)},{tab="Universo"})
     "Universo"->MapScreen{item->selected=item;history=(listOf(item.title)+history).distinct().take(40)}
     "Guardados"->LibraryScreen("Tu colección","Los descubrimientos que quieres conservar.",catalog.filter{it.title in saved},saved,{selected=it},{t->saved=saved-t})
     "Recorrido"->LibraryScreen("Tu recorrido","Vuelve a los temas que ya exploraste.",history.mapNotNull{t->catalog.find{it.title==t}},saved,{selected=it},{t->saved=if(t in saved)saved-t else saved+t})
     else->Home(query,{query=it},category,{category=it},matches,saved,{t->saved=if(t in saved)saved-t else saved+t},{selected=it},{tab="Universo"})
    }
   }
   if(selected==null) Navigation(tab){tab=it}
  }
  selected?.let{item->Detail(item,item.title in saved,{selected=null},{saved=if(item.title in saved)saved-item.title else saved+item.title}){connection->
   selected=catalog.find{it.title.equals(connection,true)}?:Discovery(connection,"NUEVA RUTA","Todo","Una conexión para continuar tu exploración.",listOf("Contexto","Referencias","Ideas relacionadas"))
   history=(listOf(connection)+history).distinct().take(40)
  }}
 }
}

@Composable private fun Home(query:String,onQuery:(String)->Unit,category:String,onCategory:(String)->Unit,results:List<Discovery>,saved:Set<String>,onSave:(String)->Unit,onOpen:(Discovery)->Unit,onMap:()->Unit){
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

@Composable private fun LibraryScreen(title:String,subtitle:String,items:List<Discovery>,saved:Set<String>,onOpen:(Discovery)->Unit,onSave:(String)->Unit){
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
  Column(Modifier.weight(1f)){Text(item.type,fontSize=8.sp,color=Muted,letterSpacing=1.3.sp,fontWeight=FontWeight.Bold);Text(item.title,fontSize=14.sp,fontWeight=FontWeight.SemiBold);Text(item.description,fontSize=10.sp,color=Muted,maxLines=2,lineHeight=14.sp)}
  Text(if(isSaved)"●" else "＋",Modifier.clickable{onSave()}.padding(start=10.dp),fontSize=17.sp,color=if(isSaved)Ink else Muted)
 }
}

@Composable private fun Detail(item:Discovery,saved:Boolean,onBack:()->Unit,onSave:()->Unit,onConnection:(String)->Unit){
 Column(Modifier.fillMaxSize().background(Paper)){
  Row(Modifier.fillMaxWidth().padding(20.dp),verticalAlignment=Alignment.CenterVertically){Text("←  VOLVER",Modifier.clickable{onBack()},fontSize=10.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp);Spacer(Modifier.weight(1f));Text("RABBIT HOLE / DISCOVERY",fontSize=8.sp,color=Muted)}
  Column(Modifier.padding(horizontal=22.dp)){
   Box(Modifier.fillMaxWidth().height(165.dp).background(Ink,RoundedCornerShape(3.dp)),contentAlignment=Alignment.Center){UniverseGlyph(Modifier.size(130.dp));Text(item.type,Modifier.align(Alignment.BottomStart).padding(14.dp),fontSize=9.sp,color=Color.White,letterSpacing=1.6.sp)}
   Text("YOU FOUND A RABBIT HOLE",Modifier.padding(top=20.dp),fontSize=9.sp,color=Muted,letterSpacing=1.6.sp)
   Text(item.title,Modifier.padding(top=5.dp),fontSize=34.sp,lineHeight=37.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.4).sp)
   Text(item.description,Modifier.padding(top=9.dp),fontSize=13.sp,lineHeight=20.sp,color=Muted)
   Text(if(saved)"GUARDADO  ✓" else "GUARDAR DESCUBRIMIENTO  +",Modifier.padding(top=16.dp).clip(RoundedCornerShape(3.dp)).background(Ink).clickable{onSave()}.padding(horizontal=14.dp,vertical=10.dp),fontSize=9.sp,color=Color.White,letterSpacing=1.sp,fontWeight=FontWeight.Bold)
   Text("SIGUE EL HILO",Modifier.padding(top=24.dp,bottom=10.dp),fontSize=9.sp,color=Muted,letterSpacing=1.7.sp,fontWeight=FontWeight.Bold)
  }
  LazyColumn(contentPadding=PaddingValues(horizontal=22.dp,vertical=2.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){items(item.connections){connection->Row(Modifier.fillMaxWidth().background(Color.White,RoundedCornerShape(3.dp)).border(1.dp,Rule,RoundedCornerShape(3.dp)).clickable{onConnection(connection)}.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(7.dp).background(Ink,CircleShape));Spacer(Modifier.width(12.dp));Text(connection,Modifier.weight(1f),fontSize=12.sp,fontWeight=FontWeight.Medium);Text("↗",fontSize=16.sp,color=Muted)}}}
 }
}

@Composable private fun Portal(modifier:Modifier=Modifier){Canvas(modifier){val c=center;val d=size.minDimension;drawCircle(Ink,d*.46f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()));drawCircle(Ink,d*.29f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()));drawArc(Ink,210f,235f,false,Offset(d*.16f,d*.16f),androidx.compose.ui.geometry.Size(d*.68f,d*.68f),style=androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx(),cap=StrokeCap.Round));drawCircle(Ink,d*.05f,c)}}
@Composable private fun UniverseGlyph(modifier:Modifier=Modifier){Canvas(modifier){val d=size.minDimension;drawCircle(Color(0xFF777777),d*.44f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()));drawCircle(Color(0xFFBDBDBD),d*.30f,style=androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()));drawArc(Color.White,35f,270f,false,Offset(d*.19f,d*.19f),androidx.compose.ui.geometry.Size(d*.62f,d*.62f),style=androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx(),cap=StrokeCap.Round));drawCircle(Color.White,d*.12f);listOf(Offset(d*.5f,d*.02f),Offset(d*.91f,d*.38f),Offset(d*.8f,d*.88f),Offset(d*.12f,d*.75f)).forEach{drawCircle(Color.White,d*.025f,it)}}}
@Composable private fun Navigation(active:String,onSelect:(String)->Unit){Row(Modifier.fillMaxWidth().background(Color.White).border(1.dp,Rule).padding(vertical=9.dp),horizontalArrangement=Arrangement.SpaceEvenly){listOf("Inicio","Universo","Guardados","Recorrido").forEachIndexed{i,name->Column(Modifier.weight(1f).clickable{onSelect(name)},horizontalAlignment=Alignment.CenterHorizontally){Text(listOf("⌂","◎","◇","↺")[i],fontSize=17.sp,color=if(active==name)Ink else Muted);Text(name.uppercase(),fontSize=8.sp,color=if(active==name)Ink else Muted,letterSpacing=.5.sp,fontWeight=if(active==name)FontWeight.Bold else FontWeight.Normal)}}}}
