package com.siginfinite.si_contact_ringtone

import android.app.Activity
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import java.util.*

class SICRSetContactName {
    fun setContactNameByNumber(call_activity: Activity, number: String, newName: String): Bool{
        println("setContactNameByNumber")
        val values = ContentValues()
        val resolver: ContentResolver = call_activity.applicationContext.contentResolver

        println("lookup contact by number")

        val contact_number = number
        val newName = newName
        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            contact_number
        )

        val projection = arrayOf(
            ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
        )
        val data: Cursor? = resolver.query(lookupUri
            , projection, null, null, null)

        if (data != null && data.moveToFirst()) {

            println("found contact proj data ")
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

            //selection for name
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
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newName)
                    .build()
            );

            try {

                val results : Array<ContentProviderResult> =
                    call_activity.applicationContext.getContentResolver().applyBatch(
                        ContactsContract.AUTHORITY,
                        operations
                    )

                for (result in results) {
                    println("Update Result" + result.toString());
                    if (result.count!! > 0){
                        printlnt("result updated")
                        return true
                    }
                }

                data.close()
            }
            catch (e: Exception) {
                e.printStackTrace();
                return false
            }

        }
        else {
            println("contact not existing")
            return false
        }

        return false

    }
}