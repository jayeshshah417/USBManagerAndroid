package com.japps.usbmanager.viewmodel

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.japps.usbmanager.Application

class USBManagerViewModel() : ViewModel() {

    private val _attachedDevices = MutableLiveData<UsbDevice>()
    val attachedDevice: LiveData<UsbDevice> = _attachedDevices

    private val _updatePermission = MutableLiveData<UsbDevice>()
    val updatePermission: LiveData<UsbDevice> = _updatePermission

    private val _detachedDevices = MutableLiveData<UsbDevice>()
    val detachedDevice: LiveData<UsbDevice> = _detachedDevices



    fun deviceDetached(device: UsbDevice) {
        // Notify when a device is detached
        _detachedDevices.postValue(device)
    }

    fun deviceAttached(device: UsbDevice) {
        // Notify when a device is detached
        _attachedDevices.postValue(device)
    }

    fun updatePermission(device: UsbDevice?) {
        _updatePermission.postValue(device)
    }
}