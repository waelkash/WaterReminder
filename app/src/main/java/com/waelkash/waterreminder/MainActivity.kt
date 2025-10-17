package com.waelkash.waterreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waelkash.waterreminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = SharedPrefsUtil(this)

        binding.btnStart.setOnClickListener {
            prefs.running = true
            prefs.paused = false
            WaterReminderWorker.schedule(this)
            updateUI(prefs)
        }

        binding.btnCupDone.setOnClickListener {
            if (prefs.running && !prefs.paused) {
                prefs.cupCount += 1
                if (prefs.cupCount >= 3) {
                    WaterReminderWorker.cancel(this)
                    prefs.reset()
                } else {
                    WaterReminderWorker.schedule(this)
                }
                updateUI(prefs)
            }
        }

        binding.btnPause.setOnClickListener {
            if (prefs.running) {
                prefs.paused = !prefs.paused
                if (prefs.paused) WaterReminderWorker.cancel(this)
                else WaterReminderWorker.schedule(this)
                updateUI(prefs)
            }
        }

        updateUI(prefs)
    }

    private fun updateUI(p: SharedPrefsUtil) {
        binding.tvCount.text = getString(R.string.cup_count, p.cupCount, 3)
        binding.btnPause.text = if (p.paused) getString(R.string.resume) else getString(R.string.pause)
    }
}
