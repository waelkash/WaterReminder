package com.waelkash.waterreminder

import android.os.CountDownTimer

class CountdownUtil(
    private val onTick: (Long) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null

    fun start(millis: Long = 30 * 60 * 1000) {   // 30 min default
        cancel()
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) = onTick(millisUntilFinished)
            override fun onFinish() = onFinish()
        }.start()
    }

    fun cancel() {
        timer?.cancel()
        timer = null
    }
}
