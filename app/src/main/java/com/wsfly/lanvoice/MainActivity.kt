package com.wsfly.lanvoice

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this)

        tv.text = "LanVoice\nUDP Multicast Voice"

        tv.textSize = 24f

        setContentView(tv)
    }
}
