package com.dichotomyllc.awty

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var serviceRunning = false

    var message = ""
    var number: Long = 0
    var minutes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var serviceIntent: Intent = intent
        findViewById<EditText>(R.id.etMessage).addTextChangedListener(TextChangedHandler{
            message = it
        })

        findViewById<EditText>(R.id.etPhone).addTextChangedListener(TextChangedHandler{
            number = it.toLong()
        })

        findViewById<EditText>(R.id.etMin).addTextChangedListener(TextChangedHandler{
            minutes = it.toInt()
        })

        // Start and Stop service
        val btn = findViewById<Button>(R.id.btnService)
        fun CheckLegality() : Boolean {
            if (minutes < 1) {
                Toast.makeText(applicationContext, "Invalid minutes supplied", Toast.LENGTH_SHORT).show()
                return false
            }
            if (number < 1000000) {
                Toast.makeText(applicationContext, "Invalid phone number", Toast.LENGTH_SHORT).show()
                return false
            }
            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "Empty message!", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }
        btn.setOnClickListener {
            val check: Boolean = CheckLegality()
            if (check) {
                if (!serviceRunning) {
                    // start service
                    btn.text = getString(R.string.btnStopText)
                    serviceIntent = Intent(this, AWTYService::class.java).also {
                        it.putExtra("message", message)
                        it.putExtra("number", number)
                        it.putExtra("minutes", minutes)
                        startService(it)
                    }
                } else {
                    // stop service
                    btn.text = getString(R.string.btnStartText)
                    intent.also { stopService(serviceIntent) }
                }
                serviceRunning = !serviceRunning
            }
        }
    }

    class TextChangedHandler(private val action: (s: String) -> Unit): TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        override fun afterTextChanged(s: Editable?) {
            action(s.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "Pausing...")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "Resuming...")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "Stopping...")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(TAG, "Restarting...")
    }
}
