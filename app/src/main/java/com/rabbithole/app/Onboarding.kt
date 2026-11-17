package com.rabbithole.app
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
private val ink=Color(0xFF111111)
private val paper=Color(0xFFF4F3F0)
private val muted=Color(0xFF777570)
private val rule=Color(0xFFE2E0DC)
private val groups=linkedMapOf(
 "Música" to listOf("Rock alternativo","Grunge","Música electrónica","Hip-hop","Jazz","Música clásica"),
 "Cine y series" to listOf("Cine de autor","Ciencia ficción","Terror","Documentales","Animación"),
 "Arte y diseño" to listOf("Diseño gráfico","Arquitectura","Fotografía","Arte moderno","Diseño industrial"),
 "Ciencia" to listOf("Astronomía","Tecnología","Naturaleza","Psicología","Física"),
 "Historia" to listOf("Historia antigua","Historia contemporánea","Culturas","Personajes","Exploración"),
 "Libros e ideas" to listOf("Literatura","Filosofía","Cómics","Ensayo","Mitología"),
 "Videojuegos" to listOf("Historia del videojuego","Juegos independientes","Diseño de juegos","Mundos y lore")
)
@Composable fun OnboardingScreen(selected:Set<String>,onToggle:(String)->Unit,onContinue:()->Unit){
 var group by remember{mutableStateOf("Música")}
 Column(Modifier.fillMaxSize().background(paper)){
  Row(Modifier.fillMaxWidth().padding(22.dp),verticalAlignment=Alignment.CenterVertically){
   Text("◉",fontSize=25.sp);Spacer(Modifier.width(9.dp));Text("RABBIT HOLE",fontWeight=FontWeight.Black,letterSpacing=2.sp);Spacer(Modifier.weight(1f));Text("TU PERFIL",fontSize=8.sp,color=muted)
  }
  Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal=22.dp)){
   Text("UNA EXPERIENCIA QUE EVOLUCIONA CONTIGO",fontSize=8.sp,color=muted,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold)
   Text("Tu curiosidad\nmarca el camino.",Modifier.padding(top=12.dp),fontSize=38.sp,lineHeight=40.sp,fontWeight=FontWeight.Black,letterSpacing=(-1.8).sp)
   Text("RabbitHole convierte tus intereses en rutas de descubrimiento. Explora temas, encuentra conexiones y aprende algo inesperado en cada paso.",Modifier.padding(top=12.dp),fontSize=12.sp,lineHeight=19.sp,color=muted)
   Row(Modifier.fillMaxWidth().padding(top=19.dp).background(ink,RoundedCornerShape(4.dp)).padding(15.dp),verticalAlignment=Alignment.CenterVertically){
    Column(Modifier.weight(1f)){Text("NO HAY DOS UNIVERSOS IGUALES",fontSize=8.sp,color=Color.LightGray,letterSpacing=1.sp);Text("Tus elecciones ayudan\na ordenar tus hallazgos.",Modifier.padding(top=6.dp),fontSize=17.sp,lineHeight=20.sp,color=Color.White,fontWeight=FontWeight.Bold)}
    Text("◎",fontSize=52.sp,color=Color.White)
   }
   Text("ELIGE TUS ÁREAS DE INTERÉS",Modifier.padding(top=22.dp,bottom=10.dp),fontSize=9.sp,color=muted,letterSpacing=1.2.sp,fontWeight=FontWeight.Bold)
   Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)){
    groups.keys.forEach{label->val active=group==label;Text(label,Modifier.background(if(active)ink else Color.White,RoundedCornerShape(30.dp)).border(1.dp,if(active)ink else rule,RoundedCornerShape(30.dp)).clickable{group=label}.padding(horizontal=11.dp,vertical=9.dp),fontSize=9.sp,color=if(active)Color.White else ink)}
   }
   Text("Marca los temas que te gustaría explorar. Puedes elegir varios.",Modifier.padding(top=10.dp,bottom=12.dp),fontSize=10.sp,color=muted)
   groups[group].orEmpty().chunked(2).forEach{pair->
    Row(Modifier.fillMaxWidth().padding(bottom=8.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){
     pair.forEach{topic->
      val active=topic in selected
      Column(Modifier.weight(1f).height(76.dp).background(if(active)ink else Color.White,RoundedCornerShape(4.dp)).border(1.dp,if(active)ink else rule,RoundedCornerShape(4.dp)).clickable{onToggle(topic)}.padding(11.dp),verticalArrangement=Arrangement.SpaceBetween){
       Text(if(active)"AÑADIDO" else "DESCUBRIR",fontSize=7.sp,color=if(active)Color.LightGray else muted)
       Row(verticalAlignment=Alignment.CenterVertically){Text(topic,Modifier.weight(1f),fontSize=11.sp,color=if(active)Color.White else ink,fontWeight=FontWeight.SemiBold);Text(if(active)"✓" else "+",fontSize=17.sp,color=if(active)Color.White else muted)}
      }
     }
     if(pair.size==1)Spacer(Modifier.weight(1f))
    }
   }
   Text(selected.size.toString()+" temas seleccionados",Modifier.padding(top=5.dp,bottom=8.dp),fontSize=9.sp,color=muted)
   Text("También podrás explorar cualquier tema fuera de tus preferencias y cambiar esta selección cuando quieras.",Modifier.padding(bottom=15.dp),fontSize=9.sp,lineHeight=14.sp,color=muted)
  }
  Column(Modifier.fillMaxWidth().padding(horizontal=22.dp,vertical=13.dp)){
   Box(Modifier.fillMaxWidth().background(ink,RoundedCornerShape(4.dp)).clickable{onContinue()}.padding(vertical=16.dp),contentAlignment=Alignment.Center){Text("EMPEZAR A EXPLORAR  →",fontSize=10.sp,color=Color.White,fontWeight=FontWeight.Bold,letterSpacing=1.sp)}
   Text("Tu universo puede cambiar contigo.",Modifier.align(Alignment.CenterHorizontally).padding(top=8.dp),fontSize=8.sp,color=muted)
  }
 }
}