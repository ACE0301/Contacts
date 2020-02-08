package com.ace.contacts

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class SecondActivity : AppCompatActivity() {

    companion object {
        private const val CONTACTS = "CONTACTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    fun onClickButtonLaunchService(v: View) {
        startService(Intent(this, MyIntentService::class.java))
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(MyIntentService.BROADCAST)
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice)
    }

    private val onNotice = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var contacts = intent.getStringArrayListExtra(CONTACTS)
            intent.putExtra(CONTACTS, contacts)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}