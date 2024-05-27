package com.japps.usbmanager.utility

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class WebsocketConnection {

    companion object {
        final val TAG: String = "WebsocketConnection"

        fun connectWebSocket(ip: String, port: String, webSocketListener: WebSocketListener): WebSocket {
            Log.d(TAG, String.format("IP %s \t PORT %s", ip, port))
            return connectWebSocket("ws://"+ip+":"+port,webSocketListener)
        }
        fun connectWebSocket(ip: String, port: String): WebSocket {
            Log.d(TAG, String.format("IP %s \t PORT %s", ip, port))
            return connectWebSocket("ws://"+ip+":"+port, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.d(TAG, String.format("onOpen response %s",response.toString()))
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.d(TAG, String.format("OnClosed code %s \t reason %s",code.toString(),reason))
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.d(TAG, String.format("Message Received: %s \n", text))
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    Log.d(TAG, String.format("onClosing code %s \t reason %s",code.toString(),reason))
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    Log.d(TAG, String.format("onFailure response %s",response.toString()))
                }
            })
        }
        fun connectWebSocket(url:String): WebSocket {
            return connectWebSocket(url, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.d(TAG, String.format("onOpen response %s",response.toString()))
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.d(TAG, String.format("OnClosed code %s \t reason %s",code.toString(),reason))
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.d(TAG, String.format("Message Received: %s \n", text))
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    Log.d(TAG, String.format("onClosing code %s \t reason %s",code.toString(),reason))
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    Log.d(TAG, String.format("onFailure response %s",response.toString()))
                }
            })
        }

        fun connectWebSocket(url:String,webSocketListener: WebSocketListener): WebSocket {
            val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
            val request = Request.Builder().url(url).build()
            val webSocket = client.newWebSocket(request, webSocketListener)
            return webSocket
        }

        fun closeWebsocket(websocket:WebSocket){
            websocket.close(1000,"Closing web Socket")
        }
        fun sendMessage(websocket:WebSocket,msg:String):Boolean{
            return websocket.send(msg)
        }

    }
}