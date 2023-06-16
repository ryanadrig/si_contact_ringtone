package com.siginfinite.si_contact_ringtone

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import java.util.*

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SICRPermissions {
    fun requestPerms(activity: Activity?): Boolean{
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
            return false
        }
        return true
    }
}