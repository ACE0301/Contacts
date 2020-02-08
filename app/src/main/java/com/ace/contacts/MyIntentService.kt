package com.ace.contacts

import android.app.IntentService
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MyIntentService : IntentService("MyIntentService") {

    companion object {
        const val BROADCAST = "MyIntentService.BROADCAST"
        private const val CONTACTS = "CONTACTS"
        const val TAG = "IntentServiceLogs"
    }

    private val broadcast = Intent(BROADCAST)
    private var contacts: ArrayList<String> = arrayListOf()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        contacts = getContacts()
        broadcast.putStringArrayListExtra(CONTACTS, contacts)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast)
    }

    private fun getContacts(): ArrayList<String> {
        val contacts = ArrayList<String>()
        val cr = contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    )).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        if (cursorPhone != null) {
                            if (cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(
                                        cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    contacts.add(
                                        getString(
                                            R.string.contact_format,
                                            name,
                                            phoneNumValue
                                        )
                                    )
                                }
                            }
                        }
                        cursorPhone?.close()
                    }
                }
            }
        }
        cursor?.close()
        return contacts
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}