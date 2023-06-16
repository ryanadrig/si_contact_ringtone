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

import com.siginfinite.si_contact_ringtone.SICRPermissions
import com.siginfinite.si_contact_ringtone.SICRGetContacts
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

    else if (call.method == "requestContactsMusicAndStoragePermissions"){
      val perms_granted : Boolean = SICRPermissions().requestPerms(activity!!)
      result.success(perms_granted)
    }

    else if (call.method == "getContacts") {
      val jsonresstr : String = SICRGetContacts().getContacts(activity!!)
      result.success(jsonresstr)
      }


    else if (call.method == "setContactNameByNumber") {
      result.success("setContactNameByNumber complete name ~ " + call.argument("newName")
      + "number ~ " + call.argument("contactNumber"))
    }

    else if (call.method == "setContactRingtoneByNumber") {
      result.success("setContactRingtoneByNumber ringtone data path ~ " + call.argument("path")
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
