package com.wsfly.lanvoice

import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class MainActivity : AppCompatActivity() {


    private val groupAddress = "239.1.1.1"
    private val port = 5000


    private var running = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30,30,30,30)


        val title = TextView(this)
        title.text = "LanVoice\nUDP Multicast Test"
        title.textSize = 24f


        val send = Button(this)
        send.text = "发送组播"


        val receive = Button(this)
        receive.text = "接收组播"


        val log = TextView(this)
        log.text = "状态:停止"


        layout.addView(title)
        layout.addView(send)
        layout.addView(receive)
        layout.addView(log)


        setContentView(layout)



        // 保持安卓允许组播
        val wifi =
            applicationContext.getSystemService(WIFI_SERVICE)
                    as WifiManager

        val lock =
            wifi.createMulticastLock("LanVoice")

        lock.setReferenceCounted(true)
        lock.acquire()



        send.setOnClickListener {

            Thread {

                try {

                    val socket = DatagramSocket()

                    val address =
                        InetAddress.getByName(groupAddress)


                    val msg =
                        "HELLO LANVOICE"

                    val data =
                        msg.toByteArray()


                    val packet =
                        DatagramPacket(
                            data,
                            data.size,
                            address,
                            port
                        )


                    socket.send(packet)


                    runOnUiThread {
                        log.text =
                            "已发送:\n$msg"
                    }


                    socket.close()


                } catch(e:Exception){

                    runOnUiThread {
                        log.text=e.toString()
                    }

                }


            }.start()


        }




        receive.setOnClickListener {


            Thread {


                try {


                    val socket =
                        DatagramSocket(port)


                    val group =
                        InetAddress.getByName(groupAddress)


                    socket.joinGroup(group)



                    runOnUiThread {
                        log.text =
                            "监听组播中..."
                    }



                    val buffer =
                        ByteArray(1024)



                    while(true){


                        val packet =
                            DatagramPacket(
                                buffer,
                                buffer.size
                            )


                        socket.receive(packet)


                        val msg =
                            String(
                                packet.data,
                                0,
                                packet.length
                            )


                        runOnUiThread {

                            log.text =
                                "收到:\n$msg"

                        }

                    }


                }catch(e:Exception){

                    runOnUiThread {

                        log.text=e.toString()

                    }

                }


            }.start()

        }


    }

}
