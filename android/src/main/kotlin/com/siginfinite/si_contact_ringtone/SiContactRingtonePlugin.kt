package com.siginfinite.si_contact_ringtone

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONObject
import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.gson.Gson

//import kotlinx.serialization.Serializable
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.*
//import kotlinx.serialization.json.JSON
//import org.json.JSONObject
//import org.json.JSONStringer


/** SiContactRingtonePlugin */
class SiContactRingtonePlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private var activity: Activity? = null
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "si_contact_ringtone")
    channel.setMethodCallHandler(this)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
    this.activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    this.activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    this.activity = null
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    if (call.method == "requestContactsMusicAndStoragePermissions"){

      val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

        val readExternal =
          ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeExternal =
          ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readContacts =
          ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.READ_CONTACTS)
        val writeContacts =
          ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.WRITE_CONTACTS)

        val readMedia =
          ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.READ_MEDIA_AUDIO)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (readExternal != PackageManager.PERMISSION_GRANTED) {
          listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
          listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (readContacts != PackageManager.PERMISSION_GRANTED) {
          listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (writeContacts != PackageManager.PERMISSION_GRANTED) {
          listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)
        }

        // Api level 31 and above which is pretty new
        if (readMedia != PackageManager.PERMISSION_GRANTED) {
          listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO)
        }

        if (!listPermissionsNeeded.isEmpty()) {
          ActivityCompat.requestPermissions(
            activity!!,
            listPermissionsNeeded.toTypedArray(),
            REQUEST_ID_MULTIPLE_PERMISSIONS
          )
          result.success(false)
        }
        result.success(true)

    }

    if (call.method == "getContacts") {

        var bres: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

        val resolver: ContentResolver = activity!!.contentResolver

        val lookupUri = ContactsContract.Contacts.CONTENT_URI
        // Query Contact Data
        val ct_data_test: Cursor? = resolver.query(
          lookupUri!!, null, null, null, null)

        ct_data_test!!.moveToFirst()
        val ct_data_len = ct_data_test!!.count
        var ct_idx= 0
        while (ct_idx < ct_data_len) {
          var col_idx = 0;
          for (col in ct_data_test.columnNames) {
            var brci : MutableMap<String, String> = mutableMapOf()
            val columnKey: String = ct_data_test.getColumnName(col_idx)


            if (columnKey == "display_name") {
              var columnVal = ct_data_test.getString(col_idx)
              if (columnVal == null){
                columnVal = "null"
              }
              brci.put(columnKey, columnVal)
            }
            if (columnKey == "custom_ringtone") {
              var columnVal = ct_data_test.getString(col_idx)
              if (columnVal == null){
                columnVal = "null"
              }
              brci.put(columnKey, columnVal)
            }
            if (columnKey == "_id"){

              val columnVal = ct_data_test.getInt(col_idx)
              val phoneCursor: Cursor? = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + columnVal,
                null,
                null
              )

              while (phoneCursor!!.moveToNext()) {
                val phone = phoneCursor.getString(
                  phoneCursor
                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                if (phone == null){

                }else {

                  brci.put("phoneNumber", phone.toString())
                }
              }


            }
            if (columnKey == "display_name_reverse"){
              var columnVal = ct_data_test.getString(col_idx)
              if (columnVal == null){
                columnVal = "null"
              }
              brci.put(columnKey, columnVal.toString())
            }
            if (columnKey == "photo_thumb_uri"){
              var columnVal = ct_data_test.getString(col_idx)
              if (columnVal == null){
                columnVal = "null"
              }
              brci.put(columnKey, columnVal.toString())
            }
            if (columnKey == "photo_uri"){
              var columnVal = ct_data_test.getString(col_idx)
              if (columnVal == null){
                columnVal = "null"
              }
              brci.put(columnKey, columnVal.toString())
            }
            if (columnKey == "photo_file_id"){
              var columnVal = ct_data_test.getInt(col_idx)
              if (columnVal == null){
                columnVal = 0
              }
              brci.put(columnKey, columnVal.toString())
            }
            if (columnKey == "photo_id"){
              var columnVal  = ct_data_test.getInt(col_idx)
              if (columnVal == null){
                columnVal = 0
              }
              brci.put(columnKey, columnVal.toString())
            }
            if (brci.isEmpty() == false) {
              if (bres[ct_idx.toString()] == null){
                bres[ct_idx.toString()] = mutableMapOf()
              }
              bres[ct_idx.toString()]!!.putAll(brci)
            }
            col_idx += 1
          }
          ct_idx += 1
          ct_data_test.moveToNext()

        }
        ct_data_test!!.close()

//        val jsonres: JSONObject = JSONObject(bres.toString())

      val gson = Gson()
      val jsonres = gson.toJson(bres)
//      println("json convert complete")
//      println("to gson ~ " + json.toString())
      val jsonresstr = jsonres.toString()
        return result.success(jsonresstr)
      }


    if (call.method == "setContactNameByNumber") {
      result.success("setContactNameByNumber complete name ~ " + call.argument("newName")
      + "number ~ " + call.argument("contactNumber"))
    }

    else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
