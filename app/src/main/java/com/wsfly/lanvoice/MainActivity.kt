package com.wsfly.lanvoice

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        val text = TextView(this)

        text.text =
            "LanVoice启动成功\nAndroid 13 OK"


        text.textSize = 30f


        setContentView(text)

    }

}
