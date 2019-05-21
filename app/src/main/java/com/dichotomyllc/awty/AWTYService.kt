package com.dichotomyllc.awty

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import android.os.Looper
import android.util.Log
import java.util.*
import android.support.v4.os.HandlerCompat.postDelayed
import android.telephony.SmsManager


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class AWTYService : IntentService("AWTYService") {

    private val TAG = "AWTYService"
    private var running = true

    var message: String = ""
    var number: Long = 0
    private var minutes: Int = 0
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onHandleIntent(intent: Intent) {
        if (intent.extras != null && intent.extras!!.containsKey("message")) {
            message = intent.extras!!.getString("message")!!
            number = intent.extras!!.getLong("number")
            minutes = intent.extras!!.getInt("minutes")
        }
        val waitTime: Long = (minutes * 60 * 1000 + 1).toLong() // to prevent 0 we add 1
        val runnable = object : Runnable {
            override fun run() {
                mHandler.post{
                    //Toast.makeText(applicationContext, "$number: $message", Toast.LENGTH_LONG).show()\
                    try {
                        val smgr: SmsManager = SmsManager.getDefault()
                        smgr.sendTextMessage(number.toString(), null, message, null, null)
                        MainActivity.instance.sendMMS("gaurplains", "audio/mpeg", number.toString(), message)
                        MainActivity.instance.sendMMS("stupidvoters", "video/mp4", number.toString(), message)
                    } catch(e: Exception) {
                        Toast.makeText(applicationContext, "Something went wrong, failed to send text.", Toast.LENGTH_LONG).show()
                        Log.e(TAG, e.toString())
                        e.printStackTrace()
                    }
                }
                if (running)
                    mHandler.postDelayed(this, waitTime)
            }
        }
        mHandler.postDelayed(runnable, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        running = false
    }
}


