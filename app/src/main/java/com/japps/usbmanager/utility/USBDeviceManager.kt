package com.japps.usbmanager.utility

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

class USBDeviceManager(private val context: Context) {

    companion object {
        const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    }

    private val usbManager: UsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    fun getConnectedUSBDevices(): List<UsbDevice>? {
        return usbManager.deviceList.values.toList()
    }

    fun hasPermission(usbDevice:UsbDevice): Boolean{
        return usbManager.hasPermission(usbDevice)
    }

    fun requestUSBPermission(device: UsbDevice, activity: Activity) {
        val permissionIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )
        usbManager.requestPermission(device, permissionIntent)
    }
}