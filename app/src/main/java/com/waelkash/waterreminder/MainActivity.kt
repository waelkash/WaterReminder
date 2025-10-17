override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val prefs = SharedPrefsUtil(this)
    val countdown = CountdownUtil(
        onTick = { millis ->
            binding.tvCountdown.text = formatMillisMinSec(millis)
        },
        onFinish = {
            binding.tvCountdown.visibility = View.GONE
        }
    )

    fun updateUI() {
        binding.tvCount.text = getString(R.string.cup_count, prefs.cupCount, 3)
        binding.btnPause.text = if (prefs.paused) getString(R.string.resume) else getString(R.string.pause)
    }

    binding.btnStart.setOnClickListener {
        prefs.running = true
        prefs.paused = false
        WaterReminderWorker.schedule(this)
        countdown.start()                       // ⏱️ start 30-min clock
        binding.tvCountdown.visibility = View.VISIBLE
        updateUI()
    }

    binding.btnCupDone.setOnClickListener {
        if (prefs.running && !prefs.paused) {
            prefs.cupCount += 1
            countdown.cancel()                  // ⏱️ stop clock
            binding.tvCountdown.visibility = View.GONE
            if (prefs.cupCount >= 3) {
                WaterReminderWorker.cancel(this)
                prefs.reset()
            } else {
                WaterReminderWorker.schedule(this)
                countdown.start()               // ⏱️ restart 30-min
                binding.tvCountdown.visibility = View.VISIBLE
            }
            updateUI()
        }
    }

    binding.btnPause.setOnClickListener {
        if (prefs.running) {
            prefs.paused = !prefs.paused
            if (prefs.paused) {
                WaterReminderWorker.cancel(this)
                countdown.cancel()              // ⏱️ pause clock
            } else {
                WaterReminderWorker.schedule(this)
                countdown.start()               // ⏱️ resume clock
            }
            updateUI()
        }
    }

    updateUI()
    if (prefs.running && !prefs.paused) {
        val elapsed = System.currentTimeMillis() - prefs.startTime
        val remaining = 30 * 60 * 1000 - elapsed
        if (remaining > 0) {
            countdown.start(remaining)
            binding.tvCountdown.visibility = View.VISIBLE
        }
    }
}
