package com.japps.usbmanager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.widget.Toast
import com.japps.usbmanager.Application
import com.japps.usbmanager.utility.USBDeviceManager

class USBPermissionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            USBDeviceManager.ACTION_USB_PERMISSION ->{
                run {
                    synchronized(this) {
                        val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        val granted: Boolean =
                            intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                        if (granted) {
                            Toast.makeText(context, "Permission Granted for "+device?.productName, Toast.LENGTH_LONG).show()
                            Application.getInstance().viewModel.updatePermission(device)
                        } else {
                            Application.getInstance().viewModel.updatePermission(device)
                            Toast.makeText(context, "Permission Denied for "+device?.productName, Toast.LENGTH_LONG).show()
                            // Permission denied, handle accordingly
                        }
                    }
                }
            }
            UsbManager.ACTION_USB_DEVICE_ATTACHED->{
                try {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    // Device attached, do something with the device
                    device?.let {
                        // Handle the connected USB device, for example, log its details
                        val deviceName = it.deviceName
                        val vendorId = it.vendorId
                        val productId = it.productId
                        Toast.makeText(
                            context,
                            String.format("Device Atttached %s Vendor %s Product ID %s ", deviceName,vendorId,productId),
                            Toast.LENGTH_LONG
                        ).show()
                        Application.getInstance().viewModel.deviceAttached(device)
                        // Process the device information as needed
                    }
                }catch (e:Exception){
                    Toast.makeText(
                        context,
                        String.format("Error  %s ", e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED->{
                try {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    // Device attached, do something with the device
                    device?.let {
                        // Handle the connected USB device, for example, log its details
                        val deviceName = it.deviceName
                        val vendorId = it.vendorId
                        val productId = it.productId
                        Toast.makeText(
                            context,
                            String.format("Device Detached %s Vendor %s Product ID %s ", deviceName,vendorId,productId),
                            Toast.LENGTH_LONG
                        ).show()
                        Application.getInstance().viewModel.deviceDetached(device)
                        // Process the device information as needed
                    }
                }catch (e:Exception){
                    Toast.makeText(
                        context,
                        String.format("Error  %s ", e.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}
