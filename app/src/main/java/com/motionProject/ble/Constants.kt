package com.motionProject.ble

import android.Manifest

// used to identify adding bluetooth names
const val REQUEST_ENABLE_BT = 1
// used to request fine location permission
const val REQUEST_ALL_PERMISSION = 2
val PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION
)

//사용자 BLE UUID Service/Rx/Tx
const val DEVICE_NAME = "Test Sensor"
const val DEVICE_ADDRESS = "B4:9F:3B:40:AE:A0"
const val SERVICE_STRING = "0000ffff-0000-1000-8000-00805f9b34fb"
/*
val CHARACTERISTIC_COMMAND_STRING = arrayOf("0000ffa1-0000-1000-8000-00805f9b34fb",
    "0000ffa2-0000-1000-8000-00805f9b34fb", "0000ffa3-0000-1000-8000-00805f9b34fb",
    "0000ffb1-0000-1000-8000-00805f9b34fb", "0000ffb2-0000-1000-8000-00805f9b34fb",
    "0000ffb3-0000-1000-8000-00805f9b34fb", "00002A19-0000-1000-8000-00805f9b34fb")
val CHARACTERISTIC_RESPONSE_STRING = arrayOf("0000ffa1-0000-1000-8000-00805f9b34fb",
    "0000ffa2-0000-1000-8000-00805f9b34fb", "0000ffa3-0000-1000-8000-00805f9b34fb",
    "0000ffb1-0000-1000-8000-00805f9b34fb", "0000ffb2-0000-1000-8000-00805f9b34fb",
    "0000ffb3-0000-1000-8000-00805f9b34fb", "00002A19-0000-1000-8000-00805f9b34fb")
*/


const val CHARACTERISTIC_COMMAND_STRING = "0000fff1-0000-1000-8000-00805f9b34fb"
const val CHARACTERISTIC_RESPONSE_STRING = "0000fff1-0000-1000-8000-00805f9b34fb"
//const val CHARACTERISTIC_COMMAND_STRING = "00002A19-0000-1000-8000-00805f9b34fb"
//const val CHARACTERISTIC_RESPONSE_STRING = "00002A19-0000-1000-8000-00805f9b34fb"

//BluetoothGattDescriptor 고정
const val CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"
