package com.japps.usbmanager

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.japps.usbmanager.viewmodel.USBManagerViewModel

class Application : Application() {

    lateinit var viewModel:USBManagerViewModel
    companion object {
        private lateinit var instance: com.japps.usbmanager.Application

        fun getInstance(): com.japps.usbmanager.Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(USBManagerViewModel::class.java)
        // Any initialization code you want to run when the application starts
    }
}