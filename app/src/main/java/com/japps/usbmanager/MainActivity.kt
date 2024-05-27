package com.japps.usbmanager

import android.content.Intent
import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.japps.usbmanager.adapter.GenericRecyclerAdapter
import com.japps.usbmanager.utility.USBDeviceManager
import com.japps.usbmanager.viewmodel.USBManagerViewModel


class MainActivity : AppCompatActivity() {

    private var usbDevicesConnected:ArrayList<UsbDevice> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bt_websocket = findViewById<Button>(R.id.websocket)
        bt_websocket.setOnClickListener({
            startActivity(Intent(this@MainActivity,WebSocketActivity::class.java))
        })
        val usbManager = USBDeviceManager(this)
        val connectedDevices = usbManager.getConnectedUSBDevices()
        /*connectedDevices?.forEach { device ->
            usbManager.requestUSBPermission(device, this)
        }*/
        connectedDevices?.forEach {
            usbDevicesConnected = ArrayList()
            usbDevicesConnected.add(it)
        }

        var adapter = object:GenericRecyclerAdapter<UsbDevice>(usbDevicesConnected,R.layout.usb_list_item,true){
            override fun onBind(holder: RecyclerView.ViewHolder, position: Int, item: UsbDevice) {
                var textView = holder.itemView.findViewById<TextView>(R.id.tv_item)
                textView.setText(item.productName + if(usbManager.hasPermission(item)) "Has Permission" else "Not Granted ")
            }

            override fun onRowClick(position: Int, item: UsbDevice) {
                usbManager.requestUSBPermission(item, this@MainActivity)
            }

        }

        var rv_usbdevices = findViewById<RecyclerView>(R.id.rv_usbdevices)
        rv_usbdevices.layoutManager = LinearLayoutManager(this@MainActivity)
        rv_usbdevices.adapter = adapter
        adapter.notifyDataSetChanged()




        Application.getInstance().viewModel.attachedDevice.observe(this, { device ->
            // Handle attached device
            Log.d("USBManagerViewModel", "Device attached: ${device.deviceName}")
            usbDevicesConnected.add(device)
            adapter.updateData(usbDevicesConnected)
            adapter.notifyDataSetChanged()
        })

        Application.getInstance().viewModel.updatePermission.observe(this, { device ->
            // Handle attached device
            Log.d("USBManagerViewModel", "Update Permission: ${device.deviceName}")
            adapter.updateData(usbDevicesConnected)
            adapter.notifyDataSetChanged()
        })

        Application.getInstance().viewModel.detachedDevice.observe(this, { device ->
            var foundPosition = -1
            for(k in 0..usbDevicesConnected.size-1){
                if(usbDevicesConnected.get(k).deviceId == device.deviceId){
                    foundPosition = k
                    break
                }
            }
            if(foundPosition>-1){
                usbDevicesConnected.removeAt(foundPosition)
                adapter.updateData(usbDevicesConnected)
                adapter.notifyDataSetChanged()
            }
            // Handle detached device
            Log.d("USBManagerViewModel", "Device detached: ${device.deviceName}")
        })

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}