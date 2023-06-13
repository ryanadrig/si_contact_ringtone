package com.siginfinite.si_contact_ringtone

import android.app.Activity
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.*

/** SiContactRingtonePlugin */
class SiContactRingtonePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel


  private var call_activity: Activity? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "si_contact_ringtone")
    channel.setMethodCallHandler(this)



  }



  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (activity == null) return result.success(null)

    when (call.method) {
      "getPlatformVersion" -> {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }

      "setContactNameByNumber" -> {
        val cont_number: String = call.argument("contactNumber")
        val cont_new_name: String = call.argument("newName")

        val values = ContentValues()
        val resolver: ContentResolver = call_activity!!.applicationContext.contentResolver

        println("lookup contact by number")

//        val cont_number = "4445555"
//        val cont_new_name = "Some New Name"

        val lookupUri = Uri.withAppendedPath(
          ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
          cont_number
        )

        val projection = arrayOf(
          ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
        )
        val data: Cursor? = resolver.query(
          lookupUri, projection, null, null, null
        )

        if (data != null && data.moveToFirst()) {

          println("found contact all proj data ")
          data.moveToFirst()
          // Get the contact lookup Uri
          val contactId = data.getLong(0)
          val lookupKey = data.getString(1)

          println("contact id " + contactId.toString())

          val contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey)

          println("got contactUri " + contactUri.toString())

          val DATA_COLS = arrayOf(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,  //phone number
            ContactsContract.Data.CONTACT_ID
          )

          val operations: ArrayList<ContentProviderOperation> = ArrayList()

          //change selection for number
//                where = String.format(
//                    "%s = '%s' AND %s = ?",
//                    DATA_COLS[0],//mimetype
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                    DATA_COLS[1]/*number*/);
//                operations.add(
//                    ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                        .withSelection(where, args)
//                        .withValue(DATA_COLS[1]/*number*/, newNumber)
//                        .build()
//                );

          val where = java.lang.String.format(
            "%s = '%s' AND %s = ?",
            DATA_COLS[0],  //mimetype
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
            DATA_COLS[2] /*contactId*/
          )

          val args = arrayOf<String>(contactId.toString())

          operations.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
              .withSelection(where, args)
              .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, cont_new_name)
              .build()
          );



          try {

            val results: Array<ContentProviderResult> =
              call_activity!!.applicationContext.getContentResolver().applyBatch(
                ContactsContract.AUTHORITY,
                operations
              )

            for (result in results) {
              println("Update Result" + result.toString());
            }

            data.close()
          } catch (e: Exception) {
            e.printStackTrace();
          }

        } else {
          println("contact not existing")
        }
        result.success(null)
      }

      else -> {
        result.notImplemented()
      }
    }
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
