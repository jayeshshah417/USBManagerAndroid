package com.japps.usbmanager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener
import com.japps.usbmanager.utility.WebsocketConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketActivity : AppCompatActivity() {

    private lateinit var switch_ip_url:SwitchCompat
    private lateinit var et_msg:EditText
    private lateinit var et_logs:EditText
    private lateinit var et_ip_url:EditText
    private lateinit var et_port:EditText
    private lateinit var bt_connect:Button
    private lateinit var bt_disconnect:Button
    private lateinit var bt_send:Button
    private lateinit var iv_connection_status:ImageView
    private lateinit var webSocketListener:WebSocketListener
    private lateinit var cl3:ConstraintLayout
    private var webSocket:WebSocket? = null
    private var isConnected:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_socket)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initWebSocketListener()
        defineWidgets()
    }

    private fun initWebSocketListener() {
        webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(WebsocketConnection.TAG, String.format("onOpen response %s",response.toString()))
                updateLogs(String.format("onOpen response %s",response.toString()))
                webSocketConnected(true)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(WebsocketConnection.TAG, String.format("OnClosed code %s \t reason %s",code.toString(),reason))
                updateLogs(String.format("OnClosed code %s \t reason %s",code.toString(),reason))
                webSocketConnected(false)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(WebsocketConnection.TAG, String.format("Message Received: %s \n", text))
                updateLogs(String.format("Message Received: %s \n", text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(WebsocketConnection.TAG, String.format("onClosing code %s \t reason %s",code.toString(),reason))
                updateLogs(String.format("onClosing code %s \t reason %s",code.toString(),reason))
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(WebsocketConnection.TAG, String.format("onFailure response %s",response.toString()))
                updateLogs(String.format("onFailure response %s",response.toString()))
                webSocketConnected(false)
            }
        }
    }

    private fun updateLogs(logs: String) {
        GlobalScope.launch(Dispatchers.Main){
            et_logs.setText(et_logs.text.toString()+"\n"+logs)
        }
    }

    private fun webSocketConnected(connected: Boolean) {
        if(connected){
            isConnected = true
            iv_connection_status.setImageDrawable(resources.getDrawable(R.drawable.baseline_check_circle_24,resources.newTheme()))
            bt_disconnect.visibility = View.VISIBLE
            bt_connect.visibility = View.GONE
            cl3.visibility = View.VISIBLE
        }else{
            isConnected = false
            iv_connection_status.setImageDrawable(resources.getDrawable(R.drawable.baseline_cancel_24,resources.newTheme()))
            bt_connect.visibility = View.VISIBLE
            bt_disconnect.visibility = View.GONE
            cl3.visibility = View.GONE
        }
    }

    private fun defineWidgets() {
        switch_ip_url = findViewById(R.id.switch_ip_url)
        et_msg = findViewById(R.id.et_msg)
        et_ip_url = findViewById(R.id.et_ip)
        et_port = findViewById(R.id.et_port)
        et_logs = findViewById(R.id.et_logs)
        bt_send = findViewById(R.id.bt_send)
        bt_connect = findViewById(R.id.bt_connect)
        bt_disconnect = findViewById(R.id.bt_disconnect)
        iv_connection_status = findViewById(R.id.iv_connection_status)
        cl3 = findViewById(R.id.cl3)
        bt_connect.setOnClickListener({
            if(!switch_ip_url.isChecked) {
                updateLogs(String.format("Connecting IP %s PORT %s ",et_ip_url.text.toString(),et_port.text.toString()))
                webSocket =  WebsocketConnection.connectWebSocket(
                    ip = et_ip_url.text.toString(),
                    port = et_port.text.toString(),
                    webSocketListener = webSocketListener
                )
            }else{
                updateLogs(String.format("Connecting URL %s ",et_ip_url.text.toString()))
                webSocket =  WebsocketConnection.connectWebSocket(
                    url = et_ip_url.text.toString(),
                    webSocketListener = webSocketListener
                )
            }
        })

        bt_disconnect.setOnClickListener({
            if(webSocket!=null) {
                WebsocketConnection.closeWebsocket(webSocket!!)
            }
        })

        bt_send.setOnClickListener({
            if(webSocket!=null) {
                WebsocketConnection.sendMessage(webSocket!!,et_msg.text.toString())
            }
        })

        switch_ip_url.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(isConnected){
                   switch_ip_url.isChecked = !p1
                    Toast.makeText(this@WebSocketActivity,"Please Disconnect Active Connection ",Toast.LENGTH_LONG).show()
                }else{
                    if(p1){
                        et_ip_url.setHint("URL")
                        et_port.visibility = View.GONE
                    }else{
                        et_ip_url.setHint("IP")
                        et_port.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}