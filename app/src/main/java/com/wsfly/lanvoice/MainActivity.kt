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

    private var socket: DatagramSocket? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        Thread.setDefaultUncaughtExceptionHandler { _, e ->

            runOnUiThread {

                Toast.makeText(
                    this,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()

            }

        }



        val layout = LinearLayout(this)

        layout.orientation =
            LinearLayout.VERTICAL

        layout.setPadding(
            30,
            30,
            30,
            30
        )



        val title = TextView(this)

        title.text =
            "LanVoice\nUDP Multicast"

        title.textSize =
            24f



        val send =
            Button(this)

        send.text =
            "发送组播"



        val receive =
            Button(this)

        receive.text =
            "接收组播"



        val log =
            TextView(this)

        log.text =
            "状态:停止"



        layout.addView(title)

        layout.addView(send)

        layout.addView(receive)

        layout.addView(log)



        setContentView(layout)



        // 防止组播锁导致闪退

        try {

            val wifi =
                applicationContext
                    .getSystemService(WIFI_SERVICE)
                        as WifiManager


            val lock =
                wifi.createMulticastLock(
                    "LanVoice"
                )


            lock.setReferenceCounted(true)

            lock.acquire()


        } catch(e:Exception){

            log.text =
                "组播锁失败:\n$e"

        }





        //发送

        send.setOnClickListener {


            Thread {


                try {


                    val sendSocket =
                        DatagramSocket()


                    val address =
                        InetAddress
                            .getByName(
                                groupAddress
                            )


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



                    sendSocket.send(packet)



                    runOnUiThread {

                        log.text =
                            "发送成功:\n$msg"

                    }



                    sendSocket.close()



                }catch(e:Exception){


                    runOnUiThread {

                        log.text =
                            "发送错误:\n$e"

                    }


                }


            }.start()


        }





        //接收

        receive.setOnClickListener {


            Thread {


                try {



                    socket =
                        DatagramSocket(port)



                    val group =
                        InetAddress
                            .getByName(
                                groupAddress
                            )



                    socket!!.joinGroup(group)



                    runOnUiThread {

                        log.text =
                            "监听中..."

                    }





                    val buffer =
                        ByteArray(2048)



                    while(true){



                        val packet =
                            DatagramPacket(
                                buffer,
                                buffer.size
                            )



                        socket!!.receive(packet)



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


                        log.text =
                            "接收错误:\n$e"


                    }


                }



            }.start()


        }



    }



    override fun onDestroy(){

        super.onDestroy()

        try{

            socket?.close()

        }catch(_:Exception){}


    }


}
