package com.siginfinite.si_contact_ringtone
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import android.database.Cursor
import android.media.RingtoneManager
import android.provider.MediaStore
import java.util.*

class SICRAudioList {

    fun getAllRingtones(call_activity: Activity): MutableList<MutableMap<String, String>>{
        val manager = RingtoneManager(call_activity)
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor = manager.cursor

//        val list: MutableMap<String, String> = HashMap()
        val build_ret_list : MutableList<MutableMap<String, String>> = mutableListOf()
        cursor.moveToFirst()
        val notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
        println("rt not title ~ " + notificationTitle)
        val notificationUri =
            cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(
                RingtoneManager.ID_COLUMN_INDEX
            )
        val song0 = mutableMapOf("title" to notificationTitle,
            "contentURI" to notificationUri)
        build_ret_list.add(song0)
        while (cursor.moveToNext()) {
            val notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            println("rt not title ~ " + notificationTitle)
            val notificationUri =
                cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(
                    RingtoneManager.ID_COLUMN_INDEX
                )
            println("rt not uri ~ " + notificationUri)
            val songadd = mutableMapOf("title" to notificationTitle,
                "contentURI" to notificationUri,
                "media_type" to "ringtone"
            )

            build_ret_list.add(songadd)
        }
        cursor.close()
        println("getAllRingtones build ret list ~ " + build_ret_list.toString())
        return build_ret_list
    }

    fun getAllMusicAndRTRemDups(call_activity: Activity): String{
        println("Get all music remove duplicates called")

        val gar_list: MutableList<MutableMap<String, String>> = getAllRingtones(call_activity)

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
//            MediaStore.Audio.Media.DURATION
        )

        val cursor: Cursor? = call_activity.applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

//        val songs: MutableList< MutableMap<String, String>> = ArrayList()
        var songno = 0
        var songs = gar_list
        while (cursor!!.moveToNext()) {
            var song_data:MutableMap<String, String>

            var song_exists : Boolean = false
            for(sn in songs){
                if (sn["title"] == cursor.getString(2)){
                    song_exists = true
                }
            }

            if (song_exists == false) {
                song_data = mutableMapOf(
                    "song_id" to cursor.getString(0),
                    "artist" to cursor.getString(1),
                    "title" to cursor.getString(2),
                    "data" to cursor.getString(3),
                    "display_name" to cursor.getString(4),
                    "media_type" to "music"
                )
                songs.add(song_data)
            }
        }
        cursor.close()
        println("all ringtones and songs rem dups~~ " + songs.toString())
        val gson = Gson()
        val jsonres = gson.toJson(songs)
        val jsonresstr = jsonres.toString()
        return jsonresstr
    }

}