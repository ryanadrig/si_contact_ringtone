package com.siginfinite.si_contact_ringtone

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import java.io.File

class SICRSetRingtone {

    fun setRingtoneByNumber(call_activity: Activity, number: String, path: String) : Bool{

        val resolver: ContentResolver = call_activity.applicationContext.contentResolver

//        val fpath = "/storage/emulated/0/Download/exc_ogg.ogg"
        val file = File(path)
        val contact_number = number

        val fContUri = MediaStore.Audio.Media.getContentUriForPath(path)
        //        val fContUri = MediaStore.Audio.Media.getCollectionUriForFile()

        println("Deleting content uri ~ " + fContUri)
        resolver.delete(fContUri!!, null, null)

//        resolver.delete(fContUri!!,
//            MediaStore.MediaColumns.DATA + "=?", arrayOf( file.getAbsolutePath())
//            , null)


        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            contact_number
        )

        var ct_rt_uri : String = "notfound"

        println("Ct uri ~ " + lookupUri.toString())
        // Query Contact Data
        val ct_data_test: Cursor? = resolver.query(lookupUri!!, null, null, null, null)

        while (ct_data_test!!.moveToNext()) {
            var col_idx = 0;
            for (col in ct_data_test.columnNames) {
                val columnKey: String = ct_data_test.getColumnName(col_idx)
                println("CT Column name ~ " + columnKey)
                val columnVal = ct_data_test.getString(col_idx)
                println("CT Col val ~ " + columnVal)

                if (columnKey == "custom_ringtone" && columnVal != null){
                    ct_rt_uri = columnVal
                }
                col_idx += 1
            }
        }
        ct_data_test!!.close()

        println("check content vals for uri ~ " + ct_rt_uri)

        val rt_uri = Uri.parse(ct_rt_uri)

        val rt_data: Cursor? = resolver.query(rt_uri, null, null, null, null)
        println("what is rt_data " + rt_data.toString())
        if (rt_data != null ) {
            if (rt_data!!.count > 0) {
                println("rtct ~" + rt_data!!.count.toString())
                rt_data!!.moveToFirst()
                SICCursorUtil().loopCurse(rt_data)

            }
            rt_data!!.close()
        }


        val projection = arrayOf(
            ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
        )

        val ct_data: Cursor? = resolver.query(
            lookupUri,
            projection, null, null, null
        )

        if (ct_data != null && ct_data.moveToFirst()) {

            ct_data.moveToFirst()
            // Get the contact lookup Uri
            val contactId = ct_data.getLong(0)
            val lookupKey = ct_data.getString(1)

            println("Uri String for Contact " + ContactsContract.Contacts.CONTENT_URI)
            println("Got Contact Data :: ID ~ " + contactId + " lookupKEY ~ " + lookupKey)
            println("setting file abs path ~ " + path)


            val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId.toString())
            println(" got contact lookup URI ~ " + contactUri.toString())
            println("update media file uri ~ " + fContUri.toString())


            val mf_projection = arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA
            )

            // media file data
            val mf_data: Cursor? = resolver.query(
                fContUri,
                mf_projection, null, null, null
            )
            if (mf_data != null && mf_data.moveToFirst()) {
                mf_data.moveToFirst()
                println("loop media content media field data")
                SICCursorUtil().loopCurse(mf_data)
            }


            // Attempt to get Ringtone into Ringtones list failed with many permutations of these calls
//                val media_values: ContentValues = ContentValues()
//                media_values.put(MediaStore.MediaColumns.DATA, fpath)
//                media_values.put(MediaStore.MediaColumns.TITLE, "Exc 105")
//                media_values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Exc DDD")
//                media_values.put(MediaStore.MediaColumns.MIME_TYPE, getMIMEType(fpath))
//                media_values.put(MediaStore.MediaColumns.SIZE, file.length())
//                media_values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true)
//                media_values.put(MediaStore.Audio.Media.IS_ALARM, true)
//                media_values.put(MediaStore.Audio.Media.IS_MUSIC, false)
////                media_values.put(MediaStore.MediaColumns.BITRATE, 160000)
////                media_values.put(MediaStore.MediaColumns._ID, 666)
////                media_values.put(MediaStore.MediaColumns.XMP, Blob())
////                media_values.put(MediaStore.MediaColumns.SIZE, 6666666)
//                media_values.put(MediaStore.Audio.Media.IS_RINGTONE, 1)
//                val newUri: Uri? = resolver.insert(fContUri!!, media_values)
//                collection =  MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY); // added in @api<=29 to get the primary external storage
//                values.put(MediaStore.Audio.Media.RELATIVE_PATH, (Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath());
//                Uri itemUri = resolver.insert(collection, values);
//                val updated = resolver.update(fContUri, media_values, null, null)
//                val updated = resolver.update(mediaUri!!, media_values, null, null)


            val contact_values: ContentValues = ContentValues()
//                contact_values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId.toString())
            contact_values.put(ContactsContract.Data.CUSTOM_RINGTONE, path)
            resolver.update(contactUri, contact_values, null, null);
            return true
        }
        ct_data!!.close()
        return false
    }

}