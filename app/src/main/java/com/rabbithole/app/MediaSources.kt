package com.rabbithole.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
suspend fun loadCommonsImage(query:String):String=withContext(Dispatchers.IO){
 try{
  val address="https://commons.wikimedia.org/w/api.php?action=query&generator=search&gsrsearch="+URLEncoder.encode(query,"UTF-8")+"&gsrnamespace=6&gsrlimit=1&prop=imageinfo&iiprop=url&iiurlwidth=1200&format=json"
  val c=URL(address).openConnection() as HttpURLConnection
  c.connectTimeout=7000;c.readTimeout=7000;c.setRequestProperty("User-Agent","RabbitHole/5.0.34 Android discovery app")
  try{
   val json=JSONObject(c.inputStream.bufferedReader().use{it.readText()})
   val pages=json.optJSONObject("query")?.optJSONObject("pages")?:return@withContext ""
   val keys=pages.keys();if(!keys.hasNext())return@withContext ""
   val page=pages.optJSONObject(keys.next())?:return@withContext ""
   val info=page.optJSONArray("imageinfo")?.optJSONObject(0)?:return@withContext ""
   info.optString("thumburl").ifBlank{info.optString("url")}
  }finally{c.disconnect()}
 }catch(_:Exception){""}
}