package com.siginfinite.si_contact_ringtone

import android.app.Activity
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract

import com.google.gson.Gson


class SICRGetContacts {

    fun getContacts(activity: Activity): String{
        var bres: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

        val resolver: ContentResolver = activity!!.contentResolver

        val lookupUri = ContactsContract.Contacts.CONTENT_URI
        // Query Contact Data
        val ct_data_test: Cursor? = resolver.query(
            lookupUri!!, null, null, null, null
        )

        ct_data_test!!.moveToFirst()
        val ct_data_len = ct_data_test!!.count
        var ct_idx = 0
        while (ct_idx < ct_data_len) {
            var col_idx = 0;
            for (col in ct_data_test.columnNames) {
                var brci: MutableMap<String, String> = mutableMapOf()
                val columnKey: String = ct_data_test.getColumnName(col_idx)


                if (columnKey == "display_name") {
                    var columnVal = ct_data_test.getString(col_idx)
                    if (columnVal == null) {
                        columnVal = "null"
                    }
                    brci.put(columnKey, columnVal)
                }
                if (columnKey == "custom_ringtone") {
                    var columnVal = ct_data_test.getString(col_idx)
                    if (columnVal == null) {
                        columnVal = "null"
                    }
                    brci.put(columnKey, columnVal)
                }
                if (columnKey == "_id") {

                    val columnVal = ct_data_test.getInt(col_idx)
                    println("[SICRGetContacts] contact id ~ " + columnVal.toString())
                    brci.put(columnKey, columnVal.toString())
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
                        if (phone == null) {

                        } else {

                            brci.put("phoneNumber", phone.toString())
                        }
                    }

                    phoneCursor.close()

                }
                if (columnKey == "display_name_reverse") {
                    var columnVal = ct_data_test.getString(col_idx)
                    if (columnVal == null) {
                        columnVal = "null"
                    }
                    brci.put(columnKey, columnVal.toString())
                }
                if (columnKey == "photo_thumb_uri") {
                    var columnVal = ct_data_test.getString(col_idx)
                    if (columnVal == null) {
                        columnVal = "null"
                    }
                    brci.put(columnKey, columnVal.toString())
                }
                if (columnKey == "photo_uri") {
                    var columnVal = ct_data_test.getString(col_idx)
                    if (columnVal == null) {
                        columnVal = "null"
                    }
                    brci.put(columnKey, columnVal.toString())
                }
                if (columnKey == "photo_file_id") {
                    var columnVal = ct_data_test.getInt(col_idx)
                    if (columnVal == null) {
                        columnVal = 0
                    }
                    brci.put(columnKey, columnVal.toString())
                }
                if (columnKey == "photo_id") {
                    var columnVal = ct_data_test.getInt(col_idx)
                    if (columnVal == null) {
                        columnVal = 0
                    }
                    brci.put(columnKey, columnVal.toString())
                }
                if (brci.isEmpty() == false) {
                    if (bres[ct_idx.toString()] == null) {
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
        return jsonresstr

    }

}