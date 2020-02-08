package com.ace.contacts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_first.*


class FirstActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val MY_REQUEST_CODE = 1
        private const val CONTACTS = "CONTACTS"
        private var contacts: ArrayList<String> = arrayListOf()
    }

    private val mAdapter = ContactsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = mAdapter
        mAdapter.data = contacts
    }

    fun onClickButtonOpenSecondActivity(v: View) {
        showContacts()
    }

    private fun showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            startSecondActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startSecondActivity()
            } else {
                Toast.makeText(this, R.string.permission_toast, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        this.startActivityForResult(intent, MY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == MY_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    contacts = data.getStringArrayListExtra(CONTACTS)
                }
                if (contacts.isNotEmpty()) {
                    mAdapter.data = contacts
                } else {
                    rvList.visibility = View.GONE
                    tvEmptyContactListInfo.visibility = View.VISIBLE
                    ivEmEmptyContactList.visibility = View.VISIBLE
                }
            }
        }
    }
}
