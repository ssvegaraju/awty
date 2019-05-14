package com.dichotomyllc.awty

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Handler
import android.widget.Toast
import android.app.AlarmManager
import android.support.v4.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.os.Looper
import android.util.Log
import java.util.*


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class AWTYService : IntentService("AWTYService") {

    private val TAG = "AWTYService"

    var message: String = ""
    var number: Long = 0
    private var minutes: Int = 0
    private val timer: Timer = Timer()

    private lateinit var mHandler: Handler
    init {
        mHandler = Handler(Looper.getMainLooper())
    }

    override fun onHandleIntent(intent: Intent) {
        if (intent.extras != null && intent.extras!!.containsKey("message")) {
            message = intent.extras!!.getString("message")!!
            number = intent.extras!!.getLong("number")
            minutes = intent.extras!!.getInt("minutes")
        }
        val waitTime = minutes * 1000 + 1 // to prevent 0
        Log.v(TAG, "${waitTime.toLong()} waiting........")
        timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                Looper.prepare()
                Log.v(TAG, "Running task....")
                mHandler.post{
                    Toast.makeText(applicationContext, "$number: $message", Toast.LENGTH_LONG).show()
                }
                Looper.loop()
            }
        }, 0, 10000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}


